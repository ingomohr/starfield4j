package com.example.starfield;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.lang.reflect.Method;
import java.util.Random;
import javax.swing.*;

public class Starfield extends JFrame {

    private JMenuItem enterFullScreenItem;
    private JMenuItem exitFullScreenItem;

    public Starfield() {
        initUI();
    }

    private void setupMacOSFullscreenSupport() {
        try {
            // Setup fullscreen capability for macOS using reflection
            // This works with Java 9+ when proper module access is granted
            Class<?> util = Class.forName("com.apple.eawt.FullScreenUtilities");
            Method setWindowCanFullScreen = util.getMethod(
                "setWindowCanFullScreen",
                java.awt.Window.class,
                boolean.class
            );
            setWindowCanFullScreen.invoke(util, this, true);
            System.out.println("macOS fullscreen capability enabled");
        } catch (Exception e) {
            System.err.println(
                "Could not enable macOS fullscreen: " + e.getMessage()
            );
        }
    }

    private void toggleFullScreen() {
        boolean isEnteringFullScreen = enterFullScreenItem.isVisible();
        enterFullScreenItem.setVisible(!isEnteringFullScreen);
        exitFullScreenItem.setVisible(isEnteringFullScreen);

        try {
            // Use macOS native fullscreen API via reflection
            Class<?> appClass = Class.forName("com.apple.eawt.Application");
            Method getApplicationMethod = appClass.getMethod("getApplication");
            Object appInstance = getApplicationMethod.invoke(null);

            Method requestToggleFullScreenMethod = appClass.getMethod(
                "requestToggleFullScreen",
                java.awt.Window.class
            );
            requestToggleFullScreenMethod.invoke(appInstance, this);
        } catch (Exception e) {
            System.err.println(
                "Failed to toggle native macOS fullscreen: " + e.getMessage()
            );
            e.printStackTrace();

            // Fallback to the old method (borderless fullscreen)
            GraphicsDevice device =
                GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
            if (device.isFullScreenSupported()) {
                if (device.getFullScreenWindow() == null) {
                    device.setFullScreenWindow(this);
                } else {
                    device.setFullScreenWindow(null);
                }
            }
        }
    }

    private void initUI() {
        add(new StarPanel());
        setTitle("Starfield Simulation");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JMenuBar menuBar = new JMenuBar();
        JMenu viewMenu = new JMenu("View");
        viewMenu.setMnemonic(KeyEvent.VK_V);

        KeyStroke fullScreenKeyStroke = KeyStroke.getKeyStroke(
            KeyEvent.VK_F,
            Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx() |
                InputEvent.CTRL_DOWN_MASK
        );

        enterFullScreenItem = new JMenuItem("Enter Fullscreen", KeyEvent.VK_F);
        enterFullScreenItem.setAccelerator(fullScreenKeyStroke);
        enterFullScreenItem.addActionListener(e -> toggleFullScreen());

        exitFullScreenItem = new JMenuItem("Exit Fullscreen", KeyEvent.VK_F);
        exitFullScreenItem.setAccelerator(fullScreenKeyStroke);
        exitFullScreenItem.addActionListener(e -> toggleFullScreen());
        exitFullScreenItem.setVisible(false);

        viewMenu.add(enterFullScreenItem);
        viewMenu.add(exitFullScreenItem);
        menuBar.add(viewMenu);
        setJMenuBar(menuBar);

        getRootPane()
            .getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(
                KeyStroke.getKeyStroke(
                    KeyEvent.VK_F,
                    Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx() |
                        InputEvent.CTRL_DOWN_MASK
                ),
                "toggleFullScreen"
            );
        getRootPane()
            .getActionMap()
            .put(
                "toggleFullScreen",
                new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        toggleFullScreen();
                    }
                }
            );
    }

    public static void main(String[] args) {
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        System.setProperty("apple.awt.application.appearance", "system");

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        EventQueue.invokeLater(() -> {
            Starfield ex = new Starfield();
            ex.setVisible(true);
            ex.setupMacOSFullscreenSupport();
        });
    }

    class StarPanel extends JPanel implements ActionListener {

        private final int STAR_COUNT = 2000;
        private final Star[] stars = new Star[STAR_COUNT];
        private final Random random = new Random();

        private final Timer timer;
        private boolean starsInitialized = false;

        public StarPanel() {
            setBackground(Color.BLACK);
            timer = new Timer(10, this);
        }

        private void initStars() {
            for (int i = 0; i < STAR_COUNT; i++) {
                stars[i] = new Star(
                    random.nextInt(getWidth() * 2) - getWidth(),
                    random.nextInt(getHeight() * 2) - getHeight(),
                    random.nextInt(getWidth())
                );
            }
        }

        private void updateStars() {
            for (int i = 0; i < STAR_COUNT; i++) {
                stars[i].z -= 1;
                if (stars[i].z <= 0) {
                    stars[i].x = random.nextInt(getWidth() * 2) - getWidth();
                    stars[i].y = random.nextInt(getHeight() * 2) - getHeight();
                    stars[i].z = getWidth();
                }
            }
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (!starsInitialized) {
                initStars();
                starsInitialized = true;
                timer.start();
            }

            drawStars(g);
        }

        private void drawStars(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            int centerX = getWidth() / 2;
            int centerY = getHeight() / 2;

            for (Star star : stars) {
                float k = (float) getWidth() / star.z;
                int x = (int) (star.x * k + centerX);
                int y = (int) (star.y * k + centerY);

                if (x < 0 || x >= getWidth() || y < 0 || y >= getHeight()) {
                    continue;
                }

                float size = (1 - star.z / getWidth()) * 4;
                if (size < 1) size = 1;

                int brightness = (int) ((1 - star.z / getWidth()) * 255);
                if (brightness > 255) brightness = 255;
                if (brightness < 0) brightness = 0;

                g2d.setColor(new Color(brightness, brightness, brightness));
                g2d.fillOval(x, y, (int) size, (int) size);
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            updateStars();
            repaint();
        }

        private class Star {

            float x, y, z;

            Star(float x, float y, float z) {
                this.x = x;
                this.y = y;
                this.z = z;
            }
        }
    }
}
