package team.redrock.texteditor.mvc.model;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by rain on 2023/6/3
 **/
public class TextEditorModel {
    public void readFile(File file, IOCallback<FileReader> callback) {
        try (FileReader fileReader = new FileReader(file)) {
            callback.onSuccess(fileReader);
        } catch (IOException e) {
            callback.onError(e);
        }
    }

    public void writeFile(File file, String text, IOCallback<FileWriter> callback) {
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                callback.onError(e);
            }
        }
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(text);
            callback.onSuccess(fileWriter);
        } catch (IOException e) {
            callback.onError(e);
        }
    }

    @FunctionalInterface
    public interface IOCallback<T> {
        void onSuccess(T item) throws IOException;

        default void onError(IOException e) {
            e.printStackTrace();
        }
    }
}
