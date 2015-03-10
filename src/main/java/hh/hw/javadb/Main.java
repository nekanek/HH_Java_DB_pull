package hh.hw.javadb;

import com.google.inject.Guice;
import com.google.inject.Injector;
import hh.hw.javadb.employers.*;
import hh.hw.javadb.vacancies.*;
import java.io.IOException;
import java.sql.SQLException;
import org.hibernate.SessionFactory;

class Main {
    
    public static void main(final String[] args) throws IOException, ClassNotFoundException, SQLException {
        final Injector injector = Guice.createInjector(new MainModule());
        final EmployerService employerService = injector.getInstance(EmployerService.class);  
        final VacancyDAO vacancyDAO = injector.getInstance(VacancyDAO.class);                  

        // clear both tables
        employerService.clearEmployersTable(vacancyDAO);

        // CRUD example
        // Create in Hibernate
        Employer e1 = new Employer("Augmented future");
        employerService.addEmployer(e1);
        Employer e2 = new Employer("ML implementers");
        employerService.addEmployer(e2);
        System.out.println("Added two employers");
        System.out.println("employers in db: " + employerService.getAllEmployers());

        // Update in Hibernate
        e1.setTitle("Augmented present");
        employerService.updateEmployer(e1);
        System.out.println("Updated employer name");
        System.out.println("employers in db: " + employerService.getAllEmployers());

        // Create in JDBC
        Vacancy coolGuy = new Vacancy("Awesome job", e2.getId());
        vacancyDAO.addVacancy(coolGuy);
        Vacancy aiBuilder = new Vacancy("AI construct developer", e1.getId());
        vacancyDAO.addVacancy(aiBuilder);
        Vacancy aiEngineer = new Vacancy("AI construct engineer", e1.getId());
        vacancyDAO.addVacancy(aiEngineer);
        System.out.println("3 vacancies created");
        System.out.println("vacancies in db: " + vacancyDAO.getAllVacancies());

        // Update in JDBC
        coolGuy.setTitle("Not so awesome job");
        vacancyDAO.updateVacancy(coolGuy);
        System.out.println("Changed vacancy title");
        System.out.println("vacancies in db: " + vacancyDAO.getAllVacancies());            

        // Delete in Hibernate
        // Calls VacancyService to delete all vacancies added by this employer
        employerService.deleteEmployer(e2, vacancyDAO);
        System.out.println("deleted employer " + e2.getId() + " and all corresponfing vacancies");
        System.out.println("employers in db: " + employerService.getAllEmployers());
        System.out.println("vacancies in db: " + vacancyDAO.getAllVacancies()); 

        // Delete in JDBC
        vacancyDAO.deleteAllEmployersVacancies(e1);
        System.out.println("deleted all vacancies by employer with id " + e1.getId());
        System.out.println("vacancies in db: " + vacancyDAO.getAllVacancies());
        System.out.println("employers in db: " + employerService.getAllEmployers());

        // cleaning up
        System.out.println("deleted employer with id " + e1.getId());
        employerService.deleteEmployer(e1, vacancyDAO);
        System.out.println("employers in db: " + employerService.getAllEmployers());

        System.out.println("Some fun stats:");
        System.out.println(injector.getInstance(SessionFactory.class).getStatistics());
    }
}

