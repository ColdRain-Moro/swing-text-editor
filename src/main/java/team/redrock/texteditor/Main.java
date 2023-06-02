package team.redrock.texteditor;

import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.fonts.jetbrains_mono.FlatJetBrainsMonoFont;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import team.redrock.texteditor.transform.TextTransformer;
import team.redrock.texteditor.window.TextEditorWindow;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.io.File;

/**
 * Created by rain on 2023/5/29
 **/
public class Main {

    public static Icon CLOSED_ICON;
    public static Icon LEAF_ICON;
    public static Icon OPEN_ICON;

    public static void main(String[] args) {

        DefaultTreeCellRenderer defaultTreeCellRenderer = new DefaultTreeCellRenderer();
        CLOSED_ICON = defaultTreeCellRenderer.getDefaultClosedIcon();
        LEAF_ICON = defaultTreeCellRenderer.getDefaultLeafIcon();
        OPEN_ICON = defaultTreeCellRenderer.getDefaultOpenIcon();

        // 使用 FlatLight 主题
        FlatMacLightLaf.setup();
        // Jetbrains Mono 字体
        FlatJetBrainsMonoFont.install();
        FlatLaf.setPreferredMonospacedFontFamily(FlatJetBrainsMonoFont.FAMILY);
        // 在 macOS 系统上使用系统的 menuBar
        System.setProperty("apple.laf.useScreenMenuBar", "true");

        TextTransformer.registerAll();

        SwingUtilities.invokeLater(() -> {
            TextEditorWindow textEditor = args.length == 0 ? new TextEditorWindow() : new TextEditorWindow(new File(args[0]));
            textEditor.setVisible(true);
        });
    }
}
