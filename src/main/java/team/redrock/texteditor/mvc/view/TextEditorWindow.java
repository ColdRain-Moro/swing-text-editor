package team.redrock.texteditor.mvc.view;

import com.formdev.flatlaf.fonts.jetbrains_mono.FlatJetBrainsMonoFont;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;
import team.redrock.texteditor.Main;
import team.redrock.texteditor.mvc.controller.TextEditorController;
import team.redrock.texteditor.mvc.model.TextEditorModel;
import team.redrock.texteditor.util.FileTreeAdapter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.StyleContext;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rain on 2023/5/29
 **/
public class TextEditorWindow extends JFrame {

    private final RSyntaxTextArea textArea;
    private final JTree fileTree;

    private final TextEditorController controller;

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

        // init controller
        this.controller = new TextEditorController(this, new TextEditorModel());

        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
        renderer.setClosedIcon(Main.CLOSED_ICON);
        renderer.setOpenIcon(Main.OPEN_ICON);
        renderer.setLeafIcon(Main.LEAF_ICON);
        fileTree.setCellRenderer(renderer);

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

        add(splitPane, BorderLayout.CENTER);

        // menu
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("文件");
        JMenuItem openItem = new JMenuItem("打开");
        JMenuItem saveItem = new JMenuItem("保存");
        JMenuItem runItem = new JMenuItem("运行");
        JMenuItem closeItem = new JMenuItem("关闭");

        openItem.addActionListener(e -> controller.load());
        saveItem.addActionListener(e -> controller.save());
        closeItem.addActionListener(e -> System.exit(0));
        runItem.addActionListener(e -> controller.run());

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
        controller.setOpeningFile(openingFile);
    }

    public RSyntaxTextArea getTextArea() {
        return textArea;
    }

    public JTree getFileTree() {
        return fileTree;
    }
}