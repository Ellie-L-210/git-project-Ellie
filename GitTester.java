import java.io.IOException;

public class GitTester {

    public static void main(String args[]) throws Exception {
		GitWrapper gw = new GitWrapper();
        gw.add("myProgram/hello.txt");
        gw.add("myProgram/inner/world.txt");
        gw.commit("John Doe", "Initial commit");
        gw.checkout("74d475aeeced024a5e9984b7a6f5e0ffd091ccbc");
    }
}