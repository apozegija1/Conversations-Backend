FROM adoptopenjdk/openjdk14:alpine
MAINTAINER Dzejlan Muharemovic <dmuharemov1@etf.unsa.ba>

# Add the service itself
ARG JAR_FILE
ADD target/${JAR_FILE} /usr/share/jwt-spring-security-demo.jar

ENTRYPOINT ["java", "-jar", "/usr/share/jwt-spring-security-demo.jar"]
