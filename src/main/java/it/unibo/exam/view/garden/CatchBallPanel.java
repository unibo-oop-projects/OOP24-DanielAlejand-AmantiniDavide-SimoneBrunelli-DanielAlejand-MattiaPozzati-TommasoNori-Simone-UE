package it.unibo.exam.view.garden;

import it.unibo.exam.model.entity.minigame.garden.BallEntity;
import it.unibo.exam.model.entity.minigame.garden.BottleEntity;
import it.unibo.exam.model.entity.minigame.garden.CatchBallModel;
import it.unibo.exam.utility.medialoader.AssetLoader;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;

/**
 * Panel for rendering the CatchBall minigame.
 */
public final class CatchBallPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private static final int SCORE_FONT_SIZE = 14;
    private static final int SCORE_PADDING = 10;
    private static final int SCORE_Y = 20;
    private static final int LIVES_Y = 20;

    private final transient CatchBallModel model;
    private final transient Image backgroundImage;

    /**
     * Constructs the CatchBallPanel using the given game model.
     *
     * @param model the CatchBallModel to render
     */
    public CatchBallPanel(final CatchBallModel model) {
        this.model = model;
        this.backgroundImage = AssetLoader.loadImage("Garden/fountain.png");
    }

    /**
     * Paints the minigame panel, including background, balls, bottle, score, and lives.
     *
     * @param g the Graphics context to draw on
     */
    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        final Graphics2D g2 = (Graphics2D) g;

        if (backgroundImage != null) {
            g2.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        } else {
            g2.setColor(Color.DARK_GRAY);
            g2.fillRect(0, 0, getWidth(), getHeight());
        }

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.PLAIN, SCORE_FONT_SIZE));
        final String scoreText = "Score: " + model.getScore();
        final FontMetrics fm = g2.getFontMetrics();
        final int textWidth = fm.stringWidth(scoreText);
        g2.drawString(scoreText, getWidth() - textWidth - SCORE_PADDING, SCORE_Y);
        g2.drawString("Lives: " + model.getLives(), SCORE_PADDING, LIVES_Y);

        for (final BallEntity ball : model.getBalls()) {
            ball.draw(g2);
        }

        final BottleEntity bottle = model.getBottle();
        if (bottle != null) {
            bottle.draw(g2);
        }
    }
}
