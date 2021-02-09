package API.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")


public class MainController {
    @GetMapping("/")
    public String search()
    {
        return"<h1>Benvenuto nella ReST API, da qi puoi accedere a diversi Endpoint:</h1> </br> " +
                "<h2>Humidity: </h2><ul>" +
                "<li><a href=\"/humidity/B8:27:EB:B8:E3:4C/lasts\">Last</a></li>" +
                "<li><a href=\"/humidity/B8:27:EB:B8:E3:4C/search\">Search</a></li>" +
                "<li><a href=\"/humidity/B8:27:EB:B7:E3:4C/8C:AA:B5:04:5F:F7/average/2021-02-08T00:00:00.000Z/2021-02-08T21:59:59.999Z\">media</a></li>" +
                "</ul></br>"+
                "<h2>Temperature: </h2><ul>" +
                "<li><a href=\"/temperature/B8:27:EB:B8:E3:4C/lasts\">Last</a></li>" +
                "<li><a href=\"/temperature/B8:27:EB:B8:E3:4C/search\">Search</a></li>" +
                "<li><a href=\"/temperature/B8:27:EB:B7:E3:4C/8C:AA:B5:04:5F:F7/average/2021-02-08T00:00:00.000Z/2021-02-08T21:59:59.999Z\">media</a></li>" +
                "</ul></br>"+
                "<h2>Switch: </h2><ul>" +
                "<li><a href=\"/switch/B8:27:EB:B8:E3:4C/getLights\">Lista punti luce nella casa </a></li>" +
                "<li><a href=\"/switch/B8:27:EB:B7:E3:4C/D8:F1:5B:8D:33:BF/getGraphData/2021-02-09T08:12:19.000Z/2021-02-09T12:12:20.000Z\">dati per grafico [limite alto] [limite basso]</a></li>" +
                "<li><a href=\"/switch/B8:27:EB:B7:E3:4C/D8:F1:5B:8D:33:BF/getHours/2021-02-09T08:12:19.000Z/2021-02-09T12:12:20.000Z\"> ore ON </a></li>" +
                "<li><a href=\"/switch/B8:27:EB:B7:E3:4C/D8:F1:5B:8D:33:BF/getAverageONHours\">media giornaliera</a></li>" +
                "</ul></br>"
                ;
    }
}
