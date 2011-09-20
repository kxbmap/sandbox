

scalaVersion := "2.8.1"

resolvers += "twitter.com" at "http://maven.twttr.com/"

libraryDependencies ++= {
  def twttr(a: String) = "com.twitter" % a % "1.11.4"
  Seq(
    twttr("util-core"),
    twttr("util-eval")
  )
}
