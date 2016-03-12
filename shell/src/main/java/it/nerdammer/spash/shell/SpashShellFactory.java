package it.nerdammer.spash.shell;

import org.apache.sshd.common.Factory;
import org.apache.sshd.server.Command;

/**
 * The factory of the Spash shell.
 *
 * @author Nicola Ferraro
 */
public class SpashShellFactory implements Factory<Command> {

    public Command create()
    {
        return new SpashShell();
    }
}
