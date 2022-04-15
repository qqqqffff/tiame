package root;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

public class ResourceLoader {
    private final Map<String, ImageView> resources = new HashMap<>();
    public ResourceLoader(){
        resources.put("clock",getResource("clock",50,50));


        resources.put("timer",getResource("timer",50,50));
        resources.put("note",getResource("note",50,50));


        resources.put("edit",getResource("edit",10,12));
        resources.put("save",getResource("save",10,12));
        resources.put("close",getResource("close",12,12));
        resources.put("maximize",getResource("maximize",10,12));
        resources.put("minimize",getResource("minimize",10,12));
        resources.put("pin",getResource("pin",10,12));
        resources.put("pinned",getResource("pinned",10,12));

        resources.put("settings",getResource("settings",10,30));
        resources.put("menu",getResource("menu",30,35));
        resources.put("menu_open",getResource("menu_open",30,35));
        resources.put("new",getResource("new",30,30));

        resources.put("start",getResource("start",20,12));
        resources.put("pause",getResource("pause",25,12));
    }
    private ImageView getResource(String fileName, double width, double height){
        try {
            Image i = new Image(new FileInputStream("resources/" + fileName + ".png"));
            ImageView iv = new ImageView(i);
            iv.setFitHeight(height);
            iv.setFitWidth(width);

            return iv;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public ImageView getImage(String search){
        for(Map.Entry<String, ImageView> entry : resources.entrySet()){
            if(entry.getKey().equals(search)){
                ImageView iv = new ImageView(entry.getValue().getImage());
                iv.setFitWidth(entry.getValue().getFitWidth());
                iv.setFitHeight(entry.getValue().getFitHeight());
                return iv;
            }
        }
        return null;
    }
}
