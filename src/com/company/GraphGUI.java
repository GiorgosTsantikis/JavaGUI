package com.company;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Η παρακάτω κλάση παρέχει την γραφική παρουσίαση μίας προσομοίωσης στον χρήστη. Μέσα στην γραφική αυτή παρουσίαση ο χρήστης βλέπει διαρκώς
 * τις αλλαγές που συμβαίνουν στα στατιστικά καθώς και έναν χάρτη ύπο κλίμακωση στον οποίο υπάρχουν χρώματα ανάλογα με το αν υπάρχουν μολυσμένοι
 * εκεί ή όχι. Τα τετράγωνα που δεν υπάρχουν άνθρωποι παραμένουν άσπρα, αυτά που έχουν κόσμο αλλά όχι κρούσματα είναι πράσσινα και κόκκινα είναι τα
 * τετράγωνα με ένα τουλάχιστον άτομο που μπορεί να μολύνει και δεν βρίσκεται σε καραντίνα.
 * @author Τσαντίκης Γεώργιος
 * @version 1
 */
public class GraphGUI  {

    private JPanel[][]panels;
    private World world;
    private JFrame frame;
    private JPanel squaresPanel,bigPanel,buttonPanel,buttonPanel1,buttonPanel2,labelPanel1,labelPanel2;
    private JLabel deaths,vaccinated,infected,population,hospitalized,percVacc,days,infections;
    private int maxX,maxY;
    private boolean[][]painted;
    private JButton nextDayButton,showButton,saveButton,skip;

    /**
     * Ο κατασκευαστής της κλάσης, η οποία χρησιμοποιεί με σύνθεση ένα αντικείμενο world με το οποίο και δημιουργείτε για να
     * αρχήσει την διεπαφή. Η διεπαφή αυτή αποτελείτε από ένα μεγάλο παράθυρο μέσα στο οποίο βρίσκονται 4 κουμπία που αρχικοποιούνται
     * από την συνάρτηση initButtons που δημιουργήθηκε για να μειώσει την έκταση του κατασκευαστή. Ακόμη αριστερά από τα κουμπιά υπάρχουν
     * οι πληροφορίες του κόσμου που ανανεώνονται σε κάθε μέρα της προσομοίωσης και ένα μεγάλο παράθυρο που περιέχει ένα πλέγμα από 150χ150
     * JPanels τα οποία θα χρωματιστούν αργότερα.
     * @param world
     */
    public GraphGUI(World world){
        this.maxX=GUI.maxX;
        this.maxY=GUI.maxY;
        painted=new boolean[maxX][maxX];
        panels=new JPanel[maxX][maxX];
        this.world=world;
        frame=new JFrame("SIMULATION");
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        bigPanel=new JPanel();
        frame.add(bigPanel);

        squaresPanel=new JPanel();
        squaresPanel.setPreferredSize(new Dimension(1366,718));
        squaresPanel.setLayout(new GridLayout(maxX,maxY));

        for(int i=0;i<maxX;i++){
            for(int j=0;j<maxY;j++) {
                panels[i][j] = new JPanel();
                panels[i][j].setPreferredSize(new Dimension(1, 1));

                panels[i][j].setBackground(Color.white);

                squaresPanel.add(panels[i][j]);
            }

        }

        buttonPanel=new JPanel();
        buttonPanel.setPreferredSize(new Dimension(1366,127));
        buttonPanel.setLayout(new GridLayout(2,2));

        labelPanel1=new JPanel();
        labelPanel1.setLayout(new GridLayout(2,2));
        population=new JLabel("Population: 0");
        infected=new JLabel("Infected: 0");
        vaccinated=new JLabel("Vaccinated: 0");
        days=new JLabel("Days: 0");
        labelPanel1.add(population);
        labelPanel1.add(infected);
        labelPanel1.add(vaccinated);
        labelPanel1.add(days);

        percVacc=new JLabel("Vaccinated: 0%");
        hospitalized=new JLabel("Hospitalized: 0");
        deaths=new JLabel("Deaths: 0");
        infections=new JLabel("Infections: 0");



        buttonPanel.add(labelPanel1);

        buttonPanel1=new JPanel();
        buttonPanel1.setLayout(new GridLayout(1,2));
        nextDayButton=new JButton("Next");


        nextDayButton.setFocusable(false);

        skip=new JButton("Run Auto");
        skip.setFocusable(false);

        buttonPanel1.add(nextDayButton);
        buttonPanel1.add(skip);
        buttonPanel.add(buttonPanel1);

        labelPanel2=new JPanel();
        labelPanel2.setLayout(new GridLayout(2,2));
        labelPanel2.add(percVacc);
        labelPanel2.add(hospitalized);
        labelPanel2.add(deaths);
        labelPanel2.add(infections);
        buttonPanel.add(labelPanel2);

        buttonPanel2=new JPanel();
        buttonPanel2.setLayout(new GridLayout(1,2));
        saveButton=new JButton("Save");
        showButton=new JButton("Show options");
        showButton.setFocusable(false);
        saveButton.setFocusable(false);

        buttonPanel2.add(showButton);
        buttonPanel2.add(saveButton);
        buttonPanel.add(buttonPanel2);


        bigPanel.setLayout(new BorderLayout());
        bigPanel.add(squaresPanel);
        bigPanel.add(buttonPanel,BorderLayout.PAGE_END);
        frame.setSize(new Dimension(1366,768));
        frame.setLocationRelativeTo(null);
        initButton();

        frame.setVisible(true);
    }

    /**
     * Αυτή η ιδιωτική συνάρτηση καλείτε σε κάθε ημέρα και αποχρωματίζει τα πανελ για να μπορέσουν να ξαναβαφούν με το
     * κατάλληλο χρώμα. Ακόμη αρχικοποιεί τον πίνακα painted.
     */
    private synchronized void clearPanels(){
        for(int i=0;i<maxX;i++){
            for(int j=0;j<maxY;j++){
                panels[i][j].setBackground(Color.white);
                painted[i][j]=false;
            }
        }
    }

    /**
     * Αυτή η συνάρτηση καλείτε από την διεπαφή αμέσως μετά την nextDay της κλάσης κόσμου. Έτσι αφού πραγματοποιηθούν οι
     * αλλαγές στην λογική, η συνάρτηση αυτή της οπτικοποιεί και ανανεώνει τα στατιστικά. Για τον χρωματισμό πραγματοποιείτε
     * διαπέραση κάθε κλειδιού στην προσομοίωση. Για κάθε κλειδί ψάχνουμε αν υπάρχει στην τοποθεσία εκείνη άτομο, αν όχι παραμένει
     * αχρωμάτιστο το πανελ. Αλλιώς εάν δεν υπάρχει κανένα μολυσμένο άτομο στο συγκεκριμένο τετράγωνο βάφεται πράσσινο. Στις περισσότερες
     * περιπτώσεις το μέγεθος του κόσμου είναι μεγαλύτερο από αυτό της διεπαφής που περιορίζεται στα 150χ150. Έτσι πολλά τετράγωνα του κόσμου
     * αντιστοιχούν σε ένα του κόσμου και αυτό ανα πάσα στιγμή μπορεί από πράσσινο να γίνει ξανά κόκκινο σε κάθε βήμα. Εάν όμως γίνει κόκκινο
     * τότε το χρώμα δεν μπορεί να ξαναλλάξει, γιαυτό υπάρχει βοηθιτικά ο πίνακας painted του οποίου τα κελιά που βάφονται κόκκινα γίνονται true
     * και έτσι αν πετύχουμε κάποια συντεταγμένη που αντιστοιχεί σε τετράγωνο που έχει ηδή βαφθεί κόκκινο συνεχίζει το πρόγραμμα (για λόγους ταχύτητας).
     * Τα κλειδία αντιστοιχούν σε συντεταγμένες μεγαλύτερες της διεπαφής γιαυτό χρησιμοποιείται μία βοηθητική συνάρτηση κλιμάκωσης.
     * @param inf Τα κρούσματα της προηγούμενης μέρας έτσι ώστε να ανανεωθεί το πεδίο newinfections σωστά.
     */
    public synchronized void updateGUI(int inf){
        clearPanels();
        HashMap<Integer,ArrayList<Person>>map= world.getWorldMap();
        Person pers=null;
        int persX,persY;
        for(Integer x: map.keySet()) {
            if (!map.get(x).isEmpty()) {
                pers = map.get(x).get(0);
                persX = scale(pers.getX());
                persY = scale(pers.getY());
                if (!painted[persX][persY]) {
                    if (world.hasInfected(x) ) {
                        panels[persX][persY].setBackground(Color.RED);
                        painted[persX][persY] = true;
                    } else {
                        panels[persX][persY].setBackground(Color.GREEN);
                    }
                }
            }
        }
        population.setText("Population: "+world.getPopulation());
        infected.setText("Infected: "+world.getCurrentlyInfected());
        vaccinated.setText("Vaccinated: "+world.getCurrentlyVaccinated());
        percVacc.setText("Vaccinated: "+world.getVacPerc()+"%");
        hospitalized.setText("Hospitalized: "+world.getCurrentlyHospitalized());
        deaths.setText("Deaths: "+world.getCurrentlyDead());
        days.setText("Days: "+world.getDay());
        infections.setText("New Infections: "+(world.getCurrentlyInfected()-inf));
    }

    /**
     * Αυτή η ιδιωτική συνάρτηση αρχικοποιεί τα 4 κουμπιά που υπάρχουν στην διεπαφή.
     */
    private void initButton(){
        nextDayButton.addActionListener(new ActionListener() {
            /**
             * Το κουμπί αυτό δίνει σήμα στο πρόγραμμα να προχωρήσει την προσομοίωση στο επόμενο βήμα ή μερα. Αρχικά
             * κρύβεται το κουμπί που πατήθηκε, για να μπορέσει το πρόγραμμα να φορτώσει τα αποτελέσματα πριν ζητηθεί η
             * επόμενη πάλι μέρα (πολλές συνεχόμενες πιέσεις σε προσομοίωση με μεγάλο πληθυσμό σε κάποιες περιπτώσεις κολλάνε
             * την εφαρμογή). Μετά δημιουργείτε ένα νέο νήμα στο οποίο αποθηκηκευονται τα προηγούμενα κρούσματα καλείτε η next()
             * και μετά η updateGUI, έτσι προχωράει ο κόσμος στο επόμενο βήμα.
             * @param e Το κουμπί.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                {
                    buttonPanel1.setVisible(false);
                    new Thread(){
                        @Override
                        public void run() {
                            int prevInfs= world.getCurrentlyInfected();
                            world.next();
                            updateGUI(prevInfs);
                        }
                    }.start();
                }
            }
        });

        showButton.addActionListener(new ActionListener() {
            /**
             * Αυτό το κουμπί πρέπει να πατηθεί για να ξαναεμφανίσει τα κουμπιά που κρύφτηκαν και κολλάει όταν τρέχει η
             * ενημέρωση έτσι ώστε να απαγορευει πολλαπλές συνεχόμενες ανανεώσεις.
             * @param e Το κουμπί.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                buttonPanel1.setVisible(true);
            }
        });

        saveButton.addActionListener(new ActionListener() {
            /**
             * Αυτό το κουμπί πατάει ο χρήστης όταν θέλει να αποθηκεύσει μία προσομοίωση στον σκληρό του δίσκο. Δημιουργείτε
             * αρχικά ένα νέο παραθυράκι στο οποίο υπάρχει ένα field στο οποίο αναμένεται να δωθεί το μονοπάτι στο οποίο θα
             * αποθηκευτεί το αρχείο αυτό. Υπάρχουν δύο επιλογές, είτε αποθήκευση δυαδικού αρχείου η οποία επιτρέπει ανάκτηση
             * και συνέχιση της, είτε αποθήκευση αρχείου κειμένου στο οποίο βρίσκονται οι αρχηκές παράμετροι που δώθηκαν και
             * οι εξελίξεις που σημειώθηκαν σε κάθε ημέρα/βήμα της προσομοίωσης.
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame savingFrame = new JFrame("Save as...");
                savingFrame.setSize(new Dimension(500, 100));
                savingFrame.setLocationRelativeTo(null);
                savingFrame.setResizable(false);
                savingFrame.setLayout(new GridLayout(2, 1));

                JPanel buttons = new JPanel();
                buttons.setLayout(new GridLayout(1, 2));
                JButton asText = new JButton("txt file");
                asText.setPreferredSize(new Dimension(100, 50));
                asText.setFocusable(false);
                JButton asLoadable = new JButton("Save file");
                asLoadable.setFocusable(false);
                asLoadable.setPreferredSize(new Dimension(100, 50));
                buttons.add(asText);
                buttons.add(asLoadable);
                JTextField field = new JTextField(20);
                savingFrame.add(field);
                savingFrame.add(buttons);
                savingFrame.setVisible(true);
                asLoadable.addActionListener(new ActionListener() {
                    /**
                     * Όταν ο χρήστης πατήσει αυτό το κουμπί θέλει να αποθηκεύσει ένα δυαδικό αρχείο στο δίσκο που μπορεί
                     * να ξαναφορτωθεί. Με το μονοπάτι που δώθηκε γίνεται απόπειρα δημιουργίας του αρχείου στον δίσκο από
                     * την FileHandler. Εάν κάτι πάει λάθος υπάρχει ο κατάλληλος χειρισμός και εμφάνιση μηνύματος.
                     * @param e Το κουμπί.
                     */
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        new Thread() {
                            @Override
                            public void run() {
                                try {
                                    savingFrame.setVisible(false);
                                    FileHandler.saveFile(field.getText() + ".covid", world);
                                    field.setText("success");
                                } catch (FileNotFoundException fileNotFoundException) {
                                    field.setText("Error: file not found, last in the pathname should be the new filename");
                                    System.out.println("fcl");
                                } catch (IOException ioException) {
                                    field.setText("Error: please try again, last in the pathname should be the new filename");
                                    System.out.println("fck");
                                }
                            }
                        }.start();
                    }
                });

                asText.addActionListener(new ActionListener() {
                    /**
                     * Το ίδιο συμβαίνει και με αυτό το κουμπί απλά καλείτε η συνάρτηση του FileHanlder για txt.
                     * @param e Το κουμπί.
                     */
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            savingFrame.setVisible(false);
                            FileHandler.saveTxtFile(field.getText()+".txt", world.getLog());
                        } catch (FileNotFoundException exception) {
                            field.setText("File not found");
                        } catch (IOException ioException) {
                            field.setText("Error please try again");
                        }
                    }
                });
            }
        });

        skip.addActionListener(new ActionListener() {
            /**
             * Αυτό το κουμπί με όνομα Run auto δίνει την δυνατότητα στον χρήστη να προχωρήσει με μεγάλη ταχύτητα την
             * προσομοίωση έτσι ώστε να φτάσει σε μεταγενέστερα στάδια της εξάπλωσης γρήγορα και δει τα αποτελέσματα των
             * παραμέτρων που έδωσε. Μόλις το πατήσει ξεκινάνε να καλούνται next() στο αντικείμενο κόσμου αυτόματα και ανα
             * 15 μέρες καλείτε ανανέωση της διεπαφής για να υπάρχει μεγάλη ταχύτητα καθώς η ανανέωση της διεπαφής ανάλογα
             * το μέγεθος του πληθυσμού χρειάζεται λίγο χρόνο. Το κουμπί κρύβεται όταν πατηθεί, αν ο χρήστης επιθυμεί να
             * σταματήσει θα πατήσει το κουμπί εμφάνισης και το όνομα του κουμπιού αυτού θα έχει αλλάξει σε stop οταν το
             * ξαναπατήσει θα σταματήσει μέχρι στην ημέρα που έφτασε.
             */
            int presses=0;
            @Override
            public void actionPerformed(ActionEvent e) {
                presses++;
                buttonPanel1.setVisible(false);
                if(presses%2==0){skip.setText("Run auto");}
                else{skip.setText("Stop");}

                new Thread(){
                    int ctr=0;
                    @Override
                    public void run() {
                        while(world.getCurrentlyInfected()!=0&&presses%2!=0){
                            int prevInfs= world.getCurrentlyInfected();
                            world.next();
                            if(ctr==10) {
                                updateGUI(prevInfs);
                                ctr=0;
                            }
                            ++ctr;
                        }
                    }
                }.start();
            }
        });
    }

    /**
     * Αυτή η ιδιωτική συνάρτηση δέχεται έναν αριθμό που είναι συντεταγμένη κάποιου ανθρώπου και την κλιμακώνει στα όρια
     * 150χ150 της γραφικής διεπαφής ταιριάζοντας κάθε άτομο στην ανάλογη του θέση στην διεπαφή.
     * @param x Η συντεταγμένη.
     * @return Η κλιμακούμενη συντεταγμένη.
     */
    private int scale(int x){
        double ret;
        ret=((double) x/(world.getMaxX()))*(GUI.maxX-1);
        ret=Math.round(ret);
        if((int)ret>=150){return 149;}
        return (int)ret;
    }
}
