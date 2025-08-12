#!/bin/bash

# NMOX Studio Eclipse Development Runner
# This script sets up and runs the application in development mode

echo "🚀 NMOX Studio Eclipse - Development Runner"
echo "==========================================="

# Check if Eclipse is available
ECLIPSE_PATH=$(which eclipse 2>/dev/null)
if [ -z "$ECLIPSE_PATH" ]; then
    echo "❌ Eclipse not found in PATH"
    echo "   Please install Eclipse IDE and add it to your PATH"
    echo "   Download from: https://eclipse.org/downloads/"
    exit 1
fi

echo "✅ Eclipse found at: $ECLIPSE_PATH"

# Check Java version
JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2)
echo "✅ Java version: $JAVA_VERSION"

# Create workspace directory
WORKSPACE_DIR="$HOME/nmox-studio-workspace"
if [ ! -d "$WORKSPACE_DIR" ]; then
    mkdir -p "$WORKSPACE_DIR"
    echo "✅ Created workspace: $WORKSPACE_DIR"
else
    echo "✅ Using existing workspace: $WORKSPACE_DIR"
fi

# Check project structure
echo ""
echo "📁 Validating project structure..."
java TestRunner

if [ $? -eq 0 ]; then
    echo ""
    echo "🎯 Starting NMOX Studio Eclipse in development mode..."
    echo "   Workspace: $WORKSPACE_DIR"
    echo "   Project: $(pwd)"
    
    # Import project into Eclipse workspace
    echo ""
    echo "📥 To import the project into Eclipse:"
    echo "   1. Start Eclipse with workspace: $WORKSPACE_DIR"
    echo "   2. File > Import > Existing Projects into Workspace"
    echo "   3. Select root directory: $(pwd)"
    echo "   4. Import all NMOX Studio projects"
    echo ""
    echo "🔧 To run as Eclipse Application:"
    echo "   1. Right-click on com.nmox.studio.product"
    echo "   2. Run As > Eclipse Application"
    echo "   3. Or Debug As > Eclipse Application for debugging"
    
    # Try to launch Eclipse with the workspace
    if command -v open >/dev/null 2>&1; then
        # macOS
        echo "🚀 Launching Eclipse..."
        open -a Eclipse --args -data "$WORKSPACE_DIR"
    elif command -v gnome-open >/dev/null 2>&1; then
        # Linux with GNOME
        gnome-open eclipse -data "$WORKSPACE_DIR"
    else
        echo "💡 Launch Eclipse manually with: eclipse -data $WORKSPACE_DIR"
    fi
    
else
    echo "❌ Project structure validation failed"
    exit 1
fi