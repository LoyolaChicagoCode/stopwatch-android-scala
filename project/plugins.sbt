// Android development and IntelliJ IDEA integration

addSbtPlugin("com.hanhuy.sbt" % "android-sdk-plugin" % "1.3.13")

// Scoverage and Coveralls

resolvers += Resolver.sbtPluginRepo("snapshots")

// Do not update until sbt-scoverage 1.0 stabilizes!

addSbtPlugin("org.scoverage" %% "sbt-scoverage" % "0.99.7.1")

addSbtPlugin("org.scoverage" %% "sbt-coveralls" % "0.99.0")