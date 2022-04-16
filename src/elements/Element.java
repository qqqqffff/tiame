package elements;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import root.HomeScreen;
import root.LoadCache;

import java.util.HashMap;
import java.util.Map;

public abstract class Element {
    public Group element = new Group();
    public int ID;
    public String type;
    public abstract Group create(double x, double y, Map<String, String> metaData);
    protected abstract String getElementID();
    public abstract String generateSuperID();
    protected abstract void updatePos(double offsetX, double offsetY);
    protected abstract void hideElements();
    protected abstract void showElements();
    public abstract Map<String, String> generateMetaData();
    protected abstract void parseMetaData(Map<String, String> metaData);
    public Map<String, String> getSuperElementData(){
        Map<String, String> data = new HashMap<>();
        for(Node n : HomeScreen.homeDisplay.getChildren()){
            if(n.getId() != null){
                if(n.getId().contains(generateSuperID())){
                    Group g = (Group) n;
                    for(Node m : g.getChildren()){
                        if(m.getId() != null){
                            if(m.getId().equals("title")){
                                data.put("Title",((Text) m).getText());
                            }else if(m.getId().equals("saveTitle")){
                                data.put("Title",((TextField) m).getPromptText());
                            }else if(m.getId().equals("base")){
                                data.put("LayoutX",String.valueOf(m.getLayoutX()));
                                data.put("LayoutY",String.valueOf(m.getLayoutY()));
                            }else if(m.getId().equals("showing")){
                                data.put("Minimize","false");
                            }else if(m.getId().equals("hidden")){
                                data.put("Minimize","true");
                            }else if(m.getId().equals("pinned")){
                                data.put("Pin","true");
                            }else if(m.getId().equals("unpinned")){
                                data.put("Pin","false");
                            }
                        }
                    }
                    break;
                }
            }
        }
        return data;
    }
    private static boolean existingID(int id){
        for(int i : LoadCache.getCacheIds()){
            if(i == id){
                return true;
            }
        }
        return false;
    }
    protected void setID(){
        int id = (int) Math.floor(Math.random() * 10000000);
        while(existingID(id)){
            id = (int) Math.floor(Math.random() * 10000000);
        }
        this.ID = id;
    }
}
