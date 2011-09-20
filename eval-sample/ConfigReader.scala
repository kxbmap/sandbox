import com.twitter.util.Eval
import scala.tools.nsc.io.Path

trait ConfigReader {
  def read[A, B](path: Path): Map[A, B] = {
    val ev = new Eval()
    (Map.empty[A, B] /: path.walkFilter(_.isFile)) {
      case (m, p) => m ++ ev[Map[A, B]](p.toFile.inputStream())
    }
  }
}

object Main extends ConfigReader {
  def main(args: Array[String]) {
    val ws = read[Int, Weapon](Path("config") / "weapons")
    println(ws)

    val loc = read[Int, Weapon => Weapon](Path("localize") / "weapons.scala")
    val lws = for ((i, w) <- ws) yield i -> (loc.get(i).map(_(w)) getOrElse w)
    println(lws)
  }
}
