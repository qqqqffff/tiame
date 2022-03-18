package root;

import javafx.scene.Group;
import javafx.scene.control.TextArea;
import javafx.scene.text.Font;

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
    public Group create(double x, double y, Map<String, String> metaData) {
        if(metaData == null) {
            setID();
        }else{
            parseMetaData(metaData);
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
    public String getElementID(){
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
        Map<String, String> metaData = new HashMap<>(getSuperElementData());

        metaData.put("TextArea",ta.getText());
        metaData.put("ID",String.valueOf(this.ID));

        return metaData;
    }

    @Override
    protected void parseMetaData(Map<String, String> metaData) {
        for(Map.Entry<String, String> entry : metaData.entrySet()){
            if(entry.getKey().equals("TextArea")){
                ta.setText(entry.getValue());
            }else if(entry.getKey().equals("ID")){
                this.ID = Integer.parseInt(entry.getValue());
            }
        }
    }
    private void setID(){
        this.ID = (int) Math.floor(Math.random() * 10000000);
    }
}
