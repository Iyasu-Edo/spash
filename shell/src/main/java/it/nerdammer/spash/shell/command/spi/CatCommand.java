package it.nerdammer.spash.shell.command.spi;

import it.nerdammer.spash.shell.api.fs.FileSystemFacade;
import it.nerdammer.spash.shell.api.fs.SpashFileSystem;
import it.nerdammer.spash.shell.api.spark.SpashSparkSubsystem;
import it.nerdammer.spash.shell.command.AbstractCommand;
import it.nerdammer.spash.shell.command.CommandResult;
import it.nerdammer.spash.shell.command.ExecutionContext;
import it.nerdammer.spash.shell.common.SpashCollection;
import it.nerdammer.spash.shell.SpashSession;
import it.nerdammer.spash.shell.common.SpashCollectionEmptyAdapter;

import java.nio.file.Path;
import java.util.List;

/**
 * Command to get the content of a file or directory.
 *
 * @author Nicola Ferraro
 */
public class CatCommand extends AbstractCommand {

    public CatCommand(String commandString) {
        super(commandString);
    }

    @Override
    public CommandResult execute(ExecutionContext ctx) {

        FileSystemFacade fs = SpashFileSystem.get();

        List<String> files = this.getArguments();
        if(files.size()==0) {
            return CommandResult.error(this, "No file provided");
        }

        SpashCollection<String> content = new SpashCollectionEmptyAdapter<>();
        for(String file : files) {
            Path path = fs.getAbsolutePath(ctx.getSession().getWorkingDir(), file);
            boolean exists = fs.exists(path.toString());
            if (!exists) {
                return CommandResult.error(this, "No such file or directory");
            }

            content = content.union(SpashSparkSubsystem.get().read(path));
        }

        return CommandResult.success(this, content);
    }
}
