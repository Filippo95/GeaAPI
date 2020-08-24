package API.controller;

import API.controller.TemperaturaController;
import API.model.Temperatura;
import API.service.TemperatureService;
import org.json.JSONArray;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@WebMvcTest(value = TemperaturaController.class)
@WithMockUser
public class TemperaturaControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TemperatureService temperatureService;
    private List<Temperatura> lista;
    String exampleTemperatureJson="";

    public TemperaturaControllerTest()  {
        lista = new ArrayList<Temperatura>();
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");

            Temperatura mockTemperatura = new Temperatura(
                    "8Z60-3MBv7xejceh6jSY",
                    "B8:27:EB:B7:E3:4C",
                    "8C:AA:B5:04:5F:F7",
                    new Timestamp(df.parse("2020-08-17T09:16:44.458+00:00").getTime()),
                    new Double("29.09"));

            lista.add(mockTemperatura);
            lista.add(mockTemperatura);
            lista.add(mockTemperatura);
               }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
       exampleTemperatureJson = "[{\"id\":\"8Z60-3MBv7xejceh6jSY\",\"home_mac\":\"B8:27:EB:B7:E3:4C\",\"sensor_mac\":\"8C:AA:B5:04:5F:F7\",\"timestamp\":\"2020-08-17T09:16:44.458+00:00\",\"value\":29.09},{\"id\":\"8Z60-3MBv7xejceh6jSY\",\"home_mac\":\"B8:27:EB:B7:E3:4C\",\"sensor_mac\":\"8C:AA:B5:04:5F:F7\",\"timestamp\":\"2020-08-17T09:16:44.458+00:00\",\"value\":29.09},{\"id\":\"8Z60-3MBv7xejceh6jSY\",\"home_mac\":\"B8:27:EB:B7:E3:4C\",\"sensor_mac\":\"8C:AA:B5:04:5F:F7\",\"timestamp\":\"2020-08-17T09:16:44.458+00:00\",\"value\":29.09}]";
    }



    @Test
    public void Test_controller_lasts_should_return_an_arrray_of_3_elem() throws Exception {
        Mockito.when(
                temperatureService.getLastTemperatures(Mockito.anyString())
        ).thenReturn(lista);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/temperature/B8:27:EB:B7:E3:4C/lasts").accept(
                MediaType.APPLICATION_JSON);
        String expected = exampleTemperatureJson;


        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        JSONAssert.assertEquals(
                expected,
                result.getResponse().getContentAsString(),
                false);
        System.out.println(expected);
        System.out.println(result.getResponse().getContentAsString());

        //controllo che gli array abbiano la stessa lunghezza
        JSONArray expected_array=new JSONArray(expected);
        JSONArray response_array=new JSONArray(result.getResponse().getContentAsString());
        Assert.assertEquals(expected_array.length(),response_array.length());

    }
    @Test
    public void Test_controller_search_should_return_many_of_same_home_mac () throws Exception {
        Mockito.when(
                temperatureService.search(Mockito.anyString())
        ).thenReturn(lista);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/temperature/B8:27:EB:B7:E3:4C/search").accept(
                MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String expected=result.getResponse().getContentAsString();
        JSONArray expected_array=new JSONArray(expected);
        System.out.println(expected);

    }

    /*
    il controller ha dei compiti, ovvero verificare i parametri e eseguire la chiamata al service
    con questi test si vuolee verificare che il controller verifichi la correttezza dei parametri
    che gli vengono passati

    - test che siano mac
    - test che siano timestamp
    - test che i mac esistano nel db
    - test che
     */
    @Test
    public void method_search_should_return_error_if_mac_is_not_valid() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/temperature/B8:27:EB:B7:E3:ff/search").accept(
                MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String expected=result.getResponse().getContentAsString();
        JSONArray expected_array=new JSONArray(expected);
        Assert.assertEquals("errore",expected_array.getJSONObject(0).optString("id").toString());
    }
    @Test
    public void method_lasts_should_return_error_if_mac_is_not_valid() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/temperature/B8:27:EB:B7:E3:ff/lasts").accept(
                MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String expected=result.getResponse().getContentAsString();
        JSONArray expected_array=new JSONArray(expected);
        Assert.assertEquals("errore",expected_array.getJSONObject(0).optString("id").toString());
    }
    @Test
    public void method_getaverageTemperature_should_return_error_if_home_mac_is_not_valid() throws Exception{
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/temperature/B8:27:EB:B7:E3:ff/68:C6:3A:87:F9:FB/average/2020-08-22T22:00:00.000Z/2020-08-23T21:59:59.999Z").accept(
                MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String expected=result.getResponse().getContentAsString();

        Assert.assertEquals("0.0",expected);
    }
    @Test
    public void method_getaverageTemperature_should_return_error_if_sensor_mac_is_not_valid() throws Exception{
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/temperature/68:C6:3A:87:F9:FB/6B8:27:EB:B7:E3:ff/average/2020-08-22T22:00:00.000Z/2020-08-23T21:59:59.999Z").accept(
                MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String expected=result.getResponse().getContentAsString();

        Assert.assertEquals("0.0",expected);
    }
    @Test
    public void method_getaverageTemperature_should_return_error_if_timestamp_lte_is_not_valid() throws Exception{
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/temperature/68:C6:3A:87:F9:FB/68:C6:3A:87:F9:FB/average/2020-08-22T22/2020-08-23T21:59:59.999Z").accept(
                MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String expected=result.getResponse().getContentAsString();

        Assert.assertEquals("0.0",expected);
    }
    @Test
    public void method_getaverageTemperature_should_return_error_if_timestamp_gte_is_not_valid() throws Exception{
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/temperature/68:C6:3A:87:F9:FB/68:C6:3A:87:F9:FB/average/2020-08-22T22/2020-08-23T21:59:59.999Z").accept(
                MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String expected=result.getResponse().getContentAsString();

        Assert.assertEquals("0.0",expected);
    }
}
