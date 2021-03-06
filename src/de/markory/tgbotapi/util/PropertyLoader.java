package de.markory.tgbotapi.util;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Properties;
import java.util.Set;

public class PropertyLoader {


	private static final String suffix = ".properties";

	/** Loads properties of <b>targetClass</b>. Search by Name:"<i>Classname.properties</i>" in classpath. */
	public static void load(Class<?> targetClass) {

		String propFile = targetClass.getSimpleName()+suffix;

		load(targetClass,propFile);
	}


	/** Loads Properties from given <b>propFile</b> into <b>targetClass</b> */
	public static void load(Class<?> targetClass, String propertiesFilename) {

		InputStream propFileInputStream = targetClass.getClassLoader().getResourceAsStream(propertiesFilename);

			if ( propFileInputStream == null ) return;

			Properties properties = new Properties();

			try {
				properties.load(propFileInputStream);

				initProps(properties, targetClass);

			} catch (IOException e) {
				throw new RuntimeException("de.markory.notibot.facade.config.PropertyLoader.load(Class) - "
					+ "Error loading file: "+propertiesFilename+". Exception: "+e.getStackTrace());
			}
	}


	/**
	 *
	 */
	private static void initProps(Properties properties, Class<?> targetClass) {

		Set<Object> propertiesSet = properties.keySet();

		for (Object propObj: propertiesSet) {

			setField(targetClass, (String)propObj, properties.getProperty((String)propObj));
		}
	}


	/**
	 *
	 */
	private static void setField(Class<?> targetClass, String name, String value) {

		boolean changedAccessible = false;

		Field targetField = null;

		try {
			targetField = targetClass.getDeclaredField(name);

			if ( !targetField.isAccessible() ) {
				targetField.setAccessible(true);
				changedAccessible = true;
			}

			if ( targetField.getType() ==  boolean.class ) {
				targetField.setBoolean(null, new Boolean(value).booleanValue());
			}
			else if( targetField.getType() == int.class ) {
				targetField.setInt(null, new Integer(value).intValue());
			}
			else if( targetField.getType() == double.class ) {
				targetField.setDouble(null, new Double(value).intValue());
			}
			else{
				targetField.set(null, value);
			}

		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException("de.markory.notibot.facade.config.PropertyLoader.setField(Class<?>, String, Class<?>) -"
					+ "Can't set Field: "+name+".", e);
		}
		finally{

			if(changedAccessible && targetField != null) {
				targetField.setAccessible(false);
			}
		}
	}
}
