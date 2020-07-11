import com.google.inject.AbstractModule
import servers.InitDB

class Module extends AbstractModule {

  override def configure() = {
    bind(classOf[InitDB]).asEagerSingleton()
  }

}