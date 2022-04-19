package menus;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import root.*;

public class ArchiveMenu {
    public static Group getArchiveDisplay(){
        return generateArchiveDisplay();
    }
    private static Group generateArchiveDisplay(){
        Delta[] offsets = new Delta[10];
        offsets[0] = new Delta(((Screen.windowWidth - 350) / 2.0),((Screen.windowHeight - 600) / 2.0));
        offsets[1] = new Delta(320,4); //Close Button
        offsets[2] = new Delta(((350 - 180) / 2.0),75); //pane start


        Group display = new Group();
        display.setId("ArchiveMenu");
        display.setOnKeyPressed(event -> {
            if(event.getCode().equals(KeyCode.ESCAPE)){
                HomeScreen.homeDisplay.getChildren().remove(display);
                Archive.insideArchiveMenu = false;
                for(Node n : HomeScreen.homeDisplay.getChildren()){
                    n.setEffect(null);
                    n.setDisable(false);
                }
                for(Node n : HomeScreen.sideMenu.getChildren()){
                    n.setEffect(null);
                    n.setDisable(false);
                }
            }
        });
        Init.formatObj(display, offsets[0].getX(), offsets[0].getY());

        Rectangle base = new Rectangle(350,600);
        base.setFill(Color.WHITE);
        base.setStrokeWidth(2.5);
        base.setStroke(Color.BLACK);
        display.getChildren().add(base);

        Button close = new Button();
        close.setId("close");
        close.setGraphic(Screen.resources.getImage("close"));
        Init.formatObj(close, offsets[1].getX(), offsets[1].getY());
        close.setOnAction(event -> {
            HomeScreen.homeDisplay.getChildren().remove(display);
            Archive.insideArchiveMenu = false;
            for(Node n : HomeScreen.homeDisplay.getChildren()){
                n.setEffect(null);
                n.setDisable(false);
            }
            for(Node n : HomeScreen.sideMenu.getChildren()){
                n.setEffect(null);
                n.setDisable(false);
            }
        });
        display.getChildren().add(close);

        Text header = new Text("Archives");
        header.setFont(new Font(25));
        Init.formatObj(header, ((350 - header.getLayoutBounds().getWidth()) / 2),25);
        display.getChildren().add(header);

        offsets[3] = new Delta(((350 - header.getLayoutBounds().getWidth()) / 2),29);
        Line l = new Line(offsets[3].getX(),offsets[3].getY(),offsets[3].getX() + header.getLayoutBounds().getWidth(), offsets[3].getY());
        l.setStroke(Color.BLACK);
        l.setStrokeWidth(3);
        display.getChildren().add(l);

        ScrollPane pane = new ScrollPane();
        pane.setId("contents");
        pane.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
        pane.setFitToWidth(true);
        pane.setMinSize(180,500);
        pane.setMaxHeight(500);
        Group content = Archive.buildArchiveMenuDisplay();
        pane.setContent(content);
        Init.formatObj(pane, offsets[2].getX(), offsets[2].getY());
        display.getChildren().add(pane);

        return display;
    }
}
