package API.service;

import API.model.Switch;
import API.model.SwitchGraphData;
import API.model.Temperatura;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.cli.UserException;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.inject.internal.ErrorsException;
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
public class SwitchService {
    @Autowired
    private RestHighLevelClient client;

    //metodo che dato:
    // - un periodo di tempo, gte e lte,
    // - un home mac
    // - un sensor_mac
    // restituisce la lista dei valori 0 o 1 con relativo timestmap

    public List<SwitchGraphData> getGraphData(String home_mac, String sensor_mac, String lte, String gte)throws Exception
    {
        List<SwitchGraphData> l=new ArrayList<SwitchGraphData>();
        Pattern pattern=Pattern.compile("^([0-9A-F]{2}[:-]){5}([0-9A-F]{2})$");
        Pattern timestmapPattern=Pattern.compile("^(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.\\d{3})Z$");

        if(!pattern.matcher(home_mac).matches() ||
                !pattern.matcher(sensor_mac).matches() )
        {
            throw new IllegalArgumentException("Metodo getGraphData il mac  devono essere valido");
        }
        if(
        !timestmapPattern.matcher(lte).matches() ||
                !timestmapPattern.matcher(gte).matches())
        {
            System.out.println(gte.toString());
            System.out.println(lte.toString());
            throw new IllegalArgumentException("Metodo getGraphData il timestamp  devono essere validi");
        }

        BoolQueryBuilder boolQuery = new BoolQueryBuilder();
        boolQuery.filter(QueryBuilders.matchPhraseQuery("id_home",home_mac));
        boolQuery.filter(QueryBuilders.matchPhraseQuery("id_sensor",sensor_mac));
        boolQuery.filter(QueryBuilders.rangeQuery("@timestamp").gte(gte));
        boolQuery.filter(QueryBuilders.rangeQuery("@timestamp").lte(lte));

        SearchResponse response = client.search(new SearchRequest("switch").source(
                new SearchSourceBuilder().query(
                        boolQuery
                ).size(10000)
        ), RequestOptions.DEFAULT);
        System.out.println(new SearchRequest("switch").source(
                new SearchSourceBuilder().query(
                        boolQuery
                ).size(10000)
        ).source().toString());
        System.out.println(response.toString());
        SearchHits hits = response.getHits();
        SearchHit[] searchHits = hits.getHits();
        List<SwitchGraphData> list=new ArrayList<>();
        for (SearchHit hit : searchHits) {
            SwitchGraphData d=new SwitchGraphData();
            Map<String, Object> entry = hit.getSourceAsMap();
            d.value=new Integer(entry.get("status").toString());
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
            d.timestamp=new Timestamp(df.parse(entry.get("@timestamp").toString()).getTime());
            list.add(d);
        }
        return list;
    }

    /*metodo che dato :
        - un periodo di tempo, gte e lte,
        - un home mac
        - un sensor_mac
        retituisce il totale delle ore che è stato accesa la luce

     */
    public Double getTotalHours(String home_mac, String sensor_mac, String lte, String gte)throws Exception
    {
        List<SwitchGraphData> l=new ArrayList<SwitchGraphData>();

        l=this.getGraphData(home_mac,sensor_mac,lte,gte);

        if(l.size()<=0)
        {
            return 0.0;
        }
        //la ordino
        l.sort((e1,e2)->e1.timestamp.compareTo(e2.timestamp));
        // se il primo è 0 allora lo elimino
        if(l.get(0).value==0)
        {
            l.remove(0);
        }
        // se l'ultimo è 1 allora lo elimino
        if(l.get(l.size()-1).value==1)
        {
            l.remove(l.size()-1);
        }


        // controllo che la lista sia 0101010101010
        int i=0;
        while(i+1<l.size())
        {
            if(l.get(i).value==0  && l.get(i+1).value==0)
            {
                l.remove(i+1);
            }
            else if(l.get(i).value==1 && l.get(i+1).value==1)
            {
                l.remove(i+1);
            }
            else
            {
                i++;
            }
        }
        System.out.println(l.toString());
        System.out.println("Metodo getTotalHours: la lista formattata correttamente è: "+l.toString());
        // ora la lista dovrebbe contenere 01010101010
        //la ordino

        System.out.println("Lista ordinata: "+l);
        i=0;
        Double toth=0.0;
        while(i<l.size())
        {
            if(l.get(i).value==1 && i+1!=l.size() && l.get(i+1).value==0)
            {
                System.out.println(l.get(i+1).timestamp+" - "+l.get(i).timestamp);
                System.out.println("ret: "+SecondsDiff(l.get(i+1).timestamp,l.get(i).timestamp));
                toth+=SecondsDiff(l.get(i+1).timestamp,l.get(i).timestamp);
                i++;
                i++;
            }else
            {
                i++;
            }
        }
        System.out.println(toth);
        //ritorno in secondi
        return toth/1000;

    }


    public static long SecondsDiff(Timestamp magg, Timestamp min)
    {
        long milliseconds1 = min.getTime();
        long milliseconds2 = magg.getTime();

        long diff = milliseconds2-milliseconds1;
        if(milliseconds2>milliseconds1){
            System.out.println("milli2 è maggiore di milli1");
        }
        else
        {
            System.out.println("!!!!!!!milli2 è minore di milli1");
            System.out.println("magg:"+magg+"->"+magg.getTime()+" min: "+min+"->"+min.getTime());
        }
        System.out.println("end:"+milliseconds2+" start: "+milliseconds1);
        System.out.println("diff: "+diff);
        long diffseconds = diff;

        return diffseconds;
    }
    /*metodo che dato :
        - una lista di date (giornate),
        - un home mac
        - un sensor_mac
        retituisce la media delle ore che è stato accesa la luce

     */
    public Double getAverageONHours(String home_mac, String sensor_mac, List<Timestamp> date) throws Exception {
        Double sum=0.0;
        int i=0;
        while(i<date.size())
        {
            Timestamp start=new Timestamp(date.get(i).getTime());

            Timestamp stop=new Timestamp(date.get(i).getTime());


            stop.setTime(date.get(i).getTime()+1000*60*60*23+(60*60*1000));
            String sstart=start.toString();
            String sstop=stop.toString();
            sstart=sstart+"00Z";
            sstop=sstop+"00Z";
            sstop=sstop.replaceAll("\\s","T");
            sstart=sstart.replaceAll("\\s","T");


            System.out.println("Metodo geetAverageONHours nel seervice, chiamo get total hours per "+sstart+" "+sstop);
            Double s=getTotalHours(home_mac, sensor_mac,sstop,sstart);
            sum+=s*60*60;
            System.out.println("Da "+sstart+" a: "+sstop+" è stato acceso:"+s);
        i++;
        }

        return sum/date.size();
    }
    public List<Switch> getLights(String home_mac) throws Exception{
        Pattern pattern=Pattern.compile("^([0-9A-F]{2}[:-]){5}([0-9A-F]{2})$");
        Matcher matcher=pattern.matcher(home_mac);
        if(!matcher.matches())
        {
            throw new IllegalArgumentException("il mac deve essere un mac valido");
        }
        List<Switch> l=new ArrayList<Switch>();
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(HttpHost.create("http://192.168.1.7:9200")));

        SearchResponse response = client.search(new SearchRequest("switch")
                .source( new SearchSourceBuilder().query(
                        QueryBuilders.matchQuery("id_home", home_mac)
                        )
                                .aggregation(AggregationBuilders
                                        .terms("getLastSwitch")
                                        .field("id_sensor.keyword")
                                        .subAggregation(AggregationBuilders
                                                .topHits("ultimeSwitch")
                                                .sort("@timestamp", SortOrder.DESC).size(1))
                                )
                                .size(0)
                ), RequestOptions.DEFAULT);
        System.out.println(response.toString());
        Aggregations aggs = response.getAggregations();
        Terms getLastsw = aggs.get("getLastSwitch");
        for (Terms.Bucket bucket : getLastsw.getBuckets())
        {
            ParsedTopHits a=bucket.getAggregations().get("ultimeSwitch");
            for (SearchHit hit :a.getHits())
            {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
                Switch sw=new Switch(
                        hit.getSourceAsMap().get("id_home").toString(),
                        hit.getSourceAsMap().get("id_sensor").toString(),
                        hit.getSourceAsMap().get("pre").toString(),
                        hit.getSourceAsMap().get("name").toString(),
                        hit.getSourceAsMap().get("value").toString(),
                        hit.getSourceAsMap().get("metric").toString(),
                        Integer.parseInt(hit.getSourceAsMap().get("status").toString()),
                        new Timestamp(df.parse(hit.getSourceAsMap().get("@timestamp").toString()).getTime())
                );

                l.add(sw);
            }
        }
        return l;
    }


}
