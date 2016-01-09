package models


case class Id[V, T](value: V) extends AnyVal {
  override def toString = value.toString
  def as[T2] = Id[V, T2](value)
}