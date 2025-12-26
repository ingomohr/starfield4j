package com.example.starfield;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.*;

public class Starfield extends JFrame {

    public Starfield() {
        initUI();
    }

    private void initUI() {
        add(new StarPanel());
        setTitle("Starfield Simulation");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            Starfield ex = new Starfield();
            ex.setVisible(true);
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
