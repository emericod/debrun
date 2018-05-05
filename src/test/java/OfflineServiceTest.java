import controllers.HomeWindowController;
import controllers.LoginWindowController;
import controllers.PreferencesWindowController;
import dao.OfflineDao;
import models.Applicant;
import models.Client;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import services.OfflineService;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class OfflineServiceTest {
    private OfflineDao offlinedao;
    private OfflineService offlineservice;
    private Applicant testApplicantOffline;
    private Applicant testApplicantMysql;
    private Client testActiveClient;
    private Client testInactiveClient;

    @Before
    /*
     * Setting the default values
     */
    public void setUp() throws SQLException {
        offlinedao = new OfflineDao();
        offlineservice = new OfflineService(offlinedao);
        //constructor parameters - String username, String firstName, String lastName, String password, int status, boolean enabledStatus
        testActiveClient = new Client("userA", "Minta", "Péter", "1234", 1, true);
        testInactiveClient = new Client("userB", "Minta", "Márton", "1234", 1, false);

        //constructor parameters - int start_number, String applicant_id, int order_id, int product_id, int applicant_number, String clientName, String clientGender, String tshirtSize, int clientBirthDate, String clientEmail, String qrcode, Timestamp registration_date, LocalDate modified_date, LocalTime modified_time, String notes, int completed_status, int applicant_status, int trash_status, int loggedInStatus
        Timestamp testApplicantRegistrationDate1 = Timestamp.valueOf("2018-02-13 14:23:12");
        testApplicantOffline = new Applicant(301, "O1783P136A1", 1783, 136, 1, "Minta Nevező1", "férfi", "XL", 1986, null, null, testApplicantRegistrationDate1, null, null, null, 1, 0, 0, 0);

        Timestamp testApplicantRegistrationDate2 = Timestamp.valueOf("2018-02-13 18:21:35");
        testApplicantMysql = new Applicant(301, "O1783P136A2", 1783, 136, 2, "Minta Nevező2", "nő", "M", 1992, null, null, testApplicantRegistrationDate2, null, null, null, 1, 0, 0, 0);
    }

    @Test
    public void loginSuccessTest() throws SQLException {
        String userName = "demo";
        String passWord = "1234";
        Boolean loggedIn = offlineservice.userLoggedIn(userName, passWord);
        Assert.assertTrue(loggedIn);
    }

    @Test
    public void loginFailTest() throws SQLException {
        String userName = "userName";
        String passWord = "12345";
        Boolean loggedIn = offlineservice.userLoggedIn(userName, passWord);
        Assert.assertFalse(loggedIn);
    }

    @Test
    public void getUserByUsernameTest() throws SQLException {
        String userName = "demo";
        Client userResult = offlineservice.getUserByUsername(userName);
        Assert.assertNotNull(userResult);
    }

    @Test
    public void getAllApplicantFromSqlLiteTest() throws SQLException {
        List<Applicant> applicantsResult = offlineservice.getAllApplicantFromSqlLite();
        Assert.assertTrue(applicantsResult != null);
    }

    @Test
    public void getModifiedApplicantsFromSqlLiteTest() throws SQLException {
        List<Applicant> applicantsResult = offlineservice.getModifiedApplicantsFromSqlLite();
        Assert.assertTrue(applicantsResult != null);
    }

    @Test
    public void findApplicantsByKeywordTest() throws SQLException {
        String keyword = "O";
        List<Applicant> applicantsResult = offlineservice.findApplicantsByKeyword(keyword);
        Assert.assertTrue(applicantsResult != null);
    }

    @Test
    public void getAllApplicantsFromMysqlTest() throws SQLException {
        List<Applicant> applicantsResult = offlineservice.getAllApplicantsFromMysql();
        Assert.assertTrue(applicantsResult != null);
    }

    @Test
    public void countMysqlApplicantsTest() throws SQLException {
        Assert.assertTrue(offlineservice.countMysqlApplicants() >= 0);
    }

    @Test
    public void sendApplicantsToMysqlTest() throws SQLException {
        Assert.assertTrue((HomeWindowController.networkStatus && offlineservice.sendApplicantsToMysql()) || (!HomeWindowController.networkStatus && offlineservice.sendApplicantsToMysql() == false));
    }

    @Test
    public void getPreference_by_keyTest() throws SQLException {
        Assert.assertNotNull(offlineservice.getPreference_by_key("remote_db_host"));
        Assert.assertNotNull(offlineservice.getPreference_by_key("remote_db_user"));
        Assert.assertNotNull(offlineservice.getPreference_by_key("remote_db_password"));
    }

    @Test
    public void PreferencesValidatorTest(){
        String sampleText1 = "1234";
        Assert.assertTrue(PreferencesWindowController.isValidNumberField(sampleText1));
        String sampleText2 = "ssdas1234";
        Assert.assertFalse(PreferencesWindowController.isValidNumberField(sampleText2));
    }
}
