# Stage 1: build
# Start with a maven image that includes JDK 21
FROM maven:3.9.9-amazoncorretto-21 AS build

# Copy source code and pom.xml file to /app folder
# Container như một ngôi nhà ảo
# /app như một căn phòng trong ngôi nhà đó
WORKDIR /app
COPY pom.xml .
# ./src nghĩa là "tạo thư mục src trong thư mục hiện tại(/app) của container"
COPY src ./src

# Build source code with maven
RUN mvn package -DskipTests

#Stage 2: create image
# Start with Amazon Correcto JDK 21
FROM amazoncorretto:21.0.5

# Set working folder to App and copy compiled file from above step
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]