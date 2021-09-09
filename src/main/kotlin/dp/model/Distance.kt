package dp.model

data class Distance(
    val from: Place,
    val to: Place,
    val distance: Int?,
    val duration: Int?
)
