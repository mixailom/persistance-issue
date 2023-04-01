package org.example.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Repository
public class EmployeeDAOImpl implements EmployeeDAO{
  @Autowired
  private JdbcTemplate jdbcTemplate;

  private static Vector<Connection> connectionsGraveYard = new Vector<>();

  private static final Logger log = LoggerFactory.getLogger(EmployeeDAOImpl.class);

  @Override
  public List<Employee> findAllEmployees() {
    final String SELECT_ALL_QUERY = "SELECT * from EMPLOYEE";
    List<Employee> res = this.jdbcTemplate.query(SELECT_ALL_QUERY, new EmployeeMapper());
      try {
      connectionsGraveYard.add(jdbcTemplate.getDataSource().getConnection());
      log.info("Connections in connectionsGraveYeard: " + connectionsGraveYard.size());
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return res;
  }

  private static final class EmployeeMapper implements RowMapper<Employee> {
    public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
      Employee emp = new Employee();
      emp.setEmpId(rs.getInt("id"));
      emp.setEmpName(rs.getString("name"));
      emp.setAge(rs.getInt("age"));
      return emp;
    }
  }
}
