package events;

import javafx.concurrent.Task;
import javafx.scene.Group;
import javafx.scene.Node;
import root.Screen;

public class MoveSideMenu extends Task<Void> {
    //TODO: figure out how to interrupt and switch directions
    private final boolean pos; //true means shown, false means hidden
    private final Group root;
    public MoveSideMenu(Group root, boolean pos){
        this.pos = pos;
        this.root = root;
    }
    @Override
    protected Void call() throws Exception {
        int initialPos = (int) root.getLayoutX();
        int endStopA = (((Screen.windowWidth - (Screen.windowWidth - 80 )) / 2));
        int endStopB = (((Screen.windowWidth - (Screen.windowWidth - 160)) / 2));
        if(pos){
            for(int i = initialPos; i < initialPos + 80; i++){
                root.setLayoutX(i);
                for(Node n : root.getChildren()){
                    if(n.getId() != null){
                        if (n.getId().equals("menu") || n.getId().equals("add")) {
                            if(i > (initialPos + 80 - endStopA)) {
                                n.setLayoutX(n.getLayoutX() - 1);
                            }
                        }
                        else{
                            double op = 1 - ((i + 1 - initialPos) / (80.0));
                            if(n.getId().equals("header")){
                                for(Node m : ((Group) n).getChildren()){
                                    if(m.getId() != null){
                                        if(m.getId().equals("title")){
                                            m.setOpacity(op);
                                        }
                                    }
                                }
                            }else{
                                n.setOpacity(op);
                            }
                        }
                    }
                }
                Thread.sleep(4);
            }
        }
        else{
            for(int i = initialPos; i > initialPos - 80; i--){
                root.setLayoutX(i);
                for(Node n : root.getChildren()){
                    if(n.getId() != null){
                        if(n.getId().equals("menu") || n.getId().equals("add")) {
                            if(i < (initialPos - 120 + endStopB)) {
                                n.setLayoutX(n.getLayoutX() + 1);
                            }
                        }
                        else{
                            double op = -1 * ((i + 1 - initialPos) / (80.0));
                            if(n.getId().equals("header")){
                                for(Node m : ((Group) n).getChildren()){
                                    if(m.getId() != null){
                                        if(m.getId().equals("title")){
                                            m.setOpacity(op);
                                        }
                                    }
                                }
                            }else{
                                n.setOpacity(op);
                            }
                        }
                    }
                }
                Thread.sleep(4);
            }
        }
        for(Node n : root.getChildren()){
            if(n.getId() != null){
                if(n.getId().equals("menu")){
                    n.setDisable(false);
                }
            }
        }
        return null;
    }
}
