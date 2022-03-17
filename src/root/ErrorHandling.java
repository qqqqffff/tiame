package root;

import javafx.concurrent.Task;

public class ErrorHandling extends Task<Void> {
    private final int errorValue;
    public ErrorHandling(int errorValue){
        this.errorValue = errorValue;
    }
    @Override
    public Void call() throws Exception {
        if(errorValue == 1) {
            int index = -1;
            for (int i = 0; i < LoginScreen.loginDisplay.getChildren().size(); i++) {
                if (LoginScreen.loginDisplay.getChildren().get(i).getId() != null) {
                    if (LoginScreen.loginDisplay.getChildren().get(i).getId().contains("blank")) {
                        index = i;
                        break;
                    }
                }
            }
            if(index != -1){
                LoginScreen.loginDisplay.getChildren().get(index).setOpacity(1);
                LoginScreen.loginDisplay.getChildren().get(index+1).setOpacity(1);
                Thread.sleep(5000);
                for(double i = 0.95 ; i >= 0 ; i -= 0.05) {
                    LoginScreen.loginDisplay.getChildren().get(index).setOpacity(i);
                    LoginScreen.loginDisplay.getChildren().get(index+1).setOpacity(i);
                    Thread.sleep(50);
                }
                LoginScreen.loginDisplay.getChildren().get(index).setOpacity(0);
                LoginScreen.loginDisplay.getChildren().get(index+1).setOpacity(0);
            }else{
                System.out.println("Elements not Found!");
            }
        }
        else if(errorValue == 2){
            int index = -1;
            for (int i = 0; i < LoginScreen.loginDisplay.getChildren().size(); i++) {
                if (LoginScreen.loginDisplay.getChildren().get(i).getId() != null) {
                    if (LoginScreen.loginDisplay.getChildren().get(i).getId().contains("wrongPass")) {
                        index = i;
                        break;
                    }
                }
            }
            if(index != -1){
                LoginScreen.loginDisplay.getChildren().get(index).setOpacity(1);
                Thread.sleep(3500);
                for(double i = 0.95 ; i >= 0 ; i -= 0.05) {
                    LoginScreen.loginDisplay.getChildren().get(index).setOpacity(i);
                    Thread.sleep(50);
                }
                LoginScreen.loginDisplay.getChildren().get(index).setOpacity(0);
            }else{
                System.out.println("Element not Found!");
            }
        }
        else if(errorValue == 3){
            int index = -1;
            for (int i = 0; i < LoginScreen.createAccount.getChildren().size(); i++) {
                if (LoginScreen.createAccount.getChildren().get(i).getId() != null) {
                    if (LoginScreen.createAccount.getChildren().get(i).getId().contains("duplicate")) {
                        index = i;
                        break;
                    }
                }
            }
            if(index != -1){
                LoginScreen.createAccount.getChildren().get(index).setOpacity(1);
                Thread.sleep(4000);
                for(double i = 0.95 ; i >= 0 ; i -= 0.05) {
                    LoginScreen.createAccount.getChildren().get(index).setOpacity(i);
                    Thread.sleep(50);
                }
                LoginScreen.createAccount.getChildren().get(index).setOpacity(0);
            }else{
                System.out.println("Element not Found!");
            }
        }
        else if(errorValue == 4){
            int index = -1;
            for (int i = 0; i < LoginScreen.createAccount.getChildren().size(); i++) {
                if (LoginScreen.createAccount.getChildren().get(i).getId() != null) {
                    if (LoginScreen.createAccount.getChildren().get(i).getId().contains("blankC")) {
                        System.out.println("found index");
                        index = i;
                        break;
                    }
                }
            }
            if(index != -1){
                LoginScreen.createAccount.getChildren().get(index).setOpacity(1);
                LoginScreen.createAccount.getChildren().get(index+1).setOpacity(1);
                Thread.sleep(5000);
                for(double i = 0.95 ; i >= 0 ; i -= 0.05) {
                    LoginScreen.createAccount.getChildren().get(index).setOpacity(i);
                    LoginScreen.createAccount.getChildren().get(index+1).setOpacity(i);
                    Thread.sleep(50);
                }
                LoginScreen.createAccount.getChildren().get(index).setOpacity(0);
                LoginScreen.createAccount.getChildren().get(index+1).setOpacity(0);
            }else{
                System.out.println("Element not Found!");
            }
        }
        return null;
    }
}
