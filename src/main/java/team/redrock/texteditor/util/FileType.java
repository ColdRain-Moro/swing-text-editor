package team.redrock.texteditor.util;

import org.fife.ui.rsyntaxtextarea.SyntaxConstants;

/**
 * Created by rain on 2023/5/29
 **/
public enum FileType {
    C("c", SyntaxConstants.SYNTAX_STYLE_C),
    JAVA("java", SyntaxConstants.SYNTAX_STYLE_JAVA),
    JS("js", SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT),
    TS("ts", SyntaxConstants.SYNTAX_STYLE_TYPESCRIPT),
    CSS("css", SyntaxConstants.SYNTAX_STYLE_CSS),
    KT("kt", SyntaxConstants.SYNTAX_STYLE_KOTLIN),
    GO("go", SyntaxConstants.SYNTAX_STYLE_GO),
    GROOVY("groovy", SyntaxConstants.SYNTAX_STYLE_GROOVY),
    SCALA("scala", SyntaxConstants.SYNTAX_STYLE_SCALA),
    PYTHON("python", SyntaxConstants.SYNTAX_STYLE_PYTHON),
    SQL("sql", SyntaxConstants.SYNTAX_STYLE_SQL),
    SH("sh", SyntaxConstants.SYNTAX_STYLE_UNIX_SHELL),
    LUA("lua", SyntaxConstants.SYNTAX_STYLE_LUA),
    ASSEMBLY("asm", SyntaxConstants.SYNTAX_STYLE_ASSEMBLER_X86),
    MD("md", SyntaxConstants.SYNTAX_STYLE_MARKDOWN),
    MAKEFILE("makefile", SyntaxConstants.SYNTAX_STYLE_MAKEFILE),
    XML("xml", SyntaxConstants.SYNTAX_STYLE_XML),
    YAML("yml", SyntaxConstants.SYNTAX_STYLE_YAML),
    JSON("json", SyntaxConstants.SYNTAX_STYLE_JSON),
    HTML("html", SyntaxConstants.SYNTAX_STYLE_HTML);

    private final String suffix;
    private final String highlight;
    FileType(String suffix, String highlight) {
        this.suffix = suffix;
        this.highlight = highlight;
    }

    public String getSuffix() {
        return suffix;
    }

    public String getHighlight() {
        return highlight;
    }
}
