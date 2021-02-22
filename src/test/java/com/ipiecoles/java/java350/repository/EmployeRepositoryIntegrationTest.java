package com.ipiecoles.java.java350.repository;

import com.ipiecoles.java.java350.model.Employe;
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

        employeRepository.save(new Employe("Dean", "John", "C12345", LocalDate.now(), 1500d, 3, 1.0));
        employeRepository.save(new Employe("Joe", "Dohn", "C55555", LocalDate.now(), 1500d, 5, 1.0));
        employeRepository.save(new Employe("Doe", "John", "C77777", LocalDate.now(), 1500d, 1, 1.0));
        employeRepository.save(new Employe("Doe", "John", "C66666", LocalDate.now(), 1500d, 3, 1.0));
        employeRepository.save(new Employe("Doe", "John", "M12353", LocalDate.now(), 1500d, null, 1.0));

        Double avgPerfC = employeRepository.avgPerformanceWhereMatriculeStartsWith("C");
        Double avgPerfM = employeRepository.avgPerformanceWhereMatriculeStartsWith("M");

        Assertions.assertThat(avgPerfC).isEqualTo(3);
        Assertions.assertThat(avgPerfM).isNull();
    }

    @BeforeEach
    @AfterEach
    public void purgeBdd(){
        employeRepository.deleteAll();
    }
}
