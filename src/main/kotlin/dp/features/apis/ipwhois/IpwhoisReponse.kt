package dp.features.apis.ipwhois

import com.google.gson.annotations.SerializedName

data class IpwhoisReponse(
    val city: String?,
    @SerializedName("country_code")
    val countryCode: String?,
    val latitude: Double?,
    val longitude: Double?,
    val message: String?,
    val success: Boolean
)
