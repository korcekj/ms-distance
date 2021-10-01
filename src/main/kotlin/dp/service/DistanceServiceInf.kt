package dp.service

import dp.model.Distance

interface DistanceServiceInf {
    fun createDistance(from: String, to: String): Distance?
    fun getDistance(from: String, to: String): Distance?
}