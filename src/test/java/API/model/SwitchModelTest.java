package API.model;

import org.junit.Test;
import API.model.Switch;
import org.junit.jupiter.api.TestTemplate;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;

public class SwitchModelTest {
    @Test
    public void correct_creation_of_switch(){
        Switch s=new Switch("B8:27:EB:B7:E3:4C",
        "D8:F1:5B:8D:33:BF",
        "stat/",
        "stat/B8:27:EB:B7:E3:4C/D8:F1:5B:8D:33:BF/POWER",
        "OFF",
        "POWER",
        0,
                new Timestamp(0));

    }
    @Test
    public void creation_with_home_mac_not_valid()
    {
        Exception exception=assertThrows(IllegalArgumentException.class,()->{
            Switch s=new Switch("B8:27:EB:B7:E3:4",
                    "D8:F1:5B:8D:33:BF",
                    "stat/",
                    "stat/B8:27:EB:B7:E3:4C/D8:F1:5B:8D:33:BF/POWER",
                    "OFF",
                    "POWER",
                    0,
                    new Timestamp(0));
        });
        String expectedMessage = "il mac home deve essere valido!";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
    @Test
    public void creation_with_sensor_mac_not_valid()
    {
        //creazione con sensor mac non valido
        Exception exception2=assertThrows(IllegalArgumentException.class,()->{
            Switch s=new Switch("B8:27:EB:B7:E3:4C",
                    "D8:F1:5B:8D:33:",
                    "stat/",
                    "stat/B8:27:EB:B7:E3:4C/D8:F1:5B:8D:33:BF/POWER",
                    "OFF",
                    "POWER",
                    0,
                    new Timestamp(0));
             });
        String expectedMessage = "il mac del sensore deve essere valido!";
        String actualMessage = exception2.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
    @Test
    public void set_sensor_mac_with_sensor_mac_not_valid()
    {
        Exception exception3=assertThrows(IllegalArgumentException.class,()->{
            Switch s=new Switch("B8:27:EB:B7:E3:4C",
                    "D8:F1:5B:8D:33:BF",
                    "stat/",
                    "stat/B8:27:EB:B7:E3:4C/D8:F1:5B:8D:33:BF/POWER",
                    "OFF",
                    "POWER",
                    0,
                    new Timestamp(0));
            s.setSensor_mac("fds");
        });
        String expectedMessage = "il mac del sensore deve essere valido!";
        String actualMessage = exception3.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
    @Test
    public void set_home_mac_with_homee_mac_not_valid()
    {
        Exception exception4=assertThrows(IllegalArgumentException.class,()->{
            Switch s=new Switch("B8:27:EB:B7:E3:4C",
                    "D8:F1:5B:8D:33:BF",
                    "stat/",
                    "stat/B8:27:EB:B7:E3:4C/D8:F1:5B:8D:33:BF/POWER",
                    "OFF",
                    "POWER",
                    0,
                    new Timestamp(0));
            s.setHome_mac("fds");
        });
        String expectedMessage = "il mac home deve essere valido!";
        String actualMessage = exception4.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
    // test che se lo status è 0 allora il valore deve essere off oppure se lo status è 1 allora status è 1
    @Test
    public void test_costructor_if_value_is_0_value_must_be_OFF()
    {
        //testo il costruttore
        Exception exception4=assertThrows(IllegalArgumentException.class,()->{
            Switch s=new Switch("B8:27:EB:B7:E3:4C",
                    "D8:F1:5B:8D:33:BF",
                    "stat/",
                    "stat/B8:27:EB:B7:E3:4C/D8:F1:5B:8D:33:BF/POWER",
                    "OFF",
                    "POWER",
                    1,
                    new Timestamp(0));
        });
        String expectedMessage = "value è OFF, non si può avere status 1 ";
        String actualMessage = exception4.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

        //testo setValue
            Switch s=new Switch("B8:27:EB:B7:E3:4C",
                    "D8:F1:5B:8D:33:BF",
                    "stat/",
                    "stat/B8:27:EB:B7:E3:4C/D8:F1:5B:8D:33:BF/POWER",
                    "ON",
                    "POWER",
                    1,
                    new Timestamp(0));
            s.setValue("OFF");
            assertEquals(s.status,0);

        //testo setStatus
         s=new Switch("B8:27:EB:B7:E3:4C",
                "D8:F1:5B:8D:33:BF",
                "stat/",
                "stat/B8:27:EB:B7:E3:4C/D8:F1:5B:8D:33:BF/POWER",
                "ON",
                "POWER",
                1,
                new Timestamp(0));
        s.setStatus(0);
        assertEquals(s.getValue(),0);
    }
    @Test
    public void test_costructor_if_value_is_1_value_must_be_ON()
    {
        //testo il costruttore
        Exception exception4=assertThrows(IllegalArgumentException.class,()->{
            Switch s=new Switch("B8:27:EB:B7:E3:4C",
                    "D8:F1:5B:8D:33:BF",
                    "stat/",
                    "stat/B8:27:EB:B7:E3:4C/D8:F1:5B:8D:33:BF/POWER",
                    "ON",
                    "POWER",
                    0,
                    new Timestamp(0));
        });
        String expectedMessage = "value è OFF, non si può avere status 1 ";
        String actualMessage = exception4.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

        //testo setValue
        Switch s=new Switch("B8:27:EB:B7:E3:4C",
                "D8:F1:5B:8D:33:BF",
                "stat/",
                "stat/B8:27:EB:B7:E3:4C/D8:F1:5B:8D:33:BF/POWER",
                "ON",
                "POWER",
                1,
                new Timestamp(0));
        s.setValue("OFF");
        assertEquals(s.getStatus(),0);

        //testo setStatus
        s=new Switch("B8:27:EB:B7:E3:4C",
                "D8:F1:5B:8D:33:BF",
                "stat/",
                "stat/B8:27:EB:B7:E3:4C/D8:F1:5B:8D:33:BF/POWER",
                "ON",
                "POWER",
                1,
                new Timestamp(0));
        s.setStatus(0);
        assertEquals(s.getValue(),0);
    }
    @Test
    public void test_setValue_accept_only_ON_or_OFF()
    {
        Exception exception4=assertThrows(IllegalArgumentException.class,()->{
            Switch s=new Switch("B8:27:EB:B7:E3:4C",
                    "D8:F1:5B:8D:33:BF",
                    "stat/",
                    "stat/B8:27:EB:B7:E3:4C/D8:F1:5B:8D:33:BF/POWER",
                    "ON",
                    "POWER",
                    0,
                    new Timestamp(0));
            s.setValue("fd");
        });

        String expectedMessage = "value è diverso da ON o OFF";
        String actualMessage = exception4.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
    public void test_setStatus_accept_only_ON_or_OFF()
    {
        Exception exception4=assertThrows(IllegalArgumentException.class,()->{
            Switch s=new Switch("B8:27:EB:B7:E3:4C",
                    "D8:F1:5B:8D:33:BF",
                    "stat/",
                    "stat/B8:27:EB:B7:E3:4C/D8:F1:5B:8D:33:BF/POWER",
                    "ON",
                    "POWER",
                    0,
                    new Timestamp(0));
            s.setStatus(132);
        });

        String expectedMessage = "status è diverso da 0 o 1";
        String actualMessage = exception4.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }


}
