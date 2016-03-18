package it.nerdammer.spash.shell.command.spi;

import it.nerdammer.spash.shell.api.fs.SpashFileSystem;
import it.nerdammer.spash.shell.command.AbstractCommand;
import it.nerdammer.spash.shell.command.CommandResult;
import it.nerdammer.spash.shell.command.ExecutionContext;
import it.nerdammer.spash.shell.common.SpashCollection;
import it.nerdammer.spash.shell.SpashSession;

import java.nio.file.Path;
import java.util.List;

/**
 * A command that lists the files contained in the current directory.
 *
 * @author Nicola Ferraro
 */
public class LsCommand extends AbstractCommand {

    public LsCommand(String commandString) {
        super(commandString);
    }

    @Override
    public CommandResult execute(ExecutionContext ctx) {

        List<String> args = this.getArguments();
        if(args.size()>0) {
            return CommandResult.error(this, "Unexpected arguments: " + args);
        }

        SpashCollection<Path> files = SpashFileSystem.get().ls(ctx.getSession().getWorkingDir());
        SpashCollection<String> fileNames = files.map(f -> f.getFileName().toString());

        return CommandResult.success(this, fileNames);
    }
}
