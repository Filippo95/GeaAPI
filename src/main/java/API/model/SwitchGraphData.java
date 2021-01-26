package API.model;

import java.sql.Timestamp;


    public class SwitchGraphData{
        public Timestamp timestamp;
        public int value;

    public String toString(){
        return timestamp+"-"+value;
    }
}
