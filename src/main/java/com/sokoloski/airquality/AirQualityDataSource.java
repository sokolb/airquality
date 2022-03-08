package com.sokoloski.airquality;

import org.json.JSONException;

import java.util.List;

public interface AirQualityDataSource {
    public List<AirQuality> getByCountryAndMeasuredParam(String countryCode, String measuredParam) throws JSONException;
    public List<AirQuality> getByCoordinatesAndMeasuredParam(double latitude, double longitude, String measuredParam);
}
