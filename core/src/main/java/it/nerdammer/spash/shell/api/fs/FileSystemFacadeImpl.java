package it.nerdammer.spash.shell.api.fs;

import com.google.common.collect.Lists;
import it.nerdammer.spash.shell.common.SpashCollection;
import it.nerdammer.spash.shell.common.SpashCollectionListAdapter;

import java.io.IOException;
import java.net.URI;
import java.nio.file.*;
import java.nio.file.attribute.PosixFileAttributes;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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

            List<Path> children = Lists.newArrayList(Files.newDirectoryStream(dir, new DirectoryStream.Filter<Path>() {
                @Override
                public boolean accept(Path entry) throws IOException {
                    return true;
                }
            }));

            return new SpashCollectionListAdapter<>(children);
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

            List<Path> children = Lists.newArrayList(Files.newDirectoryStream(target, new DirectoryStream.Filter<Path>() {
                @Override
                public boolean accept(Path entry) throws IOException {
                    return true;
                }
            }));

            if(!recursive) {
                long contained = children.size();
                if (contained > 0) {
                    return false;
                }
            } else {
                Collections.sort(children, new Comparator<Path>() {
                    @Override
                    public int compare(Path p1, Path p2) {
                        return p2.normalize().toString().length() - p1.normalize().toString().length();
                    }
                });

                for (Path p : children) {
                    try {
                        Files.delete(p);
                    } catch (RuntimeException e) {
                        throw e;
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            Files.delete(target);

            return true;

        } catch(RuntimeException e) {
            throw e;
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PosixFileAttributes getAttributes(String path) {
        if(path==null || !path.startsWith("/")) {
            throw new IllegalArgumentException("Paths must be absolute. Path=" + path);
        }

        try {
            URI uri = getURI(path);
            Path target = Paths.get(uri);

            PosixFileAttributes attrs = Files.readAttributes(target, PosixFileAttributes.class, LinkOption.NOFOLLOW_LINKS);
            return attrs;

        } catch(RuntimeException e) {
            throw e;
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}
