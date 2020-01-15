package dbService;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DBService {
    private static final Logger LOGGER = LogManager.getLogger(DBService.class);
    private static final String hibernate_show_sql = "true";
    private static final String hibernate_hbm2ddl_auto = "update";
    private static final String CONFIG_FILE = "db_config.xml";

    private final SessionFactory sessionFactory;

    public DBService() {
        Configuration configuration = getConfig();
        sessionFactory = createSessionFactory(configuration);
    }

    private static Configuration getConfig() {
        Configuration configuration = new Configuration();
//        configuration.addAnnotatedClass(UsersDataSet.class);

        Properties properties = new Properties();
        try {
            properties.loadFromXML(ClassLoader.getSystemResourceAsStream(CONFIG_FILE));
        } catch (IOException e) {
            e.printStackTrace();
        }

        configuration.setProperties(properties);
        configuration.setProperty("hibernate.show_sql", hibernate_show_sql);
        configuration.setProperty("hibernate.hbm2ddl.auto", hibernate_hbm2ddl_auto);
        return configuration;
    }

    private static SessionFactory createSessionFactory(Configuration configuration) {
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        ServiceRegistry serviceRegistry = builder.build();
        return configuration.buildSessionFactory(serviceRegistry);
    }
}
