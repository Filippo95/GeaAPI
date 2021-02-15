package API.controller;

import API.model.Switch;
import API.model.SwitchGraphData;
import API.service.SwitchService;
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
@WebMvcTest(value = SwitchController.class)
@WithMockUser
public class SwitchControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SwitchService service;
    private List<Switch> lista;
    String ex="";

    public SwitchControllerTest(){
        lista=new ArrayList<Switch>();
        try{
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
            Switch mockswitch=new Switch(
                    "B8:27:EB:B7:E3:4C",
                    "D8:F1:5B:8D:33:BF",
                    "stat/",
                    "stat/B8:27:EB:B7:E3:4C/D8:F1:5B:8D:33:BF/POWER",
                    "OFF",
                    "POWER",
                    0,
                    new Timestamp(df.parse("2020-09-23T04:47:40.414+00:00").getTime())
            );
            lista.add(mockswitch);
            lista.add(mockswitch);
            lista.add(mockswitch);


        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
        ex="[" +
                "  {" +
                "    \"home_mac\": \"B8:27:EB:B7:E3:4C\"," +
                "    \"sensor_mac\": \"D8:F1:5B:8D:33:BF\"," +
                "    \"pre\": \"stat/\"," +
                "    \"name\": \"stat/B8:27:EB:B7:E3:4C/D8:F1:5B:8D:33:BF/POWER\"," +
                "    \"value\": \"OFF\"," +
                "    \"metric\": \"POWER\"," +
                "    \"status\": 0," +
                "    \"timestamp\": \"2020-09-23T04:47:40.414+00:00\"" +
                "  }," +
                "{" +
                "    \"home_mac\": \"B8:27:EB:B7:E3:4C\"," +
                "    \"sensor_mac\": \"D8:F1:5B:8D:33:BF\"," +
                "    \"pre\": \"stat/\"," +
                "    \"name\": \"stat/B8:27:EB:B7:E3:4C/D8:F1:5B:8D:33:BF/POWER\"," +
                "    \"value\": \"OFF\"," +
                "    \"metric\": \"POWER\"," +
                "    \"status\": 0," +
                "    \"timestamp\": \"2020-09-23T04:47:40.414+00:00\"" +
                "  }," +
                "{" +
                "    \"home_mac\": \"B8:27:EB:B7:E3:4C\"," +
                "    \"sensor_mac\": \"D8:F1:5B:8D:33:BF\"," +
                "    \"pre\": \"stat/\"," +
                "    \"name\": \"stat/B8:27:EB:B7:E3:4C/D8:F1:5B:8D:33:BF/POWER\"," +
                "    \"value\": \"OFF\"," +
                "    \"metric\": \"POWER\"," +
                "    \"status\": 0," +
                "    \"timestamp\": \"2020-09-23T04:47:40.414+00:00\"" +
                "  }]";
    }

    //Metodo getLights
    @Test
    public void Test_Controller_getLights_should_return_an_array_of_3_elem() throws Exception{
        Mockito.when(
                service.getLights(Mockito.anyString())
        ).thenReturn(lista);

        RequestBuilder requestBuilder= MockMvcRequestBuilders.get(
                "/switch/B8:27:EB:B7:E3:4C/getLights").accept(
                MediaType.APPLICATION_JSON);
        String expected= ex;

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();


        JSONAssert.assertEquals(
                expected,
                result.getResponse().getContentAsString(),
                false);


        JSONArray expected_array=new JSONArray(expected);
        JSONArray response_array=new JSONArray(result.getResponse().getContentAsString());
        Assert.assertEquals(expected_array.length(),response_array.length());

    }
    @Test
    public void Test_Controller_getLights_should_return_error_if_mac_is_not_valid() throws Exception{
        // non ho bisogno del mock service perchè non dovrebbe neanchee fare la chiamata al servizio
        RequestBuilder requestBuilder= MockMvcRequestBuilders.get(
                "/switch/B8:27:EB:3:4C/getLights").accept(
                MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String expected=result.getResponse().getContentAsString();
        JSONArray expected_array=new JSONArray(expected);

        //Assertions

        //mi aspetto un array con un elemento
        Assert.assertEquals(1,expected_array.length());

        //mi aspetto che l'elemento dell'array sia:
        String exp="[{\"home_mac\":\"B8:27:EB:B7:E3:4C\",\"sensor_mac\":\"B8:27:EB:B7:E3:4C\",\"pre\":\"B8:27:EB:B7:E3:4C\",\"name\":\"\",\"value\":null,\"metric\":\"\",\"status\":0,\"timestamp\":\"1970-01-01T00:00:00.000+00:00\"}]";
        Assert.assertEquals(exp,result.getResponse().getContentAsString());
    }
    // Metodo getHours
    @Test
    public void Test_Controller_getHours_should_return_0_if_macs_are_not_valid() throws Exception{
        // non mi serve il mock del service perchè in teoria non ci dovrebbe neanche andaare
        // Testo il mac sensore sbagliato
        RequestBuilder requestBuilderErrorOnSensorMac= MockMvcRequestBuilders.get(
                "/switch/B8:27:EB:B7:E3:4C/D8:F1:fdfF/getHours/2021-02-09T08:12:19.000Z/2021-02-09T12:12:20.000Z").accept(
                MediaType.APPLICATION_JSON);

        MvcResult resultErrorOnSensorMac = mockMvc.perform(requestBuilderErrorOnSensorMac).andReturn();
        String expectedErrorOnSensorMac=resultErrorOnSensorMac.getResponse().getContentAsString();

        //Assertions
        String exp="0.0";
        Assert.assertEquals(exp,expectedErrorOnSensorMac);


        RequestBuilder requestBuilderErrorOnHomeMac= MockMvcRequestBuilders.get(
                "/switch/D8:F1:fdfF/B8:27:EB:B7:E3:4C/getHours/2021-02-09T08:12:19.000Z/2021-02-09T12:12:20.000Z").accept(
                MediaType.APPLICATION_JSON);

        MvcResult resultErrorOnHomeMac = mockMvc.perform(requestBuilderErrorOnHomeMac).andReturn();
        String expectedErrorOnHomeMac=resultErrorOnHomeMac.getResponse().getContentAsString();

        //Assertions
        Assert.assertEquals(exp,expectedErrorOnHomeMac);
    }
    @Test
    public void Test_Controller_getHours_should_return_a_value_Double_() throws Exception{
        // controlla che ritorni un double e che sia quello giusto.
        Mockito.when(
                service.getTotalHours(Mockito.anyString(),Mockito.anyString(),Mockito.anyString(),Mockito.anyString())
        ).thenReturn(1.1);
        RequestBuilder requestBuilder= MockMvcRequestBuilders.get(
                "/switch/B8:27:EB:B7:E3:4C/D8:F1:5B:8D:33:C0/getHours/2020-08-26T11:00:00.000Z/2020-08-28T21:59:59.999Z").accept(
                MediaType.APPLICATION_JSON);


        MvcResult result = mockMvc.perform(requestBuilder).andReturn();


        JSONAssert.assertEquals(
                "1.1",
                result.getResponse().getContentAsString(),
                false);
        Double current=Double.parseDouble(result.getResponse().getContentAsString());
        Assert.assertFalse(current.isNaN());


    }
    //Metodo getAverageONHours
    @Test
    public void Test_Controller_getAverageONHours_should_return_error_for_incorrect_arguments() throws Exception{
        // non uso mock service perchè non dovrebbe neanche chiamare il servizio
        // mi aspetto che ritorni sempre 0.0
        String expected="0.0";

        // HomeMAc sbagliato
            RequestBuilder requestBuilder= MockMvcRequestBuilders.get(
                    "/switch/B8:27:EB/D8:F1:5B:8D:33:C0/getAverageONHours").accept(
                    MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content("[" +
                    "\"2020-08-27 00:00:00\"," +
                    "\"2020-08-28 00:00:00\"" +
                    "]");
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            //controllo che torni il valore expected
            Assert.assertEquals(expected,result.getResponse().getContentAsString());

        // SensorMAc sbagliato
            requestBuilder= MockMvcRequestBuilders.get(
                "/switch/D8:F1:5B:8D:33:C0/B8:27:EB/getAverageONHours").accept(
                MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content("[" +
                "\"2020-08-27 00:00:00\"," +
                "\"2020-08-28 00:00:00\"" +
                "]");
            result = mockMvc.perform(requestBuilder).andReturn();
            //controllo che torni il valore expected
            Assert.assertEquals(expected,result.getResponse().getContentAsString());

        // Lista di Timestamp non esistente
            requestBuilder= MockMvcRequestBuilders.get(
                    "/switch/D8:F1:5B:8D:33:C0/B8:27:EB/getAverageONHours").accept(
                    MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON);
            result = mockMvc.perform(requestBuilder).andReturn();
            //controllo che torni il valore vuoto
            Assert.assertEquals("",result.getResponse().getContentAsString());

        // Lista di Timestamp vuota
            requestBuilder= MockMvcRequestBuilders.get(
                    "/switch/D8:F1:5B:8D:33:C0/B8:27:EB/getAverageONHours").accept(
                    MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content("[]");
            result = mockMvc.perform(requestBuilder).andReturn();
            //controllo che torni il valore atteso
            Assert.assertEquals(expected,result.getResponse().getContentAsString());

        // Lista di Timestamp errati
            requestBuilder= MockMvcRequestBuilders.get(
                    "/switch/D8:F1:5B:8D:33:C0/B8:27:EB/getAverageONHours").accept(
                    MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content("[" +
                    "\"20208-27 00:00:00\"," +
                    "\"2020-08 00:00:00\"" +
                    "]");
            result = mockMvc.perform(requestBuilder).andReturn();
            //controllo che torni il valore atteso
            Assert.assertEquals(expected,result.getResponse().getContentAsString());


    }
    @Test
    public void Test_Controller_getAverageONHours_should_return_Double() throws Exception{

        Mockito.when(
                service.getAverageONHours(Mockito.anyString(),Mockito.anyString(),Mockito.anyList())
        ).thenReturn(1.1);

        RequestBuilder requestBuilder= MockMvcRequestBuilders.get(
                "/switch/B8:27:EB:B7:E3:4C/D8:F1:5B:8D:33:C0/getAverageONHours").accept(
                MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content("[" +
                "\"2020-08-27 00:00:00.000\"," +
                "\"2020-08-28 00:00:00.000\"" +
                "]");
        // in questo caso i timestamp sono dati senza il solito formato dateTtimeZ ma solo date time volutamente
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        //controllo che torni il valore giusto
        Assert.assertEquals("1.1",
                result.getResponse().getContentAsString());
        //controllo che il valore tornato sia Double
        Double current=Double.parseDouble(result.getResponse().getContentAsString());
        Assert.assertFalse(current.isNaN());



    }
    //Metodo getGraphData
    @Test
    public void Test_Controller_getGraphData_should_return_error_for_incorrect_arguments() throws Exception{
        // non uso mock service perchè non dovrebbe neanche chiamare il servizio
        // HomeMAc sbagliato --> deve tornare lista vuota
            RequestBuilder requestBuilder= MockMvcRequestBuilders.get(
                    "/switch/B8:27:EB:B7:/D8:F1:5B:8D:33:BF/getGraphData/2020-09-10T00:00:00.000Z/2020-09-10T23:59:59.999Z").accept(
                    MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content("[" +
                    "\"2020-08-27 00:00:00\"," +
                    "\"2020-08-28 00:00:00\"" +
                    "]");
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            //controllo che torni il valore expected
            Assert.assertEquals("[]",result.getResponse().getContentAsString());
        // SensorMAc sbagliato --> deve tornare lista vuota
             requestBuilder= MockMvcRequestBuilders.get(
                "/switch/D8:F1:5B:8D:33:BF/D8:F1::33:BF/getGraphData/2020-09-10T00:00:00.000Z/2020-09-10T23:59:59.999Z").accept(
                MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content("[" +
                "\"2020-08-27 00:00:00\"," +
                "\"2020-08-28 00:00:00\"" +
                "]");
             result = mockMvc.perform(requestBuilder).andReturn();
            //controllo che torni il valore expected
            Assert.assertEquals("[]",result.getResponse().getContentAsString());
        // Timestmap  sbagliato --> deve tornare lista vuota
            requestBuilder= MockMvcRequestBuilders.get(
                    "/switch/D8:F1:5B:8D:33:BF/D8:F1:5B:8D:33:BF/getGraphData/2020-0900:00.000Z/2020-09-10T23:59:59.999Z").accept(
                    MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content("[" +
                    "\"2020-08-27 00:00:00\"," +
                    "\"2020-08-28 00:00:00\"" +
                    "]");
            result = mockMvc.perform(requestBuilder).andReturn();
            //controllo che torni il valore expected
            Assert.assertEquals("[]",result.getResponse().getContentAsString());
        // Timestmap  sbagliato --> deve tornare lista vuota
            requestBuilder= MockMvcRequestBuilders.get(
                    "/switch/D8:F1:5B:8D:33:BF/D8:F1:5B:8D:33:BF/getGraphData/2020-09-1000:00.000Z/202-:59:59.999Z").accept(
                    MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content("[" +
                    "\"2020-08-27 00:00:00\"," +
                    "\"2020-08-28 00:00:00\"" +
                    "]");
            result = mockMvc.perform(requestBuilder).andReturn();
            //controllo che torni il valore expected
            Assert.assertEquals("[]",result.getResponse().getContentAsString());
    }
    @Test
    public void Test_Controller_getGraphData_should_retrun_list() throws Exception{
        List<SwitchGraphData> l=new ArrayList<SwitchGraphData>();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
        l.add(new SwitchGraphData(0,new Timestamp(df.parse("2020-09-10T13:01:12.009+00:00").getTime())));
        l.add(new SwitchGraphData(1,new Timestamp(df.parse("2020-09-10T13:01:49.737+00:00").getTime())));

        Mockito.when(
                service.getGraphData(Mockito.anyString(),Mockito.anyString(),Mockito.anyString(),Mockito.anyString())
        ).thenReturn(l);

        RequestBuilder requestBuilder= MockMvcRequestBuilders.get(
                "/switch/B8:27:EB:B7:E3:4C/D8:F1:5B:8D:33:BF/getGraphData/2020-09-10T00:00:00.000Z/2020-09-10T23:59:59.999Z").accept(
                MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String expectedlist="[{\"timestamp\":\"2020-09-10T13:01:12.009+00:00\",\"value\": 0 },{\"timestamp\": \"2020-09-10T13:01:49.737+00:00\",  \"value\": 1 }]";
        JSONAssert.assertEquals(expectedlist,result.getResponse().getContentAsString(),false);
    }
}
