package root;

class Delta {
    private double x;
    private double y;
    private boolean dragging;
    private boolean draggable;

    Delta(double x, double y) {
        this.x = x;
        this.y = y;
    }

    Delta() {
    }

    protected void setX(double x) {
        this.x = x;
    }

    protected void setY(double y) {
        this.y = y;
    }

    protected void setDraggable(boolean draggable) {
        this.draggable = draggable;
    }

    protected boolean isDraggable() {
        return draggable;
    }

    protected void setDragging(boolean dragging) {
        this.dragging = dragging;
    }

    protected boolean isDragging() {
        return dragging;
    }

    protected double getX() {
        return x;
    }

    protected double getY() {
        return y;
    }

    protected boolean outOfBounds(double x, double y, double w){
        return ((Screen.windowHeight - 40) <= y) || (this.x <= (x + w)) || (this.y >= y) || (x <= 0);
    }
}
