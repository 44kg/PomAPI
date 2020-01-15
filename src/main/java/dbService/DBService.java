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
        try (InputStream inputStream = ClassLoader.getSystemResourceAsStream(CONFIG_FILE)) {
            if (inputStream != null) {
                properties.loadFromXML(inputStream);
            }
        } catch (IOException e) {
            LOGGER.log(Level.ERROR, "Hibernate configuration loading error. File: " + CONFIG_FILE, e);
        }
        configuration.addProperties(properties);
        return configuration;
    }

    private static SessionFactory createSessionFactory(Configuration configuration) {
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        ServiceRegistry serviceRegistry = builder.build();
        return configuration.buildSessionFactory(serviceRegistry);
    }
}
