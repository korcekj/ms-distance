# DISTANCE API (Microservice)

## 1. Get the minimum time distance by specifying the location

### Request

`GET /address?from=Slovakia&to=Russia`

### Response

```json
{
  "from": {
    "address": "Slovakia",
    "lat": 48.669026,
    "lng": 19.699024
  },
  "to": {
    "address": "Russia",
    "lat": 61.52401,
    "lng": 105.318756
  },
  "distance": 5224325,
  "duration": 23509
}
```

## 2. Get the minimum time distance by specifying the ip address

### Request

`GET /ip?from=85.198.185.26&to=188.133.153.161`

### Response

```json
{
  "from": {
    "address": "Dnipro",
    "lat": 48.464717,
    "lng": 35.046183
  },
  "to": {
    "address": "Moscow",
    "lat": 55.755826,
    "lng": 37.6173
  },
  "distance": 830024,
  "duration": 3735
}
```