package hh.hw.javadb.employers;

import com.google.inject.Inject;
import hh.hw.javadb.vacancies.VacancyDAO;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class EmployerService {

    private final SessionFactory sessionFactory;
    private final EmployerDAO employerDAO;

    @Inject
    public EmployerService(final SessionFactory sessionFactory, EmployerDAO employerDAO) {
        this.sessionFactory = sessionFactory;
        this.employerDAO = employerDAO;
    }

    public void clearEmployersTable(VacancyDAO vacancyDAO) throws SQLException {
        List<Employer> employers = getAllEmployers();
        for (Employer e : employers) {
            deleteEmployer(e, vacancyDAO);
        }
    }

    public void addEmployer(Employer employer) throws SQLException {
        inTransaction(() -> employerDAO.addEmployer(employer));
    }

    public void updateEmployer(Employer employer) throws SQLException {
        inTransaction(() -> employerDAO.updateEmployer(employer));
    }

    public Employer getEmployerById(Integer id) throws SQLException {
        return inTransaction(() -> employerDAO.getEmployerById(id));
    }

    public List<Employer> getAllEmployers() throws SQLException  {
        return inTransaction(() -> employerDAO.getAllEmployers());
    }

    public void deleteEmployer(Employer employer, VacancyDAO vacancyServ) throws SQLException {
        final Session session = sessionFactory.openSession();
        try {
            final Transaction tx = session.beginTransaction();
            try {
                session.delete(employer);
                vacancyServ.deleteAllEmployersVacancies(employer);                
                tx.commit();
            } catch (RuntimeException e) {
                tx.rollback();
                throw e;
            }
        } finally {
            session.close();
        }
    }

    private <T> T inTransaction(final Supplier<T> supplier) throws SQLException {
        final Optional<Transaction> transaction = beginTransaction();
        try {
            final T result = supplier.get();
            transaction.ifPresent(Transaction::commit);
            return result;
        } catch (RuntimeException e) {
            transaction.ifPresent(Transaction::rollback);
            throw e;
        }
    }

    private void inTransaction(final Runnable runnable) throws SQLException {
        inTransaction(() -> {
            runnable.run();
            return null;
        });
    }

    private Optional<Transaction> beginTransaction() {
        final Transaction transaction = sessionFactory.getCurrentSession().getTransaction();
        if (!transaction.isActive()) {
            transaction.begin();
            return Optional.of(transaction);
        }
        return Optional.empty();
    }
}
