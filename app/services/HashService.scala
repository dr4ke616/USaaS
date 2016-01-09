package services

import models._

import com.google.inject.ImplementedBy

import scala.math
import scala.util.Try
import scala.util.control.NonFatal
import scala.annotation.tailrec

import scalaz._
import Scalaz._


@ImplementedBy(classOf[HashServiceImpl])
trait HashService {

  def generate(input: Long): IssueMessage \/ String
  def reverse(input: String): IssueMessage \/ Long
}


class HashServiceImpl extends HashService {

  private val Alphabet = ('a' to 'z') ++ ('A' to 'Z') ++ ('0' to '9')
  private val Base = Alphabet.size

  def generate(input: Long): IssueMessage \/ String = {

    @tailrec
    def recr(num: Long, digits: Seq[Long]): Seq[Long] = {
      if (num == 0) {
        digits.reverse
      } else {
        digits match {
          case Nil => recr(num / Base, Seq(num % Base))
          case x => recr(num / Base, x :+ num % Base)
        }
      }
    }

    Try {
      recr(input, Nil).map(v => Alphabet(v.toInt)).mkString.right[IssueMessage]
    }.recover {
      case NonFatal(err) =>
        "There was a problem generating uri hash".id[Issue].left
    }.get
  }

  def reverse(input: String): IssueMessage \/ Long = {
    Try {
      val list: Seq[Int] = input.toCharArray.map(Alphabet.indexOf).toSeq
      list.zip((list.length - 1).to(0, -1)).map {
        case (value, idx) =>
          (value * math.pow(Base, idx)).toLong
      }.sum.right[IssueMessage]
    }.recover {
      case NonFatal(err) =>
        "There was a problem reversing uri hash".id[Issue].left
    }.get
  }
}