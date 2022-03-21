package root;

import com.google.gson.Gson;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

//TODO: verify generated id doesnt already exist
public class LoadCache {
    protected static Map<String, Map<String, String>> loadCache(){
        Map<String, Map<String, String>> cache = new HashMap<>();
        try{
            Gson gson = new Gson();
            for(int i : getCacheIds()) {
                BufferedReader reader = new BufferedReader(new FileReader("src/cache/" + Screen.user.userName + "/" + i + ".json"));

                Map<?, ?> read = gson.fromJson(reader, Map.class);
                reader.close();


                Map<String, String> data = new HashMap<>();
                for(Map.Entry<?, ?> entry : read.entrySet()){
                    data.put(entry.getKey().toString(), entry.getValue().toString());
                }

                cache.put(String.valueOf(i), data);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return cache;
    }
    protected static void updateCache(Element e){
        String rawID = String.valueOf(e.ID);
        Map<String, String> write = new HashMap<>();
        for (Map.Entry<String, ?> entry : e.generateMetaData().entrySet()) {
            write.put(entry.getKey(), entry.getValue().toString());
//            System.out.println("Header: " + entry.getKey() + ", Data: " + entry.getValue().toString());
        }
        if(e.generateSuperID().contains("N")) {
            write.put("Element", "Note");
        }
        try {
            if (!Screen.user.cacheInit) {
                Screen.user.initializeCache();
            }
            File cache = new File("src/cache/" + Screen.user.userName + "/" + rawID + ".json");

            BufferedWriter writer = new BufferedWriter(new FileWriter(cache));

            new Gson().toJson(write, writer);
            writer.close();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
    protected static ArrayList<Integer> getCacheIds(){
        ArrayList<Integer> ids = new ArrayList<>();
        File cache = new File("src/cache/" + Screen.user.userName );
        for(String fileN : Objects.requireNonNull(cache.list())){
            ids.add(Integer.parseInt(fileN.substring(0,fileN.length()-5)));
        }
        return ids;
    }
    protected static void clearCache(int ID){
        File toDelete = new File("src/cache/" + Screen.user.userName + "/" + ID + ".json");
        toDelete.delete();
    }
    protected static void updateUserCache(String uID, Map<String, String> data){

    }
}
