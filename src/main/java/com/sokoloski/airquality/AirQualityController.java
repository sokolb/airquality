package com.sokoloski.airquality;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AirQualityController {
    @Autowired
    OpenAqDataSource openAqDataSource;

    @GetMapping(value = "/healthCheck")
    public ResponseEntity<String> healthCheck(){
        return new ResponseEntity<String>("Healthy", HttpStatus.OK);
    }

    @GetMapping(value = "/airQualityByCountryAndMeasuredParam")
    public ResponseEntity<String> airQualityByCountryAndMeasuredParam(@RequestParam("country") String country, @RequestParam("measuredParam") String measuredParam) throws JSONException, JsonProcessingException {
        ResponseEntity<String> response = null;
        try{
            List<AirQuality> allAirQuality = openAqDataSource.getByCountryAndMeasuredParam(country, measuredParam);

            ObjectMapper mapper = new ObjectMapper();
            String actualString = mapper.writeValueAsString(allAirQuality);
            JSONArray actualAirQualities = new JSONArray(actualString);

            response = new ResponseEntity<String>(actualAirQualities.toString(), HttpStatus.OK);
        }
        catch(JSONException ex){
            response = new ResponseEntity<String>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }
}
