package dao;

import controllers.HomeWindowController;
import controllers.LoginWindowController;
import interfaces.OfflineDaoInterface;
import main.MainApp;
import models.Applicant;
import models.Client;
import models.Setting;
import services.MysqlService;
import java.io.File;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * OfflineDao is class of SqlLite database's DAO
 * @author Ilyés Imre
 * @version 1.0
 * @since 2018-04-08
 */
public class OfflineDao implements OfflineDaoInterface<Applicant> {
    private Connection connection = null;
    private Statement statement;
    private MysqlDao mysqldao;
    private MysqlService mysqlservice;

    /**
     * Constructor of OfflineDao class
     * @throws SQLException if there is any problem with remoted SQL database
     */
    public OfflineDao() throws SQLException {
        MainApp.logger.info("Connecting in processing...");
        connection = DriverManager.getConnection("jdbc:sqlite:C:/debrunwayrun/src/main/resources/localDatabase/localdb.sqllite");
        statement = connection.createStatement();
        statement.setQueryTimeout(30);
    }

    /**
     * This method is dooing the user authentication by usernam and password in the local SqlLite database
     * @param username is user name
     * @param password is password of user
     * @return true, if the authenticaion is succeed, else returns false
     * @throws SQLException if there is any problem with remoted SQL database
     */
    @Override
    public boolean userLoggedIn(String username, String password) throws SQLException {
        MainApp.logger.info("Authenticating user...");
        String getUserSqlString = "SELECT * FROM clients WHERE (username = '" + username + "' and password = '" + LoginWindowController.encryptPassword(password).toUpperCase() + "') and status = '1' ;";
        ResultSet userResults = statement.executeQuery(getUserSqlString);

        List<Client> clientsList = new ArrayList<>();

        while(userResults.next()){
            Client app = new Client();
            app.setId(userResults.getInt("id"));
            app.setUsername(userResults.getString("username"));
            app.setFirstName(userResults.getString("firstName"));
            app.setLastName(userResults.getString("lastName"));
            app.setPassword(userResults.getString("password"));
            app.setStatus(userResults.getInt("status"));
            app.setEnabledStatus(userResults.getBoolean("enabledStatus"));
            clientsList.add(app);
        }
        if(clientsList.size() == 1){
            statement.close();
            return true;
        }
        else{
            statement.close();
            return false;
        }
    }


    /**
     * This method adds the logged in user to the local SqlLite database
     * @param user is object of user
     * @throws SQLException if there is any problem with remoted SQL database
     */
    @Override
    public void addUserToSqlLite(Client user) throws SQLException {
        String getUserSqlString = "SELECT * FROM clients WHERE id = '" + user.getId() + "';";
        ResultSet userResults = statement.executeQuery(getUserSqlString);
        List<Client> usersList = new ArrayList<>();

        while(userResults.next()){
            Client app = new Client();
            app.setId(userResults.getInt("id"));
            app.setUsername(userResults.getString("username"));
            app.setFirstName(userResults.getString("firstName"));
            app.setLastName(userResults.getString("lastName"));
            app.setPassword(userResults.getString("password"));
            app.setStatus(userResults.getInt("status"));
            app.setEnabledStatus(userResults.getBoolean("enabledStatus"));
            usersList.add(app);
        }

        if(usersList.size() == 0){
            MainApp.logger.info("User is not exists in local database, adding user: " + user.getUsername());
            String insertUserSqlString = "INSERT INTO clients (id, username, firstName, lastName, password, status, enabledStatus) VALUES('" + user.getId() + "', '" + user.getUsername() + "', '" + user.getFirstName() + "', '" + user.getLastName() + "', '" + user.getPassword() + "', '" + user.getStatus() + "', '" + user.getEnabledStatus() + "');";
            statement.executeUpdate(insertUserSqlString);
        }
        else{
            MainApp.logger.info("The user is exists in local database, modifiing user: " + user.getUsername());
            String updateUserSqlString = "UPDATE clients SET id = '" + user.getId() + "', username = '" + user.getUsername() + "', firstName = '" + user.getFirstName() + "', lastName = '" + user.getLastName() + "', password = '" + user.getPassword() + "', status = '" + user.getStatus() + "', enabledStatus = '" + user.getEnabledStatus() + "' WHERE id = '" + user.getId() + "';";
            statement.executeUpdate(updateUserSqlString);
        }
        statement.close();
    }

    /**
     * This method search user by username in the local SqlLite database
     * @param username is user name
     * @return Client, result of Applicant what has username the parameter
     * @throws SQLException if there is any problem with remoted SQL database
     */
    @Override
    public Client getUserByUsername(String username) throws SQLException {
        MainApp.logger.info("Getting user details from local database...");
        String getClientSqlString = "SELECT * FROM clients WHERE username = '" + username + "' limit 1;";
        ResultSet userResults = statement.executeQuery(getClientSqlString);
        Client client = null;
        while(userResults.next()){
            client = new Client();
            client.setId(userResults.getInt("id"));
            client.setUsername(userResults.getString("username"));
            client.setFirstName(userResults.getString("firstName"));
            client.setLastName(userResults.getString("lastName"));
            client.setPassword(userResults.getString("password"));
            client.setStatus(userResults.getInt("status"));
            client.setEnabledStatus(userResults.getBoolean("enabledStatus"));
        }
        statement.close();
        return client;
    }

    /**
     * Create SqlLite database file, if the database not exists
     * @throws SQLException if there is any problem with remoted SQL database
     */
    @Override
    public void sqlLiteConnection() throws SQLException {
        File applicantDatabaseFile = new File("C:/debrunwayrun/src/main/resources/localDatabase/localdb.sqllite");
        connection = DriverManager.getConnection("jdbc:sqlite:C:/debrunwayrun/src/main/resources/localDatabase/localdb.sqllite");
        statement = connection.createStatement();
        statement.setQueryTimeout(30);
        if(!applicantDatabaseFile.exists()) {
            MainApp.logger.info("Creating local database file...");
            statement.executeUpdate("create table debrun_applicants (id int PRIMARY KEY, start_number int NULL, applicant_id VARCHAR(255), order_id int, product_id int, applicant_number int, clientName VARCHAR(255), clientGender VARCHAR(255), tshirtSize VARCHAR(255), clientBirthDate int, clientEmail VARCHAR(255), qrcode Text, registration_date TIMESTAMP NULL, modified_date DATE NULL, modified_time TIME NULL,  notes Text, completed_status TINYINT, applicant_status TINYINT, trash_status TINYINT, loggedInStatus TINYINT);");
        }

        statement.close();

    }

    /**
     * Getting all applicants from the local SqlLite database
     * @return Results of applicants
     * @throws SQLException if there is any problem with remoted SQL database
     */
    @Override
    public List<Applicant> getAllApplicantFromSqlLite() throws SQLException {
        //sqlLiteConnection();
        MainApp.logger.info("Getting applicants from local database...");
        List<Applicant> applicantList = new ArrayList<>();
        String getAllApplicantSqlString = "SELECT * FROM debrun_applicants;";
        ResultSet rs = statement.executeQuery(getAllApplicantSqlString);

        while (rs.next()) {
            Applicant actualApplicant = new Applicant();
            actualApplicant.setId(rs.getInt("id"));
            actualApplicant.setStart_number(rs.getInt("start_number"));
            actualApplicant.setApplicant_id(rs.getString("applicant_id"));
            actualApplicant.setOrder_id(rs.getInt("order_id"));
            actualApplicant.setProduct_id(rs.getInt("product_id"));
            actualApplicant.setApplicant_number(rs.getInt("applicant_number"));
            actualApplicant.setClientName(rs.getString("clientName"));
            actualApplicant.setClientGender(rs.getString("clientGender"));
            actualApplicant.setTshirtSize(rs.getString("tshirtSize"));
            actualApplicant.setClientBirthDate(rs.getInt("clientBirthDate"));
            actualApplicant.setClientEmail(rs.getString("clientEmail"));
            actualApplicant.setQrcode(rs.getString("qrcode"));

            if(rs.getString("registration_date").compareTo("null") != 0) {
                actualApplicant.setRegistration_date(rs.getTimestamp("registration_date"));
            }
            else{
                actualApplicant.setRegistration_date(null);
            }

            if(rs.getString("modified_date").compareTo("null") != 0){
                String readedDate = rs.getString("modified_date");
                LocalDate date = LocalDate.of(Integer.valueOf(readedDate.substring(0,4)), Integer.valueOf(readedDate.substring(6,7)),Integer.valueOf(readedDate.substring(8,10)));
                actualApplicant.setModified_date(date);
            }
            else{
                actualApplicant.setModified_date(null);
            }

            if(rs.getString("modified_time").compareTo("null") != 0){
                String readedTime = rs.getString("modified_time");


                LocalTime modified_time = null;

                if(readedTime.length() == 5){
                    modified_time = LocalTime.of(Integer.valueOf(readedTime.substring(0,2)), Integer.valueOf(readedTime.substring(3,5)), 00);
                }
                else if(readedTime.length() >= 6){
                    modified_time = LocalTime.of(Integer.valueOf(readedTime.substring(0,2)), Integer.valueOf(readedTime.substring(3,5)), Integer.valueOf(readedTime.substring(6,8)));

                }
                actualApplicant.setModified_time(modified_time);
            }
            else{
                actualApplicant.setModified_time(null);
            }

            if(actualApplicant.getModified_date() != null && actualApplicant.getModified_time() != null){
                actualApplicant.setModified(actualApplicant.getModified_date(), actualApplicant.getModified_time());
            }
            else{
                actualApplicant.setModifiedToNull();
            }

            actualApplicant.setNotes(rs.getString("notes"));
            actualApplicant.setCompleted_status(rs.getInt("completed_status"));
            actualApplicant.setApplicant_status(rs.getInt("applicant_status"));
            actualApplicant.setTrash_status(rs.getInt("trash_status"));
            actualApplicant.setLoggedInStatus(rs.getInt("loggedInStatus"));
            applicantList.add(actualApplicant);
        }


        statement.close();
        return applicantList;
    }

    /**
     * Searching newer modified applicants from local SqlLite database
     * @return Results of applicants
     * @throws SQLException if there is any problem with remoted SQL database
     */
    @Override
    public List<Applicant> getModifiedApplicantsFromSqlLite() throws SQLException {
        //sqlLiteConnection();
        List<Applicant> applicantList = new ArrayList<>();
        MainApp.logger.info("Getting modified applicants from local database...");
        String getModifiedApplicantSqlString = "SELECT * FROM debrun_applicants WHERE modified_date != 'null' and modified_time != 'null';";
        ResultSet rs = statement.executeQuery(getModifiedApplicantSqlString);

        while (rs.next()) {
            Applicant actualApplicant = new Applicant();
            actualApplicant.setId(rs.getInt("id"));
            actualApplicant.setStart_number(rs.getInt("start_number"));
            actualApplicant.setApplicant_id(rs.getString("applicant_id"));
            actualApplicant.setOrder_id(rs.getInt("order_id"));
            actualApplicant.setProduct_id(rs.getInt("product_id"));
            actualApplicant.setApplicant_number(rs.getInt("applicant_number"));
            actualApplicant.setClientName(rs.getString("clientName"));
            actualApplicant.setClientGender(rs.getString("clientGender"));
            actualApplicant.setTshirtSize(rs.getString("tshirtSize"));
            actualApplicant.setClientBirthDate(rs.getInt("clientBirthDate"));
            actualApplicant.setClientEmail(rs.getString("clientEmail"));
            actualApplicant.setQrcode(rs.getString("qrcode"));

            if(rs.getString("registration_date").compareTo("null") != 0) {
                actualApplicant.setRegistration_date(rs.getTimestamp("registration_date"));
            }
            else{
                actualApplicant.setRegistration_date(null);
            }

            if(rs.getString("modified_date").compareTo("null") != 0){
                String readedDate = rs.getString("modified_date");
                LocalDate date = LocalDate.of(Integer.valueOf(readedDate.substring(0,4)), Integer.valueOf(readedDate.substring(5,7)),Integer.valueOf(readedDate.substring(8,10)));
                actualApplicant.setModified_date(date);
            }
            else{
                actualApplicant.setModified_date(null);
            }

            if(rs.getString("modified_time").compareTo("null") != 0){
                String readedTime = rs.getString("modified_time");


                LocalTime modified_time = null;

                if(readedTime.length() == 5){
                    modified_time = LocalTime.of(Integer.valueOf(readedTime.substring(0,2)), Integer.valueOf(readedTime.substring(3,5)), 00);
                }
                else if(readedTime.length() >= 6){
                    modified_time = LocalTime.of(Integer.valueOf(readedTime.substring(0,2)), Integer.valueOf(readedTime.substring(3,5)), Integer.valueOf(readedTime.substring(6,8)));

                }
                actualApplicant.setModified_time(modified_time);
            }
            else{
                actualApplicant.setModified_time(null);
            }

            if(actualApplicant.getModified_date() != null && actualApplicant.getModified_time() != null){
                actualApplicant.setModified(actualApplicant.getModified_date(), actualApplicant.getModified_time());
            }
            else{
                actualApplicant.setModifiedToNull();
            }

            actualApplicant.setNotes(rs.getString("notes"));
            actualApplicant.setCompleted_status(rs.getInt("completed_status"));
            actualApplicant.setApplicant_status(rs.getInt("applicant_status"));
            actualApplicant.setTrash_status(rs.getInt("trash_status"));
            actualApplicant.setLoggedInStatus(rs.getInt("loggedInStatus"));
            applicantList.add(actualApplicant);
        }


        statement.close();
        return applicantList;
    }

    /**
     * Finding applicants by parameter / keyword in local SqlLite database
     * @param keyword is keyword what is included in ID,, applicant_id, client name, client gender or trash status
     * @return Results of applicants
     * @throws SQLException if there is any problem with remoted SQL database
     */
    @Override
    public List<Applicant> findApplicantsByKeyword(String keyword) throws SQLException {
        //sqlLiteConnection();
        List<Applicant> applicantList = new ArrayList<>();
        MainApp.logger.info("Searhing applicants by keyword is processing...");
        String getAllApplicantSqlString = "SELECT * FROM debrun_applicants WHERE (id like '%" + keyword + "%' OR applicant_id like '%" + keyword + "%' OR clientName like '%" + keyword + "%' OR clientGender like '%" + keyword + "%') and trash_status != '1';";
        ResultSet rs = statement.executeQuery(getAllApplicantSqlString);

        while(rs.next()){
            Applicant actualApplicant = new Applicant();
            actualApplicant.setId(rs.getInt("id"));
            actualApplicant.setStart_number(rs.getInt("start_number"));
            actualApplicant.setApplicant_id(rs.getString("applicant_id"));
            actualApplicant.setOrder_id(rs.getInt("order_id"));
            actualApplicant.setProduct_id(rs.getInt("product_id"));
            actualApplicant.setApplicant_number(rs.getInt("applicant_number"));
            actualApplicant.setClientName(rs.getString("clientName"));
            actualApplicant.setClientGender(rs.getString("clientGender"));
            actualApplicant.setTshirtSize(rs.getString("tshirtSize"));
            actualApplicant.setClientBirthDate(rs.getInt("clientBirthDate"));
            actualApplicant.setClientEmail(rs.getString("clientEmail"));
            actualApplicant.setQrcode(rs.getString("qrcode"));

            if(rs.getString("registration_date").compareTo("null") != 0) {
                actualApplicant.setRegistration_date(rs.getTimestamp("registration_date"));
            }
            else{
                actualApplicant.setRegistration_date(null);
            }


            if(rs.getString("modified_date").compareTo("null") != 0){
                String readedDate = rs.getString("modified_date");
                LocalDate date = LocalDate.of(Integer.valueOf(readedDate.substring(0,4)), Integer.valueOf(readedDate.substring(5,7)),Integer.valueOf(readedDate.substring(8,10)));
                actualApplicant.setModified_date(date);
            }
            else{
                actualApplicant.setModified_date(null);
            }

            if(rs.getString("modified_time").compareTo("null") != 0){
                String readedTime = rs.getString("modified_time");


                LocalTime modified_time = null;

                if(readedTime.length() == 5){
                    modified_time = LocalTime.of(Integer.valueOf(readedTime.substring(0,2)), Integer.valueOf(readedTime.substring(3,5)), 00);
                }
                else if(readedTime.length() >= 6){
                    modified_time = LocalTime.of(Integer.valueOf(readedTime.substring(0,2)), Integer.valueOf(readedTime.substring(3,5)), Integer.valueOf(readedTime.substring(6,8)));

                }
                actualApplicant.setModified_time(modified_time);
            }
            else{
                actualApplicant.setModified_time(null);
            }

            if(actualApplicant.getModified_date() != null && actualApplicant.getModified_time() != null){
                actualApplicant.setModified(actualApplicant.getModified_date(), actualApplicant.getModified_time());
            }
            else{
                actualApplicant.setModifiedToNull();
            }
            actualApplicant.setNotes(rs.getString("notes"));
            actualApplicant.setCompleted_status(rs.getInt("completed_status"));
            actualApplicant.setApplicant_status(rs.getInt("applicant_status"));
            actualApplicant.setTrash_status(rs.getInt("trash_status"));
            actualApplicant.setLoggedInStatus(rs.getInt("loggedInStatus"));

            applicantList.add(actualApplicant);
        }

        statement.close();
        return applicantList;
    }

    /**
     * Updating applicant status by parameter
     * This method updating applicant status what applicant_id is the parameter
     * @param applicant_id is ID of applicant
     * @param status is status of applicant (0, if false or 1, if true)
     * @throws SQLException if there is any problem with remoted SQL database
     */
    public void updateApplicant(String applicant_id, int status) throws SQLException {
        LocalDate modified_date = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), LocalDate.now().getDayOfMonth());
        LocalTime modified_time = LocalTime.of(LocalTime.now().getHour(), LocalTime.now().getMinute(), LocalTime.now().getSecond());
        MainApp.logger.info("Modifiing applicant in local database...");
        String changeStatusApplicantSqlString = "UPDATE debrun_applicants SET applicant_status = '" + status + "', modified_date = '" + modified_date + "', modified_time = '" + modified_time + "' WHERE applicant_id = '" + applicant_id + "';";
        statement.executeUpdate(changeStatusApplicantSqlString);
        if(HomeWindowController.networkStatus) {
            if(HomeWindowController.networkStatus && mysqlservice == null){
                mysqldao = new MysqlDao();
                mysqlservice = new MysqlService(mysqldao);
            }
            mysqlservice.updateApplicant(applicant_id, status, LocalDateTime.of(modified_date, modified_time));

        }
        statement.close();
    }

    /**
     * Getting all applicants from the remoted database if the network connection is available
     * @return results of applicant list
     * @throws SQLException if there is any problem with remoted SQL database
     */
    @Override
    public List<Applicant> getAllApplicantsFromMysql() throws SQLException {
        List<Applicant> applicants;
        applicants = new ArrayList<>();
        HomeWindowController.networkStatus = MainApp.hasNetConnection();

        if(HomeWindowController.networkStatus) {
            if(mysqlservice == null) {
                mysqldao = new MysqlDao();
                mysqlservice = new MysqlService(mysqldao);
            }


            for (Applicant applicant : mysqlservice.getAllApplicantsFromMysql()) {
                MainApp.logger.info(applicant.getClientName() + " (id: " + applicant.getId()+ ")" + " from remoted database has imported...");
              String checkApplicantSqlLite = "SELECT * FROM debrun_applicants WHERE applicant_id = '" + applicant.getApplicant_id() + "' LIMIT 1;";
              ResultSet rs = statement.executeQuery(checkApplicantSqlLite);

                List<Applicant> resultApplicant = new ArrayList<>();
                while(rs.next()) {
                    Applicant actualApplicant = new Applicant();
                    actualApplicant.setId(rs.getInt("id"));
                    actualApplicant.setStart_number(rs.getInt("start_number"));
                    actualApplicant.setApplicant_id(rs.getString("applicant_id"));
                    actualApplicant.setOrder_id(rs.getInt("order_id"));
                    actualApplicant.setProduct_id(rs.getInt("product_id"));
                    actualApplicant.setApplicant_number(rs.getInt("applicant_number"));
                    actualApplicant.setClientName(rs.getString("clientName"));
                    actualApplicant.setClientGender(rs.getString("clientGender"));
                    actualApplicant.setTshirtSize(rs.getString("tshirtSize"));
                    actualApplicant.setClientBirthDate(rs.getInt("clientBirthDate"));
                    actualApplicant.setClientEmail(rs.getString("clientEmail"));
                    actualApplicant.setQrcode(rs.getString("qrcode"));

                    if(rs.getString("registration_date").compareTo("null") != 0) {
                        actualApplicant.setRegistration_date(rs.getTimestamp("registration_date"));
                    }
                    else{
                        actualApplicant.setRegistration_date(null);
                    }


                    if(rs.getString("modified_date").compareTo("null") != 0){
                        String readedDate = rs.getString("modified_date");
                        LocalDate date = LocalDate.of(Integer.valueOf(readedDate.substring(0,4)), Integer.valueOf(readedDate.substring(5,7)),Integer.valueOf(readedDate.substring(8,10)));
                        actualApplicant.setModified_date(date);
                    }
                    else{
                        actualApplicant.setModified_date(null);
                    }

                    if(rs.getString("modified_time").compareTo("null") != 0){
                        String readedTime = rs.getString("modified_time");


                        LocalTime modified_time = null;

                        if(readedTime.length() == 5){
                            modified_time = LocalTime.of(Integer.valueOf(readedTime.substring(0,2)), Integer.valueOf(readedTime.substring(3,5)), 00);
                        }
                        else if(readedTime.length() >= 6){
                            modified_time = LocalTime.of(Integer.valueOf(readedTime.substring(0,2)), Integer.valueOf(readedTime.substring(3,5)), Integer.valueOf(readedTime.substring(6,8)));

                        }
                        actualApplicant.setModified_time(modified_time);
                    }
                    else{
                        actualApplicant.setModified_time(null);
                    }

                    if(actualApplicant.getModified_date() != null && actualApplicant.getModified_time() != null){
                        actualApplicant.setModified(actualApplicant.getModified_date(), actualApplicant.getModified_time());
                    }
                    else{
                        actualApplicant.setModifiedToNull();
                    }
                    actualApplicant.setNotes(rs.getString("notes"));
                    actualApplicant.setCompleted_status(rs.getInt("completed_status"));
                    actualApplicant.setApplicant_status(rs.getInt("applicant_status"));
                    actualApplicant.setTrash_status(rs.getInt("trash_status"));
                    actualApplicant.setLoggedInStatus(rs.getInt("loggedInStatus"));

                    resultApplicant.add(actualApplicant);
                }

                if(resultApplicant.size() == 0){
                    String InsertApplicants = "INSERT INTO debrun_applicants (id, start_number, applicant_id, order_id, product_id, applicant_number, clientName, clientGender, tshirtSize, clientBirthDate, clientEmail, qrcode, registration_date, modified_date, modified_time, notes, completed_status, applicant_status, trash_status, loggedInStatus) VALUES('" + applicant.getId() + "', '" + applicant.getStart_number() + "', '" + applicant.getApplicant_id() + "', '" + applicant.getOrder_id() + "', '" + applicant.getProduct_id() + "', '" + applicant.getApplicant_number() + "', '" + applicant.getClientName() + "', '" + applicant.getClientGender() + "', '" + applicant.getTshirtSize() + "', '" + applicant.getClientBirthDate() + "', '" + applicant.getClientEmail() + "', '" + applicant.getQrcode() + "', '" + applicant.getRegistration_date() + "', '" + applicant.getModified_date() + "', '" + applicant.getModified_time() + "', '" + applicant.getNotes() + "', " + applicant.getCompleted_status() + ", " + applicant.getApplicant_status() + ", " + applicant.getTrash_status() + ", " + applicant.getLoggedInStatus() + ");";
                    statement.executeUpdate(InsertApplicants);
                    MainApp.logger.info("Added applicant: " + applicant.getId() + " " + applicant.getClientName());
                }
                else{
                    for (Applicant appSqlLite : resultApplicant) {
                        if(appSqlLite.getModified_date() != null && appSqlLite.getModified_time() != null){
                            appSqlLite.setModified(appSqlLite.getModified_date(),appSqlLite.getModified_time());
                        }
                        else{
                            appSqlLite.setModifiedToNull();
                        }

                        if(applicant.getModified_date() != null && applicant.getModified_time() != null){
                            applicant.setModified(applicant.getModified_date(),applicant.getModified_time());
                        }
                        else{
                            applicant.setModifiedToNull();
                        }

                        if(appSqlLite.getModified() == null && applicant.getModified() != null){

                            String changeStatusApplicantSqlString = "UPDATE debrun_applicants SET applicant_status = '" + applicant.getApplicant_status() + "', modified_date = '" + applicant.getModified_date() + "', modified_time = '" + applicant.getModified_time() + "' WHERE applicant_id = '" + applicant.getApplicant_id() + "';";
                            statement.executeUpdate(changeStatusApplicantSqlString);
                            MainApp.logger.info("Modified applicant: " + applicant.getId() + " " + applicant.getClientName());
                        }
                        else if(appSqlLite.getModified() != null && applicant.getModified() != null && applicant.getModified().isAfter(appSqlLite.getModified())){
                            String changeStatusApplicantSqlString = "UPDATE debrun_applicants SET applicant_status = '" + applicant.getApplicant_status() + "', modified_date = '" + applicant.getModified_date() + "', modified_time = '" + applicant.getModified_time() + "' WHERE applicant_id = '" + applicant.getApplicant_id() + "';";
                            statement.executeUpdate(changeStatusApplicantSqlString);
                            MainApp.logger.info("Modified applicant: " + applicant.getId() + " " + applicant.getClientName());

                        }
                    }
                }

        }

        statement.close();
        }

        applicants.addAll(getAllApplicantFromSqlLite());
        return applicants;

    }


    /**
     * Counting applicants in local SqlLite database
     * @return number of applicants
     * @throws SQLException if there is any problem with remoted SQL database
     */
    @Override
    public int countSqlLiteApplicants() throws SQLException {
        String countApplicantSqlString = "SELECT COUNT(*) AS counted_applicants FROM main.debrun_applicants;";
        ResultSet rs = statement.executeQuery(countApplicantSqlString);
        int countedApplicants = rs.getInt("counted_applicants");
        statement.close();
        return countedApplicants;
    }

    /**
     * Counting applicants in remoted database
     * @return number of applicants
     * @throws SQLException if there is any problem with remoted SQL database
     */
    @Override
    public int countMysqlApplicants() throws SQLException {
        HomeWindowController.networkStatus = MainApp.hasNetConnection();
        if (HomeWindowController.networkStatus) {
            if(mysqlservice == null){
                mysqldao = new MysqlDao();
                mysqlservice = new MysqlService(mysqldao);
            }

            return mysqlservice.countMysqlApplicants();
        }
        else{
            return 0;
        }
    }

    /**
     * This method sending applicants to remoted database
     * @return true, if the operation has succeed, else return false
     * @throws SQLException if there is any problem with remoted SQL database
     */
    @Override
    public boolean sendApplicantsToMysql() throws SQLException {
        if(HomeWindowController.networkStatus == null){
            HomeWindowController.networkStatus = MainApp.hasNetConnection();
        }
        if(HomeWindowController.networkStatus) {
            mysqldao = new MysqlDao();
            mysqlservice = new MysqlService(mysqldao);
            if(mysqlservice == null){
            }
            MainApp.logger.info("Applicant sending for remoted database...");
            return mysqlservice.sendApplicantsToMysql();
        }
        else{
            return false;
        }
    }

    /**
     * This method calls mysqlDao syncDatabase method
     * @throws SQLException if there is any problem with remoted SQL database
     */
    @Override
    public void syncDatabase() throws SQLException {
        if(HomeWindowController.networkStatus && mysqlservice == null){
            mysqldao = new MysqlDao();
            mysqlservice = new MysqlService(mysqldao);
        }
        MainApp.logger.info("Synchronizing applicants between local and remoted database...");
        mysqlservice.syncDatabase();
    }

    /**
     * This method storing preferences in the local SqlLite database
     * if the setting key is not available in the database, the method create or if it exists, the method update this
     * @param settings is a list of settings
     * @throws SQLException if there is any problem with remoted SQL database
     */
    @Override
    public void savePreferences(List<Setting> settings) throws SQLException {

        for (Setting setting : settings) {
            String getConfigSqlString = "SELECT * FROM preferences WHERE configKey = '" + setting.getConfigKey() + "' LIMIT 1;";
            ResultSet result = statement.executeQuery(getConfigSqlString);
            List<Setting> settingResultList = new ArrayList<>();
            while(result.next()){
                Setting sett = new Setting();
                sett.setConfigKey(result.getString("configKey"));
                sett.setConfigValue(result.getString("configValue"));
                settingResultList.add(sett);
            }

            if(settingResultList.size() == 0){
                String saveConfigSqlString = "INSERT INTO preferences (configKey, configValue) VALUES('" + setting.getConfigKey() + "','" + setting.getConfigValue() + "');";
                statement.executeUpdate(saveConfigSqlString);

            }
            else{
                String updateConfigSqlString = "UPDATE preferences SET configValue = '" + setting.getConfigValue() +"' WHERE configKey = '" + setting.getConfigKey() + "';";
                statement.executeUpdate(updateConfigSqlString);
            }

        }
        statement.close();
    }

    /**
     * This method getting the setting record by configKey parameter from the local SqlLite database
     * @param configKey is the key of config
     * @return config value
     * @throws SQLException if there is any problem with remoted SQL database
     */
    @Override
    public Setting getPreference_by_key(String configKey) throws SQLException {
        //String createTable = "CREATE TABLE preferences (id INTEGER PRIMARY KEY AUTOINCREMENT, configKey VARCHAR(256), configValue VARCHAR(256)); CREATE UNIQUE INDEX preferences_id_uindex ON preferences (id);";
        //statement.executeQuery(createTable);
        String getSettingSqlString = "SELECT * FROM preferences WHERE configKey = '" + configKey + "' LIMIT 1;";
        ResultSet result = statement.executeQuery(getSettingSqlString);

        Setting setting = new Setting();
        while (result.next()) {
            setting.setConfigKey(result.getString("configKey"));
            setting.setConfigValue(result.getString("configValue"));
        }

        statement.close();
        return setting;
    }


}
