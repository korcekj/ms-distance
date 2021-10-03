package dp.features.apis.ipwhois

data class IpwhoisReponse(
    val city: String?,
    val latitude: Double?,
    val longitude: Double?,
    val message: String?,
    val success: Boolean
)
