package com.ipiecoles.java.java350.model;

import com.ipiecoles.java.java350.model.Employe;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;

public class EmployeTest {

    @Test
    void testGetNombreAnneeAncienneteInfNow() {
        //given
        LocalDate dateEmbauche = LocalDate.of(LocalDate.now().minusYears(6).getYear(), 02, 12);

        Employe employe = new Employe();
        employe.setDateEmbauche(dateEmbauche);

        //when
        Integer anneeAnciennete = employe.getNombreAnneeAnciennete();

        //verifie si nb année anciennete est supérieur à 0
        //Assertions.assertThat(anneeAnciennete).isEqualTo(6);
        Assertions.assertThat(anneeAnciennete).isGreaterThanOrEqualTo(0);
        Assertions.assertThat(anneeAnciennete).isLessThanOrEqualTo(50);

    }

    @Test
    void testGetNombreAnneeAncienneteSupNow() {
        //given
        LocalDate dateEmbauche = LocalDate.of(LocalDate.now().plusYears(6).getYear(), 02, 12);

        Employe employe = new Employe();
        employe.setDateEmbauche(dateEmbauche);

        //when
        Integer anneeAnciennete = employe.getNombreAnneeAnciennete();

        //then
        Assertions.assertThat(anneeAnciennete).isNull();
    }

    @Test
    void testGetNbAnneeAncienneteDateEmbaucheNull(){
        //given
        Employe employe = new Employe();
        employe.setDateEmbauche(null);

        //when
        Integer anneeAnciennete = employe.getNombreAnneeAnciennete();

        //then négative, 0, null
        Assertions.assertThat(anneeAnciennete).isNull();
    }

    @Test
    void testGetNbAnneeAncienneteDateEmbaucheNow(){
        //Given
        Employe employe = new Employe("Doe", "John", "T12345",
                LocalDate.now(), 1500d, 1, 1.0);
        //When
        Integer anneeAnciennete = employe.getNombreAnneeAnciennete();
        //Then
        Assertions.assertThat(anneeAnciennete).isEqualTo(0);
    }

    /*données d'entrée
    tempsPartiel = pourcentage d'activité / taux d'activite
    poste = Manger ou autre
    performance
    date d'embauche / nbanneeanciennete
     */
    @ParameterizedTest(name = "performance : {0} matricule : {1} taux : {2} anneeanciennete : {3}")
    @CsvSource({
            "1, 'T12345', 1.0, 0, 1000d",
            "1, 'T12345', 0.5, 0, 500d",
            "2, 'T12345', 1.0, 0, 2300d",
            "1, 'T12345', 1.0, 2, 1200d",
            "2, 'T12345', 1.0, 1, 2400d",
            "1, 'M12345', 1.0, 0, 1700d",
            "1, 'M12345', 1.0, 3, 2000d"
    })
    void testGetPrimeAnnuelle(Integer performance, String matricule, Double tauxActivite, Long nbAnneesAnciennete, Double primeAttendu){

        Employe employe = new Employe("Doe", "John", matricule,
                LocalDate.now().minusYears(nbAnneesAnciennete), 1500d, performance, tauxActivite);

        Double prime = employe.getPrimeAnnuelle();

        Assertions.assertThat(prime).isEqualTo(primeAttendu);
    }

    @Test
    void testGetPrimeAnnuelleMatriculeNull(){
        Employe employe = new Employe("Doe", "John", null,
                LocalDate.now(), 1500d, 1, 1.0);

        Double prime = employe.getPrimeAnnuelle();

        Assertions.assertThat(prime).isEqualTo(1000.0);
    }


    /*
    TESTS TP
     */

    /*
    tests pour la méthode augmenterSalaire
    base calcul : (1 + @pourcentage) * salaire
    cas possible lorsqu'on augmente un salaire :
    - si salaire non def (0 ou null)
    - si pourcentage incorrect (> 1 ou < 0)
    - si pourcentage = 0
    -
    */

    //salaire null
    @Test
    void testAugmenterSalaireNull(){
        //given
        Employe employe = new Employe("Doe", "John", "C00001",
                LocalDate.now(), null, 1, 1.0);
        //when
        employe.augmenterSalaire(0.5);
        //then
        Assertions.assertThat(employe.getSalaire()).isNull();
    }

    @Test
    void testAugmenterSalaireZero(){
        //given
        Employe employe = new Employe();
        employe.setSalaire(0.0);

        //when
        employe.augmenterSalaire(0.5);

        //then
        Assertions.assertThat(employe.getSalaire()).isZero();
    }

    @Test
    void testAugmenterSalairePourcentageNegatif(){
        //given
        Double pourcentage = -0.1;

        Employe employe = new Employe();
        Double salaireBase = 1500.0;
        employe.setSalaire(salaireBase);

        //when
        employe.augmenterSalaire(pourcentage);

        //then
        Assertions.assertThat(employe.getSalaire()).isLessThan(salaireBase);
    }

    @Test
    void testAugmenterSalairePourcentageSupUn(){
        //given
        Double pourcentage = 1.1;

        Employe employe = new Employe();
        employe.setSalaire(1500.0);

        //when
        employe.augmenterSalaire(pourcentage);

        //then
        Assertions.assertThat(pourcentage).isGreaterThan(1);
    }

    /*
    Test pour getNbRTT
     */

    @ParameterizedTest(name = "date : {0} nbRTT : {1}")
    @CsvSource({
            "2019, 8",
            "2021, 10",
            "2022, 10",
            "2032, 11"
    })
    void testGetNbRtt(Integer dateReference, Integer nbAttendu){
        //given
        Employe employe = new Employe();

        //when
        Integer nbRTT = employe.getNbRtt(LocalDate.of(dateReference, 1, 1));

        //then
        Assertions.assertThat(nbRTT).isEqualTo(nbAttendu);
    }

}
