package root;

import com.google.gson.Gson;
import com.sun.istack.internal.NotNull;
import javafx.scene.Node;
import javafx.scene.shape.Shape;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

public class Init {
    private final int status;
    private final boolean saveLogin;
    private final String user;
    public Init(boolean saveLogin, String user, int status){
        this.status = status;
        this.saveLogin = saveLogin;
        this.user = user;
    }
//    public Init(){ }
    public int getStatus(){ return this.status; }
    public static void hideElement(Node n){
        n.setOpacity(0);
        n.setDisable(true);
        n.setFocusTraversable(false);
        n.setMouseTransparent(true);
    }
    public static void showElement(Node n){
        n.setOpacity(1);
        n.setDisable(false);
        n.setFocusTraversable(true);
        n.setMouseTransparent(false);
    }
    public boolean getSaveLogin(){ return this.saveLogin; }
    public String getUser(){ return this.user; }
    public static String setTitle(int windowState){
        if(windowState == 0)
            return "Manager - Login";
        return "Manager";
    }
    public static void formatObj(Node n, double x, double y){
        n.setLayoutX(x);
        n.setLayoutY(y);
    }
    public static void updateInit(int status, boolean saveLogin, String user){
        try {
//            System.out.println("Attempting to write to init");

            Map<String, String> map = new HashMap<>();
            map.put("status",String.valueOf(status));
            map.put("save-login",String.valueOf(saveLogin));
            map.put("past-user",user);

            BufferedWriter writer = new BufferedWriter(new FileWriter("src/init.json"));
            new Gson().toJson(map,writer);
            writer.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
