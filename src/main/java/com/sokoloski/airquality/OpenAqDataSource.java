package com.sokoloski.airquality;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class OpenAqDataSource implements AirQualityDataSource{

    private RestTemplate restTemplate = new RestTemplate();
    private final String locationUrlForCountryCode = "https://docs.openaq.org/v2/locations?limit=10";
    private final String locationUrlForCoordinates = "https://docs.openaq.org/v2/locations?limit=10&radius=1";


    @Override
    public List<AirQuality> getByCountryAndMeasuredParam(String countryCode, String measuredParam) throws JSONException {
        List<AirQuality> retval = new ArrayList<AirQuality>();

        String targetUrl = String.format(locationUrlForCountryCode + "&country_id=%s", countryCode);
        ResponseEntity<String> response = restTemplate.exchange(targetUrl, HttpMethod.GET, null, String.class);
        if (!response.getStatusCode().equals(HttpStatus.OK)) {
            retval = null;
        }else{
            ObjectMapper objectMapper = new ObjectMapper();

            JSONObject body = null;
            try {
                body = new JSONObject(response.getBody());
                JSONArray results = body.getJSONArray("results");

                for(int i = 0; i < results.length(); i++){
                    JSONObject result = results.getJSONObject(i);
                    try {
                        AirQuality aq = objectMapper.readValue(result.toString(), AirQuality.class);
                        aq.getParameters().removeIf(p -> !p.getParameter().equals(measuredParam));

                        retval.add(aq);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                }
            } catch (JSONException e) {
                throw new JSONException("Result has invalid JSON " + e);
            }
        }

        return retval;
    }

    @Override
    public List<AirQuality> getByCoordinatesAndMeasuredParam(double latitude, double longitude, String measuredParam) {
        List<AirQuality> retval = new ArrayList<AirQuality>();

        String targetUrl = String.format(locationUrlForCoordinates + "&coordinates=" + latitude + "," + longitude);
        ResponseEntity<String> response = restTemplate.exchange(targetUrl, HttpMethod.GET, null, String.class);
        if (!response.getStatusCode().equals(HttpStatus.OK)) {
            retval = null;
        } else {

        }
        return retval;
    }
}
