package root;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.FileInputStream;

public class ResourceLoader {
    //TODO: optimize so that all files are preloaded
    protected static ImageView getResource(String fileName, double width, double height){
        try {
            Image i = new Image(new FileInputStream("resources/" + fileName + ".png"));
            ImageView iv = new ImageView(i);
            iv.setFitHeight(height);
            iv.setFitWidth(width);
            iv.setPreserveRatio(true);
//            HomeScreen.homeDisplay.getChildren().add(iv);
            return iv;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
