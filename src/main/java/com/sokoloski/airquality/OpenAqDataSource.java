package com.sokoloski.airquality;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Service
public class OpenAqDataSource implements AirQualityDataSource{

    private RestTemplate restTemplate = new RestTemplate();

    private final String locationUrl = "https://docs.openaq.org/v2/locations?limit=10";

    @Override
    public List<AirQuality> getByCountryAndMeasuredParam(String countryCode, String measuredParam) {
        List<AirQuality> retval = new ArrayList<AirQuality>();

        String targetUrl = String.format(locationUrl + "&country_id=%s", countryCode);
        ResponseEntity<String> response = restTemplate.exchange(targetUrl, HttpMethod.GET, null, String.class);
        if (!response.getStatusCode().equals(HttpStatus.OK)) {
            retval = null;
        }

        return retval;
    }

    @Override
    public List<AirQuality> getByCoordinatesAndMeasuredParam(String coordinates, String measuredParam) {
        return null;
    }
}
