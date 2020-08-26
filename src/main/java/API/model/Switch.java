package API.model;

import java.sql.Timestamp;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        super();
        if(!isMac(home_mac)){
            throw new IllegalArgumentException("il mac home deve essere valido!");
        }
        this.home_mac = home_mac;

        if(!isMac(sensor_mac))
        {
            throw new IllegalArgumentException("il mac del sensore deve essere valido!");
        }
        this.sensor_mac =sensor_mac;
        this.pre=pre;
        this.name=name;
        this.metric=metric;
        this.timestamp=timestamp;

        if((value.compareTo("ON")==0 && status==1)||(value.compareTo("OFF")==0 && status==0))
        {
            this.value=value;
            this.status=status;
        }
        else if(value.compareTo("OFF")==0)
        {
            throw new IllegalArgumentException("value è OFF, non si può avere status 1 ");
        }
        else if(value.compareTo("ON")==0)
        {
            throw new IllegalArgumentException("value è ON, non si può avere status 0 ");
        }


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

    public String getHome_mac() {
        return home_mac;
    }

    public void setHome_mac(String home_mac) {
        if(!isMac(home_mac)){
            throw new IllegalArgumentException("il mac home deve essere valido!");
        }
        else{
            this.home_mac = home_mac;
        }

    }

    public String getSensor_mac() {
        return sensor_mac;
    }

    public void setSensor_mac(String sensor_mac) {
        if(!isMac(sensor_mac)){
            throw new IllegalArgumentException("il mac del sensore deve essere valido!");
        }
        else{
            this.sensor_mac = sensor_mac;
        }
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

        if(value.compareTo("ON")==0)
        {
            this.value="ON";
            this.status=1;
        }
        else if(value.compareTo("OFF")==0){
            this.value="OFF";
            this.status=0;
        }
        else{
            throw new IllegalArgumentException("value è diverso da ON o OFF");
        }
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
        if(status==1)
        {
            this.value="ON";
            this.status=1;
        }
        else if(status==0){
            this.value="OFF";
            this.status=0;
        }
        else{
            throw new IllegalArgumentException("status è diverso da 0 o 1");
        }
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
