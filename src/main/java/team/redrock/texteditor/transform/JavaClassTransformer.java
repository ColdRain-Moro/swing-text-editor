package team.redrock.texteditor.transform;

import team.redrock.texteditor.util.FileType;
import team.redrock.texteditor.util.ProcessCallback;
import team.redrock.texteditor.util.ProcessUtils;

import java.io.File;
import java.util.function.Consumer;

/**
 * Created by rain on 2023/6/1
 **/
public class JavaClassTransformer implements TextTransformer {

    @Override
    public boolean shouldTransform(File file) {
        return file.getName().endsWith(".class");
    }

    @Override
    public FileType transform(File file, Consumer<String> contentReceiver) {
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
        }, "javap", "-v", file.getAbsolutePath());
        return null;
    }
}
