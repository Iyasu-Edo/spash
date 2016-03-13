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

    /**
     * Retrieve the absolute path of a given path provided as string.
     * The given path can be either absolute or relative. When it is relative, the base path is used to
     * get the absolute path. Absolute paths are simply transformed into {@link Path} objects.
     *
     * @param base the current base path
     * @param path the path that will be converted
     * @return the absolute path object
     */
    Path getAbsolutePath(String base, String path);

    /**
     * Indicates wether the given path exists.
     *
     * @param path the path to check
     * @return true if it exists, false otherwise
     */
    boolean exists(String path);

    /**
     * Indicates wether the given path is a directory.
     *
     * @param path the path to check
     * @return true if it is a directory, false otherwise
     */
    boolean isDirectory(String path);

}
