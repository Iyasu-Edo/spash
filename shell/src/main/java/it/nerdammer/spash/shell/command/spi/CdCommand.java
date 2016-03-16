package it.nerdammer.spash.shell.command.spi;

import it.nerdammer.spash.shell.api.fs.FileSystemFacade;
import it.nerdammer.spash.shell.api.fs.SpashFileSystem;
import it.nerdammer.spash.shell.command.AbstractCommand;
import it.nerdammer.spash.shell.command.CommandResult;
import it.nerdammer.spash.shell.SpashSession;

import java.nio.file.Path;

/**
 * @author Nicola Ferraro
 */
public class CdCommand extends AbstractCommand {

    public CdCommand(String commandString) {
        super(commandString);
    }

    @Override
    public CommandResult execute(SpashSession session, CommandResult previousResult) {

        try {
            FileSystemFacade fs = SpashFileSystem.get();

            if(this.getCommandTokenizer().getArguments().size()==0) {
                return CommandResult.error(this, "No file provided");
            } else if(this.getCommandTokenizer().getArguments().size()>1) {
                return CommandResult.error(this, "Too many arguments");
            }

            String dir = this.getCommandTokenizer().getArguments().get(0);
            if (dir == null) {
                dir = "/"; // the default dir
            }

            Path dest = fs.getAbsolutePath(session.getWorkingDir(), dir);
            boolean exists = fs.exists(dest.toString());
            if (!exists) {
                return CommandResult.error(this, "No such file or directory");
            }

            boolean isDir = fs.isDirectory(dest.toString());
            if (!isDir) {
                return CommandResult.error(this, "Not a directory");
            }

            session.setWorkingDir(dest.toAbsolutePath().normalize().toString());

            return CommandResult.success(this);
        } catch(IllegalArgumentException e) {
            return CommandResult.error(this, "Illegal input");
        }
    }
}
