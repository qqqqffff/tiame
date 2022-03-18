package root;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.util.HashMap;
import java.util.Map;

public abstract class Element {
    Group element = new Group();
    protected abstract Group create(double x, double y, Map<String, String> metaData);
    protected abstract String getElementID();
    protected abstract String generateSuperID();
    protected abstract void updatePos(double offsetX, double offsetY);
    protected abstract void hideElements();
    protected abstract void showElements();
    protected abstract Map<String, ?> generateMetaData();
    protected abstract void parseMetaData(Map<String, String> metaData);
    protected Map<String, String> getSuperElementData(){
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
}
