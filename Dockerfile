FROM openjdk:8-jdk
EXPOSE 8080:8080
RUN mkdir /app
COPY ./build/install/ms-distance/ /app/
WORKDIR /app/bin
CMD ["./ms-distance"]