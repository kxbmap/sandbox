import com.twitter.util.Eval
import scala.tools.nsc.io.Path

trait ConfigReader {
  def read[A, B](path: Path): Map[A, B] = {
    val ev = new Eval()
    (Map.empty[A, B] /: path.walk) { case (m, p) =>
      p.ifFile(f => ev[Map[A, B]](f.inputStream())).map(m ++) getOrElse m
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
