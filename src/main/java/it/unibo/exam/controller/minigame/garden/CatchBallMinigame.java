package it.unibo.exam.controller.minigame.garden;

import it.unibo.exam.model.entity.minigame.Minigame;
import it.unibo.exam.model.entity.minigame.MinigameCallback;
import it.unibo.exam.view.garden.CatchBallPanel;
import it.unibo.exam.model.entity.minigame.garden.CatchBallModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class CatchBallMinigame implements Minigame {

    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;
    private static final int TIMER_DELAY = 16;

    private JFrame frame;
    private CatchBallModel model;
    private CatchBallPanel panel;
    private Timer gameTimer;
    private MinigameCallback callback;

    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private long startTime;

    @Override
    public void start(JFrame parentFrame, MinigameCallback onComplete) {
        this.callback = onComplete;
        this.model = new CatchBallModel();
        this.panel = new CatchBallPanel(model);

        frame = new JFrame("Catch the Balls");
        frame.setSize(WIDTH, HEIGHT);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(parentFrame);
        frame.setResizable(false);
        frame.add(panel);
        frame.setVisible(true);

        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_A) {
                    leftPressed = true;
                } else if (e.getKeyCode() == KeyEvent.VK_D) {
                    rightPressed = true;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_A) {
                    leftPressed = false;
                } else if (e.getKeyCode() == KeyEvent.VK_D) {
                    rightPressed = false;
                }
            }
        });

        startTime = System.currentTimeMillis();
        gameTimer = new Timer(TIMER_DELAY, this::gameLoop);
        gameTimer.start();
    }

    private void gameLoop(ActionEvent e) {
        model.update(leftPressed, rightPressed);
        panel.repaint();

        if (model.hasWon()) {
            endGame(true);
        }
    }

    private void endGame(boolean success) {
        gameTimer.stop();
        frame.dispose();
        int time = (int) ((System.currentTimeMillis() - startTime) / 1000);
        callback.onComplete(success, time);
    }

    @Override
    public void stop() {
        if (frame != null) {
            frame.dispose();
        }
        if (gameTimer != null) {
            gameTimer.stop();
        }
    }

    @Override
    public String getName() {
        return "Garden Minigame";
    }

    @Override
    public String getDescription() {
        return "Move the bottle to catch falling balls. Reach 5 to win!";
    }
}
