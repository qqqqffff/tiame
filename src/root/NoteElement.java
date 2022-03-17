package root;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.HashMap;
import java.util.Map;

//TODO: Add Option to archive Note after Title is entered
public class NoteElement extends Element{
    private final TextArea ta;
    private final Delta taOffset;
    private int seed;
    protected int ID;
    public NoteElement(){
        taOffset = new Delta(4,39);
        ta = new TextArea();
    }
    public Group create(double x, double y, Map<String, ?> metaData) {
        if(metaData == null) {
            setID();
        }
        setSeed(HomeScreen.currentElements);
        element.setId(generateID());

        Init.formatObj(ta,x + taOffset.getX(),y + taOffset.getY());
        ta.setMinHeight(304);
        ta.setMaxWidth(242);
        ta.setFont(new Font(15));
        ta.setWrapText(true);
        element.getChildren().add(ta);

        return element;
    }
    private void setSeed(int seed){
        this.seed = 1000 + seed;
    }
    private String generateID() {
        return this.seed + "EN";
    }
    protected String generateSuperID(){ return this.seed + "N"; }
    public String getID(){
        return element.getId();
    }
    public void updatePos(double offsetX, double offsetY) {
        Init.formatObj(ta,offsetX + taOffset.getX(),offsetY + taOffset.getY());
    }

    @Override
    protected void hideElements() {
        Init.hideElement(ta);
    }

    @Override
    protected void showElements() {
        Init.showElement(ta);
    }

    @Override
    protected Map<String, ?> generateMetaData() {
        Map<String, String> metaData = new HashMap<>();

        metaData.put("TextArea",ta.getText());

        for(Node n : HomeScreen.homeDisplay.getChildren()){
            if(n.getId() != null){
                if(n.getId().contains(generateSuperID())){
                    Group g = (Group) n;
                    for(Node m : g.getChildren()){
                        if(m.getId() != null){
                            if(m.getId().equals("title")){
                                metaData.put("Title",((Text) m).getText());
                            }else if(m.getId().equals("saveTitle")){
                                metaData.put("Title",((TextField) m).getPromptText());
                            }if(m.getId().equals("base")){
                                metaData.put("LayoutX",String.valueOf(m.getLayoutX()));
                                metaData.put("LayoutY",String.valueOf(m.getLayoutY()));
                            }
                        }
                    }
                    break;
                }
            }
        }

        return metaData;
    }

    @Override
    protected void parseMetaData() {

    }
    private void setID(){
        this.ID = (int) Math.floor(Math.random() * 10000000);
    }
}
