import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOError;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class initializeGit {
    public static void main(String[] args) throws IOException {
        // initialize();
        // System.out.println("testing SHA1");
        // System.out.println(SHA1Hash("HELLO"));
        // createBLOB("hello");
        // createBLOB("Hi");
        // System.out.println("SHA1 of hell0: " + SHA1Hash("hello"));
        // System.out.println(blobExists(SHA1Hash("hello")));
        // System.out.println(blobExists(SHA1Hash("Hi")));
        // deleteAllBlobs();
        // System.out.println(blobExists(SHA1Hash("hello")));
        // System.out.println(blobExists(SHA1Hash("Hi")));

        // 2.4.1 Testing
        String one = "hi";
        String two = "hello";
        String three = "hi hello";

        createBLOB(one);
        createBLOB(two);
        createBLOB(three);

        addToIndex(SHA1Hash(one), "hi.txt");
        addToIndex(SHA1Hash(two), "hello.txt");
        addToIndex(SHA1Hash(three), "hihello.txt");

        // resetAllFiles();
    }

    public static void initialize() throws IOException {
        // make git directory obj
        File git = new File("git");

        // make objects directory obj
        File objects = new File("git/objects");

        // make index file in git directory obj
        File index = new File("git/index");

        // make HEAD file in git directory obj
        File HEAD = new File("git/HEAD");

        // if created objects already exists, throw below message
        if (objects.exists() && index.exists() && HEAD.exists()) {
            System.out.println("Git Repository Already Exists");
        } else { // if objects are not yet created, create and throw completion message
            git.mkdir();
            objects.mkdir();
            index.createNewFile();
            HEAD.createNewFile();
            System.out.println("Git Repository Created");
        }
    }

    // SHA1 Code taken from geeksforgeeks website
    public static String SHA1Hash(String input) {
        try {
            // getInstance() method is called with algorithm SHA-1
            MessageDigest md = MessageDigest.getInstance("SHA-1");

            // digest() method is called
            // to calculate message digest of the input string
            // returned as array of byte
            byte[] messageDigest = md.digest(input.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);

            // Add preceding 0s to make it 40 digits long
            while (hashtext.length() < 40) {
                hashtext = "0" + hashtext;
            }

            // return the HashText
            return hashtext;
        }

        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static void createBLOB(String fileContent) throws IOException {
        String hash = SHA1Hash(fileContent);
        if (!blobExists(hash)) {
            File blob = new File("git/objects/" + hash);
            BufferedWriter br = new BufferedWriter(new FileWriter(blob));
            br.write(fileContent);
            br.close();
        }
    }

    public static boolean blobExists(String fileName) {
        File current = new File("git/objects/" + fileName);
        if (current.exists()) {
            return true;
        }
        return false;
    }

    public static void deleteAllBlobs() {
        File[] blobs = new File("git/objects").listFiles();
        if (blobs != null) {
            for (File blob : blobs) {
                blob.delete();
            }
        }
    }

    public static void addToIndex(String hash, String fileName) throws IOException {
        BufferedWriter br = new BufferedWriter(new FileWriter("git/index", true));
        br.write(hash + " " + fileName + "\n");
        br.close();
    }

    public static void resetAllFiles() throws IOException {
        deleteAllBlobs();
        File index = new File("git/index");
        BufferedWriter br = new BufferedWriter(new FileWriter(index));
        br.write("");
        br.close();
    }
}