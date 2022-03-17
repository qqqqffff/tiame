package root;

import javafx.concurrent.Task;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.text.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

//TODO: (BUG) fix formatting for time > 1HR
//TODO: set format to hh:mm:ss when editing time
public class Timer extends Task<Void> {
    private double time;
    private final String id;
    protected double currentTime;
    protected static ArrayList<Integer> timerSelectors = new ArrayList<>();
    private boolean interrupted;
    public Timer(double time, String id){
        this.time = time;
        this.id = id;
        this.interrupted = false;
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
        SimpleDateFormat formatter;
        long deltaTime = (long) currentTime;
        if( (deltaTime / (3600000)) > 1){
            formatter = new SimpleDateFormat("hh:mm:ss");
        }else if( (deltaTime / 600000) > 1){
            formatter = new SimpleDateFormat("mm:ss");
        }else if( (deltaTime / 60000) >= 1){
            formatter = new SimpleDateFormat("m:ss");
        }else{
            formatter = new SimpleDateFormat("ss.SSS");
        }
        Date date = new Date(deltaTime);
        return formatter.format(date);
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
    protected static void setTimerSelectors(int a, int b, int c, int d, int e){
        while(timerSelectors.size() < 5){
            timerSelectors.add(0);
        }

        timerSelectors.set(0, a);
        timerSelectors.set(1, b);
        timerSelectors.set(2, c);
        timerSelectors.set(3, d);
        timerSelectors.set(4, e);
    }
    private void findTimerText(boolean completed){
        for(Node n : HomeScreen.homeDisplay.getChildren()) {
            if(n.getId() != null){
//                System.out.println(n.getId() + ", "+ id);
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
