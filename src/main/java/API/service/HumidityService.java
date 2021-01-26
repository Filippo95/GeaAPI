package API.service;

import API.model.Humidity;
import API.model.Temperatura;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
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
public class HumidityService {
    @Autowired
    private RestHighLevelClient client;

    public List<Humidity> search(String home_mac) throws Exception{
        List<Humidity> l=new ArrayList<Humidity>();
        Pattern pattern=Pattern.compile("^([0-9A-F]{2}[:-]){5}([0-9A-F]{2})$");
        Matcher matcher=pattern.matcher(home_mac);
        if(!matcher.matches())
        {
            throw new IllegalArgumentException("il mac deve essere un mac valido");
        }
        SearchResponse response = client.search(new SearchRequest("humidity").source(
                new SearchSourceBuilder().query(
                        QueryBuilders.matchQuery("id_home", home_mac)
                ).size(10000)
        ), RequestOptions.DEFAULT);
        System.out.println(response.toString());
        SearchHits hits = response.getHits();
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit hit : searchHits) {
            Map<String, Object> entry = hit.getSourceAsMap();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
            Humidity humidity=new Humidity(
                    hit.getId(),
                    entry.get("id_home").toString(),
                    entry.get("id_sensor").toString(),
                    new Timestamp(df.parse(entry.get("@timestamp").toString()).getTime()),
                    new Double(entry.get("value").toString())
            );
            l.add(humidity);
        }
        return l;
    }

    public List<Humidity> getLastHumidity(String home_mac) throws Exception{
        Pattern pattern=Pattern.compile("^([0-9A-F]{2}[:-]){5}([0-9A-F]{2})$");
        Matcher matcher=pattern.matcher(home_mac);
        if(!matcher.matches())
        {

            throw new IllegalArgumentException("il mac deve essere un mac valido");
        }

        List<Humidity> l=new ArrayList<Humidity>();
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(HttpHost.create("http://192.168.1.7:9200")));

        SearchResponse response = client.search(new SearchRequest("humidity")
                .source( new SearchSourceBuilder().query(
                        QueryBuilders.matchQuery("id_home", home_mac)
                        )
                                .aggregation(AggregationBuilders
                                        .terms("getLasthumidity")
                                        .field("id_sensor.keyword")
                                        .subAggregation(AggregationBuilders
                                                .topHits("ultimehumidity")
                                                .sort("@timestamp", SortOrder.DESC).size(1))
                                )
                                .size(0)
                ), RequestOptions.DEFAULT);

        System.out.println(response.toString());
        Aggregations aggs = response.getAggregations();
        Terms getLastHumidity = aggs.get("getLasthumidity");
        for (Terms.Bucket bucket : getLastHumidity.getBuckets())
        {
            ParsedTopHits a=bucket.getAggregations().get("ultimehumidity");
            for (SearchHit hit :a.getHits())
            {


                DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
                Humidity humidity=new Humidity(
                        hit.getId(),
                        hit.getSourceAsMap().get("id_home").toString(),
                        hit.getSourceAsMap().get("id_sensor").toString(),
                        new Timestamp(df.parse(hit.getSourceAsMap().get("@timestamp").toString()).getTime()),
                        new Double(hit.getSourceAsMap().get("value").toString())
                );

                l.add(humidity);
            }
        }

        return l;
    }

    public Double getAverage(String home_mac, String sensor_mac, String lte, String gte)throws Exception {
        List<Humidity> l=new ArrayList<Humidity>();
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

        SearchResponse response = client.search(new SearchRequest("humidity").source(
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
