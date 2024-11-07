package com.company;
import java.io.Serializable;
import java.util.*;

/**
 * Αυτή η κλάση αποτελεί τον χειριστή των ανθρώπων της προσομοίωσης. Είναι υπέυθηνη για την δημιουργία του κόσμου καθώς και την εξέλιξη του. Επιπλέον
 * περιέχει, όλες τις πληροφορίες και στατιστικά, που μπορεί να φανούν χρήσιμα και τα ανανεώνει σε κάθε βήμα. Κάθε "ημέρα" στην προσομοίωση αντιστοιχεί
 * σε μία κλήση της συνάρτησης next() η οποία τρέχει την λογική της μετακινήσεως των ανθρώπων, της μόλυνσης, εμβολιασμών και βαριάς ασθένειας, ανανεώνοντας
 * παράλληλα τα στατιστικά και το log. Για την αναπαράσταση της τοποθεσίας των ανθρώπων χρησιμοποείτε ένας χάρτης με κλειδί τις συντεταγμένες ενός τετραγώνου
 * και το ArrayList με τους ανθρώπους. Μολύνσεις γίνονται μόνο μέσα στο ίδιο τετράγωνο. Η κλάση αυτή υλοιποιεί την διεπαφή Serializable και μπορεί να αποθηκευτεί
 * σε αρχείο. Αυτή η κλάση δίνεται στην διεπαφή και ξεκινάει το πρόγραμμα.
 * @author Ελένη Τσερμεντσέλη
 * @version 1
 */

public class World implements Serializable {

    private static final long serialVersionUID = 1L;
    private StringBuilder log;
    private HashMap<Integer,ArrayList<Person>>worldMap;
    private HashSet<Person> hospital;

    private int currentlyVaccinated,currentlyDead,day,maxX,population,initPopulation,vaccinationRate,initInf,upperVacLimit;
    private Random generator;

    /**
     * Ο κατασκευαστής της κλάσης world. Δέχεται ως παραμέτρους όλα τα δεδομένα που παρέχει ο χρήστης στο πρόγραμμα όταν
     * θέλει να φτιάξει μια νεά προσομείωση και αρχηκοποιεί τα πεδιά της κλάσης με βάση τα δεδομένα αυτά περνώντας τα και
     * στην συμβολοσειρά log.
     * @param population Ο συνολικός αριθμός ανθρώπων που θα υπάρχουν στο κόσμο.
     * @param vaccinationRate Ο ημερήσιος ρυθμός εμβολιασμού.
     * @param initInf Ο αριθμός των μολυσμένων στην αρχή.
     * @param size Το μέγεθος της μίας διάστασης του κόσμου (ο οποίος είναι σε κάθε περίπτωση τετράγωνος).
     * @param upperVacLimit Το ανώτατο ποσοστό του πληθυσμού που θα εμβολιαστεί (με βάση τον πληθυσμό στην αρχή).
     */
    public World(int population,int vaccinationRate,int initInf,int size,int upperVacLimit){
        worldMap=new HashMap<>();
        day=0;
        log=new StringBuilder("Starting data: Population "+population+" Initially infected "+initInf+" Vaccination rate "+vaccinationRate
                +" Upper Vaccination %"+upperVacLimit+" Size of Simulation Map: "+size+"\n");
        hospital=new HashSet<>();
        maxX=size;
        this.population=initPopulation=population;
        currentlyVaccinated=0;
        this.initInf=initInf;
        this.upperVacLimit=upperVacLimit;
        this.vaccinationRate=vaccinationRate;
        generator=new Random(System.currentTimeMillis());
    }

    /**
     *Αυτή η συνάρτηση είναι υπεύθηνη για την δημιουργία όλων των αντικειμένων person καθώς και την τοποθέτηση τους μέσα
     * στον κόσμο της προσομοίωσης. Υπάρχει μια for της οποίας ο μετρητής όταν φτάσει τον αριθμό ατόμων του ηλικιακού group
     * που έπρεπε να δημιουργηθεί με βάση το enum, προχωράει στο επόμενο και έτσι υπάρχει σωστή κατανομή ηλικιών στον κόσμο.
     * Σε κάθε επανάληψη δημιουργείται ένα νέο αντικείμενο person με τυχαία στοιχεία τα οποία όμως βρίσκονται μέσα στα επιθυμητά
     * όρια των group και τυχαίες συντεταγμένες περιορισμένες από το όριο που έδωσε ο χρήστης. Κάθε τετράγωνο του κόσμου περιλαμβάνει
     * ένα ArrayList στο οποίο υπάρχουν μέσα persons που αλληλεπιδρούν και κινδυνεύουν από μόλυνση. Τέλος αρχικοποιείται η προσομοίωση
     * με τους δωσμένους πάλι από τον χρήστη αρχικά νοσούντες.
     */



    public void createWorld() {
        ArrayList<StatisticsByAge>newBlock=new ArrayList<>();
        for(StatisticsByAge ok:StatisticsByAge.values()){
            newBlock.add(ok);
        }
        int ctr=0,low=newBlock.get(0).getLowerBound(),high=newBlock.get(0).getUpperBound(),age=0;
        for(int i=0;i<=(int)(population*newBlock.get(ctr).getPercentageOfPopulation());i++) {
            if (i == (int) (population * newBlock.get(ctr).getPercentageOfPopulation()) && ctr < newBlock.size() - 1) {
                ctr++;
                i = 0;
                low = newBlock.get(ctr).getLowerBound();
                high = newBlock.get(ctr).getUpperBound();
            }
            age = generator.nextInt(high - low) + low;
            Person p = new Person(generator.nextInt(maxX), generator.nextInt(maxX), age, false, generator.nextBoolean(), Vaccine.NOTVACCINATED);
            if (worldMap.containsKey(p.hashCode())) {
                worldMap.get(p.hashCode()).add(p);
            } else {
                ArrayList<Person> temp = new ArrayList<>();
                temp.add(p);
                worldMap.put(p.hashCode(), temp);
            }
        }

        int infCtr=0;
        while(infCtr<initInf){
            for(Integer x: worldMap.keySet()){
                for(Person p: worldMap.get(x)){
                    if(generator.nextInt(1001)==1&&infCtr<initInf&&!p.isInfected()) {
                        p.infect();
                        ++infCtr;
                    }
                }
            }
        }
    }

    /**
     * Αυτή η συνάρτηση καλείται καθημερινα στην προσομοιωση , κρατάει τον αριθμό των μολυσμένων της προηγούμενης μέρας
     * για να μπορουν να υπολογιστούν τα νέα κρούσματα, ανανεώνει το πεδίο day καθώς και την συμβολοσειρά log με τα νέα δεδομένα του
     * κόσμου, αφού καλέσει τις συναρτήσεις μετακίνησης, εμβολιασμού, αρρώστειας και νοσηλείας.
     */
    public synchronized void next(){
        int prevInf=getCurrentlyInfected();
        nextDay();
        ++day;
        infectMe();
        vaccinateMe();
        severeDisease();
        log.append("Day: "+day+"\n New infections: "+(getCurrentlyInfected()-prevInf)+" Population: "+getPopulation()+" Currently infected: "+getCurrentlyInfected()+
                " Currently Vaccinated: "+getCurrentlyVaccinated()+" % Of Vaccinated: "+getVacPerc()+" Hospitalized: "+getCurrentlyHospitalized()+" Deaths: "+getCurrentlyDead()+"\n");
    }

    /**
     * Αυτή η συνάρτηση αναπαριστά μία καινούργια μέρα στον κόσμο της προσομοίωσης. Κάθε μέρα όλοι οι άνθρωποι μετακινούνται
     * σε μία νέα τοποθεσία εάν δεν βρίσκονται στον νοσοκομείο ή τα πεδία infected και responsible δεν είναι αληθή. Για να γίνει
     * αυτή η μετακίνηση σωστά με τρόπο που δεν έχουν οι δομές πρόβλημα, για κάθε κίνηση δημιουργείτε ένα νέο άτομο με βάση τον copy
     * constructor και σε αυτό καλείτε η συνάρτηση move. Έτσι πρώτα αφαιρείται το παλιό αντικείμενο και μετά προστίθεται το νέο. Αν σε
     * εκείνο το τετραγωνάκι δεν έχει ξαναπάει άτομο, τότε δημιουργείται νέο arraylist για εκείνο το τετράγωνο το οποίο μαζί με το κλειδί
     * του τετραγώνου προστίθενται σε ένα προσωρινό hashmap για να μην δημιουργείται concurrentmodificationexception. Στο τέλος αντιγράφονται
     * τα περιεχόμενα του προσωρινού map στο κύριο worldMap.
     */

    public synchronized void nextDay(){
        int newX,newY;
        HashMap<Integer,ArrayList<Person>>temp=new HashMap<>();
        boolean wouldMove;
        Person per;
        for(Integer x:worldMap.keySet()) {
            for (int i=0;i<worldMap.get(x).size();i++) {
                per=worldMap.get(x).get(i);
                wouldMove=!per.isResponsible()&&!per.isInfected();
                if(wouldMove) {
                    Person p = new Person(worldMap.get(x).get(i));
                    newX = generator.nextInt(maxX);
                    newY = generator.nextInt(maxX);
                    if (p.move(newX, newY)) {
                        worldMap.get(x).remove(i--);
                        if (worldMap.containsKey(p.hashCode())) {
                            worldMap.get(p.hashCode()).add(p);
                        } else if (temp.containsKey(p.hashCode())) {
                            temp.get(p.hashCode()).add(p);
                        } else {
                            ArrayList<Person> ei = new ArrayList<>();
                            ei.add(p);
                            temp.put(p.hashCode(), ei);
                        }
                    }
                }
            }
        }
        for(Integer x: temp.keySet()){
            worldMap.put(x,temp.get(x));
        }
    }

    /**
     * Αυτή η συνάρτηση τρέχει σε κάθε βήμα της προσομοίωσης και αντιπροσωπέυει τις νέες μολύνσεις που προκύπτουν κάθε μέρα.
     * Καλείτε μετά από την move και ελέγχει για κάθε τετράγωνο της προσομείωσης στο οποίο βρίσκεται άνθρωπος ή άνθρωποι, εάν
     * ανάμεσα τους υπάρχει κάποιος προσβεβλημένος από τον ιό. Εάν υπάρχει τότε όλοι στο τετράγωνο μολύνονται με βάση τις πιθανότητες
     * που έχουν να προσβληθούν.
     */
    public synchronized void infectMe(){
        for(Integer x: worldMap.keySet()){
            if(hasInfected(x)){
                for(Person p: worldMap.get(x)){
                    if(generator.nextInt(101)<=p.chanceToGetInfected()&&!p.isInfected())
                        p.infect();
                }
            }
        }
    }

    /**
     * Αυτή η συνάρτηση τρέχει σε κάθε μέρα της προσομοίωσης και αποτελεί το πρόγραμμα εμβολιασμού της. Εάν το ανώτατο
     * ποσοστό εμβολιασμού που δίνεται από τον χρήστη στην αρχή έχει ξεπεραστεί, δεν κάνει τίποτα και επιστρέφει. Αλλιώς
     * με βάση τον ρυθμό εμβολιασμού που επίσης δίνει ο χρήστης εμβολιάζει αυτόν τον αριθμό ανθρώπων με τα διάφορα εμβόλια
     * που είναι διαθέσιμα και αυξάνει το πεδίο currentlyVaccinated.
     */
    public synchronized void vaccinateMe(){
        if(getVacPerc()>=upperVacLimit){return;}
        int vacCtr=0;
        while(vacCtr<vaccinationRate){
            for(Integer x: worldMap.keySet()){
                for(Person p: worldMap.get(x)){
                    if(p.getVaccine().getName().equals("NOT")){
                        p.vaccinate(generator.nextInt(4)+1);
                        ++vacCtr;
                        ++currentlyVaccinated;
                    }
                    if(vacCtr==vaccinationRate)
                        break;
                    if(getVacPerc()>=upperVacLimit){return;}
                }
                if(vacCtr==vaccinationRate)
                    break;
            }
        }
    }


    /**
     * Αυτή η συνάρτηση δέχεται έναν αριθμό και κοιτάει έαν σε εκείνη την τοποθεσία (ή τετραγωνάκι) του worldMap υπάρχει έστω και
     * ένας μολυσμένος άνθρωπος.
     * @param x Ο αριθμός τοποθεσιάς μετά από hashing του X,Y.
     * @return Επιστρέφει αν βρήκε ή όχι κάποιον μολυσμένο σε εκείνο το μέρος.
     */
    public synchronized boolean hasInfected(int x){
        if(worldMap.containsKey(x)){
            for(Person p:worldMap.get(x)){
                if(p.isInfected()&&!p.isResponsible())
                    return true;
            }
        }
        return false;
    }

    /**
     * Αυτή η συνάρτηση καλείτε σε κάθε βήμα της προσομοίωσης. Διατρέχει όλο το worldMap και κάθε φορά που βρίσκει άτομο
     * μολυσμένο κάτω από δύο μέρες, σύμφωνα με τις πιθανότητες του να νοσηλευτεί το βάζει στο νοσοκομείο.
     * Μετά διατρέχει κάθε άτομο που βρίσκεται στο νοσοκομείο και ανάλογα τις πιθανότητες του το διαγράφει από την προσομείωση
     * και αναπαριστά τον θάνατο του ατόμου αυτού ανανεώνοντας τα πεδία currentlyDead, population.
     * Έαν από την άλλη έχουν ολοκληρωθεί οι 10 μέρες νοσηλείας το αφαιρεί από το νοσοκομείο και το επανατοποθετεί στο worldMap.
     */
    public synchronized void severeDisease() {
        Person p;
        for (Integer x : worldMap.keySet()) {
            for (Iterator<Person> y = worldMap.get(x).listIterator(); y.hasNext(); ) {
                p = y.next();
                if (p.isInfected() && generator.nextInt(101) <= p.chanceOfHospitalization()*5&&p.getDaysOfInfection()>12) {
                    hospital.add(p);
                    p.hospitalize();
                    y.remove();
                }
            }
        }

        for (Iterator<Person> y = hospital.iterator(); y.hasNext(); ) {
            p = y.next();
            if (p.isHospitalized() && generator.nextInt(101) <= p.chanceOfDeath()) {
                y.remove();
                ++currentlyDead;
                --population;
            } else if (!p.isHospitalized()) {
                y.remove();
                worldMap.get(p.hashCode()).add(p);
            }
        }
    }
    /**
     * Αυτή η συνάρτηση διατρέχει όλη την δομή που περιέχει κάθε άνθρωπο και μετράει τους μολυσμένους.
     * @return Τον αριθμό που υπολόγισε + όσους είναι στο νοσοκομείο, δηλαδή όλοι όσοι νοσούν στον κόσμο.
     */
    public synchronized int getCurrentlyInfected() {
        int ctr=0;
        for(Integer x: worldMap.keySet()){
            for(Person p: worldMap.get(x)){
                if(p!=null&&p.isInfected())
                    ++ctr;
            }
        }
        return ctr+hospital.size();
    }


    /**
     * @return Επιστρέφει τον αριθμό των νεκρών ανθρώπων.
     */
    public synchronized int getCurrentlyDead(){return currentlyDead;}

    /**
     * @return Επιστρέφει τον αριθμό των ανθρώπων στο νοσοκομείο.
     */
    public synchronized int getCurrentlyHospitalized(){return hospital.size();}

    /**
     * @return Επιστρέφει την συμβολοσείρα που κρατάει τις αρχηκές παραμέτρους και τις εξελίξεις του κόσμου.
     */
    public synchronized StringBuilder getLog(){return log;}

    /**
     * @return Επιστρέφει το πλήθος των εμβολιασμένων.
     */
    public synchronized int getCurrentlyVaccinated() {return currentlyVaccinated;}

    /**
     * @return Υπολογίζει και επιστρέφει το ποσοστό του πληθυσμού που έχει εμβολιαστεί.
     */
    public synchronized double getVacPerc(){return ((double) currentlyVaccinated/population)*100;}

    /**
     * @return Επιστρέφει το μέγεθος του πληθυσμού εκείνη την στιγμή.
     */
    public synchronized int getPopulation(){return population;}

    /**
     * @return Επιστρέφει την δομή στην οποία βρίσκονται αποθηκευμένοι όλοι οι άνθρωποι της προσομοίωσης εκτός από αυτούς
     * που νοσηλέυονται.
     */
    public synchronized HashMap<Integer,ArrayList<Person>> getWorldMap(){return worldMap;}

    /**
     * @return Επιστρέφει την ημέρα στην οποία βρίσκεται η προσομόιωση
     */
    public synchronized int getDay(){return day;}

    /**
     * @return Επιστρέφει το μέγεθος της μίας διάστασης του κόσμου που αρκεί καθώς είναι πάντα τετράγωνος.
     */
    public  synchronized int getMaxX(){return maxX;}
}
