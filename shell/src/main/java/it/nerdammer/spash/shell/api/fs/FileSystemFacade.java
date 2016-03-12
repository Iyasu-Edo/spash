package it.nerdammer.spash.shell.api.fs;

import it.nerdammer.spash.shell.common.SpashCollection;

import java.nio.file.Path;

/**
 * An abstraction for a file system.
 *
 * @author Nicola Ferraro
 */
public interface FileSystemFacade {

    /**
     * Returns a collection of paths contained in the given path.
     *
     * @param path the base path
     * @return the sub paths
     */
    SpashCollection<Path> ls(String path);

}
