package root;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
//TODO: add text input ability
public class TimerElement extends Element{
    protected ArrayList<Integer> timerSelectors;
    private Text timer;
    private Button timeSelectorA;
    private Button timeSelectorB;
    private Button timeSelectorC;
    private Button timeSelectorD;
    private Button timeSelectorE;
    private Button startTimer;
    private Button pauseTimer;
    private final Delta timerTextOffset;
    private final Delta timerSelAOffset;
    private final Delta timerSelBOffset;
    private final Delta timerSelCOffset;
    private final Delta timerSelDOffset;
    private final Delta timerSelEOffset;
    private final Delta timerStartOffset;
    private final Delta timerPauseOffset;
    private int seed;
    protected boolean minimizable;
    private Node tempMinimize;
    final Timer[] t;
    long[] time = {0};
    public TimerElement(){
        this.timerSelectors = Timer.timerSelectors;
        timerTextOffset = new Delta(4,60);
        timerSelAOffset = new Delta(4,70);
        timerSelBOffset = new Delta(44,70);
        timerSelCOffset = new Delta(84,70);
        timerSelDOffset = new Delta(124,70);
        timerSelEOffset = new Delta(164,70);
        timerStartOffset = new Delta(204,70);
        timerPauseOffset = new Delta(196,40);

        setSeed(HomeScreen.currentElements);
        t = new Timer[]{new Timer(0, generateSuperID(),this)};
    }
    @Override
    protected Group create(double x, double y, Map<String, String> metaData) {
        if(metaData == null) {
            setID();
        }
        minimizable = true;
        element.setId(generateID());
        int selectorFontSize = 11;

        final ExecutorService[] service = {Executors.newFixedThreadPool(1)};

        timer = new Text();
        timer.setText("00:00:00");
        timer.setId("timer");
        timer.setFont(new Font(25));
        Init.formatObj(timer,x + timerTextOffset.getX(),y + timerTextOffset.getY());
        element.getChildren().add(timer);

        if(metaData != null){
            parseMetaData(metaData);
            timer.setText(t[0].formatDate(time[0]));
        }

        timeSelectorA = new Button(Timer.generateSelectorString(Timer.timerSelectors.get(0)));
        timeSelectorA.setFont(new Font(selectorFontSize));
        timeSelectorA.setTextFill(Color.LIMEGREEN);
        Init.formatObj(timeSelectorA,x + timerSelAOffset.getX(),y + timerSelAOffset.getY());
        timeSelectorA.setMaxSize(35,25);
        timeSelectorA.setOnAction(event1 -> {
            time[0] += (Timer.timerSelectors.get(0) * 60000);
            timer.setText(t[0].formatDate(time[0]));
            LoadCache.updateCache(this);
        });
        element.getChildren().add(timeSelectorA);

        timeSelectorB = new Button(Timer.generateSelectorString(Timer.timerSelectors.get(1)));
        timeSelectorB.setFont(new Font(selectorFontSize));
        timeSelectorB.setTextFill(Color.LIMEGREEN);
        Init.formatObj(timeSelectorB,x + timerSelBOffset.getX(),y + timerSelBOffset.getY());
        timeSelectorB.setMaxSize(35,25);
        timeSelectorB.setOnAction(event1 -> {
            time[0] += (Timer.timerSelectors.get(1) * 60000);
            timer.setText(t[0].formatDate(time[0]));
            LoadCache.updateCache(this);
        });
        element.getChildren().add(timeSelectorB);

        timeSelectorC = new Button(Timer.generateSelectorString(Timer.timerSelectors.get(2)));
        timeSelectorC.setFont(new Font(selectorFontSize));
        timeSelectorC.setTextFill(Color.LIMEGREEN);
        Init.formatObj(timeSelectorC,x + timerSelCOffset.getX(),y + timerSelCOffset.getY());
        timeSelectorC.setMaxSize(35,25);
        timeSelectorC.setOnAction(event1 -> {
            time[0] += (Timer.timerSelectors.get(2) * 60000);
            timer.setText(t[0].formatDate(time[0]));
            LoadCache.updateCache(this);
        });
        element.getChildren().add(timeSelectorC);

        timeSelectorD = new Button(Timer.generateSelectorString(Timer.timerSelectors.get(3)));
        timeSelectorD.setFont(new Font(selectorFontSize));
        timeSelectorD.setTextFill(Color.RED);
        Init.formatObj(timeSelectorD,x + timerSelDOffset.getX(),y + timerSelDOffset.getY());
        timeSelectorD.setMaxSize(35,25);
        timeSelectorD.setOnAction(event1 -> {
            time[0] -= (Timer.timerSelectors.get(3) * 60000);
            if(time[0] <= 0){
                time[0] = 0;
            }
            timer.setText(t[0].formatDate(time[0]));
            LoadCache.updateCache(this);
        });
        element.getChildren().add(timeSelectorD);

        timeSelectorE = new Button(Timer.generateSelectorString(Timer.timerSelectors.get(4)));
        timeSelectorE.setFont(new Font(selectorFontSize));
        timeSelectorE.setTextFill(Color.RED);
        Init.formatObj(timeSelectorE,x + timerSelEOffset.getX(),y + timerSelEOffset.getY());
        timeSelectorE.setMaxSize(35,25);
        timeSelectorE.setOnAction(event1 -> {
            time[0] -= (Timer.timerSelectors.get(4) * 60000);
            if(time[0] <= 0){
                time[0] = 0;
            }
            timer.setText(t[0].formatDate(time[0]));
        });
        element.getChildren().add(timeSelectorE);

        startTimer = new Button();
        startTimer.setGraphic(Screen.resources.getImage("start"));
        startTimer.setId("timeStart");
        startTimer.setFont(new Font(12));
        Init.formatObj(startTimer,x + timerStartOffset.getX(),y + timerStartOffset.getY());
        startTimer.setOnAction(event -> {
            //TODO: add error handling
            if(time[0] == 0){
                time[0] = 60000;
            }
            element.getChildren().remove(timeSelectorA);
            element.getChildren().remove(timeSelectorB);
            element.getChildren().remove(timeSelectorC);
            element.getChildren().remove(timeSelectorD);
            element.getChildren().remove(timeSelectorE);
            element.getChildren().remove(startTimer);

            for(Node n : HomeScreen.homeDisplay.getChildren()){
                if(n.getId() != null){
                    if(n.getId().contains(generateSuperID())){
                        Group g = (Group) n;
                        for(int i = 0; i < g.getChildren().size(); i++){
                            if(g.getChildren().get(i).getId() != null){
                                if(g.getChildren().get(i).getId().contains("base")){
                                    ((Rectangle) g.getChildren().get(i)).setHeight(65);
                                }else if(g.getChildren().get(i).getId().equals("hidden") ||
                                        g.getChildren().get(i).getId().equals("showing")){
                                    System.out.println("found");
                                    tempMinimize = g.getChildren().get(i);
                                    g.getChildren().remove(i);
                                    i--;
                                }else if(g.getChildren().get(i).getId().equals("save")){
                                    Init.formatObj(g.getChildren().get(i),g.getChildren().get(i).getLayoutX()+25,g.getChildren().get(i).getLayoutY());
                                }
                            }
                        }
                        break;
                    }
                }
            }
            minimizable = false;

            element.getChildren().add(pauseTimer);

            service[0].execute(t[0]);
            service[0].shutdown();
        });
        element.getChildren().add(startTimer);

        //TODO: fix formatting of the pause button
        pauseTimer = new Button();
        pauseTimer.setGraphic(Screen.resources.getImage("pause"));
        pauseTimer.setId("timePause");
        pauseTimer.setFont(new Font(12));
        Init.formatObj(pauseTimer,x + timerPauseOffset.getX(),y + timerPauseOffset.getY());
        pauseTimer.setOnAction(event -> {
            t[0].interrupt();
            t[0] = new Timer(t[0].currentTime,generateSuperID(),this);
            service[0].shutdownNow();
            service[0] = Executors.newFixedThreadPool(1);

            for(Node n : HomeScreen.homeDisplay.getChildren()){
                if(n.getId() != null){
                    if(n.getId().contains(generateSuperID())){
                        Group g = (Group) n;
                        for(int i = 0; i < g.getChildren().size(); i++){
                            if(g.getChildren().get(i).getId() != null){
                                if(g.getChildren().get(i).getId().contains("base")){
                                    ((Rectangle) g.getChildren().get(i)).setHeight(100);
                                }else if(g.getChildren().get(i).getId().equals("save")){
                                    Init.formatObj(g.getChildren().get(i),g.getChildren().get(i).getLayoutX() - 25,g.getChildren().get(i).getLayoutY());
                                }
                            }
                        }
                        g.getChildren().add(tempMinimize);
                        break;
                    }
                }
            }
            minimizable = true;

            element.getChildren().add(timeSelectorA);
            element.getChildren().add(timeSelectorB);
            element.getChildren().add(timeSelectorC);
            element.getChildren().add(timeSelectorD);
            element.getChildren().add(timeSelectorE);
            element.getChildren().add(startTimer);

            element.getChildren().remove(pauseTimer);
        });

        return element;
    }
    @Override
    protected String getElementID() {
        return element.getId();
    }
    private void setSeed(int seed){
        this.seed = 1000 + seed;
    }
    private String generateID(){
        return seed + "ET";
    }
    @Override
    protected String generateSuperID(){
        return seed + "T";
    }
    @Override
    protected void updatePos(double offsetX, double offsetY) {
        Init.formatObj(timer,offsetX + timerTextOffset.getX(),offsetY + timerTextOffset.getY());
        Init.formatObj(timeSelectorA,offsetX + timerSelAOffset.getX(),offsetY + timerSelAOffset.getY());
        Init.formatObj(timeSelectorB,offsetX + timerSelBOffset.getX(),offsetY + timerSelBOffset.getY());
        Init.formatObj(timeSelectorC,offsetX + timerSelCOffset.getX(),offsetY + timerSelCOffset.getY());
        Init.formatObj(timeSelectorD,offsetX + timerSelDOffset.getX(),offsetY + timerSelDOffset.getY());
        Init.formatObj(timeSelectorE,offsetX + timerSelEOffset.getX(),offsetY + timerSelEOffset.getY());
        Init.formatObj(startTimer,offsetX + timerStartOffset.getX(),offsetY + timerStartOffset.getY());
        Init.formatObj(pauseTimer,offsetX + timerPauseOffset.getX(),offsetY + timerPauseOffset.getY());
    }

    @Override
    protected void hideElements() {
        if(minimizable) {
            Init.hideElement(timeSelectorA);
            Init.hideElement(timeSelectorB);
            Init.hideElement(timeSelectorC);
            Init.hideElement(timeSelectorD);
            Init.hideElement(timeSelectorE);
            Init.hideElement(startTimer);
        }
    }

    @Override
    protected void showElements() {
        if(minimizable) {
            Init.showElement(timeSelectorA);
            Init.showElement(timeSelectorB);
            Init.showElement(timeSelectorC);
            Init.showElement(timeSelectorD);
            Init.showElement(timeSelectorE);
            Init.showElement(startTimer);
        }
    }

    @Override
    protected Map<String, ?> generateMetaData() {
        Map<String, String> metaData = new HashMap<>(getSuperElementData());

        metaData.put("Time",String.valueOf(this.time[0]));
        metaData.put("ID",String.valueOf(this.ID));

        return metaData;
    }

    @Override
    protected void parseMetaData(Map<String, String> metaData) {
        for(Map.Entry<String, String> entry : metaData.entrySet()){
            if(entry.getKey().equals("Time")){
                time[0] = (long) Double.parseDouble(entry.getValue());
            }else if(entry.getKey().equals("ID")){
                this.ID = Integer.parseInt(entry.getValue());
            }
        }
    }
    protected void updateTime(Long newTime){
        this.time[0] = newTime;
    }
}