package it.nerdammer.spash.shell.api.spark;

import it.nerdammer.spash.shell.api.fs.FileSystemFacade;
import it.nerdammer.spash.shell.api.fs.SpashFileSystem;
import it.nerdammer.spash.shell.common.SpashCollection;
import it.nerdammer.spash.shell.common.SpashCollectionListAdapter;
import it.nerdammer.spash.shell.common.SpashCollectionRDDAdapter;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.net.URI;
import java.nio.file.Path;
import java.util.List;

/**
 * The default implementation of the {@code SparkFacade}.
 *
 * @author Nicola Ferraro
 */
public class SparkFacadeImpl implements SparkFacade {

    private JavaSparkContext sc;

    public SparkFacadeImpl(JavaSparkContext sc) {
        this.sc = sc;
    }

    @Override
    public SpashCollection<String> read(Path file) {
        URI uri = SpashFileSystem.get().getURI(file.normalize().toString());
        JavaRDD<String> rdd = sc.textFile(uri.toString());

        return new SpashCollectionRDDAdapter<>(rdd);
    }

    @Override
    public void write(SpashCollection<String> content, Path file) {
        FileSystemFacade fs = SpashFileSystem.get();
        if(fs.exists(file.toString())) {
            fs.rm(file.toString(), true);
        }

        URI uri = SpashFileSystem.get().getURI(file.normalize().toString());
        content.toRDD(sc).saveAsTextFile(uri.toString());
    }

    @Override
    public SpashCollection<String> head(Path file, int lines) {
        URI uri = SpashFileSystem.get().getURI(file.normalize().toString());
        JavaRDD<String> rdd = sc.textFile(uri.toString());
        List<String> stream = rdd.take(lines);

        return new SpashCollectionListAdapter<>(stream);
    }

    @Override
    public <T> SpashCollection<T> parallelize(List<T> coll) {
        return new SpashCollectionRDDAdapter<>(sc.parallelize(coll));
    }
}
