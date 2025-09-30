import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class GPTester {
    public static void main(String[] args) throws IOException {
        // initalize then reset at least 5 times
        initializeGit.initialize();
        reset();
        initializeGit.initialize();
        reset();
        initializeGit.initialize();
        reset();
        initializeGit.initialize();
        reset();
        initializeGit.initialize();
    }

    static File git = new File("git");
    static File objects = new File("objects");
    static File index = new File("git/index");
    static File HEAD = new File("git/HEAD");

    public static boolean verifyAll() {
        // if all directories (git and objects) and files (index and HEAD) exist, return
        // true
        if (verifyGit() && verifyHEAD() && verifyIndex() && verifyObjects()) {
            return true;
        }
        return false;
    }

    public static boolean verifyGit() {
        return git.exists();
    }

    public static boolean verifyObjects() {
        return objects.exists();
    }

    public static boolean verifyIndex() {
        return index.exists();
    }

    public static boolean verifyHEAD() {
        return HEAD.exists();
    }

    // make deletion recursive, since you can't delete directories that are not
    // empty
    public static void reset() {
        File[] gitFiles = git.listFiles();
        if (gitFiles != null) {
            for (File file : gitFiles) {
                file.delete();
            }
        }

        File[] objectsFiles = objects.listFiles();
        if (objectsFiles != null) {
            for (File file2 : objectsFiles) {
                file2.delete();
            }
        }

        git.delete();
        objects.delete();
    }
}
