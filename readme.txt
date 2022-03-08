This Java spring boot project uses JDK 8, maven, and docker.
To deploy the project, run the following commands from the project's root directory:
1.  mvn clean install
2.  docker build . -t airquality:latest
3.  docker run -p 8080:8080 airquality:latest

You can now access the app via http://localhost:8080
Please refer to the "API Definitions.html" for documentation on the APIs.

Example URLs:
http://localhost:8080/healthCheck
http://localhost:8080/airQualityByCountryAndMeasuredParam?country=US&measuredParam=um025
http://localhost:8080/airQualityByCoordinatesAndRadiusAndMeasuredParam?latitude=33.999504&longitude=-117.41602&radius=100&measuredParam=um025

Data from these endpoints can be consumed and used to create a Heat Map.  The 'lastValue' of the AirQualityParameter would be the data point of interest,
along with AirQualityCoordinate data for finding the exact location.
