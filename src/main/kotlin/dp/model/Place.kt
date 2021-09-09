package dp.model

import me.piruin.geok.LatLng

data class Place(
    val address: String,
    val lat: Double?,
    val lng: Double?,
) {
    // Flight duration (s)
    fun airplaneDurationTo(toPlace: Place): Int? {
        val distance = airPlaneDistanceTo(toPlace)

        if (distance == null) return null

        return (distance / (800 * 0.27778)).toInt()
    }
    // Distance in (m)
    fun airPlaneDistanceTo(toPlace: Place): Int? {
        val (_, toLat, toLng) = toPlace

        if (
            lat == null ||
            lng == null ||
            toLat == null ||
            toLng == null
        ) return null

        return LatLng(lat, lng).distanceTo(LatLng(toLat, toLng)).toInt()
    }
}
