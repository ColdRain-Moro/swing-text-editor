package team.redrock.texteditor.util;

import java.io.File;

/**
 * Created by rain on 2023/5/30
 **/
public enum FileExecution {
    C(name -> name.endsWith(".c"),FileExecution::executeC),
    JAVA(name -> name.endsWith(".java"), FileExecution::executeJava),
    JS(name -> name.endsWith(".js"), FileExecution::executeJs),
    PY(name -> name.endsWith(".py"), FileExecution::executePython),
    GO(name -> name.endsWith(".go"), FileExecution::executeGo),
    KOTLIN(name -> name.endsWith(".kt"), FileExecution::executeKotlin),
    RUST(name -> name.endsWith(".rs"), FileExecution::executeRust);

    private final Handler handler;
    private final Judge judge;

    FileExecution(Judge judge, Handler handler) {
        this.handler = handler;
        this.judge = judge;
    }

    public interface Judge {
        boolean pass(String fileName);
    }

    public interface Handler {
        void handle(File file, ProcessCallback callback);
    }

    private static void executeC(File file, ProcessCallback callback) {
        IOTask.runTask(() -> {
            File pwd = file.getParentFile();
            ProcessUtils.executeCmds(pwd, callback,
                    new String[] { "gcc", "-o", file.getAbsolutePath() + ".out", file.getAbsolutePath() },
                    new String[] { file.getAbsolutePath() + ".out" }
            );
        });
    }

    private static void executeJava(File file, ProcessCallback callback) {
        IOTask.runTask(() -> {
            File pwd = file.getParentFile();
            ProcessUtils.executeCmds(pwd, callback,
                    new String[] { "javac", file.getAbsolutePath() },
                    new String[] { "java", file.getName().split("\\.java")[0] }
            );
        });
    }

    private static void executeJs(File file, ProcessCallback callback) {
        IOTask.runTask(() -> {
            File pwd = file.getParentFile();
            ProcessUtils.executeCmds(pwd, callback, new String[] { "node", file.getAbsolutePath() });
        });
    }

    private static void executePython(File file, ProcessCallback callback) {
        IOTask.runTask(() -> {
            File pwd = file.getParentFile();
            ProcessUtils.executeCmds(pwd, callback, new String[] { "python3", file.getAbsolutePath() });
        });
    }

    private static void executeGo(File file, ProcessCallback callback) {
        IOTask.runTask(() -> {
            File pwd = file.getParentFile();
            ProcessUtils.executeCmds(pwd, callback, new String[]{ "go", "run", "." });
        });
    }

    private static void executeKotlin(File file, ProcessCallback callback) {
        IOTask.runTask(() -> {
            File pwd = file.getParentFile();
            ProcessUtils.executeCmds(pwd, callback,
                    new String[] { "kotlinc", file.getAbsolutePath() },
                    new String[] { "java", file.getName().split("\\.kt")[0] + "Kt" }
            );
        });
    }

    private static void executeRust(File file, ProcessCallback callback) {
        IOTask.runTask(() -> {
            File pwd = file.getParentFile();
            ProcessUtils.executeCmds(pwd, callback,
                    new String[] { "rustc", "-o", file.getAbsolutePath() + ".out", file.getAbsolutePath() },
                    new String[] { file.getAbsolutePath() + ".out" }
            );
        });
    }

    public Handler getHandler() {
        return handler;
    }

    public Judge getJudge() {
        return judge;
    }
}
