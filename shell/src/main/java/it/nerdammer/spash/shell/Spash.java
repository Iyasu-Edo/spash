package it.nerdammer.spash.shell;

import org.apache.sshd.server.SshServer;

/**
 * The main class of the application.
 *
 * @author Nicola Ferraro
 */
public class Spash {

    public static void main(String[] args) throws Exception {

        SshServer server = SshServerFactory.create();
        server.start();

        synchronized(Spash.class) {
            Spash.class.wait();
        }

    }
}
