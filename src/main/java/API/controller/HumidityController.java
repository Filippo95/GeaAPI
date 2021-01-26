package API.controller;

import API.Logging;
import API.model.Humidity;
import API.service.HumidityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class HumidityController {
    @Autowired
    private HumidityService service;

    @GetMapping("/humidity/{home_mac}/search")
    public List<Humidity> search(@PathVariable String home_mac)
    {
        Pattern pattern=Pattern.compile("^([0-9A-F]{2}[:-]){5}([0-9A-F]{2})$");
        Matcher matcher=pattern.matcher(home_mac);
        if(!matcher.matches())
        {

            List<Humidity> err=new ArrayList<Humidity>();
            err.add(new Humidity("errore","B8:27:EB:B7:E3:4C","B8:27:EB:B7:E3:4C",new Timestamp(0),0.0));
            return err;
        }
        else {
            try {
                return service.search(home_mac);
            } catch (Exception e) {
                new Logging("Errore nel controller humidity",
                        "Errore nel return service.search(home_mac);",
                        e.getMessage(),
                        "/humidity/" + home_mac + "/search");
            }
        }
        List<Humidity> err=new ArrayList<>();
        err.add(new Humidity("","","",new Timestamp(0),0.0));
        return err;
    }

    @GetMapping("/humidity/{home_mac}/lasts")
    public List<Humidity> lasts(@PathVariable String home_mac) {
        Pattern pattern=Pattern.compile("^([0-9A-F]{2}[:-]){5}([0-9A-F]{2})$");
        Matcher matcher=pattern.matcher(home_mac);
        if(!matcher.matches())
        {
            List<Humidity> err=new ArrayList<>();
            err.add(new Humidity("errore","B8:27:EB:B7:E3:4C","B8:27:EB:B7:E3:4C",new Timestamp(0),0.0));
            return err;
        }
        else {
            try {
                return service.getLastHumidity(home_mac);
            } catch (Exception e) {
                new Logging("Errore nel controller humidity",
                        "Errore nel return service.getLastHumidity(home_mac);",
                        e.getMessage(),
                        "/humidity/" + home_mac + "/lasts");
            }
        }
        List<Humidity> err=new ArrayList<>();
        err.add(new Humidity("","B8:27:EB:B7:E3:4C","B8:27:EB:B7:E3:4C",new Timestamp(0),0.0));
        return err;
    }

    @GetMapping(value = "/humidity/{home_mac}/{sensor_mac}/average/{gte}/{lte}")
    public Double getaverageHumidity(@PathVariable String home_mac, @PathVariable String sensor_mac,@PathVariable String lte, @PathVariable String gte )
    {
        Pattern pattern=Pattern.compile("^([0-9A-F]{2}[:-]){5}([0-9A-F]{2})$");
        Pattern timestmapPattern=Pattern.compile("^(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.\\d{3})Z$");

        if(!pattern.matcher(home_mac).matches() && !pattern.matcher(sensor_mac).matches() && !timestmapPattern.matcher(gte).matches() && !timestmapPattern.matcher(lte).matches())
        {
            new Logging("Errore nel controller humidity","Errore il mac inserito non ","ritorno 0","/humidity/"+home_mac+"/"+sensor_mac+"/average/"+lte+"/"+gte);
            return 0.0;
        }
        else
        {
            try{
                return service.getAverage(home_mac,sensor_mac,lte,gte);
            }catch (Exception e){
                new Logging("Errore nel controller humidity",
                        "Errore nel return service.getAverage(home_mac,sensor_mac,lte,gte);",
                        e.getMessage(),
                        "/humidity/"+home_mac+"/"+sensor_mac+"/average/"+lte+"/"+gte);
            }
        }
        return 0.0;
    }
}
