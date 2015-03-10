package hh.hw.javadb.vacancies;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import hh.hw.javadb.Config;
import java.sql.SQLException;
import javax.sql.DataSource;

public class VacancyModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(VacancyDAO.class).to(VacancyDAOImpl.class).in(Singleton.class);
    }
    
    @Provides
    @Singleton
    DataSource provideDataSource() throws SQLException {  
        return Config.pgSimpleDataSource();
    }  

}
