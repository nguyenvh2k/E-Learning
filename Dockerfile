#
# Build stage
#
FROM maven:3.8.3-openjdk-11 AS build
COPY . .
RUN mvn clean package -DskipTests

#
# Package stage
#
FROM adoptopenjdk/openjdk11
ENV TZ=Asia/Ho_Chi_Minh
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
COPY --from=build /target/elearning-1.0.jar demo.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","demo.jar"]
