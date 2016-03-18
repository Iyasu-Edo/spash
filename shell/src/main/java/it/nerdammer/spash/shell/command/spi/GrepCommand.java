package it.nerdammer.spash.shell.command.spi;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import it.nerdammer.spash.shell.api.fs.FileSystemFacade;
import it.nerdammer.spash.shell.api.fs.SpashFileSystem;
import it.nerdammer.spash.shell.api.spark.SpashSparkSubsystem;
import it.nerdammer.spash.shell.command.*;
import it.nerdammer.spash.shell.common.SpashCollection;
import it.nerdammer.spash.shell.common.SpashCollectionEmptyAdapter;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Command to filter collections of data.
 *
 * @author Nicola Ferraro
 */
public class GrepCommand extends AbstractCommand {

    public GrepCommand(String commandString) {
        super(commandString, ImmutableMap.<String, Boolean>builder()
                .put("v", false)
                .put("i", false)
                .build());
    }

    @Override
    public CommandResult execute(ExecutionContext ctx) {

        List<String> args = this.getArguments();
        if(args.size()==0) {
            return CommandResult.error(this, "Missing argument");
        } else if(args.size()==1) {
            // pipe mode
            SpashCollection<String> prev = ctx.getPreviousCommandResult() != null ? ctx.getPreviousCommandResult().getContent() : null;
            if(prev==null) {
                prev = new SpashCollectionEmptyAdapter<>();
            }
            return execute(ctx, prev);
        } else {
            // cat mode
            String catCmdString = "cat " + args.subList(1, args.size()).stream().collect(Collectors.joining(" "));
            Command cat = CommandFactory.getInstance().getCommand(catCmdString);
            CommandResult res = cat.execute(ctx);
            if(!res.isSuccess()) {
                return CommandResult.error(this, res.getErrorMessage());
            }

            return execute(ctx, res.getContent());
        }

    }

    protected CommandResult execute(ExecutionContext ctx, SpashCollection<String> source) {

        String filter = this.getArguments().get(0);
        boolean reverse = this.getOptions().containsKey("v");
        boolean insensitive = this.getOptions().containsKey("i");

        Function<String, Boolean> matchFunction = (insensitive) ?
                (String s) -> s.toLowerCase().contains(filter.toLowerCase()) : (String s) -> s.contains(filter);


        SpashCollection<String> content = source.filter(s -> reverse ^ matchFunction.apply(s));

        return CommandResult.success(this, content);
    }
}
