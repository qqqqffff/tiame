package menus;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import root.*;

import java.util.ArrayList;

public class Settings {
    private static Group display;
    public static Node settingsDisplay(){
        if(display == null){
            generateSettingsDisplay();
        }
        return display;
    }
    private static Node generateSettingsDisplay(){
        display = new Group();

        boolean[] displayingFields = {false, false};

        ArrayList<Delta> offsets = new ArrayList<>();
        offsets.add(new Delta(4,45)); //
        offsets.add(new Delta(4,offsets.get(0).getY() + 30));
        offsets.add(new Delta(4,offsets.get(1).getY() + 30));

        Rectangle base = new Rectangle(300,500);
        double defx = ((Screen.windowWidth - base.getWidth())/2), defy = 150;
        Init.formatObj(base,defx,defy);
        base.setStrokeWidth(2.5);
        base.setStroke(Color.BLACK);
        base.setFill(Color.WHITE);
        display.getChildren().add(base);

        Text title = new Text("Settings");
        title.setUnderline(true);
        title.setFont(new Font(25));
        title.setFill(Color.BLACK);
        display.getChildren().add(title);
        Init.formatObj(title,(base.getLayoutX() + ((base.getWidth() - title.getLayoutBounds().getWidth()) / 2)),(base.getLayoutY() + title.getFont().getSize() + 5));



        Button exit = new Button();
        exit.setId("close");
        exit.setGraphic(Screen.resources.getImage("close"));
        Init.formatObj(exit,defx + base.getWidth() - 42,defy + 4);
        exit.setOnAction(event -> {
            HomeScreen.homeDisplay.getChildren().remove(display);
            for(Node n : HomeScreen.homeDisplay.getChildren()){
                n.setEffect(null);
                n.setDisable(false);
            }
        });
        display.getChildren().add(exit);


        TextField setDNField = new TextField();
        setDNField.setPromptText("Display Name");
        setDNField.setFont(new Font(12));
        setDNField.setMaxSize(150,25);
        setDNField.setMinWidth(150);
        Init.formatObj(setDNField,defx+offsets.get(1).getX(),base.getLayoutY() + offsets.get(1).getY());
        Init.hideElement(setDNField);
        display.getChildren().add(setDNField);

        //TODO: checkmark icon
        Button confirmDN = new Button("C");
        confirmDN.setFont(new Font(12));
        Init.formatObj(confirmDN,(setDNField.getLayoutX() + setDNField.getMinWidth() + 5), setDNField.getLayoutY());
        Init.hideElement(confirmDN);
        confirmDN.setOnAction(event -> {
            Screen.user.setDisplayName(setDNField.getText());
            System.out.println("Set Display Name to: "+setDNField.getText());
            setDNField.setText(null);
            setDNField.setPromptText("Success");
            for(Node n : HomeScreen.homeDisplay.getChildren()){
                if(n.getId() != null){
                    if(n.getId().equals("TitleText")){
                        ((Text) n).setText("Welcome Back, " + Screen.user.displayName + "!");
                        break;
                    }
                }
            }
        });
        display.getChildren().add(confirmDN);

        Button setDisplayName = new Button("Set Display Name");
        setDisplayName.setFont(new Font(12));
        Init.formatObj(setDisplayName,defx + offsets.get(0).getX(),base.getLayoutY()+offsets.get(0).getY());
        setDisplayName.setOnAction(event -> {
            //TODO: animate buttons going down;
            if(!displayingFields[0]){
                displayingFields[0] = true;

                //

                Init.showElement(setDNField);
                Init.showElement(confirmDN);
            }else{
                displayingFields[0] = false;
                displayingFields[1] = false;

                Init.hideElement(setDNField);
                Init.hideElement(confirmDN);

                //
            }
        });
        display.getChildren().add(setDisplayName);

        //TODO: implement
        Button changePW = new Button("Change Password");
        changePW.setFont(new Font(12));
        Init.formatObj(changePW,defx + offsets.get(2).getX(),defy + offsets.get(2).getY());
        changePW.setOnAction(event -> {
            if(displayingFields[0]){
                displayingFields[0] = false;
                displayingFields[1] = true;

                Init.hideElement(setDNField);
                Init.hideElement(confirmDN);

                //
                Init.formatObj(changePW,changePW.getLayoutX(),changePW.getLayoutY()-40);
            }else if(!displayingFields[1]){
                //to be implemented
                System.out.println("to be implemented");
            }else{
                //to be implemented
                System.out.println("to be implemented");
            }
        });

        //TODO: stylize
        Button logout = new Button("Logout");
        Init.formatObj(logout,defx + 5,defy + 450);
        logout.setFont(new Font(12));
        logout.setOnAction(event -> {
            Init.updateInit(Screen.saveLogin, Screen.user.userName);
            Screen.pastUsername = Screen.user.userName;
            Screen.user = new UserData();
            Screen.root.getChildren().remove(HomeScreen.homeDisplay);
            LoginScreen.display0();
        });
        display.getChildren().add(logout);


        //TODO: implement changing default timer increments
        return display;
    }
}
