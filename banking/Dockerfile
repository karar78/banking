FROM openjdk:8
ADD target/docker-fractal.jar docker-fractal.jar
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "docker-fractal.jar"]