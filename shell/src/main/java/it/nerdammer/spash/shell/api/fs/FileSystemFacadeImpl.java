package it.nerdammer.spash.shell.api.fs;

import it.nerdammer.spash.shell.common.SpashCollection;
import it.nerdammer.spash.shell.common.SpashCollectionStreamAdapter;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * The implementation of the file system.
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
    public SpashCollection<Path> ls(String path) {
        if(path==null || !path.startsWith("/")) {
            throw new IllegalArgumentException("Paths must be absolute. Path=" + path);
        }

        try {
            URI uri = new URI("hdfs://" + host + ":" + port + path);
            Path dir = Paths.get(uri);

            Stream<Path> children = StreamSupport.stream(Files.newDirectoryStream(dir).spliterator(), false);

            return new SpashCollectionStreamAdapter<>(children);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}
