package com.nmox.studio.core.nature;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;

public class WebProjectNature implements IProjectNature {

    public static final String NATURE_ID = "com.nmox.studio.core.webNature";
    
    private IProject project;

    @Override
    public void configure() throws CoreException {
        // Configure project nature
    }

    @Override
    public void deconfigure() throws CoreException {
        // Deconfigure project nature
    }

    @Override
    public IProject getProject() {
        return project;
    }

    @Override
    public void setProject(IProject project) {
        this.project = project;
    }
}