package it.nerdammer.spash.shell.command.spi;

import it.nerdammer.spash.shell.api.fs.FileSystemFacade;
import it.nerdammer.spash.shell.api.fs.SpashFileSystem;
import it.nerdammer.spash.shell.api.spark.SparkFacade;
import it.nerdammer.spash.shell.api.spark.SpashSparkSubsystem;
import it.nerdammer.spash.shell.command.AbstractCommand;
import it.nerdammer.spash.shell.command.CommandResult;
import it.nerdammer.spash.shell.command.ExecutionContext;
import it.nerdammer.spash.shell.common.SpashCollection;
import it.nerdammer.spash.shell.common.SpashCollectionEmptyAdapter;

import java.nio.file.Path;
import java.util.List;

/**
 * Command to append the result of a the previous command to a Hadoop file.
 *
 * @author Nicola Ferraro
 */
public class WriteCommand extends AbstractCommand {

    public WriteCommand(String commandString) {
        super(commandString);
    }

    @Override
    public CommandResult execute(ExecutionContext ctx) {

        FileSystemFacade fs = SpashFileSystem.get();
        SparkFacade spark = SpashSparkSubsystem.get();

        List<String> files = this.getArguments();
        if(files.size()==0) {
            return CommandResult.error(this, "No file provided");
        } else if(files.size()>1) {
            return CommandResult.error(this, "Too many arguments");
        }

        String file = this.getArguments().get(0);
        Path path = fs.getAbsolutePath(ctx.getSession().getWorkingDir(), file);

        CommandResult prevRes = ctx.getPreviousCommandResult();
        if(!prevRes.isSuccess()) {
            return CommandResult.success(this);
        }

        SpashCollection<String> content = prevRes.getContent();
        spark.write(content, path);

        return CommandResult.success(this);
    }
}
