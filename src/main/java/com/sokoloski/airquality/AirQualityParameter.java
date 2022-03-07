package com.sokoloski.airquality;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AirQualityParameter {
    private int id;
    private String parameter;
    private String displayName;
    private String unit;
    private double lastValue;

    public AirQualityParameter(){

    }

    public AirQualityParameter(int id, String parameter, String displayName, String unit, double lastValue) {
        this.id = id;
        this.parameter = parameter;
        this.displayName = displayName;
        this.unit = unit;
        this.lastValue = lastValue;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double getLastValue() {
        return lastValue;
    }

    public void setLastValue(double lastValue) {
        this.lastValue = lastValue;
    }
}
