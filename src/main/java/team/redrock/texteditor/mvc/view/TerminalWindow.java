package team.redrock.texteditor.mvc.view;

import team.redrock.texteditor.util.FileExecution;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;

/**
 * Created by rain on 2023/6/1
 **/
public class TerminalWindow extends JFrame {

    public TerminalWindow(File file, FileExecution execution) {
        setTitle("Terminal");
        setSize(800, 600);
        JTextArea textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        // 不可编辑
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setMargin(new Insets(5, 10, 5, 10));
        scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        add(scrollPane, BorderLayout.CENTER);

        execution.getHandler().handle(file, c -> textArea.setText(textArea.getText() + c));
    }
}
