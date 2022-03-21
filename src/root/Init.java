package root;

import com.google.gson.Gson;
import javafx.scene.Node;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
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
    protected int getStatus(){ return this.status; }
    protected static void hideElement(Node n){
        n.setOpacity(0);
        n.setDisable(true);
        n.setFocusTraversable(false);
        n.setMouseTransparent(true);
    }
    protected static void showElement(Node n){
        n.setOpacity(1);
        n.setDisable(false);
        n.setFocusTraversable(true);
        n.setMouseTransparent(false);
    }
    protected boolean getSaveLogin(){ return this.saveLogin; }
    protected String getUser(){ return this.user; }
    protected static String setTitle(boolean saveLogin){
        if(!saveLogin)
            return "Manager - Login";
        return "Manager";
    }
    protected static void formatObj(Node n, double x, double y){
        n.setLayoutX(x);
        n.setLayoutY(y);
    }
    protected static void updateInit(boolean saveLogin, String user){
        try {
            Map<String, String> map = new HashMap<>();
            map.put("save-login",String.valueOf(saveLogin));
            map.put("past-user",user);

            BufferedWriter writer = new BufferedWriter(new FileWriter("src/init.json"));
            new Gson().toJson(map,writer);
            writer.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    protected static Map<String, String> parseInit(){
        Map<String, String> parsedInit = new HashMap<>();
        try{
            BufferedReader reader = new BufferedReader(new FileReader("src/init.json"));
            Gson gson = new Gson();

            Map<?, ?> read = gson.fromJson(reader,Map.class);

            for(Map.Entry<?, ?> entry : read.entrySet()){
                parsedInit.put(entry.getKey().toString(), entry.getValue().toString());
            }
            return parsedInit;
        }
        catch(Exception e){ e.printStackTrace(); }
        return parsedInit;
    }
}
