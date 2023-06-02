package team.redrock.texteditor.window;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Created by rain on 2023/6/1
 **/
public class TerminalWindow extends JFrame {

    private final JTextArea textArea;

    public TerminalWindow() {
        setTitle("Terminal");
        setSize(800, 600);
        textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        // 不可编辑
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setMargin(new Insets(5, 10, 5, 10));
        scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        getContentPane().add(scrollPane, BorderLayout.CENTER);
    }

    public JTextArea getTextArea() {
        return textArea;
    }
}
