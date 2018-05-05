package controllers;

import dao.OfflineDao;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import main.MainApp;
import services.OfflineService;

import java.sql.SQLException;

/**
 * MenubarController is controller class of menubar on Home window.
 * In this controller can communicate the DAO service.
 * @author Ilyés Imre
 * @version 1.0
 * @since 2018-04-08
 */
public class MenuBarController {
    /**
     * Offline DAO.
     */
    private OfflineDao offlineDao;

    /**
     * Service of Offline DAO.
     */
    private OfflineService offlineService;

    /**
     * MenuItem of settings.
     */
    @FXML
    private MenuItem settingsMenu;

    /**
     * MenuItem of exit.
     */
    @FXML
    private MenuItem exitBtn;

    /**
     * MenuItem of synchronizing.
     */
    @FXML
    private MenuItem syncButton;

    /**
     * MenuItem of getting synchronizing from remote database.
     */
    @FXML
    private MenuItem getSyncButton;

    /**
     * MenuItem of setting synchronizing from remote database.
     */
    @FXML
    private MenuItem sendSyncButton;

    /**
     * MenuItem of showing about dialog.
     */
    @FXML
    private MenuItem aboutMenuItem;

    /**
     * Controller of menu bar.
     */
    public MenuBarController() {
        try {
            offlineDao = new OfflineDao();
            offlineService = new OfflineService(offlineDao);
        } catch (SQLException e) {
            MainApp.logger.error("Source of error: " + e.getMessage());
        }
    }

    /**
     * Initialize is first method what runs automatically when the gui components is loaded.
     */
    @FXML
    protected void initialize(){
    }

    /**
     * If the user select exit on home window's menubar, the application is closing.
      * @param event left mouse click, when the user select the exit menuItem in file menu.
     */
    public void exitBtnOnLeftClick(ActionEvent event){
        MainApp.primaryStage.close();
    }

    /**
     * If the user select syncronizing database on home window's menubar, this method synchronizing the local and remoted database.
     * @param event is select event synchronizing menuItem from database menu.
     */
    public void SyncDatabaseLeftMouseClicked(ActionEvent event){

        if(MainApp.hasNetConnection()){
            try {
                offlineService.syncDatabase();
            } catch (SQLException e) {
                MainApp.logger.error("Source of error: " + e.getMessage());
            }

            if(MainApp.homeWindowLoader != null) {
                HomeWindowController controller = MainApp.homeWindowLoader.getController();
                MainApp.logger.info("Refreshing tableview...");
                controller.syncRefresh();
            }
        }
    }

    /**
     * If the user select send datas to remote database from home window's menubar, in this method sending and modifying the local newer modified applicant's data to the remoted database.
     * @param event is select event with left mouse click send modified applicants to remoted database.
     */
    public void SendSyncLeftMouseClicked(ActionEvent event){
        if(HomeWindowController.networkStatus){
            try {
                offlineService.sendApplicantsToMysql();
            } catch (SQLException e) {
                MainApp.logger.error("Source of error: " + e.getMessage());
            }
        }
    }

    /**
     * If the user select the get applicants from the remoted database, this method get the newer modified applicants from remoted database and storing or modifying in the local database.
     * @param event is select event with left mouse click on sync both-side (from local database for remoted database and remoted database for local database).
     * @throws SQLException is there any problem with remote or local SQL database.
     */
    public void GetSyncLeftMouseClicked(ActionEvent event) throws SQLException {
        MainApp.logger.info("Getting applicants from remoted database...");
        offlineService.getAllApplicantsFromMysql();
        MainApp.logger.info("Sending all applicants from remoted database...");
        offlineService.getAllApplicantFromSqlLite();
        if(MainApp.homeWindowLoader != null) {
            HomeWindowController controller = MainApp.homeWindowLoader.getController();
            MainApp.logger.info("Refreshing tableview...");
            controller.syncRefresh();
        }
    }

    /**
     * Showing the about dialog.
     * @param event is select with left mouse click the about dialog from help menu.
     */
    public void openAboutLeftMouseClicked(ActionEvent event){
        MainApp.showAboutDialog();
    }

    /**
     * Open preferences dialog.
     * @param event is select event with left mouse click on preferences menuItem in file menu.
     */
    public void openPreferencesLeftMouseClicked(ActionEvent event){
        MainApp.showPreferencesDialog();
    }

    /**
     * Checking menuItem availability by connection status.
     * @param status is the Remoted database options are allowed in menuItems of main menubar.
     */
    public void checkEnabledMenuItems(boolean status){
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                if(status){
                    syncButton.setDisable(false);
                    getSyncButton.setDisable(false);
                    sendSyncButton.setDisable(false);
                }
                else{
                    syncButton.setDisable(true);
                    getSyncButton.setDisable(true);
                    sendSyncButton.setDisable(true);
                }
            }

        });
    }
}
