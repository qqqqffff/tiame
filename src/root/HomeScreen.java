package root;

import elements.NoteElement;
import elements.TimerElement;
import events.MoveSideMenu;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import menus.Settings;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HomeScreen {
    public static Group homeDisplay = new Group();
    public static Group sideMenu = new Group();
    public static int currentElements;
    protected static boolean showSideMenu;
    private static Delta boundaries;
    //TODO: implement icon view versus text view for showing the side menu
    public static void display1(){
        Delta[] offsets = new Delta[10];
        offsets[0] = new Delta(Screen.windowWidth - 160,50); //side menu shown
        offsets[1] = new Delta(Screen.windowWidth - 80,50); //side menu hidden
        offsets[2] = new Delta(0,50); //header line start

        //TODO: set as default on init
        Screen.user.updateUserPreferences();
        if(showSideMenu) Init.formatObj(sideMenu, offsets[0].getX(), offsets[0].getY());
        else             Init.formatObj(sideMenu, offsets[1].getX(), offsets[1].getY());

        String userName = Screen.user.displayName;
        boundaries = new Delta(Screen.windowWidth - 160,50);
        int x, y;


        Line sideMenuLine =  new Line(0, 0, 0, Screen.windowHeight);
        sideMenuLine.setStroke(Color.BLACK);
        sideMenuLine.setStrokeWidth(3);
        sideMenu.getChildren().add(sideMenuLine);

        Line headerLine = new Line(offsets[2].getX(), offsets[2].getY(), Screen.windowWidth, offsets[2].getY());
        headerLine.setStroke(Color.BLACK);
        headerLine.setStrokeWidth(5);
        homeDisplay.getChildren().add(headerLine);

        Text welcomeText = new Text("Welcome Back, " + userName + "!");
        welcomeText.setId("TitleText");
        welcomeText.setFont(new Font(25));
        welcomeText.setFill(Color.BLACK);
        homeDisplay.getChildren().add(welcomeText);
        Init.formatObj(welcomeText,((Screen.windowWidth - welcomeText.getLayoutBounds().getWidth())/ 2),35);

        Button addElement = new Button();
        addElement.setId("add");
        addElement.setGraphic(Screen.resources.getImage("new"));
        addElement.setMaxSize(50,50);
        addElement.setMinSize(50,50);
        x = (int) ((Screen.windowWidth - sideMenu.getLayoutX() - 50) / 2);
        Init.formatObj(addElement,x,90);
        addElement.setTextFill(Color.GREY);
        addElement.setOnAction(event -> homeDisplay.getChildren().add(generateElement(null)));
        sideMenu.getChildren().add(addElement);

        Button sideMenuButton = new Button();
        sideMenuButton.setId("menu");
        sideMenuButton.setGraphic(Screen.resources.getImage("menu_open"));
        sideMenuButton.setTextFill(Color.GREY);
        sideMenuButton.setOnAction(event -> {
            sideMenuButton.setDisable(true);
            ExecutorService mvService = Executors.newFixedThreadPool(1);
            MoveSideMenu mv = new MoveSideMenu(sideMenu, showSideMenu);
            if(!showSideMenu) {
                showSideMenu = true;
                sideMenuButton.setGraphic(Screen.resources.getImage("menu"));
                boundaries.setX(Screen.windowWidth - 80);
                //TODO: phase in display
//                Archive.buildArchiveDisplay(sideMenu.getLayoutX());
            }else{
                showSideMenu = false;
                sideMenuButton.setGraphic(Screen.resources.getImage("menu_open"));
                boundaries.setX(Screen.windowWidth - 175);
            }
            Screen.user.updateUserPreferences();
            mvService.execute(mv);
            mvService.shutdown();
        });
        Init.formatObj(sideMenuButton,x,20);
        sideMenuButton.setMaxSize(50,50);
        sideMenuButton.setMinSize(50,50);
        sideMenu.getChildren().add(sideMenuButton);

        Button settingsMenu = new Button();
        settingsMenu.setGraphic(Screen.resources.getImage("settings"));
        x = Screen.windowWidth - 30;
        Init.formatObj(settingsMenu,x,5);
        settingsMenu.setTextFill(Color.GREY);
        settingsMenu.setOnAction(event -> {
            for (Node n : homeDisplay.getChildren()) {
                n.setEffect(new GaussianBlur());
                n.setDisable(true);
            }
            homeDisplay.getChildren().add(Settings.settingsDisplay());
        });
        homeDisplay.getChildren().add(settingsMenu);


        Screen.root.getChildren().add(homeDisplay);
        Screen.root.getChildren().add(sideMenu);
    }
    protected static Group generateElement(Map<String, String> metaData){
        double defX = 25, defY = 50;

        Group element = new Group();
        currentElements++;
        String id = String.valueOf(1000+currentElements);
        element.setId(id+"S");
        final String[] titleTextD = {""};
        String type = "";

        final Delta dragDelta = new Delta();
        dragDelta.setDraggable(true);

        final boolean[] titleEditable = {true};
        final boolean[] showing = {true};
        final boolean[] updated = {false};
        final boolean[] titleSet = {false};

        NoteElement ne = new NoteElement();
        TimerElement te = new TimerElement();

        if(metaData != null){
            defX = parseDataX(metaData);
            defY = parseDataY(metaData);
            type = parseDataEle(metaData);
            titleTextD[0] = parseDataTitle(metaData);
            updated[0] = true;
            dragDelta.setDraggable(!parseDataPin(metaData));
            showing[0] = !parseDataMinimize(metaData);
        }


        //TODO: cleanup offsets
        ArrayList<Delta> offsets = new ArrayList<>();
        offsets.add(new Delta(0,37)); //Line 0
        offsets.add(new Delta(217,4)); //Close 1
        offsets.add(new Delta(4,4)); //Title Field 2
        offsets.add(new Delta(-999,-999)); //DEPRECATED
        offsets.add(new Delta(140,4)); //Save Title 4
        offsets.add(new Delta(7,24)); //Title Text 5
        offsets.add(new Delta(190,4)); //Pin 6
        offsets.add(new Delta(165,4)); //Minimize 7
        offsets.add(new Delta(30,45)); //Type N 8
        offsets.add(new Delta(30,105)); //Type T 9

        Rectangle base = new Rectangle(250,350);
        base.setId("base");
        base.setLayoutX(defX);
        base.setLayoutY(defY);
        base.setStroke(Color.BLACK);
        base.setStrokeWidth(2.5);
        base.setFill(Color.WHITE);

        Button pin = new Button();
        Button saveTitle = new Button();
        Button minimize = new Button();

        Line l = new Line();
        l.setLayoutX(defX + offsets.get(0).getX());
        l.setLayoutY(defY + offsets.get(0).getY());
        l.setStartX(0);
        l.setStartY(0);
        l.setEndX(250);
        l.setEndY(0);
        l.setStroke(Color.BLUE);
        l.setStrokeWidth(2.5);

        Button close = new Button();
        close.setId("close");
        close.setGraphic(Screen.resources.getImage("close"));
        close.setLayoutX(defX + offsets.get(1).getX());
        close.setLayoutY(defY + offsets.get(1).getY());
        close.setOnAction(event -> {
            HomeScreen.homeDisplay.getChildren().remove(element);
            if(element.getId().contains("N")) {
                LoadCache.clearCache(ne.ID);
            }else if(element.getId().contains("T")){
                LoadCache.clearCache(te.ID);
            }
        });

        TextField title = new TextField();
        title.setId("saveTitle");
        title.setPromptText("Untitled");
        title.setMaxSize(125,25);
        title.setLayoutX(defX + offsets.get(2).getX());
        title.setLayoutY(defY + offsets.get(2).getY());
        title.setFont(new Font(13));
        title.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ENTER){
                if(!titleEditable[0]){
                    purgeElements(element,"title",true);
                    saveTitle.setGraphic(Screen.resources.getImage("edit"));
                }

                String t = title.getText();
                if(t.equals("")){
                    t = title.getPromptText();
                }
                double x = base.getLayoutX() + offsets.get(5).getX();
                double y = base.getLayoutY() + offsets.get(5).getY();

                Text titleText = new Text(t);
                titleText.setId("title");
                title.setOpacity(0);
                title.setDisable(true);
                titleText.setFont(new Font(20));
                titleText.setLayoutX(x);
                titleText.setLayoutY(y);
                titleText.setFill(Color.BLUE);
                titleText.setOnMousePressed(event1 -> {
                    element.getChildren().remove(titleText);

                    double xt = base.getLayoutX() + offsets.get(2).getX();
                    double yt = base.getLayoutY() + offsets.get(2).getY();

                    title.setLayoutX(xt);
                    title.setLayoutY(yt);
                    title.setOpacity(1);
                    title.setDisable(false);
                    title.setText(titleTextD[0]);

                    titleEditable[0] = true;
                    saveTitle.setGraphic(Screen.resources.getImage("edit"));

                    title.requestFocus();
                });
                element.getChildren().add(titleText);

                if(updated[0]){
                    if(element.getId().contains("N")){
                        LoadCache.updateCache(ne);
                    }else if(element.getId().contains("T")){
                        LoadCache.updateCache(te);
                    }
                    titleTextD[0] = title.getText();
                }
                while((titleText.getLayoutBounds().getWidth() > 140)){
                    t = titleText.getText().substring(0,titleText.getText().length() - 4) + "...";
                    titleText.setText(t);
                }
                titleEditable[0] = false;
                saveTitle.setGraphic(Screen.resources.getImage("save"));
            }
        });

        Button typeN = new Button();
        Button typeT = new Button();
        Button typeC = new Button("Clock");
        Button typeW = new Button("Weather");
        Button typeL = new Button("List");

        base.setOnMouseDragged(event -> {
            double x = base.getLayoutX();
            double y = base.getLayoutY();
            double xoffset = event.getX() - dragDelta.getX();
            double yoffset = event.getY() - dragDelta.getY();

            if(dragDelta.isDragging() && !boundaries.outOfBounds(x + xoffset,y + yoffset, base.getWidth())) {

                base.setLayoutX(x + xoffset);
                base.setLayoutY(y + yoffset);

                l.setLayoutX(x + xoffset + offsets.get(0).getX());
                l.setLayoutY(y + yoffset + offsets.get(0).getY());


                if(updated[0]) {
                    for(Node n : element.getChildren()){
                        if(n.getId() != null){
                            if(n.getId().equals(ne.getElementID())){
                                ne.updatePos(x + xoffset, y + yoffset);
                            }else if(n.getId().equals(te.getElementID())){
                                te.updatePos(x + xoffset, y + yoffset);
                            }
                        }
                    }
                }else{
                    typeN.setLayoutX(x + xoffset + offsets.get(8).getX());
                    typeN.setLayoutY(y + yoffset + offsets.get(8).getY());

                    typeT.setLayoutX(x + xoffset + offsets.get(9).getX());
                    typeT.setLayoutY(y + yoffset + offsets.get(9).getY());
                }

                pin.setLayoutX(x + xoffset + offsets.get(6).getX());
                pin.setLayoutY(y + yoffset + offsets.get(6).getY());

                close.setLayoutX(x + xoffset + offsets.get(1).getX());
                close.setLayoutY(y + yoffset + offsets.get(1).getY());

                title.setLayoutX(x + xoffset + offsets.get(2).getX());
                title.setLayoutY(y + yoffset + offsets.get(2).getY());

                minimize.setLayoutX(x + xoffset + offsets.get(7).getX());
                minimize.setLayoutY(y + yoffset + offsets.get(7).getY());

                if(updated[0] && !te.minimizable && element.getId().contains("T")) {
                    saveTitle.setLayoutX(x + xoffset + offsets.get(7).getX());
                    saveTitle.setLayoutY(y + yoffset + offsets.get(7).getY());
                }else{
                    saveTitle.setLayoutX(x + xoffset + offsets.get(4).getX());
                    saveTitle.setLayoutY(y + yoffset + offsets.get(4).getY());
                }

                for (Node n : element.getChildren()) {
                    if (n.getId() != null) {
                        if (n.getId().equals("title")) {
                            n.setLayoutX(x + xoffset + offsets.get(5).getX());
                            n.setLayoutY(y + yoffset + offsets.get(5).getY());
                        }
                    }
                }
                if(titleSet[0]){
                    if(element.getId().contains("N")) {
                        LoadCache.updateCache(ne);
                    }else if(element.getId().contains("T")){
                        LoadCache.updateCache(te);
                    }
                }
            }
        });
        base.setOnMousePressed(event -> {
            dragDelta.setX(event.getX());
            dragDelta.setY(event.getY());
            if(dragDelta.getY() < 35 && dragDelta.isDraggable()){
                dragDelta.setDragging(true);
            }
        });
        base.setOnMouseReleased(event -> dragDelta.setDragging(false));

        saveTitle.setId("save");
        saveTitle.setGraphic(Screen.resources.getImage("edit"));
        saveTitle.setLayoutX(defX + offsets.get(4).getX());
        saveTitle.setLayoutY(defY + offsets.get(4).getY());
        saveTitle.setOnAction(event -> {
            double x, y;
            titleSet[0] = true;
            if(titleEditable[0]) {
                x = base.getLayoutX() + offsets.get(5).getX();
                y = base.getLayoutY() + offsets.get(5).getY();

                String t = title.getText();
                if(t.equals("")){
                    t = title.getPromptText();
                }
                Text titleText = new Text(t);
                titleText.setId("title");
                Init.hideElement(title);
                titleText.setLayoutX(x);
                titleText.setLayoutY(y);
                titleText.setFont(new Font(20));
                titleText.setFill(Color.BLUE);
                titleText.setOnMousePressed(event1 -> {
                    element.getChildren().remove(titleText);

                    double xt = base.getLayoutX() + offsets.get(2).getX();
                    double yt = base.getLayoutY() + offsets.get(2).getY();

                    title.setLayoutX(xt);
                    title.setLayoutY(yt);
                    title.setOpacity(1);
                    title.setDisable(false);
                    title.setText(titleTextD[0]);

                    titleEditable[0] = true;
                    saveTitle.setGraphic(Screen.resources.getImage("edit"));

                    title.requestFocus();
                });
                if(updated[0]){
                    if(element.getId().contains("N")) {
                        LoadCache.updateCache(ne);
                    }else if(element.getId().contains("T")){
                        LoadCache.updateCache(te);
                    }
                    titleTextD[0] = t;
                }

                element.getChildren().add(titleText);
                while((titleText.getLayoutBounds().getWidth() > 140)){
                    t = titleText.getText().substring(0,titleText.getText().length() - 4) + "...";
                    titleText.setText(t);
                }

                titleEditable[0] = false;
                saveTitle.setGraphic(Screen.resources.getImage("save"));
            }else{
                x = base.getLayoutX() + offsets.get(2).getX();
                y = base.getLayoutY() + offsets.get(2).getY();

                purgeElements(element,"title",true);

                title.setLayoutX(x);
                title.setLayoutY(y);
                Init.showElement(title);
                title.setText(titleTextD[0]);

                titleEditable[0] = true;
                saveTitle.setGraphic(Screen.resources.getImage("edit"));
                title.requestFocus();
            }
        });

        if(dragDelta.isDraggable()){
            pin.setId("unpinned");
            pin.setGraphic(Screen.resources.getImage("pin"));
        }
        else{
            pin.setId("pinned");
            pin.setGraphic(Screen.resources.getImage("pinned"));
        }
        pin.setLayoutX(defX + offsets.get(6).getX());
        pin.setLayoutY(defY + offsets.get(6).getY());
        pin.setOnAction(event -> {
            dragDelta.setDraggable(!dragDelta.isDraggable());
            if(pin.getId().equals("unpinned")) {
                pin.setId("pinned");
                pin.setGraphic(Screen.resources.getImage("pinned"));
            }else{
                pin.setId("unpinned");
                pin.setGraphic(Screen.resources.getImage("pin"));
            }
            if(element.getId().contains("N")) {
                LoadCache.updateCache(ne);
            }else if(element.getId().contains("T")){
                LoadCache.updateCache(te);
            }
        });

        minimize.setId("showing");
        minimize.setGraphic(Screen.resources.getImage("minimize"));
        minimize.setLayoutX(defX + offsets.get(7).getX());
        minimize.setLayoutY(defY + offsets.get(7).getY());
        minimize.setOnAction(event -> {
            if(showing[0]){
                showing[0] = false;
                minimize.setId("hidden");
                minimize.setGraphic(Screen.resources.getImage("maximize"));

                if(element.getId().contains("N")){
                    l.setOpacity(0);
                    ne.hideElements();
                    base.setHeight(40);
                }
                else if(element.getId().contains("T") && te.minimizable){
                    te.hideElements();
                    base.setHeight(70);
                }
                else if(element.getId().contains("S")){
                    l.setOpacity(0);
                    for(Node n : element.getChildren()){
                        if(n.getId() != null){
                            if(n.getId().contains("selector")){
                                Init.hideElement(n);
                            }
                        }
                    }
                    base.setHeight(40);
                }
            }
            else{
                showing[0] = true;
                minimize.setId("showing");
                minimize.setGraphic(Screen.resources.getImage("minimize"));
                l.setOpacity(1);

                if(element.getId().contains("N")){
                    ne.showElements();
                    base.setHeight(350);
                }
                else if(element.getId().contains("T") && te.minimizable){
                    te.showElements();
                    base.setHeight(100);
                }
                else if(element.getId().contains("S")){
                    for(Node n : element.getChildren()){
                        if(n.getId() != null){
                            if(n.getId().contains("selector")){
                                Init.showElement(n);
                            }
                        }
                    }
                    base.setHeight(350);
                }
            }
            System.out.println(showing[0] + ", " + minimize.getId());
            if(element.getId().contains("N")) {
                LoadCache.updateCache(ne);
            }else if(element.getId().contains("T")){
                LoadCache.updateCache(te);
            }
        });

        typeN.setGraphic(Screen.resources.getImage("note"));
        typeN.setLayoutX(defX + offsets.get(8).getX());
        typeN.setLayoutY(defY + offsets.get(8).getY());
        typeN.setId("selectorN");
        typeN.setOnAction(event -> {
            purgeElements(element,"selector",false);
            element.getChildren().add(ne.create(base.getLayoutX(), base.getLayoutY(), metaData));
            element.setId(id+"N");

            updated[0] = true;
        });

        typeT.setGraphic(Screen.resources.getImage("timer"));
        typeT.setLayoutX(defX + offsets.get(9).getX());
        typeT.setLayoutY(defY + offsets.get(9).getY());
        typeT.setId("selectorT");
        typeT.setOnAction(event -> {
            element.setId(id+"T");
            base.setHeight(100);

            purgeElements(element,"selector",false);

            element.getChildren().add(te.create(base.getLayoutX(), base.getLayoutY(), metaData));
            updated[0] = true;
        });

        element.getChildren().add(base);
        element.getChildren().add(l);
        element.getChildren().add(pin);
        element.getChildren().add(close);
        element.getChildren().add(title);
        element.getChildren().add(minimize);
        element.getChildren().add(saveTitle);

        if(metaData == null) {
            element.getChildren().add(typeN);
            element.getChildren().add(typeT);
        }
        else{
            titleSet[0] = true;
            title.setText(titleTextD[0]);
            saveTitle.fire();
            assert type != null;
            if(type.equals("Note")){
                element.setId(id+"N");
                element.getChildren().add(ne.create(defX,defY,metaData));
            }else if(type.equals("Timer")){
                element.setId(id+"T");
                element.getChildren().add(te.create(defX,defY,metaData));
                base.setHeight(100);
            }
            if(showing[0]){
                System.out.println("minimizing");
                minimize.fire();
            }else{
                showing[0] = true;
            }
        }

        //TODO: /to implement/ - element type selector *In Progress*


        return element;
    }
    public static void purgeElements(Group element, String id, boolean breakable){
        for(int i = 0; i < element.getChildren().size(); i++) {
            if(element.getChildren().get(i).getId() != null){
                if(element.getChildren().get(i).getId().contains(id)){
                    element.getChildren().remove(i);
                    if(breakable){
                        break;
                    }
                    i--;
                }
            }
        }
    }
    protected static void loadFromCache(Map<String, Map<String, String>> data){
        for(Map.Entry<String, Map<String, String>> entry : data.entrySet()){
            System.out.println("Generating Cache ID: " + entry.getKey());
            homeDisplay.getChildren().add(generateElement(entry.getValue()));
        }
    }
    //TODO: implement error checking (broken cache file)
    private static double parseDataX(Map<String, String> data){
        for(Map.Entry<String, String> entry: data.entrySet()){
            if(entry.getKey().equals("LayoutX")){
                return Double.parseDouble(entry.getValue());
            }
        }
        return 0;
    }
    private static double parseDataY(Map<String, String> data){
        for(Map.Entry<String, String> entry: data.entrySet()){
            if(entry.getKey().equals("LayoutY")){
                return Double.parseDouble(entry.getValue());
            }
        }
        return 0;
    }
    private static String parseDataEle(Map<String, String> data){
        for(Map.Entry<String, String> entry: data.entrySet()){
            if(entry.getKey().equals("Element")){
                return entry.getValue();
            }
        }
        return null;
    }
    private static String parseDataTitle(Map<String, String> data){
        for(Map.Entry<String, String> entry: data.entrySet()){
            if(entry.getKey().equals("Title")){
                return entry.getValue();
            }
        }
        return null;
    }
    private static boolean parseDataMinimize(Map<String, String> data){
        for(Map.Entry<String, String> entry: data.entrySet()){
            if(entry.getKey().equals("Minimize")){
                return !Boolean.parseBoolean(entry.getValue());
            }
        }
        return false;
    }
    private static boolean parseDataPin(Map<String, String> data){
        for(Map.Entry<String, String> entry: data.entrySet()){
            if(entry.getKey().equals("Pin")){
                return Boolean.parseBoolean(entry.getValue());
            }
        }
        return false;
    }
}
