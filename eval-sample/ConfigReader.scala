import com.twitter.util.Eval
import scala.tools.nsc.io.Path

trait ConfigReader {
  def read[A, B](path: Path): Map[A, B] = {
    val ev = new Eval()
    (Map.empty[A, B] /: path.walkFilter(_.isFile)) {
      case (m, p) => m ++ ev[Map[A, B]](p.toFile.inputStream())
    }
  }

  def localize[A, B](base: Map[A, B], path: Path): Map[A, B] = {
    val loc = read[A, B => B](path)
    for ((i, b) <- base) yield i -> (loc.get(i).map(_(b)) getOrElse b)
  }
}

object Main extends ConfigReader {
  def main(args: Array[String]) {
    val ws = read[Int, Weapon](Path("config") / "weapons")
    println(ws)

    val lws = localize(ws, Path("localize") / "weapons.scala")
    println(lws)
  }
}
