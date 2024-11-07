package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Random;

/**
 * Αυτή η κλάση τρέχει όλη την διεπαφή με τον χρήστη, δέχεται από αυτόν δεδομένα για νέες προσομοιώσεις, αποθηκεύει ήδη
 * τρέχουσες προσομοιώσεις, τις φορτώνει απο τον δίσκο σε νέο παράθυρο και συνδέει τα υπόλοιπα μέρη του προγράμματος έτσι
 * ώστε να τρέχουν με τον ρυθμό που ορίζει ο χρήστης. Η κλάση επιτρέπει στον χρήστη να τρέξει πολλές προσομοιώσεις παράλληλα
 * ανάλογα με τις δυνατότητες του συστήματος του.
 * @author Τσαντίκης Γεώργιος
 * @version 1
 */
public class GUI {
    private JFrame startFrame;
    private JButton newSimButton,infoButton,loadButton;
    public static final int maxX=150,maxY=150;

    /**
     * Ο κατασκευαστής της κλάσης δημιουργεί ένα παραθυράκι που λειτουργεί ως το αρχικό σημείο της εφαρμόγης, στο οποίο
     * δίνονται στον χρήστη τρεις επιλογές. Μπορεί  να ξεκινήσει μία νέα προσομοίωση, να φορτώσει από τον δίσκο μία από
     * το παρελθόν και να διαβάσει πληροφορίες και οδηγίες σχετικά με την εφαρμογή. Κάθε επιλογή δημιουργεί ένα νέο παράθυρο,
     * αν κλείσει το παράθυρο αυτό χωρίς να τρέχει κάποιο άλλο η εφαρμογή τερματίζει.
     */
    public GUI(){
        startFrame=new JFrame("Welcome");
        startFrame.setSize(new Dimension(300,200));
        startFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        startFrame.setResizable(false);

        newSimButton=new JButton("New");
        newSimButton.setPreferredSize(new Dimension(70,30));
        newSimButton.setFocusable(false);

        infoButton=new JButton("Info");
        infoButton.setFocusable(false);
        infoButton.setPreferredSize(new Dimension(70,30));

        loadButton=new JButton("Load");
        loadButton.setFocusable(false);
        loadButton.setPreferredSize(new Dimension(70,30));

        JPanel panel=new JPanel();
        panel.setBackground(Color.CYAN);
        panel.setLayout(new FlowLayout());
        panel.add(newSimButton);
        panel.add(loadButton);
        panel.add(infoButton);

        JLabel label=new JLabel("THE COVID-19 \n SIMULATION");
        label.setFont(new Font("Comic sans",Font.BOLD,20));
        label.setForeground(Color.RED);
        startFrame.add(label, BorderLayout.CENTER);
        startFrame.add(panel,BorderLayout.SOUTH);
        startFrame.setLocationRelativeTo(null);
        initButtons();
        startFrame.setVisible(true);
    }

    /**
     * Αυτή η μέθοδος είναι private και η μόνη χρήση της είναι να αρχικοποιεί τα κουμπιά της διεπαφής, χρησιμοποιήθηκε για
     * να μην είναι υπερβολικά μεγάλη η έκταση του κατασκευαστή.
     */
    private void initButtons(){
        newSimButton.addActionListener(new ActionListener() {
            /**
             * Το κουμπί new καλεί την συνάρτηση που χειρίζεται την δημιουργία μίας νέας προσομοίωσης.
             * @param e Το κουμπί που πατήθηκε.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                newSimulation();
            }
        });

        infoButton.addActionListener(new ActionListener() {
            /**
             * Το κουμπί info δημιουργεί ένα παραθυράκι στο οποίο προβάλεται το κείμενο που περιέχει αυτή η συνάρτηση και
             * ενημερώνει τον χρήστη για τον τρόπο λειτουργίας του προγράμματος.
             * @param e Το κουμπί που πατήθηκε.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame infoFrame=new JFrame();
                infoFrame.setResizable(false);
                infoFrame.setLocationRelativeTo(null);
                infoFrame.setSize(new Dimension(500,300));
                StringBuilder found=new StringBuilder("Hello, welcome to the covid simulation. Programme parameters are:\n" +
                        "1)Size of population (large populations require more RAM!).\n" +
                        "2)The daily rate of vaccinations.\n" +
                        "3)The amount of people infected on day 0.\n" +
                        "4)The size of the map simulation square (the bigger it is the slower the spread).\n" +
                        "5)The maximum percentage of the population to be vaccinated.\n" +
                        "\n" +
                        "Everytime you press Next you need to press the show options button when the next day" +
                        "becomes available.\n" +
                        "\n" +
                        "The run auto button makes the simulation skip days and reach later parts of the virus spread.\n"+
                        "In order to stop you have to press the show options button and the stop button and wait until it finishes.\n"+
                        "The benchmark numbers are the ones used to measure the properties of the virus and the" +
                        "results are after 2,759 days the virus was completely eradicated after 453,712 people got" +
                        "vaccinated and 181,547 died.\n"+"When loading from a file you have to put in the complete path\n"+
                        "to the covid file and write .covid in the end of it.\n " +"Loading takes a while because files are large in size\n"
                        +"The program saves loadable covid files and txt files.\n "+
                        "that show the initial parameters as well as the results of everyday.\n"+
                        "Even though this is a way deadlier virus we still se how much vaccines affect its damage\n"
                        +"Benchmarks results are for 50% limit of vaccination deaths after 2491 come up to 763413\n"+
                        "While when everyone is willing to be vaccinated the deaths after 3101 days are limited to 489218");
                JTextArea area=new JTextArea(found.toString());
                area.setEditable(false);
                area.setLineWrap(true);
                area.setWrapStyleWord(true);
                JScrollPane scroll=new JScrollPane(area,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                infoFrame.add(scroll);
                infoFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                infoFrame.setVisible(true);
            }
        });

        loadButton.addActionListener(new ActionListener() {
            /**
             * Το κουμπί load είναι υπεύθηνο για την φόρτωση αρχείων .covid από τον δίσκο. Εμφανίζει ένα παράθυρο όπου ζητάει
             * από τον χρήστη το μονοπάτι του αρχείου και προσπάθει με την βοήθεια της FileHandler να το πραγματοποιήσει. Το παράθυρο
             * αυτό δέχεται είσοδο όταν πατηθεί το ok.
             * @param e Το κουμπί που πατήθηκε.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame load=new JFrame("Load");
                load.setLocationRelativeTo(null);
                load.setSize(new Dimension(400,120));

                JPanel panel=new JPanel();
                panel.setLayout(new GridLayout(3,1));

                JLabel label=new JLabel("Give absolute path: don't forget to add .covid at the end!");
                JTextField fileName=new JTextField(20);

                JButton okButton=new JButton("OK");
                okButton.setFocusable(false);

                panel.add(label);
                panel.add(fileName);
                panel.add(okButton);
                load.add(panel);
                load.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                load.setVisible(true);

                okButton.addActionListener(new ActionListener() {
                    /**
                     * Όταν ο χρήστης πατήσει ok, περνάμε στην startSimulation το αντικείμενο του κόσμου που φορτώθηκε, αλλιώς
                     * εμφανίζεται το κατάλληλο μήνυμα λάθους στον χρήστη.
                     * @param e Το κουμπί.
                     */
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        new Thread(){
                            @Override
                            public void run() {
                                try {
                                    startSimulation(FileHandler.loadWorld(fileName.getText()));
                                    load.dispose();
                                    System.out.println("succes");

                                }catch(ClassNotFoundException exception){
                                    fileName.setText("Error or corrupt file");
                                }catch(IOException exc){
                                    fileName.setText("File not found");
                                }
                            }
                        }.start();
                    }
                });
            }
        });


    }

    /**
     * Αυτή η συνάρτηση είναι private και χρησιμοποιείται για να επιβεβαιώσει ότι ο χρήστης έδωσε αριθμούς στα πεδία και
     * όχι κάποιον άλλο χαρακτήρα ή το κενό.
     * @param str Η συμβολοσειρά που θα ελεγχθεί.
     * @return Αν είναι μη κενή, αριθμητική συμβολοσείρα.
     */
    private static boolean isNumeric(String str){
        {
            if(str.equals("")){return false;}
            for (char c : str.toCharArray())
            {
                if (!Character.isDigit(c)) return false;
            }
            return true;
        }
    }

    /**
     * Αυτή η συνάρτηση καλείτε όταν ο χρήστης επιθυμεί να δημιουργήσει μία νέα προσομοίωση. Αρχηκά δημιουργεί ένα παράθυρο
     * στο οποίο ζητάει από τον χρήστη τις παραμέτρους της. Έαν ο χρήστης το επιθυμεί μπορεί να τρέξει την benchmark προσομοίωση
     * η οποία περιέχει το νούμερα με τα οποία δοκιμαζόταν το πρόγραμμα και γινόταν η αποσφαλμάτωση ή να δοκιμάσει με τυχαία νούμερα
     * δωσμένα από μία γεννήτρια. Για το input χρησιμοποιούνται 4 text fields και ένας slider για τις παραμέτρους: 1)Μέγεθος πληθυσμού
     * 2)Ρυθμός Εμβολιασμών, 3)Αρχικά κρούσματα, 4)Μέγεθος προσομοίωσης και το slider για το ανώτατο ποσοστό του πληθυσμού που θα εμβολιαστεί.
     */
    private void newSimulation(){
        JFrame sim=new JFrame("Parameters");
        sim.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        sim.setLocationRelativeTo(null);
        sim.setSize(new Dimension(400,250));
        sim.setLayout(new GridLayout(2,1));

        JTextField field1=new JTextField(10);
        field1.setToolTipText("Population");
        JLabel label1=new JLabel("Population:");

        JTextField field2=new JTextField(10);
        field2.setToolTipText("Vaccination rate");
        JLabel label2=new JLabel("Vaccination rate:");

        JTextField field3=new JTextField(10);
        field3.setToolTipText("Initially infected");
        JLabel label3=new JLabel("Initially infected:");

        JTextField field4=new JTextField(10);
        field4.setToolTipText("Size of map");
        JLabel label4=new JLabel("Size of square simulation space XxX");

        JPanel textPanel=new JPanel();
        textPanel.setLayout(new GridLayout(4,2));
        textPanel.add(label1);
        textPanel.add(field1);
        textPanel.add(label2);
        textPanel.add(field2);
        textPanel.add(label3);
        textPanel.add(field3);
        textPanel.add(label4);
        textPanel.add(field4);
        sim.add(textPanel);

        JSlider slider=new JSlider();
        JLabel label5=new JLabel("Upper vaccination percentage:");
        slider.setToolTipText("0%-100%");

        JButton okButton=new JButton("OK");
        okButton.setPreferredSize(new Dimension(100,46));
        okButton.setFocusable(false);

        JPanel thirdParPanel=new JPanel();
        thirdParPanel.setLayout(new GridLayout(1,2));
        thirdParPanel.add(label5);
        thirdParPanel.add(slider);

        JPanel buttonPanel=new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(okButton);

        JButton randButton=new JButton("Random");
        randButton.setPreferredSize(new Dimension(100,46));
        randButton.setFocusable(false);
        buttonPanel.add(randButton);

        JButton benchMarkButton=new JButton("Benchmark");
        benchMarkButton.setFocusable(false);
        benchMarkButton.setPreferredSize(new Dimension(100,46));
        buttonPanel.add(benchMarkButton);

        JPanel secondPanel=new JPanel();
        secondPanel.setLayout(new GridLayout(2,1));
        secondPanel.add(thirdParPanel);
        secondPanel.add(buttonPanel);
        sim.add(secondPanel);

        okButton.addActionListener(new ActionListener() {
            /**
             * Όταν ο χρήστης ολοκληρώσει το πέρασμα παραμέτρων τότε πατάει αυτό το κουμπί και ελέγχονται όλα τα πεδία. Εάν βρεθεί
             * κάποιο που να δημιουργεί προβλήματα είτε επειδή η τιμή είναι ακραία είτε επειδή δεν είναι τιμή εμφανίζεται το κατάλληλο
             * μήνυμα. Εάν η είσοδος είναι αποδεκτή τότε δημιουργείτε ένα νέο αντικείμενο του κόσμου με τις παραμέτρους που πήρε το πρόγραμμα
             * και φορτώνεται σε νέο παράθυρο στην γραφική διεπαφή του GraphGUI.
             * @param e Το κουμπί.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                String f1,f2,f3,f4;
                int i1,i2,i3,i4,i5,ii2,ii3;
                boolean goodInput=true;
                f1=field1.getText();
                f2=field2.getText();
                f3=field3.getText();
                f4=field4.getText();
                if(!GUI.isNumeric(f1)){
                    goodInput=false;
                    field1.setText("Wrong Input");
                }else if(Integer.parseInt(f1)>10000000){
                    goodInput=false;
                    field1.setText("Population too big");
                }
                if(!GUI.isNumeric(f2)){
                    goodInput=false;
                    field2.setText("Wrong Input");
                }
                if(!GUI.isNumeric(f3)){
                    goodInput=false;
                    field3.setText("Wrong Input");
                }
                if(!GUI.isNumeric(f4)){
                    goodInput=false;
                    field3.setText("Wrong Input");
                }else if(Integer.parseInt(f4)>100000||Integer.parseInt(f4)==0||Integer.parseInt(f4)<150){
                    goodInput=false;
                    field4.setText("Should be between 150 and 100000");
                }
                if(goodInput){
                    i1=Integer.parseInt(f1);
                    i2=Integer.parseInt(f2);
                    i3=Integer.parseInt(f3);
                    i4=Integer.parseInt(f4);
                    if(i2>i1){
                        ii2=i1;
                    }else{
                        ii2=i2;
                    }
                    if(i3>i1){
                        ii3=i1;
                    }else{
                        ii3=i3;
                    }
                    i5=slider.getValue();
                    sim.dispose();
                    World newWorld = new World(i1, ii2, ii3,i4, i5);
                    startSimulation(newWorld);
                }
            }
        });

        randButton.addActionListener(new ActionListener() {
            /**
             * Εάν ο χρήστης επιθυμεί να τρέξει μία προσομοίωση με τυχαίες παραμέτρους, τότε με την χρήση μίας γεννήτριας
             * γεμίζονται τα πεδία σε αποδεκτές τυχαίες τιμές.
             * @param e Το κουμπί.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                Random random=new Random(System.currentTimeMillis());
                field1.setText(String.valueOf(random.nextInt(10000000)));
                field2.setText(String.valueOf(random.nextInt(100000)));
                field3.setText(String.valueOf(random.nextInt(10000)));
                field4.setText(String.valueOf(random.nextInt(5000)));
                slider.setValue(random.nextInt(101));
            }
        });

        benchMarkButton.addActionListener(new ActionListener() {
            /**
             * Εάν πατήσει ο χρήστης το κουμπί benchmark τότε τα πεδία γεμίζουν με τις τιμές που χρησιμοποιήθηκαν ευρέως στο testing.
             * @param e Το κουμπί.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                field1.setText(String.valueOf(1000000));
                field2.setText(String.valueOf(1000));
                field3.setText(String.valueOf(1000));
                field4.setText(String.valueOf(1000));
                slider.setValue(50);
            }
        });
        sim.setResizable(false);
        sim.setVisible(true);
    }

    /**
     * Αυτή η συνάρτηση είναι private και χρησιμεύει στην δημιουργεία η φόρτωση προσομοίωσης σε νέο παράθυρο.
     * @param world Το αντικείμενο του κόσμου, πάντα δημιουργείτε πριν φτάσει η εκτέλεση σε αυτό το σημείο.
     */
    private void startSimulation(World world){
        world.createWorld();
        GraphGUI graph=new GraphGUI(world);
        int infs=world.getCurrentlyInfected();
        graph.updateGUI(infs);
    }
}