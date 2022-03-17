package root;

import com.google.gson.Gson;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LoadCache {
    protected static Map<?, ?> loadCache(){
        Map<?, ?> cache = new HashMap<>();
        try{
            Gson gson = new Gson();
            for(int i : getCacheIds()) {
//                BufferedReader reader = new BufferedReader(new FileReader("src/cache/" + i + ".json"));
                System.out.println("src/cache/" + i + ".json");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return cache;
    }
    protected static void updateCache(String id, Map<String, ?> data){
        String rawID = String.valueOf(Integer.parseInt(id.substring(0, id.length() - 1)));
        Map<String, String> write = new HashMap<>();
        for (Map.Entry<String, ?> entry : data.entrySet()) {
            write.put(entry.getKey(), entry.getValue().toString());
//            System.out.println(id + ", Header: " + entry.getKey() + ", Data: " + entry.getValue().toString());
        }
        if(id.contains("N")) {
            write.put("Element", "Note");
        }
        try {
            File cache = new File("src/cache/" + rawID + ".json");

            BufferedWriter writer = new BufferedWriter(new FileWriter(cache));

            new Gson().toJson(write, writer);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static ArrayList<Integer> getCacheIds(){
        ArrayList<Integer> ids = new ArrayList<>();
        File cache = new File("src/cache");
        for(String fileN : Objects.requireNonNull(cache.list())){
            ids.add(Integer.parseInt(fileN.substring(0,fileN.length()-5)));
        }
        return ids;
    }
}
