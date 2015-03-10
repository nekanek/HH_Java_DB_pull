package hh.hw.javadb.vacancies;

import com.google.inject.Inject;
import hh.hw.javadb.employers.Employer;
import hh.hw.javadb.employers.EmployerService;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


class VacancyDAOImpl implements VacancyDAO {

    private final DataSource dataSource;
    private static final Logger log = LoggerFactory.getLogger(EmployerService.class);
    
    @Inject
    public VacancyDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void addVacancy(Vacancy vacancy) throws SQLException {
        log.info("Create vacancy {} (JDBC)", vacancy);
        if (vacancy.getId() > 0) {
            throw new IllegalArgumentException("cannot add vacancy with already assigned id");
        }
        Connection conn = dataSource.getConnection();
        conn.setAutoCommit(false);
        try {
            String query = "INSERT INTO vacancies (title, employer_id) VALUES (?, ?)";
            try (final PreparedStatement statement
                    = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, vacancy.getTitle());
                statement.setInt(2, vacancy.getEmployer_id());

                statement.executeUpdate();
                try (final ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    generatedKeys.next();
                    vacancy.setId(generatedKeys.getInt(1));  
                }
            }
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw new RuntimeException("failed to add vacancy " + vacancy, e);
        }
        finally { conn.close();}
    }

    @Override
    public void updateVacancy(Vacancy vacancy) throws SQLException {
        log.info("Update vacancy {} (JDBC)", vacancy);
        if (vacancy.getId() <= 0) {
            throw new IllegalArgumentException("cannot update vacancy without id");
        }
        Connection conn = dataSource.getConnection();
        conn.setAutoCommit(false);
        try  {
            final String query = "UPDATE vacancies SET title = ?, employer_id = ? WHERE vacancy_id = ?";
            try (final PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setString(1, vacancy.getTitle());
                statement.setInt(2, vacancy.getEmployer_id());
                statement.setInt(3, vacancy.getId());

                statement.executeUpdate();
            }
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw new RuntimeException("failed to update vacancy", e);
        }
        finally { conn.close();}
    }

    @Override
    public List getAllVacancies() throws SQLException {
        log.info("Get all vacancies (JDBC)");
        try (final Connection connection = dataSource.getConnection()) {
            try (final Statement statement = connection.createStatement()) {
                final String query = "SELECT * FROM vacancies";
                try (final ResultSet resultSet = statement.executeQuery(query)) {
                    List<Vacancy> vacancies = new ArrayList<>();
                    int id;
                    int e_id;
                    String title;
                    while (resultSet.next()) {
                        id = resultSet.getInt("vacancy_id");
                        title = resultSet.getString("title");
                        e_id = resultSet.getInt("employer_id");
                        vacancies.add(new Vacancy(id, title, e_id));
                    }
                    return vacancies;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("failed to get vacancies", e);
        }
    }

    @Override
    public List getAllEmployersVacancies(Employer employer) throws SQLException {
        log.info("Get vacancies linked to employer {} (JDBC)", employer);
        try (final Connection conn = dataSource.getConnection()) {
            final String query = "SELECT * FROM vacancies WHERE employer_id = ?";
            try (final PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setInt(1, employer.getId());
                try (final ResultSet resultSet = statement.executeQuery();) {
                    List<Vacancy> vacancies = new ArrayList<>();
                    int id;
                    int e_id;
                    String title;
                    while (resultSet.next()) {
                        id = resultSet.getInt("vacancy_id");
                        title = resultSet.getString("title");
                        e_id = resultSet.getInt("employer_id");
                        vacancies.add(new Vacancy(id, title, e_id));
                    }
                    return vacancies;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void deleteVacancy(Vacancy vacancy) throws SQLException {
        log.info("Delete vacancy {} (JDBC)", vacancy);
        Connection conn = dataSource.getConnection();
        conn.setAutoCommit(false);        
        try {
            final String query = "DELETE FROM vacancies WHERE vacancy_id = ?";
            try (final PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setInt(1, vacancy.getId());
                statement.executeUpdate();
            }
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw new SQLException("Failed to delete vacancy " + vacancy, e);
        }
        finally { conn.close();}
    }

    @Override
    public void deleteAllEmployersVacancies(Employer employer) throws SQLException {
        log.info("Delete vacancies of employer {} (JDBC)", employer);
        List<Vacancy> vacancies = getAllEmployersVacancies(employer);
        for (Vacancy v : vacancies) {
            deleteVacancy(v);
        }
    }

}
