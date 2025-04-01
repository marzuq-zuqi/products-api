@echo off
REM Install Java 17
winget install --id Amazon.Corretto.17
REM Install Maven
winget install --id Apache.Maven

REM Build the Spring Boot app
mvn clean package -DskipTests
