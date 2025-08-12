package com.nmox.studio.ui.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.part.ViewPart;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TerminalView extends ViewPart {

    public static final String ID = "com.nmox.studio.ui.terminal";
    
    private StyledText terminal;
    private Process shellProcess;
    private PrintWriter shellInput;
    private Thread outputThread;
    private Thread errorThread;
    
    private List<String> commandHistory = new ArrayList<>();
    private int historyIndex = -1;
    private StringBuilder currentLine = new StringBuilder();
    private int cursorPosition = 0;

    @Override
    public void createPartControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NONE);
        container.setLayout(new GridLayout(1, false));

        terminal = new StyledText(container, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
        terminal.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        terminal.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
        terminal.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
        terminal.setFont(new org.eclipse.swt.graphics.Font(Display.getDefault(), "Consolas", 10, SWT.NORMAL));

        terminal.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e);
            }
        });

        startShell();
        terminal.append("NMOX Studio Terminal\n");
        terminal.append("Type 'help' for available commands\n\n");
        showPrompt();
    }

    private void startShell() {
        try {
            ProcessBuilder pb = new ProcessBuilder();
            String os = System.getProperty("os.name").toLowerCase();
            
            if (os.contains("win")) {
                pb.command("cmd.exe");
            } else if (os.contains("mac")) {
                pb.command("/bin/zsh");
            } else {
                pb.command("/bin/bash");
            }
            
            pb.redirectErrorStream(false);
            shellProcess = pb.start();
            
            shellInput = new PrintWriter(new OutputStreamWriter(shellProcess.getOutputStream()), true);
            
            outputThread = new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(shellProcess.getInputStream()))) {
                    int c;
                    while ((c = reader.read()) != -1) {
                        final char ch = (char) c;
                        Display.getDefault().asyncExec(() -> {
                            if (!terminal.isDisposed()) {
                                if (ch == '\n') {
                                    terminal.append("\n");
                                    currentLine.setLength(0);
                                    cursorPosition = 0;
                                } else if (ch != '\r') {
                                    terminal.append(String.valueOf(ch));
                                }
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            outputThread.setDaemon(true);
            outputThread.start();
            
            errorThread = new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(shellProcess.getErrorStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        final String errorLine = line;
                        Display.getDefault().asyncExec(() -> {
                            if (!terminal.isDisposed()) {
                                terminal.append(errorLine + "\n");
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            errorThread.setDaemon(true);
            errorThread.start();
            
        } catch (IOException e) {
            terminal.append("Failed to start shell: " + e.getMessage() + "\n");
        }
    }

    private void handleKeyPress(KeyEvent e) {
        if (e.keyCode == SWT.CR || e.keyCode == SWT.LF) {
            String command = currentLine.toString();
            terminal.append("\n");
            
            if (!command.trim().isEmpty()) {
                commandHistory.add(command);
                historyIndex = commandHistory.size();
                executeCommand(command);
            } else {
                showPrompt();
            }
            
            currentLine.setLength(0);
            cursorPosition = 0;
            e.doit = false;
            
        } else if (e.keyCode == SWT.ARROW_UP) {
            if (historyIndex > 0) {
                historyIndex--;
                replaceCurrentLine(commandHistory.get(historyIndex));
            }
            e.doit = false;
            
        } else if (e.keyCode == SWT.ARROW_DOWN) {
            if (historyIndex < commandHistory.size() - 1) {
                historyIndex++;
                replaceCurrentLine(commandHistory.get(historyIndex));
            } else if (historyIndex == commandHistory.size() - 1) {
                historyIndex = commandHistory.size();
                replaceCurrentLine("");
            }
            e.doit = false;
            
        } else if (e.keyCode == SWT.BS) {
            if (cursorPosition > 0) {
                currentLine.deleteCharAt(cursorPosition - 1);
                cursorPosition--;
            } else {
                e.doit = false;
            }
            
        } else if (e.keyCode == SWT.DEL) {
            if (cursorPosition < currentLine.length()) {
                currentLine.deleteCharAt(cursorPosition);
            } else {
                e.doit = false;
            }
            
        } else if (e.keyCode == SWT.ARROW_LEFT) {
            if (cursorPosition > 0) {
                cursorPosition--;
            } else {
                e.doit = false;
            }
            
        } else if (e.keyCode == SWT.ARROW_RIGHT) {
            if (cursorPosition < currentLine.length()) {
                cursorPosition++;
            } else {
                e.doit = false;
            }
            
        } else if (e.keyCode == SWT.HOME) {
            cursorPosition = 0;
            e.doit = false;
            
        } else if (e.keyCode == SWT.END) {
            cursorPosition = currentLine.length();
            e.doit = false;
            
        } else if ((e.stateMask & SWT.CTRL) != 0 && e.keyCode == 'c') {
            terminal.append("^C\n");
            currentLine.setLength(0);
            cursorPosition = 0;
            showPrompt();
            e.doit = false;
            
        } else if (!Character.isISOControl(e.character)) {
            currentLine.insert(cursorPosition, e.character);
            cursorPosition++;
        }
    }

    private void replaceCurrentLine(String newLine) {
        int lineStart = terminal.getText().lastIndexOf('\n') + 1;
        int promptEnd = terminal.getText().indexOf('>', lineStart) + 2;
        terminal.replaceTextRange(promptEnd, terminal.getText().length() - promptEnd, newLine);
        currentLine = new StringBuilder(newLine);
        cursorPosition = newLine.length();
        terminal.setCaretOffset(terminal.getText().length());
    }

    private void executeCommand(String command) {
        if (shellInput != null) {
            shellInput.println(command);
            shellInput.flush();
        }
    }

    private void showPrompt() {
        Display.getDefault().asyncExec(() -> {
            if (!terminal.isDisposed()) {
                terminal.append("nmox> ");
                terminal.setCaretOffset(terminal.getText().length());
            }
        });
    }

    @Override
    public void setFocus() {
        terminal.setFocus();
    }

    @Override
    public void dispose() {
        if (shellProcess != null && shellProcess.isAlive()) {
            shellProcess.destroyForcibly();
        }
        if (outputThread != null) {
            outputThread.interrupt();
        }
        if (errorThread != null) {
            errorThread.interrupt();
        }
        super.dispose();
    }
}