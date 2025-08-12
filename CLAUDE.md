# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build Commands

```bash
# Build all plugins
mvn clean compile

# Create product distribution
mvn clean package

# Run tests
mvn test

# Run component validation tests
java TestRunner

# Start development environment
./run-development.sh
```

## Development Setup

This is an Eclipse RCP (Rich Client Platform) application built using Tycho Maven plugin. To develop:

1. Import all projects into Eclipse IDE
2. Set target platform to Eclipse 2023-12
3. Run as Eclipse Application from `com.nmox.studio.product`
4. Use `run-development.sh` for automated setup

## Architecture Overview

NMOX Studio Eclipse is a multi-module Eclipse plugin system providing web development tools:

### Core Modules

- **com.nmox.studio.core**: Foundation services, project nature, preferences
  - Exports: core model classes, server abstractions, utilities
  - Dependencies: Eclipse core runtime, resources, UI

- **com.nmox.studio.ui**: User interface components and perspectives  
  - Exports: perspectives, views, UI editors
  - Dependencies: Eclipse SWT, core module
  - Key classes: `WebDevelopmentPerspective`, `LivePreviewView`, `TerminalView`

- **com.nmox.studio.editor**: Language-specific editors
  - HTML, CSS, JavaScript, JSON, Markdown editors
  - Syntax highlighting and content assist
  - Dependencies: Eclipse text framework

- **com.nmox.studio.debug**: Debugging capabilities
  - Node.js launch configuration
  - Integration with Eclipse debug framework

- **com.nmox.studio.server**: Development server integration
  - Manages external development servers
  - Process lifecycle management

### Build System

- Maven + Tycho for Eclipse plugin builds
- Target platform: Eclipse 2023-12
- Java 17 required
- Product definition in `com.nmox.studio.product/nmox-studio.product`

### Plugin Architecture

Each module follows Eclipse plugin conventions:
- `META-INF/MANIFEST.MF`: Bundle metadata and dependencies
- `plugin.xml`: Extension points and contributions
- `pom.xml`: Maven/Tycho build configuration

### Key Extension Points

Views and editors are contributed via Eclipse extension points defined in `plugin.xml` files. The main perspective aggregates specialized views for web development workflow.

## Testing

Use `TestRunner.java` for component validation - tests plugin structure, manifests, and basic class loading without requiring full Eclipse platform.

## Known Issues & Workarounds

### Maven Build Issues
- **Problem**: Maven/Tycho builds may fail with p2 repository access errors when using Java 24+ 
- **Symptoms**: JAXP00010003 entity size limit errors, provisioning exceptions
- **Workaround**: 
  1. Use Eclipse IDE directly for development instead of Maven builds
  2. Run `./run-development.sh` to set up workspace and import projects
  3. Use "Run As > Eclipse Application" from Eclipse IDE to test functionality
- **Root Cause**: Java version compatibility and XML parsing limits in newer JVM versions

### Development Workflow
1. Use `./run-development.sh` to launch Eclipse with proper workspace
2. Import all projects into Eclipse workspace
3. Run individual plugins as Eclipse Application for testing
4. Maven builds work for clean operations but may fail for compilation due to p2 issues