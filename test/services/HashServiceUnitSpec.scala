package services

import org.scalatest.{FlatSpec, Matchers}

import scalaz._
import Scalaz._


/**
  * Created by adam on 09/01/2016.
  */
class HashServiceUnitSpec extends FlatSpec with Matchers {

  val hashService = new HashServiceImpl()

  "HashService" should "create a hash and then reverse it back to original" in {
    val before = 100

    val \/-(generated) = hashService.generate(before)
    val \/-(after) = hashService.reverse(generated)

    before should === (after)
  }

  it should "generate 'e9a' from the number 19158" in {
    hashService.generate(19158) should === ("e9a".right)
  }

  it should "reverse 'cb' to get 125" in {
    hashService.reverse("cb") should === (125.right)
  }

  it should "generate a large hash from a large number" in {
    hashService.generate(234234532) should === ("p0Zgu".right)
  }

  it should "reverse a large hash to get a large number" in {
    hashService.reverse("9ekay") should === (902348272.right)
  }
}
