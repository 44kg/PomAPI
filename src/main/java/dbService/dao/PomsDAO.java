package dbService.dao;

import dbService.dataSets.PomDataSet;
import org.hibernate.Session;

public class PomsDAO {
    private Session session;

    public PomsDAO(Session session) {
        this.session = session;
    }

    public PomDataSet get(long id) {
        return (PomDataSet) session.get(PomDataSet.class, id);
    }

    public long insert(PomDataSet pomDataSet) {
        return (long) session.save(pomDataSet);
    }
}
