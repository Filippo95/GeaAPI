package API.model;

import java.sql.Timestamp;

public class Switch {
    String home_mac;
    String sensor_mac;
    String pre;
    String name;
    String value;
    String metric;
    int status;
    Timestamp timestamp;

    public Switch(String home_mac,
            String sensor_mac,
            String pre,
            String name,
            String value,
            String metric,
            int status,
                  Timestamp timestamp)
    {

    }

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

    public String getPre() {
        return pre;
    }

    public void setPre(String pre) {
        this.pre = pre;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getMetric() {
        return metric;
    }

    public void setMetric(String metric) {
        this.metric = metric;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
