package com.ipiecoles.java.java350.repository;

import com.ipiecoles.java.java350.model.Employe;
import com.ipiecoles.java.java350.service.EmployeService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@SpringBootTest
class EmployeRepositoryIntegrationTest {

    @Autowired
    private EmployeRepository employeRepository;

    @Test
    void testAvgPerformanceWhereMatriculeStartsWith(){

        employeRepository.save(new Employe("Dean", "John", "T12345", LocalDate.now(), 1500d, 1, 1.0));
        employeRepository.save(new Employe("Joe", "Dohn", "M55555", LocalDate.now(), 1500d, 1, 1.0));
        employeRepository.save(new Employe("Doe", "John", "C77777", LocalDate.now(), 1500d, 1, 1.0));

        Double avgPerf = employeRepository.avgPerformanceWhereMatriculeStartsWith("C");

        Assertions.assertThat(avgPerf).isEqualTo(1);
    }

    @BeforeEach
    @AfterEach
    public void purgeBdd(){
        employeRepository.deleteAll();
    }
}
