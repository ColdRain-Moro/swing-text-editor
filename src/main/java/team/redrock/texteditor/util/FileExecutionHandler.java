package team.redrock.texteditor.util;

import java.io.File;
import java.io.IOException;

/**
 * Created by rain on 2023/5/30
 **/
public interface FileExecutionHandler {
    /**
     * @param file 文件
     * @return 结果
     */
    void execute(File file, FileExecutionFinishHandler callback);
}
