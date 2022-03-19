package root;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HomeScreen {
    protected static Group homeDisplay;
    private static Group sideMenu;
    private static Group settingsDisplay;
    protected static int currentElements;
    protected static boolean showSideMenu;
    //TODO: implement icon view versus text view for showing the side menu
    public static Group display1(){
        ArrayList<Delta> offsets = new ArrayList<>();
        offsets.add(new Delta(Screen.windowWidth - 145,50)); //Side Menu Line Start 0
        offsets.add(new Delta(Screen.windowWidth - 145,Screen.windowHeight)); //Side Menu Line End 1
        offsets.add(new Delta(0,50)); //Header Line Start 2
        offsets.add(new Delta(Screen.windowWidth,50)); //Header Line End 3

        Timer.setTimerSelectors(1,5,30,1,5);
        homeDisplay = new Group();
        generateSettingsDisplay(Screen.user);

        String userName = Screen.user.displayName;
        int x, y;

        Line sideMenuLine = new Line(offsets.get(0).getX(), offsets.get(0).getY(), offsets.get(1).getX(), offsets.get(1).getY());
        sideMenuLine.setStroke(Color.BLACK);
        sideMenuLine.setStrokeWidth(3);
        homeDisplay.getChildren().add(sideMenuLine);

        Line headerLine = new Line(offsets.get(2).getX(), offsets.get(2).getY(), offsets.get(3).getX(), offsets.get(3).getY());
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
        addElement.setGraphic(ResourceLoader.getResource("new",35,30));
        x = Screen.windowWidth - ((145 + 30) / 2);
        Init.formatObj(addElement,x,125);
        addElement.setTextFill(Color.GREY);
        addElement.setOnAction(event -> homeDisplay.getChildren().add(generateElement(null)));
        homeDisplay.getChildren().add(addElement);
        System.out.println(addElement.getLayoutBounds().getWidth());

        Button sideMenu = new Button();
        sideMenu.setGraphic(ResourceLoader.getResource("menu",30,35));
        sideMenu.setTextFill(Color.GREY);
        sideMenu.setOnAction(event -> {
            EventHandling e = new EventHandling(1);
            ExecutorService service = Executors.newFixedThreadPool(1);
            if(!showSideMenu) {
                showSideMenu = true;
                sideMenuLine.setId("hidden");
                sideMenu.setId("hiddenB");
                addElement.setId("hiddenB");
                sideMenu.setGraphic(ResourceLoader.getResource("menu_open",30,35));
            }else{
                showSideMenu = false;
                sideMenuLine.setId("shown");
                sideMenu.setId("shownB");
                addElement.setId("shownB");
                sideMenu.setGraphic(ResourceLoader.getResource("menu",30,35));
            }
            service.execute(e);
            service.shutdown();
        });
        homeDisplay.getChildren().add(sideMenu);
        x = Screen.windowWidth - ((145 + 30) / 2);
        Init.formatObj(sideMenu,x,65);

        Button settingsMenu = new Button();
        settingsMenu.setGraphic(ResourceLoader.getResource("settings",35,30));
        x = Screen.windowWidth - 30;
        Init.formatObj(settingsMenu,x,5);
        settingsMenu.setTextFill(Color.GREY);
        settingsMenu.setOnAction(event -> {
            for (Node n : homeDisplay.getChildren()) {
                n.setEffect(new GaussianBlur());
                n.setDisable(true);
            }
            homeDisplay.getChildren().add(settingsDisplay);
        });
        homeDisplay.getChildren().add(settingsMenu);


        return homeDisplay;
    }
    private static Group generateElement(Map<String, String> metaData){
        double defX = 25, defY = 50;

        Group element = new Group();
        currentElements++;
        String id = String.valueOf(1000+currentElements);
        element.setId(id+"S");
        String titleTextD = "";
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
            titleTextD = parseDataTitle(metaData);
            updated[0] = true;
            dragDelta.setDraggable(!parseDataPin(metaData));
            showing[0] = !parseDataMinimize(metaData);
        }


        //TODO: cleanup offsets
        ArrayList<Delta> offsets = new ArrayList<>();
        offsets.add(new Delta(0,35)); //Line 0
        offsets.add(new Delta(225,4)); //Close 1
        offsets.add(new Delta(4,4)); //Title Field 2
        offsets.add(new Delta(-999,-999)); //DEPRECATED
        offsets.add(new Delta(150,4)); //Save Title 4
        offsets.add(new Delta(7,27)); //Title Text 5
        offsets.add(new Delta(200,4)); //Pin 6
        offsets.add(new Delta(175,4)); //Minimize 7
        offsets.add(new Delta(30,40)); //Type N 8
        offsets.add(new Delta(30,70)); //Type T 9


        Line l = new Line();
        l.setLayoutX(defX + offsets.get(0).getX());
        l.setLayoutY(defY + offsets.get(0).getY());
        l.setStartX(0);
        l.setStartY(0);
        l.setEndX(250);
        l.setEndY(0);
        l.setStroke(Color.BLUE);
        l.setStrokeWidth(2.5);

        //TODO: Stylize to remove Borders
        Button close = new Button();
        close.setGraphic(ResourceLoader.getResource("close",15,12));
        close.setLayoutX(defX + offsets.get(1).getX());
        close.setLayoutY(defY + offsets.get(1).getY());
        close.setFont(new Font(12));
        close.setOnAction(event -> {
            HomeScreen.homeDisplay.getChildren().remove(element);
            if(element.getId().contains("N")) {
                LoadCache.clearCache(ne.ID);
            }
        });

        //TODO: Stylize to remove borders (make big line at the bottom only thing visible)
        TextField title = new TextField();
        title.setId("saveTitle");
        title.setPromptText("Untitled");
        title.setMaxSize(125,25);
        title.setLayoutX(defX + offsets.get(2).getX());
        title.setLayoutY(defY + offsets.get(2).getY());
        title.setFont(new Font(13));

        Button pin = new Button();
        Button saveTitle = new Button();
        Button minimize = new Button();

        Rectangle base = new Rectangle(250,350);
        base.setId("base");
        base.setLayoutX(defX);
        base.setLayoutY(defY);
        base.setStroke(Color.BLACK);
        base.setStrokeWidth(2.5);
        base.setFill(Color.WHITE);

        Button typeN = new Button();
        Button typeT = new Button();
        Button typeC = new Button("Clock");
        Button typeW = new Button("Weather");
        Button typeL = new Button("List");

        base.setOnMouseDragged(event -> {
            if(dragDelta.isDragging()) {
                double x = base.getLayoutX();
                double y = base.getLayoutY();
                double xoffset = event.getX() - dragDelta.getX();
                double yoffset = event.getY() - dragDelta.getY();

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
                        LoadCache.updateCache(ne.ID+"N",ne.generateMetaData());
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
        saveTitle.setGraphic(ResourceLoader.getResource("edit",15,12));
        saveTitle.setLayoutX(defX + offsets.get(4).getX());
        saveTitle.setLayoutY(defY + offsets.get(4).getY());
        saveTitle.setMaxSize(25,12);
        saveTitle.setOnAction(event -> {
            double x, y;
            titleSet[0] = true;
            if(titleEditable[0]) {
                x = base.getLayoutX() + offsets.get(5).getX();
                y = base.getLayoutY() + offsets.get(5).getY();

                Text titleText = new Text(title.getText());
                titleText.setId("title");
                title.setOpacity(0);
                title.setDisable(true);
                titleText.setLayoutX(x);
                titleText.setLayoutY(y);
                titleText.setFont(new Font(23));
                titleText.setFill(Color.BLUE);
                //TODO: auto-format the editButton
//                System.out.println(titleText.getLayoutBounds().getWidth());
                element.getChildren().add(titleText);

                if(updated[0]){
                    if(element.getId().contains("N")) {
                        LoadCache.updateCache(ne.ID+"N",ne.generateMetaData());
                    }
                }
                titleEditable[0] = false;
                saveTitle.setGraphic(ResourceLoader.getResource("save",25,12));
            }else{
                x = base.getLayoutX() + offsets.get(2).getX();
                y = base.getLayoutY() + offsets.get(2).getY();

                String text = "";

                for(Node n : element.getChildren()){
                    if(n.getId() != null){
                        if(n.getId().equals("title")){
                            Text t = (Text) n;
                            text = t.getText();
                            element.getChildren().remove(n);
                            break;
                        }
                    }
                }

                title.setLayoutX(x);
                title.setLayoutY(y);
                title.setOpacity(1);
                title.setDisable(false);
                title.setText(text);

                titleEditable[0] = true;
                saveTitle.setGraphic(ResourceLoader.getResource("edit",25,12));
            }
        });

        //TODO: Stylize to remove borders
        if(dragDelta.isDraggable()){
            pin.setId("unpinned");
            pin.setGraphic(ResourceLoader.getResource("pin",15,12));
        }
        else{
            pin.setId("pinned");
            pin.setGraphic(ResourceLoader.getResource("pinned",15,12));
        }
        pin.setLayoutX(defX + offsets.get(6).getX());
        pin.setLayoutY(defY + offsets.get(6).getY());
        pin.setMaxSize(25,12);
        pin.setFont(new Font(12));
        pin.setOnAction(event -> {
            dragDelta.setDraggable(!dragDelta.isDraggable());
            if(pin.getId().equals("unpinned")) {
                pin.setId("pinned");
                pin.setGraphic(ResourceLoader.getResource("pinned",15,12));
            }else{
                pin.setId("unpinned");
                pin.setGraphic(ResourceLoader.getResource("pin",15,12));
            }
            if(element.getId().contains("N")) {
                LoadCache.updateCache(ne.ID + "N", ne.generateMetaData());
            }
        });

        //TODO: Stylize to remove borders
        minimize.setId("showing");
        minimize.setGraphic(ResourceLoader.getResource("minimize",15,12));
        minimize.setLayoutX(defX + offsets.get(7).getX());
        minimize.setLayoutY(defY + offsets.get(7).getY());
        minimize.setMaxSize(25,25);
        minimize.setOnAction(event -> {
            if(showing[0]){
                showing[0] = false;
                minimize.setId("hidden");
                minimize.setGraphic(ResourceLoader.getResource("maximize",15,12));

                if(element.getId().contains("N")){
//                    System.out.println("hiding type: Note");
                    l.setOpacity(0);
                    ne.hideElements();
                    base.setHeight(40);
                }
                else if(element.getId().contains("T") && te.minimizable){
//                    System.out.println("hiding type: Timer");
                    te.hideElements();
                    base.setHeight(70);
                }
                else if(element.getId().contains("S")){
//                    System.out.println("hiding selectors");
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
                minimize.setGraphic(ResourceLoader.getResource("minimize",15,12));
                l.setOpacity(1);

                if(element.getId().contains("N")){
//                    System.out.println("displaying type: Note");
                    ne.showElements();
                    base.setHeight(350);
                }
                else if(element.getId().contains("T") && te.minimizable){
//                    System.out.println("displaying type: Timer");
                    te.showElements();
                    base.setHeight(100);
                }
                else if(element.getId().contains("S")){
//                    System.out.println("displaying selectors");
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
                LoadCache.updateCache(ne.ID + "N", ne.generateMetaData());
            }
        });

        //TODO: Stylize to remove borders
        typeN.setGraphic(ResourceLoader.getResource("note",25,15));
        typeN.setLayoutX(defX + offsets.get(8).getX());
        typeN.setLayoutY(defY + offsets.get(8).getY());
        typeN.setMaxHeight(30);
        typeN.setId("selectorN");
        typeN.setOnAction(event -> {
            purgeElements(element);
            element.getChildren().add(ne.create(base.getLayoutX(), base.getLayoutY(), metaData));
            element.setId(id+"N");

            updated[0] = true;
        });

        //TODO: Stylize to remove borders
        typeT.setGraphic(ResourceLoader.getResource("timer",25,15));
        typeT.setLayoutX(defX + offsets.get(9).getX());
        typeT.setLayoutY(defY + offsets.get(9).getY());
        typeT.setMaxHeight(30);
        typeT.setId("selectorT");
        typeT.setOnAction(event -> {
            element.setId(id+"T");
            base.setHeight(100);

            //TODO: implement getting default timer selectors from cache

            purgeElements(element);

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
            title.setText(titleTextD);
            saveTitle.fire();
            assert type != null;
            if(type.equals("Note")){
                element.setId(id+"N");
                element.getChildren().add(ne.create(defX,defY,metaData));
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
    private static void generateSettingsDisplay(UserData user){
        settingsDisplay = new Group();

        boolean[] displayingFields = {false, false};

        ArrayList<Delta> offsets = new ArrayList<>();
        offsets.add(new Delta(4,45));
        offsets.add(new Delta(4,offsets.get(0).getY()+30));
        offsets.add(new Delta(4,offsets.get(1).getY()+30));


        Rectangle base = new Rectangle(300,500);
        Init.formatObj(base,((Screen.windowWidth - base.getWidth())/2),((Screen.windowHeight - base.getHeight())/ 2));
        base.setStrokeWidth(2.5);
        base.setStroke(Color.BLACK);
        base.setFill(Color.WHITE);
        settingsDisplay.getChildren().add(base);

        Text title = new Text("Settings");
        title.setUnderline(true);
        title.setFont(new Font(25));
        Init.formatObj(title,(base.getLayoutX()+((base.getWidth()-(title.getText().length() * 10))/2)),(base.getLayoutY()+title.getFont().getSize()+5));
        title.setFill(Color.BLACK);
        settingsDisplay.getChildren().add(title);



        Button exit = new Button("x");
        exit.setMaxSize(25,25);
        exit.setFont(new Font(12));
        Init.formatObj(exit,(base.getLayoutX()+base.getWidth()-exit.getMaxWidth()-4),(base.getLayoutY()+4));
        exit.setOnAction(event -> {
            homeDisplay.getChildren().remove(settingsDisplay);
            for(Node n : homeDisplay.getChildren()){
                n.setEffect(null);
                n.setDisable(false);
            }
        });
        settingsDisplay.getChildren().add(exit);


        TextField setDNField = new TextField();
        setDNField.setPromptText("Display Name");
        setDNField.setFont(new Font(12));
        setDNField.setMaxSize(150,25);
        setDNField.setMinWidth(150);
        Init.formatObj(setDNField,(base.getLayoutX()+offsets.get(1).getX()),(base.getLayoutY()+offsets.get(1).getY()));
        Init.hideElement(setDNField);
        settingsDisplay.getChildren().add(setDNField);

        //TODO: checkmark icon
        Button confirmDN = new Button("C");
        confirmDN.setFont(new Font(12));
        confirmDN.setMaxSize(25,25);
        Init.formatObj(confirmDN,(setDNField.getLayoutX()+setDNField.getMinWidth()+5),setDNField.getLayoutY());
        Init.hideElement(confirmDN);
        confirmDN.setOnAction(event -> {
            user.setDisplayName(setDNField.getText());
            System.out.println("Set Display Name to: "+setDNField.getText());
            setDNField.setText(null);
            setDNField.setPromptText("Success");
            for(Node n : homeDisplay.getChildren()){
                if(n.getId() != null){
                    if(n.getId().equals("TitleText")){
                        ((Text) n).setText("Welcome Back, " + user.displayName + "!");
                        break;
                    }
                }
            }
        });
        settingsDisplay.getChildren().add(confirmDN);



        Button changePW = new Button("Change Password");
        Button setDisplayName = new Button("Set Display Name");
        
        setDisplayName.setMaxSize(115,25);
        setDisplayName.setFont(new Font(12));
        Init.formatObj(setDisplayName,base.getLayoutX()+offsets.get(0).getX(),base.getLayoutY()+offsets.get(0).getY());
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
        settingsDisplay.getChildren().add(setDisplayName);
        
        changePW.setMaxSize(115,25);
        changePW.setFont(new Font(12));
        Init.formatObj(changePW,(base.getLayoutX()+offsets.get(2).getX()),(base.getLayoutY()+offsets.get(2).getY()));
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
        
    }
    private static void purgeElements(Group element){
        for(int i = 0; i < element.getChildren().size(); i++) {
            if(element.getChildren().get(i).getId() != null){
                if(element.getChildren().get(i).getId().contains("selector")){
                    element.getChildren().remove(i);
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
