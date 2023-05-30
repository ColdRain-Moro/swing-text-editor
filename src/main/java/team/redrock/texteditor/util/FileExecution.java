package team.redrock.texteditor.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by rain on 2023/5/30
 **/
public enum FileExecution {
    C(FileExecution::executeC);
    private FileExecutionHandler handler;

    FileExecution(FileExecutionHandler handler) {
        this.handler = handler;
    }

    private static final Executor executor = Executors.newSingleThreadExecutor();

    private static void executeC(File file, FileExecutionFinishHandler callback) {
        executor.execute(() -> {
            try {
                StringBuilder info = new StringBuilder();
                String error = "";
                Process exec = Runtime.getRuntime().exec(new String[]{"gcc", "-o", file.getAbsolutePath() + ".out", file.getAbsolutePath()});
                exec.waitFor();
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(exec.getInputStream()))) {
                    info.append(reader.readLine());
                    info.append("\n");
                }
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
