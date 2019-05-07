
import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class RecordedSimulation extends Simulation {

	val httpProtocol = http
		.baseUrl("http://localhost:3013")
		.disableFollowRedirect
		.disableAutoReferer
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("fr,fr-FR;q=0.8,en-US;q=0.5,en;q=0.3")
		.upgradeInsecureRequestsHeader("1")
		.userAgentHeader("Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:66.0) Gecko/20100101 Firefox/66.0")

	val headers_0 = Map("Cache-Control" -> "max-age=0")



	val scn = scenario("RecordedSimulation")
		.exec(http("RecordedSimulation_0")
			.get("/spark-mg-ms/api/v1/spark-manager/func/sparkStats")
			.headers(headers_0)
			)

  setUp(
    scn.inject(
      incrementUsersPerSec(20) // Double
        .times(5)
        .eachLevelLasting(2 seconds)
        .separatedByRampsLasting(2 seconds)
        .startingFrom(10) // Double
    )
  ).protocols(httpProtocol)

}
