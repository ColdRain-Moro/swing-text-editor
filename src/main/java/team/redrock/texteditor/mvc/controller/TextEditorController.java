package team.redrock.texteditor.mvc.controller;

import team.redrock.texteditor.mvc.model.TextEditorModel;
import team.redrock.texteditor.transform.TextTransformer;
import team.redrock.texteditor.util.FileExecution;
import team.redrock.texteditor.util.FileTreeAdapter;
import team.redrock.texteditor.util.FileType;
import team.redrock.texteditor.mvc.view.TerminalWindow;
import team.redrock.texteditor.mvc.view.TextEditorWindow;

import javax.swing.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by rain on 2023/6/3
 **/
public class TextEditorController {
    private final JFileChooser fileChooser = new JFileChooser();
    private File openingFile;

    private final TextEditorWindow window;
    private final TextEditorModel model;

    public TextEditorController(TextEditorWindow window, TextEditorModel model) {
        this.window = window;
        this.model = model;

        FileTreeAdapter.setupListener(window.getFileTree(), file -> {
            if (!file.isDirectory()) setOpeningFile(file);
        });
    }

    public void setOpeningFile(File file) {
        this.openingFile = file;
        window.setTitle("TextEditor - " + file.getName());

        // 设置文件浏览器路径为当前路径
        FileTreeAdapter adapter = new FileTreeAdapter(file.getParentFile());
        adapter.apply(window.getFileTree());

        // transformer 如果有 transformer 就只读
        TextTransformer transformer = TextTransformer.transformers
                .stream()
                .filter(it -> it.shouldTransform(file))
                .findFirst()
                .orElse(null);
        window.getTextArea().setEditable(transformer == null);

        // 设置高亮
        String name = file.getName();
        String[] split = name.split("\\.");
        String suffix = split[split.length - 1];
        String style = Arrays.stream(FileType.values())
                .filter(ty -> ty.getSuffix().equals(suffix))
                .findFirst()
                .map(FileType::getHighlight)
                .orElse(null);
        window.getTextArea().setSyntaxEditingStyle(style);

        if (transformer != null) {
            FileType type = transformer.transform(file, window.getTextArea()::setText);
            window.getTextArea().setSyntaxEditingStyle(type == null ? null : type.getHighlight());
            return;
        }

        model.readFile(file, new TextEditorModel.IOCallback<FileReader>() {
            @Override
            public void onSuccess(FileReader item) throws IOException {
                window.getTextArea().read(item, null);
            }

            @Override
            public void onError(IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(window, "Error opening file", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    public void load() {
        if (fileChooser.showOpenDialog(window) == JFileChooser.APPROVE_OPTION) {
            setOpeningFile(fileChooser.getSelectedFile());
        }
    }

    public void save() {
        if (openingFile == null) return;
        model.writeFile(openingFile, window.getTextArea().getText(), new TextEditorModel.IOCallback<FileWriter>() {
            @Override
            public void onSuccess(FileWriter item) {
                JOptionPane.showMessageDialog(window, "保存完毕", "Notification", JOptionPane.INFORMATION_MESSAGE);
            }

            @Override
            public void onError(IOException e) {
                JOptionPane.showMessageDialog(window, "Error opening file", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    public void run() {
        if (openingFile == null) return;
        FileExecution execution = Arrays.stream(FileExecution.values())
                .filter(it -> it.getJudge().pass(openingFile.getName()))
                .findFirst()
                .orElse(null);
        if (execution == null) {
            JOptionPane.showMessageDialog(window, "该文件不是 TextEditor 支持运行的文件类型", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        TerminalWindow terminalWindow = new TerminalWindow(openingFile, execution);
        terminalWindow.setVisible(true);
    }
}
