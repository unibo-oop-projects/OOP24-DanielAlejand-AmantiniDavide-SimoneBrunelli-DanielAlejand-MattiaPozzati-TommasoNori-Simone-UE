package it.unibo.exam.view.garden;

import it.unibo.exam.model.entity.minigame.garden.BallEntity;
import it.unibo.exam.model.entity.minigame.garden.BottleEntity;
import it.unibo.exam.model.entity.minigame.garden.CatchBallModel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class CatchBallPanel extends JPanel {

    private final CatchBallModel model;
    private Image backgroundImage;

    public CatchBallPanel(CatchBallModel model) {
        this.model = model;

        try {
            var resource = getClass().getClassLoader().getResource("Garden/fountain.png");
            if (resource != null) {
                backgroundImage = ImageIO.read(resource);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        if (backgroundImage != null) {
            g2.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        } else {
            g2.setColor(Color.DARK_GRAY);
            g2.fillRect(0, 0, getWidth(), getHeight());
        }

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.PLAIN, 14));
        String scoreText = "Score: " + model.getScore();
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(scoreText);
        g2.drawString(scoreText, getWidth() - textWidth - 10, 20);
        g2.drawString("Lives: " + model.getLives(), 10, 20);

        for (BallEntity ball : model.getBalls()) {
            ball.draw(g2);
        }

        BottleEntity bottle = model.getBottle();
        if (bottle != null) {
            bottle.draw(g2);
        }
    }
}
