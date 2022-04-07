package com.assurity.runtime.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.assurity.runtime.exceptions.ConfigurationException;

/**
 * This utility class will be used to read the property files within in the
 * project and get the property values out.
 * 
 * @author cdushmantha
 *
 */
public class PropertyReader {

	private static final Logger LOGGER = LogManager.getLogger();

	private Properties properties;

	/**
	 * Creates a PropertyReader instance. Initializes the Properties to be used by
	 * next read commands.
	 * 
	 * @param peopertyFileName : Relative file path of the property file
	 */
	public PropertyReader(String propertyFileName) {

		try (InputStream input = this.getClass().getClassLoader().getResourceAsStream(propertyFileName)) {

			properties = new Properties();

			// load properties file
			properties.load(input);

		} catch (IOException e) {
			String errorMessage = "Error occured while reading property file : " + propertyFileName;
			LOGGER.error(errorMessage, e);
			throw new ConfigurationException(errorMessage, e);
		}
	}

	/**
	 * Returns the value of the given property within the property file.
	 * 
	 * @param key Key of the property
	 * @return Value of the property
	 */
	public String getProperty(String key) {
		return String.valueOf(properties.get(key));
	}

}
