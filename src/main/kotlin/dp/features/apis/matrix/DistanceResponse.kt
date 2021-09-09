package dp.features.apis.matrix

import com.google.gson.annotations.SerializedName

data class DistanceResponse(
    @SerializedName("origin_addresses")
    val origins: List<String>,
    @SerializedName("destination_addresses")
    val destinations: List<String>,
    val rows: List<Elements?>?,
    @SerializedName("error_message")
    val error: String?,
    val status: String
)

data class Elements(
    val elements: List<Element?>?
)

data class Element (
    val distance: Property?,
    val duration: Property?,
    val status: String
)

data class Property(
    val text: String,
    val value: Int
)
