#
# Build stage
#
FROM maven:3.8.3-openjdk-11 AS build
COPY . .
RUN mvn clean package -DskipTests

#
# Package stage
#
FROM openjdk:17.0.1-jdk-slim
ENV TZ=Asia/Ho_Chi_Minh
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
COPY --from=build /target/meolaptrinh-3.0.jar demo.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","demo.jar"]
