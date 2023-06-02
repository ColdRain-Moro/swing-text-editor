package team.redrock.texteditor.util;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by rain on 2023/6/1
 **/
public class IOTask {

    private static final Executor executor = Executors.newCachedThreadPool();

    public static void runTask(Runnable task) {
        executor.execute(task);
    }
}
