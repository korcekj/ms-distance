package dp

import kotlin.test.*

import org.litote.kmongo.json
import io.ktor.server.testing.*
import io.ktor.application.*
import io.ktor.http.*

import dp.model.*

class ApplicationTest {
    /**
     * Verifies that the Distance routes returns HTTP 400 status
     */
    @Test
    fun testDistanceRoutesWithoutParams() = withTestApplication(Application::module) {
        with(handleRequest(HttpMethod.Get, "/address")) {
            assertEquals(HttpStatusCode.BadRequest, response.status())
            assertEquals("Distance can't be calculated", response.content)
        }
        with(handleRequest(HttpMethod.Get, "/ip")) {
            assertEquals(HttpStatusCode.BadRequest, response.status())
            assertEquals("Distance can't be calculated", response.content)
        }
    }

    /**
     * Verifies that the Address route returns JSON properly formatted and
     * the minimal distance along with the duration will be calculated
     */
    @Test
    fun testAddressWithParams() = withTestApplication(Application::module) {
        with(handleRequest(HttpMethod.Get, "/address?from=Slovakia&to=Russia")) {
            val distance = Distance(
                from = Place(address = "Slovakia", lat = 48.669026, lng = 19.699024),
                to = Place(address = "Russia", lat = 61.52401, lng = 105.318756),
                distance = 5224325,
                duration = 23509
            ).json

            assertEquals(HttpStatusCode.OK, response.status())
            assertEquals(distance, response.content)
        }
    }

    /**
     * Verifies that the Ip route returns JSON properly formatted and
     * the minimal distance along with the duration will be calculated
     */
    @Test
    fun testIpWithParams() = withTestApplication(Application::module) {
        with(handleRequest(HttpMethod.Get, "/ip?from=85.198.185.26&to=188.133.153.161")) {
            val distance = Distance(
                from = Place(address = "Dnipro", lat = 48.464717, lng = 35.046183),
                to = Place(address = "Moscow", lat = 55.755826, lng = 37.6173),
                distance = 830024,
                duration = 3735
            ).json

            assertEquals(HttpStatusCode.OK, response.status())
            assertEquals(distance, response.content)
        }
    }
}