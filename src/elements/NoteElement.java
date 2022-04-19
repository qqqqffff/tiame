package elements;

import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import root.*;

import java.util.HashMap;
import java.util.Map;

//TODO: Add Option to archive Note after Title is entered
public class NoteElement extends Element {
    private final TextArea ta;
    private final Button archive;
    private final Button dropDownButton;
    private final Button colorPicker;
    private final Button increaseFont;
    private final Button decreaseFont;
    private final Group dropDown;
    private final Delta taOffset;
    private final Delta dropDownButtonOffset;
    private final Delta dropDownMenuOffset;
    private int seed;
    private boolean showingMenu;
    private boolean hoveringMenu;
    private boolean tempShowMenu;
    private boolean insideMenu;
    public NoteElement(){
        this.type = "Note";
        taOffset = new Delta(4,39);
        dropDownButtonOffset = new Delta(221,39);
        dropDownMenuOffset = new Delta(149,66);
        ta = new TextArea();
        archive = new Button("A");
        dropDown = new Group();
        dropDownButton = new Button("<");
        colorPicker = new Button("C");
        increaseFont = new Button("Z");
        decreaseFont = new Button("z");
    }
    public Group create(double x, double y, Map<String, String> metaData) {
        if(metaData == null) {
            setID();
        }else{
            parseMetaData(metaData);
        }
        setSeed(HomeScreen.currentElements);
        element.setId(generateID());

        Init.formatObj(ta,x + taOffset.getX(),y + taOffset.getY());
        ta.setId("noteArea");
        ta.setMinHeight(297);
        ta.setMaxWidth(242);
        ta.setFont(new Font(15));
        ta.setWrapText(true);
        ta.setOnKeyPressed(event -> LoadCache.updateCache(this));
        element.getChildren().add(ta);

        Init.formatObj(dropDownButton,x + dropDownButtonOffset.getX(),y + dropDownButtonOffset.getY());
        dropDownButton.setOnAction(event -> {
            //Cases: click while showing, click while not showing
            if(!showingMenu && !tempShowMenu){
                element.getChildren().add(dropDown);
                showingMenu = true;
            }
            else{
                element.getChildren().remove(dropDown);
                showingMenu = false;
                tempShowMenu = false;
            }
        });
        dropDownButton.setOnMouseEntered(event -> {
            //Cases: hovering w/ click, hovering w/o click
            if(!hoveringMenu && !showingMenu){
                hoveringMenu = true;
                tempShowMenu = true;
                if(!element.getChildren().contains(dropDown)){
                    element.getChildren().add(dropDown);
                }
            }
            else if(!hoveringMenu){
                hoveringMenu = true;
            }
        });
        dropDownButton.setOnMouseExited(event -> {
            //Cases: hovering w/o click, hovering with click
            if(hoveringMenu && tempShowMenu && !showingMenu && !insideMenu){
                element.getChildren().remove(dropDown);
                tempShowMenu = false;
            }
            hoveringMenu = false;
        });
        element.getChildren().add(dropDownButton);

        Init.formatObj(dropDown,x + dropDownMenuOffset.getX(),y + dropDownMenuOffset.getY());
        dropDown.setOnMouseEntered(event -> {
            //Cases: inside without click (tempShow), inside with click (showingMenu)
            insideMenu = true;
        });
        dropDown.setOnMouseExited(event -> {
            //Cases: exited with click (showingMenu), exited without click (tempShow)
            if(tempShowMenu && !showingMenu){
                tempShowMenu = false;
                insideMenu = false;
                hoveringMenu = false;
            }else if(showingMenu && !tempShowMenu){
                showingMenu = false;
                insideMenu = false;
                hoveringMenu = false;
            }
            element.getChildren().remove(dropDown);
        });

        Rectangle base = new Rectangle(100,25);
        base.setStroke(Color.GREY);
        base.setFill(Color.LIGHTGREY); //TODO: FIX COLOR (too dark)
        base.setStrokeWidth(1.5);
        dropDown.getChildren().add(base);

        //TODO: create icon for archive button
//        Init.formatObj(archive,x + archiveOffset.getX(),y + archiveOffset.getY());
        archive.setId("archive");
        archive.setFont(new Font(12));
        archive.setOpacity(.75);
        archive.setOnAction(event -> {
            Map<String, String> data = new HashMap<>(getSuperElementData());
            data.put("ID", String.valueOf(this.ID));
            data.put("Type", "Note");
            Archive.updateArchive(this);
            HomeScreen.purgeElements(HomeScreen.homeDisplay, generateSuperID(),true);
            HomeScreen.sideMenu.getChildren().add(Archive.generateArchiveElement(data));
        });
        dropDown.getChildren().add(archive);


        return element;
    }
    private void setSeed(int seed){
        this.seed = 1000 + seed;
    }
    private String generateID() {
        return this.seed + "EN";
    }
    public String generateSuperID(){ return this.seed + "N"; }
    public String getElementID(){
        return element.getId();
    }
    public void updatePos(double offsetX, double offsetY) {
        Init.formatObj(ta,offsetX + taOffset.getX(),offsetY + taOffset.getY());
        Init.formatObj(dropDownButton,offsetX + dropDownButtonOffset.getX(),offsetY + dropDownButtonOffset.getY());
        Init.formatObj(dropDown,offsetX + dropDownMenuOffset.getX(),offsetY + dropDownMenuOffset.getY());
    }

    @Override
    public void hideElements() {
        Init.hideElement(ta);
        Init.hideElement(archive);
    }

    @Override
    public void showElements() {
        Init.showElement(ta);
        Init.showElement(archive);
    }

    @Override
    public Map<String, String> generateMetaData() {
        Map<String, String> metaData = new HashMap<>(getSuperElementData());

        metaData.put("TextArea",ta.getText());
        metaData.put("ID",String.valueOf(this.ID));

        return metaData;
    }

    @Override
    protected void parseMetaData(Map<String, String> metaData) {
        for(Map.Entry<String, String> entry : metaData.entrySet()){
            if(entry.getKey().equals("TextArea")){
                ta.setText(entry.getValue());
            }else if(entry.getKey().equals("ID")){
                this.ID = Integer.parseInt(entry.getValue());
            }
        }
    }
}
