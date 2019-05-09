package com.agrim.scala.cypherDSL

import com.agrim.scala.cypherDSL.spec._
import com.agrim.scala.cypherDSL.spec.implicits.QueryProvider
import shapeless.ops.hlist.ToTraversable
import shapeless.{HList, HNil}

object syntax {

  object v1 {
    def cypher = Statement()

    implicit class RichStatement(statement: Statement) {
      def MATCH[T <: Product](element: T)(implicit queryProvider: QueryProvider[T]): Statement = {
        statement.copy(readingClause = statement.readingClause :+ Matches(element))
      }

      def MATCH[T <: Product, TH <: HList](element: Node[T, TH])(implicit queryProvider: QueryProvider[T],
                                                                 i0: ToTraversable.Aux[TH, List, Symbol]): Statement = {
        statement.copy(readingClause = statement.readingClause :+ Matches(element))
      }

      def MATCH(element: Path): Statement = {
        statement.copy(readingClause = statement.readingClause :+ Matches(element))
      }

      def RETURN[T <: Product](elements: T*): Statement = {
        statement.copy(returnClause = Returns(elements: _*))
      }
    }
  }

  implicit class RichProduct[T <: Product, TH <: HList](element: T) {
    def --[U <: Product, UH <: HList](rel: U)(implicit qpT: QueryProvider[T], qpU: QueryProvider[U]) =
      new Path(PathLink(None, Node(element, HNil), Some("-")), PathLink(Some("-"), Node(rel, HNil), None))

    def -->[U <: Product, UH <: HList](rel: U)(implicit qpU: QueryProvider[U], qpT: QueryProvider[T]) =
      new Path(PathLink(None, Node(element, HNil), Some("-")), PathLink(Some("->"), Node(rel, HNil), None))

    def <--[U <: Product, UH <: HList](rel: U)(implicit qpU: QueryProvider[U], qpT: QueryProvider[T]) =
      new Path(PathLink(None, Node(element, HNil), Some("<-")), PathLink(Some("-"), Node(rel, HNil), None))

    def <-|[U <: Product, UH <: HList](rel: U)(implicit qpU: QueryProvider[U], qpT: QueryProvider[T]) =
      new Path(PathLink(None, Node(element, HNil), Some("<-")), PathLink(None, Relationship(rel, HNil), None))

    def -|(rel: Path)(implicit qpT: QueryProvider[T]) =
      new Path(PathLink(None, Node(element, HNil), Some("-")))

    def -|[U <: Product, UH <: HList](rel: U)(implicit qpU: QueryProvider[U], qpT: QueryProvider[T]) =
      new Path(PathLink(None, Node(element, HNil), Some("-")), PathLink(None, Relationship(rel, HNil), None))
  }

  implicit class RichNode[T <: Product](element: T) {
    def apply(implicit queryProvider: QueryProvider[T]) = Node(element, HNil)

    def apply(p1: Symbol)(implicit queryProvider: QueryProvider[T]) = Node(element, p1 :: HNil)

    def apply(p1: Symbol, p2: Symbol)(implicit queryProvider: QueryProvider[T]) =
      Node(element, p1 :: p2 :: HNil)

    def apply(p1: Symbol, p2: Symbol, p3: Symbol)(implicit queryProvider: QueryProvider[T]) =
      Node(element, p1 :: p2 :: p3 :: HNil)

    def apply(p1: Symbol, p2: Symbol, p3: Symbol, p4: Symbol)(implicit
                                                              queryProvider: QueryProvider[T]) =
      Node(element, p1 :: p2 :: p3 :: p4 :: HNil)

    def apply(p1: Symbol, p2: Symbol, p3: Symbol, p4: Symbol, p5: Symbol)(implicit
                                                                          queryProvider: QueryProvider[T]) =
      Node(element, p1 :: p2 :: p3 :: p4 :: p5 :: HNil)

    def apply(p1: Symbol, p2: Symbol, p3: Symbol, p4: Symbol, p5: Symbol, p6: Symbol)(implicit
                                                                                      queryProvider: QueryProvider[T]) =
      Node(element, p1 :: p2 :: p3 :: p4 :: p5 :: p6 :: HNil)

    def apply(p1: Symbol, p2: Symbol, p3: Symbol, p4: Symbol, p5: Symbol, p6: Symbol, p7: Symbol)(
        implicit
        queryProvider: QueryProvider[T]) =
      Node(element, p1 :: p2 :: p3 :: p4 :: p5 :: p6 :: p7 :: HNil)

    def apply(p1: Symbol, p2: Symbol, p3: Symbol, p4: Symbol, p5: Symbol, p6: Symbol, p7: Symbol, p8: Symbol)(
        implicit
        queryProvider: QueryProvider[T]) =
      Node(element, p1 :: p2 :: p3 :: p4 :: p5 :: p6 :: p7 :: p8 :: HNil)

    def apply(p1: Symbol,
              p2: Symbol,
              p3: Symbol,
              p4: Symbol,
              p5: Symbol,
              p6: Symbol,
              p7: Symbol,
              p8: Symbol,
              p9: Symbol)(implicit queryProvider: QueryProvider[T]) =
      Node(element, p1 :: p2 :: p3 :: p4 :: p5 :: p6 :: p7 :: p8 :: p9 :: HNil)

    def apply(p1: Symbol,
              p2: Symbol,
              p3: Symbol,
              p4: Symbol,
              p5: Symbol,
              p6: Symbol,
              p7: Symbol,
              p8: Symbol,
              p9: Symbol,
              p10: Symbol)(implicit queryProvider: QueryProvider[T]) =
      Node(element, p1 :: p2 :: p3 :: p4 :: p5 :: p6 :: p7 :: p8 :: p9 :: p10 :: HNil)
  }

  implicit class EnrichedRichNode[T <: Product, TH <: HList](element: Node[T, TH]) {
    def <-|[U <: Product, UH <: HList](rel: U)(implicit queryProvider: QueryProvider[U]) =
      new Path(PathLink(None, element, Some("<-")), PathLink(None, Relationship(rel, HNil), None))

    def <-|[U <: Product, UH <: HList](rel: Node[U, UH])(implicit
                                                         queryProvider: QueryProvider[U],
                                                         i0: ToTraversable.Aux[UH, List, Symbol]) =
      new Path(PathLink(None, element, Some("<-")), PathLink(None, Relationship(rel.element, rel.properties), None))

    def -|[U <: Product, UH <: HList](rel: U)(implicit queryProvider: QueryProvider[U]) =
      new Path(PathLink(None, element, Some("-")), PathLink(None, Relationship(rel, HNil), None))

    def --[U <: Product, UH <: HList](rel: U)(implicit queryProvider: QueryProvider[U]) =
      new Path(PathLink(None, element, Some("-")), PathLink(Some("-"), Node(rel, HNil), None))

    def --[U <: Product, UH <: HList](rel: Node[U, UH])(implicit context: Context) =
      new Path(PathLink(None, element, Some("-")), PathLink(Some("-"), rel, None))

    def -->[U <: Product, UH <: HList](rel: U)(implicit queryProvider: QueryProvider[U]) =
      new Path(PathLink(None, element, Some("-")), PathLink(Some("->"), Node(rel, HNil), None))

    def -->[U <: Product, UH <: HList](rel: Node[U, UH])(implicit context: Context) =
      new Path(PathLink(None, element, Some("-")), PathLink(Some("->"), rel, None))

    def <--[U <: Product, UH <: HList](rel: U)(implicit queryProvider: QueryProvider[U]) =
      new Path(PathLink(None, element, Some("<-")), PathLink(Some("-"), Node(rel, HNil), None))

    def <--[U <: Product, UH <: HList](rel: Node[U, UH])(implicit context: Context) =
      new Path(PathLink(None, element, Some("<-")), PathLink(Some("-"), rel, None))
  }

}
