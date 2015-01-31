package hh.hw.javadb.employers;

import hh.hw.javadb.vacancies.VacancyDAO;
import java.sql.SQLException;
import java.util.List;

public interface EmployerDAO {

    public void dropEmployersTable(VacancyDAO vacancyServ) throws SQLException;
    
    public void addEmployer(Employer employer) throws SQLException;

    public void updateEmployer(Employer employer) throws SQLException;

    public Employer getEmployerById(Long id) throws SQLException;

    public List getAllEmployers() throws SQLException;

    public void deleteEmployer(Employer employer, VacancyDAO vacancyServ) throws SQLException;
    
}
