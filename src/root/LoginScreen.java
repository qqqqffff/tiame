package root;

import events.LoginEvents;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//TODO: Create Logo for login screen
public class LoginScreen {
    public static Group loginDisplay;
    public static Group createAccount;
    public static void display0(){
        loginDisplay = new Group();
        loginDisplay.setId("loginDisplay");

        TextField userField = new TextField();
        Init.formatObj(userField,400,200);
        userField.setMaxSize(150,45);
        if(Screen.saveLogin){
            userField.setText(Screen.pastUsername);
        }else {
            userField.setPromptText("Username");
        }
        loginDisplay.getChildren().add(userField);

        Label userFieldLabel = new Label("Username:");
        Init.formatObj(userFieldLabel,315,202);
        userFieldLabel.setLabelFor(userField);
        userFieldLabel.setFont(new Font(15));
        loginDisplay.getChildren().add(userFieldLabel);

        PasswordField passField = new PasswordField();
        Init.formatObj(passField,400,250);
        passField.setMaxSize(150,45);
        passField.setPromptText("Password");
        loginDisplay.getChildren().add(passField);

        Label passFieldLabel = new Label("Password:");
        Init.formatObj(passFieldLabel,315,252);
        passFieldLabel.setLabelFor(passField);
        passFieldLabel.setFont(new Font(15));
        loginDisplay.getChildren().add(passFieldLabel);

        Text createdAcc = new Text("Account Successfully Created!");
        createdAcc.setId("successCreate");
        createdAcc.setFont(new Font(15));
        Init.formatObj(createdAcc,320,150);
        createdAcc.setFill(Color.LIMEGREEN);
        createdAcc.setOpacity(0);
        loginDisplay.getChildren().add(createdAcc);

        CheckBox saveLogin = new CheckBox("Stay Logged In?");
        Init.formatObj(saveLogin,325,300);
        saveLogin.setFont(new Font(12));
        if(Screen.saveLogin){
            saveLogin.setSelected(true);
        }
        loginDisplay.getChildren().add(saveLogin);

        Button signIn = new Button("Login");
        signIn.setId("login");
        Init.formatObj(signIn, 580,250);
        signIn.setMaxSize(50,30);
        signIn.setOnAction(event -> {
            String pass = passField.getText();
            String userN = userField.getText();
            if(!(pass.equals("") || userN.equals(""))) {
                if(Screen.user.signInAttempt(userN, pass)) { //Success
                    Screen.user.initializeCache();
                    Init.updateInit(saveLogin.isSelected(), userField.getText());
                    Screen.root.getChildren().remove(loginDisplay);
                    HomeScreen.display1();
                    HomeScreen.loadFromCache(LoadCache.loadCache());
                }else{ //error
                    LoginEvents error = new LoginEvents(LoginEvents.Event.incorrectInfo);
                    ExecutorService service = Executors.newFixedThreadPool(1);
                    service.execute(error);
                    service.shutdown();
                }
            }
            if (userN.equals("")) { //Blank User
                LoginEvents error = new LoginEvents(LoginEvents.Event.blankUser);
                ExecutorService service = Executors.newFixedThreadPool(1);
                service.execute(error);
                service.shutdown();
            }
            if (pass.equals("")){ //Blank Pass
                LoginEvents error = new LoginEvents(LoginEvents.Event.blankPass);
                ExecutorService service = Executors.newFixedThreadPool(1);
                service.execute(error);
                service.shutdown();
            }
        });
        signIn.requestFocus();
        loginDisplay.getChildren().add(signIn);

        Hyperlink createAcc = new Hyperlink("Create an Account");
        Init.formatObj(createAcc,580,200);
        createAcc.setFont(new Font(12));
        createAcc.setOnAction(event -> {
            for(Node n : loginDisplay.getChildren()){
                n.setEffect(new GaussianBlur());
            }
            loginDisplay.getChildren().add(createAccount());
        });
        loginDisplay.getChildren().add(createAcc);


        loginDisplay.getChildren().add(blankPass());
        loginDisplay.getChildren().add(blankUser());
        loginDisplay.getChildren().add(incorrectInfo());

        Screen.root.getChildren().add(loginDisplay);
    }
    //TODO: update with icons
    private static Group createAccount(){
        createAccount = new Group();
        createAccount.setId("createAccount");

        Rectangle background = new Rectangle();
        background.setWidth(400);
        background.setHeight(600);
        Init.formatObj(background,450,100);
        background.setFill(Color.WHITE);
        background.setStroke(Color.BLACK);
        background.setStrokeWidth(1.5);
        createAccount.getChildren().add(background);

        TextField userField = new TextField();
        Init.formatObj(userField,575,200);
        userField.setMaxSize(150,45);
        userField.setPromptText("Username");
        createAccount.getChildren().add(userField);

        Label userLabel = new Label("Username");
        Init.formatObj(userLabel,490,202);
        userLabel.setLabelFor(userField);
        userLabel.setFont(new Font(15));
        createAccount.getChildren().add(userLabel);

        PasswordField passField = new PasswordField();
        Init.formatObj(passField,575,250);
        passField.setMaxSize(150,45);
        passField.setPromptText("Password");
        createAccount.getChildren().add(passField);

        Label passLabel = new Label("Password:");
        Init.formatObj(passLabel,490,252);
        passLabel.setLabelFor(passField);
        passLabel.setFont(new Font(15));
        createAccount.getChildren().add(passLabel);

        Text duplicateUser = new Text("Username already exists");
        duplicateUser.setId("duplicate");
        duplicateUser.setFont(new Font(15));
        Init.formatObj(duplicateUser,490,325);
        duplicateUser.setFill(Color.RED);
        duplicateUser.setOpacity(0);
        createAccount.getChildren().add(duplicateUser);

        Text failedPassword = new Text("Password field is blank");
        failedPassword.setId("blankC");
        failedPassword.setFont(new Font(15));
        Init.formatObj(failedPassword,490,325);
        failedPassword.setFill(Color.RED);
        failedPassword.setOpacity(0);
        createAccount.getChildren().add(failedPassword);

        Text failedUser = new Text("Username field is blank");
        failedUser.setId("blankC");
        failedUser.setFont(new Font(15));
        Init.formatObj(failedUser,490,350);
        failedUser.setFill(Color.RED);
        failedUser.setOpacity(0);
        createAccount.getChildren().add(failedUser);

        Button create = new Button("Create");
        Init.formatObj(create,750,250);
        create.setOnAction(event -> {
            String userN = userField.getText();
            String pass = passField.getText();
            if(!(pass.equals("") || userN.equals(""))) {
                if (UserData.checkDuplicate(userN)) {
                    System.out.println("creating user: " + userN);
                    UserData.setData(userN, pass);
                    loginDisplay.getChildren().remove(createAccount);
                    for (Node n : loginDisplay.getChildren()) {
                        n.setEffect(null);
                    }

                    for(int i = 0; i < loginDisplay.getChildren().size(); i++){
                        if(loginDisplay.getChildren().get(i).getId() != null){
                            if(loginDisplay.getChildren().get(i).getId().equals("login")){
                                loginDisplay.getChildren().get(i).requestFocus();
                                break;
                            }
                        }
                    }

                    EventHandling e = new EventHandling(0);
                    ExecutorService service = Executors.newFixedThreadPool(1);
                    service.execute(e);
                    service.shutdown();
                } else {
                    userField.setText(null);
                    ErrorHandling task = new ErrorHandling(3);
                    ExecutorService service = Executors.newFixedThreadPool(1);
                    service.execute(task);
                    service.shutdown();
                }
            }
            if (userN.equals("")) {
                ErrorHandling task = new ErrorHandling(4);
                ExecutorService service = Executors.newFixedThreadPool(1);
                service.execute(task);
                service.shutdown();
            }
            if (pass.equals("")){
                ErrorHandling task = new ErrorHandling(4);
                ExecutorService service = Executors.newFixedThreadPool(1);
                service.execute(task);
                service.shutdown();
            }
        });
        create.requestFocus();
        createAccount.getChildren().add(create);

        Button exit = new Button("Exit");
        Init.formatObj(exit,750,135);
        exit.setOnAction(event -> {
            loginDisplay.getChildren().remove(createAccount);
            for(Node n : loginDisplay.getChildren()){
                n.setEffect(null);
            }
        });
        createAccount.getChildren().add(exit);

        return createAccount;
    }
    private static Text blankUser(){
        Text failedUser = new Text("Username field is blank");
        failedUser.setId("blankU");
        failedUser.setFont(new Font(15));
        Init.formatObj(failedUser,325,350);
        failedUser.setFill(Color.RED);
        failedUser.setOpacity(0);
        return failedUser;
    }
    private static Text blankPass(){
        Text failedPass = new Text("Password field is blank");
        failedPass.setId("blankP");
        failedPass.setFont(new Font(15));
        Init.formatObj(failedPass,325,375);
        failedPass.setFill(Color.RED);
        failedPass.setOpacity(0);
        return failedPass;
    }
    private static Text incorrectInfo(){
        Text wrongPass = new Text("Incorrect Username or Password");
        wrongPass.setId("wrong");
        wrongPass.setFont(new Font(15));
        Init.formatObj(wrongPass,325,400);
        wrongPass.setFill(Color.RED);
        wrongPass.setOpacity(0);
        return wrongPass;
    }
}
