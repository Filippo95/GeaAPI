package API.controller;

import API.Logging;
import API.model.Switch;
import API.model.SwitchGraphData;
import API.model.Temperatura;
import API.service.SwitchService;
import API.service.TemperatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class SwitchController {
    @Autowired
    private SwitchService service;

    @GetMapping(value = "/switch/{home_mac}/{sensor_mac}/getGraphData/{gte}/{lte}")
    public List<SwitchGraphData> getaverageTemperature(@PathVariable String home_mac, @PathVariable String sensor_mac, @PathVariable String lte, @PathVariable String gte )
    {
        Pattern pattern=Pattern.compile("^([0-9A-F]{2}[:-]){5}([0-9A-F]{2})$");
        Pattern timestmapPattern=Pattern.compile("^(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.\\d{3})Z$");
        List<SwitchGraphData> l=new ArrayList<>();

        if(!pattern.matcher(home_mac).matches() && !pattern.matcher(sensor_mac).matches() && !timestmapPattern.matcher(gte).matches() && !timestmapPattern.matcher(lte).matches())
        {
            new Logging("Errore nel controller Switch","Errore il mac inserito non ","ritorno 0","/switch/"+home_mac+"/"+sensor_mac+"/getGraphData/"+lte+"/"+gte);
            return l;
        }
        else
        {
            try{
                return service.getGraphData(home_mac,sensor_mac,lte,gte);
            }catch (Exception e){
                new Logging("Errore nel controller Switch",
                        "Errore nel return service.getGraohData(home_mac,sensor_mac,lte,gte);",
                        e.getMessage(),
                        "/switch/"+home_mac+"/"+sensor_mac+"/getGraphData/"+lte+"/"+gte);
            }
        }
        return l;

    }

    @GetMapping(value="switch/{home_mac}/{sensor_mac}/getHours/{gte}/{lte}")
    public Double getHours(@PathVariable String home_mac, @PathVariable String sensor_mac, @PathVariable String lte, @PathVariable String gte)
    {
        Pattern pattern=Pattern.compile("^([0-9A-F]{2}[:-]){5}([0-9A-F]{2})$");
        Pattern timestmapPattern=Pattern.compile("^(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.\\d{3})Z$");


        if(!pattern.matcher(home_mac).matches() && !pattern.matcher(sensor_mac).matches() && !timestmapPattern.matcher(gte).matches() && !timestmapPattern.matcher(lte).matches())
        {
            new Logging("Errore nel controller Switch","Errore il mac inserito non ","ritorno 0","/switch/"+home_mac+"/"+sensor_mac+"/getGraphData/"+lte+"/"+gte);
            return 0.0;
        }
        else
        {
            try{
                return service.getTotalHours(home_mac,sensor_mac,lte,gte);
            }catch (Exception e){
                new Logging("Errore nel controller Switch",
                        "Errore nel return service.getGraohData(home_mac,sensor_mac,lte,gte);",
                        e.getMessage(),
                        "/switch/"+home_mac+"/"+sensor_mac+"/getGraphData/"+lte+"/"+gte);
            }
        }
        return 0.0;
    }
    @GetMapping(value="switch/{home_mac}/{sensor_mac}/getAverageONHours")
    public Double getAverageONHours(@PathVariable String home_mac, @PathVariable String sensor_mac, @RequestBody List<String> list)
    {
        Pattern pattern=Pattern.compile("^([0-9A-F]{2}[:-]){5}([0-9A-F]{2})$");
        Pattern timestmapPattern=Pattern.compile("^(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.\\d{3})Z$");
        List<Timestamp> l=new ArrayList<>();
        for (String time :list) {
            if(!pattern.matcher(home_mac).matches() && !pattern.matcher(sensor_mac).matches() && !timestmapPattern.matcher(time).matches() )
            {
                new Logging("Errore nel controller Switch","Errore il mac inserito non ","ritorno 0","/switch/"+home_mac+"/"+sensor_mac+"/getAverageONHours");
                return 0.0;
            }
            else
            {
                    Timestamp t=Timestamp.valueOf(time);
                    l.add(t);
            }
        }
        for (Timestamp t:l) {
            System.out.println(t);
        }
        try{
            System.out.println("Metodo nel controller getAverageHours, chiamo il servizio service.getAverageONHours(home_mac,sensor_mac,l) ");
        return service.getAverageONHours(home_mac,sensor_mac,l);
        }catch (Exception e){
        new Logging("Errore nel controller Switch",
                "Errore nel return service.getAverageONHours(home_mac,sensor_mac,l);",
                e.getMessage(),
                "/switch/"+home_mac+"/"+sensor_mac+"/getAverageONHours");
        }
        return 0.0;
    }

    @GetMapping(value="switch/{home_mac}/getLights")
    public List<Switch> getLights(@PathVariable String home_mac){
        Pattern pattern=Pattern.compile("^([0-9A-F]{2}[:-]){5}([0-9A-F]{2})$");
        Matcher matcher=pattern.matcher(home_mac);
        if(!matcher.matches())
        {
            List<Switch> err=new ArrayList<>();
            err.add(new Switch("B8:27:EB:B7:E3:4C","B8:27:EB:B7:E3:4C","B8:27:EB:B7:E3:4C","","","",1,new Timestamp(0)));
            return err;
        }
        else {
            try {
                return service.getLights(home_mac);
            } catch (Exception e) {
                new Logging("Errore nel controller switch",
                        "Errore nel return service.getLights(home_mac);",
                        e.getMessage(),
                        "/switch/" + home_mac + "/search");
            }
        }
        List<Switch> err=new ArrayList<>();
        err.add(new Switch("B8:27:EB:B7:E3:4C","B8:27:EB:B7:E3:4C","B8:27:EB:B7:E3:4C","","","",1,new Timestamp(0)));
        return err;

    }




}
