package acds.launcher;

import java.util.Iterator;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.List;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.configuration2.ConfigurationConverter;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.io.BasePathLocationStrategy;
import org.apache.commons.configuration2.io.ClasspathLocationStrategy;
import org.apache.commons.configuration2.io.CombinedLocationStrategy;
import org.apache.commons.configuration2.io.FileLocationStrategy;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.configuration2.CompositeConfiguration;

public class AppConfiguration {

	private final static Logger LOGGER = Logger.getLogger(AppConfiguration.class.getName());

	private CompositeConfiguration config = null;

	private Properties commmandLineProps = new Properties();

	private Properties presetProps = new Properties();

	boolean errorIfMissing = true;

	public static void main(String args[]) throws ConfigurationException {

		AppConfiguration ac = new AppConfiguration();

		ac.preset("app", "launcher");
		ac.preset("run", "launch1");
		ac.preset("env", "env");

		ac.loadCommandLine(args);
		ac.loadConfiguration();
		ac.dump();
//		System.out.println(ac.getCommandLineProps(args));

	}
	
	public String getString(String name) {
		return config.getString(name);
	}

	public List<Object> getList(String name) {
		return config.getList(name);
	
	}

	public void init(String application, List<String> argList) throws ConfigurationException {
		preset("app", application);
		loadCommandLine(argList);
		loadConfiguration();
	}
	
	public void init(String application, String args[]) throws ConfigurationException {
		preset("app", application);
		loadCommandLine(args);
		loadConfiguration();
	}

	public void load(String args[]) {

	}

	public void dump() {
		Iterator<String> iter = config.getKeys();
		while (iter.hasNext()) {
			String key = iter.next();
			LOGGER.info(key + "=" + config.getString(key));
		}
	}

	/**
	 * Load the configuration
	 * 
	 * @throws ConfigurationException
	 */
	public void loadConfiguration() throws ConfigurationException {

		// Load Configuration
		config = new CompositeConfiguration();

		// Load Pre-Sets
		config.addConfiguration(ConfigurationConverter.getConfiguration(presetProps));

		// Load Command Line Properties (first)
		config.addConfiguration(ConfigurationConverter.getConfiguration(commmandLineProps));

		// Load Run Time Specified Properties (second)
		String propFile = null;
		String run = commmandLineProps.getProperty("run");
		if (run != null) {
			propFile = run + ".properties";
			config.addConfiguration(getPropertyConfiguration(propFile));
		}

		// Load Environment properties next
		String env = config.getString("env");
		if (env != null) {
			propFile = "properties/" + env + ".properties";
			config.addConfiguration(getPropertyConfiguration(propFile));
		}

		// Load application properties next
		String app = config.getString("app");
		if (app != null) {
			propFile = "properties/" + app + ".properties";
			config.addConfiguration(getPropertyConfiguration(propFile));
		}

		// Load default properties
		propFile = "properties/default.properties";
		config.addConfiguration(getPropertyConfiguration(propFile));

		Iterator<String> iter = config.getKeys();
		if ("true".equalsIgnoreCase(config.getString("debug.config.dump"))) {
			while (iter.hasNext()) {
				String key = iter.next();
				LOGGER.info(key + "=" + config.getString(key));
			}

		}

	}

	public void preset(String name, String value) {
		presetProps.put(name, value);
	}

	/**
	 * 
	 * @param argList
	 * @return
	 */
	private void loadCommandLine(List<String> argList) {

		String args[] = argList.toArray(new String[0]);

		commmandLineProps = getCommandLineProps(args);
	}

	/**
	 * 
	 * @param argList
	 * @return
	 */
	private void loadCommandLine(String args[]) {

		commmandLineProps = getCommandLineProps(args);
	}

	/**
	 * Parse command line properties.
	 * 
	 * @param args
	 * @return
	 */
	private Properties getCommandLineProps(String args[]) {

		Properties cmdProps = new Properties();

		int i = 0;
		String opt = null;

		for (String arg : args) {

			if (arg.startsWith("-")) {

				// Option may begin with one or two hyphens
				opt = StringUtils.substringAfter(arg, "-");

				if (opt.startsWith("-")) {
					opt = StringUtils.substringAfter(opt, "-");
				}

				if (opt.contains("=")) {
					cmdProps.put(StringUtils.substringBefore(opt, "="), StringUtils.substringAfter(opt, "="));
					opt = null;
				} else {
					cmdProps.put(opt, "true");
				}

			} else if (opt != null) {
				cmdProps.put(opt, arg);
				opt = null;
			} else {
				cmdProps.put("arg." + i++, arg);
			}

		}
		return cmdProps;
	}

	/**
	 * Load Configuration File.
	 * 
	 * @param path
	 * @return
	 * @throws ConfigurationException
	 */

	private PropertiesConfiguration getPropertyConfiguration(String path) throws ConfigurationException {

		List<FileLocationStrategy> lis = new ArrayList<FileLocationStrategy>();
		lis.add(new BasePathLocationStrategy());
		lis.add(new ClasspathLocationStrategy());
		CombinedLocationStrategy combStrategy = new CombinedLocationStrategy(lis);

		FileBasedConfigurationBuilder<PropertiesConfiguration> builder = new FileBasedConfigurationBuilder<PropertiesConfiguration>(
				PropertiesConfiguration.class).configure(
						new Parameters().properties().setFileName(path).setThrowExceptionOnMissing(errorIfMissing)
								.setListDelimiterHandler(new DefaultListDelimiterHandler(',')).setIncludesAllowed(false)
								.setLocationStrategy(combStrategy));
		PropertiesConfiguration config = builder.getConfiguration();

		return config;
	}

}
