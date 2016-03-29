package it.nerdammer.spash.shell;

import ch.lambdaj.Lambda;
import ch.lambdaj.function.convert.DefaultStringConverter;
import ch.lambdaj.function.convert.PropertyExtractor;
import it.nerdammer.spash.shell.api.fs.SpashFileSystem;
import it.nerdammer.spash.shell.command.CommandFactory;
import jline.console.completer.Completer;
import jline.internal.Preconditions;
import org.hamcrest.text.StringStartsWith;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;

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

        List<String> commands = Lambda.filter(StringStartsWith.startsWith(text));
        if(commands.size()>0) {
            candidates.addAll(commands);

            if(candidates.size()==1) {
                candidates.set(0, candidates.get(0) + " ");
            }

            return candidates.isEmpty() ? -1 : 0;

        } else if(text.contains(" ")) {
            int insertion = text.lastIndexOf(" ") + 1;
            String tailBuffer = text.substring(insertion);
            Path p;

            List<String> files = Lambda.convert(Lambda.convert(SpashFileSystem.get().ls(this.session.getWorkingDir()).collect(), new PropertyExtractor<Object, Path>("fileName")), new DefaultStringConverter());
            Lambda.filter(StringStartsWith.startsWith(tailBuffer), files);

            candidates.addAll(files);

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
