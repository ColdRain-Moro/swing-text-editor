package team.redrock.texteditor.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 * Created by rain on 2023/6/1
 **/
public class ProcessUtils {
    public static void executeCmds(File pwd, ProcessCallback callback, String[] ...args) {
        Arrays.stream(args).forEach(arr -> {
            callback.printlnSync("> " + String.join(" ", arr));
            executeCmd(pwd, callback, arr);
        });
    }

    public static void executeCmd(File pwd, ProcessCallback callback, String ...args) {
        try {
            Process exec = new ProcessBuilder()
                    .command(args)
                    .directory(pwd)
                    .redirectErrorStream(true)
                    .start();
            int exitCode = exec.waitFor();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(exec.getInputStream()))) {
                reader.lines().forEach(callback::println);
            }
            callback.onComplete(exitCode);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
