package API.model;

import java.sql.Timestamp;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Humidity {
    private String id;
    private String home_mac;
    private String sensor_mac;
    private Timestamp timestamp;
    private double value;

    public Humidity(String id, String home_mac, String sensor_mac, Timestamp timestamp,double value) {

    }


        private boolean isMac(String mac){
        Pattern pattern=Pattern.compile("^([0-9A-F]{2}[:-]){5}([0-9A-F]{2})$");
        Matcher matcher=pattern.matcher(mac);
        if(matcher.matches())
        {
            return true;
        }
        return false;
    }
    //Getter e setter
    public String getHome_mac() {
        return home_mac;
    }

    public void setHome_mac(String home_mac) {

        this.home_mac = home_mac;
    }

    public String getSensor_mac() {
        return sensor_mac;
    }

    public void setSensor_mac(String sensor_mac) {

        this.sensor_mac = sensor_mac;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) { this.timestamp = timestamp; }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    @Override
    public String toString() {
        return "Humidity [id=" + id + ", home_mac=" + home_mac + ", sensor_mac=" + sensor_mac + ", value=" + value + ", timestamp=" + timestamp +"]";
    }


}
