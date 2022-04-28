package dp.features.apis.geocode

import com.google.gson.annotations.SerializedName

data class GeocodeResponse(
    val results: List<Result?>,
    @SerializedName("error_message")
    val error: String?,
    val status: String
)

data class Result(
    @SerializedName("address_components")
    val addressComponents: List<AddressComponent?>?,
    val geometry: Geometry?
) {
    /**
     * Returns formatted address of the address_components
     */
    fun formattedAddress(): String? {
        if (addressComponents != null && addressComponents.size > 1) {
            val locality = addressComponents.find { it?.types?.contains("locality") == true }
            val country = addressComponents.find { it?.types?.contains("country") == true }
            return "${locality?.long_name}, ${country?.short_name}"
        }
        return addressComponents?.firstOrNull()?.short_name
    }
}

data class AddressComponent(
    val long_name: String?,
    val short_name: String?,
    val types: List<String?>?
)

data class Geometry(
    val location: Location?
)

data class Location(
    val lat: Double?,
    val lng: Double?,
)
