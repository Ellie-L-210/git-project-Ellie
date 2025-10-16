import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

public class GPTester {
    public static void main(String[] args) throws IOException {
        // GP 2.0 Tests
        // TEST: test init() functionality
        // initalize then reset at least 5 times
        // Git.init();
        // reset();
        // Git.init();
        // reset();
        // Git.init();
        // reset();
        // Git.init();
        // reset();
        // Git.init();

        // GP 3.0 Tests
        // TEST: createBLOB and addToIndex work for new unique files
        // File folder1 = new File("folder1");
        // folder1.mkdir();
        // File test1 = new File("folder1/test1.txt");
        // File test2 = new File("test2.txt");
        // File test3 = new File("folder1/test3.txt");

        // BufferedWriter br1 = new BufferedWriter(new FileWriter(test1));
        // BufferedWriter br2 = new BufferedWriter(new FileWriter(test2));
        // BufferedWriter br3 = new BufferedWriter(new FileWriter(test3));

        // br1.write("hi this is test file1.");
        // br2.write("hello this is test file2.");
        // br3.write("hello hi this is test file3.");

        // br1.close();
        // br2.close();
        // br3.close();

        // Git.createBLOB(test1);
        // Git.createBLOB(test2);
        // Git.createBLOB(test3);

        // Git.addToIndex(Git.SHA1Hash(test1), test1);
        // Git.addToIndex(Git.SHA1Hash(test2), test2);
        // Git.addToIndex(Git.SHA1Hash(test3), test3);

        // TEST: if addToIndex ignores the SAME file with the same hash from the same
        // directory - YES
        // Git.addToIndex(Git.SHA1Hash(test1), test1);

        // TEST: if addToIndex will add the SAME file in a DIFFERENT directory - YES
        // File diffDirectoryTest1 = new File("git/text1.txt");
        // BufferedWriter brDiffDirectory = new BufferedWriter(new
        // FileWriter(diffDirectoryTest1));
        // brDiffDirectory.write("hi this is test file1.");
        // brDiffDirectory.close();
        // Git.addToIndex(Git.SHA1Hash(diffDirectoryTest1), diffDirectoryTest1);

        // TEST: if addToIndex will add a file with the SAME contents, DIFFERENT name -
        // YES
        // File test4 = new File("test4.txt");
        // BufferedWriter br4 = new BufferedWriter(new FileWriter(test4, true));
        // br4.write("hi this is test file1.");
        // Git.addToIndex(Git.SHA1Hash(test4), test4);
        // System.out.println(Git.SHA1Hash(test4));

        // TEST: if addToIndex will update the index file when a CURRENT file is
        // // MODIFIED - YES
        // br4.write("this is the modification to file test4!");
        // br4.close();
        // System.out.println(Git.SHA1Hash(test4));
        // Git.addToIndex(Git.SHA1Hash(test4), test4);

        // TEST: if createTree works - YES
        // resetTestDirectory("groceryStore");
        // makeTestDirectory();
        // System.out.println(Git.createTree("groceryStore"));

        // TEST: if treeFromIndex() and supporting methods work for a larger directory -
        // YES
        // Git.resetAllFiles();
        // leagueOfLegends();
        // Git.treeFromIndex();
    }

    // GP 2.0 TEST METHODS
    static File git = new File("git");
    static File objects = new File("objects");
    static File index = new File("git/index");
    static File HEAD = new File("git/HEAD");

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

    // GP 3.0 TEST METHODS
    public static void makeTestDirectory() throws IOException {
        File groceryStore = new File("groceryStore");
        groceryStore.mkdir();

        File fruits = new File("groceryStore/fruits");
        fruits.mkdir();

        // File vegetables = new File("groceryStore/vegetables");
        // vegetables.mkdir();

        File apple = new File("groceryStore/fruits/apple");
        BufferedWriter appleWriter = new BufferedWriter(new FileWriter(apple));
        appleWriter.write("this is the apple file!");
        appleWriter.close();

        File broccoli = new File("groceryStore/broccoli");
        BufferedWriter broccoliWriter = new BufferedWriter(new FileWriter(broccoli));
        broccoliWriter.write("this is the broccoli file!");
        broccoliWriter.close();

        Git.addToIndex(Git.SHA1Hash(broccoli), broccoli);
        Git.addToIndex(Git.SHA1Hash(apple), apple);

    }

    public static void resetTestDirectory(String currentDirPath) throws IOException {
        File currentDir = new File(currentDirPath);
        File[] files = currentDir.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    resetTestDirectory(file.getPath());
                } else {
                    file.delete();
                }
            }

            currentDir.delete();
        }
    }

    public static void leagueOfLegends() throws IOException {
        File LoL = new File("LoL");
        LoL.mkdir();

        // Top + laners
        File top = new File("LoL/top");
        top.mkdir();

        File aatrox = new File("LoL/top/aatrox");
        BufferedWriter aatroxWriter = new BufferedWriter(new FileWriter(aatrox));
        aatroxWriter.write("this is aatrox");
        aatroxWriter.close();

        File jayce = new File("LoL/top/jayce");
        BufferedWriter jayceWriter = new BufferedWriter(new FileWriter(jayce));
        jayceWriter.write("this is jayce");
        jayceWriter.close();

        // Jungle + champs
        File jungle = new File("LoL/jungle");
        jungle.mkdir();

        File lillia = new File("LoL/jungle/lillia");
        BufferedWriter lilliaWriter = new BufferedWriter(new FileWriter(lillia));
        lilliaWriter.write("this is lillia");
        lilliaWriter.close();

        File nunuAndWillump = new File("LoL/jungle/nunuAndWillump");
        BufferedWriter nunuAndWillumpWriter = new BufferedWriter(new FileWriter(nunuAndWillump));
        nunuAndWillumpWriter.write("this is nunu and willump");
        nunuAndWillumpWriter.close();

        // Mid + laners
        File mid = new File("LoL/mid");
        mid.mkdir();

        File syndra = new File("LoL/mid/syndra");
        BufferedWriter syndraWriter = new BufferedWriter(new FileWriter(syndra));
        syndraWriter.write("this is syndra");
        syndraWriter.close();

        File ahri = new File("LoL/mid/ahri");
        BufferedWriter ahriWriter = new BufferedWriter(new FileWriter(ahri));
        ahriWriter.write("this is ahri");
        ahriWriter.close();

        // Bot + laners
        File bot = new File("LoL/bot");
        bot.mkdir();

        File ziggs = new File("LoL/bot/ziggs");
        BufferedWriter ziggsWriter = new BufferedWriter(new FileWriter(ziggs));
        ziggsWriter.write("this is ziggs");
        ziggsWriter.close();

        File caitlyn = new File("LoL/bot/caitlyn");
        BufferedWriter caitlynWriter = new BufferedWriter(new FileWriter(caitlyn));
        caitlynWriter.write("this is caitlyn");
        caitlynWriter.close();

        // Support + laners
        File support = new File("LoL/support");
        support.mkdir();

        File seraphine = new File("LoL/support/seraphine");
        BufferedWriter seraphineWriter = new BufferedWriter(new FileWriter(seraphine));
        seraphineWriter.write("this is seraphine");
        seraphineWriter.close();

        File yuumi = new File("LoL/support/yuumi");
        BufferedWriter yuumiWriter = new BufferedWriter(new FileWriter(yuumi));
        yuumiWriter.write("this is yuumi");
        yuumiWriter.close();

        Git.addToIndex(Git.SHA1Hash(aatrox), aatrox);
        Git.addToIndex(Git.SHA1Hash(jayce), jayce);
        Git.addToIndex(Git.SHA1Hash(lillia), lillia);
        Git.addToIndex(Git.SHA1Hash(nunuAndWillump), nunuAndWillump);
        Git.addToIndex(Git.SHA1Hash(syndra), syndra);
        Git.addToIndex(Git.SHA1Hash(ahri), ahri);
        Git.addToIndex(Git.SHA1Hash(ziggs), ziggs);
        Git.addToIndex(Git.SHA1Hash(caitlyn), caitlyn);
        Git.addToIndex(Git.SHA1Hash(seraphine), seraphine);
        Git.addToIndex(Git.SHA1Hash(yuumi), yuumi);
    }
}
