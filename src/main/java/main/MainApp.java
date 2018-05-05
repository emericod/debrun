package main;

import controllers.*;
import dao.OfflineDao;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import models.Applicant;
import models.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import services.OfflineService;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;

/**
 * <h1>Debrun Applicant Management Application</h1>
 * This application was created for the WizzAir Airport Running Race.
 * This implements the local login applicant management at the race.
 * First running, when the local database is empty, run this with connection to the remoted database.
 * Log into the application, what the admin gived for you to connect the remoted MySql database.
 * After the login, the application storing your login datas into the local SqlLite database, so next time you can be offline, the application will allow log in.
 * After the first time running, the application will sync the applicants between your local SqlLite and remoted Mysql database.
 * This application is made for mobile internet based connection and planned for small data trafic. The data security was important, therefore it based two database (local and remoted)
 * The connection is monitored while running the application, if the connection has broken, you can work in the application, because everything is storing into your local database,
 * and when the connection will fixed, you can synchronize your database. This application supported the multi client working, it means if more than one client using this application,
 * everybody will see the newest versions of applicant attributes. There is automatic synchronizing, the period time can be configurable in preferences.
 *
 * @author Ilyés Imre
 * @version 1.0
 * @since 2018-04-08
 */

public class MainApp extends Application {

    /**
     * Main stage of application.
     */
    public static Stage primaryStage;

    /**
     * Root panel of application.
     */
    public static BorderPane rootPanel;

    /**
     * Views of main windows.
     */
    public static AnchorPane loginWindow, homeWindow, dialogPanel;

    /**
     * FXML loader of about dialog.
     */
    public static FXMLLoader aboutWindow;
    /**
     * FXML loader of preferences dialog.
     */
    public static FXMLLoader preferencesWindow;
    /**
     * FXML loader of home window.
     */
    public static FXMLLoader homeWindowLoader;
    /**
     * Controller of loaded home window.
     */
    public static HomeWindowController actualHomeWindowcontroller;

    /**
     * Controller of loaded menubar.
     */
    public static MenuBarController actualMenuBarController;

    /**
     * Menubar.
     */
    public static MenuBar menuBar;

    /**
     * Main scene of application.
     */
    private static Scene scene;

    /**
     * Logger of application.
     */
    public static Logger logger = LoggerFactory.getLogger(MainApp.class);

    /**
     * Logged in actual user.
     */
    public static Client actualUser;

    /**
     * Dialog panel.
     */
    public static Stage dialogStage;

    /**
     * Controller of loaded statusBar.
     */
    public static StatusBarController actualStatusbarController;

    /**
     * Offline DAO.
     */
    public OfflineDao dao;

    /**
     * Offline service of Offline DAO.
     */
    public OfflineService service;

    /**
     * FXML loader of status bar.
     */
    public static FXMLLoader StatusBarloader;

    /**
     * Constructor of MainApp.
     */
    public MainApp() {
        try {
            dao = new OfflineDao();
            service = new OfflineService(dao);
        } catch (SQLException e) {
            MainApp.logger.error("Source of error: " + e.getMessage());
        }
    }

    /**
     * Start implemented javaFx method.
     * @param primaryStage is the main panel / window.
     * @throws Exception if there is any problem with SQL database.
     */
    public void start(Stage primaryStage) throws Exception {
        logger.info("Creating and setting up the main window");
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("WIZZ AIR Airport Run Debrecen");
        this.primaryStage.setMinWidth(850);
        this.primaryStage.setMinHeight(640);

        logger.info("Sign up for main window's close event");
        this.primaryStage.setOnCloseRequest((WindowEvent event) -> {

                Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION, "A módosításokat szeretné szinkronizálni a központi adatbázissal?", ButtonType.YES, ButtonType.NO);
                alert1.headerTextProperty().setValue("Adatok szinkronizálása kilépés előtt...");
                alert1.setTitle("Adatbázis szinkronizálás");
                alert1.showAndWait();

            if(HomeWindowController.networkStatus) {
                if (alert1.getResult() == ButtonType.YES) {
                    try {
                            if(service.sendApplicantsToMysql()){
                                logger.info("Successfuly connected to the remoted database...");
                            }
                            else{
                                logger.error("The remoted database is not available...");
                            }
                        } catch (SQLException e) {
                            MainApp.logger.error("Source of error: " + e.getMessage());
                        }
                        logger.info("Closing application...");
                        primaryStage.close();
                    }
                    else if(alert1.getResult() == ButtonType.NO){
                        logger.info("Synchronizing is aborted by user...");
                        logger.info("Closing application...");
                        primaryStage.close();
                    }
            }
            else{
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Az adatok szinkronizálásához kapcsolódnia kell a hálózathoz, a szinkronizálás elmulasztása adatvesztéshez vezethet! A progrm újraindítása után kapcsolódva a hálózathoz szinkronizálhatja az adatokat!", ButtonType.CLOSE, ButtonType.CANCEL);
                alert.headerTextProperty().setValue("Adatok szinkronizálása kilépés előtt...");
                alert.setTitle("Adatbázis szinkronizálás");
                alert.showAndWait();

                if (alert.getResult() == ButtonType.YES) {
                    logger.info("Exit without sysnchronizing...");
                    primaryStage.close();
                }
                else if (alert.getResult() == ButtonType.CANCEL) {
                    logger.info("Exit aborted");
                    event.consume();

                }
            }

        });

        logger.info("Preparing main panel");
        rootPanel = new BorderPane();
        rootPanel.setMinWidth(800);
        rootPanel.setMinHeight(600);

        rootPanel.setStyle("-fx-background-color: #FCEDF5;");
        FXMLLoader dialogloader = new FXMLLoader();
        dialogloader.setLocation(MainApp.class.getResource("/views/ApplicantDialog.fxml"));
        dialogPanel = dialogloader.load();

        scene = new Scene(rootPanel);
        logger.info("Loading window components...:");
        try {
            FXMLLoader menuBarLoader = new FXMLLoader();
            menuBarLoader.setLocation(MainApp.class.getResource("/views/MenuBar.fxml"));
            menuBar = menuBarLoader.load();
            actualMenuBarController = menuBarLoader.getController();

            logger.info("Menubar is loaded...");
            loginWindow = FXMLLoader.load(main.MainApp.class.getResource("/views/LoginWindow.fxml"));
            logger.info("Login window is loaded...");
            homeWindowLoader = new FXMLLoader();
            homeWindowLoader.setLocation(main.MainApp.class.getResource("/views/HomeWindow.fxml"));
            homeWindow = (AnchorPane) homeWindowLoader.load();
            logger.info("Main window is loaded...");
        }
        catch(Exception e){
            MainApp.logger.error("Source of error: " + e.getMessage());
        }

        logger.info("Showing login window...");
        SetActiveWindow(loginWindow);
        primaryStage.show();
    }

    /**
     * Main method.
     * @param args are arguments of main method.
     * @throws ClassNotFoundException if there is any class not found exception.
     */
    public static void main(String[] args) throws ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        launch(args);
    }


    /**
     * Setting and loading active window by parameter.
     * @param pane is a panel of views.
     */
    public static void SetActiveWindow(AnchorPane pane){
        if(pane != null) {
            if (!pane.equals(loginWindow)) {
                logger.info("Showing menubar...");
                rootPanel.setTop(menuBar);
                logger.info("Showing sidebar...");
                SetSidebar();
                logger.info("Showing statusbar...");
                SetStatusBar();
                if(homeWindowLoader != null){
                    actualHomeWindowcontroller = homeWindowLoader.getController();
                    actualHomeWindowcontroller.syncRefresh();
                }
            }

            logger.info("Loading content to the main window...");
            rootPanel.setCenter(pane);
            primaryStage.setScene(scene);
        }
        else{
            try {
                logger.info("Creating main window...");
                homeWindowLoader = new FXMLLoader();
                homeWindowLoader.setLocation(MainApp.class.getResource("/views/HomeWindow.fxml"));
                homeWindow = (AnchorPane) homeWindowLoader.load();
                actualHomeWindowcontroller = homeWindowLoader.getController();
                logger.info("Showing menubar...");
                rootPanel.setTop(menuBar);
                logger.info("Showing sidebar...");
                SetSidebar();
                logger.info("Showing statusbar...");
                SetStatusBar();
                rootPanel.setCenter(homeWindow);
                logger.info("Setting main window...");
                primaryStage.setScene(scene);
            } catch (IOException e) {
                MainApp.logger.error("Source of error: " + e.getMessage());
            }
        }
    }


    /**
     * Setting and loading sidebar.
     */
    public static void SetSidebar(){
        Pane sideBarPanel = null;
        try {
            logger.info("Loading sidebar...");
            sideBarPanel = FXMLLoader.load(main.MainApp.class.getResource("/views/leftSideBar.fxml"));

        } catch (Exception e) {
            MainApp.logger.error("Source of error: " + e.getMessage());
        }
        logger.info("Setting sidebar...");
        rootPanel.setLeft(sideBarPanel);

    }


    /**
     * Setting and loading statusbar.
      */
    public static void SetStatusBar(){
        HBox statusbar = null;
        try {
            logger.info("Loading statusbar...");
            StatusBarloader = new FXMLLoader();
            StatusBarloader.setLocation(main.MainApp.class.getResource("/views/StatusBar.fxml"));
            statusbar = (HBox) StatusBarloader.load();
            actualStatusbarController = StatusBarloader.getController();
            actualStatusbarController.netw = new SimpleStringProperty("");
            actualStatusbarController.netw.addListener((observable, oldValue, newValue) -> {
                actualStatusbarController.changeStatus(newValue);
            });
        } catch (Exception e) {
            MainApp.logger.error("Source of error: " + e.getMessage());
        }
        logger.info("Showing statusbar...");
        rootPanel.setBottom(statusbar);

    }

    /**
     * Checking network connection.
     * @return true, if the network is available, else return false.
     */
    public static boolean hasNetConnection(){
        boolean status = false;
        try {
            OfflineDao dao = new OfflineDao();
            OfflineService service = new OfflineService(dao);
            final URL url = new URL(dao.getPreference_by_key("connected_type").getConfigValue() + service.getPreference_by_key("remote_db_host").getConfigValue());
            final URLConnection conn = url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.connect();
            logger.info("Connected to the network...");
            status = true;
        } catch (Exception e) {
            logger.info("The network is not available...");
            status = false;
        }

        if(actualMenuBarController != null){
            actualMenuBarController.checkEnabledMenuItems(status);
        }

        if(actualStatusbarController != null && status){
            actualStatusbarController.setStatusBarNetworkStatus("Kapcsólódva");
        }

        if(actualStatusbarController != null && !status){
            actualStatusbarController.setStatusBarNetworkStatus("Nincs kapcsolat");
        }

        return status;
    }

    /**
     * Load applicant dialog and giving by parameter the selected applicant from honewindow of tableview.
     * @param applicant is object of selected applicant to load into the applicant details dialog.
     */
    public static void showApplicantEditDialog(Applicant applicant) {
        try {
            logger.info("Loading " + applicant.getClientName() + "'s properties...");
            FXMLLoader loader = new FXMLLoader(main.MainApp.class.getResource("/views/ApplicantDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            dialogStage = new Stage();
            dialogStage.setTitle(applicant.getClientName() + " adatlapja");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            ApplicantDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setApplicant(applicant);
            logger.info("Showing " + applicant.getClientName() + "'s form...");
            dialogStage.showAndWait();
        } catch (IOException e) {
            MainApp.logger.error("Source of error: " + e.getMessage());
        }
    }

    /**
     * Loading and showing about dialog.
     */
    public static void showAboutDialog(){

        try {
            logger.info("Creading about window...");
            aboutWindow = new FXMLLoader(main.MainApp.class.getResource("/views/AboutWindow.fxml"));
            AnchorPane page = (AnchorPane) aboutWindow.load();
            dialogStage = new Stage();
            dialogStage.setTitle("Névjegy");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initStyle(StageStyle.UNDECORATED);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            logger.info("Loading about window...");
            dialogStage.setScene(scene);

            AboutWindowController controller = aboutWindow.getController();

            controller.aboutCloseBtn.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {
                    logger.info("Closing about window...");
                    dialogStage.close();
                }

            });

            logger.info("Showing about window...");
            dialogStage.showAndWait();

        } catch (IOException e) {
            MainApp.logger.error("Source of error: " + e.getMessage());
        }
    }

    /**
     * Loading and showing preferences dialog.
     */
    public static void showPreferencesDialog(){

        try {
            logger.info("Creading preferences window...");
            preferencesWindow = new FXMLLoader(MainApp.class.getResource("/views/PreferencesWindow.fxml"));
            AnchorPane page = (AnchorPane) preferencesWindow.load();
            dialogStage = new Stage();
            dialogStage.setTitle("Beállítások");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initStyle(StageStyle.UNDECORATED);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            logger.info("Loading preferences window...");
            dialogStage.setScene(scene);

            PreferencesWindowController prefController = preferencesWindow.getController();

            prefController.cancelBtn.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {
                    logger.info("Closing preferences window...");
                    dialogStage.close();
                }

            });

            logger.info("Showing preferences window...");
            dialogStage.showAndWait();

        } catch (IOException e) {
            MainApp.logger.error("Source of error: " + e.getMessage());
        }
    }
}
