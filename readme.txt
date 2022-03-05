This Java spring boot project uses JDK 8, maven, and docker.
To deploy the project, run the following commands from the project's root directory:
1.  mvn clean install
2.  docker build . -t airquality:latest
3.  docker run -p 8080:8080 airquality:latest

You can now access the app via http://localhost:8080

