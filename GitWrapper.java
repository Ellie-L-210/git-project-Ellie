import java.io.File;
import java.io.IOException;

public class GitWrapper {

    /**
     * Initializes a new Git repository.
     * This method creates the necessary directory structure
     * and initial files (index, HEAD) required for a Git repository.
     */
    public void init() throws IOException {
        Git.init();
    };

    /**
     * Stages a file for the next commit.
     * This method adds a file to the index file.
     * If the file does not exist, it throws an IOException.
     * If the file is a directory, it throws an IOException.
     * If the file is already in the index, it does nothing.
     * If the file is successfully staged, it creates a blob for the file.
     * 
     * @param filePath The path to the file to be staged.
     * @throws IOException
     */
    public void add(String filePath) throws IOException {
        File file = new File(filePath);
        Git.createBLOB(file);
        Git.addToIndex(Git.SHA1Hash(file), file);
    };

}