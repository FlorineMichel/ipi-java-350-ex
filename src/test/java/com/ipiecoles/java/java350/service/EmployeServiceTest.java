package com.ipiecoles.java.java350.service;

import com.ipiecoles.java.java350.exception.EmployeException;
import com.ipiecoles.java.java350.model.Employe;
import com.ipiecoles.java.java350.model.NiveauEtude;
import com.ipiecoles.java.java350.model.Poste;
import com.ipiecoles.java.java350.repository.EmployeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityExistsException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EmployeServiceTest {

    @InjectMocks
    private EmployeService employeService;

    @Mock
    private EmployeRepository employeRepository;

    @Test
    public void testEmbaucheLimiteMatricule() throws EmployeException {
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
//        Employe employe = null;
//        try {
//            employe = employeService.embaucheEmploye(nom, prenom, poste, niveauEtude, tempsPartiel);
//            Assertions.fail("embauche employe aurait du lancer une exception");
//        } catch (EmployeException e){
//            //then
//            Assertions.assertThat(e.getMessage()).isEqualTo("Limite des 100000 matricules atteinte !");
//        }

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
    public void testEmbaucheEmployeExisteDeja() throws EmployeException {
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
        } catch (EmployeException e){
            //then
            Assertions.assertThat(e).isInstanceOf(EntityExistsException.class);
            Assertions.assertThat(e.getMessage()).isEqualTo("L'employé de matricule \" + matricule + \" existe déjà en BDD");
        }
    }

    @Test
    public void testEmbauchePremierEmploye() throws EmployeException {
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
}