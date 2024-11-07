package com.company;

import java.io.Serializable;
import java.util.Objects;
/**

 *  Η κλάση αυτή αναπαριστά έναν άνθρωπο μέσα στην προσομοίωση και τα πεδία αναπαριστούν αν νοσεί αυτήν την στιγμή, αν βρίσκεται στο νοσοκομείο, την ηλικία του,  καθώς και το εμβόλιο.
 *  Ακόμη υπάρχει ένα πεδίο responsible το οποίο πρόκειτε για υπεύθυνα ατομα  και σε περιπτώση που νοσήσουν  δεν μετακινούνται και δεν μεταδίδουν τον ιό. Ακόμη η κλάση υποστηρίζει
 *  συμπεριφορές κίνησης, μόλυνσης και νοσηλείας, βάση της ηλικίας και του εμβολίου υπολογίζονται και είναι διαθέσιμα στο υπόλοιπο πρόγραμμα οι πιθανότητες που έχει αυτό το άτομο να
 *  νοσήσει, πεθάνει ή μολύνει. Η κλάση αυτή κάνει implement την διεπαφή serializable και υποστηρίζει μόνιμη αποθήκευση.
 *  @author Ελένη Τσερμεντσέλη
 *  @version 1
 */

public class Person implements Serializable {

    private static final long serialVersionUID = 3L;
    private int x, y, age, daysOfInfection = 0, daysOfHospitalization = 0;
    private boolean infected, hospitalized;
    private boolean responsible;
    private Vaccine vaccine;
    private StatisticsByAge group;


    /**
     * Ο κατασκευαστής της κλάσης, δέχεται αρχική τοποθεσία ατόμου, ηλικία με την οποία τον κατηγοριοποιεί στο  κατάλληλο group.
     * Κάποιοι είναι εξαρχής μολυσμένοι με αποτελέσμα να  αρχικοποιούνται τα πέδια εμβόλιο και υπεθυνότητα.
     *
     * @param x           Η οριζόντια συντεταγμένη του συγκεκριμένου ατόμου.
     * @param y           Η κάθετη συντεταγμένη.
     * @param age         Η ηλικία.
     * @param inf         Έαν είναι μολυσμένος.
     * @param responsible Έαν προσέχει και δεν μεταδίδει.
     * @param v           Αρχικοποίηση εμβολίου σε NOTVACCINATED.
     */

    public Person(int x, int y, int age, boolean inf, boolean responsible, Vaccine v) {
        this.x = x;
        this.y = y;
        this.age = age;
        this.infected = inf;
        if (infected) {
            daysOfInfection = 14;
        }
        this.responsible = responsible;
        this.vaccine = v;
        if (age <= 17)
            group = StatisticsByAge.GROUP00TO17;
        else if (age <= 39)
            group = StatisticsByAge.GROUP18TO39;
        else if (age <= 64)
            group = StatisticsByAge.GROUP40TO64;
        else
            group = StatisticsByAge.GROUP65PLUS;
    }

    /**
     * Δημιουργούμε έναν  copy constructor.
     *
     * @param a Το αντικείμενο που θα αντιγραφεί.
     */
    public Person(Person a) {
        this.x = a.getX();
        this.y = a.getY();
        this.infected = a.isInfected();
        this.daysOfInfection = a.daysOfInfection;
        this.responsible = a.responsible;
        this.age = a.age;
        this.vaccine = a.vaccine;
        this.group = a.group;
    }

    /**
     * Η συνάρτηση αυτή υποστηρίζει την συμπεριφορά ενός ανθρώπου να κινείται δέχεται δηλαδή συντεταγμένες και αλλάζει η τοποθεσία
     * του ατόμου και αν έχει προηγουμένως μολυνθεί μειώνεται κατά μιά μέρα το υπόλοιπο της ασθένειας του. Ακόμη αν μηδενιστούν οι
     * μέρες τότε πάυει το άτομο να είναι μολυσμένο. Το ίδιο συμβαίνει και αν βρίσκεται στο νοσοκομείο. Τέλος εάν το άτομο αυτό νοσεί
     * και έχει το πεδίο responsible ως αληθές τότε δεν κινείται επειδή βρίσκεται σε καραντίνα.
     *
     * @param x Η νέα οριζόντια συντεταγμένη.
     * @param y Η νέα κάθετη συντεταγμένη.
     * @return Έαν το άτομο όντως κινήθηκε, για περιπτώσεις που στέλνουμε ίδιες συντεταγμένες με εκεί που ήταν ή νοσεί και είναι responsible.
     */
    public boolean move(int x, int y) {
        if (infected) {
            --daysOfInfection;
            if (daysOfInfection == 0)
                infected = false;
        }
        if (hospitalized) {
            --daysOfHospitalization;
            if (daysOfHospitalization == 0)
                hospitalized = false;
        }
        if ((infected && responsible) || (this.x == x && this.y == y))
            return false;
        this.x = x;
        this.y = y;
        return true;
    }


    /**
     * Η συγκρεκριμένη  συνάρτηση απλά μολύνει το άτομο και θέτει της μέρες που θα νοσεί σε 14.
     */
    public void infect() {
        infected = true;
        daysOfInfection = 14;
    }

    /**
     * Αυτή η συνάρτηση θέτει ανάλογα την παράμετρο του πεδίου  εμβόλιο στο άτομο και αλλάζουν οι πιθανότητες που έχει
     * για νόσο και βαρία ασθένεια.
     *
     * @param x Ο αριθμός του εμβολίου που θα κάνει.
     */

    public void vaccinate(int x) {
        switch (x) {
            case 1:
                vaccine = Vaccine.JOHNSON;
                break;
            case 2:
                vaccine = Vaccine.MODENA;
                break;
            case 3:
                vaccine = Vaccine.PFIZER;
                break;
            case 4:
                vaccine = Vaccine.ASTRAZANECA;

        }
    }

    /**
     * Η συνάρτηση αυτή θέτει αληθή την νοσηλεία και τις μέρες που θα διαρκέσει  με μέγιστο όριο 10 ημερών.
     */
    public void hospitalize() {
        hospitalized = true;
        daysOfHospitalization = 10;
    }

    /**
     * Η συνάρτηση αυτή επιστρέφει τις πιθανότητες ενός ατόμου να προσβληθεί από τον ιό εάν έρθει σε επαφή με άτομο που νοσεί
     * και υπολογίζεται ως 100% επί το χαρακτηριστικό του εμβολίου (το οποίο έχει τεθεί ως 1 για ανεμβολίαστους και >1 για τα υπόλοιπα εμβόλια).
     *
     * @return η Πιθανότητα του ατόμου να κολλήσει τον ιό.
     */
    public double chanceToGetInfected() {
        return 100 * vaccine.getInfectionRateMinimization();
    }

    /**
     * Αυτή η συνάρτηση υπολογίζει κάθε στιγμή την πιθανότητα ενός ατόμου να πεθάνει από τον ιό.
     *
     * @return Υπολογίζεται ως 0.1 * τις πιθανότητες του ηλικιακού group * την επίδραση του εμβολίου.
     */
    public double chanceOfDeath() {
        return 0.1 * group.getPercentageOfDeath() * vaccine.getDeathRateMinimization();
    }

    /**
     * Αυτή η συνάρτηση υπολογίζει κάθε στιγμή την πιθάνοτητα ενός ατόμου να νοσήσει βαρία και να νοσηλευτεί.
     *
     * @return Υπολογίζεται όπως και οι πιθανότητες θανάτου.
     */
    public double chanceOfHospitalization() {
        return 0.1 * group.getPercentageOfHospitalization() * vaccine.getHospitalizationMinimization();
    }

    /**
     * @return Το πεδίο νοσηλείας του ατόμου.
     */
    public boolean isHospitalized() {
        return hospitalized;
    }


    /**
     * @return Τις ημέρες νόσησης που έχουν απομείνει στο συγκεκριμένο άτομο.
     */
    public int getDaysOfInfection() {
        return daysOfInfection;
    }


    /**
     * @return Η τιμή του πεδίου responsible, εάν δηλαδή  μεταδίδει ή όχι.
     */
    public boolean isResponsible() {
        return responsible;
    }

    /**
     * @return Το συγκεκριμένο εμβόλιο που έχει το άτομο.
     */
    public Vaccine getVaccine() {
        return vaccine;
    }

    /**
     * @return Το ηλικιακό group του ατόμου.
     */
    public StatisticsByAge getGroup() {
        return group;
    }


    /**
     * @return Η οριζόντια συντεταγμένη του ατόμου.
     */
    public int getX() {
        return x;
    }


    /**
     * @return Η κάθετη συντεταγμένη του ατόμου.
     */
    public int getY() {
        return y;
    }


    /**
     * @return Η ηλικία του ατόμου.
     */
    public int getAge() {
        return age;
    }

    /**
     * @return Εάν το άτομο νοσεί αυτήν την στιγμή.
     */
    public boolean isInfected() {
        return infected;
    }

    /**
     * Μία απλή equals που ελέγχει όλα τα πεδία του ατόμου και μόνο τότε χρήζει δύο άτομα ως ίδια.
     *
     * @param o Το άτομο που συγκρίνεται.
     * @return Έαν τα αντικείμενα είναι ίσα.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return x == person.x && y == person.y && age == person.age && infected == person.infected && responsible == person.responsible && vaccine == person.vaccine;
    }

    /**
     * @return Επιστρέφει ένα hashcode το οποίο βασίζεται μόνο στις συντεταγμένες του ατόμου έτσι ώστε δύο άτομα με ίδιες
     * συντεταγμένες να έχουν ίδιο hashcode.
     */
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}