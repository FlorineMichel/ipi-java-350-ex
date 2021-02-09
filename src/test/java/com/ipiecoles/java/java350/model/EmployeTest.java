package com.ipiecoles.java.java350.model;

import com.ipiecoles.java.java350.model.Employe;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class EmployeTest {

    @Test
    public void testGetNombreAnneeAncienneteInfNow() {
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
    public void testGetNombreAnneeAncienneteSupNow() {
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
    public void testGetNbAnneeAncienneteDateEmbaucheNull(){
        //given
        Employe employe = new Employe();
        employe.setDateEmbauche(null);

        //when
        Integer anneeAnciennete = employe.getNombreAnneeAnciennete();

        //then négative, 0, null
        Assertions.assertThat(anneeAnciennete).isNull();
    }

    @Test
    public void testGetNbAnneeAncienneteDateEmbaucheNow(){
        //Given
        Employe employe = new Employe("Doe", "John", "T12345",
                LocalDate.now(), 1500d, 1, 1.0);
        //When
        Integer anneeAnciennete = employe.getNombreAnneeAnciennete();
        //Then
        Assertions.assertThat(anneeAnciennete).isEqualTo(0);
    }

    /*données d'entrée
    tempsPartiel = pourcentage d'activité
    poste = Manger ou autre
    performance
    date d'embauche
     */
    @Test
    public void testGetPrimeAnnuelDateEmbaucheNull(){
        Employe employe = new Employe();
        employe.setDateEmbauche(null); 

        Double primeAnnuelle = employe.getPrimeAnnuelle();

        Assertions.assertThat(primeAnnuelle).isLessThan(0);
    }

}
