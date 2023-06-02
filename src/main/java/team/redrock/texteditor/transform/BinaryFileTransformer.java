package team.redrock.texteditor.transform;

import org.apache.tika.Tika;
import team.redrock.texteditor.util.FileType;
import team.redrock.texteditor.util.IOTask;
import team.redrock.texteditor.util.ProcessCallback;
import team.redrock.texteditor.util.ProcessUtils;

import java.io.File;
import java.util.function.Consumer;

/**
 * Created by rain on 2023/6/2
 **/
public class BinaryFileTransformer implements TextTransformer {

    private final Tika tika = new Tika();

    @Override
    public boolean shouldTransform(File file) {
        try {
            String contentType = tika.detect(file);
            return contentType.startsWith("application/octet-stream");
        } catch (Throwable e) {
            return false;
        }
    }

    @Override
    public FileType transform(File file, Consumer<String> contentReceiver) {
        IOTask.runTask(() -> {
            StringBuilder buffer = new StringBuilder();
            ProcessUtils.executeCmd(file.getParentFile(), new ProcessCallback() {
                @Override
                public void print(char c) {
                    buffer.append(c);
                }

                @Override
                public void onComplete(int exitCode) {
                    contentReceiver.accept(buffer.toString());
                }
            }, "objdump", "-d", file.getAbsolutePath());
        });
        return FileType.ASSEMBLY;
    }
}
