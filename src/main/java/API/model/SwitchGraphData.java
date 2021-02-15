package API.model;

import java.sql.Timestamp;


    public class SwitchGraphData{
        public Timestamp timestamp;
        public int value;
        public SwitchGraphData(){}
        public SwitchGraphData(int i, Timestamp s){
            this.value=i;
            this.timestamp=s;
        }

    public String toString(){
        return timestamp+"-"+value;
    }
}
