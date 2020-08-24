package API.service;

import API.model.Temperatura;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class TemperatureServiceTest {
    /*
    La classi service devono:
    - interfacciarsi con la base di dati
    - estrarre i dati
    - rappresentare i dati estratti in strutture dati di tipo lista secondo i modelli di dati estratti

     Purtroppo non riesco a fare un mock della classe RestHighLevelClient poichè dichiarata final
     quindi verificherò che i metodi della classe service rilancino le eccezzioni giuste.

     */

    @Test
    public void test_TemperatureService_search_should_throw_exception_with_mac_string_empty() {
        TemperatureService service=new TemperatureService();
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.search("");
        });
        String expectedMessage = "il mac deve essere un mac valido";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
    @Test
    public void test_TemperatureService_lasts_should_throw_exception_with_mac_string_empty() {
        TemperatureService service=new TemperatureService();
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.getLastTemperatures("");
        });
        String expectedMessage = "il mac deve essere un mac valido";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
    @Test
    public void test_TemperatureService_getAverage_should_throw_exception_with_home_mac_string_empty() {
        TemperatureService service=new TemperatureService();
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.getAverage("","B8:27:EB:B7:E3:4C","2020-08-23T21:59:59.999Z","2020-08-23T21:59:59.999Z");
        });
        String expectedMessage = "il mac o il timestamp devono essere validi";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
    @Test
    public void test_TemperatureService_getAverage_should_throw_exception_with_sensor_mac_string_empty() {
        TemperatureService service=new TemperatureService();
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.getAverage("B8:27:EB:B7:E3:4C","","2020-08-22T22:00:00.000Z","2020-08-23T21:59:59.999Z");
        });
        String expectedMessage = "il mac o il timestamp devono essere validi";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
    @Test
    public void test_TemperatureService_getAverage_should_throw_exception_with_timestamp_lte_string_empty() {
        TemperatureService service=new TemperatureService();
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.getAverage("B8:27:EB:B7:E3:4C","B8:27:EB:B7:E3:4C","","2020-08-23T21:59:59.999Z");
        });
        String expectedMessage = "il mac o il timestamp devono essere validi";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
    @Test
    public void test_TemperatureService_getAverage_should_throw_exception_with_timestamp_gte_string_empty() {
        TemperatureService service=new TemperatureService();
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.getAverage("B8:27:EB:B7:E3:4C","B8:27:EB:B7:E3:4C","2020-08-23T21:59:59.999Z","");
        });
        String expectedMessage = "il mac o il timestamp devono essere validi";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

}


