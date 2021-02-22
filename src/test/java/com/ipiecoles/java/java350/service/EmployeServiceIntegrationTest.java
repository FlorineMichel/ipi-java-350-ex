package com.ipiecoles.java.java350.service;

import com.ipiecoles.java.java350.exception.EmployeException;
import com.ipiecoles.java.java350.model.Employe;
import com.ipiecoles.java.java350.model.NiveauEtude;
import com.ipiecoles.java.java350.model.Poste;
import com.ipiecoles.java.java350.repository.EmployeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
class EmployeServiceIntegrationTest {

    @Autowired
    private EmployeService employeService;

    @Autowired
    private EmployeRepository employeRepository;

    @Test
    public void testEmbauchePremierEmploye() throws EmployeException {
        //given
        String nom = "Doe";
        String prenom = "Jojo";
        Poste poste = Poste.TECHNICIEN;
        NiveauEtude niveauEtude = NiveauEtude.BTS_IUT;
        Double tempsPartiel = 1.0;

        //when
        employeService.embaucheEmploye(nom, prenom, poste, niveauEtude, tempsPartiel);

        //then
        List<Employe> employes = employeRepository.findAll();
        Assertions.assertThat(employes).hasSize(1);
        Employe employe = employeRepository.findAll().get(0);
        Assertions.assertThat(employe.getNom()).isEqualTo(nom);
        Assertions.assertThat(employe.getPrenom()).isEqualTo(prenom);
        Assertions.assertThat(employe.getSalaire()).isEqualTo(1825.46);
        Assertions.assertThat(employe.getTempsPartiel()).isEqualTo(1.0);
        Assertions.assertThat(employe.getDateEmbauche()).isEqualTo(LocalDate.now());
        Assertions.assertThat(employe.getMatricule()).isEqualTo("T00001");
    }


    /*
    tests TP
     */
    @Test
    public void testCalculPerformanceCommercial() throws EmployeException {
        //given
        String nom = "Doe";
        String prenom = "Jojo";
        Poste poste = Poste.COMMERCIAL;
        NiveauEtude niveauEtude = NiveauEtude.BTS_IUT;
        Double tempsPartiel = 1.0;

        //when
        employeService.embaucheEmploye(nom, prenom, poste, niveauEtude, tempsPartiel);
        List<Employe> employes = employeRepository.findAll();
        Assertions.assertThat(employes).hasSize(1);
        Employe employe = employeRepository.findAll().get(0);

        Integer performanceBase = employe.getPerformance();

        employeService.calculPerformanceCommercial(employe.getMatricule(), 0L, 1000L);

        //+1 parce que + avg selon les performances des autres commerciaux. Vu que notre employe est seul, +1 au test
        Assertions.assertThat(employe.getPerformance()).isEqualTo(performanceBase);
    }
}