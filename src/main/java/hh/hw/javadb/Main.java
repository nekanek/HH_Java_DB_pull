package hh.hw.javadb;

import com.google.inject.Guice;
import com.google.inject.Injector;
import hh.hw.javadb.employers.EmployerService;
import hh.hw.javadb.employers.Employer;
import hh.hw.javadb.employers.EmployerModule;
import hh.hw.javadb.vacancies.VacancyService;
import hh.hw.javadb.vacancies.Vacancy;
import hh.hw.javadb.vacancies.VacancyModule;
import java.io.IOException;
import java.sql.SQLException;
import org.hibernate.SessionFactory;

class Main {
    
    public static void main(final String[] args) throws IOException, ClassNotFoundException, SQLException {
        final Injector injector = Guice.createInjector(new EmployerModule(), new VacancyModule());
        final EmployerService employerService = injector.getInstance(EmployerService.class);  
        final VacancyService vacancyService = injector.getInstance(VacancyService.class);                  

        employerService.clearEmployersTable(vacancyService);

        Employer e1 = new Employer("Augmented future");
        employerService.addEmployer(e1);
        Employer e2 = new Employer("ML implementers");
        employerService.addEmployer(e2);
        System.out.println("employers in db: " + employerService.getAllEmployers());

        e1.setTitle("Augmented present");
        employerService.updateEmployer(e1);
        System.out.println("employers in db: " + employerService.getAllEmployers());

        Vacancy coolGuy = new Vacancy("Awesome job", e2.getId());
        vacancyService.addVacancy(coolGuy);
        Vacancy aiBuilder = new Vacancy("AI construct developer", e1.getId());
        vacancyService.addVacancy(aiBuilder);
        Vacancy aiEngineer = new Vacancy("AI construct engineer", e1.getId());
        vacancyService.addVacancy(aiEngineer);
        System.out.println("vacancies in db: " + vacancyService.getAllVacancies());

        coolGuy.setTitle("Not so awesome job");
        vacancyService.updateVacancy(coolGuy);
        System.out.println("vacancies in db: " + vacancyService.getAllVacancies());            

        employerService.deleteEmployer(e2, vacancyService);
        System.out.println("employers in db: " + employerService.getAllEmployers());
        System.out.println("vacancies in db: " + vacancyService.getAllVacancies()); 

        vacancyService.deleteAllEmployersVacancies(e1);
        System.out.println("vacancies in db: " + vacancyService.getAllVacancies());
        System.out.println("employers in db: " + employerService.getAllEmployers());

        employerService.deleteEmployer(e1, vacancyService);
        System.out.println("employers in db: " + employerService.getAllEmployers());

        System.out.println("Some fun stats:");
        System.out.println(injector.getInstance(SessionFactory.class).getStatistics());
        
        employerService.closeSessionFactory();
    }
}

