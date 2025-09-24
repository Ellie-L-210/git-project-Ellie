import java.io.File;
import java.io.IOError;
import java.io.IOException;

public class initializeGit {
    public static void main(String[] args) throws IOException {
        initialize();
        System.out.println("done");
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
}