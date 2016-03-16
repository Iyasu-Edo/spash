package it.nerdammer.spash.shell.command.spi;

import it.nerdammer.spash.shell.SpashSession;
import it.nerdammer.spash.shell.api.fs.FileSystemFacade;
import it.nerdammer.spash.shell.api.fs.SpashFileSystem;
import it.nerdammer.spash.shell.api.spark.SpashSparkSubsystem;
import it.nerdammer.spash.shell.command.AbstractCommand;
import it.nerdammer.spash.shell.command.CommandResult;
import it.nerdammer.spash.shell.common.SpashCollection;
import it.nerdammer.spash.shell.common.SpashCollectionEmptyAdapter;
import it.nerdammer.spash.shell.common.SpashCollectionStreamAdapter;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

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
    public CommandResult execute(SpashSession session, CommandResult previousResult) {

        List<String> args = this.getCommandTokenizer().getArguments();
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

        Stream<String> stream = Collections.singletonList(bui.toString()).stream();
        return CommandResult.success(this, new SpashCollectionStreamAdapter<>(stream));
    }
}
