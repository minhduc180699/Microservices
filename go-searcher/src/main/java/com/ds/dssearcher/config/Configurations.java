package com.ds.dssearcher.config;

import java.util.Properties;

public class Configurations {
	private Properties properties = null;
	private static Configurations instance = null;

	/** Private constructor */
	private Configurations(){
	    this.properties = new Properties();
	    try{
	    	ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
	        properties.load(classLoader.getResourceAsStream("config.properties"));
	    }catch(Exception ex){
	        ex.printStackTrace();
	    }
	}   

	/** Creates the instance is synchronized to avoid multithreads problems */
	private synchronized static void createInstance () {
	    if (instance == null) { 
	        instance = new Configurations ();
	    }
	}

	/** Get the properties instance. Uses singleton pattern */
	public static Configurations getInstance(){
	    // Uses singleton pattern to guarantee the creation of only one instance
	    if(instance == null) {
	        createInstance();
	    }
	    return instance;
	}

	/** Get a property of the property file */
	public String getProperty(String key){
	    String result = null;
	    if(key !=null && !key.trim().isEmpty()){
	        result = this.properties.getProperty(key);
	    }
	    return result;
	}

	/** Override the clone method to ensure the "unique instance" requeriment of this class */
	public Object clone() throws CloneNotSupportedException {
	    throw new CloneNotSupportedException();
	}
}
