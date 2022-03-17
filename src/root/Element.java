package root;

import javafx.scene.Group;

import java.util.Map;

public abstract class Element {
    Group element = new Group();
    protected abstract Group create(double x, double y, Map<String, ?> metaData);
    protected abstract String getID();
    protected abstract String generateSuperID();
    protected abstract void updatePos(double offsetX, double offsetY);
    protected abstract void hideElements();
    protected abstract void showElements();
    protected abstract Map<String, ?> generateMetaData();
    protected abstract void parseMetaData();
}
