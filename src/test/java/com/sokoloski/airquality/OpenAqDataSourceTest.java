package com.sokoloski.airquality;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class OpenAqDataSourceTest {

    private static final String locationUrlForCountryCode = "https://docs.openaq.org/v2/locations?limit=10";
    private static final String locationUrlForCoordinates = "https://docs.openaq.org/v2/locations?limit=10&radius=1";

    @Mock
    RestTemplate restTemplate;

    @InjectMocks
    @Autowired
    private OpenAqDataSource testObject;

    @Before
    public void init() throws JSONException {
        List<AirQuality> allAqs = getTestListOfAirQuality();
        JSONObject jsonBody = getResponseJsonBody(allAqs);

        ResponseEntity<String> response = new ResponseEntity<String>(jsonBody.toString(), HttpStatus.OK);
        Mockito.when(restTemplate.exchange(
                        ArgumentMatchers.anyString(),
                        ArgumentMatchers.any(HttpMethod.class),
                        ArgumentMatchers.any(),
                        ArgumentMatchers.<Class<String>>any()))
                .thenReturn(response);
    }

    @Test
    public void getByCountryAndMeasuredParam_CallsOpenAqLocationsUrl() throws JSONException {
        String countryCode = "CA";
        String measuredParam = "um025";

        testObject.getByCountryAndMeasuredParam(countryCode, measuredParam);

        verify(restTemplate, times(1)).exchange(
                eq(locationUrlForCountryCode + "&country_id=" + countryCode),
                eq(HttpMethod.GET),
                eq(null),
                eq(String.class));
    }

    @Test
    public void getByCountryAndMeasuredParam_ReturnEmptyListWhenAqLocationsDoesntReturn200() throws JSONException {
        String countryCode = "US";
        ResponseEntity<String> response = new ResponseEntity<String>("", HttpStatus.INTERNAL_SERVER_ERROR);

        Mockito.when(restTemplate.exchange(
                        eq(locationUrlForCountryCode + "&country_id=" + countryCode),
                        ArgumentMatchers.any(HttpMethod.class),
                        ArgumentMatchers.any(),
                        ArgumentMatchers.<Class<String>>any()))
                .thenReturn(response);

        List<AirQuality> retval = testObject.getByCountryAndMeasuredParam(countryCode,"pm25");

        Assert.assertEquals(null, retval);
    }

    @Test
    public void getByCountryAndMeasuredParam_ReturnListWithValidData1() throws JSONException {
        String countryCode = "US";
        String measuredParam = "pm1";

        List<AirQuality> allAqs = getTestListOfAirQuality();

        List<AirQuality> retval = testObject.getByCountryAndMeasuredParam(countryCode, measuredParam);

        Assert.assertEquals(allAqs.size(), retval.size());
        Assert.assertEquals(allAqs.get(0).getId(), retval.get(0).getId());
        for (AirQualityParameter aqm: retval.get(0).getParameters()) {
            Assert.assertEquals(measuredParam, aqm.getParameter());
        }
    }

    @Test
    public void getByCountryAndMeasuredParam_ReturnListWithValidData2() throws JSONException {
        String countryCode = "CA";
        String measuredParam = "um025";

        List<AirQuality> allAqs = getTestListOfAirQuality();

        List<AirQuality> retval = testObject.getByCountryAndMeasuredParam(countryCode, measuredParam);

        Assert.assertEquals(allAqs.size(), retval.size());
        Assert.assertEquals(allAqs.get(0).getId(), retval.get(0).getId());
        for (AirQualityParameter aqm: retval.get(0).getParameters()) {
            Assert.assertEquals(measuredParam, aqm.getParameter());
        }
    }

    @Test
    public void getByCountryAndMeasuredParam_ThrowsExceptionOnInvalidResponse() throws JSONException {
        String countryCode = "CA";
        String measuredParam = "um025";

        List<AirQuality> allAqs = getTestListOfAirQuality();

        JSONObject jsonBody = getResponseJsonBody(allAqs);
        ResponseEntity<String> response = new ResponseEntity<String>("{meta:{},results:{no result set, will result in parse error}}", HttpStatus.OK);

        Mockito.when(restTemplate.exchange(
                        eq(locationUrlForCountryCode + "&country_id=" + countryCode),
                        ArgumentMatchers.any(HttpMethod.class),
                        ArgumentMatchers.any(),
                        ArgumentMatchers.<Class<String>>any()))
                .thenReturn(response);

        Exception exception = assertThrows(JSONException.class, () -> {
            testObject.getByCountryAndMeasuredParam(countryCode, measuredParam);
        });

        String expected = "Result has invalid JSON";
        String actual = exception.getMessage();

        Assert.assertTrue(actual.contains(expected));
    }

    @Test
    public void getByCoordinatesAndMeasuredParam_CallsOpenAqLocationsUrl(){
        double latitude = 33.999504;
        double longitude = -117.41602;
        String measuredParam = "um025";

        testObject.getByCoordinatesAndMeasuredParam(latitude, longitude, measuredParam);

        verify(restTemplate, times(1)).exchange(
                eq(locationUrlForCoordinates + "&coordinates=" + latitude + "," + longitude),
                eq(HttpMethod.GET),
                eq(null),
                eq(String.class));
    }

    @Test
    public void getByCoordinatesAndMeasuredParam_ReturnEmptyListWhenAqLocationsDoesntReturn200() throws JSONException {
        double latitude = 33.999504;
        double longitude = -117.41602;
        ResponseEntity<String> response = new ResponseEntity<String>("", HttpStatus.INTERNAL_SERVER_ERROR);

        Mockito.when(restTemplate.exchange(
                        eq(locationUrlForCoordinates + "&coordinates=" + latitude + "," + longitude),
                        ArgumentMatchers.any(HttpMethod.class),
                        ArgumentMatchers.any(),
                        ArgumentMatchers.<Class<String>>any()))
                .thenReturn(response);

        List<AirQuality> retval = testObject.getByCoordinatesAndMeasuredParam(latitude, longitude,"pm25");

        Assert.assertEquals(null, retval);
    }


    private JSONObject getResponseJsonBody(List<AirQuality> airQualities) throws JSONException {
        JSONObject jsonBody = new JSONObject();

        JSONObject meta = new JSONObject();
        jsonBody.put("meta", "");

        JSONArray results = new JSONArray();
        for (AirQuality aq : airQualities) {
            JSONObject result = new JSONObject();
            result.put("id", aq.getId());
            result.put("name", aq.getName());
            result.put("country", aq.getCountry());
            JSONObject coordinates = new JSONObject();
            coordinates.put("latitude", aq.getCoordinates().getLatitude());
            coordinates.put("longitude", aq.getCoordinates().getLongitude());
            result.put("coordinates", coordinates);

            JSONArray parameters = new JSONArray();
            for (AirQualityParameter aqp: aq.getParameters()) {
                JSONObject parameter = new JSONObject();
                parameter.put("id", aqp.getId());
                parameter.put("unit", aqp.getUnit());
                parameter.put("lastValue", aqp.getLastValue());
                parameter.put("parameter", aqp.getParameter());
                parameter.put("displayName", aqp.getDisplayName());
                parameters.put(parameter);
            }
            result.put("parameters", parameters);

            results.put(result);
        }
        jsonBody.put("results", results);

        return jsonBody;
    }

    private List<AirQuality> getTestListOfAirQuality(){
        List<AirQuality> allAqs = new ArrayList<AirQuality>();
        AirQuality aq = new AirQuality();
        aq.setId(44654);
        aq.setName("Faimront MN");
        aq.setCountry("US");
        aq.setCoordinates(new AirQualityCooredinates(36.1335,-122.443));
        aq.getParameters().add(new AirQualityParameter(51902, "um025", "PM2.5 count", "particles/cm2", .02));
        aq.getParameters().add(new AirQualityParameter(51998, "pm1", "PM1", "ug/m3", 1.3));
        allAqs.add(aq);
        return allAqs;
    }
}