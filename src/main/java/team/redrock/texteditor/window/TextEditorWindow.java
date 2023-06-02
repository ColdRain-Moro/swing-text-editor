package team.redrock.texteditor.window;

import com.formdev.flatlaf.fonts.jetbrains_mono.FlatJetBrainsMonoFont;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;
import team.redrock.texteditor.Main;
import team.redrock.texteditor.transform.TextTransformer;
import team.redrock.texteditor.util.FileExecution;
import team.redrock.texteditor.util.FileTreeAdapter;
import team.redrock.texteditor.util.FileType;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.StyleContext;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
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
    private final JTree fileTree;
    private final JFileChooser fileChooser = new JFileChooser();
    private File openingFile;

    public TextEditorWindow() {
        setTitle("TextEditor");
        setSize(800, 600);
        // close 就直接退出
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.textArea = new RSyntaxTextArea();
        textArea.setLineWrap(true);
        textArea.setMargin(new Insets(5, 10, 5, 10));
//        textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_C);
        textArea.setHighlightCurrentLine(false);

        fileTree = new JTree(new DefaultMutableTreeNode("nothing"));
        fileTree.setShowsRootHandles(true);

        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
        renderer.setClosedIcon(Main.CLOSED_ICON);
        renderer.setOpenIcon(Main.OPEN_ICON);
        renderer.setLeafIcon(Main.LEAF_ICON);
        fileTree.setCellRenderer(renderer);

        FileTreeAdapter.setupListener(fileTree, file -> {
            if (!file.isDirectory()) setOpeningFile(file);
        });
        JScrollPane fileTreeScrollPane = new JScrollPane(fileTree);
        fileTreeScrollPane.setPreferredSize(new Dimension(150, 1));
        fileTreeScrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));

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

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, fileTreeScrollPane, scrollPane);

        getContentPane().add(splitPane, BorderLayout.CENTER);

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
        runItem.addActionListener(e -> run());

        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(runItem);
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

    void run() {
        if (openingFile == null) return;
        FileExecution execution = Arrays.stream(FileExecution.values())
                .filter(it -> it.getJudge().pass(openingFile.getName()))
                .findFirst()
                .orElse(null);
        if (execution == null) {
            JOptionPane.showMessageDialog(this, "该文件不是 TextEditor 支持运行的文件类型", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        TerminalWindow terminalWindow = new TerminalWindow();
        terminalWindow.setVisible(true);
        execution.getHandler().handle(openingFile, c -> {
            terminalWindow.getTextArea().setText(
                    terminalWindow.getTextArea().getText() + c
            );
        });
    }

    private void setOpeningFile(File file) {
        this.openingFile = file;
        setTitle("TextEditor - " + file.getName());

        // 设置文件浏览器路径为当前路径
        FileTreeAdapter adapter = new FileTreeAdapter(file.getParentFile());
        adapter.apply(fileTree);

        // transformer 如果有 transformer 就只读
        TextTransformer transformer = TextTransformer.transformers
                .stream()
                .filter(it -> it.shouldTransform(file))
                .findFirst()
                .orElse(null);
        textArea.setEditable(transformer == null);

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

        if (transformer != null) {
            FileType type = transformer.transform(file, textArea::setText);
            textArea.setSyntaxEditingStyle(type == null ? null : type.getHighlight());
            return;
        }

        try (FileReader reader = new FileReader(openingFile)) {
            textArea.read(reader, null);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error opening file", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}