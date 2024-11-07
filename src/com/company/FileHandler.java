package com.company;
import java.io.*;
import java.nio.file.*;

/**
 * Αυτή η κλάση αποτελείται αποκλειστικά απο στατικές μεθόδους που δημιουργήθηκαν για να διαχειρίζονται την αποθήκευση
 * και φόρτωση δεδομένων από και σε αρχεία. Γενικά υποστηρίζει αποθήκευση δυαδικού αρχείου με αντικείμενο world και
 * person, φόρτωση τέτοιου αντικειμένου από τον δίσκο καθώς και αποθήκευση αντικειμένου σε αρχείο .txt απλά για παρουσίαση
 * πληροφοριών.
 * @author Τσαντίκης Γεώργιος
 * @version 1
 */
public class FileHandler {

    /**
     * Αυτή η μέθοδος καλείτε όταν ο χρήστης θέλει να αποθηκέυσει μία κατάσταση της προσωμοίωσης σε ένα αρχείο για να έχει
     * πρόσβαση σε αυτήν και αργότερα. Έαν δωθεί το ίδιο αρχείο πάνω από μία φορά τότε τα περιεχόμενα του αντικαθίστανται με αυτά
     * της νέας κατάστασης. 'Εαν υπάρχει κάποιο πρόβλημα ρίχνει exception πίσω στο μέρος που την κάλεσε και εκεί αντιμετωπίζεται
     * @param path Το απόλυτο μονοπάτι του αρχείου, αν δεν υπάρχει το αρχείο το δημιουργεί, όμως εάν λείπουν μητρικοί φάκελοι αποτυγχάνει.
     * @param world Το αντικείμενο της προσωμοίωσης που θέλουμε να αποθηκεύσουμε.
     * @throws IOException
     */
    public static void saveFile(String path,World world)throws IOException{
        Path pathOfFile=Paths.get(path);
        File file=pathOfFile.toFile();
        file.createNewFile();
        if(!file.exists()){throw new FileNotFoundException();}
        FileOutputStream fileOutputStream = new FileOutputStream(file,false);
        ObjectOutputStream objectOutputStream=new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(world);
        objectOutputStream.close();
    }

    /**
     * Αυτή η μέθοδος αποθηκεύει σε ένα αρχείο κειμένου τις αρχικές παραμέτρους της προσωμοίωσης και τις αλλαγές που
     * προκλήθηκαν σε αυτήν κάθε μέρα της, χάρη στην συμβολοσειρά log στην κλάση προσωμοίωσης, το μόνο που κάνει είναι
     * να το αντιγράφει στο επιθυμητό αρχείο, συμπεριφέρεται με τον ίδιο τρόπο της saveFile σχετικά με το μονοπάτι και τα
     * προβλήματα που μπορεί να προκληθούν.
     * @param path Το απόλυτο μονοπάτι του αρχείου, αν δεν υπάρχει το αρχείο το δημιουργεί, όμως εάν λείπουν μητρικοί φάκελοι αποτυγχάνει.
     * @param toSave 'Ενα αντικείμενο StringBuilder που γράφεται στο αρχείο.
     * @throws IOException
     */
    public static void saveTxtFile(String path,StringBuilder toSave)throws IOException{
        Path pathOfFile=Paths.get(path);
        File file=pathOfFile.toFile();
        file.createNewFile();
        if(!file.exists()){throw new FileNotFoundException();}
        BufferedWriter writer=new BufferedWriter(new FileWriter(file));
        writer.write(toSave.toString());
        writer.close();
    }

    /**
     * Αυτή η συνάρτηση δέχεται το όνομα ενός μονοπατιού και προσπαθεί να ανακτήσει από το αρχείο αυτό ένα αντικείμενο
     * της προσωμοίωσης του κόσμου και να το επιστρέψει πίσω στη συνάρτηση που την κάλεσε. Αν κάτι πάει στραβά ρίχνει δύο
     * exceptions που τα χειρίζεται η συνάρτηση που την καλεί.
     * @param path Το απόλυτο μονοπάτι του αρχείου.
     * @return Το αντικείμενο που φωρτόθηκε από το αρχείο.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static World loadWorld(String path)throws IOException,ClassNotFoundException{
        FileInputStream inputStream=new FileInputStream(Paths.get(path).toFile());
        ObjectInputStream objectIN=new ObjectInputStream(inputStream);
        return (World)objectIN.readObject();
    }
}