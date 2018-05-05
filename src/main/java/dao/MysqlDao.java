package dao;

import controllers.HomeWindowController;
import controllers.LoginWindowController;
import interfaces.MysqlDaoInterface;
import main.MainApp;
import models.Applicant;
import models.Client;
import models.EntityManagerFactoryHelper;
import services.OfflineService;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * MysqlDao is class of Mysql database's DAO.
 * @author Ilyés Imre
 * @version 1.0
 * @since 2018-04-08
 */
public class MysqlDao implements MysqlDaoInterface<Applicant> {

    /**
     * Default EntityManagerFactory.
     */
    private EntityManagerFactory emfactory;

    /**
     * EntityManager.
     */
    private EntityManager eManager;

    /**
     * Offline DAO.
     */
    OfflineDao offlineDao;

    /**
     * Offline service of Offline DAO.
     */
    OfflineService offlineService;

    /**
     * EntityManagerFactoryHelper for remote connection.
     */
    private EntityManagerFactoryHelper mysqlconnection;

    /**
     * Constructor of MysqlDao.
     * @throws SQLException if there is any problem with the remoted database.
     */
    public MysqlDao() throws SQLException {
        if(HomeWindowController.networkStatus == null){
            HomeWindowController.networkStatus = MainApp.hasNetConnection();
        }
        if(HomeWindowController.networkStatus) {
            mysqlconnection = new EntityManagerFactoryHelper();
            emfactory = mysqlconnection.getEmFactory();
            eManager = emfactory.createEntityManager();
        }
        offlineDao = new OfflineDao();
        offlineService = new OfflineService(offlineDao);
    }

    /**
     * Implemented method of MysqlDao interface.
     * This method checking the user authentication by username and password.
     * @param username is user name.
     * @param password is password of user.
     * @return boolean, when the authentication is succeed return true, else return false.
     */
    @Override
    public boolean checkUserLogedIn(String username, String password) {
        eManager = emfactory.createEntityManager();
        eManager.getTransaction().begin();
        String queryString = "SELECT c FROM Client c WHERE c.username = :username and c.password = :password and c.status = :status" ;
        MainApp.logger.info("User authentication is processing...");
        Query query = eManager.createQuery(queryString).setParameter("username", username).setParameter("password", LoginWindowController.encryptPassword(password)).setParameter("status", 1);
        if(query.getResultList().size() == 1){
            List<Applicant> applicantList = null;

            if(HomeWindowController.networkStatus || MainApp.hasNetConnection()) {
                try {
                    MainApp.logger.info("Getting applicants from remoted database...");
                    applicantList = offlineService.getAllApplicantsFromMysql();
                    MainApp.logger.info("Counting applicants from remoted database...");
                    LoginWindowController.countedMysqlApplicants = applicantList.size();
                } catch (SQLException e) {
                    MainApp.logger.error("Source of error: " + e.getMessage());
                }
            }

            eManager.close();
            return true;
        }
        else{
            eManager.close();
            return false;
        }
    }

    /**
     * This method is implemented method of MysqlDao interface.
     * This method search Applicant by parameter / username.
      * @param username is user name.
     * @return Client, the result what has username the parameter.
     */
    @Override
    public Client getUserByUsername(String username) {
        eManager = emfactory.createEntityManager();
        eManager.getTransaction().begin();

        String queryString = "SELECT c FROM Client c WHERE c.username = :username" ;
        MainApp.logger.info("Getting user from remoted database: " + username);
        Query query = eManager.createQuery(queryString).setParameter("username", username).setMaxResults(1);
        List<Client> clients = query.getResultList();
        Client user = null;

        for (Client client : clients) {
            user = client;
        }

        eManager.close();
        return user;
    }

    /**
     * This method list all Applicants from the remoted database.
     * @return the list of results.
     * @throws SQLException if there is any problem with remoted SQL database.
     */
    @Override
    public List<Applicant> getAllApplicantsFromMysql() throws SQLException {
        List<Applicant> applicantList = new ArrayList<>();

        if(HomeWindowController.networkStatus == null){
            HomeWindowController.networkStatus = MainApp.hasNetConnection();
        }

        if(HomeWindowController.networkStatus){
            MainApp.logger.info("Connected to remoted database, getting applicants...");
            emfactory = mysqlconnection.getEmFactory();
            eManager = emfactory.createEntityManager();
            eManager.getTransaction().begin();

            String queryString = "SELECT a FROM Applicant a";
            Query results = eManager.createQuery(queryString);
            applicantList.addAll(results.getResultList());

            eManager.close();
        }

        return applicantList;
    }

    /**
     * This method counting Applicants in remoted database.
     * @return int, the number of counted applicants.
     * @throws SQLException if there is any problem with remoted SQL database.
     */
    @Override
    public int countMysqlApplicants() throws SQLException {
        List<Applicant> applicantList = new ArrayList<>();

        if(HomeWindowController.networkStatus == null){
            HomeWindowController.networkStatus = MainApp.hasNetConnection();
        }

        if(HomeWindowController.networkStatus){
            emfactory = mysqlconnection.getEmFactory();
            eManager = emfactory.createEntityManager();
            eManager.getTransaction().begin();
            MainApp.logger.info("Counting applicants in remote database...");
            String queryString = "SELECT a FROM Applicant a";
            Query results = eManager.createQuery(queryString);
            applicantList.addAll(results.getResultList());
            eManager.close();
            return applicantList.size();
        }
        else{
            eManager.close();
            return 0;
        }
    }

    /**
     * This method sending the modified applicants from local SqlLite database for remoted database.
     * @return boolean, true, if the transaction was succeed, else returns false.
     * @throws SQLException if there is any problem with remoted SQL database.
     */
    @Override
    public boolean sendApplicantsToMysql() throws SQLException {

        if(HomeWindowController.networkStatus == null){
            HomeWindowController.networkStatus = MainApp.hasNetConnection();
        }

        if(HomeWindowController.networkStatus) {
            emfactory = mysqlconnection.getEmFactory();
            eManager = emfactory.createEntityManager();
            eManager.getTransaction().begin();

            List<Applicant> aList = new ArrayList<>();
            System.out.println(offlineService.getModifiedApplicantsFromSqlLite().size());
            for (Applicant applicant : offlineService.getModifiedApplicantsFromSqlLite()) {
                MainApp.logger.info("Getting applicant details: " + applicant.getClientName());
                    String queryString = "SELECT a FROM Applicant a WHERE a.applicant_id = :applicant_id";
                    Query query = eManager.createQuery(queryString).setParameter("applicant_id", applicant.getApplicant_id()).setMaxResults(1);

                    List<Applicant> applicantResult = new ArrayList<>();
                    applicantResult.addAll(query.getResultList());

                    if(applicantResult.size() != 0) {
                        for (Applicant applicantMysql : applicantResult) {
                            if (applicantMysql.getModified_date() == null && applicantMysql.getModified_time() == null) {
                                MainApp.logger.info("The applicant is not exists, adding to list: " + applicantMysql.getClientName());
                                aList.add(applicant);
                            } else {
                                LocalDateTime mysqlModified = LocalDateTime.of(applicantMysql.getModified_date(), applicantMysql.getModified_time());
                                if (applicant.getModified().isAfter(mysqlModified)) {
                                    System.out.println(applicant.getId() + " " + applicant.getClientName() + " " + applicant.getModified());
                                    MainApp.logger.info(applicant.getClientName() + " is newer in offline database...");
                                    aList.add(applicant);
                                }
                            }
                        }
                    }
        }

        if(aList.size() != 0) {
            MainApp.logger.info("Refreshing applicants...");
            for (Applicant app : aList) {
                MainApp.logger.info("Modifiing " + app.getApplicant_id() + " " + app.getClientName());
                String updateApplicantString = "UPDATE Applicant a SET a.applicant_status = :applicant_status, a.modified_date = :modified_date, a.modified_time = :modified_time WHERE a.applicant_id = :applicant_id";
                Query updateApplicant = eManager.createQuery(updateApplicantString).setParameter("applicant_status", app.getApplicant_status()).setParameter("modified_date", app.getModified_date()).setParameter("modified_time", app.getModified_time()).setParameter("applicant_id", app.getApplicant_id());
                int result = updateApplicant.executeUpdate();
            }
        }

        eManager.getTransaction().commit();
        eManager.close();
        return true;
        }
        else {
            eManager.close();
            return false;
        }
    }

    /**
     * This method calling the sendApplicantsToMysql and getAllApplicantsFromMysql methods.
     * @throws SQLException if there is any problem with remoted SQL database.
     */
    @Override
    public void syncDatabase() throws SQLException {
        MainApp.logger.info("Sending offline applicants for remoted database...");
        sendApplicantsToMysql();
        MainApp.logger.info("Getting modified applicants from remoted database...");
        getAllApplicantsFromMysql();
    }

    /**
     * This method updates the Applicant in the remoted database when the network connection is available.
     * @param applicant_id is ID of applicant.
     * @param status is logged in status of applicant.
     * @param modified is modified date and time.
     * @throws SQLException if there is any problem with remoted SQL database.
     */
    @Override
    public void updateApplicant(String applicant_id, int status, LocalDateTime modified) throws SQLException {
        if(HomeWindowController.networkStatus) {
            emfactory = mysqlconnection.getEmFactory();
            eManager = emfactory.createEntityManager();
            eManager.getTransaction().begin();

            MainApp.logger.info("Getting applicant from database by id: " + applicant_id);
            String queryString = "SELECT a FROM Applicant a WHERE a.applicant_id = :applicant_id";
            Query query = eManager.createQuery(queryString).setParameter("applicant_id", applicant_id).setMaxResults(1);
            List<Applicant> applicantResult = query.getResultList();
            LocalDate modified_date = LocalDate.of(modified.getYear(), modified.getMonth(), modified.getDayOfMonth());
            LocalTime modified_time = LocalTime.of(modified.getHour(), modified.getMinute(), modified.getSecond());

            for (Applicant applicantMysql: applicantResult) {

                if(applicantMysql.getModified_date() == null && applicantMysql.getModified_time() == null){
                    MainApp.logger.info("The modifiing date and time is not setted in remoted database, therefore setting the newer version...");
                    String updateApplicantString = "UPDATE Applicant a SET a.applicant_status = :applicant_status, a.modified_date = :modified_date, a.modified_time = :modified_time WHERE a.applicant_id = :applicant_id";
                    Query updateApplicant = eManager.createQuery(updateApplicantString).setParameter("applicant_status", status).setParameter("modified_date", modified_date).setParameter("modified_time", modified_time).setParameter("applicant_id", applicant_id);
                    int result = updateApplicant.executeUpdate();
                }
                else{

                    if(applicantMysql.getModified() == null || modified.isAfter(applicantMysql.getModified())){
                        MainApp.logger.info("The applicant data is newer in offline database, therefore modifiing in remoted database...");
                        String updateString = "UPDATE Applicant a SET a.applicant_status = :applicant_status, a.modified_date = :modified_date, a.modified_time = :modified_time WHERE a.applicant_id = :applicant_id";
                        Query update = eManager.createQuery(updateString).setParameter("applicant_status", status).setParameter("modified_date", modified_date).setParameter("modified_time", modified_time).setParameter("applicant_id", applicant_id);
                        int result = update.executeUpdate();
                    }
                }
            }

            eManager.getTransaction().commit();
            eManager.close();
        }
    }
}
