package services

import org.scalatest.{FlatSpec, Matchers}

import scalaz._


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
}
