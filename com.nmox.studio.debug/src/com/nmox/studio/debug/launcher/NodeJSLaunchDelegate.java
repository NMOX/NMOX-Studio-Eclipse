package com.nmox.studio.debug.launcher;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.core.DebugPlugin;

import java.util.ArrayList;
import java.util.List;

public class NodeJSLaunchDelegate implements ILaunchConfigurationDelegate {

    public static final String ATTR_NODE_EXECUTABLE = "node_executable";
    public static final String ATTR_SCRIPT_PATH = "script_path";
    public static final String ATTR_SCRIPT_ARGUMENTS = "script_arguments";
    public static final String ATTR_WORKING_DIRECTORY = "working_directory";
    public static final String ATTR_NODE_ARGUMENTS = "node_arguments";
    public static final String ATTR_DEBUG_PORT = "debug_port";

    @Override
    public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, 
                      IProgressMonitor monitor) throws CoreException {
        
        if (monitor.isCanceled()) {
            return;
        }

        String nodeExecutable = configuration.getAttribute(ATTR_NODE_EXECUTABLE, "node");
        String scriptPath = configuration.getAttribute(ATTR_SCRIPT_PATH, "");
        String scriptArguments = configuration.getAttribute(ATTR_SCRIPT_ARGUMENTS, "");
        String workingDirectory = configuration.getAttribute(ATTR_WORKING_DIRECTORY, "");
        String nodeArguments = configuration.getAttribute(ATTR_NODE_ARGUMENTS, "");
        int debugPort = configuration.getAttribute(ATTR_DEBUG_PORT, 9229);

        if (scriptPath.isEmpty()) {
            throw new CoreException(org.eclipse.core.runtime.Status.error("Script path is required"));
        }

        List<String> commandLine = new ArrayList<>();
        commandLine.add(nodeExecutable);

        if ("debug".equals(mode)) {
            commandLine.add("--inspect=" + debugPort);
        }

        if (!nodeArguments.isEmpty()) {
            String[] nodeArgs = nodeArguments.split("\\s+");
            for (String arg : nodeArgs) {
                if (!arg.trim().isEmpty()) {
                    commandLine.add(arg.trim());
                }
            }
        }

        commandLine.add(scriptPath);

        if (!scriptArguments.isEmpty()) {
            String[] scriptArgs = scriptArguments.split("\\s+");
            for (String arg : scriptArgs) {
                if (!arg.trim().isEmpty()) {
                    commandLine.add(arg.trim());
                }
            }
        }

        ProcessBuilder processBuilder = new ProcessBuilder(commandLine);
        
        if (!workingDirectory.isEmpty()) {
            processBuilder.directory(new java.io.File(workingDirectory));
        }

        try {
            Process process = processBuilder.start();
            
            IProcess eclipseProcess = DebugPlugin.newProcess(launch, process, 
                "Node.js - " + scriptPath);
            
            eclipseProcess.setAttribute(IProcess.ATTR_CMDLINE, String.join(" ", commandLine));
            
        } catch (Exception e) {
            throw new CoreException(org.eclipse.core.runtime.Status.error("Failed to launch Node.js application", e));
        }
    }
}