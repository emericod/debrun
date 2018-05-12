package services;

import dao.MysqlDao;
import interfaces.MysqlServiceInterface;
import models.Applicant;
import models.Client;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * This class is implements the service of mysql database.
 * @author Ilyés Imre
 * @version 1.0
 * @since 2018-04-08
 */

public class MysqlService implements MysqlServiceInterface<Applicant>{

    /** DAO.
     * Online DAO.
     */
    private MysqlDao dao;

    /** Service.
     * Constructor of MysqlService.
     */
    public MysqlService() {
        super();
    }

    /** Constructor.
     * Constructor of MysqlService.
     * @param dao is Mysql DAO.
     */
    public MysqlService(MysqlDao dao) {
        this.dao = dao;
    }

    /** User authentication method in remoted database.
     * This method authenticate user when try logging in.
     * @param username is user name.
     * @param password is password of user.
     * @return true, if the authentication has succeed, else return false.
     */
    @Override
    public boolean checkUserLogedIn(String username, String password) {
        return dao.checkUserLogedIn(username, password);
    }

    /** Getting all clients from remoted database.
     * This method is getting the client list from remoted database.
     * @return List of Client
     */
    @Override
    public List<Client> getAllClientsFromMysql() {
        return dao.getAllClientsFromMysql();
    }

    /** Search client by username from remoted database.
     * Getting user by username parameter.
     * @param username is user name.
     * @return result of Client.
     */
    @Override
    public Client getUserByUsername(String username) {
        return dao.getUserByUsername(username);
    }

    /** Getting all applicants from remoted database.
     * This method list all Applicants from the remoted database.
     * @return list of applicant results.
     * @throws SQLException if there is any problem with SQL database.
     */
    @Override
    public List<Applicant> getAllApplicantsFromMysql() throws SQLException {
        return dao.getAllApplicantsFromMysql();    }

    /** Count applicants in remoted database.
     * This method counting Applicants in remoted database.
     * @return number of counted applicants.
     * @throws SQLException if there is any problem with SQL database.
     */
    @Override
    public int countMysqlApplicants() throws SQLException {
        return dao.countMysqlApplicants();
    }

    /** Sending applicants for remoted database.
     * Sending applicants from local SqlLite database for remoted database.
     * @return true, if the operation is succeed, else returns false.
     * @throws SQLException if there is any problem with SQL database.
     */
    @Override
    public boolean sendApplicantsToMysql() throws SQLException {
        return dao.sendApplicantsToMysql();
    }

    /** Database synchronizer method (both side).
     * Synchronizing database with the latest modified applicants.
     * @throws SQLException if there is any problem with SQL database.
     */
    @Override
    public void syncDatabase() throws SQLException {
        dao.syncDatabase();
    }

    /** Applicant updater method.
     * Updating applicant status in the local SqlLite database.
     * @param applicant_id is ID of applicant.
     * @param status is logged in status of applicant.
     * @param modified is modify datetime of applicant.
     * @throws SQLException if there is any problem with SQL database.
     */
    @Override
    public void updateApplicant(String applicant_id, int status, LocalDateTime modified) throws SQLException {
        dao.updateApplicant(applicant_id, status, modified);
    }
}
