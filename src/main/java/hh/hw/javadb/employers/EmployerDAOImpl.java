package hh.hw.javadb.employers;

import com.google.inject.Inject;
import java.util.List;
import static java.util.Objects.requireNonNull;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class EmployerDAOImpl implements EmployerDAO {
    
    private final SessionFactory sessionFactory;
    
    @Inject
    public EmployerDAOImpl(final SessionFactory sessionFactory) {
        this.sessionFactory = requireNonNull(sessionFactory);
    }

    @Override
    public void addEmployer(Employer employer) {
        if (employer.getId() != null) {
            throw new IllegalArgumentException("can not save " + employer.toString() + " with assigned id");
        }
        session().save(employer);
    }
    
    @Override
    public void  updateEmployer(Employer employer) {
        session().update(employer);
    }

    @Override
    public Employer getEmployerById(Integer id) {
        return (Employer) session().load(Employer.class, id);
    }

    @Override
    public List<Employer> getAllEmployers() {
        return session().createCriteria(Employer.class).list();
    }
    
    private Session session() {
        return sessionFactory.getCurrentSession();
    }

}
