
package hh.hw.javadb.vacancies;

import com.google.inject.Inject;
import hh.hw.javadb.employers.Employer;
import java.sql.SQLException;
import java.util.List;


public class VacancyService {
    private final VacancyDAO vacancyDAO;

    @Inject
    VacancyService(VacancyDAO vacancyDAO) {
        this.vacancyDAO = vacancyDAO;
    }

    public void addVacancy(Vacancy vacancy) throws SQLException {
        vacancyDAO.addVacancy(vacancy);
    }

    public void updateVacancy(Vacancy vacancy) throws SQLException {
        vacancyDAO.updateVacancy(vacancy);
    }

    public List getAllVacancies() throws SQLException {
        return vacancyDAO.getAllVacancies();
    }

    public List getAllEmployersVacancies(Employer employer) throws SQLException {
        return vacancyDAO.getAllEmployersVacancies(employer);
    }

    public void deleteVacancy(Vacancy vacancy) throws SQLException {
        vacancyDAO.deleteVacancy(vacancy);
    }

    public void deleteAllEmployersVacancies(Employer employer) throws SQLException {
        vacancyDAO.deleteAllEmployersVacancies(employer);
    }

}
