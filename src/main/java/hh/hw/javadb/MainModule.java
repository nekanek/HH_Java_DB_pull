package hh.hw.javadb;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import hh.hw.javadb.employers.*;
import hh.hw.javadb.vacancies.*;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.hibernate.SessionFactory;

public class MainModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(EmployerDAO.class).to(EmployerDAOImpl.class).in(Singleton.class);
        bind(VacancyDAO.class).to(VacancyDAOImpl.class).in(Singleton.class);
    }
    
    @Provides
    @Singleton
    DataSource provideDataSource() throws SQLException {  
        return Config.pgSimpleDataSource();
    }
    
    @Provides
    @Singleton
    SessionFactory provideSessionFactory() {  
        return Config.getSessionFactory();
    }    

}
