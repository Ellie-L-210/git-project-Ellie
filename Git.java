import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOError;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
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
                // System.out.println("Added: " + child.getAbsolutePath());
            } else if (child.isDirectory()) {
                String treeHash = createTree(child.getPath());

                if (treeContent.isEmpty()) {
                    treeContent.append("tree " + treeHash + " " + child.getPath());
                } else {
                    treeContent.append("\ntree " + treeHash + " " + child.getPath());
                }
                // System.out.println("Added: " + child.getAbsolutePath());
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

    public static void treeFromIndex() throws IOException {
        initializeList();
        makeTreeFromIndex();

        BufferedReader br = new BufferedReader(new FileReader("git/workingList"));

        File temp = File.createTempFile("finalTemp", ".txt");
        StringBuilder listContents = new StringBuilder();
        BufferedWriter tempWriter = new BufferedWriter(new BufferedWriter(new FileWriter(temp)));
        String finalLine = "";

        while (br.ready()) {
            String line = br.readLine();
            if (br.ready()) {
                listContents.append(line + "\n");
            } else {
                listContents.append(line);
                finalLine = line;
            }
        }

        tempWriter.write(listContents.toString());
        tempWriter.close();
        br.close();

        BufferedWriter finalWriter = new BufferedWriter(new FileWriter("git/workingList"));
        String lastThing = "tree " + SHA1Hash(temp) + " " + finalLine.substring(finalLine.lastIndexOf(" ") + 1);
        System.out.println(lastThing);
        finalWriter.write(lastThing);
        finalWriter.close();

    }

    // adds "blob" to all entries in the index file, then sorts the list by
    // directory path
    public static void initializeList() throws IOException {
        List<String> workingList = new ArrayList<String>();
        // workingList file to write sorted index into
        File workingListFile = new File("git/workingList");
        BufferedWriter bw = new BufferedWriter(new FileWriter(workingListFile));

        // workingList array to sort index file
        BufferedReader br = new BufferedReader(new FileReader(("git/index")));

        // add "blob" to each line in index file
        while (br.ready()) {
            workingList.add("blob " + br.readLine());
        }
        br.close();

        Collections.sort(workingList, new Comparator<String>() {
            @Override
            public int compare(String line1, String line2) {
                // get full path of each line
                String fullPath1 = line1.substring(line1.indexOf("git-project-Ellie/"));
                String fullPath2 = line2.substring(line2.indexOf("git-project-Ellie/"));

                // exclude file name from each directory path
                String directoryPath1 = fullPath1.substring(0, fullPath1.lastIndexOf("/"));
                String directoryPath2 = fullPath2.substring(0, fullPath2.lastIndexOf("/"));

                return directoryPath1.compareTo(directoryPath2);
            }

        });

        // reverse sorted list so leaf-most files come first
        workingList = workingList.reversed();

        // write sorted workingList array into workingList file
        for (int i = 0; i < workingList.size(); i++) {
            if (i == 0) {
                bw.write(workingList.get(i));
            } else {
                bw.write("\n" + workingList.get(i));
            }
        }

        bw.close();
    }

    public static void makeTreeFromIndex() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("git/workingList"));
        StringBuilder tree = new StringBuilder();
        String previousPath = "";
        Boolean isSameDir = true;

        while (br.ready() && isSameDir == true) {
            String line = br.readLine();
            String restOfLine = line.substring(0, line.indexOf("git-project-Ellie/"));
            String currentPath = line.substring(line.indexOf("git-project-Ellie/"), line.lastIndexOf("/"));

            // if previous and current path are the same, or if previous path is empty
            // (first time)
            if (previousPath.equals(currentPath) | previousPath.equals("")) {
                String appendedLine = restOfLine + line.substring(line.lastIndexOf("/") + 1);
                if (tree.isEmpty()) {
                    tree.append(appendedLine);
                } else {
                    tree.append("\n" + appendedLine);
                }
                removeFromWorkingList(line);
                previousPath = currentPath;
            } else {
                isSameDir = false;
                makeTreeFromIndex();
            }

        }
        br.close();

        File treeFile = new File("treeFile");
        BufferedWriter bw = new BufferedWriter(new FileWriter(treeFile));
        bw.write(tree.toString());
        bw.close();

        String finalTreeHash = SHA1Hash(treeFile);
        treeFile.renameTo(new File("git/objects/" + finalTreeHash));

        addToWorkingList("tree " + finalTreeHash + " " + previousPath);
    }

    public static void removeFromWorkingList(String readLine) throws IOException {
        File workingList = new File("git/workingList");
        BufferedReader br = new BufferedReader(new FileReader(workingList));

        File temp = File.createTempFile("temporaryWorkingList", ".txt");
        BufferedWriter bw = new BufferedWriter(new FileWriter(temp));

        while (br.ready()) {
            String line = br.readLine();
            if (!line.equals(readLine)) {
                if (br.ready()) {
                    bw.write(line + "\n");
                } else {
                    bw.write(line);
                }
            }
        }
        temp.renameTo(workingList);
        br.close();
        bw.close();
    }

    public static void addToWorkingList(String addedLine) throws IOException {
        File workingList = new File("git/workingList");
        BufferedReader br = new BufferedReader(new FileReader(workingList));

        File temp = File.createTempFile("temporaryWorkingList", ".txt");
        BufferedWriter bw = new BufferedWriter(new FileWriter(temp));

        bw.write(addedLine);
        while (br.ready()) {
            String line = br.readLine();
            if (!line.equals(addedLine)) {
                bw.write("\n" + line);
            }
        }
        temp.renameTo(workingList);
        br.close();
        bw.close();
    }

}