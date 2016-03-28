package it.nerdammer.spash.shell;

import it.nerdammer.spash.shell.api.fs.SpashFileSystem;
import it.nerdammer.spash.shell.api.spark.SpashSparkSubsystem;
import org.apache.sshd.server.SshServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * The main class of the application.
 *
 * @author Nicola Ferraro
 */
public class Spash {

    public static void main(String[] args) throws Exception {

        Logger logger = LoggerFactory.getLogger(Spash.class);

        logger.info("Initializing the file system");
        SpashFileSystem.get().getFileSystem();

        logger.info("Initializing Spark by running a simple job");
        SpashSparkSubsystem.get().parallelize(Arrays.asList(1, 2, 3)).collect();

        logger.info("Starting the Spash shell");
        SshServer server = SshServerFactory.create();
        server.start();

        logger.info("Spash shell started");

        synchronized(Spash.class) {
            Spash.class.wait();
        }

    }
}
