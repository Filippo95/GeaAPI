package API.service;


import API.model.Temperatura;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.*;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.ParsedTopHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;

import org.apache.http.HttpHost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class TemperatureService {
    @Autowired
    private RestHighLevelClient client;


    public List<Temperatura> search  (String mac) throws Exception
    {
        List<Temperatura> l=new ArrayList<Temperatura>();
        Pattern pattern=Pattern.compile("^([0-9A-F]{2}[:-]){5}([0-9A-F]{2})$");
        Matcher matcher=pattern.matcher(mac);
        if(!matcher.matches())
        {
            throw new IllegalArgumentException("il mac deve essere un mac valido");
        }
            SearchResponse response = client.search(new SearchRequest("temperature").source(
                    new SearchSourceBuilder().query(
                            QueryBuilders.matchQuery("id_home", mac)
                    ).size(10000)
            ), RequestOptions.DEFAULT);
            System.out.println(response.toString());
            SearchHits hits = response.getHits();
            SearchHit[] searchHits = hits.getHits();
            for (SearchHit hit : searchHits) {
                Map<String, Object> entry = hit.getSourceAsMap();
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
                Temperatura temperature=new Temperatura(
                        hit.getId(),
                        entry.get("id_home").toString(),
                        entry.get("id_sensor").toString(),
                        new Timestamp(df.parse(entry.get("@timestamp").toString()).getTime()),
                        new Double(entry.get("value").toString())
                );
                l.add(temperature);
            }
            return l;
    }

    public List<Temperatura> getLastTemperatures(String mac) throws Exception
    {
        Pattern pattern=Pattern.compile("^([0-9A-F]{2}[:-]){5}([0-9A-F]{2})$");
        Matcher matcher=pattern.matcher(mac);
        if(!matcher.matches())
        {
            throw new IllegalArgumentException("il mac deve essere un mac valido");
        }
        List<Temperatura> l=new ArrayList<Temperatura>();
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(HttpHost.create("http://192.168.1.7:9200")));

            SearchResponse response = client.search(new SearchRequest("temperature")
                    .source( new SearchSourceBuilder().query(
                            QueryBuilders.matchQuery("id_home", mac)
                            )
                                    .aggregation(AggregationBuilders
                                            .terms("getLastTemperatures")
                                            .field("id_sensor.keyword")
                                            .subAggregation(AggregationBuilders
                                                    .topHits("ultimeTemperature")
                                                    .sort("@timestamp", SortOrder.DESC).size(1))
                                    )
                                    .size(0)
                    ), RequestOptions.DEFAULT);

            Aggregations aggs = response.getAggregations();
            Terms getLastTemperatures = aggs.get("getLastTemperatures");
            for (Terms.Bucket bucket : getLastTemperatures.getBuckets())
            {
                ParsedTopHits a=bucket.getAggregations().get("ultimeTemperature");
                for (SearchHit hit :a.getHits())
                {
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
                    Temperatura temperature=new Temperatura(
                            hit.getId(),
                            hit.getSourceAsMap().get("id_home").toString(),
                            hit.getSourceAsMap().get("id_sensor").toString(),
                            new Timestamp(df.parse(hit.getSourceAsMap().get("@timestamp").toString()).getTime()),
                            new Double(hit.getSourceAsMap().get("value").toString())
                    );

                    l.add(temperature);
                }
            }
            return l;

    }
    public Double getAverage(String home_mac, String sensor_mac, String lte, String gte)throws Exception{
        List<Temperatura> l=new ArrayList<Temperatura>();
        Pattern pattern=Pattern.compile("^([0-9A-F]{2}[:-]){5}([0-9A-F]{2})$");
        Pattern timestmapPattern=Pattern.compile("^(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.\\d{3})Z$");

        if(!pattern.matcher(home_mac).matches() ||
                !pattern.matcher(sensor_mac).matches() ||
                !timestmapPattern.matcher(lte).matches() ||
                !timestmapPattern.matcher(gte).matches())
        {
            throw new IllegalArgumentException("il mac o il timestamp devono essere validi");

        }

        BoolQueryBuilder boolQuery = new BoolQueryBuilder();
        boolQuery.filter(QueryBuilders.matchPhraseQuery("id_home",home_mac));
        boolQuery.filter(QueryBuilders.matchPhraseQuery("id_sensor",sensor_mac));
        boolQuery.filter(QueryBuilders.rangeQuery("@timestamp").gte(gte));
        boolQuery.filter(QueryBuilders.rangeQuery("@timestamp").lte(lte));

        SearchResponse response = client.search(new SearchRequest("temperature").source(
                new SearchSourceBuilder().query(
                        boolQuery
                ).size(10000)
        ), RequestOptions.DEFAULT);
        System.out.println(response.toString());
        SearchHits hits = response.getHits();
        SearchHit[] searchHits = hits.getHits();
        double sum=0;
        for (SearchHit hit : searchHits) {
            Map<String, Object> entry = hit.getSourceAsMap();
            sum+=new Double(entry.get("value").toString());
        }
        int count=searchHits.length;
        return sum/count;
    }
}

