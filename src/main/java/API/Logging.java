package API;

import java.sql.Timestamp;

public class Logging{
    Timestamp timestamp;
    String Description;
    String Name;
    String ExceptionMessage;
    String Path;

    public Logging( String Description, String Name, String ExceptionMessage,String Path){
        this.timestamp=new Timestamp(System.currentTimeMillis());
        this.Description=Description;
        this.Name=Name;
        this.Path=ExceptionMessage;
        this.ExceptionMessage=ExceptionMessage;

        System.err.println(this.timestamp+" "+Name+" Path: "+this.Path+" Description: "+this.Description+" Exception: "+this.ExceptionMessage);
        System.out.println(this.timestamp+" "+Name+" Path: "+this.Path+" Description: "+this.Description+" Exception: "+this.ExceptionMessage);

    }
}
