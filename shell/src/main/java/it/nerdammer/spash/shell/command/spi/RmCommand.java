package it.nerdammer.spash.shell.command.spi;

import com.google.common.collect.ImmutableMap;
import it.nerdammer.spash.shell.api.fs.FileSystemFacade;
import it.nerdammer.spash.shell.api.fs.SpashFileSystem;
import it.nerdammer.spash.shell.command.AbstractCommand;
import it.nerdammer.spash.shell.command.CommandResult;
import it.nerdammer.spash.shell.command.ExecutionContext;

import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

/**
 * Command to remove a file or directory.
 *
 * @author Nicola Ferraro
 */
public class RmCommand extends AbstractCommand {

    public RmCommand(String commandString) {
        super(commandString, ImmutableMap.<String, Boolean>builder()
                .put("r", false)
                .build());
    }

    @Override
    public CommandResult execute(ExecutionContext ctx) {

        FileSystemFacade fs = SpashFileSystem.get();

        List<String> files = this.getArguments();
        if(files.size()==0) {
            return CommandResult.error(this, "Missing argument");
        }

        boolean recursive = this.getOptions().containsKey("r");

        List<Path> paths = new LinkedList<>();
        for(String file : files) {
            Path path = fs.getAbsolutePath(ctx.getSession().getWorkingDir(), file);
            paths.add(path);

            if(!fs.exists(path.toString())) {
                return CommandResult.error(this, "No such file or directory");
            }

            boolean isDirectory = fs.isDirectory(path.toString());
            if(isDirectory && !recursive) {
                return CommandResult.error(this, "is a directory");
            }
        }

        for(Path path : paths) {
            boolean removed = fs.rm(path.toString(), recursive);
            if(!removed) {
                return CommandResult.error(this, "Unable to remove file: operation interrupted");
            }
        }

        return CommandResult.success(this);
    }
}
