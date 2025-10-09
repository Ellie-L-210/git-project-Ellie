import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class GitWrapper {

    /**
     * Initializes a new Git repository.
     * This should create the necessary directory structure
     * and initial files required for a Git repository.
     * This should create the initial commit and update HEAD accordingly
     */
    public void init() throws IOException {
        GPTester.reset();
        initializeGit.initialize();
    }

    /**
     * Stages a file for the next commit.
     *
     * @param filePath The path to the file to be staged.
     */
    public void add(String filePath) throws Exception {
        String contents = Files.readString(Path.of(filePath));
        initializeGit.addToIndex(initializeGit.SHA1Hash(contents), filePath);
        initializeGit.createBLOB(contents);
        initializeGit.generateWorkingList();
    }

    /**
     * Creates a commit with the given author and message.
     * It should capture the current state of the repository,
     * update the HEAD, and return the commit hash.
     *
     * @param author  The name of the author making the commit.
     * @param message The commit message describing the changes.
     * @return The SHA1 hash of the new commit.
     */
    public String commit(String author, String message) throws IOException {
        return initializeGit.commit(author, message);
    }

    /**
     * EXTRA CREDIT: Checks out a specific commit given its hash.
     * This should update the working directory to match the
     * state of the repository at that commit.
     *
     * @param commitHash The SHA1 hash of the commit to check out.
     * @throws IOException 
     */
    public void checkout(String commitHash) throws IOException {
        // String contents = Files.readString(Path.of("git/objects/" + commitHash));
        // contents = contents.substring(contents.indexOf(":") + 2);
        // contents = contents.substring(0, contents.indexOf("\n"));
    }
}
