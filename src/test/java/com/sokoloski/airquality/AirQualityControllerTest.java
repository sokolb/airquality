package com.sokoloski.airquality;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AirQualityController.class)
public class AirQualityControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    OpenAqDataSource mockOpenAqDataSource;

    @Test
    public void healthCheck_ReturnsHealthyString() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get("/healthCheck")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn();

        Assert.assertEquals("Healthy", result.getResponse().getContentAsString());
    }

    @Test
    public void airQualityByCountryAndMeasuredParam_ReturnsListOfAirQuality() throws Exception {
        String country = "US";
        String measuredParam = "um025";
        List<AirQuality> expected = getTestListOfAirQuality();

        when(mockOpenAqDataSource.getByCountryAndMeasuredParam(country, measuredParam)).thenReturn(expected);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get("/airQualityByCountryAndMeasuredParam?country=" + country + "&measuredParam=" + measuredParam)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn();

        ObjectMapper mapper = new ObjectMapper();
        String expectedString = mapper.writeValueAsString(expected);
        JSONArray expectedAirQualities = new JSONArray(expectedString);

        Assert.assertTrue(result.getResponse().getContentAsString().contains(expectedAirQualities.toString()));
    }

    @Test
    public void airQualityByCountryAndMeasuredParam_ReturnsEmptyListWhenQueryHasNoResults() throws Exception {
        String country = "US";
        String measuredParam = "um025";
        List<AirQuality> expected = new ArrayList<AirQuality>();

        when(mockOpenAqDataSource.getByCountryAndMeasuredParam(country, measuredParam)).thenReturn(expected);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get("/airQualityByCountryAndMeasuredParam?country=" + country + "&measuredParam=" + measuredParam)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        ObjectMapper mapper = new ObjectMapper();
        String expectedString = mapper.writeValueAsString(expected);
        JSONArray expectedAirQualities = new JSONArray(expectedString);

        Assert.assertTrue(result.getResponse().getContentAsString().contains(expectedAirQualities.toString()));
    }

    @Test
    public void airQualityByCountryAndMeasuredParam_ReturnsErrorMessageOnFailedQuery() throws Exception {
        String country = "US";
        String measuredParam = "um025";
        String errorMessage = "We had an error.  Could not parse JSON result";

        when(mockOpenAqDataSource.getByCountryAndMeasuredParam(country, measuredParam)).thenThrow(new JSONException(errorMessage));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get("/airQualityByCountryAndMeasuredParam?country=" + country + "&measuredParam=" + measuredParam)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError())
                .andReturn();

        Assert.assertTrue(result.getResponse().getContentAsString().contains(errorMessage));
    }

    @Test
    public void airQualityByCoordinatesAndRadiusAndMeasuredParam_ReturnsListOfAirQuality() throws Exception {
        double latitude = 34.11;
        double longitude = -122.021;
        int radius = 5;
        String measuredParam = "um025";
        List<AirQuality> expected = getTestListOfAirQuality();

        when(mockOpenAqDataSource.getByCoordinatesAndMeasuredParam(latitude, longitude, radius, measuredParam)).thenReturn(expected);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get("/airQualityByCoordinatesAndRadiusAndMeasuredParam?latitude=" + latitude + "&longitude=" + longitude + "&radius=" + radius + "&measuredParam=" + measuredParam)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        ObjectMapper mapper = new ObjectMapper();
        String expectedString = mapper.writeValueAsString(expected);
        JSONArray expectedAirQualities = new JSONArray(expectedString);

        Assert.assertTrue(result.getResponse().getContentAsString().contains(expectedAirQualities.toString()));
    }

    @Test
    public void airQualityByCoordinatesAndRadiusAndMeasuredParam_ReturnsEmptyListWhenQueryHasNoResults() throws Exception {
        double latitude = 34.11;
        double longitude = -122.021;
        int radius = 5;
        String measuredParam = "um025";
        List<AirQuality> expected = new ArrayList<AirQuality>();

        when(mockOpenAqDataSource.getByCoordinatesAndMeasuredParam(latitude, longitude, radius, measuredParam)).thenReturn(expected);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get("/airQualityByCoordinatesAndRadiusAndMeasuredParam?latitude=" + latitude + "&longitude=" + longitude + "&radius=" + radius + "&measuredParam=" + measuredParam)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        ObjectMapper mapper = new ObjectMapper();
        String expectedString = mapper.writeValueAsString(expected);
        JSONArray expectedAirQualities = new JSONArray(expectedString);

        Assert.assertTrue(result.getResponse().getContentAsString().contains(expectedAirQualities.toString()));
    }

    @Test
    public void airQualityByCoordinatesAndRadiusAndMeasuredParam_ReturnsErrorMessageOnFailedQuery() throws Exception {
        double latitude = 34.11;
        double longitude = -122.021;
        int radius = 5;
        String measuredParam = "um025";
        String errorMessage = "We had an error.  Could not parse JSON result";

        when(mockOpenAqDataSource.getByCoordinatesAndMeasuredParam(latitude, longitude, radius, measuredParam)).thenThrow(new JSONException(errorMessage));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get("/airQualityByCoordinatesAndRadiusAndMeasuredParam?latitude=" + latitude + "&longitude=" + longitude + "&radius=" + radius + "&measuredParam=" + measuredParam)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError())
                .andReturn();

        Assert.assertTrue(result.getResponse().getContentAsString().contains(errorMessage));
    }

    private List<AirQuality> getTestListOfAirQuality(){
        List<AirQuality> allAqs = new ArrayList<AirQuality>();

        AirQuality aq = new AirQuality();
        aq.setId(44654);
        aq.setName("Faimront MN");
        aq.setCountry("US");
        aq.setCoordinates(new AirQualityCooredinates(36.1335,-122.443));
        aq.getParameters().add(new AirQualityParameter(51998, "pm1", "PM1", "ug/m3", 1.3));
        allAqs.add(aq);

        aq = new AirQuality();
        aq.setId(44699);
        aq.setName("Sherburn MN");
        aq.setCountry("US");
        aq.setCoordinates(new AirQualityCooredinates(35.123,-124.444));
        aq.getParameters().add(new AirQualityParameter(51998, "pm1", "PM1", "ug/m3", 1.3));
        allAqs.add(aq);

        return allAqs;
    }
}