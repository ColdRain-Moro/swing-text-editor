package team.redrock.texteditor.util;

import javax.swing.*;

/**
 * Created by rain on 2023/6/1
 **/
public interface ProcessCallback {
    default void println(String line) {
        line.chars().forEach(c -> print((char) c));
        print('\n');
    };

    default void printlnSync(String line) {
        SwingUtilities.invokeLater(() -> println(line));
    }

    default void printSync(char c) {
        SwingUtilities.invokeLater(() -> printSync(c));
    }

    void print(char c);

    default void onComplete(int exitCode) {
        printlnSync("> process exited with code " + exitCode);
    };
}
