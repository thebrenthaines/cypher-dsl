package me.manishkatoch.scala.cypherDSL.spec.clauses
import me.manishkatoch.scala.cypherDSL.spec.Context

private[spec] trait Clause {
  def toQuery(context: Context): String
}
