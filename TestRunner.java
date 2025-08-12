import java.io.*;
import java.util.*;

/**
 * Simple test runner for NMOX Studio Eclipse components
 * Tests the basic functionality without requiring full Eclipse platform
 */
public class TestRunner {
    
    public static void main(String[] args) {
        System.out.println("NMOX Studio Eclipse - Component Test Runner");
        System.out.println("==========================================");
        
        TestRunner runner = new TestRunner();
        boolean allTestsPassed = true;
        
        // Test basic class loading
        allTestsPassed &= runner.testClassLoading();
        
        // Test plugin manifest validation
        allTestsPassed &= runner.testManifestValidation();
        
        // Test file structure
        allTestsPassed &= runner.testFileStructure();
        
        // Test basic UI components (mock)
        allTestsPassed &= runner.testUIComponents();
        
        System.out.println("\n==========================================");
        if (allTestsPassed) {
            System.out.println("✓ All tests passed! NMOX Studio structure is valid.");
        } else {
            System.out.println("✗ Some tests failed. Check the output above.");
        }
    }
    
    private boolean testClassLoading() {
        System.out.println("\n1. Testing Class Structure...");
        
        String[] requiredClasses = {
            "com.nmox.studio.core.Activator",
            "com.nmox.studio.ui.Activator", 
            "com.nmox.studio.ui.perspectives.WebDevelopmentPerspective",
            "com.nmox.studio.ui.views.LivePreviewView",
            "com.nmox.studio.editor.html.HTMLEditor"
        };
        
        boolean allFound = true;
        for (String className : requiredClasses) {
            String filePath = className.replace('.', '/') + ".java";
            File file = new File("src/" + filePath);
            
            // Check in each module
            boolean found = false;
            String[] modules = {"com.nmox.studio.core", "com.nmox.studio.ui", "com.nmox.studio.editor"};
            for (String module : modules) {
                File moduleFile = new File(module + "/src/" + filePath);
                if (moduleFile.exists()) {
                    System.out.println("  ✓ " + className);
                    found = true;
                    break;
                }
            }
            
            if (!found) {
                System.out.println("  ✗ " + className + " - NOT FOUND");
                allFound = false;
            }
        }
        
        return allFound;
    }
    
    private boolean testManifestValidation() {
        System.out.println("\n2. Testing Plugin Manifests...");
        
        String[] modules = {
            "com.nmox.studio.core",
            "com.nmox.studio.ui", 
            "com.nmox.studio.editor",
            "com.nmox.studio.debug"
        };
        
        boolean allValid = true;
        for (String module : modules) {
            File manifestFile = new File(module + "/META-INF/MANIFEST.MF");
            if (manifestFile.exists()) {
                try {
                    Properties props = new Properties();
                    props.load(new FileInputStream(manifestFile));
                    String symbolicName = props.getProperty("Bundle-SymbolicName");
                    if (symbolicName != null && symbolicName.contains(module)) {
                        System.out.println("  ✓ " + module + " manifest valid");
                    } else {
                        System.out.println("  ✗ " + module + " manifest invalid symbolic name");
                        allValid = false;
                    }
                } catch (Exception e) {
                    System.out.println("  ✗ " + module + " manifest read error: " + e.getMessage());
                    allValid = false;
                }
            } else {
                System.out.println("  ✗ " + module + " manifest missing");
                allValid = false;
            }
        }
        
        return allValid;
    }
    
    private boolean testFileStructure() {
        System.out.println("\n3. Testing File Structure...");
        
        String[] requiredFiles = {
            "pom.xml",
            "com.nmox.studio.core/pom.xml",
            "com.nmox.studio.ui/pom.xml",
            "com.nmox.studio.editor/pom.xml",
            "com.nmox.studio.feature/feature.xml",
            "com.nmox.studio.product/nmox-studio.product"
        };
        
        boolean allExist = true;
        for (String filePath : requiredFiles) {
            File file = new File(filePath);
            if (file.exists()) {
                System.out.println("  ✓ " + filePath);
            } else {
                System.out.println("  ✗ " + filePath + " - MISSING");
                allExist = false;
            }
        }
        
        return allExist;
    }
    
    private boolean testUIComponents() {
        System.out.println("\n4. Testing UI Component Interfaces...");
        
        // Test perspective implementation
        boolean perspectiveValid = testFileContains(
            "com.nmox.studio.ui/src/com/nmox/studio/ui/perspectives/WebDevelopmentPerspective.java",
            "IPerspectiveFactory"
        );
        
        // Test view implementations
        boolean livePreviewValid = testFileContains(
            "com.nmox.studio.ui/src/com/nmox/studio/ui/views/LivePreviewView.java", 
            "ViewPart"
        );
        
        boolean terminalValid = testFileContains(
            "com.nmox.studio.ui/src/com/nmox/studio/ui/views/TerminalView.java",
            "ViewPart"
        );
        
        System.out.println("  " + (perspectiveValid ? "✓" : "✗") + " WebDevelopmentPerspective");
        System.out.println("  " + (livePreviewValid ? "✓" : "✗") + " LivePreviewView");
        System.out.println("  " + (terminalValid ? "✓" : "✗") + " TerminalView");
        
        return perspectiveValid && livePreviewValid && terminalValid;
    }
    
    private boolean testFileContains(String filePath, String content) {
        try {
            File file = new File(filePath);
            if (!file.exists()) return false;
            
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                if (scanner.nextLine().contains(content)) {
                    scanner.close();
                    return true;
                }
            }
            scanner.close();
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}