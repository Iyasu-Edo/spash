package it.nerdammer.spash.shell.api.spark;

import it.nerdammer.spash.shell.common.SpashCollection;

import java.nio.file.Path;
import java.util.List;

/**
 * Provide useful Spark functions and methods to access HDFS from Spark.
 *
 * @author Nicola Ferraro
 */
public interface SparkFacade {

    /**
     * Returns a {@code SpashCollection} from the content of the file or directory.
     *
     * @param file the file or the directory to read
     * @return a collection of lines contained in the file
     */
    SpashCollection<String> read(Path file);

    /**
     * Writes the provided content to the file.
     *
     * @param content the content
     * @param file the output file
     */
    void write(SpashCollection<String> content, Path file);

    /**
     * Returns a {@code SpashCollection} from the first lines of the file or directory.
     *
     * @param file the file or the directory to read
     * @param lines the number of lines to show
     * @return a collection of the first lines contained in the file
     */
    SpashCollection<String> head(Path file, int lines);

    /**
     * Parallelizes a collection as an RDD backed {@code SpashCollection}.
     *
     * @param coll the standard java collection
     * @param <T> the type of the contained objects
     * @return the parallelized collection
     */
    <T> SpashCollection<T> parallelize(List<T> coll);

}
