package API.service;

import API.model.Humidity;
import API.model.Temperatura;

import java.util.ArrayList;
import java.util.List;

public class HumidityService {
    public List<Humidity> search(String home_mac) throws Exception{
        return new ArrayList<Humidity>();
    }

    public List<Humidity> getLastHumidity(String home_mac) throws Exception{
        return new ArrayList<Humidity>();
    }

    public Double getAverage(String home_mac, String sensor_mac, String lte, String gte)throws Exception {
        return 0.0;
    }
}
