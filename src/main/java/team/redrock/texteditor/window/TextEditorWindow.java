package team.redrock.texteditor.window;

import com.formdev.flatlaf.fonts.jetbrains_mono.FlatJetBrainsMonoFont;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;
import team.redrock.texteditor.util.FileType;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rain on 2023/5/29
 **/
public class TextEditorWindow extends JFrame {

    private final RSyntaxTextArea textArea;
    private final JFileChooser fileChooser = new JFileChooser();
    private File openingFile;

    public TextEditorWindow() {
        setTitle("TextEditor");
        setSize(800, 600);
        // close 就直接退出
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        this.textArea = new RSyntaxTextArea();
        textArea.setLineWrap(true);
        textArea.setMargin(new Insets(5, 10, 5, 10));
//        textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_C);
        textArea.setHighlightCurrentLine(false);

        // 设置字体 支持中文
        final Font f = StyleContext.getDefaultStyleContext().getFont(FlatJetBrainsMonoFont.FAMILY, Font.PLAIN, 13);
        final Map<TextAttribute, Object> textAttributes = new HashMap<>();
        textAttributes.put(TextAttribute.KERNING, TextAttribute.KERNING_ON);
        textAttributes.put(TextAttribute.LIGATURES, TextAttribute.LIGATURES_ON);
        textArea.setFont(
                f.deriveFont(textAttributes)
        );

        RTextScrollPane scrollPane = new RTextScrollPane(textArea);
        scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        // menu
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("文件");
        JMenuItem openItem = new JMenuItem("打开");
        JMenuItem saveItem = new JMenuItem("保存");
        JMenuItem runItem = new JMenuItem("运行");
        JMenuItem closeItem = new JMenuItem("关闭");

        openItem.addActionListener(e -> load());
        saveItem.addActionListener(e -> save());
        closeItem.addActionListener(e -> System.exit(0));

        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.addSeparator();
        fileMenu.add(closeItem);

        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
    }

    public TextEditorWindow(File openingFile) {
        this();
        setOpeningFile(openingFile);
    }

    void load() {
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            setOpeningFile(fileChooser.getSelectedFile());
        }
    }

    void save() {
        if (openingFile == null) return;
        if (!openingFile.exists()) {
            openingFile.getParentFile().mkdirs();
            try {
                openingFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error creating file", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        try (FileWriter fileWriter = new FileWriter(openingFile)) {
            fileWriter.write(textArea.getText());
            JOptionPane.showMessageDialog(this, "保存完毕", "Notification", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error opening file", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setOpeningFile(File file) {
        this.openingFile = file;
        setTitle("TextEditor - " + file.getName());
        try (FileReader reader = new FileReader(openingFile)) {
            textArea.read(reader, null);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error opening file", "Error", JOptionPane.ERROR_MESSAGE);
        }
        // 设置高亮
        String name = file.getName();
        String[] split = name.split("\\.");
        String suffix = split[split.length - 1];
        String style = Arrays.stream(FileType.values())
                .filter(ty -> ty.getSuffix().equals(suffix))
                .findFirst()
                .map(FileType::getHighlight)
                .orElse(null);
        textArea.setSyntaxEditingStyle(style);
    }

}