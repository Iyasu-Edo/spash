package it.nerdammer.spash.shell.command.spi;

import it.nerdammer.spash.shell.api.fs.FileSystemFacade;
import it.nerdammer.spash.shell.api.fs.SpashFileSystem;
import it.nerdammer.spash.shell.command.AbstractCommand;
import it.nerdammer.spash.shell.command.CommandResult;
import it.nerdammer.spash.shell.session.SpashSession;

import java.nio.file.Path;

/**
 * @author Nicola Ferraro
 */
public class CdCommand extends AbstractCommand {

    public CdCommand(String commandString) {
        super(commandString);
    }

    @Override
    public CommandResult execute(SpashSession session) {

        FileSystemFacade fs = SpashFileSystem.get();

        String dir = this.getFirstArgument();
        if(dir==null) {
            dir = "/"; // the default dir
        }

        Path dest = fs.getAbsolutePath(session.getWorkingDir(), dir);
        boolean exists = fs.exists(dest.toString());
        if(!exists) {
            return new CommandResult(this, "No such file or directory");
        }

        boolean isDir = fs.isDirectory(dest.toString());
        if(!isDir) {
            return new CommandResult(this, "Not a directory");
        }

        session.setWorkingDir(dest.toAbsolutePath().normalize().toString());

        return new CommandResult(this, true);
    }
}
