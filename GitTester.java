import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class GitTester {

    public static void main(String args[]) throws IOException {
        /* Your tester code goes here */
        GitWrapper gw = new GitWrapper();

        // init() tests
        gw.init();

        // deleting index and HEAD to be reinitialized
        File index = new File("git/index");
        File HEAD = new File("git/HEAD");
        index.delete();
        HEAD.delete();

        gw.init();

        // deleting object to be reinitialized
        File objects = new File("git/objects");
        objects.delete();
        gw.init();

        // add() tests
        File addTestFile = new File("addTestFile");

        BufferedWriter addWriter = new BufferedWriter(new FileWriter(addTestFile));
        addWriter.write("this is the initial writing for the add test file.");
        addWriter.close();

        // adding same file twice
        gw.add(addTestFile.getPath());
        gw.add(addTestFile.getPath());

        BufferedWriter addWriter2 = new BufferedWriter(new FileWriter(addTestFile));
        addWriter2.write(" this is the modified addition to the test file.");
        addWriter2.close();
        // adding a modified file
        gw.add(addTestFile.getPath());

        createGivenTestFiles();
        gw.add("myProgram/hello.txt");
        gw.add("myProgram/inner/world.txt");

        // commit() tests
        // gw.commit("John Doe", "Initial commit");
    }

    public static void createGivenTestFiles() throws IOException {
        File myProgram = new File("myProgram");
        myProgram.mkdir();

        File inner = new File("myProgram/inner");
        inner.mkdir();

        File world = new File("myProgram/inner/world.txt");
        BufferedWriter worldWriter = new BufferedWriter(new FileWriter(world));
        worldWriter.write("this is the world file!");
        worldWriter.close();

        File hello = new File("myProgram/hello.txt");
        BufferedWriter helloWriter = new BufferedWriter(new FileWriter(hello));
        helloWriter.write("hello world!");
        helloWriter.close();
    }
}