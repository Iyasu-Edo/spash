package it.nerdammer.spash.shell.command.spi;

import it.nerdammer.spash.shell.api.fs.FileSystemFacade;
import it.nerdammer.spash.shell.api.fs.SpashFileSystem;
import it.nerdammer.spash.shell.command.AbstractCommand;
import it.nerdammer.spash.shell.command.CommandResult;
import it.nerdammer.spash.shell.command.ExecutionContext;

import java.nio.file.Path;
import java.util.List;

/**
 * Command to remove a directory.
 *
 * @author Nicola Ferraro
 */
public class RmDirCommand extends AbstractCommand {

    public RmDirCommand(String commandString) {
        super(commandString);
    }

    @Override
    public CommandResult execute(ExecutionContext ctx) {

        FileSystemFacade fs = SpashFileSystem.get();

        List<String> files = this.getArguments();
        if(files.size()==0) {
            return CommandResult.error(this, "Missing argument");
        } else if(files.size()>1) {
            return CommandResult.error(this, "Too many arguments");
        }

        String file = files.get(0);

        Path path = fs.getAbsolutePath(ctx.getSession().getWorkingDir(), file);

        if(!fs.exists(path.toString())) {
            return CommandResult.error(this, "No such file or directory");
        }

        if(!fs.isDirectory(path.toString())) {
            return CommandResult.error(this, "Not a directory");
        }

        boolean removed = fs.rm(path.toString(), false);
        if(!removed) {
            return CommandResult.error(this, "Directory not empty");
        }

        return CommandResult.success(this);
    }
}
