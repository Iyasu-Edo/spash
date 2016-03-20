package it.nerdammer.spash.shell.api.fs;

import it.nerdammer.spash.shell.common.SpashCollection;
import it.nerdammer.spash.shell.common.SpashCollectionStreamAdapter;

import java.net.URI;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.function.BiPredicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * A file system facade to access HDFS.
 *
 * @author Nicola Ferraro
 */
public class FileSystemFacadeImpl implements FileSystemFacade {

    private String host;

    private int port;

    public FileSystemFacadeImpl(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public FileSystem getFileSystem() {
        return FileSystems.getFileSystem(getURI("/"));
    }

    @Override
    public SpashCollection<Path> ls(String path) {
        if(path==null || !path.startsWith("/")) {
            throw new IllegalArgumentException("Paths must be absolute. Path=" + path);
        }

        try {
            URI uri = getURI(path);
            Path dir = Paths.get(uri);

            Stream<Path> children = StreamSupport.stream(Files.newDirectoryStream(dir).spliterator(), false);

            return new SpashCollectionStreamAdapter<>(children);
        } catch(RuntimeException e) {
            throw e;
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Path getAbsolutePath(String base, String path) {
        if(path==null || base==null) {
            throw new IllegalArgumentException("Both arguments must be provided. Base=" + base + ", Path=" + path);
        }
        if(!base.startsWith("/")) {
            throw new IllegalArgumentException("Base path must be absolute. Base=" + base);
        }

        try {
            if (path.startsWith("/")) {
                URI uri = getURI(path);
                Path p = Paths.get(uri);
                return p;
            }

            Path p = Paths.get(base, path);
            return p;

        } catch(RuntimeException e) {
            throw e;
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean exists(String path) {
        if(path==null || !path.startsWith("/")) {
            throw new IllegalArgumentException("Paths must be absolute. Path=" + path);
        }

        try {
            URI uri = getURI(path);
            Path p = Paths.get(uri);

            return Files.exists(p);

        } catch(RuntimeException e) {
            throw e;
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isDirectory(String path) {
        if(path==null || !path.startsWith("/")) {
            throw new IllegalArgumentException("Paths must be absolute. Path=" + path);
        }

        try {
            URI uri = getURI(path);
            Path p = Paths.get(uri);

            return Files.isDirectory(p);

        } catch(RuntimeException e) {
            throw e;
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public URI getURI(String path) {
        if(path==null || !path.startsWith("/")) {
            throw new IllegalArgumentException("Paths must be absolute. Path=" + path);
        }
        try {
            return new URI("hdfs://" + host + ":" + port + path);
        } catch(RuntimeException e) {
            throw e;
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean mkdir(String path) {
        if(path==null || !path.startsWith("/")) {
            throw new IllegalArgumentException("Paths must be absolute. Path=" + path);
        }

        try {
            URI uri = getURI(path);
            Path p = Paths.get(uri);

            Files.createDirectory(p);
            return true;

        } catch(FileAlreadyExistsException e) {
            return false;
        } catch(RuntimeException e) {
            throw e;
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean rm(String path, boolean recursive) {
        if(path==null || !path.startsWith("/")) {
            throw new IllegalArgumentException("Paths must be absolute. Path=" + path);
        }

        try {
            URI uri = getURI(path);
            Path target = Paths.get(uri);

            Stream<Path> children = Files.find(target, Integer.MAX_VALUE, (childPath, attr) -> true);
            if(!recursive) {
                long contained = children.count();
                if (contained > 1) {
                    return false;
                }
            } else {
                children.sorted((p1, p2) -> p2.normalize().toString().length() - p1.normalize().toString().length())
                        .forEach(p -> {
                    try {
                        Files.delete(p);
                    } catch (RuntimeException e) {
                        throw e;
                    } catch(Exception e) {
                        throw new RuntimeException(e);
                    }
                });
            }

            return true;

        } catch(RuntimeException e) {
            throw e;
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

}
