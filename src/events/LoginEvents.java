package events;

import javafx.concurrent.Task;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import root.Init;
import root.LoginScreen;
import root.Screen;

public class LoginEvents extends Task<Void> {
    public enum Event {blankPass, blankUser, incorrectInfo}
    private final Event eventID;
    public LoginEvents(Event ID){
        this.eventID = ID;
    }
    @Override
    public Void call() throws Exception {
        if(eventID == Event.blankPass){
            for(Node n : LoginScreen.loginDisplay.getChildren()){
                if(n.getId() != null){
                    if(n.getId().equals("blankP")){
                        n.setOpacity(1);
                        Thread.sleep(1000);
                        for(int i = 100; i > 0; i--){
                            n.setOpacity(i / 100.0);
                            Thread.sleep(5);
                        }
                        n.setOpacity(0);
                        break;
                    }
                }
            }
        }
        else if(eventID == Event.blankUser){
            for(Node n : LoginScreen.loginDisplay.getChildren()){
                if(n.getId() != null){
                    if(n.getId().equals("blankU")){
                        n.setOpacity(1);
                        Thread.sleep(1000);
                        for(int i = 100; i > 0; i--){
                            n.setOpacity(i / 100.0);
                            Thread.sleep(5);
                        }
                        n.setOpacity(0);
                        break;
                    }
                }
            }
        }
        else if(eventID == Event.incorrectInfo){
            System.out.println("eventc");
            for(Node n : LoginScreen.loginDisplay.getChildren()){
                if(n.getId() != null){
                    if(n.getId().equals("wrong")){
                        n.setOpacity(1);
                        Thread.sleep(1000);
                        for(int i = 100; i > 0; i--){
                            n.setOpacity(i / 100.0);
                            Thread.sleep(5);
                        }
                        n.setOpacity(0);
                        break;
                    }
                }
            }
        }
        return null;
    }
}
