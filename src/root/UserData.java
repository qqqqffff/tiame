package root;

import com.google.gson.Gson;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

public class UserData {
    protected String userName;
    protected String displayName;
    private int sessionID;
    protected boolean cacheInit;
    private Map<String, String> userPreferences;
    public UserData(String userName, String displayName){
        this.userName = userName;
        this.displayName = displayName;
        this.userPreferences = new HashMap<>();
        generateSessionID();
    }
    public UserData(){}
    public static boolean checkDuplicate(String user){
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
    public static void setData(String userName, String unencryptedPass){
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(unencryptedPass.getBytes(StandardCharsets.UTF_8));

            Gson gson = new Gson();

            BufferedReader reader = new BufferedReader(new FileReader("src/users.json"));

            Map<?, ?> read = gson.fromJson(reader, Map.class);
            reader.close();

            Map<String, String> write = new HashMap<>();
            for(Map.Entry<?, ?> entry : read.entrySet()){
                write.put(entry.getKey().toString(),entry.getValue().toString());
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
    public void signInAttempt(String USER, String PASS){
        setData(USER);
        boolean success = checkPass(PASS);
        if(success){
            System.out.println("Login Successful!");
            System.out.println("Credentials: "+this.userName+", SessionID: "+sessionID);
        }else{
            setData(null);
            System.out.println("Login Unsuccessful");
            ErrorHandling task = new ErrorHandling(2);
            ExecutorService service = Executors.newFixedThreadPool(1);
            service.execute(task);
            service.shutdown();
        }

    }
    private void generateSessionID(){
        sessionID = ThreadLocalRandom.current().nextInt(0,65536);
    }
    protected static String getDisplayName(String userName){
        try{
            Gson gson = new Gson();
            BufferedReader reader = new BufferedReader(new FileReader("src/displayNames.json"));

            Map<?, ?> map = gson.fromJson(reader, Map.class);
            reader.close();

            for(Map.Entry<?, ?> entry: map.entrySet()){
                if(entry.getKey().toString().equals(userName)){
                    return entry.getValue().toString();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return userName;
    }
    protected void setDisplayName(String displayName){
        initializeCache();

        this.displayName = displayName;
        try{
            Gson gson = new Gson();
            BufferedReader reader =  new BufferedReader(new FileReader("src/displayNames.json"));

            Map<?, ?> read = gson.fromJson(reader, Map.class);
            reader.close();

            Map<String, String> write = new HashMap<>();
            for(Map.Entry<?, ?> entry : read.entrySet()){
                if(entry.getKey().equals(this.userName)){
                    write.put(entry.getKey().toString(),displayName);
                }else{
                    write.put(entry.getKey().toString(),entry.getValue().toString());
                }
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter("src/displayNames.json"));
            new Gson().toJson(write, writer);
            writer.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    protected void initializeCache(){
        File cache = new File("src/cache/");
        if(!cache.exists()){
            cache.mkdirs();
        }
        if(Objects.requireNonNull(cache.list()).length == 0){
            File temp = new File("src/cache/.ignore");
            try {
                temp.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        cache = new File("src/cache/" + Screen.user.userName);
        if(!cache.exists()){
            cache.mkdirs();
        }
        cacheInit = true;
    }
    protected void loadUserPreferences(){

    }
}
