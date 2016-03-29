package it.nerdammer.spash.shell.command.spi;

import com.google.common.collect.ImmutableMap;
import it.nerdammer.spash.shell.api.fs.SpashFileSystem;
import it.nerdammer.spash.shell.command.AbstractCommand;
import it.nerdammer.spash.shell.command.CommandResult;
import it.nerdammer.spash.shell.command.ExecutionContext;
import it.nerdammer.spash.shell.common.SerializableFunction;
import it.nerdammer.spash.shell.common.SpashCollection;
import it.nerdammer.spash.shell.common.SpashCollectionListAdapter;
import it.nerdammer.spash.shell.common.TabulatedValue;

import java.nio.file.Path;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * A command that lists the files contained in the current directory.
 *
 * @author Nicola Ferraro
 */
public class LsCommand extends AbstractCommand {

    public LsCommand(String commandString) {
        super(commandString, ImmutableMap.<String, Boolean>builder()
                .put("l", false)
                .build());
    }

    @Override
    public CommandResult execute(ExecutionContext ctx) {

        List<String> args = this.getArguments();
        if(args.size()>0) {
            return CommandResult.error(this, "Unexpected arguments: " + args);
        }

        SpashCollection<Path> files = SpashFileSystem.get().ls(ctx.getSession().getWorkingDir());

        SpashCollection<String> fileNames = files.map(new SerializableFunction<Path, String>() {
            @Override
            public String apply(Path v1) {
                return v1.getFileName().toString();
            }
        });

        SpashCollection<String> res = fileNames;
        if(this.getOptions().containsKey("l")) {
            res = toDetail(ctx, fileNames);
        }

        return CommandResult.success(this, res);
    }

    private SpashCollection<String> toDetail(ExecutionContext ctx, SpashCollection<String> fileColl) {
        List<String> files = fileColl.collect();
        if(files.isEmpty()) {
            return fileColl;
        }

        SimpleDateFormat fmtThisYear = new SimpleDateFormat("dd MMM HH:mm");
        SimpleDateFormat fmtPast = new SimpleDateFormat("dd MMM  yyyy");

        List<TabulatedValue> fileDets = new ArrayList<>();
        for(String file : files) {
            String fullFile = ctx.getSession().getWorkingDir() + file;

            TabulatedValue val = new TabulatedValue();

            PosixFileAttributes attr = SpashFileSystem.get().getAttributes(fullFile);
            Set<PosixFilePermission> perms = attr.permissions();

            StringBuilder permStr = new StringBuilder();
            permStr.append(attr.isDirectory() ? "d" : "-");
            permStr.append(perms.contains(PosixFilePermission.OWNER_READ) ? "r" : "-");
            permStr.append(perms.contains(PosixFilePermission.OWNER_WRITE) ? "w" : "-");
            permStr.append(perms.contains(PosixFilePermission.OWNER_EXECUTE) ? "x" : "-");
            permStr.append(perms.contains(PosixFilePermission.GROUP_READ) ? "r" : "-");
            permStr.append(perms.contains(PosixFilePermission.GROUP_WRITE) ? "w" : "-");
            permStr.append(perms.contains(PosixFilePermission.GROUP_EXECUTE) ? "x" : "-");
            permStr.append(perms.contains(PosixFilePermission.OTHERS_READ) ? "r" : "-");
            permStr.append(perms.contains(PosixFilePermission.OTHERS_WRITE) ? "w" : "-");
            permStr.append(perms.contains(PosixFilePermission.OTHERS_EXECUTE) ? "x" : "-");

            val.add(permStr.toString());

            val.add(String.valueOf(SpashFileSystem.get().ls(fullFile).collect().size() + 2));

            val.add(attr.owner().getName());
            val.add(attr.group().getName());

            val.add(String.valueOf(attr.size()));

            long fileTime = attr.lastModifiedTime().toMillis();
            Date fileDate = new Date(fileTime);

            Calendar lastYear = Calendar.getInstance();
            lastYear.add(Calendar.YEAR, -1);
            long lastYearTime = lastYear.getTimeInMillis();

            if(fileTime>lastYearTime) {
                val.add(fmtThisYear.format(fileDate));
            } else {
                val.add(fmtPast.format(fileDate));
            }

            val.add(file);
            fileDets.add(val);
        }

        List<Integer> combinedColSizes = fileDets.get(0).columnSizes();
        for(TabulatedValue val : fileDets) {
            List<Integer> colSizes = val.columnSizes();
            combinedColSizes = TabulatedValue.combineColumnSizes(combinedColSizes, colSizes);
        }

        List<String> res = new ArrayList<>();
        for(TabulatedValue val : fileDets) {
            String vStr = val.toString(combinedColSizes);
            res.add(vStr);
        }

        return new SpashCollectionListAdapter<>(res);
    }
}
