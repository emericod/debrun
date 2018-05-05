package interfaces;

import models.Applicant;
import models.Client;
import models.Setting;

import java.sql.SQLException;
import java.util.List;

/**
 * OfflineServiceInterface is class of SQL Lite interface.
 * @author Ilyés Imre
 * @version 1.0
 * @since 2018-04-08
 * @param <T> is actual parameter.
 */
public interface OfflineServiceInterface<T> {

    /**
     * This method is doing the user authentication by usernam and password in the local SqlLite database.
     * @param username is user name.
     * @param password is password of user.
     * @return true, if the authenticaion is succeed, else returns false.
     * @throws SQLException if there is any problem with local SQL database.
     */
    public boolean userLoggedIn(String username, String password) throws SQLException;

    /**
     * This method adds the logged in user to the local SqlLite database.
     * @param user is object of Client.
     * @throws SQLException if there is any problem with local SQL database.
     */
    public void addUserToSqlLite(Client user) throws SQLException;

    /**
     * This method search user by username in the local SqlLite database.
     * @param username is user name.
     * @return Client, result of Applicant what has username the parameter.
     * @throws SQLException if there is any problem with local SQL database.
     */
    public Client getUserByUsername(String username) throws SQLException;

    /**
     * Getting all applicants from the local SqlLite database.
     * @return Results of applicants.
     * @throws SQLException if there is any problem with local SQL database.
     */
    public List<Applicant> getAllApplicantFromSqlLite() throws SQLException;

    /**
     * Searching newer modified applicants from local SqlLite database.
     * @return Results of applicants.
     * @throws SQLException if there is any problem with local SQL database.
     */
    public List<Applicant> getModifiedApplicantsFromSqlLite() throws SQLException;

    /**
     * Finding applicants by parameter / keyword in local SqlLite database.
     * @param keyword is keyword what is included applicant data contents.
     * @return Results of applicants.
     * @throws SQLException if there is any problem with local SQL database.
     */
    public List<Applicant> findApplicantsByKeyword(String keyword) throws SQLException;

    /**
     * Updating applicant status by parameter.
     * @param applicant_id ID of applicant.
     * @param status is logged in status of applicant (0, if the status is false, 1, if status is true).
     * @throws SQLException if there is any problem with local SQL database.
     */
    public void updateApplicant(String applicant_id, int status) throws SQLException;

    /**
     * Getting all applicants from the remoted database if the network connection is available.
     * @return Results of applicants.
     * @throws SQLException if there is any problem with local SQL database.
     */
    public List<Applicant> getAllApplicantsFromMysql() throws SQLException;

    /**
     * Counting applicants in local SqlLite database.
     * @return number of applicants.
     * @throws SQLException if there is any problem with local SQL database.
     */
    public int countSqlLiteApplicants() throws SQLException;

    /**
     * Counting applicants in remoted database.
     * @return number of applicants.
     * @throws SQLException if there is any problem with local SQL database.
     */
    public int countMysqlApplicants() throws SQLException;

    /**
     * This method sending applicants to remoted database.
     * @return true, if the operation has succeed, else return false.
     * @throws SQLException if there is any problem with local SQL database.
     */
    public boolean sendApplicantsToMysql() throws SQLException;

    /**
     * This method calls mysqlDao syncDatabase method.
     * @throws SQLException if there is any problem with local SQL database.
     */
    public void syncDatabase() throws SQLException;

    /**
     * This method storing preferences in the local SqlLite database.
     * if the setting key is not available in the database, the method create or if it exists, the method update this.
     * @param settings is a list of settings.
     * @throws SQLException if there is any problem with local SQL database.
     */
    public void savePreferences(List<Setting> settings) throws SQLException;

    /**
     * This method getting the setting record by configKey parameter from the local SqlLite database.
     * @param config_key is name of setting in the local SQL database.
     * @return config value.
     * @throws SQLException if there is any problem with local SQL database.
     */
    public Setting getPreference_by_key(String config_key) throws SQLException;
}
