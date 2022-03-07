package com.sokoloski.airquality;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class OpenAqDataSourceTest {

    private static final String locationUrl = "https://docs.openaq.org/v2/locations?limit=10";

    @Mock
    RestTemplate restTemplate;

    @InjectMocks
    @Autowired
    private OpenAqDataSource testObject;

    @Before
    public void init() {
        ResponseEntity<String> response = new ResponseEntity<String>("", HttpStatus.OK);

        Mockito.when(restTemplate.exchange(
                        ArgumentMatchers.anyString(),
                        ArgumentMatchers.any(HttpMethod.class),
                        ArgumentMatchers.any(),
                        ArgumentMatchers.<Class<String>>any()))
                .thenReturn(response);
    }

    @Test
    public void getByCountryAndMeasuredParam_CallsOpenAqLocationsUrl() {
        String countryCode = "US";
        testObject.getByCountryAndMeasuredParam(countryCode,"pm25");

        verify(restTemplate, times(1)).exchange(
                eq(locationUrl + "&country_id=" + countryCode),
                eq(HttpMethod.GET),
                eq(null),
                eq(String.class));
    }

    @Test
    public void getByCountryAndMeasuredParam_ReturnEmptyListWhenAqLocationsDoesntReturn200(){
        String countryCode = "US";
        ResponseEntity<String> response = new ResponseEntity<String>("", HttpStatus.INTERNAL_SERVER_ERROR);

        Mockito.when(restTemplate.exchange(
                        eq(locationUrl + "&country_id=" + countryCode),
                        ArgumentMatchers.any(HttpMethod.class),
                        ArgumentMatchers.any(),
                        ArgumentMatchers.<Class<String>>any()))
                .thenReturn(response);

        List<AirQuality> retval = testObject.getByCountryAndMeasuredParam(countryCode,"pm25");

        Assert.assertEquals(null, retval);
    }
}