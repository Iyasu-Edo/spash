package it.nerdammer.spash.shell.api.fs;

import it.nerdammer.spash.shell.common.SpashCollection;

import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFileAttributes;

/**
 * An abstraction for a file system.
 *
 * @author Nicola Ferraro
 */
public interface FileSystemFacade {

    /**
     * Returns the defaul filesystem.
     *
     * @return the filesystem
     */
    FileSystem getFileSystem();

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

    /**
     * Returns the full URI of the path, to identify the resource from outside the facade.
     *
     * @param path the path of the file
     * @return the URI of the path
     */
    URI getURI(String path);

    /**
     * Creates the directory corresponding to the given path.
     *
     * @param path the absolute path of the directory
     * @return true if the directory did not exist before
     */
    boolean mkdir(String path);

    /**
     * Removes the directory or file corresponding to the given path. If the target is a directory, it must be empty.
     *
     * @param path the absolute path of the directory or file
     * @param recursive true if the content of the directory should be removed as well
     * @return true if the target can be removed
     */
    boolean rm(String path, boolean recursive);

    /**
     * Gets the posix file attributes of the given file.
     *
     * @param path the file
     * @return the posix file attributes
     */
    PosixFileAttributes getAttributes(String path);

}
