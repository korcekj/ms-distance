package dp

import kotlin.test.*

import org.litote.kmongo.json
import io.ktor.server.testing.*
import io.ktor.application.*
import io.ktor.http.*

import dp.model.*

class ApplicationTest {
    /**
     * Verifies that the Distance route returns HTTP 400 status
     */
    @Test
    fun testDistanceWithoutParams() = withTestApplication(Application::module) {
        with(handleRequest(HttpMethod.Get, "/address")) {
            assertEquals(HttpStatusCode.BadRequest, response.status())
            assertEquals("Distance can't be calculated", response.content)
        }
    }

    /**
     * Verifies that the Distance route returns JSON properly formatted and
     * the minimal distance along with the duration will be calculated
     */
    @Test
    fun testDistanceWithParams() = withTestApplication(Application::module) {
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
}