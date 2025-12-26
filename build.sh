#!/bin/bash

# Build Starfield with proper module access for macOS fullscreen support
javac --add-exports java.desktop/com.apple.eawt=ALL-UNNAMED \
      -d bin \
      src/main/java/com/example/starfield/Starfield.java

echo "Build complete. Run with ./run.sh"
