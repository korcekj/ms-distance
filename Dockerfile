FROM openjdk:8-jdk as build
COPY . /app
WORKDIR /app
RUN ./gradlew --no-daemon installDist

FROM openjdk:8-jre
EXPOSE 8080:8080
COPY --from=build /app/build/install/ms-distance/ /app/
WORKDIR /app/bin
CMD ["./ms-distance"]