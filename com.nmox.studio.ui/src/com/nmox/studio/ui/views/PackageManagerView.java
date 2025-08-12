package com.nmox.studio.ui.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.jface.viewers.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PackageManagerView extends ViewPart {

    public static final String ID = "com.nmox.studio.ui.packageManager";
    
    private TreeViewer dependencyTree;
    private TableViewer packageTable;
    private Text searchText;
    private Combo managerCombo;
    private Button installButton;
    private Button updateButton;
    private Button removeButton;
    private Text commandOutput;
    
    private String currentManager = "npm";

    @Override
    public void createPartControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NONE);
        container.setLayout(new GridLayout(1, false));

        createToolbar(container);
        
        SashForm sashForm = new SashForm(container, SWT.VERTICAL);
        sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        
        createPackageSection(sashForm);
        createOutputSection(sashForm);
        
        sashForm.setWeights(new int[] { 70, 30 });
    }

    private void createToolbar(Composite parent) {
        Composite toolbar = new Composite(parent, SWT.NONE);
        toolbar.setLayout(new GridLayout(6, false));
        toolbar.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

        Label managerLabel = new Label(toolbar, SWT.NONE);
        managerLabel.setText("Package Manager:");

        managerCombo = new Combo(toolbar, SWT.READ_ONLY);
        managerCombo.setItems(new String[] { "npm", "yarn", "pnpm", "bower" });
        managerCombo.select(0);
        managerCombo.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                currentManager = managerCombo.getText();
                refreshPackages();
            }
        });

        Label searchLabel = new Label(toolbar, SWT.NONE);
        searchLabel.setText("Search:");

        searchText = new Text(toolbar, SWT.BORDER | SWT.SEARCH);
        searchText.setLayoutData(new GridData(200, SWT.DEFAULT));
        searchText.setMessage("Search packages...");
        searchText.addListener(SWT.DefaultSelection, e -> searchPackages());

        installButton = new Button(toolbar, SWT.PUSH);
        installButton.setText("Install");
        installButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                installPackage();
            }
        });

        updateButton = new Button(toolbar, SWT.PUSH);
        updateButton.setText("Update All");
        updateButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                updatePackages();
            }
        });
    }

    private void createPackageSection(Composite parent) {
        SashForm packageSash = new SashForm(parent, SWT.HORIZONTAL);
        
        Group dependencyGroup = new Group(packageSash, SWT.NONE);
        dependencyGroup.setText("Dependencies");
        dependencyGroup.setLayout(new GridLayout(1, false));
        
        dependencyTree = new TreeViewer(dependencyGroup, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
        dependencyTree.getTree().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        dependencyTree.setContentProvider(new DependencyContentProvider());
        dependencyTree.setLabelProvider(new DependencyLabelProvider());
        
        Group availableGroup = new Group(packageSash, SWT.NONE);
        availableGroup.setText("Available Packages");
        availableGroup.setLayout(new GridLayout(1, false));
        
        packageTable = new TableViewer(availableGroup, SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL);
        packageTable.getTable().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        packageTable.getTable().setHeaderVisible(true);
        packageTable.getTable().setLinesVisible(true);
        
        TableViewerColumn nameColumn = new TableViewerColumn(packageTable, SWT.NONE);
        nameColumn.getColumn().setText("Package");
        nameColumn.getColumn().setWidth(200);
        
        TableViewerColumn versionColumn = new TableViewerColumn(packageTable, SWT.NONE);
        versionColumn.getColumn().setText("Version");
        versionColumn.getColumn().setWidth(100);
        
        TableViewerColumn descColumn = new TableViewerColumn(packageTable, SWT.NONE);
        descColumn.getColumn().setText("Description");
        descColumn.getColumn().setWidth(300);
        
        packageTable.setContentProvider(ArrayContentProvider.getInstance());
        packageTable.setLabelProvider(new PackageLabelProvider());
        
        packageSash.setWeights(new int[] { 40, 60 });
    }

    private void createOutputSection(Composite parent) {
        Group outputGroup = new Group(parent, SWT.NONE);
        outputGroup.setText("Command Output");
        outputGroup.setLayout(new GridLayout(1, false));
        
        commandOutput = new Text(outputGroup, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL | SWT.READ_ONLY);
        commandOutput.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        commandOutput.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
        commandOutput.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_GREEN));
    }

    private void refreshPackages() {
        List<Package> packages = loadInstalledPackages();
        dependencyTree.setInput(packages);
    }

    private List<Package> loadInstalledPackages() {
        List<Package> packages = new ArrayList<>();
        
        File packageFile = getPackageFile();
        if (packageFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(packageFile))) {
                packages.add(new Package("express", "4.18.2", "Fast web framework"));
                packages.add(new Package("react", "18.2.0", "UI library"));
                packages.add(new Package("webpack", "5.88.0", "Module bundler"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        return packages;
    }

    private File getPackageFile() {
        if ("npm".equals(currentManager) || "pnpm".equals(currentManager)) {
            return new File("package.json");
        } else if ("yarn".equals(currentManager)) {
            return new File("package.json");
        } else if ("bower".equals(currentManager)) {
            return new File("bower.json");
        }
        return new File("package.json");
    }

    private void searchPackages() {
        String query = searchText.getText();
        if (query.isEmpty()) {
            return;
        }
        
        commandOutput.append("Searching for: " + query + "\n");
        
        List<Package> results = new ArrayList<>();
        results.add(new Package(query, "latest", "Search result"));
        packageTable.setInput(results);
    }

    private void installPackage() {
        IStructuredSelection selection = (IStructuredSelection) packageTable.getSelection();
        if (!selection.isEmpty()) {
            Package pkg = (Package) selection.getFirstElement();
            String command = getInstallCommand(pkg.name);
            executeCommand(command);
        }
    }

    private void updatePackages() {
        String command = getUpdateCommand();
        executeCommand(command);
    }

    private String getInstallCommand(String packageName) {
        switch (currentManager) {
            case "npm":
                return "npm install " + packageName;
            case "yarn":
                return "yarn add " + packageName;
            case "pnpm":
                return "pnpm add " + packageName;
            case "bower":
                return "bower install " + packageName;
            default:
                return "npm install " + packageName;
        }
    }

    private String getUpdateCommand() {
        switch (currentManager) {
            case "npm":
                return "npm update";
            case "yarn":
                return "yarn upgrade";
            case "pnpm":
                return "pnpm update";
            case "bower":
                return "bower update";
            default:
                return "npm update";
        }
    }

    private void executeCommand(String command) {
        commandOutput.append("> " + command + "\n");
        commandOutput.append("Command execution simulated\n\n");
    }

    @Override
    public void setFocus() {
        searchText.setFocus();
    }

    private static class Package {
        String name;
        String version;
        String description;
        List<Package> dependencies;

        Package(String name, String version, String description) {
            this.name = name;
            this.version = version;
            this.description = description;
            this.dependencies = new ArrayList<>();
        }
    }

    private class DependencyContentProvider implements ITreeContentProvider {
        @Override
        public Object[] getElements(Object inputElement) {
            if (inputElement instanceof List) {
                return ((List<?>) inputElement).toArray();
            }
            return new Object[0];
        }

        @Override
        public Object[] getChildren(Object parentElement) {
            if (parentElement instanceof Package) {
                return ((Package) parentElement).dependencies.toArray();
            }
            return new Object[0];
        }

        @Override
        public Object getParent(Object element) {
            return null;
        }

        @Override
        public boolean hasChildren(Object element) {
            if (element instanceof Package) {
                return !((Package) element).dependencies.isEmpty();
            }
            return false;
        }
    }

    private class DependencyLabelProvider extends LabelProvider {
        @Override
        public String getText(Object element) {
            if (element instanceof Package) {
                Package pkg = (Package) element;
                return pkg.name + " @ " + pkg.version;
            }
            return super.getText(element);
        }
    }

    private class PackageLabelProvider extends LabelProvider implements ITableLabelProvider {
        @Override
        public String getColumnText(Object element, int columnIndex) {
            if (element instanceof Package) {
                Package pkg = (Package) element;
                switch (columnIndex) {
                    case 0:
                        return pkg.name;
                    case 1:
                        return pkg.version;
                    case 2:
                        return pkg.description;
                }
            }
            return null;
        }

        @Override
        public Image getColumnImage(Object element, int columnIndex) {
            return null;
        }
    }
}