package events;

import javafx.concurrent.Task;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import root.Init;
import root.LoginScreen;
import root.Screen;

import java.util.ArrayList;

public class LoginEvents extends Task<Void> {
    public enum Event {blankPass, blankUser, incorrectInfo}
    private static ArrayList<Event> eventID;
    public static Text blankUser = blankUser();
    public static Text blankPass = blankPass();
    public static Text incorrectInfo = incorrectInfo();
    public LoginEvents(){
        eventID = new ArrayList<>();
    }
    public void addEvent(Event e){
        eventID.add(e);
    }    @Override
    public Void call() throws Exception {
        autoFormat();
        if(eventID.contains(Event.blankPass)){
            for(Node n : LoginScreen.loginDisplay.getChildren()){
                if(n.getId() != null){
                    if(n.getId().equals("blankP")){
                        n.setOpacity(1);
                        break;
                    }
                }
            }
        }
        if(eventID.contains(Event.blankUser)){
            for(Node n : LoginScreen.loginDisplay.getChildren()){
                if(n.getId() != null){
                    if(n.getId().equals("blankU")){
                        n.setOpacity(1);
                        break;
                    }
                }
            }
        }
        if(eventID.contains(Event.incorrectInfo)){
            for(Node n : LoginScreen.loginDisplay.getChildren()){
                if(n.getId() != null){
                    if(n.getId().equals("wrong")){
                        n.setOpacity(1);
                        break;
                    }
                }
            }
        }
        Thread.sleep(1000);
        for(int i = 0; i < 100; i++){
            if(eventID.contains(Event.blankPass)){
                for(Node n : LoginScreen.loginDisplay.getChildren()){
                    if(n.getId() != null){
                        if(n.getId().equals("blankP")){
                            n.setOpacity(1 - (i / 100.0));
                            break;
                        }
                    }
                }
            }
            if(eventID.contains(Event.blankUser)){
                for(Node n : LoginScreen.loginDisplay.getChildren()){
                    if(n.getId() != null){
                        if(n.getId().equals("blankU")){
                            n.setOpacity(1 - (i / 100.0));
                            break;
                        }
                    }
                }
            }
            if(eventID.contains(Event.incorrectInfo)){
                for(Node n : LoginScreen.loginDisplay.getChildren()){
                    if(n.getId() != null){
                        if(n.getId().equals("wrong")){
                            n.setOpacity(1 - (i / 100.0));
                            break;
                        }
                    }
                }
            }
            Thread.sleep(4);
        }
        System.out.println("completed");
        return null;
    }
    public static Text blankUser(){
        Text failedUser = new Text("Username field is blank");
        failedUser.setId("blankU");
        failedUser.setFont(new Font(15));
        failedUser.setFill(Color.RED);
        failedUser.setOpacity(0);
        return failedUser;
    }
    public static Text blankPass(){
        Text failedPass = new Text("Password field is blank");
        failedPass.setId("blankP");
        failedPass.setFont(new Font(15));
        failedPass.setFill(Color.RED);
        failedPass.setOpacity(0);
        return failedPass;
    }
    public static Text incorrectInfo(){
        Text wrongPass = new Text("Incorrect Username or Password");
        wrongPass.setId("wrong");
        wrongPass.setFont(new Font(15));
        wrongPass.setFill(Color.RED);
        wrongPass.setOpacity(0);
        return wrongPass;
    }
    private static void autoFormat(){
        int initialY = 350;
        for(Event e : eventID){
            if(e.equals(Event.blankPass)){
                Init.formatObj(blankPass,325, initialY);
            }
            if(e.equals(Event.blankUser)){
                Init.formatObj(blankUser,325, initialY);
            }
            if(e.equals(Event.incorrectInfo)){
                Init.formatObj(incorrectInfo,325, initialY);
            }
            initialY += 25;
        }
    }
}
