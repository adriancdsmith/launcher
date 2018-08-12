package acds.launcher;

import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 
 * @author adrian
 *
 */
public class AppCommandLauncher implements AppLauncher {

	boolean isWindows = false;
	String homeDirectory = null ;
	
	boolean spawnTerminal = true ;
	
	AppConfiguration appConfig = null ;
	
	public void setAppConfig(AppConfiguration appConfig) {
		this.appConfig = appConfig;
	}


	public AppCommandLauncher() {
		isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
		homeDirectory = System.getProperty("user.home");
		
	}
	
	
	public static void main(String args[]) throws IOException {
		
			Map<String,String> commandMap = new HashMap<String,String>();
			commandMap.put("command","ls");
			commandMap.put("l","true");
	
			AppCommandLauncher al = new AppCommandLauncher();
			al.runProcess(commandMap);
	
			
			
	}
	
	@Override
	public void launch(Map<String, String> commandMap) throws IOException {
		
		System.out.println(commandMap);
		runProcess(commandMap);
		
	}

	
	@Override
	public void exit() {
		System.exit(0);
	}

	
	
	
	public void runProcess(Map<String,String> commandMap) throws IOException {
		
	
		StringBuffer arguments = new StringBuffer();
		StringBuffer commandLine = new StringBuffer();
		
	
		for (String key : commandMap.keySet() ) {
				String value = commandMap.get(key);
			
				if ( key.equals("command") ) {
					commandLine.append(value) ;
				} 
				else {
					String type = appConfig.getString(key + ".type") ;
					
					if ( "--".equals(type)) {
						arguments.append("--");
						arguments.append(key);
						arguments.append("=");
						arguments.append(value);
						arguments.append(" ");
					} 
					else if ( "flag".equals(type)) {
						arguments.append("-");
						arguments.append(key);
					} 
					else {
						arguments.append("-");
						arguments.append(key);
						arguments.append(" ");
						arguments.append(value);
						arguments.append(" ");
						
					}
				}
		}
		
		commandLine.append(" ").append(arguments);

		System.out.println(commandLine.toString());
		
		
	
		ProcessBuilder processBuilder = new ProcessBuilder();
		processBuilder.redirectInput(Redirect.INHERIT);
		processBuilder.redirectOutput(Redirect.INHERIT);
		processBuilder.redirectError(Redirect.INHERIT);
		
		if ( spawnTerminal ) {
//			processBuilder.command("/usr/bin/x-terminal-emulator","-e",commandLine.toString());
			processBuilder.command("/usr/bin/gnome-terminal", "--", "/bin/bash","-c", commandLine.toString() + "; sleep 10");
		} else {
			processBuilder.command("bash","-c",commandLine.toString());
		}
		
		
//		System.out.println(processBuilder.environment());
	
	//	builder.directory(new File(System.getProperty("user.home")));
		
		
		processBuilder.start();
	
	}
	
	
	
}




