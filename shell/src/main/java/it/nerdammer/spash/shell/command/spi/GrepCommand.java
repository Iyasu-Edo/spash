package it.nerdammer.spash.shell.command.spi;

import it.nerdammer.spash.shell.api.fs.FileSystemFacade;
import it.nerdammer.spash.shell.api.fs.SpashFileSystem;
import it.nerdammer.spash.shell.api.spark.SpashSparkSubsystem;
import it.nerdammer.spash.shell.command.AbstractCommand;
import it.nerdammer.spash.shell.command.CommandResult;
import it.nerdammer.spash.shell.command.ExecutionContext;
import it.nerdammer.spash.shell.common.SpashCollection;
import it.nerdammer.spash.shell.common.SpashCollectionEmptyAdapter;

import java.nio.file.Path;
import java.util.List;

/**
 * Command to filter collections of data.
 *
 * @author Nicola Ferraro
 */
public class GrepCommand extends AbstractCommand {

    public GrepCommand(String commandString) {
        super(commandString);
    }

    @Override
    public CommandResult execute(ExecutionContext ctx) {

        List<String> args = this.getArguments();
        if(args.size()!=1) {
            return CommandResult.error(this, "Too many arguments");
        }

        String filter = args.get(0);

        SpashCollection<String> prev = ctx.getPreviousCommandResult() != null ? ctx.getPreviousCommandResult().getContent() : null;
        if(prev==null) {
            prev = new SpashCollectionEmptyAdapter<>();
        }

        SpashCollection<String> content = prev.filter(s -> s.contains(filter));

        return CommandResult.success(this, content);
    }
}
