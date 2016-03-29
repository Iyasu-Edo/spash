package it.nerdammer.spash.shell.command.spi;

import ch.lambdaj.Lambda;
import com.google.common.collect.ImmutableMap;
import it.nerdammer.spash.shell.command.*;
import it.nerdammer.spash.shell.common.SerializableFunction;
import it.nerdammer.spash.shell.common.SpashCollection;
import it.nerdammer.spash.shell.common.SpashCollectionEmptyAdapter;

import java.util.List;

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

            String catCmdString = "cat " + Lambda.join(args.subList(1, args.size()), " ");
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

        SpashCollection<String> content = source.filter(new FilterFunction(filter, reverse, insensitive));

        return CommandResult.success(this, content);
    }

    static class FilterFunction extends SerializableFunction<String, Boolean> {

        private String filter;

        private boolean reverse;

        private boolean insensitive;

        public FilterFunction(String filter, boolean reverse, boolean insensitive) {
            this.filter = filter;
            this.reverse = reverse;
            this.insensitive = insensitive;
        }

        @Override
        public Boolean apply(String s) {
            return reverse ^ (
                    insensitive ?
                            s.toLowerCase().contains(filter.toLowerCase()) :
                            s.contains(filter)
            );
        }

    }

}

