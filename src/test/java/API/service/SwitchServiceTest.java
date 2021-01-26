package API.service;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SwitchServiceTest {
    @Test
    public void test_SwitchService_getGraphData_should_throw_exception_with_home_mac_string_empty() {
        SwitchService service=new SwitchService();
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.getGraphData("","B8:27:EB:B7:E3:4C","2020-08-23T21:59:59.999Z","2020-08-23T21:59:59.999Z");
        });
        String expectedMessage = "Metodo getGraphData il mac  devono essere valido";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
    @Test
    public void test_SwitchService_getGraphData_should_throw_exception_with_sensor_mac_string_empty() {
        SwitchService service=new SwitchService();
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.getGraphData("B8:27:EB:B7:E3:4C","","2020-08-22T22:00:00.000Z","2020-08-23T21:59:59.999Z");
        });
        String expectedMessage = "Metodo getGraphData il mac  devono essere valido";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
    @Test
    public void test_SwitchService_getGraphData_should_throw_exception_with_timestamp_lte_string_empty() {
        SwitchService service=new SwitchService();
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.getGraphData("B8:27:EB:B7:E3:4C","B8:27:EB:B7:E3:4C","","2020-08-23T21:59:59.999Z");
        });
        String expectedMessage = "Metodo getGraphData il timestamp  devono essere validi";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
    @Test
    public void test_SwitchService_getGraphData_should_throw_exception_with_timestamp_gte_string_empty() {
        SwitchService service=new SwitchService();
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.getGraphData("B8:27:EB:B7:E3:4C","B8:27:EB:B7:E3:4C","2020-08-23T21:59:59.999Z","");
        });
        String expectedMessage = "Metodo getGraphData il timestamp  devono essere validi";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

}
