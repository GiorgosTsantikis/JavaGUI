package com.company;

/**
 * Αυτό το enum αναπαριστά τα εμβόλια που υπάρχουν διαθέσιμα στο πρόγραμμα και αντιστοιχούν σε αυτά που έχουμε στην πραγματικότητα
 * με την σχετική τους αποτελεσματικότητα. Κάθε εμβόλιο μείωνει τις πιθανότητες να νοσήσει, να νοσηλευθεί και να πεθάνει από τον ιό ένας άνθρωπος.
 * Εκτός φυσικά από το NOTVACCINATED που υπάρχει για αρχικοιποίηση και επιστρέφει 1 σε όλα τα πεδία δηλαδή δεν επηρεάζει καθόλου τις πιθανότητες.
 * Πληροφορίες για τα εμβόλια από https://www.yalemedicine.org/news/covid-19-vaccine-comparison
 * @author Ελένη Τσερμεντσέλη
 * @version 1
 */
public enum Vaccine {
    NOTVACCINATED("NOT",1,1,1),
    ASTRAZANECA("Astra",0,0.15,0.24),
    JOHNSON("J&J",0.05,0.29,0.28),
    PFIZER("PF",0.05,0.16,0.087),
    MODENA("MOD",0.05,0.1,0.059);


    private double deathRateMinimization,hospitalizationMinimization,infectionRateMinimization;
    private String name;

    /**
     * Ο κατασκευαστής του enum, συμβάλει ώστε να μπορούν να προστεθούν νέα εμβόλια στο μέλλον με βάση το ορισμένο πρότυπο.
     * @param name Το όνομα του εμβολίου.
     * @param death Κατά πόσο μειώνει την πιθανότητα θανάτου.
     * @param hospital Κατά πόσο μειώνει την πιθανότητα νοσηλείας.
     * @param infection Κατά πόσο μειώνει την πιθανότητα νόσου.
     */
    Vaccine(String name,double death,double hospital,double infection){
        this.name=name;
        this.deathRateMinimization=death;
        this.hospitalizationMinimization=hospital;
        this.infectionRateMinimization=infection;
    }
    /**
     * @return Το όνομα του εμβολίου.
     */
    public String getName() {
        return name;
    }

    /**
     * @return Μείωση πιθανότητας θανάτου.
     */
    public double getDeathRateMinimization() {
        return deathRateMinimization;
    }
    /**
     * @return Μείωση πιθανότητας νοσηλείας.
     */
    public double getHospitalizationMinimization() {
        return hospitalizationMinimization;
    }

    /**
     * @return Μείωση πιθανότητας νόσου.
     */
    public double getInfectionRateMinimization() {
        return infectionRateMinimization;
    }


}
