package com.nmox.studio.ui.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.navigator.CommonNavigator;
import org.eclipse.ui.navigator.CommonViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.core.resources.IFile;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

public class WebProjectExplorerView extends CommonNavigator {

    public static final String ID = "com.nmox.studio.ui.webExplorer";

    @Override
    protected CommonViewer createCommonViewerObject(Composite parent) {
        CommonViewer viewer = super.createCommonViewerObject(parent);
        
        viewer.addDoubleClickListener(new IDoubleClickListener() {
            @Override
            public void doubleClick(DoubleClickEvent event) {
                IStructuredSelection selection = (IStructuredSelection) event.getSelection();
                if (!selection.isEmpty()) {
                    Object element = selection.getFirstElement();
                    if (element instanceof IFile) {
                        IFile file = (IFile) element;
                        openFileInEditor(file);
                    }
                }
            }
        });
        
        return viewer;
    }

    private void openFileInEditor(IFile file) {
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        try {
            IDE.openEditor(page, file);
        } catch (PartInitException e) {
            e.printStackTrace();
        }
    }
}