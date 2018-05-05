package controllers;

import dao.OfflineDao;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import main.MainApp;
import models.Applicant;
import services.OfflineService;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * CheckNetwork is class of check network connection on new thread.
 * @author Ilyés Imre
 * @version 1.0
 * @since 2018-04-08
 */
class checkNetwork extends Task<Integer>{

    /**
     * Call method is implemented method of Task<Integer>.
     * @return 1, if the task is active, else 0 if the task cancelled.
     * @throws Exception when is there any problem at the thread running.
     */
    @Override
    protected Integer call() throws Exception {
        OfflineDao dao = new OfflineDao();
        OfflineService service = new OfflineService(dao);

        while(!isCancelled()){
            HomeWindowController.networkStatus = MainApp.hasNetConnection();
            Thread.sleep(Integer.valueOf(service.getPreference_by_key("check_connection_period").getConfigValue()));
            MainApp.logger.info("Checking network...");
        }
        if(isCancelled()){
            return 0;
        }
        return 1;
    }
}


/**
 * HoneWindowController is controller class of Home window.
 * In this class can communicate the DAO service with the homeWindow view.
 * @author Ilyés Imre
 * @version 1.0
 * @since 2018-04-08
 */
public class HomeWindowController{
    /**
     * Offline DAO.
     */
    private OfflineDao dao;

    /**
     * Offline Service of Offline DAO.
     */
    private OfflineService service;

    /**
     * Global network status, the network monitoring is modifying this.
     */
    public static Boolean networkStatus;

    /**
     * Dialog of Applicant's details.
     */
    public static Stage dialog;

    /**
     * Selected Applicant in TableView.
     */
    public static Applicant selectedApplicant;

    /**
     * List of applicants from database.
     */
    public List<Applicant> applicantCollection;

    /**
     * TextField of searchBox.
     */
    @FXML
    private TextField searchBox;

    /**
     * Tableview of Applicant's list.
     */
    @FXML
    public TableView<Applicant> applicantTable;

    /**
     * Constructor of HomeWindowController.
     */
    public HomeWindowController() {
        try {
            dao = new OfflineDao();
            service = new OfflineService(dao);
        } catch (SQLException e) {
            MainApp.logger.error("Source of error: " + e.getMessage());
        }
        networkStatus = false;
    }

    /**
     * Initialize is first method what runs when the gui components is loaded.
     */
    @FXML
    protected void initialize(){
        networkStatus = MainApp.hasNetConnection();
        checkNetwork checkNetworkTask = new checkNetwork();
        new Thread(checkNetworkTask).start();

        SyncDatabase syncdatabaseTask = new SyncDatabase();
        new Thread(syncdatabaseTask).start();

        try {
            MainApp.logger.info("Getting all applicants");
            applicantCollection = service.getAllApplicantsFromMysql();
        } catch (SQLException e) {
            MainApp.logger.error("Source of error: " + e.getMessage());
        }
        MainApp.logger.info("Loading applicants to tableview...");
        loadApplicants(applicantCollection);

        applicantTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        MainApp.logger.info("Sign up for the selected applicant of tableview event...");
        applicantTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {

            if (newSelection != null) {
                selectedApplicant = newSelection;
                MainApp.logger.info("Sending " + newSelection.getClientName() + "'s properties into the form...");
                MainApp.showApplicantEditDialog(newSelection);

                MainApp.logger.info("Refreshing tableview...");
                refreshTable();
            }

        });

        StatusBarController controller = MainApp.actualStatusbarController;
    }

    /**
     * Getter of SearchBox.
     * @return Textfield of searchbox.
     */
    public TextField getSearchBox() {
        return searchBox;
    }

    /**
     * Setter of SearchBox.
     * @param searchBox is Textfield of searchbox.
     */
    public void setSearchBox(TextField searchBox) {
        this.searchBox = searchBox;
    }

    /**
     * LoadApplicants is method, what loads the applicants into the home window's tableview.
     * @param applicantList list of applicants.
     */
    private void loadApplicants(List<Applicant> applicantList){
        MainApp.logger.info("Clearing columns of tableview...");
        applicantTable.getColumns().clear();

        MainApp.logger.info("Setting header...");
        TableColumn id = new TableColumn("#");
        TableColumn applicant_status = new TableColumn("Státusz");
        TableColumn applicant_id = new TableColumn("Azonosító");
        TableColumn clientName = new TableColumn("Név");
        TableColumn start_number = new TableColumn("Rajtszám");
        TableColumn clientGender = new TableColumn("Neme");
        TableColumn tshirtSize = new TableColumn("Pólóméret");
        TableColumn clientBirthDate = new TableColumn("Születési év");
        TableColumn clientEmail = new TableColumn("Email");
        TableColumn notes = new TableColumn("Megjegyzés");
        TableColumn product_id = new TableColumn("Termék azonosító");
        TableColumn order_id = new TableColumn("Rendelés szám");
        TableColumn applicant_number = new TableColumn("Nevező szám");
        TableColumn registration_date = new TableColumn("Regisztáció dátuma");
        TableColumn modified = new TableColumn("Módosítva");

        MainApp.logger.info("Setting columns...");
        applicantTable.getColumns().add(id);
        id.setMinWidth(50);
        applicantTable.getColumns().add(applicant_status);
        applicant_status.setMinWidth(80);
        applicantTable.getColumns().add(applicant_id);
        applicant_id.setMinWidth(120);
        applicantTable.getColumns().add(clientName);
        clientName.setMinWidth(160);
        applicantTable.getColumns().add(start_number);
        start_number.setMinWidth(80);
        applicantTable.getColumns().add(clientGender);
        clientGender.setMinWidth(80);
        applicantTable.getColumns().add(tshirtSize);
        tshirtSize.setMinWidth(80);
        applicantTable.getColumns().add(clientBirthDate);
        clientBirthDate.setMinWidth(80);
        applicantTable.getColumns().add(clientEmail);
        clientEmail.setMinWidth(120);
        applicantTable.getColumns().add(notes);
        notes.setMinWidth(150);
        applicantTable.getColumns().add(product_id);
        product_id.setMinWidth(80);
        applicantTable.getColumns().add(order_id);
        order_id.setMinWidth(80);
        applicantTable.getColumns().add(applicant_number);
        applicant_number.setMinWidth(80);
        applicantTable.getColumns().add(registration_date);
        registration_date.setMinWidth(150);
        applicantTable.getColumns().add(modified);
        modified.setMinWidth(150);

        MainApp.logger.info("Clearing rows of tableview...");
        applicantTable.getItems().clear();

        MainApp.logger.info("Loading applicants into tableview...");
        if(applicantList != null) {
            applicantTable.getItems().addAll(applicantList);

            id.setCellValueFactory(new PropertyValueFactory<Applicant, String>("id"));
            //applicant_status.setCellValueFactory(new PropertyValueFactory<Applicant, Boolean>("applicant_status"));
            applicant_status.setCellValueFactory(new PropertyValueFactory<Applicant, String>("applicantStatusString"));
            applicant_id.setCellValueFactory(new PropertyValueFactory<Applicant, String>("applicant_id"));
            clientName.setCellValueFactory(new PropertyValueFactory<Applicant, String>("clientName"));
            start_number.setCellValueFactory(new PropertyValueFactory<Applicant, Integer>("start_number"));
            clientGender.setCellValueFactory(new PropertyValueFactory<Applicant, String>("clientGender"));
            tshirtSize.setCellValueFactory(new PropertyValueFactory<Applicant, String>("tshirtSize"));
            clientBirthDate.setCellValueFactory(new PropertyValueFactory<Applicant, Integer>("clientBirthDate"));
            clientEmail.setCellValueFactory(new PropertyValueFactory<Applicant, String>("clientEmail"));
            registration_date.setCellValueFactory(new PropertyValueFactory<Applicant, Timestamp>("registration_date"));
            modified.setCellValueFactory(new PropertyValueFactory<Applicant, LocalDateTime>("modified"));
            order_id.setCellValueFactory(new PropertyValueFactory<Applicant, String>("order_id"));
            product_id.setCellValueFactory(new PropertyValueFactory<Applicant, Integer>("product_id"));
            applicant_number.setCellValueFactory(new PropertyValueFactory<Applicant, Integer>("applicant_number"));
            notes.setCellValueFactory(new PropertyValueFactory<Applicant, String>("notes"));
        }
    }

    /**
     * refreshTableview is method, what reload applicants when the applicant list or applicants property has changed.
     * @param event is left mouse click on tablewiew.
     */
    public void refreshTableView(MouseEvent event){
    List<Applicant> getApplicantsFromDb = new ArrayList<>();
    try {
        MainApp.logger.info("Getting applicants from the remoted databse...");
        getApplicantsFromDb = service.getAllApplicantsFromMysql();
    } catch (SQLException e) {
        MainApp.logger.error("Source of error: " + e.getMessage());
    }
    MainApp.logger.info("Clearing columns of tableview...");
    applicantTable.getColumns().clear();


    TableColumn id = new TableColumn("#");
    TableColumn applicant_status = new TableColumn("Státusz");
    TableColumn applicant_id = new TableColumn("Azonosító");
    TableColumn clientName = new TableColumn("Név");
    TableColumn start_number = new TableColumn("Rajtszám");
    TableColumn clientGender = new TableColumn("Neme");
    TableColumn tshirtSize = new TableColumn("Pólóméret");
    TableColumn clientBirthDate = new TableColumn("Születési év");
    TableColumn clientEmail = new TableColumn("Email");
    TableColumn notes = new TableColumn("Megjegyzés");
    TableColumn product_id = new TableColumn("Termék azonosító");
    TableColumn order_id = new TableColumn("Rendelés szám");
    TableColumn applicant_number = new TableColumn("Nevező szám");
    TableColumn registration_date = new TableColumn("Regisztáció dátuma");
    TableColumn modified = new TableColumn("Módosítva");


    MainApp.logger.info("Setting columns...");
    applicantTable.getColumns().add(id);
    id.setMinWidth(50);
    applicantTable.getColumns().add(applicant_status);
    applicant_status.setMinWidth(80);
    applicantTable.getColumns().add(applicant_id);
    applicant_id.setMinWidth(120);
    applicantTable.getColumns().add(clientName);
    clientName.setMinWidth(160);
    applicantTable.getColumns().add(start_number);
    start_number.setMinWidth(80);
    applicantTable.getColumns().add(clientGender);
    clientGender.setMinWidth(80);
    applicantTable.getColumns().add(tshirtSize);
    tshirtSize.setMinWidth(80);
    applicantTable.getColumns().add(clientBirthDate);
    clientBirthDate.setMinWidth(80);
    applicantTable.getColumns().add(clientEmail);
    clientEmail.setMinWidth(120);
    applicantTable.getColumns().add(notes);
    notes.setMinWidth(150);
    applicantTable.getColumns().add(product_id);
    product_id.setMinWidth(80);
    applicantTable.getColumns().add(order_id);
    order_id.setMinWidth(80);
    applicantTable.getColumns().add(applicant_number);
    applicant_number.setMinWidth(80);
    applicantTable.getColumns().add(registration_date);
    registration_date.setMinWidth(150);
    applicantTable.getColumns().add(modified);
    modified.setMinWidth(150);

    MainApp.logger.info("Clearing rows of tableview...");
    applicantTable.getItems().clear();

    MainApp.logger.info("Loading applicants into tableview...");
    if(getApplicantsFromDb != null) {
        applicantTable.getItems().addAll(getApplicantsFromDb);

        id.setCellValueFactory(new PropertyValueFactory<Applicant, String>("id"));
        applicant_status.setCellValueFactory(new PropertyValueFactory<Applicant, String>("applicantStatusString"));
        applicant_id.setCellValueFactory(new PropertyValueFactory<Applicant, String>("applicant_id"));
        clientName.setCellValueFactory(new PropertyValueFactory<Applicant, String>("clientName"));
        start_number.setCellValueFactory(new PropertyValueFactory<Applicant, Integer>("start_number"));
        clientGender.setCellValueFactory(new PropertyValueFactory<Applicant, String>("clientGender"));
        tshirtSize.setCellValueFactory(new PropertyValueFactory<Applicant, String>("tshirtSize"));
        clientBirthDate.setCellValueFactory(new PropertyValueFactory<Applicant, Integer>("clientBirthDate"));
        clientEmail.setCellValueFactory(new PropertyValueFactory<Applicant, String>("clientEmail"));
        registration_date.setCellValueFactory(new PropertyValueFactory<Applicant, Timestamp>("registration_date"));
        modified.setCellValueFactory(new PropertyValueFactory<Applicant, LocalDateTime>("modified"));
        order_id.setCellValueFactory(new PropertyValueFactory<Applicant, String>("order_id"));
        product_id.setCellValueFactory(new PropertyValueFactory<Applicant, Integer>("product_id"));
        applicant_number.setCellValueFactory(new PropertyValueFactory<Applicant, Integer>("applicant_number"));
        notes.setCellValueFactory(new PropertyValueFactory<Applicant, String>("notes"));
    }
}

    /**
     * FindApplicantsByKeyword is method, what search applicants by keyword of searchfield.
     * @param keyevent is typing keyword in the search textfield.
     */
    public void findApplicantsByKeywordsKeydown(KeyEvent keyevent){
        List<Applicant> applicantList = null;
        try {
            applicantList = new ArrayList<>();
            MainApp.logger.info("Searching applicants by keyword: " + searchBox.getText());
            applicantList = service.findApplicantsByKeyword(searchBox.getText());
        } catch (SQLException e) {
            MainApp.logger.error("Source of error: " + e.getMessage());
        }
        MainApp.logger.info("Loading applicant results into tableview...");
        loadApplicants(applicantList);

    }

    /**
     * refreshTable is callable manually to refresh tableview in home window.
     */
    public void refreshTable(){
        applicantTable.refresh();
    }

    /**
     * syncRefresh is method, what can load applicant's data from the remoted database.
     */
    public void syncRefresh(){
        try {
            MainApp.logger.info("Getting all applicants from remoted database...");
            loadApplicants(service.getAllApplicantsFromMysql());
        } catch (SQLException e) {
            MainApp.logger.error("Source of error: " + e.getMessage());
        }
    }

}
