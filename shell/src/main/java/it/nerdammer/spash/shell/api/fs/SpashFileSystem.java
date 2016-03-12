package it.nerdammer.spash.shell.api.fs;

/**
 * Holds a reference to the default file system.
 *
 * @author Nicola Ferraro
 */
public final class SpashFileSystem {

    private static final FileSystemFacade FACADE_INSTANCE = new FileSystemFacadeImpl("hdfshost", 8020);

    private SpashFileSystem() {
    }

    public static FileSystemFacade get() {
        return FACADE_INSTANCE;
    }

}
