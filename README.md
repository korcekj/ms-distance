# DISTANCE API (Microservice)

## Get the minimum time distance

### Request

`GET /distance?from=Slovakia&to=Russia`

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