package services

import com.google.inject.ImplementedBy
import models.IssueMessage

import scalaz.\/


@ImplementedBy(classOf[HashServiceImpl])
trait HashService {

  def generate(input: Int): IssueMessage \/ String
  def reverse(input: String): IssueMessage \/ Int
}


class HashServiceImpl extends HashService {

  def generate(input: Int): IssueMessage \/ String = {
    ???
  }

  def reverse(input: String): IssueMessage \/ Int = {
    ???
  }
}