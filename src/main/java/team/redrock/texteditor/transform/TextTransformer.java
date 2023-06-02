package team.redrock.texteditor.transform;

import team.redrock.texteditor.util.FileType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 *
 * transform 后的文件一律只读
 * <p>
 * 该功能一般用于翻译二进制文件
 * <p>
 * Created by rain on 2023/6/1
 **/
public interface TextTransformer {
    static List<TextTransformer> transformers = new ArrayList<>();

    public static void registerAll() {
        transformers.add(new JavaClassTransformer());
        transformers.add(new BinaryFileTransformer());
    }

    boolean shouldTransform(File file);

    /**
     *
     * @param contentReceiver 接受字符串内容并将其渲染到文本编辑器
     * @return 文件高亮类型
     */
    FileType transform(File file, Consumer<String> contentReceiver);
}
