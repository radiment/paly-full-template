package service.executors;

import play.data.DynamicForm;
import service.CommandExecutor;
import service.fs.PathEncoder;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.Iterator;

public class DimensionsCommandExecutor implements CommandExecutor {

    @Inject
    private PathEncoder pathEncoder;

    @Override
    public Object execute(DynamicForm dynamicForm) throws IOException {

        String target = dynamicForm.get("target");

        PathEncoder.FsItem image = pathEncoder.getPathByHash(target);

        String type = Files.probeContentType(image.path);

        if (type.startsWith("image/")) {
            try (ImageInputStream stream = ImageIO.createImageInputStream(image.path.toFile())) {
                Iterator<ImageReader> readers = ImageIO.getImageReaders(stream);
                if (readers.hasNext()) {
                    ImageReader reader = readers.next();
                    try {
                        reader.setInput(stream);
                        return Collections.singletonMap("dim", reader.getWidth(0) + "x" + reader.getHeight(0));
                    } finally {
                        reader.dispose();
                    }
                }
            }
        }
        return "";
    }
}
