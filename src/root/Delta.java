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

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setDraggable(boolean draggable) {
        this.draggable = draggable;
    }

    public boolean isDraggable() {
        return draggable;
    }

    public void setDragging(boolean dragging) {
        this.dragging = dragging;
    }

    public boolean isDragging() {
        return dragging;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
