package it.unibo.exam.controller.minigame.gym;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

/**
 * A simple mouse input handler.
 */
public class MouseHandler {
    private final GymController controller;

    /**
     * Creates a new MouseHandler associated with the given GymController.
     *
     * @param controller the GymController to be associated with this MouseHandler
     */
    public MouseHandler(final GymController controller) {
        this.controller = controller;
    }

    /**
     * @return a MouseAdapter that handles the mouse press event to fire a projectile
     */
    public MouseAdapter getMouseAdapter() {
        return new MouseAdapter() {
            @Override
            public void mousePressed(final MouseEvent e) {
                controller.fireProjectile();
            }
        };
    }

    /**
     * 
     * @return a MouseMotionAdapter that handles the mouse move event to update the firing angle
     */
    public MouseMotionAdapter getMouseMotionAdapter() {
        return new MouseMotionAdapter() {
            @Override
            public void mouseMoved(final MouseEvent e) {
                controller.updateAngle(e.getX(), e.getY());
            }
        };
    }
}
