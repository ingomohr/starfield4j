# Starfield Simulation

A Java-based starfield animation with native macOS fullscreen support.

## Building

To build the project, run:

```bash
mvn package
```

This will create an executable JAR file in the `target` directory.

## Running

To run the application using Maven:

```bash
mvn exec:exec
```

Or run the built JAR file directly:

```bash
java --add-exports java.desktop/com.apple.eawt=ALL-UNNAMED \
     -jar target/starfield4j-1.0-SNAPSHOT.jar
```

## Fullscreen Support

### macOS
On macOS, the application supports native fullscreen mode with smooth animations:

- **Keyboard shortcut**: `Cmd+Ctrl+F`
- **Mouse**: Click the green fullscreen button in the window title bar

The native fullscreen mode moves the window to a separate Space with the standard macOS animation.

### Important Note for Java 9+
The `--add-exports java.desktop/com.apple.eawt=ALL-UNNAMED` flag is required to access macOS-specific APIs in Java 9 and later versions. Without this flag, the application will fall back to borderless fullscreen mode instead of the native macOS fullscreen.
The Maven `exec:exec` goal is configured to include this flag automatically.

## Requirements

- Java 25 or later
- Maven 3.6 or later
- macOS (for native fullscreen support)

## Features

- 2000 animated stars creating a 3D depth effect
- Smooth animation at ~100 FPS
- Native macOS fullscreen support with keyboard shortcut