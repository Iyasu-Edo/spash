package it.nerdammer.spash.shell;

import org.apache.sshd.server.SshServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The main class of the application.
 *
 * @author Nicola Ferraro
 */
public class Spash {

    public static void main(String[] args) throws Exception {

        Logger logger = LoggerFactory.getLogger(Spash.class);
        logger.info("Starting the Spash shell");

        SshServer server = SshServerFactory.create();
        server.start();

        logger.info("Spash shell started");

        synchronized(Spash.class) {
            Spash.class.wait();
        }

    }
}
