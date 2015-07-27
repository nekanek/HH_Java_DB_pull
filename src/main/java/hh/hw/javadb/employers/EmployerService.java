package hh.hw.javadb.employers;

import com.google.inject.Inject;
import hh.hw.javadb.vacancies.VacancyService;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmployerService {

    private final SessionFactory sessionFactory;
    private final EmployerDAO employerDAO;
    
    private static final Logger log = LoggerFactory.getLogger(EmployerService.class);

    @Inject
    EmployerService(final SessionFactory sessionFactory, EmployerDAO employerDAO) {
        this.sessionFactory = sessionFactory;
        this.employerDAO = employerDAO;
    }

    @Inject
    public void clearEmployersTable(VacancyService vacancyService) throws SQLException {
        log.info("Tables cleanup started");
        List<Employer> employers = getAllEmployers();
        for (Employer e : employers) {
            deleteEmployer(e, vacancyService);
        }
    }

    public void addEmployer(Employer employer) throws SQLException {
        log.info("Create employer {} (hibernate)", employer);
        inTransaction(() -> employerDAO.addEmployer(employer));
    }

    public void updateEmployer(Employer employer) throws SQLException {
        log.info("Update employer {} (hibernate)", employer);
        inTransaction(() -> employerDAO.updateEmployer(employer));
    }

    public Employer getEmployerById(Integer id) throws SQLException {
        log.info("Get employer with id = {} (hibernate)", id);
        return inTransaction(() -> employerDAO.getEmployerById(id));
    }

    public List<Employer> getAllEmployers() throws SQLException  {
        return inTransaction(() -> employerDAO.getAllEmployers());
    }

    public void deleteEmployer(Employer employer, VacancyService vacancyServ) throws SQLException {
        log.info("Delete employer {} (hibernate), should also delete all vacancies linked to that employer", employer);
        final Session session = sessionFactory.openSession();
        try {
            final Transaction tx = session.beginTransaction();
            try {
                session.delete(employer);
                vacancyServ.deleteAllEmployersVacancies(employer);                
                tx.commit();
            } catch (RuntimeException e) {
                // log.error("Failed to delete employer {} (hibernate)", employer, e); 
                tx.rollback();
                closeSessionFactory();
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
            closeSessionFactory();
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
    
    public void closeSessionFactory() {
        sessionFactory.close();
        log.info("SessionFactory closed");
    } 
}
