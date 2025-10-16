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

        // Tree Tests
        // TEST: if createTree works given a directory
        resetTestDirectory("groceryStore");
        makeTestDirectory();
        System.out.println(Git.createTree("groceryStore"));

        // TEST: if createTreeFromIndex works
        Git.resetAllFiles();
        leagueOfLegends();
        Git.treeFromIndex();
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