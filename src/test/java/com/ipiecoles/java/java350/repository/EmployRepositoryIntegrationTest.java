package com.ipiecoles.java.java350.repository;

import com.ipiecoles.java.java350.model.Employe;
import com.ipiecoles.java.java350.service.EmployeService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@SpringBootTest
class EmployRepositoryIntegrationTest {

    @Autowired
    private EmployeRepository employeRepository;

    @Test
    public void testAvgPerformanceWhereMatriculeStartsWith(){

        employeRepository.save(new Employe("Jean", "John", "T12345", LocalDate.now(), 1500d, 1, 1.0));
        employeRepository.save(new Employe("Joe", "Dohn", "M55555", LocalDate.now(), 1500d, 1, 1.0));
        employeRepository.save(new Employe("Doe", "John", "C77777", LocalDate.now(), 1500d, 1, 1.0));

        Double avgPerf = employeRepository.avgPerformanceWhereMatriculeStartsWith("C");

        Assertions.assertThat(avgPerf).isGreaterThan(0);
    }
}
