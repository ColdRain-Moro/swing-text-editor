import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import team.redrock.texteditor.util.ProcessCallback;
import team.redrock.texteditor.util.ProcessUtils;

import java.io.File;

/**
 * Created by rain on 2023/6/8
 **/
public class ProcessTest {
    @Test
    void testProcessRun() {
        StringBuilder buffer = new StringBuilder();
        ProcessUtils.executeCmd(new File("/Users/rain/"), new ProcessCallback() {
            @Override
            public void print(char c) {
                buffer.append(c);
            }

            @Override
            public void onComplete(int exitCode) {
                Assertions.assertTrue(buffer.toString().contains("Happy 20th birthday"));
            }
        }, "./test");
    }
}
