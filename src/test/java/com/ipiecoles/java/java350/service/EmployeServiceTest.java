package com.ipiecoles.java.java350.service;

import com.ipiecoles.java.java350.exception.EmployeException;
import com.ipiecoles.java.java350.model.Employe;
import com.ipiecoles.java.java350.model.NiveauEtude;
import com.ipiecoles.java.java350.model.Poste;
import com.ipiecoles.java.java350.repository.EmployeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityExistsException;
import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
class EmployeServiceTest {

    @InjectMocks
    private EmployeService employeService;

    @Mock
    private EmployeRepository employeRepository;

    private static Logger logger = LoggerFactory.getLogger(EmployeServiceTest.class);
    
    @Test
    void testEmbaucheLimiteMatricule() throws EmployeException {
        //given
        String nom = "Doe";
        String prenom = "Jojo";
        Poste poste = Poste.TECHNICIEN;
        NiveauEtude niveauEtude = NiveauEtude.BTS_IUT;
        Double tempsPartiel = 1.0;
        //simuler qu'aucun employé n'est présent (ou du moins aucun matricule)
        //Mockito.when(employeRepository.findLastMatricule()).thenReturn(null);
        //simuler que la recherche par matricule ne renvoie pas de résultats
        //Mockito.when(employeRepository.findByMatricule(Mockito.anyString())).thenReturn(null);
        //simuler un matricule qui franchi la limite de numéro autorisé
        Mockito.when(employeRepository.findLastMatricule()).thenReturn("99999");

        //when
        Employe employe = null;
        try {
            employeService.embaucheEmploye(nom, prenom, poste, niveauEtude, tempsPartiel);
            Assertions.fail("embauche employe aurait du lancer une exception");
        } catch (EmployeException e){
            //then
            Assertions.assertThat(e.getMessage()).isEqualTo("Limite des 100000 matricules atteinte !");
        }

        //then
//        Assertions.assertThat(employe).isNotNull();
//        Assertions.assertThat(employe.getNom()).isEqualTo(nom);
//        Assertions.assertThat(employe.getPrenom()).isEqualTo(prenom);
//        Assertions.assertThat(employe.getSalaire()).isEqualTo(1825.46);
//        Assertions.assertThat(employe.getTempsPartiel()).isEqualTo(1.0);
//        Assertions.assertThat(employe.getDateEmbauche()).isEqualTo(LocalDate.now());
//        Assertions.assertThat(employe.getMatricule()).isEqualTo("T00001");
    }

    @Test
    void testEmbaucheEmployeExisteDeja() {
        //given
        String nom = "Doe";
        String prenom = "Jojo";
        Poste poste = Poste.TECHNICIEN;
        NiveauEtude niveauEtude = NiveauEtude.BTS_IUT;
        Double tempsPartiel = 1.0;
        Employe employeExistant = new Employe("Doe", "Joe", "T00001", LocalDate.now(), 1500d, 1, 1.0);
        //simuler qu'aucun employé n'est présent (ou du moins aucun matricule)
        Mockito.when(employeRepository.findByMatricule("T00001")).thenReturn(employeExistant);

        //when
        try {
            employeService.embaucheEmploye(nom, prenom, poste, niveauEtude, tempsPartiel);
            Assertions.fail("embauche employe aurait du lancer une exception");
        } catch (Exception e){
            //then
            logger.error(e.getMessage());
            Assertions.assertThat(e).isInstanceOf(EntityExistsException.class);
            Assertions.assertThat(e.getMessage()).isEqualTo("L'employé de matricule T00001 existe déjà en BDD");
        }
    }

    @Test
    void testEmbauchePremierEmploye() throws EmployeException {
        //given
        String nom = "Doe";
        String prenom = "Jojo";
        Poste poste = Poste.TECHNICIEN;
        NiveauEtude niveauEtude = NiveauEtude.BTS_IUT;
        Double tempsPartiel = 1.0;
        //Simuler qu'aucun employé n'est présent (ou du moins aucun matricule)
        Mockito.when(employeRepository.findLastMatricule()).thenReturn(null);
        //Simuler que la recherche par matricule ne renvoie pas de résultats
        Mockito.when(employeRepository.findByMatricule("T00001")).thenReturn(null);

        //when
        employeService.embaucheEmploye(nom, prenom, poste, niveauEtude, tempsPartiel);

        //then
        //permet de créer un objet employe meme si retour methode est void
        ArgumentCaptor<Employe>employeArgumentCaptor = ArgumentCaptor.forClass(Employe.class);
        Mockito.verify(employeRepository).save(employeArgumentCaptor.capture());
        Employe employe = employeArgumentCaptor.getValue();

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

    /*
    tests CalculPerformanceCommercial
    - cas normal : renvoyer performances attendus pour les 5 types de cas + perf ind > avg
    - matricule est null ou ne commence pas par un C : renvoyer une exception
    - caObjectif / caTraite null ou zero : renvoyer exception
    - le matricule n'existe pas : renvoyer exception
     */
//    @ParameterizedTest
//    @CsvSource({
//            "10, 10000, 4, 1", /* 1 : Si le chiffre d'affaire est inférieur de plus de 20% à l'objectif fixé, le commercial retombe à la performance de base */
//            "10, 100, 4, 2", /* 2 : Si le chiffre d'affaire est inférieur entre 20% et 5% par rapport à l'ojectif fixé, il perd 2 de performance (dans la limite de la performance de base) */
//            "1000, 1000, 1, 1", /* 3 : Si le chiffre d'affaire est entre -5% et +5% de l'objectif fixé, la performance reste la même.*/
//            "100, 10, 1, 2", /* 4 : Si le chiffre d'affaire est supérieur entre 5 et 20%, il gagne 1 de performance*/
//            "10000, 10, 1, 5", /* 5 : Si le chiffre d'affaire est supérieur de plus de 20%, il gagne 4 de performance */
//            "1000, 1000, 1, 2" /* Cas avec n°3 et perf individuel > AVG */
//    })
//    void testCalculPerformanceCommercial(Long caTraite, Long objectifCa, Integer perfBase, Integer perfAttendu) throws EmployeException {
//        //given
//        String nom = "Doe";
//        String prenom = "Jojo";
//        Poste poste = Poste.COMMERCIAL;
//        NiveauEtude niveauEtude = NiveauEtude.BTS_IUT;
//        Double tempsPartiel = 1.0;
//
//        Employe employe;
//        String matricule  = "C00001";
//        Mockito.when(employeRepository.findByMatricule(matricule)).thenReturn(
//                employe = new Employe("Doe", "Joe", matricule, LocalDate.now(), 1500d, perfBase, 1.0)
//        );
//
//        //Integer performanceBase = employe.getPerformance();
//
//        //when
//        employeService.calculPerformanceCommercial(employe.getMatricule(),caTraite, objectifCa);
//        //then
//
//        //+1 parce que + avg selon les performances des autres commerciaux
//        Assertions.assertThat(employe.getPerformance()).isEqualTo(perfAttendu);
//    }

    @ParameterizedTest
    @CsvSource({
            ",",
            "T00001"
    })
    void testCalculPerformanceCommercialMatriculeNullOrNotBeginWithC(String matricule) {
        //given
        String nom = "Doe";
        String prenom = "Jojo";
        Poste poste = Poste.TECHNICIEN;
        NiveauEtude niveauEtude = NiveauEtude.BTS_IUT;
        Double tempsPartiel = 1.0;

        Employe employe = new Employe("Doe", "Joe", matricule, LocalDate.now(), 1500d, 1, 1.0);

        Long caTraite = 1000L;
        Long objectifCa = 1000L;

        //when
        try {
            employeService.calculPerformanceCommercial(employe.getMatricule(),caTraite, objectifCa);
            Assertions.fail("La méthode calculPerformanceCommercial doit lancer une exception");
        } catch (EmployeException e) {
            //Then
            Assertions.assertThat(e.getMessage()).isEqualTo("Le matricule ne peut être null et doit commencer par un C !");
        }
    }

    @Test
    void testCalculPerformanceCommercialMatriculeDoNotExist() {
        //given
        String matricule = "C12345";

        Long caTraite = 1000L;
        Long objectifCa = 1000L;

        //when
        try {
            employeService.calculPerformanceCommercial(matricule,caTraite, objectifCa);
            Assertions.fail("La méthode calculPerformanceCommercial doit lancer une exception");
        } catch (EmployeException e) {
            //Then
            Assertions.assertThat(e.getMessage()).isEqualTo("Le matricule C12345 n'existe pas !");
        }
    }

    @ParameterizedTest
    @CsvSource({
            ",",
            "-1000"
    })
    void testCalculPerformanceCommercialObjectifCaNullOrNegatif(Long objectifCa){
        //given
        String nom = "Doe";
        String prenom = "Jojo";
        Poste poste = Poste.TECHNICIEN;
        NiveauEtude niveauEtude = NiveauEtude.BTS_IUT;
        Double tempsPartiel = 1.0;

        Employe employe = new Employe("Doe", "Joe", "C00001", LocalDate.now(), 1500d, 1, 1.0);

        Long caTraite = 1000L;

        //when
        try {
            employeService.calculPerformanceCommercial(employe.getMatricule(),caTraite, objectifCa);
            Assertions.fail("La méthode calculPerformanceCommercial doit lancer une exception");
        } catch (EmployeException e) {
            //Then
            Assertions.assertThat(e.getMessage()).isEqualTo("L'objectif de chiffre d'affaire ne peut être négatif ou null !");
        }
    }

    @ParameterizedTest
    @CsvSource({
            ",",
            "-1000"
    })
    void testCalculPerformanceCommercialCaTraiteNullOrNegatif(Long caTraite ){
        //given
        String nom = "Doe";
        String prenom = "Jojo";
        Poste poste = Poste.TECHNICIEN;
        NiveauEtude niveauEtude = NiveauEtude.BTS_IUT;
        Double tempsPartiel = 1.0;

        Employe employe = new Employe("Doe", "Joe", "C00001", LocalDate.now(), 1500d, 1, 1.0);

        Long objectifCa = 1000L;

        //when
        try {
            employeService.calculPerformanceCommercial(employe.getMatricule(),caTraite, objectifCa);
            Assertions.fail("La méthode calculPerformanceCommercial doit lancer une exception");
        } catch (EmployeException e) {
            //Then
            Assertions.assertThat(e.getMessage()).isEqualTo("Le chiffre d'affaire traité ne peut être négatif ou null !");
        }
    }
}