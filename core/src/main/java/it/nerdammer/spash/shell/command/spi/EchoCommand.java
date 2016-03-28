package it.nerdammer.spash.shell.command.spi;

import it.nerdammer.spash.shell.command.AbstractCommand;
import it.nerdammer.spash.shell.command.CommandResult;
import it.nerdammer.spash.shell.command.ExecutionContext;
import it.nerdammer.spash.shell.common.SpashCollectionListAdapter;

import java.util.Collections;
import java.util.List;

/**
 * Command to echo the user input.
 *
 * @author Nicola Ferraro
 */
public class EchoCommand extends AbstractCommand {

    public EchoCommand(String commandString) {
        super(commandString);
    }

    @Override
    public CommandResult execute(ExecutionContext ctx) {

        List<String> args = this.getArguments();
        if(args.size()==0) {
            return CommandResult.error(this, "No arguments provided");
        }

        StringBuilder bui = new StringBuilder();
        for(int i=0; i<args.size(); i++) {
            bui.append(args.get(i));
            if(i<args.size()-1) {
                bui.append(" ");
            }
        }

        List<String> stream = Collections.singletonList(bui.toString());
        return CommandResult.success(this, new SpashCollectionListAdapter<>(stream));
    }
}
