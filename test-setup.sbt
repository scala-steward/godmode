
import scala.sys.process._

testFrameworks += new TestFramework("minitest.runner.Framework")

// FIXME: this waits until docker returns, but not until postgres is ready
testOptions += Tests.Setup { _ =>
  "docker-compose up -d --build" !
}
testOptions += Tests.Cleanup { _ =>
  "docker-compose stop" !
}

fork in Test := true
