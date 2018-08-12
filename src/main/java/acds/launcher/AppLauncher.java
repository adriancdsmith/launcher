package acds.launcher;

import java.io.IOException;
import java.util.Map;

public interface AppLauncher {

		public abstract void launch(Map<String,String> parameters) throws IOException ;
		
		public abstract void exit() ;
		
		
		public abstract void setAppConfig(AppConfiguration appConfig);
	
}
