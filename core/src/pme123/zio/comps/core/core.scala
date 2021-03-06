package pme123.zio.comps

package object core {
  val renderOutputPrefix = "RENDER:"

  case class Sensitive(value: String) extends AnyVal {
    override def toString: String = "*" * 20
  }

  sealed trait Component {
    def name: String
  }
  case class DbConnection(
                           name: String,
                           url: String,
                           user: String,
                           password: Sensitive
                         ) extends Component

  case class DbLookup(
                       name: String,
                       dbConRef: CompRef,
                       statement: String,
                       params: Map[String, String]
                     ) extends Component

  case class MessageBundle(
                            name: String,
                            params: Map[String, String]
                          ) extends Component

  sealed trait CompRef {
    def url: String
  }
  case class LocalRef(name: String) extends CompRef {
    val url: String = name
  }
  case class RemoteRef(name: String, pckg: String) extends CompRef {
    val url = s"dependencies/$pckg/$name"
  }

}
