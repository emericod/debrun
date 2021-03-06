package services;

import dao.OfflineDao;
import interfaces.OfflineServiceInterface;
import models.Applicant;
import models.Client;
import models.Setting;
import java.sql.SQLException;
import java.util.List;

/**
 * This class is implements the service of SqlLite database.
 * @author Ilyés Imre
 * @version 1.0
 * @since 2018-04-08
 */

public class OfflineService implements OfflineServiceInterface<Applicant> {

    /** DAO.
     * Offline DAO.
     */
    private OfflineDao dao;

    /** Service.
     * Constructor of Offline sevice.
     */
    public OfflineService() {
        super();
    }

    /** Constructor.
     * Constructor of Offline sevice.
     * @param dao is Offline DAO.
     */
    public OfflineService(OfflineDao dao) {
        this.dao = dao;
    }

    /** User authentication method in local database.
     * This method is dooing the user authentication by usernam and password in the local SqlLite database.
     * @param username is user name.
     * @param password is password of user.
     * @return true, if the authentication is succeed, else returns false.
     * @throws SQLException if there is any problem with SQL database.
     */
    @Override
    public boolean userLoggedIn(String username, String password) throws SQLException {
        return dao.userLoggedIn(username, password);
    }

    /** Storing user to local database method.
     * This method adds the logged in user to the local SqlLite database.
     * @param user is object of Client.
     * @throws SQLException if there is any problem with SQL database.
     */
    @Override
    public void addUserToSqlLite(Client user) throws SQLException {
        dao.addUserToSqlLite(user);
    }

    /** Find user by username method.
     * This method search user by username in the local SqlLite database.
     * @param username is user name.
     * @return Client, result of Applicant what has username the parameter.
     * @throws SQLException if there is any problem with SQL database.
     */
    @Override
    public Client getUserByUsername(String username) throws SQLException {
        return dao.getUserByUsername(username);
    }

    /** Getting all applicants from local database.
     * Getting all applicants from the local SqLite database.
     * @return Results of applicants.
     * @throws SQLException if there is any problem with SQL database.
     */
    @Override
    public List<Applicant> getAllApplicantFromSqlLite() throws SQLException {
        return dao.getAllApplicantFromSqlLite();
    }

    /** Getting modified applicant list method.
     * Searching newer modified applicants from local SqlLite database.
     * @return Results of applicants.
     * @throws SQLException if there is any problem with SQL database.
     */
    @Override
    public List<Applicant> getModifiedApplicantsFromSqlLite() throws SQLException {
        return dao.getModifiedApplicantsFromSqlLite();
    }

    /** Applicant finder method by keyword.
     * Finding applicants by parameter / keyword in local SqlLite database.
     * @param keyword is keyword in search field what is included in applicant details.
     * @return Results of applicants.
     * @throws SQLException if there is any problem with SQL database.
     */
    @Override
    public List<Applicant> findApplicantsByKeyword(String keyword) throws SQLException {
        return dao.findApplicantsByKeyword(keyword);
    }

    /** Updating applicant status method by parameter.
     * Updating applicant status by parameter.
     * @param applicant_id is ID of applicant.
     * @param status is logged in status of applicant (0, if false, else 1, if the logged in true).
     * @throws SQLException if there is any problem with SQL database.
     */
    @Override
    public void updateApplicant(String applicant_id, int status) throws SQLException {
        dao.updateApplicant(applicant_id, status);
    }

    /** Getting applicant list from remoted database.
     * Getting all applicants from the remoted database if the network connection is available.
     * @return Results of applicants.
     * @throws SQLException if there is any problem with SQL database.
     */
    @Override
    public List<Applicant> getAllApplicantsFromMysql() throws SQLException {
        return dao.getAllApplicantsFromMysql();
    }

    /** Applicant counter method in local database.
     * Counting applicants in local SqlLite database.
     * @return number of applicants.
     * @throws SQLException if there is any problem with SQL database.
     */
    @Override
    public int countSqlLiteApplicants() throws SQLException {
        return dao.countSqlLiteApplicants();
    }

    /** Applicant counter method.
     * Counting applicants in remoted database.
     * @return number of applicants.
     * @throws SQLException if there is any problem with SQL database.
     */
    @Override
    public int countMysqlApplicants() throws SQLException {
        return dao.countMysqlApplicants();
    }

    /** Modify applicant in remoted database.
     * This method sending applicants to remoted database.
     * @return true, if the operation has succeed, else return false.
     * @throws SQLException if there is any problem with SQL database.
     */
    @Override
    public boolean sendApplicantsToMysql() throws SQLException {
        return dao.sendApplicantsToMysql();
    }

    /** Database synchronizer method (both side).
     * This method calls mysqlDao syncDatabase method.
     * @throws SQLException if there is any problem with SQL database.
     */
    @Override
    public void syncDatabase() throws SQLException {
        dao.syncDatabase();
    }

    /** Save setting method.
     * This method storing preferences in the local SqlLite database.
     * if the setting key is not available in the database, the method create or if it exists, the method update this.
     * @param settings is a list of settings.
     * @throws SQLException if there is any problem with SQL database.
     */
    @Override
    public void savePreferences(List<Setting> settings) throws SQLException {
        dao.savePreferences(settings);
    }

    /** Find setting by key method.
     * This method getting the setting record by configKey parameter from the local SqlLite database.
     * @param config_key is name of setting in local SQL database.
     * @return config_value.
     * @throws SQLException if there is any problem with SQL database.
     */
    @Override
    public Setting getPreference_by_key(String config_key) throws SQLException {
        return dao.getPreference_by_key(config_key);
    }
}
