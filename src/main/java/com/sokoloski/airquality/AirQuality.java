package com.sokoloski.airquality;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AirQuality {
    private int id;
    private String name;
    private String country;
    private AirQualityCooredinates coordinates;
    private List<AirQualityParameter> parameters = new ArrayList<AirQualityParameter>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public AirQualityCooredinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(AirQualityCooredinates coordinates) {
        this.coordinates = coordinates;
    }

    public List<AirQualityParameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<AirQualityParameter> parameters) {
        this.parameters = parameters;
    }
}

