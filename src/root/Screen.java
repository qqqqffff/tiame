package root;

import com.google.gson.Gson;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Map;

public class Screen extends Application {
    protected static Pane root;
    private String title;
    protected static int windowState;
    private static UserData user;
    protected static boolean saveLogin;
    protected static int windowWidth;
    protected static int windowHeight;
    public Screen(){
        Screen.windowWidth = 1200;
        Screen.windowHeight = 800;

        LoadCache.loadCache();

        root = new Pane();
        String userName = "";
        try{
            BufferedReader reader = new BufferedReader(new FileReader("src/init.json"));
            Gson gson = new Gson();

            Map<?, ?> read = gson.fromJson(reader,Map.class);

            int counter = 0;
            windowState = 0;
            for(Map.Entry<?, ?> entry : read.entrySet()){
                if(counter == 0){
                    Screen.saveLogin = Boolean.parseBoolean(entry.getValue().toString());
                }else if(counter == 1){
                    userName = entry.getValue().toString();
                }else if(counter == 2){
                    windowState = Integer.parseInt(entry.getValue().toString());
                    System.out.println(windowState + " setWindowState");
                }
                counter++;
            }

            System.out.println(saveLogin);
            if(saveLogin) {
                if(userName.equals("NA")){
                    userName = "New User";
                }
            }
        }catch(Exception e){ e.printStackTrace(); }
        title = Init.setTitle(windowState);

        user = new UserData(userName, UserData.getDisplayName(userName));
    }
    @Override
    public void start(Stage stage) throws Exception {
        if(windowState == 0) {
            root.getChildren().add(LoginScreen.display0(user));
        }else if (windowState == 1){
            root.getChildren().add(HomeScreen.display1(user));
            Init.updateInit(1,true, user.userName);
        }
        stage.setScene(new Scene(root, Screen.windowWidth, Screen.windowHeight));
        stage.setTitle(this.title);
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }

}
