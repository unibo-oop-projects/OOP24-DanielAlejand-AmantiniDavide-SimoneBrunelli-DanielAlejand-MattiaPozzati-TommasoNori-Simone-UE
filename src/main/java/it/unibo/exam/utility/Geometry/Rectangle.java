package it.unibo.exam.utility.Geometry;

/**
 * A simple record class that rappresent a rectangle.
 * @param p position of high-left corner
 * @param d dimension of the rectangle
 */
public record Rectangle(Point2D p, Point2D d) {
    /**
     * @return True if the rectangle intersects with another rectangle
     *        False otherwise
     */
    public boolean intersects(final Rectangle other)  {

        final int x = this.p.getX();
        final int y = this.p.getY();
        final int width = this.d.getX();
        final int height = this.d.getY();

        final int otherX = other.p.getX();
        final int otherY = other.p.getY();
        final int otherWidth = other.d.getX();
        final int otherHeight = other.d.getY();


        return x < otherX + otherWidth 
            && x + width > otherX 
            && y < otherY + otherHeight 
            && y + height > otherY;
    }

    /**
     * @return True if the rectangle contain point
     *         False otherwise
     */
    public boolean contains(final Point2D point) {
        return point.getX() >= p.getX() && point.getX() <= p.getX() + d.getX() 
            && point.getY() >= p.getY() && point.getY() <= p.getY() + d.getY();
    }

}
