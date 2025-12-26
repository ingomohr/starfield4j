# Starfield Simulation

A Java-based starfield animation with native macOS fullscreen support.

## Building

To build the project, run:

```bash
./build.sh
```

Or manually:

```bash
javac --add-exports java.desktop/com.apple.eawt=ALL-UNNAMED \
      -d bin \
      src/main/java/com/example/starfield/Starfield.java
```

## Running

To run the application, use:

```bash
./run.sh
```

Or manually:

```bash
java --add-exports java.desktop/com.apple.eawt=ALL-UNNAMED \
     -cp bin com.example.starfield.Starfield
```

## Fullscreen Support

### macOS
On macOS, the application supports native fullscreen mode with smooth animations:

- **Keyboard shortcut**: `Cmd+Ctrl+F`
- **Mouse**: Click the green fullscreen button in the window title bar

The native fullscreen mode moves the window to a separate Space with the standard macOS animation.

### Important Note for Java 9+
The `--add-exports java.desktop/com.apple.eawt=ALL-UNNAMED` flag is required to access macOS-specific APIs in Java 9 and later versions. Without this flag, the application will fall back to borderless fullscreen mode instead of the native macOS fullscreen.

## Requirements

- Java 9 or later
- macOS (for native fullscreen support)

## Features

- 2000 animated stars creating a 3D depth effect
- Smooth animation at ~100 FPS
- Native macOS fullscreen support with keyboard shortcut