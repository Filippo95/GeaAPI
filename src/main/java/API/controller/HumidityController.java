package API.controller;

import API.Logging;
import API.model.Humidity;
import API.model.Temperatura;
import API.service.HumidityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HumidityController {
    @Autowired
    private HumidityService service;

    @GetMapping("humidity/{home_mac}/search")
    public List<Humidity> search(@PathVariable String home_mac)
    {
 return new ArrayList<Humidity>();
    }

    @GetMapping("/humidity/{home_mac}/lasts")
    public List<Humidity> lasts(@PathVariable String home_mac) {
        return new ArrayList<Humidity>();
    }

    @GetMapping(value = "/humidity/{home_mac}/{sensor_mac}/average/{gte}/{lte}")
    public Double getaverageTemperature(@PathVariable String home_mac, @PathVariable String sensor_mac,@PathVariable String lte, @PathVariable String gte )
    {
        return 0.0;
    }
}
