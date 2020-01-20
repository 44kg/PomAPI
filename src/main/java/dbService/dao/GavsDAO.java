package dbService.dao;

import dbService.dataSets.GavDataSet;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.math.BigInteger;
import java.util.List;

public class GavsDAO {
    private Session session;

    public GavsDAO(Session session) {
        this.session = session;
    }

    public GavDataSet get(long id) {
        return (GavDataSet) session.get(GavDataSet.class, id);
    }

    public List<GavDataSet> get(String groupId, String artifactId, String version) {
        Criteria criteria = session.createCriteria(GavDataSet.class);
        return criteria.add(Restrictions.eq("groupId", groupId)).
                add(Restrictions.eq("artifactId", artifactId)).
                add(Restrictions.eq("version", version)).list();
    }

    public List<BigInteger> getMostUsedGavIds(int amount) {
        Query query = session.createSQLQuery("SELECT DISTINCT dependent_gav FROM dependencies GROUP BY dependent_gav ORDER BY COUNT(*) DESC LIMIT " + amount);
        return query.list();
    }

    public long insert(GavDataSet gavDataSet) {
        return (long) session.save(gavDataSet);
    }
}
