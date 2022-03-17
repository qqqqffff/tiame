package root;

import javafx.concurrent.Task;
import javafx.scene.Node;

public class EventHandling extends Task<Void> {
    private final int eventID;
    public EventHandling(int eventID){
        this.eventID = eventID;
    }
    @Override
    protected Void call() throws Exception {
        if(eventID == 0){
            int index = -1;
            for(int i = 0; i < LoginScreen.loginDisplay.getChildren().size(); i++){
                if(LoginScreen.loginDisplay.getChildren().get(i).getId() != null){
                    if(LoginScreen.loginDisplay.getChildren().get(i).getId().equals("successCreate")){
                        index = i;
                        break;
                    }
                }
            }
            if(index != -1){
                LoginScreen.loginDisplay.getChildren().get(index).setOpacity(1);
                Thread.sleep(20000);
                for(double i = 0.95 ; i >= 0 ; i -= 0.05) {
                    LoginScreen.loginDisplay.getChildren().get(index).setOpacity(i);
                    Thread.sleep(50);
                }
                LoginScreen.loginDisplay.getChildren().get(index).setOpacity(0);
            }else{
                System.out.println("Element not Found");
            }
        }
        else if(eventID == 1){
            if(HomeScreen.showSideMenu){
                for (int i = 0; i < 80; i++) {
                    for(Node n : HomeScreen.homeDisplay.getChildren()) {
                        if(n.getId() != null) {
                            if (n.getId().equals("hidden")) {
                                n.setLayoutX(i);
                            }else if(n.getId().equals("hiddenB") && n.getLayoutX() < 1140){
                                n.setLayoutX(i+Screen.windowWidth-100);
                                n.setDisable(true);
                            }
                        }
                    }
                    Thread.sleep(5);
                }
                for(Node n : HomeScreen.homeDisplay.getChildren()){
                    n.setDisable(false);
                }
            }else {
                for (int i = 80; i > -10; i--) {
                    for(Node n : HomeScreen.homeDisplay.getChildren()) {
                        if(n.getId() != null) {
                            if (n.getId().equals("shown")) {
                                n.setLayoutX(i);
                            }else if(n.getId().equals("shownB") && n.getLayoutX() > 1105){
                                n.setLayoutX(i+Screen.windowWidth-145);
                                n.setDisable(true);
                            }
                        }
                    }
                    Thread.sleep(5);
                }
                for(Node n : HomeScreen.homeDisplay.getChildren()){
                    n.setDisable(false);
                }
            }
        }
        return null;
    }
}
