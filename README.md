# DISTANCE API (Microservices)

## Installation

### Docker

#### 1. Define image name and environment variables

```yml
    version: "3.9"
    services:
      api:
        image: korcekj/ms-distance:<tag>
        ports:
          - "8080:8080"
        environment:
          - MONGO_URI=<mongodb uri>
          - MONGO_DB=<mongodb name>
          - DM_API_KEY=<distancematrix api key>
          - GC_API_KEY=<geocode api key>
```

#### 2. Compose container

```bash
  $ docker compose up -d
```

## Get the minimum time distance by specifying the location

### Request

`GET /address?from=Fort%20Washakie%20WY&to=Titusville%20FL`

### Response

```json
    {
      "from": {
        "address": "Fort Washakie, WY, USA",
        "lat": 43.006346,
        "lng": -108.882346
      },
      "to": {
        "address": "Titusville, FL, USA",
        "lat": 28.6122187,
        "lng": -80.8075537
      },
      "distance": 2976117,
      "duration": 13392
    }
```

## Get the minimum time distance by specifying the ip address

### Request

`GET /ip?from=74.81.177.208&to=68.205.155.14`

### Response

```json
    {
      "from": {
        "address": "Fort Washakie, United States",
        "lat": 43.006346,
        "lng": -108.882346
      },
      "to": {
        "address": "Titusville, United States",
        "lat": 28.6122187,
        "lng": -80.8075537
      },
      "distance": 2976117,
      "duration": 13392
    }
```