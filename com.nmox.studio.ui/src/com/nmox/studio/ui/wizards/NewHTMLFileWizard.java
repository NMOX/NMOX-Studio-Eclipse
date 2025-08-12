package com.nmox.studio.ui.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

public class NewHTMLFileWizard extends Wizard implements INewWizard {

    public NewHTMLFileWizard() {
        setWindowTitle("New HTML File");
    }

    @Override
    public void init(IWorkbench workbench, IStructuredSelection selection) {
        // Initialize wizard
    }

    @Override
    public void addPages() {
        // Add wizard pages
    }

    @Override
    public boolean performFinish() {
        // Create HTML file
        return true;
    }
}