package service.fs;

import org.apache.commons.codec.binary.Base64;
import play.Configuration;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Singleton
public class PathEncoder {

    private Map<String, Path> volumes;

    @Inject
    public PathEncoder(Configuration configuration) {
        Configuration fManager = configuration.getConfig("filemanager");
        List<String> paths = fManager.getStringList("paths");
        volumes = new HashMap<>(paths.size());
        char key = 'A';
        for (String path : paths) {
            volumes.put(key++ + "_", Paths.get(path).normalize().toAbsolutePath());
        }
    }

    public static class FsItem {
        public final String volumeId;
        public final Path path;

        public FsItem(String volumeId, Path path) {
            this.volumeId = volumeId;
            this.path = path;
        }
    }

    public List<FsItem> getVolumes() {
        return volumes.entrySet().stream().map(e -> new FsItem(e.getKey(), e.getValue())).collect(Collectors.toList());
    }

    public String getHashByPath(String volumeId, Path path) {
        if (!volumes.containsKey(volumeId)) return null;

        Path volume = volumes.get(volumeId);

        if (!path.toAbsolutePath().startsWith(volume))  return null;

        String relativePath = volume.relativize(path).toString();

        String pathHash = new String(Base64.encodeBase64(relativePath.getBytes()));
        pathHash = escape(pathHash);

        return volumeId + pathHash;
    }

    public boolean isVolume(String hash) {
        return volumes.containsKey(hash);
    }

    public FsItem getPathByHash(String hash) {

        if (hash == null || hash.trim().isEmpty()) return null;

        String pathHash = unEscape(hash.substring(2));
        String volumeId = hash.substring(0, 2);

        if (!volumes.containsKey(volumeId)) return null;

        Path volume = volumes.get(volumeId);
        String relativePath = new String(Base64.decodeBase64(pathHash));
        Path path = volume.resolve(relativePath).normalize();

        if (!path.toAbsolutePath().startsWith(volume)) return null;

        return new FsItem(volumeId, path);
    }

    private String[][] escapes = { { "\\+", "_P" }, { "\\-", "_M" }, { "\\/", "_S" }, { "\\.", "_D" }, { "\\=", "_E" } };

    private String escape(String string) {
        if (string == null || string.isEmpty()) return string;

        for (String[] escape : escapes) {
            string = string.replaceAll(escape[0], escape[1]);
        }

        return string;
    }

    private String unEscape(String string) {
        if (string == null || string.isEmpty()) return string;

        for (String[] escape : escapes) {
            string = string.replaceAll(escape[1], escape[0]);
        }

        return string;
    }
}
