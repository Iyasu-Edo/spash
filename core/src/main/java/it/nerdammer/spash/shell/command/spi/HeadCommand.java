package it.nerdammer.spash.shell.command.spi;

import it.nerdammer.spash.shell.api.fs.FileSystemFacade;
import it.nerdammer.spash.shell.api.fs.SpashFileSystem;
import it.nerdammer.spash.shell.api.spark.SpashSparkSubsystem;
import it.nerdammer.spash.shell.command.AbstractCommand;
import it.nerdammer.spash.shell.command.CommandResult;
import it.nerdammer.spash.shell.command.ExecutionContext;
import it.nerdammer.spash.shell.common.SpashCollection;
import it.nerdammer.spash.shell.SpashSession;

import java.nio.file.Path;

/**
 * Command to get the first lines of a file or directory.
 *
 * @author Nicola Ferraro
 */
public class HeadCommand extends AbstractCommand {

    public static final int DEFAULT_NUM_LINES = 10;

    public HeadCommand(String commandString) {
        super(commandString);
    }

    @Override
    public CommandResult execute(ExecutionContext ctx) {

        FileSystemFacade fs = SpashFileSystem.get();

        if(this.getArguments().size()==0) {
            return CommandResult.error(this, "No file provided");
        } else if(this.getArguments().size()>1) {
            return CommandResult.error(this, "Too many arguments");
        }

        String file = this.getArguments().get(0);

        Path path = fs.getAbsolutePath(ctx.getSession().getWorkingDir(), file);
        boolean exists = fs.exists(path.toString());
        if(!exists) {
            return CommandResult.error(this, "No such file or directory");
        }

        SpashCollection<String> content = SpashSparkSubsystem.get().head(path, DEFAULT_NUM_LINES);

        return CommandResult.success(this, content);
    }
}
