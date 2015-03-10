package hh.hw.javadb.employers;

import java.util.List;


public interface EmployerDAO {

    void addEmployer(Employer employer);

    List<Employer> getAllEmployers();

    Employer getEmployerById(Integer id);

    void updateEmployer(Employer employer);
    
}
