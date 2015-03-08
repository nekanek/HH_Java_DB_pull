package hh.hw.javadb.employers;

import java.util.List;
import static java.util.Objects.requireNonNull;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class EmployerDAO {
    
    private final SessionFactory sessionFactory;
    
    public EmployerDAO(final SessionFactory sessionFactory) {
        this.sessionFactory = requireNonNull(sessionFactory);
    }

    public void addEmployer(Employer employer) {
        if (employer.getId() != null) {
            throw new IllegalArgumentException("can not save " + employer.toString() + " with assigned id");
        }
        session().save(employer);
    }
    
    public void  updateEmployer(Employer employer) {
        session().update(employer);
    }

    public Employer getEmployerById(Integer id) {
        return (Employer) session().load(Employer.class, id);
    }

    public List<Employer> getAllEmployers() {
        return session().createCriteria(Employer.class).list();
    }
    
    private Session session() {
        return sessionFactory.getCurrentSession();
    }

}
