package it.nerdammer.spash.shell.api.spark;

import it.nerdammer.spash.shell.api.fs.FileSystemFacade;
import it.nerdammer.spash.shell.api.fs.FileSystemFacadeImpl;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;

/**
 * Holds a reference to the Spark context.
 *
 * @author Nicola Ferraro
 */
public class SpashSparkSubsystem {

    private static final SparkFacade FACADE_INSTANCE;

    static {
        SparkConf conf = new SparkConf().setAppName("Spash").setMaster("local");
        JavaSparkContext sc = new JavaSparkContext(conf);
        FACADE_INSTANCE = new SparkFacadeImpl(sc);
    }

    private SpashSparkSubsystem() {
    }

    public static SparkFacade get() {
        return FACADE_INSTANCE;
    }
}
