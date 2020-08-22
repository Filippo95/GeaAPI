package API.controller;

import API.model.Temperatura;
import API.service.TemperatureService;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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

        try{
            return temperatureService.search(home_mac);
        }catch (Exception e )
        {
            List<Temperatura> err=new ArrayList<>();
            err.add(new Temperatura("errore","la stringa non è un mac","",new Timestamp(0),0.0));
            return err;
        }


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
        return temperatureService.getLastTemperatures(home_mac);
    }

    @GetMapping(value = "/temperature/{home_mac}/{sensor_mac}/average")
    public Double getaverageTemperature(@PathVariable String home_mac, @PathVariable String sensor_mac,@RequestParam String timestamp_start, @RequestParam String timestamp_stop )
    {
        System.out.println("home_mac: "+home_mac+" sensor_mac: "+sensor_mac +" timestamp_start: "+timestamp_start+" timestamp_stop: "+timestamp_stop);
        return 0.0;
    }
}
