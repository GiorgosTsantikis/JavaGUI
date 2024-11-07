package com.company;

/**
 * Αυτό το enum παρέχει χρήσιμα στατιστικά και πιθανότητες για να τρέχει σωστά η προσομοίωση και να λειτουργούν οι θάνατοι, μολύνσεις και οι νοσηλείες με επιθυμητό τρόπο,
 * να είναι ελεγχόμενες και όσο το δυνατόν πιό ρεαλιστικές. Το enum αφορά της ηλικίες των ατόμων και τα ποσοστά ανά ηλικιακό group, τις αντίστοιχες πιθανότητες θανάτου
 * και νοσηλείας.
 * @author Ελένη Τσερμεντσέλη
 * @version 1
 *Δημογραφικά στατιστικά Ελλάδας https://www.statistics.gr/documents/20181/1210503/A1602_SAM01_DT_DC_00_2011_03_F_GR.pdf/e1ac0b1c-8372-4886-acb8-d00a5a68aabe
 *Θάνατοι από covid στην Ελλάδα  https://covid19.gov.gr/covid19-live-analytics/
 *Στατιστικά ηλικιών που νοσηλέυονται https://gis.cdc.gov/grasp/covidnet/COVID19_5.html
 */

public enum StatisticsByAge {
    /**
     * Ορίζουμε 4 group όπως και οι περισσότερες σελίδες και οργανισμοί που παρακολουθούν τα στατιστικά της πανδημίας.
     */
    GROUP65PLUS(0.213, 0.1629, 0.476, 65, 100),
    GROUP40TO64(0.333, 0.1111, 0.351, 40, 64),
    GROUP18TO39(0.267, 0.05, 0.153, 18, 39),
    GROUP00TO17(0.187, 0, 0.02, 0, 17);

    public final double percentageOfPopulation, percentageOfDeath, percentageOfHospitalization;
    private final int lowerBound, upperBound;


    /**
     * Ο κατασκευαστής του enum για πιθανές διαφοροποιήσεις στα ηλικιακά group, προσθήκη νέων.
     *
     * @param percOfPopulation Ποσοστό του πληθυσμού που ανήκει στο group
     * @param deathPerc        Πιθανότητα για θάνατο ενός ατόμου στο group
     * @param hospPerc         Ποσοστό νοσηλείας του group
     * @param low              Κάτω όριο ηλικίας
     * @param high             Άνω όριο ηλικίας
     */
    StatisticsByAge(double percOfPopulation, double deathPerc, double hospPerc, int low, int high) {
        this.percentageOfPopulation = percOfPopulation;
        this.percentageOfDeath = deathPerc;
        this.percentageOfHospitalization = hospPerc;
        this.lowerBound = low;
        this.upperBound = high;


    }

    /**
     * @return Επιστρέφει το άνω όριο
     */
    public int getUpperBound() {
        return upperBound;
    }

    /**
     * @return Επιστρέφει το κάτω όριο.
     */
    public int getLowerBound() {
        return lowerBound;
    }

    /**
     * @return Επιστρέφει την πιθανότητα θανάτου ενός ατόμου βάση του group.
     */
    public double getPercentageOfDeath() {
        return percentageOfDeath;
    }

    /**
     * @return Ποσοστό ατόμων που νοσηλέυονται ανα group.
     */
    public double getPercentageOfHospitalization() {
        return percentageOfHospitalization;
    }

    /**
     * @return Ποσοστό πληθυσμού που ανήκει στο group.
     */
    public double getPercentageOfPopulation() {
        return percentageOfPopulation;
    }
}