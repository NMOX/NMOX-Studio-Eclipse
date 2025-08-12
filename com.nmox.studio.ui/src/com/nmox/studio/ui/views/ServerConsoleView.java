package com.nmox.studio.ui.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ServerConsoleView extends ViewPart {

    public static final String ID = "com.nmox.studio.ui.serverConsole";
    
    private StyledText consoleText;
    private Combo projectCombo;
    private Combo serverTypeCombo;
    private Button startButton;
    private Button stopButton;
    private Button clearButton;
    private Label statusLabel;
    
    private Process serverProcess;
    private Thread outputThread;
    private IProject currentProject;
    private String serverType = "npm";

    @Override
    public void createPartControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NONE);
        container.setLayout(new GridLayout(1, false));

        Composite toolbar = new Composite(container, SWT.NONE);
        toolbar.setLayout(new GridLayout(7, false));
        toolbar.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

        Label projectLabel = new Label(toolbar, SWT.NONE);
        projectLabel.setText("Project:");

        projectCombo = new Combo(toolbar, SWT.READ_ONLY);
        projectCombo.setLayoutData(new GridData(150, SWT.DEFAULT));
        updateProjectList();
        projectCombo.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                selectProject(projectCombo.getText());
            }
        });

        Label serverLabel = new Label(toolbar, SWT.NONE);
        serverLabel.setText("Server:");

        serverTypeCombo = new Combo(toolbar, SWT.READ_ONLY);
        serverTypeCombo.setItems(new String[] { "npm", "yarn", "webpack", "vite", "parcel", "custom" });
        serverTypeCombo.select(0);
        serverTypeCombo.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                serverType = serverTypeCombo.getText();
            }
        });

        startButton = new Button(toolbar, SWT.PUSH);
        startButton.setText("Start");
        startButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                startServer();
            }
        });

        stopButton = new Button(toolbar, SWT.PUSH);
        stopButton.setText("Stop");
        stopButton.setEnabled(false);
        stopButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                stopServer();
            }
        });

        clearButton = new Button(toolbar, SWT.PUSH);
        clearButton.setText("Clear");
        clearButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                consoleText.setText("");
            }
        });

        statusLabel = new Label(container, SWT.NONE);
        statusLabel.setText("Server: Stopped");
        statusLabel.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

        consoleText = new StyledText(container, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.READ_ONLY);
        consoleText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        consoleText.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
        consoleText.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_GREEN));
    }

    private void updateProjectList() {
        IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
        List<String> projectNames = new ArrayList<>();
        for (IProject project : projects) {
            if (project.isOpen()) {
                projectNames.add(project.getName());
            }
        }
        projectCombo.setItems(projectNames.toArray(new String[0]));
        if (projectNames.size() > 0) {
            projectCombo.select(0);
            selectProject(projectCombo.getText());
        }
    }

    private void selectProject(String projectName) {
        currentProject = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
    }

    private void startServer() {
        if (currentProject == null) {
            appendToConsole("No project selected\n");
            return;
        }

        if (serverProcess != null && serverProcess.isAlive()) {
            appendToConsole("Server is already running\n");
            return;
        }

        String projectPath = currentProject.getLocation().toOSString();
        String command = getServerCommand();

        appendToConsole("Starting " + serverType + " server in " + projectPath + "\n");
        appendToConsole("Command: " + command + "\n");

        try {
            ProcessBuilder pb = new ProcessBuilder();
            pb.directory(new java.io.File(projectPath));
            
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                pb.command("cmd.exe", "/c", command);
            } else {
                pb.command("sh", "-c", command);
            }

            serverProcess = pb.start();
            
            startButton.setEnabled(false);
            stopButton.setEnabled(true);
            statusLabel.setText("Server: Running (" + serverType + ")");

            outputThread = new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(serverProcess.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        final String output = line;
                        Display.getDefault().asyncExec(() -> appendToConsole(output + "\n"));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(serverProcess.getErrorStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        final String output = line;
                        Display.getDefault().asyncExec(() -> appendToConsole("[ERROR] " + output + "\n"));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            outputThread.start();

        } catch (IOException e) {
            appendToConsole("Failed to start server: " + e.getMessage() + "\n");
            e.printStackTrace();
        }
    }

    private void stopServer() {
        if (serverProcess != null && serverProcess.isAlive()) {
            serverProcess.destroyForcibly();
            appendToConsole("\nServer stopped\n");
        }
        
        if (outputThread != null) {
            outputThread.interrupt();
        }
        
        startButton.setEnabled(true);
        stopButton.setEnabled(false);
        statusLabel.setText("Server: Stopped");
    }

    private String getServerCommand() {
        switch (serverType) {
            case "npm":
                return "npm run dev";
            case "yarn":
                return "yarn dev";
            case "webpack":
                return "webpack serve";
            case "vite":
                return "vite";
            case "parcel":
                return "parcel serve";
            default:
                return "npm start";
        }
    }

    private void appendToConsole(String text) {
        if (!consoleText.isDisposed()) {
            consoleText.append(text);
            consoleText.setSelection(consoleText.getText().length());
        }
    }

    @Override
    public void setFocus() {
        consoleText.setFocus();
    }

    @Override
    public void dispose() {
        stopServer();
        super.dispose();
    }
}