
package object models {
  type StringId[T] = Id[String, T]
  type LongId[T] = Id[Long, T]

  type IssueMessage = StringId[Issue]

  def fromId[V, T](id: Id[V, T]): V = id.value
  def asId[V, T](value: V): Id[V, T] = Id(value)

  implicit class IdOps[V](val value: V) extends AnyVal {
    def id[T] = Id[V, T](value)
  }
}