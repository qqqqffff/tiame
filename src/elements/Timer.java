package elements;

import elements.TimerElement;
import javafx.concurrent.Task;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.text.Text;
import root.HomeScreen;
import root.LoadCache;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

//TODO: create AI to make a default timer
public class Timer extends Task<Void> {
    private double time;
    private String id;
    protected double currentTime;
    public static ArrayList<Integer> timerSelectors = new ArrayList<>();
    private boolean interrupted;
    private TimerElement te;
    public Timer(double time, String id, TimerElement te){
        this.time = time;
        this.id = id;
        this.interrupted = false;
        this.te = te;
    }
    //TODO: Add an event that happens when the timer is finished
    @Override
    protected Void call() throws Exception {
        int timeTo = 0;
        while(time > timeTo && !interrupted) {
            Thread.sleep(analyzeCT());
            timeTo += analyzeCT();
            currentTime = time - timeTo;
            findTimerText(false);
            te.updateTime((long) currentTime);
            LoadCache.updateCache(te);
        }
        if(!interrupted) {
            findTimerText(true);
        }
        return null;
    }
    protected void interrupt(){
        this.interrupted = true;
    }
    private String formatDate(){
        DateTimeFormatter formatter1;
        long hourC = 3600000L;
        long minuteC = 60000L;
        long secondC = 1000L;
        int minutes = 0, hours = 0, seconds = 0, millis = 0;
        LocalTime ctime = LocalTime.of(hours,minutes,seconds);
        if( (currentTime / 60000) >= 1){
            formatter1 = DateTimeFormatter.ofPattern("HH:mm:ss");
            hours = (int) (currentTime / hourC);
            minutes = (int) ((currentTime - (hours * hourC))/ (minuteC));
            seconds = (int) ((currentTime - (hours * hourC) - (minutes * minuteC)) / secondC);
            ctime = LocalTime.of(hours,minutes,seconds);
        }else {
            formatter1 = DateTimeFormatter.ofPattern("HH:mm:ss SSS");
            hours = (int) (currentTime / hourC);
            minutes = (int) ((currentTime - (hours * hourC))/ (minuteC));
            seconds = (int) ((currentTime - (hours * hourC) - (minutes * minuteC)) / secondC);
            millis = (int) ((currentTime - (hours * hourC) - (minutes * minuteC) - (seconds * secondC)));
            ctime = LocalTime.of(hours,minutes,seconds).plus(millis, ChronoUnit.MILLIS);
        }



        return formatter1.format(ctime);
    }
    protected String formatDate(double time){
        this.time = time;
        currentTime = time;
        return formatDate();
    }
    private int analyzeCT(){
        int timeFactor = 100;
        if(currentTime < 60000){
            timeFactor = 60;
        }
        return timeFactor;
    }
    protected static String generateSelectorString(int time){
        if(time > 60){
            return time + "h";
        }
        return time + "m";
    }
    public static void initTimerSelectors(){
        while(timerSelectors.size() < 5){
            timerSelectors.add(0);
        }
    }
    public static void setDefaultTimerSelectors(){
        initTimerSelectors();

        timerSelectors.set(0, 1);
        timerSelectors.set(1, 5);
        timerSelectors.set(2, 30);
        timerSelectors.set(3, 1);
        timerSelectors.set(4, 5);
    }
    public static void setTimerSelectors(int index, int val){
        if(timerSelectors != null) {
            initTimerSelectors();
        }
        assert timerSelectors != null;
        timerSelectors.set(index,val);
    }
    private void findTimerText(boolean completed){
        for(Node n : HomeScreen.homeDisplay.getChildren()) {
            if(n.getId() != null){
                System.out.println(n.getId() + ", "+ id);
                if(n.getId().equals(id)) {
                    Group g = (Group) n;
                    for(Node p : g.getChildren()){
                        if(p.getId() != null){
                            if(p.getId().contains("ET")){
                                Group j = (Group) p;
                                for(Node k : j.getChildren()) {
                                    if(k.getId() != null) {
                                        if(k.getId().contains("timer")) {
                                            if(!completed) {
                                                ((Text) k).setText(formatDate());
                                            }else {
                                                //TODO: Replace with completion Message
                                                ((Text) k).setText("Time Up!");
                                            }
                                            break;
                                        }
                                    }
                                }
                                break;
                            }
                        }
                    }
                    break;
                }
            }
        }
    }
}
