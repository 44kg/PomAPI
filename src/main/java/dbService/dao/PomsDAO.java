package dbService.dao;

import dbService.dataSets.PomDataSet;
import org.hibernate.Session;

public class PomsDAO {
    private Session session;

    public PomsDAO(Session session) {

    }

    public PomDataSet get(long id) {
        return (PomDataSet) session.get(PomDataSet.class, id);
    }
}
