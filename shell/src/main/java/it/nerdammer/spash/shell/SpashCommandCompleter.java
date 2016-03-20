package it.nerdammer.spash.shell;

import it.nerdammer.spash.shell.api.fs.SpashFileSystem;
import it.nerdammer.spash.shell.command.CommandFactory;
import jline.console.completer.Completer;
import jline.internal.Preconditions;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Nicola Ferraro
 */
public class SpashCommandCompleter implements Completer {

    private Set<String> commands;

    private SpashSession session;

    public SpashCommandCompleter(SpashSession session) {
        this.commands = CommandFactory.getInstance().getAvailableCommands();
        this.session = session;
    }

    @Override
    public int complete(String buffer, int cursor, List<CharSequence> candidates) {
        Preconditions.checkNotNull(candidates);

        String text = contextualBuffer(buffer, cursor);

        List<String> commands = this.commands.stream().filter(c -> c.startsWith(text)).collect(Collectors.toList());
        if(commands.size()>0) {
            candidates.addAll(commands);

            if(candidates.size()==1) {
                candidates.set(0, candidates.get(0) + " ");
            }

            return candidates.isEmpty() ? -1 : 0;

        } else if(text.contains(" ")) {
            int insertion = text.lastIndexOf(" ") + 1;
            String tailBuffer = text.substring(insertion);

            List<String> files = SpashFileSystem.get().ls(this.session.getWorkingDir()).map(p -> p.getFileName().toString()).collect();

            candidates.addAll(files.stream().filter(f -> f.startsWith(tailBuffer)).collect(Collectors.toList()));

            if(candidates.size()==1) {
                candidates.set(0, candidates.get(0) + " ");
            }

            return candidates.isEmpty() ? -1 : insertion;
        }

        return -1;
    }

    private String contextualBuffer(String buffer, int cursor) {
        if(buffer==null) {
            return "";
        } else if(cursor<buffer.length()) {
            return buffer.substring(0, cursor);
        }
        return buffer;
    }
}
