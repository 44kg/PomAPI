package dbService;

import dbService.dao.GavsDAO;
import dbService.dao.PomsDAO;
import dbService.dataSets.GavDataSet;
import dbService.dataSets.PomDataSet;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

public class DBService {
    private static final Logger LOGGER = LogManager.getLogger(DBService.class);
    private static final String CONFIG_FILE = "db_config.xml";

    private final SessionFactory sessionFactory;

    public DBService() {
        Configuration configuration = getConfig();
        sessionFactory = createSessionFactory(configuration);
    }

    public void insertPom(String projectAttributes, String modelVersion, String otherCode, GavDataSet mainGav,
                          Set<GavDataSet> dependentGavs) {
        PomDataSet pomDataSet = new PomDataSet(projectAttributes, modelVersion, otherCode);
        mainGav.setDependentGavs(dependentGavs);
        pomDataSet.setGavDataSet(mainGav);
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        PomsDAO pomsDAO = new PomsDAO(session);
        pomsDAO.insert(pomDataSet);
        transaction.commit();
        session.close();
    }

    public Set<GavDataSet> getDependentGavs(String groupId, String artifactId, String version) {
        Session session = sessionFactory.openSession();
        GavsDAO gavsDAO = new GavsDAO(session);
        List<GavDataSet> gavDataSets = gavsDAO.get(groupId, artifactId, version);
        session.close();
        if (gavDataSets.size() == 0) return null;
        Set<GavDataSet> dependentGavs = new HashSet<>();
        for (GavDataSet gavDataSet : gavDataSets) {
            dependentGavs.addAll(gavDataSet.getDependentGavs());
        }
        return dependentGavs;
    }

    public Set<GavDataSet> getMainGavs(String groupId, String artifactId, String version) {
        Session session = sessionFactory.openSession();
        GavsDAO gavsDAO = new GavsDAO(session);
        List<GavDataSet> gavDataSets = gavsDAO.get(groupId, artifactId, version);
        session.close();
        if (gavDataSets.size() == 0) return null;
        Set<GavDataSet> mainGavs = new HashSet<>();
        for (GavDataSet gavDataSet : gavDataSets) {
            mainGavs.addAll(gavDataSet.getMainGavs());
        }
        return mainGavs;
    }

    public PomDataSet getPom(String groupId, String artifactId, String version) {
        Session session = sessionFactory.openSession();
        GavsDAO gavsDAO = new GavsDAO(session);
        List<GavDataSet> gavDataSets = gavsDAO.get(groupId, artifactId, version);
        session.close();
        if (gavDataSets.size() == 0) return null;
        for (GavDataSet gavDataSet : gavDataSets) {
            if (gavDataSet.getPomDataSet() != null) return gavDataSet.getPomDataSet();
        }
        return null; //добавить строитель, если есть только зависимый гав
    }

    public Set<GavDataSet> getMostUsedGavs(int amount) {
        Session session = sessionFactory.openSession();
        GavsDAO gavsDAO = new GavsDAO(session);
        List<BigInteger> list = gavsDAO.getMostUsedGavIds(amount);
        Set<GavDataSet> gavDataSets = new HashSet<>();
        for (BigInteger id : list) {
            gavDataSets.add(gavsDAO.get(id.longValue()));
        }
        return gavDataSets;
    }

    private static Configuration getConfig() {
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(GavDataSet.class);
        configuration.addAnnotatedClass(PomDataSet.class);
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

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
