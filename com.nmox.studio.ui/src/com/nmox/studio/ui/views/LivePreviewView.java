package com.nmox.studio.ui.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.core.resources.*;

public class LivePreviewView extends ViewPart implements IResourceChangeListener {

    public static final String ID = "com.nmox.studio.ui.livePreview";
    
    private Text contentArea;
    private Text urlText;
    private Label statusLabel;
    private String currentUrl = "http://localhost:3000";
    private boolean autoRefresh = true;

    @Override
    public void createPartControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NONE);
        container.setLayout(new GridLayout(1, false));

        Composite topBar = new Composite(container, SWT.NONE);
        topBar.setLayout(new GridLayout(3, false));
        topBar.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

        Label urlLabel = new Label(topBar, SWT.NONE);
        urlLabel.setText("URL:");

        urlText = new Text(topBar, SWT.BORDER);
        urlText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        urlText.setText(currentUrl);
        urlText.addListener(SWT.DefaultSelection, e -> navigateTo(urlText.getText()));

        statusLabel = new Label(topBar, SWT.NONE);
        statusLabel.setText("Ready");

        contentArea = new Text(container, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
        contentArea.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        contentArea.setText("Live Preview Area\n\nThis will show your web application when connected to a development server.\n\nURL: " + currentUrl);

        navigateTo(currentUrl);
        
        ResourcesPlugin.getWorkspace().addResourceChangeListener(this, IResourceChangeEvent.POST_CHANGE);
    }

    public void navigateTo(String url) {
        if (contentArea != null && !contentArea.isDisposed()) {
            currentUrl = url;
            contentArea.setText("Live Preview Area\n\nURL: " + url + "\n\nConnecting to development server...");
        }
    }

    public void refresh() {
        if (contentArea != null && !contentArea.isDisposed()) {
            contentArea.setText("Live Preview Area\n\nURL: " + currentUrl + "\n\nRefreshed at: " + new java.util.Date());
        }
    }

    @Override
    public void setFocus() {
        if (contentArea != null) {
            contentArea.setFocus();
        }
    }

    @Override
    public void dispose() {
        ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
        super.dispose();
    }

    @Override
    public void resourceChanged(IResourceChangeEvent event) {
        if (!autoRefresh) {
            return;
        }

        IResourceDelta delta = event.getDelta();
        if (delta != null) {
            try {
                delta.accept(resDelta -> {
                    if (resDelta.getResource() instanceof IFile) {
                        IFile file = (IFile) resDelta.getResource();
                        String ext = file.getFileExtension();
                        if (ext != null && (ext.equals("html") || ext.equals("css") || 
                            ext.equals("js") || ext.equals("jsx") || ext.equals("ts") || 
                            ext.equals("tsx") || ext.equals("vue") || ext.equals("svelte"))) {
                            Display.getDefault().asyncExec(this::refresh);
                            return false;
                        }
                    }
                    return true;
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setAutoRefresh(boolean autoRefresh) {
        this.autoRefresh = autoRefresh;
    }
}