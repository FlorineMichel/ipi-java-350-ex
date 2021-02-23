package com.ipiecoles.java.java350.repository;

import com.ipiecoles.java.java350.model.Employe;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

//@ExtendWith(SpringExtension.class)
//@ContextConfiguration(classes = {Java350Application.class})
//@DataJpaTest
@SpringBootTest
class EmployeRepositoryTest {
    @Autowired
    EmployeRepository employeRepository;

    @Test
    public void testFindLastMatricule1Employe(){
        //Given
        //Insérer des données en base
        employeRepository.save(new Employe("Doe", "John", "T12345", LocalDate.now(), 1500d, 1, 1.0));
        //When
        //Exécuter des requêtes en base
        String lastMatricule = employeRepository.findLastMatricule();
        //Then
        Assertions.assertThat(lastMatricule).isEqualTo("12345");
    }

    @Test
    public void testFindLastMatriculeNull(){
        //Given
        //When
        //Exécuter des requêtes en base
        String matricule = employeRepository.findLastMatricule();
        //Then
        Assertions.assertThat(matricule).isNull();
    }

//    @ParameterizedTest(name = "matricule : {0}")
//    @CsvSource({
//            "'T12345', '12345'",
//            "'T66666', '66666'",
//            "'T77777', '77777'"
//    })
    @Test
    public void testFindLastMatricule(){
        //Given
        employeRepository.save(new Employe("Jean", "John", "T12345", LocalDate.now(), 1500d, 1, 1.0));
        employeRepository.save(new Employe("Joe", "Dohn", "M55555", LocalDate.now(), 1500d, 1, 1.0));
        employeRepository.save(new Employe("Doe", "John", "C77777", LocalDate.now(), 1500d, 1, 1.0));

        //When
        //Exécuter des requêtes en base
        String lastMatricule = employeRepository.findLastMatricule();

        //Then
        Assertions.assertThat(lastMatricule).isEqualTo("77777");
    }

    @BeforeEach
    @AfterEach
    public void purgeBdd(){
        employeRepository.deleteAll();
    }
}