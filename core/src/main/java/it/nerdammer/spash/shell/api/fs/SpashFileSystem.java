package it.nerdammer.spash.shell.api.fs;

import it.nerdammer.spash.shell.SpashConfig;

/**
 * Holds a reference to the default file system.
 *
 * @author Nicola Ferraro
 */
public final class SpashFileSystem {

    private static final FileSystemFacade FACADE_INSTANCE = new FileSystemFacadeImpl(SpashConfig.getInstance().hdfsHost(), SpashConfig.getInstance().hdfsPort());

    private SpashFileSystem() {
    }

    public static FileSystemFacade get() {
        return FACADE_INSTANCE;
    }

}
