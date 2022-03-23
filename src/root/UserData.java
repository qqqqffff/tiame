package root;

import com.google.gson.Gson;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserData {
    protected String userName;
    protected String displayName;
    protected boolean cacheInit;
    public UserData(String userName){
        this.userName = userName;
        initializeCache();
    }
    public UserData(){}
    protected static boolean checkDuplicate(String user){
        try{
            Gson gson = new Gson();
            BufferedReader reader = new BufferedReader(new FileReader("src/users.json"));

            Map<?, ?> map = gson.fromJson(reader, Map.class);
            reader.close();

            for(Map.Entry<?, ?> entry : map.entrySet()){
                if(entry.getKey().toString().equals(user)){
                    return false;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }
    protected static void setData(String userName, String unencryptedPass){
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(unencryptedPass.getBytes(StandardCharsets.UTF_8));

            Gson gson = new Gson();

            BufferedReader reader = new BufferedReader(new FileReader("src/users.json"));

            Map<?, ?> read = gson.fromJson(reader, Map.class);
            reader.close();

            Map<String, String> write = new HashMap<>();
            if(read != null) {
                for (Map.Entry<?, ?> entry : read.entrySet()) {
                    write.put(entry.getKey().toString(), entry.getValue().toString());
                }
            }
            write.put(userName,new String(hash, StandardCharsets.UTF_8));

            BufferedWriter writer = new BufferedWriter(new FileWriter("src/users.json"));
            new Gson().toJson(write, writer);
            writer.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void setData(String userName){
        this.userName = userName;
    }
    private boolean checkPass(String unencryptedPass){
        try {
            Gson gson = new Gson();

            BufferedReader reader = new BufferedReader(new FileReader("src/users.json"));

            Map<?,?> map = gson.fromJson(reader, Map.class);

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(unencryptedPass.getBytes(StandardCharsets.UTF_8));

            for(Map.Entry<?, ?> entry : map.entrySet()){
                if(entry.getKey().toString().equals(userName)) {
                    if (new String(hash, StandardCharsets.UTF_8).equals(entry.getValue().toString())) {
                        return true;
                    }
                }
            }
            reader.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }
    public boolean signInAttempt(String USER, String PASS){
        setData(USER);
        boolean success = checkPass(PASS);
        if(success){
//            System.out.println("Login Successful!");
//            System.out.println("Credentials: "+this.userName+", SessionID: "+sessionID);
            setData(USER,PASS);
            return true;
        }
        setData(null);
        System.out.println("Login Unsuccessful");
        ErrorHandling task = new ErrorHandling(2);
        ExecutorService service = Executors.newFixedThreadPool(1);
        service.execute(task);
        service.shutdown();
        return false;
    }
    protected void setDisplayName(String displayName){
        initializeCache();
        this.displayName = displayName;
        updateUserPreferences();
    }
    protected void initializeCache() {
        File cache = new File("src/cache");
        if (!cacheInit){
            if (!cache.exists()) {
                System.out.println("creating new cache");
                cache.mkdirs();
            }
            if (Objects.requireNonNull(cache.list()).length == 0) {
                File temp = new File("src/cache/.ignore");
                try {
                    System.out.println("creating temp file");
                    temp.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            cache = new File("src/cache/" + this.userName);
            if (!cache.exists()) {
                System.out.println("creating user cache directory");
                cache.mkdirs();
            }
            cacheInit = true;
        }
        loadUserPreferences();
    }
    private void loadUserPreferences(){
        File userData = new File("src/cache/"+ userName + "/" + userName + ".json");
        if(!userData.exists()){
            try {
                userData.createNewFile();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        parseUserData();
        updateUserPreferences();
    }
    protected void updateUserPreferences(){
        Map<String, String> data = new HashMap<>();
        data.put("DisplayName",displayName);
        data.put("MenuOpen", String.valueOf(HomeScreen.showSideMenu));
        data.put("TimeSelectorA", String.valueOf(Timer.timerSelectors.get(0)));
        data.put("TimeSelectorB", String.valueOf(Timer.timerSelectors.get(1)));
        data.put("TimeSelectorC", String.valueOf(Timer.timerSelectors.get(2)));
        data.put("TimeSelectorD", String.valueOf(Timer.timerSelectors.get(3)));
        data.put("TimeSelectorE", String.valueOf(Timer.timerSelectors.get(4)));
        File userDataFile = new File("src/cache/"+ userName + "/" + userName + ".json");
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(userDataFile));

            new Gson().toJson(data, writer);
            writer.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    protected void parseUserData(){
        File userDataFile = new File("src/cache/"+ userName + "/" + userName + ".json");
        try{
            BufferedReader reader = new BufferedReader(new FileReader(userDataFile));
            Gson gson = new Gson();

            Map<?, ?> data = gson.fromJson(reader, Map.class);
            if(data != null) {
                for (Map.Entry<?, ?> entry : data.entrySet()) {
                    if (entry.getKey().toString().equals("DisplayName")) {
                        this.displayName = entry.getValue().toString();
                    }else if(entry.getKey().toString().equals("TimeSelectorA")){
                        Timer.setTimerSelectors(0, Integer.parseInt(entry.getValue().toString()));
                    }else if(entry.getKey().toString().equals("TimeSelectorB")){
                        Timer.setTimerSelectors(1, Integer.parseInt(entry.getValue().toString()));
                    }else if(entry.getKey().toString().equals("TimeSelectorC")){
                        Timer.setTimerSelectors(2, Integer.parseInt(entry.getValue().toString()));
                    }else if(entry.getKey().toString().equals("TimeSelectorD")){
                        Timer.setTimerSelectors(3, Integer.parseInt(entry.getValue().toString()));
                    }else if(entry.getKey().toString().equals("TimeSelectorE")){
                        Timer.setTimerSelectors(4, Integer.parseInt(entry.getValue().toString()));
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        if(this.displayName == null){
            this.displayName = userName;
            updateUserPreferences();
        }
    }
}
