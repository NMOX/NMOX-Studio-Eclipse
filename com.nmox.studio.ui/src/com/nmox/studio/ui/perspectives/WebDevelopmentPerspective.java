package com.nmox.studio.ui.perspectives;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.console.IConsoleConstants;

public class WebDevelopmentPerspective implements IPerspectiveFactory {

    private static final String TOP_LEFT = "topLeft";
    private static final String BOTTOM_LEFT = "bottomLeft";
    private static final String BOTTOM = "bottom";
    private static final String TOP_RIGHT = "topRight";

    @Override
    public void createInitialLayout(IPageLayout layout) {
        String editorArea = layout.getEditorArea();

        IFolderLayout topLeft = layout.createFolder(TOP_LEFT, IPageLayout.LEFT, 0.25f, editorArea);
        topLeft.addView("com.nmox.studio.ui.webExplorer");
        topLeft.addView(IPageLayout.ID_PROJECT_EXPLORER);

        IFolderLayout bottom = layout.createFolder(BOTTOM, IPageLayout.BOTTOM, 0.75f, editorArea);
        bottom.addView(IConsoleConstants.ID_CONSOLE_VIEW);
        bottom.addView("com.nmox.studio.ui.terminal");
        bottom.addView("com.nmox.studio.ui.serverConsole");
        bottom.addView(IPageLayout.ID_PROBLEM_VIEW);
        bottom.addView(IPageLayout.ID_PROGRESS_VIEW);

        IFolderLayout topRight = layout.createFolder(TOP_RIGHT, IPageLayout.RIGHT, 0.65f, editorArea);
        topRight.addView("com.nmox.studio.ui.livePreview");
        topRight.addView(IPageLayout.ID_OUTLINE);

        IFolderLayout bottomLeft = layout.createFolder(BOTTOM_LEFT, IPageLayout.BOTTOM, 0.5f, TOP_LEFT);
        bottomLeft.addView("com.nmox.studio.ui.packageManager");

        layout.addShowViewShortcut("com.nmox.studio.ui.livePreview");
        layout.addShowViewShortcut("com.nmox.studio.ui.serverConsole");
        layout.addShowViewShortcut("com.nmox.studio.ui.terminal");
        layout.addShowViewShortcut("com.nmox.studio.ui.packageManager");
        layout.addShowViewShortcut("com.nmox.studio.ui.webExplorer");
        layout.addShowViewShortcut(IPageLayout.ID_PROJECT_EXPLORER);
        layout.addShowViewShortcut(IConsoleConstants.ID_CONSOLE_VIEW);

        layout.addNewWizardShortcut("com.nmox.studio.ui.newWebProject");
        layout.addNewWizardShortcut("com.nmox.studio.ui.newHTMLFile");
        layout.addNewWizardShortcut("org.eclipse.ui.wizards.new.folder");
        layout.addNewWizardShortcut("org.eclipse.ui.wizards.new.file");

        layout.addPerspectiveShortcut("com.nmox.studio.ui.webPerspective");
    }
}