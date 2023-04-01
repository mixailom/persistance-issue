package org.example;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import java.beans.PropertyVetoException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class MyConfiguration {
  @Bean
  public JdbcTemplate jdbcTemplate(ComboPooledDataSource dataSource){
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    return jdbcTemplate;
  }
  @Bean
  public ComboPooledDataSource dataSource(){
    ComboPooledDataSource ds = new ComboPooledDataSource();
    try {
      ds.setDriverClass("com.mysql.jdbc.Driver");
    } catch (PropertyVetoException e) {
      throw new RuntimeException(e);
    }
    ds.setJdbcUrl("jdbc:mysql://localhost:3306/c3p0test");
    ds.setUser("test1");
    ds.setPassword("test1");
    ds.setInitialPoolSize(5);
    ds.setMinPoolSize(10);
    ds.setMaxPoolSize(50);
    ds.setMaxStatements(50);
    ds.setIdleConnectionTestPeriod(3600);
    ds.setAcquireIncrement(5);
    ds.setMaxIdleTime(600);
    ds.setNumHelperThreads(6);
    ds.setBreakAfterAcquireFailure(true);
    ds.setCheckoutTimeout(1000);
    ds.setUnreturnedConnectionTimeout(10);
    //ds.setMaxIdleTimeExcessConnections(600);
    ds.setDebugUnreturnedConnectionStackTraces(true);
    return ds;
  }
}
