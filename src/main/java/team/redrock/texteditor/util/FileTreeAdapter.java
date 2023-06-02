package team.redrock.texteditor.util;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import java.io.File;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Created by rain on 2023/6/2
 **/
public class FileTreeAdapter {

    private final File root;

    public FileTreeAdapter(File root) {
        this.root = root;
    }

    public void apply(JTree tree) {
        tree.removeAll();
        FileNode rootNode = new FileNode(root);
        walk(rootNode, root);
        DefaultTreeModel treeModel = new DefaultTreeModel(rootNode, false);
        tree.setModel(treeModel);
    }

    private void walk(FileNode parent, File parentFile) {
        if (!parentFile.isDirectory()) return;
        Optional.ofNullable(parentFile.listFiles())
                .ifPresent(files -> {
                    for (File file : files) {
                        // 暂时不显示文件夹
                        if (file.isDirectory()) continue;
                        FileNode node = new FileNode(file);
                        parent.add(node);
//                        walk(node, file);
                    }
                });
    }

    public static void setupListener(JTree tree, Consumer<File> onSelect) {
        tree.addTreeSelectionListener(e -> {
            Object lastSelectedPathComponent = tree.getLastSelectedPathComponent();
            if (lastSelectedPathComponent instanceof FileNode) {
                FileNode node = (FileNode) lastSelectedPathComponent;
                File file = node.getFile();
                onSelect.accept(file);
            }
        });
    }
}
