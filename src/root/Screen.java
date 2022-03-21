package root;

import com.google.gson.Gson;
import com.sun.javafx.css.Stylesheet;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Map;

public class Screen extends Application {
    protected static Pane root;
    private final String title;
    protected static int windowState;
    protected static UserData user;
    protected static boolean saveLogin;
    protected static int windowWidth;
    protected static int windowHeight;
    protected static ResourceLoader resources;
    public Screen(){
        resources = new ResourceLoader();
        Screen.windowWidth = 1200;
        Screen.windowHeight = 800;

        root = new Pane();
        String userName = "";
        try{
            //TODO: move to init class
            BufferedReader reader = new BufferedReader(new FileReader("src/init.json"));
            Gson gson = new Gson();

            Map<?, ?> read = gson.fromJson(reader,Map.class);

            int counter = 0;
            windowState = 0;
            for(Map.Entry<?, ?> entry : read.entrySet()){
                if(counter == 0){
                    saveLogin = Boolean.parseBoolean(entry.getValue().toString());
                }else if(counter == 1){
                    userName = entry.getValue().toString();
                }else if(counter == 2){
                    windowState = Integer.parseInt(entry.getValue().toString());
                }
                counter++;
            }

            if(saveLogin) {
                if(userName.equals("NA")){
                    userName = "New User";
                }
            }
        }
        catch(Exception e){ e.printStackTrace(); }
        title = Init.setTitle(windowState);

        user = new UserData();
        if(windowState == 1) {
            user = new UserData(userName, UserData.getDisplayName(userName));
            user.initializeCache();
        }
    }
    @Override
    public void start(Stage stage){
        if(windowState == 0) {
            root.getChildren().add(LoginScreen.display0());
        }else if (windowState == 1){
            root.getChildren().add(HomeScreen.display1());
            HomeScreen.loadFromCache(LoadCache.loadCache());
            Init.updateInit(1,true, user.userName);
        }
        Scene scene = new Scene(root, Screen.windowWidth, Screen.windowHeight);
        scene.getStylesheets().add(Screen.class.getResource("stylesheet.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle(this.title);
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }

}
