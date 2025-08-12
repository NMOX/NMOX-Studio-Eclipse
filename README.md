# NMOX Studio Eclipse

A comprehensive web development Swiss Army knife built on the Eclipse Platform.

## Features

### 🎨 Modern Web Development IDE
- **Multi-language Support**: HTML, CSS, JavaScript, TypeScript, JSX, TSX, SCSS, SASS, LESS
- **Syntax Highlighting**: Advanced syntax highlighting and code formatting
- **IntelliSense**: Auto-completion and code suggestions
- **Live Preview**: Real-time preview of your web applications
- **Hot Reload**: Automatic refresh on file changes

### 🚀 Development Server Integration
- **Multiple Build Tools**: Support for npm, yarn, pnpm, webpack, vite, parcel
- **Dev Server Management**: Start/stop development servers with one click
- **Real-time Logs**: Monitor server output and errors
- **Port Management**: Automatic port detection and configuration

### 📦 Package Management
- **Dependency Viewer**: Visual dependency tree
- **Package Search**: Search and install packages from npm registry
- **Version Management**: Update and manage package versions
- **Multiple Package Managers**: npm, yarn, pnpm, bower support

### 🔧 Integrated Terminal
- **Native Terminal**: Full-featured terminal integration
- **Command History**: Navigate through command history
- **Multi-platform**: Works on Windows, macOS, and Linux
- **Context Awareness**: Automatically detects project directory

### 🐛 Debugging Capabilities
- **Node.js Debugging**: Full debugging support for Node.js applications
- **Browser Integration**: Debug web applications in browser
- **Breakpoint Management**: Set and manage breakpoints
- **Variable Inspection**: Inspect variables and call stacks

### 📁 Enhanced Project Explorer
- **Web Project Structure**: Optimized for web development workflows
- **File Type Recognition**: Smart file icons and associations
- **Quick Actions**: Context menus for common tasks
- **Search Integration**: Fast file and content search

### 🎯 Web Development Perspective
- **Optimized Layout**: Carefully designed layout for web development
- **Customizable Views**: Arrange views according to your workflow
- **Quick Access**: Toolbar shortcuts for common operations
- **Workspace Management**: Multiple workspace support

## Getting Started

### Prerequisites
- Java 17 or higher
- Node.js (for JavaScript/TypeScript development)
- Git (optional, for version control)

### Installation
1. Download the latest release from the releases page
2. Extract the archive to your preferred location
3. Run the `nmox-studio` executable
4. Select or create a workspace directory

### Creating Your First Web Project
1. File → New → Other → Web Development → Web Project
2. Choose your project template (HTML/CSS/JS, React, Vue, etc.)
3. Configure project settings
4. Click Finish

### Starting Development Server
1. Open your web project
2. Click the "Start Server" button in the toolbar
3. Choose your preferred build tool (npm, yarn, etc.)
4. The dev server will start and Live Preview will show your application

## Project Structure

```
com.nmox.studio.core/       # Core functionality and models
com.nmox.studio.ui/         # User interface components and views
com.nmox.studio.editor/     # Text editors for web languages
com.nmox.studio.debug/      # Debugging support
com.nmox.studio.server/     # Development server integration
com.nmox.studio.feature/    # Eclipse feature definition
com.nmox.studio.product/    # Product configuration
com.nmox.studio.target/     # Target platform definition
```

## Building from Source

### Requirements
- Maven 3.8+
- Tycho 3.0+
- Java 17+

### Build Commands
```bash
# Build all plugins
mvn clean compile

# Create product
mvn clean package

# Run tests
mvn test
```

## Features in Detail

### Live Preview
The Live Preview view provides real-time visualization of your web applications:
- Automatic refresh on file changes
- Support for HTML, CSS, and JavaScript hot reload
- Responsive design testing
- DevTools integration

### Server Console
Monitor your development server with the Server Console:
- Real-time server logs
- Error highlighting
- Server status indicators
- Multiple server support

### Package Manager
Comprehensive package management features:
- Visual dependency tree
- Package search and installation
- Version conflict detection
- Security vulnerability scanning

### Terminal Integration
Full-featured terminal with:
- Command history navigation
- Auto-completion
- Custom command shortcuts
- Project-aware working directory

## Keyboard Shortcuts

| Action | Shortcut |
|--------|----------|
| Start Server | Ctrl+Alt+S |
| Stop Server | Ctrl+Alt+X |
| Refresh Preview | F5 |
| Toggle Breakpoint | Ctrl+Shift+B |
| Open Terminal | Ctrl+Alt+T |
| Quick Search | Ctrl+Shift+R |

## Configuration

### Preferences
Access preferences via Window → Preferences → NMOX Studio:
- Server configurations
- Editor preferences
- Live preview settings
- Keyboard shortcuts

### Workspace Settings
Project-specific settings can be configured in `.nmox` directory:
- Server configuration
- Build tool preferences
- Environment variables

## Contributing

We welcome contributions! Please see our contributing guidelines for details.

### Development Setup
1. Clone the repository
2. Import projects into Eclipse IDE
3. Set up target platform
4. Run as Eclipse Application

## License

This project is licensed under the Eclipse Public License 2.0. See LICENSE file for details.

## Support

- 📖 Documentation: [Wiki](../../wiki)
- 🐛 Bug Reports: [Issues](../../issues)
- 💬 Discussions: [Discussions](../../discussions)
- 📧 Email: support@nmox.com

## Acknowledgments

Built on the Eclipse Platform with gratitude to the Eclipse Foundation and the open-source community.

---

**NMOX Studio Eclipse** - Empowering web developers with a comprehensive, modern IDE experience.