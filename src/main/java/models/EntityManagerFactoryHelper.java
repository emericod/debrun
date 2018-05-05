package models;

import dao.OfflineDao;
import services.OfflineService;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


/**
 * EntityManagerFactoryHelper is class of EntityManagerFactory, this class helps to build the connection with mysql database.
 * @author Ilyés Imre
 * @version 1.0
 * @since 2018-04-08
 */
public class EntityManagerFactoryHelper implements AutoCloseable {

    /**
     * Default EntityManagerFactoryHelper.
     */
    private static final EntityManagerFactoryHelper ENTITY_MANAGER_FACTORY_HELPER = new EntityManagerFactoryHelper();

    /**
     * Default persistence unit name in persistence.xml.
     */
    private static final String PERSISTENCE_UNIT_NAME = "debrununit";

    /**
     * Default EntityManagerFactory.
     */
    private EntityManagerFactory emFactory;

    /**
     * Getter of instance.
     * @return ENTITY_MANAGER_FACTORY_HELPER.
     */
    public static EntityManagerFactoryHelper getInstance() {
        return ENTITY_MANAGER_FACTORY_HELPER;
    }

    /**
     * This method creating EnityManagerFactory and returns for the connection.
     * @return EntityManagerFactory.
     * @throws SQLException if there is any problem with SQL database.
     */
    public EntityManagerFactory getEmFactory() throws SQLException {
        OfflineDao dao = new OfflineDao();
        OfflineService service = new OfflineService(dao);
        Map<String,String> persistenceMap = new HashMap<>();

        String dburl = service.getPreference_by_key("remote_db_host").getConfigValue();
        persistenceMap.put("javax.persistence.jdbc.url", "jdbc:mysql://" + dburl + "/" + service.getPreference_by_key("remote_database").getConfigValue());
        persistenceMap.put("javax.persistence.jdbc.user", service.getPreference_by_key("remote_db_user").getConfigValue());
        persistenceMap.put("javax.persistence.jdbc.password", service.getPreference_by_key("remote_db_password").getConfigValue());

        if(emFactory  == null)
            emFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME, persistenceMap);
        return emFactory;
    }

    /**
     * EntityManagerFactory closing method.
     * @throws Exception if there is any problem with SQL database.
     */
    @Override
    public void close() throws Exception {

        if(emFactory != null)
            emFactory.close();
    }
}