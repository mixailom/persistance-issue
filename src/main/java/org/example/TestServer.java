package org.example;

import java.util.Arrays;

import java.util.List;
import org.example.data.Employee;
import org.example.data.EmployeeDAO;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@SpringBootApplication
public class TestServer {
  public static void main(String[] args) {
  /*  try(
    ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext
        ("appContext.xml")
    ) {
      EmployeeDAO dao = (EmployeeDAO) context.getBean("employeeDAOImpl");
      List<Employee> empList = dao.findAllEmployees();
      System.out.println("Name - " + empList.get(0).getEmpName() +
          " Age - " + empList.get(0).getAge());
      context.registerShutdownHook();
    }*/
    SpringApplication.run(TestServer.class, args);
  }
  @Bean
  public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
    return args -> {

      System.out.println("Let's inspect the beans provided by Spring Boot:");

      String[] beanNames = ctx.getBeanDefinitionNames();
      Arrays.sort(beanNames);
      for (String beanName : beanNames) {
        System.out.println(beanName);
      }

    };
  }
}
