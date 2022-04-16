package root;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.Map;

public class Screen extends Application {
    public static Pane root;
    private final String title;
    public static UserData user;
    public static boolean saveLogin;
    public static int windowWidth;
    protected static int windowHeight;
    public static ResourceLoader resources;
    public static String pastUsername;
    //TODO: create logo for window icon
    public Screen(){
        resources = new ResourceLoader();
        Screen.windowWidth = 1200;
        Screen.windowHeight = 800;

        root = new Pane();
        String pastUsername = "";
        saveLogin = false;

        Init.initialize();

        for(Map.Entry<String, String> entry : Init.parseInit().entrySet()){
            if(entry.getKey().equals("save-login")){
                saveLogin = Boolean.parseBoolean(entry.getValue());
            }else if(entry.getKey().equals("past-user")){
                pastUsername = entry.getValue();
            }
        }

        title = Init.setTitle(saveLogin);

        user = new UserData();
        if(saveLogin) {
            user = new UserData(pastUsername);
        }
    }
    @Override
    public void start(Stage stage){
        if(!saveLogin) {
            LoginScreen.display0();
        }
        else{
            HomeScreen.display1();
            Archive.loadArchive();
            HomeScreen.loadFromCache(LoadCache.loadCache());
            Init.updateInit(true, user.userName);
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
