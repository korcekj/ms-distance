package dp.features.apis.geocode

import com.google.gson.annotations.SerializedName

data class GeocodeResponse(
    val results: List<Result?>,
    @SerializedName("error_message")
    val error: String?,
    val status: String
)

data class Result(
    @SerializedName("formatted_address")
    val formattedAddress: String?,
    val geometry: Geometry?
)

data class Geometry(
    val location: Location?
)

data class Location (
    val lat: Double?,
    val lng: Double?,
)
