
organization in ThisBuild := "com.iheart"

resolvers +=  Resolver.bintrayRepo("scalaz", "releases")


lazy val noPublishSettings = Seq(
  skip in publish := true
)

lazy val root = project.in(file("."))
  .aggregate(playSwagger, sbtPlaySwagger)
  .settings(sourcesInBase := false)
  .settings(noPublishSettings ++ commonSettings)

lazy val playSwagger = project.in(file("core"))
  .settings(Publish.coreSettings ++ Format.settings ++ Testing.settings ++ commonSettings)
  .settings(
    name := "play-swagger",
    libraryDependencies ++= Dependencies.playTest ++
      Dependencies.playRoutesCompiler ++
      Dependencies.playJson ++
      Dependencies.test ++
      Dependencies.yaml,
    scalaVersion := "2.12.4",
    crossScalaVersions := Seq( "2.11.12", scalaVersion.value),
    releaseCrossBuild := true
  )

lazy val sbtPlaySwagger = project.in(file("sbtPlugin"))
  .settings(Publish.sbtPluginSettings ++ Format.settings)
  .settings(
    addSbtPlugin("com.typesafe.sbt" %% "sbt-native-packager" % "1.3.1" % Provided),
    addSbtPlugin("com.typesafe.sbt" %% "sbt-web" % "1.4.3" % Provided))
  .enablePlugins(BuildInfoPlugin)
  .settings(
    buildInfoKeys := Seq[BuildInfoKey](name, version),
    buildInfoPackage := "com.iheart.playSwagger",
    name := "sbt-play-swagger",
    description := "sbt plugin for play swagger spec generation",
    sbtPlugin := true,
    scalaVersion := "2.12.4",
    scripted := scripted.dependsOn(publishLocal in playSwagger).evaluated,

    scriptedLaunchOpts := { scriptedLaunchOpts.value ++
      Seq("-Xmx1024M", "-Dplugin.version=" + version.value)
    },
    scriptedBufferLog := false
  )

