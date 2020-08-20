package API.model;

import org.junit.Test;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TemperatureModelTest {
    @Test
    public void test_that_costructor_accept_real_mac_not_fake_mac_as_string(){

            //creazione corretta
            Temperatura temp = new Temperatura("myid", "B8:27:EB:B7:E3:4C", "B8:27:EB:B7:E3:4C", new Timestamp(0), 30.2);
            //set_home_mac corretto
            temp.setHome_mac("B8:27:EB:B7:E3:4C");
            //set_sensorr_mac corretto
            temp.setSensor_mac("B8:27:EB:B7:E3:4C");

            //creazione con home mac non valido
            Exception exception=assertThrows(IllegalArgumentException.class,()->{
                Temperatura temp1 = new Temperatura("myid", "ds", "B8:27:EB:B7:E3:4C", new Timestamp(0), 30.2);
            });
            String expectedMessage = "il mac home deve essere valido!";
            String actualMessage = exception.getMessage();
            assertTrue(actualMessage.contains(expectedMessage));

            //creazione con sensor mac non valido
            Exception exception2=assertThrows(IllegalArgumentException.class,()->{
                Temperatura temp1 = new Temperatura("myid", "B8:27:EB:B7:E3:4C", "fd", new Timestamp(0), 30.2);
            });
            expectedMessage = "il mac del sensore deve essere valido!";
            actualMessage = exception2.getMessage();
            assertTrue(actualMessage.contains(expectedMessage));

            //set_sensor_mac con sensor_mac non valido
            Exception exception3=assertThrows(IllegalArgumentException.class,()->{
                temp.setSensor_mac("fds");
            });
            expectedMessage = "il mac del sensore deve essere valido!";
            actualMessage = exception3.getMessage();
            assertTrue(actualMessage.contains(expectedMessage));

            //set_home_mac con sensor_mac non valido
            Exception exception4=assertThrows(IllegalArgumentException.class,()->{
                temp.setHome_mac("fds");
            });
            expectedMessage = "il mac home deve essere valido!";
            actualMessage = exception4.getMessage();
            assertTrue(actualMessage.contains(expectedMessage));


    }


}
