package it.unibo.exam.utility;

public record Rectangle(Point2D p, Point2D d) {
    /**
     * @return True if the rectangle intersects with another rectangle
     *        False otherwise
     */
    public boolean intersects(final Rectangle other) {
        int x = this.p.getX();
        int y = this.p.getY();
        int width = this.d.getX();
        int height = this.d.getY();

        int otherX = other.p.getX();
        int otherY = other.p.getY();
        int otherWidth = other.d.getX();
        int otherHeight = other.d.getY();


        return x < otherX + otherWidth &&
               x + width > otherX &&
               y < otherY + otherHeight &&
               y + height > otherY;
    }

}
