package service.fs;

import controllers.routes;
import service.UtilException;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static service.UtilException.rethrowConsumer;
import static service.UtilException.rethrowFunction;

@Singleton
public class FsExecutor {

    public static final String DIRECTORY = "directory";
    @Inject
    PathEncoder pathEncoder;

    public FsItemEx getEx(PathEncoder.FsItem fsItem) throws IOException {
        return getEx(fsItem.volumeId, fsItem.path);
    }

    public FsItemEx getEx(String volumeId, Path path) throws IOException {
        FsItemEx result = new FsItemEx();
        result.mime = Files.probeContentType(path);
        result.name = path.getFileName().toString();
        result.hash = pathEncoder.getHashByPath(volumeId, path);
        result.phash = pathEncoder.getHashByPath(volumeId, path.getParent());
        if (result.phash == null) {
            result.volumeid = volumeId;
            result.locked = 1;
        }
        result.ts = Files.getLastModifiedTime(path).toInstant();
        result.size = Files.size(path);
        result.read = Files.isReadable(path) ? 1 : 0;
        result.write = Files.isWritable(path) ? 1 : 0;

        if (result.mime.endsWith(DIRECTORY)) {
            result.mime = DIRECTORY;
            result.size = 0L;
            try (DirectoryStream<Path> paths = Files.newDirectoryStream(path, Files::isDirectory)) {
                result.dirs = paths.iterator().hasNext() ? 1 : 0;
            }
        } else {
            result.url = routes.Frontend.file(result.hash).url();
        }

        return result;
    }

    public void addSubFolders(Map<String, FsItemEx> files, PathEncoder.FsItem fsItem) throws IOException {
        Files.walkFileTree(fsItem.path, new SimpleFileVisitor<Path>(){
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                FsItemEx itemEx = getEx(fsItem.volumeId, dir);
                files.put(itemEx.hash, itemEx);
                return super.preVisitDirectory(dir, attrs);
            }
        });
    }

    public void addChildren(Map<String, FsItemEx> files, PathEncoder.FsItem fsItem) throws IOException {
        Files.list(fsItem.path).forEach(rethrowConsumer(path -> {
            FsItemEx ex = getEx(fsItem.volumeId, path);
            files.put(ex.hash, ex);
        }));
    }

    public List<FsItemEx> move(List<PathEncoder.FsItem> moveFiles, PathEncoder.FsItem dst) {
        return moveFiles.stream().map(rethrowFunction(fsItem -> {
            return this.getEx(dst.volumeId, Files.move(fsItem.path, dst.path.resolve(fsItem.path.getFileName())));
        })).collect(Collectors.toList());
    }

    public List<FsItemEx> copy(List<PathEncoder.FsItem> moveFiles, PathEncoder.FsItem dst) {
        return moveFiles.stream().map(rethrowFunction(fsItem -> {
            return this.getEx(dst.volumeId, Files.copy(fsItem.path, dst.path.resolve(fsItem.path.getFileName())));
        })).collect(Collectors.toList());
    }
}
