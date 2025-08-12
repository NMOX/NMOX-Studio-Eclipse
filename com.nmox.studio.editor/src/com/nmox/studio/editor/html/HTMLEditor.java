package com.nmox.studio.editor.html;

import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.texteditor.ITextEditorActionConstants;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.IViewPart;

public class HTMLEditor extends TextEditor {

    public static final String ID = "com.nmox.studio.editor.html";
    
    private HTMLConfiguration sourceViewerConfiguration;

    public HTMLEditor() {
        super();
        sourceViewerConfiguration = new HTMLConfiguration();
        setSourceViewerConfiguration(sourceViewerConfiguration);
        setDocumentProvider(new HTMLDocumentProvider());
    }

    @Override
    protected void createActions() {
        super.createActions();
        
        IAction formatAction = new HTMLFormatAction();
        formatAction.setText("Format HTML");
        setAction(ITextEditorActionConstants.FORMAT, formatAction);
        
        IAction previewAction = new HTMLPreviewAction();
        previewAction.setText("Live Preview");
        setAction("preview", previewAction);
    }

    @Override
    protected void initializeEditor() {
        super.initializeEditor();
        
        getDocumentProvider().getDocument(getEditorInput()).addDocumentListener(new IDocumentListener() {
            @Override
            public void documentChanged(DocumentEvent event) {
                updateLivePreview();
            }

            @Override
            public void documentAboutToBeChanged(DocumentEvent event) {
            }
        });
    }

    private void updateLivePreview() {
        try {
            IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
            if (window != null) {
                IViewPart viewPart = window.getActivePage().findView("com.nmox.studio.ui.livePreview");
                if (viewPart != null) {
                    // Refresh live preview
                    try {
                        viewPart.getClass().getMethod("refresh").invoke(viewPart);
                    } catch (Exception e) {
                        // Ignore reflection errors
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void doSave(org.eclipse.core.runtime.IProgressMonitor monitor) {
        super.doSave(monitor);
        updateLivePreview();
    }

    private class HTMLFormatAction implements IAction {
        @Override
        public void run() {
            ISourceViewer viewer = getSourceViewer();
            if (viewer != null) {
                viewer.doOperation(ISourceViewer.FORMAT);
            }
        }

        @Override
        public void addPropertyChangeListener(org.eclipse.jface.util.IPropertyChangeListener listener) {}
        @Override
        public int getAccelerator() { return 0; }
        @Override
        public String getActionDefinitionId() { return null; }
        @Override
        public String getDescription() { return "Format HTML document"; }
        @Override
        public org.eclipse.swt.graphics.ImageDescriptor getDisabledImageDescriptor() { return null; }
        @Override
        public org.eclipse.jface.resource.ImageDescriptor getHoverImageDescriptor() { return null; }
        @Override
        public String getId() { return "format"; }
        @Override
        public org.eclipse.jface.resource.ImageDescriptor getImageDescriptor() { return null; }
        @Override
        public org.eclipse.swt.widgets.Menu getMenu(org.eclipse.swt.widgets.Control parent) { return null; }
        @Override
        public org.eclipse.swt.widgets.Menu getMenu(org.eclipse.swt.widgets.Menu parent) { return null; }
        @Override
        public String getText() { return "Format"; }
        @Override
        public String getToolTipText() { return "Format HTML"; }
        @Override
        public boolean isChecked() { return false; }
        @Override
        public boolean isEnabled() { return true; }
        @Override
        public boolean isHandled() { return true; }
        @Override
        public void removePropertyChangeListener(org.eclipse.jface.util.IPropertyChangeListener listener) {}
        @Override
        public void runWithEvent(org.eclipse.swt.widgets.Event event) { run(); }
        @Override
        public void setActionDefinitionId(String id) {}
        @Override
        public void setChecked(boolean checked) {}
        @Override
        public void setDescription(String text) {}
        @Override
        public void setDisabledImageDescriptor(org.eclipse.jface.resource.ImageDescriptor newImage) {}
        @Override
        public void setEnabled(boolean enabled) {}
        @Override
        public void setHelpListener(org.eclipse.swt.events.HelpListener listener) {}
        @Override
        public void setHoverImageDescriptor(org.eclipse.jface.resource.ImageDescriptor newImage) {}
        @Override
        public void setId(String id) {}
        @Override
        public void setImageDescriptor(org.eclipse.jface.resource.ImageDescriptor newImage) {}
        @Override
        public void setText(String text) {}
        @Override
        public void setToolTipText(String text) {}
        @Override
        public void setAccelerator(int keycode) {}
    }

    private class HTMLPreviewAction implements IAction {
        @Override
        public void run() {
            updateLivePreview();
        }

        @Override
        public void addPropertyChangeListener(org.eclipse.jface.util.IPropertyChangeListener listener) {}
        @Override
        public int getAccelerator() { return 0; }
        @Override
        public String getActionDefinitionId() { return null; }
        @Override
        public String getDescription() { return "Show live preview"; }
        @Override
        public org.eclipse.swt.graphics.ImageDescriptor getDisabledImageDescriptor() { return null; }
        @Override
        public org.eclipse.jface.resource.ImageDescriptor getHoverImageDescriptor() { return null; }
        @Override
        public String getId() { return "preview"; }
        @Override
        public org.eclipse.jface.resource.ImageDescriptor getImageDescriptor() { return null; }
        @Override
        public org.eclipse.swt.widgets.Menu getMenu(org.eclipse.swt.widgets.Control parent) { return null; }
        @Override
        public org.eclipse.swt.widgets.Menu getMenu(org.eclipse.swt.widgets.Menu parent) { return null; }
        @Override
        public String getText() { return "Preview"; }
        @Override
        public String getToolTipText() { return "Live Preview"; }
        @Override
        public boolean isChecked() { return false; }
        @Override
        public boolean isEnabled() { return true; }
        @Override
        public boolean isHandled() { return true; }
        @Override
        public void removePropertyChangeListener(org.eclipse.jface.util.IPropertyChangeListener listener) {}
        @Override
        public void runWithEvent(org.eclipse.swt.widgets.Event event) { run(); }
        @Override
        public void setActionDefinitionId(String id) {}
        @Override
        public void setChecked(boolean checked) {}
        @Override
        public void setDescription(String text) {}
        @Override
        public void setDisabledImageDescriptor(org.eclipse.jface.resource.ImageDescriptor newImage) {}
        @Override
        public void setEnabled(boolean enabled) {}
        @Override
        public void setHelpListener(org.eclipse.swt.events.HelpListener listener) {}
        @Override
        public void setHoverImageDescriptor(org.eclipse.jface.resource.ImageDescriptor newImage) {}
        @Override
        public void setId(String id) {}
        @Override
        public void setImageDescriptor(org.eclipse.jface.resource.ImageDescriptor newImage) {}
        @Override
        public void setText(String text) {}
        @Override
        public void setToolTipText(String text) {}
        @Override
        public void setAccelerator(int keycode) {}
    }
}