package hh.hw.javadb;

import hh.hw.javadb.employers.Employer;
import java.sql.SQLException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.postgresql.ds.PGSimpleDataSource;

public class Config {
    private static final String url = "jdbc:postgresql:hh_hw_javadb";
    private static final String username = "neko";
    private static final String password = "1u1z";    
    
    private static final String dialect = "org.hibernate.dialect.PostgreSQL9Dialect";  
    private static final String driver = "org.postgresql.Driver";  
    private static final String context = "thread";  
    private static final String mappings = "true";  
    private static final String showSql = "false"; 
    private static final String formatSql = "true";  
    private static final String generators = "true";
    private static final String statistics = "false";
    
    
    public static PGSimpleDataSource pgSimpleDataSource()
            throws SQLException {

        final PGSimpleDataSource pgSimpleDataSource = new PGSimpleDataSource();
        pgSimpleDataSource.setUrl(url);
        pgSimpleDataSource.setUser(username);
        pgSimpleDataSource.setPassword(password);
        return pgSimpleDataSource;
    }

    public static SessionFactory getSessionFactory() {
        Configuration configuration = new Configuration().addAnnotatedClass(Employer.class);
        configuration.setProperty("hibernate.connection.url", url)
                     .setProperty("hibernate.connection.username", username)
                     .setProperty("hibernate.connection.password", password)
                     .setProperty("hibernate.dialect", dialect)
                     .setProperty("hibernate.connection.driver_class", driver)
                     .setProperty("hibernate.current_session_context_class", context)
                     .setProperty("hibernate.id.new_generator_mappings", mappings)
                     .setProperty("hibernate.show_sql", showSql)
                     .setProperty("hibernate.format_sql", formatSql)
                     .setProperty("hibernate.id.new_generator_mappings", generators)
                     .setProperty("hibernate.generate_statistics", statistics)
                ;
        
        return configuration.buildSessionFactory();
    }   
}

