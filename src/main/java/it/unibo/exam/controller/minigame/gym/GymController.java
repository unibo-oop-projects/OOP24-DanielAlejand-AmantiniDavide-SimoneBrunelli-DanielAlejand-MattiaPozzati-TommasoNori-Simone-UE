package it.unibo.exam.controller.minigame.gym;

import it.unibo.exam.utility.geometry.Point2D;
import it.unibo.exam.model.entity.minigame.gym.Cannon;
import it.unibo.exam.model.entity.minigame.gym.GymModel;
import it.unibo.exam.view.gym.GymPanel;

/**
 * Controller class for the Gym minigame. Manages the interaction between the model (GymModel),
 * the view (GymPanel), and user input through mouse events.
 */
public class GymController {
    private static final double MIN_ANGLE = 0;
    private static final double MAX_ANGLE = Math.PI;
    private final GymModel model;
    private final GymPanel view;
    private final MouseHandler mhandler; 

    /**
     * Constructs a GymController with the specified model and view.
     *
     * @param model the GymModel representing the game logic
     * @param view the GymPanel representing the graphical interface
     */
    public GymController(final GymModel model, final GymPanel view) {
        this.model = model;
        this.view = view;
        this.mhandler = new MouseHandler(this);
    }

    /**
     * Updates the state of the model.
     */
    public void update() {
        model.update();
    }

    /**
     * Moves the cannon horizontally by the specified amount, ensuring it stays within the board boundaries.
     *
     * @param dx the amount to move the cannon along the x-axis
     */
    public void moveCannon(final int dx) {
        final Cannon cannon = model.getCannon();
        Point2D newPos = new Point2D(cannon.getPosition().getX() + dx, cannon.getPosition().getY());
        // Controllo dei bordi
        if (newPos.getX() < 0) { 
            newPos = new Point2D(0, newPos.getY()); 
        }
        if (newPos.getX() + cannon.getWidth() > model.getBoardWidth()) {
            newPos = new Point2D(model.getBoardWidth() - cannon.getWidth(), newPos.getY());
        }

        cannon.setPosition(new Point2D(newPos.getX(), newPos.getY()));
    }

    /**
     * Updates the angle of the cannon based on the mouse position.
     *
     * @param mouseX the x-coordinate of the mouse
     * @param mouseY the y-coordinate of the mouse
     */
    public void updateAngle(final int mouseX, final int mouseY) {
        final Cannon cannon = model.getCannon();
        final Point2D cannonCenter = new Point2D(
            cannon.getPosition().getX() + cannon.getWidth() / 2,
            cannon.getPosition().getY()
        );
        final double dx = mouseX - cannonCenter.getX();
        final double dy = cannonCenter.getY() - mouseY;
        double angle = Math.atan2(dy, dx);
        // Limit the angle from 0 to PI
        if (angle < MIN_ANGLE) { 
            angle = MIN_ANGLE; 
        }
        if (angle > MAX_ANGLE) { 
            angle = MAX_ANGLE; 
        }
        cannon.setAngle(angle);
    }

    /**
     * Fires a projectile from the cannon.
     */
    public void fireProjectile() {
        model.fireProjectile();
    }

    /**
     * Attaches mouse listeners to the view for handling user input.
     */
    public void attachListeners() {
        view.addMouseListener(mhandler.getMouseAdapter());
        view.addMouseMotionListener(mhandler.getMouseMotionAdapter());
    }

    /**
     * Returns the MouseHandler associated with this controller.
     *
     * @return the MouseHandler instance
     */
    public MouseHandler getMouseHandler() { 
        return mhandler; 
    }
}
