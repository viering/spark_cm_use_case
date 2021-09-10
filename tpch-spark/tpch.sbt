name := "Spark TPC-H Queries"

version := "1.0"
resolvers += Resolver.mavenLocal

scalaVersion := "2.12.10"

libraryDependencies += "org.apache.spark" %% "spark-core" % "3.0.1-session"
libraryDependencies += "org.apache.spark" %% "spark-sql" % "3.0.1-session"
