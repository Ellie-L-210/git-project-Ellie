import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOError;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

public class initializeGit {
    public static void main(String[] args) throws IOException {
        initialize();
        // System.out.println("testing SHA1");
        // System.out.println(SHA1Hash("HELLO"));
        createBLOB("hello");
        createBLOB("Hi");
        System.out.println("SHA1 of hell0: " + SHA1Hash("hello"));
        System.out.println(blobExists(SHA1Hash("hello")));
        System.out.println(blobExists(SHA1Hash("Hi")));
        deleteAllBlobs();
        System.out.println(blobExists(SHA1Hash("hello")));
        System.out.println(blobExists(SHA1Hash("Hi")));

        // 2.4.1 Testing
        String one = "hi";
        String two = "hello";
        String three = "hi hello";
        String four = "hello";

        createBLOB(one);
        createBLOB(two);
        createBLOB(three);
        createBLOB(four);

        addToIndex(SHA1Hash(one), "hi.txt");
        addToIndex(SHA1Hash(two), "hello.txt");
        addToIndex(SHA1Hash(three), "hihello.txt");
        addToIndex(SHA1Hash(four), "hello");
        resetAllFiles();

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

    public static String commit(String author, String message) throws IOException {
        String contents = "tree: ";
        String workingListSha = Files.readString(Path.of("git/objects/workinglist")); //edit path name
        workingListSha = workingListSha.substring(workingListSha.indexOf(" ") + 1);
        workingListSha = workingListSha.substring(0, workingListSha.indexOf(" "));
        String parent = Files.readString(Path.of("git/HEAD"));
        contents = contents.concat(workingListSha + "\nparent: " + parent + "\nauthor: " + author + "\ndate: ");
        LocalDate date = LocalDate.now();
        String dateStr = date.toString();
        String year = dateStr.substring(0, dateStr.indexOf("-"));
        dateStr = dateStr.substring(dateStr.indexOf("-") + 1);
        String month = dateStr.substring(0, dateStr.indexOf("-"));
        dateStr = dateStr.substring(dateStr.indexOf("-") + 1);
        if (dateStr.charAt(0) == '0') {
            dateStr = dateStr.substring(1);
        }
        int m = Integer.parseInt(month);
        String fm = "";
        switch (m) {
            case 1:
                fm = "Jan";
                break;
            case 2:
                fm = "Feb";
                break;
            case 3:
                fm = "Mar";
                break;
            case 4:
                fm = "Apr";
                break;
            case 5:
                fm = "May";
                break;
            case 6:
                fm = "Jun";
                break;
            case 7:
                fm = "Jul";
                break;
            case 8:
                fm = "Aug";
                break;
            case 9:
                fm = "Sep";
                break;
            case 10:
                fm = "Oct";
                break;
            case 11:
                fm = "Nov";
                break;
            default:
                fm = "Dec";
        }
        contents = contents.concat(fm + " " + dateStr + ", " + year + "\nmessage: " + message);
        Path p = Path.of("git/objects/tempCommit");
        Files.createFile(p);
        Files.writeString(p, contents);
        String commitSha = SHA1Hash(p.toString());
        Files.move(p, Path.of("git/objects/" + commitSha));
        Files.writeString(Path.of("git/HEAD"), commitSha);
        return ("git/objects/" + commitSha);
    }

    public static String createTree(String path) throws Exception {
        return createTree(path, 0, false);
    }

    public static String createTree(String path, int tempCount) throws Exception {
        return createTree(path, tempCount, false);
    }

    public static String createTree(String path, boolean working) throws Exception {
        return createTree(path, 0, working);
    }

    public static String createTree(String path, int tempCount, boolean working) throws Exception {
        Path parameterPath = Path.of(path);
        // Stream<Path> streamOne = Files.walk(parameterPath);
        // for (Path p : (Iterable<Path>) streamOne::iterator) {
        //     if (checkPath(path, p.toString())) {
        //         continue;
        //     }
        //     System.out.println(p.toString());
        // }
        // System.out.println(" ");
        // streamOne.close();
        // return "";
        Stream<Path> streamTwo = Files.walk(parameterPath);
        boolean isFirst = true;
        Path treePath = Path.of("git/objects/temporary" + String.valueOf(tempCount));
        Files.createFile(treePath);
        for (Path p : (Iterable<Path>) streamTwo::iterator) {
            if (isFirst) {
                isFirst = false;
                continue;
            }
            String dsStr = p.toString();
            if (dsStr.indexOf("DS_Store") != -1) {
                continue;
            }
            if (checkPath(path, dsStr)) {
                continue;
            }
            if (working && notInIndex(p)) {
                continue;
            }
            if (Files.isDirectory(p)) {
                tempCount++;
                String workingSha = createTree(p.toString(), tempCount);
                if (Files.size(treePath) != 0) {
                    Files.writeString(treePath, "\n", StandardOpenOption.APPEND);
                }
                Files.writeString(treePath, "tree " + SHA1Hash("git/objects/" + workingSha) + " " + p.toString(),
                        StandardOpenOption.APPEND);
            } else {
                if (Files.size(treePath) != 0) {
                    Files.writeString(treePath, "\n", StandardOpenOption.APPEND);
                }
                Files.writeString(treePath, "blob " + SHA1Hash(p.toString()) + " " + p.toString(),
                        StandardOpenOption.APPEND);
                if (!working) {
                    createBLOB(p.toString());
                }
            }
        }
        // streamOne.close();
        streamTwo.close();
        String shaOne = SHA1Hash(treePath.toString());
        try {
            Files.move(treePath, Path.of("git/objects/" + shaOne));
        } catch (FileAlreadyExistsException e) {
            Files.delete(treePath);
        }
        return shaOne;
    }

    public static boolean checkPath(String enclose, String inside) {
        try {
            inside = inside.substring(enclose.length() + 1);
            return (inside.indexOf("/") != -1);
        } catch (StringIndexOutOfBoundsException e) {
            return false;
        }
    }

    public static boolean notInIndex(Path path) throws IOException {
        List<String> entireIndex = Files.readAllLines(Path.of("git/index"));
        String all = "";
        for (String s : entireIndex) {
            all = all.concat(s);
        }
        return (all.indexOf(path.toString()) == -1);
    }

    public static boolean generateWorkingList() throws Exception {
        List<String> all = Files.readAllLines(Path.of("git/index"));
        String first;
        if (all.size() != 0) {
            first = all.get(0);
        } else {
            return false;
        }
        first = first.substring(first.indexOf(" ") + 1, first.indexOf("/"));
        Path p = Path.of("git/objects/workinglist");
        try {
            Files.createFile(p);
        } catch (FileAlreadyExistsException e) {
            
        }
        Files.writeString(p, "tree " + createTree(first, true) + " " + first);
        String shaOne = SHA1Hash("git/objects/workinglist");
        Files.writeString(p, "tree " + shaOne + " (root)");
        return true;
    }

    public static String readFile(String path) throws IOException {
        return Files.readString(Path.of(path));
    }
}