package neo4s.cypher.dsl.operators

import neo4s.cypher.dsl.{Context, DSLResult}
import neo4s.cypher.dsl.entities.{AliasedProduct, Node, Relationship}
import neo4s.cypher.dsl.utils.ElementPropertyExtractingAndAliasing
import neo4s.cypher.dsl.Utils._

case class Distinct(elements: Product*) extends UnaryOperator with ElementPropertyExtractingAndAliasing {

  private val errorMessage = "DISTINCT operator requires element to be in Context"

  override def toQuery(context: Context): DSLResult = {
    val ids = AliasedProduct.makeAliasedProduct(elements.toList)
      .map(aliasedProduct => {
        val (el, properties) = getElementAndProperties(aliasedProduct.node)
        context
          .get(el)
          .map(identifier => {
            val hasProperties = (aliasedProduct.node match {
              case s: Node[_, _] => s.properties.toList.length
              case s: Relationship[_, _] => s.properties.toList.length
              case s => 0
            }) > 0
            if (aliasedProduct.alias.isDefined && !hasProperties) context.updateLiteral(el, aliasedProduct.alias.get)
            else {
              context.add(aliasedProduct.node)
              if(aliasedProduct.alias.isDefined) context.updateLiteral(aliasedProduct.node, aliasedProduct.alias.get)
            }
            makeAliasedString(identifier, properties, aliasedProduct.alias)
          })
          .getOrElse(throw new NoSuchElementException(errorMessage))
      })
      .mkString(",")

    DSLResult(if (ids.nonEmpty) s"DISTINCT $ids" else "")
  }
}
