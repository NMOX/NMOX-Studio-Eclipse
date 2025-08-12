package com.nmox.studio.editor.html;

import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;

public class HTMLPartitionScanner extends RuleBasedPartitionScanner {

    public static final String HTML_TAG = "__html_tag";
    public static final String HTML_COMMENT = "__html_comment";

    public HTMLPartitionScanner() {
        super();
        // Add HTML partitioning rules here
    }
}