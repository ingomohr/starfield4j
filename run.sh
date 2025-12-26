#!/bin/bash

# Run Starfield with proper module access for macOS fullscreen support
java --add-exports java.desktop/com.apple.eawt=ALL-UNNAMED \
     -cp bin com.example.starfield.Starfield
