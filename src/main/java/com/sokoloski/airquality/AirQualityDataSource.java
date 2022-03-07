package com.sokoloski.airquality;

import java.util.List;

public interface AirQualityDataSource {
    public List<AirQuality> getByCountryAndMeasuredParam(String countryCode, String measuredParam);
    public List<AirQuality> getByCoordinatesAndMeasuredParam(String coordinates, String measuredParam);
}
