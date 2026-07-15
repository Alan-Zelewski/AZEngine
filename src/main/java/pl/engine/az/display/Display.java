package pl.engine.az.display;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;

public class Display extends Canvas {
    private BufferStrategy bufferStrategy;

    public Display(int width, int height, String title) {
        setPreferredSize(new Dimension(width, height));

        JFrame frame = new JFrame(title);
        frame.add(this);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setFocusable(true);
    }

    public void addInputListener(KeyListener listener) {
        addKeyListener(listener);
        requestFocus();
    }

    public Graphics beginFrame() {
        if (bufferStrategy == null) {
            createBufferStrategy(3);
            bufferStrategy = getBufferStrategy();
        }
        Graphics g = bufferStrategy.getDrawGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0,0,getWidth(),getHeight());
        return g;
    }

    public void endFrame() {
        bufferStrategy.show();
    }
}
