
import org.scalatest.BeforeAndAfterEach
import org.scalatestplus.play.{PlaySpec, OneServerPerSuite}

import play.api.test._
import play.api.test.Helpers._


class ApplicationSpec extends PlaySpec with OneServerPerSuite with BeforeAndAfterEach {


  "USaaS Application" must {

    "Get 404 on a non existing endpoint" in new WithApplication(FakeApplication()) {
      val response = route(FakeRequest(GET, "/boum")).get
      status(response) must === (NOT_FOUND)
    }
  }
}
