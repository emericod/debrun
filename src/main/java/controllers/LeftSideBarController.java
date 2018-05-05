package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import main.MainApp;

/**
 * LeftSidebarController is controller class of Sidebar on the Home window.
 * @author Ilyés Imre
 * @version 1.0
 * @since 2018-04-08
 */

public class LeftSideBarController {

    /**
     * Label of logged in applicant's name.
     */
    @FXML
    private Label activeUserLabel;

    /**
     * This method is running automatically when the sidebar is loaded.
     * In this is writing the name of logged-in user.
     */
    @FXML
    protected void initialize(){
        MainApp.logger.info("Showing actual user on sidebar: " + MainApp.actualUser.getUsername());
        activeUserLabel.setText(MainApp.actualUser.getFirstName() + " " + MainApp.actualUser.getLastName());
    }

    /**
     * In this method is running when the user is clicked to the logout button.
     * This is closing the home view and open the login view.
     * @param event Basically this is the mouse left click event.
     */
    public void sidebarLogoutButton_LeftClicked(ActionEvent event){
        MainApp.logger.info("Showing login window...");
        MainApp.rootPanel.setLeft(null);
        MainApp.rootPanel.setTop(null);
        MainApp.rootPanel.setBottom(null);
        main.MainApp.SetActiveWindow(MainApp.loginWindow);
    }


}
