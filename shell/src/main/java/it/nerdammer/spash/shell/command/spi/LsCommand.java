package it.nerdammer.spash.shell.command.spi;

import it.nerdammer.spash.shell.api.fs.SpashFileSystem;
import it.nerdammer.spash.shell.command.AbstractCommand;
import it.nerdammer.spash.shell.command.CommandResult;
import it.nerdammer.spash.shell.common.SpashCollection;
import it.nerdammer.spash.shell.session.SpashSession;

import java.nio.file.Path;

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
    public CommandResult execute(SpashSession session) {
        SpashCollection<Path> files = SpashFileSystem.get().ls(session.getWorkingDir());
        SpashCollection<String> fileNames = files.map(f -> f.getFileName().toString());

        CommandResult res = new CommandResult(this, true);
        res.setContent(fileNames);
        return res;
    }
}
