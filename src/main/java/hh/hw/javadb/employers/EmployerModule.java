package hh.hw.javadb.employers;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import hh.hw.javadb.Config;
import org.hibernate.SessionFactory;

public class EmployerModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(EmployerDAO.class).to(EmployerDAOImpl.class).in(Singleton.class);
    }
    
    @Provides
    @Singleton
    SessionFactory provideSessionFactory() {  
        return Config.getSessionFactory();
    }    

}
