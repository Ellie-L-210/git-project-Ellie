import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOError;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Scanner;

public class Git {
    public Git() throws IOException {
        init();
    }

    public static void init() throws IOException {
        // make git directory
        File git = new File("git");

        // make objects directory
        File objects = new File("git/objects");

        // make index file in git directory
        File index = new File("git/index");

        // make HEAD file in git directory
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
    public static String SHA1Hash(File original) throws IOException {
        try {
            // getInstance() method is called with algorithm SHA-1
            MessageDigest md = MessageDigest.getInstance("SHA-1");

            // ADDED - reads file contents from inputFile and stores in input variable
            String input = "";
            try (BufferedReader br = new BufferedReader(new FileReader(original))) {
                while (br.ready()) {
                    input += br.readLine();
                }
            }

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

    public static File createBLOB(File original) throws IOException {
        // creates hash from original File
        String hash = SHA1Hash(original);

        if (!blobExists(hash, original)) {
            // make new blob in objects directory, with generated hash as the file name
            File blob = new File("git/objects/" + hash);

            // reader to store file contents
            BufferedReader originalBr = new BufferedReader(new FileReader(original));
            String fileContents = "";

            while (originalBr.ready()) {
                fileContents += originalBr.readLine();
            }
            originalBr.close();

            // write original file contents into blob file
            BufferedWriter br = new BufferedWriter(new FileWriter(blob));
            br.write(fileContents);
            br.close();
            return blob;
        }
        return null;
    }

    // returns true if blob with same path and hash exist in the index file
    public static boolean blobExists(String hash, File original) throws IOException {
        File index = new File("git/index");
        try (BufferedReader br = new BufferedReader(new FileReader(index))) {
            String line = "";
            while (br.ready()) {
                // read line of index file
                line = br.readLine();
                // if path of file exists in read line
                if (line.contains(original.getPath())) {
                    // if parameter hash doesn't exist
                    if (!line.contains(hash)) {
                        return false;
                    } else {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // checks if file with same hash and path name is already indexed, returns false
    // even if hashes are the same but paths are different
    public static boolean isIndexed(String hash, File original) throws IOException {
        File index = new File("git/index");
        BufferedReader br = new BufferedReader(new FileReader(index));
        String line = "";
        boolean isIndexed = false;
        while (br.ready()) {
            line = br.readLine();
            // if exact copy of hash and original file path exist
            if (line.contains(hash) && line.contains(original.getPath())) {
                isIndexed = true;
                return true;
            }
            // if hash with different path exists, still add file with different path to
            // index
            else if (line.contains(hash) && !line.contains(original.getPath())) {
                isIndexed = false;
            }
            // if same hash with different file name exists, still add file to index
            else if (line.contains(hash) && !line.contains(original.getName())) {
                isIndexed = false;
            }
        }
        br.close();
        return isIndexed;

    }

    public static void addToIndex(String hash, File original) throws IOException {
        // writer to write to index file
        BufferedWriter br = new BufferedWriter(new FileWriter("git/index", true));

        // temp file and writer in case we need to update a file with new hash
        File temp = new File("git/temporary.txt");
        BufferedWriter tempWriter = new BufferedWriter(new FileWriter(temp));
        boolean indexChanged = false;

        // if blob with same hash and path name don't exist
        if (!isIndexed(hash, original)) {
            // reader to read index file
            try (BufferedReader indexReader = new BufferedReader(new FileReader("git/index"))) {
                String line = "";
                while (indexReader.ready()) {
                    line = indexReader.readLine();
                    // if read line contains path name but different hash
                    if (line.contains(original.getPath()) && !line.contains(hash)) {
                        tempWriter.write(hash + " " + "git-project-Ellie/" + original.getPath() + "\n");
                        indexChanged = true;
                    } else {
                        // write line to temp file
                        tempWriter.write(line + "\n");
                    }
                }
                tempWriter.close();
            }

            if (indexChanged == true) {
                File index = new File("git/index");
                temp.renameTo(index);
                // index.delete();
            } else {
                br.write(hash + " " + "git-project-Ellie/" + original.getPath() + "\n");
                br.close();
                temp.delete();
            }
        }
    }

    // deletes all blobs from the objects file
    public static void deleteAllBlobs() {
        File[] blobs = new File("git/objects").listFiles();
        if (blobs != null) {
            for (File blob : blobs) {
                blob.delete();
            }
        }
    }

    // deletes all blobs via deleteAllBlobs and clears the index file
    public static void resetAllFiles() throws IOException {
        deleteAllBlobs();
        File index = new File("git/index");
        BufferedWriter br = new BufferedWriter(new FileWriter(index));
        br.write("");
        br.close();
    }

    public static String createTree(String directoryPath) throws IOException {
        StringBuilder treeContent = new StringBuilder();
        File currentFile = new File(directoryPath);
        File[] children = currentFile.listFiles();

        for (File child : children) {
            if (child.isFile()) {
                createBLOB(child);
                if (treeContent.isEmpty()) {
                    treeContent.append("blob " + SHA1Hash(child) + " " + child.getPath());
                } else {
                    treeContent.append("\nblob " + SHA1Hash(child) + " " + child.getPath());
                }
                System.out.println("Added: " + child.getAbsolutePath());

                // if (child == children[0]) {
                // text.append("blob " + SHA1Hash(child) + " " + child.getPath());
                // } else {
                // text.append("\nblob " + SHA1Hash(child) + " " + child.getPath());
                // }
            } else if (child.isDirectory()) {
                String treeHash = createTree(child.getPath());

                if (treeContent.isEmpty()) {
                    treeContent.append("tree " + treeHash + " " + child.getPath());
                } else {
                    treeContent.append("\ntree " + treeHash + " " + child.getPath());
                }
                System.out.println("Added: " + child.getAbsolutePath());

                // if (child == children[0]) {
                // text.append("tree " + SHA1Hash(hashedTree) + " " + child.getPath());
                // } else {
                // text.append("\ntree " + SHA1Hash(hashedTree) + " " + child.getPath());
                // }
            }

        }

        File tree = new File(directoryPath + "file");
        BufferedWriter bw = new BufferedWriter(new FileWriter(tree));
        bw.write(treeContent.toString());
        bw.close();
        String finalTreeHash = SHA1Hash(tree);
        tree.renameTo(new File("git/objects/" + finalTreeHash));
        return finalTreeHash;
    }

}