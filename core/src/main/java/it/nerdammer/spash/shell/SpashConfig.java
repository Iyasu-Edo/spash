package it.nerdammer.spash.shell;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Properties;

/**
 * Configuration options for Spash.
 *
 * @author Nicola Ferraro
 */
public class SpashConfig {

    private static final SpashConfig INSTANCE = new SpashConfig();

    private Logger logger = LoggerFactory.getLogger(getClass());

    private Properties properties;

    private SpashConfig() {

        this.properties = new Properties();

        String configName = "spash.config";
        String configFile = System.getProperty(configName);
        if(configFile==null) {
            logger.warn("No configuration property " + configName + " found. Using defaults.");
        } else {
            try(Reader r = new FileReader(configFile)) {
                properties.load(r);
            } catch(FileNotFoundException e) {
                throw new IllegalStateException("Unable to find the file defined in " + configName + " property: " + configFile, e);
            } catch(IOException e) {
                throw new IllegalStateException("Unable to load the file defined in " + configName + " property: " + configFile, e);
            }
        }

    }

    public static SpashConfig getInstance() {
        return INSTANCE;
    }

    public String spashKeyFileName() {
        String configName = "spash.config.dir";
        String configDir = System.getProperty(configName);
        return configDir!=null ? configDir + "/key.ser" : "key.ser";
    }

    public int spashListenPort() {
        return propOrElseAsInt("spash.listen.port", 2222);
    }

    public String hdfsHost() {
        return propOrElse("spash.hdfs.host", "hdfshost");
    }

    public int hdfsPort() {
        return propOrElseAsInt("spash.hdfs.port", 8020);
    }

    public String hdfsHostPort() {
        return hdfsHost() + ":" + hdfsPort();
    }


    private int propOrElseAsInt(String name, int defaulz) {
        String val = this.properties.getProperty(name);
        if(val==null) {
            return defaulz;
        }
        try {
            return Integer.parseInt(val);
        } catch(NumberFormatException e) {
            throw new IllegalStateException("Illegal configuration for parameter " + name + ". Value: " + val, e);
        }
    }

    private String propOrElse(String name, String defaulz) {
        return this.properties.getProperty(name, defaulz);
    }

}
