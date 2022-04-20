package dp.features.apis.ipwhois

data class IpwhoisResponse(
    val city: String?,
    val country_code: String?,
    val latitude: Double?,
    val longitude: Double?,
    val message: String?,
    val success: Boolean
)
