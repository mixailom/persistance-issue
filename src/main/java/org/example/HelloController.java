package org.example;
import jakarta.annotation.Resource;
import java.util.stream.Collectors;
import org.example.data.EmployeeDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

  @Resource
  private  EmployeeDAO dao;

  @GetMapping("/")
  public String index() {

    String allemps = dao.findAllEmployees().stream().map(e -> e.getEmpId()+" "+ e.getEmpName() + " " + e.getAge()).collect(
        Collectors.joining(", "));
    return "Greetings from Spring Boot! " + allemps;
  }

}
