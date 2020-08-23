package API.controller;

import API.Logging;
import API.model.Temperatura;
import API.service.TemperatureService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class TemperaturaController {


    @Autowired
    private TemperatureService temperatureService;

    @GetMapping("/temperature/{home_mac}/search")
    public List<Temperatura> search(@PathVariable String home_mac) {
        Pattern pattern=Pattern.compile("^([0-9A-F]{2}[:-]){5}([0-9A-F]{2})$");
        Matcher matcher=pattern.matcher(home_mac);
        if(!matcher.matches())
        {
            List<Temperatura> err=new ArrayList<>();
            err.add(new Temperatura("errore","la stringa non è un mac","",new Timestamp(0),0.0));
            return err;
        }
        else {
            try {
                return temperatureService.search(home_mac);
            } catch (Exception e) {
                new Logging("Errore nel controller temperature",
                        "Errore nel return temperatureService.search(home_mac);",
                        e.getMessage(),
                        "/temperature/" + home_mac + "/search");
            }
        }
        List<Temperatura> err=new ArrayList<>();
        err.add(new Temperatura("","","",new Timestamp(0),0.0));
        return err;

    }
    @GetMapping("/temperature/{home_mac}/lasts")
    public List<Temperatura> lasts(@PathVariable String home_mac) {
        Pattern pattern=Pattern.compile("^([0-9A-F]{2}[:-]){5}([0-9A-F]{2})$");
        Matcher matcher=pattern.matcher(home_mac);
        if(!matcher.matches())
        {
            List<Temperatura> err=new ArrayList<>();
            err.add(new Temperatura("errore","la stringa non è un mac","",new Timestamp(0),0.0));
            return err;
        }
        else {
            try {
                return temperatureService.getLastTemperatures(home_mac);
            } catch (Exception e) {
                new Logging("Errore nel controller temperature",
                        "Errore nel return temperatureService.getLastTemperatures(home_mac);",
                        e.getMessage(),
                        "/temperature/" + home_mac + "/lasts");
            }
        }
        List<Temperatura> err=new ArrayList<>();
        err.add(new Temperatura("","","",new Timestamp(0),0.0));
        return err;
    }

    @GetMapping(value = "/temperature/{home_mac}/{sensor_mac}/average/{gte}/{lte}")
    public Double getaverageTemperature(@PathVariable String home_mac, @PathVariable String sensor_mac,@PathVariable String lte, @PathVariable String gte )
    {
        Pattern pattern=Pattern.compile("^([0-9A-F]{2}[:-]){5}([0-9A-F]{2})$");
        Pattern timestmapPattern=Pattern.compile("^(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.\\d{3})Z$");

        if(!pattern.matcher(home_mac).matches() && !pattern.matcher(sensor_mac).matches() && !timestmapPattern.matcher(gte).matches() && !timestmapPattern.matcher(lte).matches())
        {
            new Logging("Errore nel controller temperature","Errore il mac inserito non ","ritorno 0","/temperature/"+home_mac+"/"+sensor_mac+"/average/"+lte+"/"+gte);
            return 0.0;
        }
        else
        {
            try{
                return temperatureService.getAverage(home_mac,sensor_mac,lte,gte);
            }catch (Exception e){
                new Logging("Errore nel controller temperature",
                        "Errore nel return temperatureService.getAverage(home_mac,sensor_mac,lte,gte);",
                        e.getMessage(),
                        "/temperature/"+home_mac+"/"+sensor_mac+"/average/"+lte+"/"+gte);
            }
        }
        return 0.0;
    }
}
