package team.redrock.texteditor.util;

import javax.swing.tree.DefaultMutableTreeNode;
import java.io.File;

/**
 * Created by rain on 2023/6/2
 **/
public class FileNode extends DefaultMutableTreeNode {

    private final File file;

    public FileNode(File file) {
        super(file.getName());
        this.file = file;
    }

    public File getFile() {
        return file;
    }
}
