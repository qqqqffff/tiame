package root;

import com.google.gson.Gson;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Archive {
    private static int currentElements;
    private static boolean builtArchiveHeader;
    private final static ArrayList<Group> currentArchives = new ArrayList<>();
    private final static String archivePath = "src/archive/" + Screen.user.userName + ".json";
    public final static Group archiveHeader = archiveHeader();
    private static void initArchive(){
        File archive = new File("src/archive");
        if(!archive.exists()) {
            archive.mkdirs();
            File ignore = new File("src/archive/.ignore");
            try{
                ignore.createNewFile();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        File userArchive = new File(archivePath);
        try {
            userArchive.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //TODO: figure out how to implement an order
    protected static void loadArchive(){
        if(!(new File(archivePath).exists())) {
            initArchive();
            return;
        }
        Gson gson = new Gson();
        try{
            BufferedReader reader = new BufferedReader(new FileReader(archivePath));
            Map<?, ?> read = gson.fromJson(reader, Map.class);
            if(read != null) {
                Map<String, String>[] dataTo = new Map[read.entrySet().size()];
                for (Map.Entry<?, ?> entry : read.entrySet()) {
                    String type = entry.getKey().toString().chars()
                            .filter(ch -> !Character.isDigit(ch))
                            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                            .toString();
                    int order = Integer.parseInt(entry.getValue().toString().substring(0,4));
                    String id = entry.getKey().toString().chars()
                            .filter(Character::isDigit)
                            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                            .toString();
                    Map<String, String> data = new HashMap<>();
                    data.put("Title", entry.getValue().toString().substring(4));
                    data.put("ID", id);
                    data.put("Type", type);
                    data.put("Order", String.valueOf(order));
                    dataTo[order - 1000] = data;
                }
                if(HomeScreen.showSideMenu) {
                    if (!builtArchiveHeader) {
                        HomeScreen.sideMenu.getChildren().add(archiveHeader);
                        builtArchiveHeader = true;
                    }
                    for (Map<String, String> data : dataTo) {
                        HomeScreen.sideMenu.getChildren().add(groupGenerateArchiveElement(data));
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    protected static void updateArchive(Element e){
        String rawID = e.ID + e.type;
        String title = "";
        Map<String, String> write = new HashMap<>();
        for(Map.Entry<String, String> entry : e.getSuperElementData().entrySet()){
            if(entry.getKey().equals("Title")){
                title = entry.getValue();
                break;
            }
        }
        write.put(rawID, 1000 + currentArchives.size() + title);
        Gson gson = new Gson();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(archivePath));
            Map<?, ?> data = gson.fromJson(reader, Map.class);
            reader.close();
            if(data != null){
                for(Map.Entry<?, ?> entry : data.entrySet()){
                    write.put(entry.getKey().toString(), entry.getValue().toString());
                }
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(archivePath));
            new Gson().toJson(write, writer);
            writer.close();
        }catch(Exception e1){
            e1.printStackTrace();
        }
    }
    protected static void clearArchive(String id){
        Gson gson = new Gson();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(archivePath));
            Map<?, ?> read = gson.fromJson(reader, Map.class);
            reader.close();
            Map<String, String> write = new HashMap<>();
            for(Map.Entry<?, ?> entry : read.entrySet()){
                if(!entry.getKey().toString().equals(id)){
                    write.put(entry.getKey().toString(), entry.getValue().toString());
                }
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(archivePath));
            new Gson().toJson(write, writer);
            writer.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    private static ArrayList<Integer> getArchiveIDs(){
        ArrayList<Integer> ids = new ArrayList<>();
        Gson gson = new Gson();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(archivePath));
            Map<?, ?> read = gson.fromJson(reader, Map.class);
            if(read != null) {
                for (Map.Entry<?, ?> entry : read.entrySet()) {
                    String id = entry.getKey().toString().chars()
                            .filter(Character::isDigit)
                            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                            .toString();
                    ids.add(Integer.parseInt(id));
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return ids;
    }
    protected static Group groupGenerateArchiveElement(Map<String, String> data){
        Group archive = new Group();
        if(!builtArchiveHeader){
            HomeScreen.sideMenu.getChildren().add(archiveHeader);
            builtArchiveHeader = true;
        }

        final String[] id = {""};
        final String[] title = {""};
        final String[] type = {""};
        final String[] order = {""};
        boolean orderSet = false;
        for(Map.Entry<String, String> entry: data.entrySet()){
            if(entry.getKey().equals("Title")){
                title[0] = entry.getValue();
            }else if(entry.getKey().equals("ID")){
                id[0] = entry.getValue();
            }else if(entry.getKey().equals("Type")){
                type[0] = entry.getValue();
            }else if(entry.getKey().equals("Order")){
                order[0] = entry.getValue();
                orderSet = true;
            }
        }


        if(type[0].equals("Note")) {
            if(orderSet){
                archive.setId(id[0] + "N" + order[0]);
            }else {
                archive.setId(id[0] + "N" + (1000 + currentElements));
            }
        }
        else if(type[0].equals("List")){
            if(orderSet){
                archive.setId(id[0] + "N" + order[0]);
            }else {
                archive.setId(id[0] + "L" + (1000 + currentElements));
            }
        }
        else{
            System.out.println("Type not found");
            return null;
        }

        double x = 10;
        double y = 720 - 35 * (10 - currentElements);
        Init.formatObj(archive, x, y);

        Delta[] offsets = new Delta[3];
        offsets[0] = new Delta(120,2);
        offsets[1] = new Delta(100,2);
        offsets[2] = new Delta(4,21);


        Button close = new Button();
        close.setGraphic(Screen.resources.getImage("close"));
        Init.formatObj(close, offsets[0].getX(), offsets[0].getY());
        close.setOnAction(event -> {
            HomeScreen.sideMenu.getChildren().remove(archive);
            currentElements--;
            currentArchives.remove(archive);
            clearArchive(id[0] + type[0]);
            LoadCache.clearCache(Integer.parseInt(id[0]));
            reformatArchives(deriveOrder(archive));
        });

        //TODO: create icon for open
        Button open = new Button("o");
        open.setFont(new Font(10));
        open.setMaxSize(25,25);
        Init.formatObj(open, offsets[1].getX(), offsets[1].getY());
        open.setOnAction(event -> {
            HomeScreen.sideMenu.getChildren().remove(archive);
            currentElements--;
            currentArchives.remove(archive);
            clearArchive(id[0] + type[0]);
            LoadCache.buildFromCache(Integer.parseInt(id[0]));
            reformatArchives(deriveOrder(archive));
        });


        Text titleText = new Text(title[0]);
        titleText.setFont(new Font(16));
        titleText.setFill(Color.BLACK);
        Init.formatObj(titleText, offsets[2].getX(), offsets[2].getY());
        while((titleText.getLayoutBounds().getWidth() > 140)){
            String t = titleText.getText().substring(0,titleText.getText().length() - 4) + "...";
            titleText.setText(t);
        }

        Rectangle base = new Rectangle(145,30);
        base.setFill(Color.WHITE);
        base.setStroke(Color.BLACK);
        base.setStrokeWidth(2.5);



        archive.getChildren().add(base);
        archive.getChildren().add(titleText);
        archive.getChildren().add(open);
        archive.getChildren().add(close);

        currentElements++;
        currentArchives.add(archive);
        return archive;
    }
    private static Group displayMore(){
        //TODO: implement this
        return null;
    }
    private static Group archiveHeader(){
        Group header = new Group();
        header.setId("header");
        Init.formatObj(header,0,350);

        Line l = new Line(0,5,160,5);
        l.setStroke(Color.BLACK);
        l.setStrokeWidth(2.5);
        header.getChildren().add(l);

        Text title = new Text("Archives");
        title.setId("title");
        title.setFont(new Font(25));
        double x = (title.getLayoutBounds().getWidth()) / 2;
        Init.formatObj(title,x - 10,0);
        header.getChildren().add(title);

        return header;
    }
    private static void reformatArchives(int index){
        //TODO: figure out how to animate them going up
        Gson gson = new Gson();
        try{
            BufferedReader reader = new BufferedReader(new FileReader(archivePath));
            Map<?, ?> read = gson.fromJson(reader, Map.class);
            reader.close();

            Map<String, String> write = new HashMap<>();

            if(currentElements == 1){
                for(Map.Entry<?, ?> entry : read.entrySet()){
                    write.put(entry.getKey().toString(), 1000 + entry.getValue().toString().substring(4));
                }
            }
            if(currentElements > 1) {
                int[] order = new int[currentElements];
                for (int i = 0; i < currentArchives.size(); i++) {
                    order[i] = deriveOrder(currentArchives.get(i));
                }
                for (int i = index; i < currentArchives.size(); i++) {
                    order[i] = order[i] - 1;
                    String idPrefix = "";
                    for (int j = 0; j < currentArchives.get(i).getId().length(); j++) {
                        if (!Character.isDigit(currentArchives.get(i).getId().charAt(j))) {
                            idPrefix = currentArchives.get(i).getId().substring(0, j);
                            break;
                        }
                    }
                    currentArchives.get(i).setId(idPrefix + 1000 + order[i]);
                }

                for (Map.Entry<?, ?> entry : read.entrySet()) {
                    if (deriveOrder(entry.getValue().toString()) > index) {
                        int newIndex = Integer.parseInt(entry.getValue().toString().substring(0, 4)) - 1;
                        write.put(entry.getKey().toString(), newIndex + entry.getValue().toString().substring(4));
                        continue;
                    }
                    write.put(entry.getKey().toString(), entry.getValue().toString());
                }
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(archivePath));
            new Gson().toJson(write, writer);
            writer.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        currentElements = 0;
        for(Group g : currentArchives){
            double x = Screen.windowWidth - 160;
            double y = Screen.windowHeight - 35 * (10 - currentElements);
            Init.formatObj(g,x,y);
            currentElements++;
        }
    }
    public static void buildArchiveDisplay(double initialX){
        currentElements = 0;
        if(!builtArchiveHeader){
            HomeScreen.sideMenu.getChildren().add(archiveHeader);
            builtArchiveHeader = true;
        }
        for(Group g : currentArchives){
            double y = Screen.windowHeight - 35 * (10 - currentElements);
            Init.formatObj(g, initialX, y);
            currentElements++;
            HomeScreen.sideMenu.getChildren().add(g);
            System.out.println(g.getId());
        }
    }
    public static void removeArchiveDisplay(){
        for(Group g : currentArchives){
            HomeScreen.homeDisplay.getChildren().remove(g);
        }
        HomeScreen.homeDisplay.getChildren().remove(archiveHeader);
        builtArchiveHeader = false;
    }
    protected static boolean insideArchive(int id){
        for(int i : getArchiveIDs()){
            if(i == id){
                return true;
            }
        }
        return false;
    }
    private static int deriveOrder(Group g){
        int order = -1;
        if(g.getId() != null){
            for(int i = 0; i < g.getId().length(); i++){
                if(!Character.isDigit(g.getId().charAt(i))){
                    order = Integer.parseInt(g.getId().substring(i+1)) - 1000;
                    break;
                }
            }
        }
        return order;
    }
    private static int deriveOrder(String s){
        int order = -1;
        if(s != null){
            for(int i = 0; i < s.length(); i++){
                if(!Character.isDigit(s.charAt(i))){
                    order = Integer.parseInt(s.substring(0, i)) - 1000;
                    break;
                }
            }
        }
        return order;
    }
}
