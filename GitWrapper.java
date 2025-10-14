import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

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
    public void checkout(String commitHash) throws IOException { // chatGPT
        Path commitPath = Path.of("git/objects", commitHash);
        if (!Files.exists(commitPath)) {
            System.out.println("Commit not found: " + commitHash);
            return;
        }

        // Step 1: read commit file and extract tree hash
        String commitContents = Files.readString(commitPath);
        String treeLine = Arrays.stream(commitContents.split("\n"))
                .filter(line -> line.startsWith("tree:"))
                .findFirst()
                .orElseThrow(() -> new IOException("No tree found in commit"));
        String treeHash = treeLine.substring(treeLine.indexOf(":") + 2).trim();

        // Step 2: read tree object
        Path treePath = Path.of("git/objects", treeHash);
        if (!Files.exists(treePath)) {
            throw new IOException("Tree object not found: " + treeHash);
        }
        String treeContents = Files.readString(treePath);

        // Step 3: clear working directory (optional: skip .git folder)
        try (var paths = Files.walk(Path.of("."))) {
            paths.filter(Files::isRegularFile)
                    .filter(p -> !p.startsWith("git"))
                    .forEach(p -> {
                        try {
                            Files.delete(p);
                        } catch (IOException e) {
                            System.err.println("Could not delete: " + p);
                        }
                    });
        }

        // Step 4: restore each file from blobs listed in tree
        for (String line : treeContents.split("\n")) {
            if (line.isBlank())
                continue;
            String[] parts = line.split(":");
            if (parts.length != 2)
                continue;

            String filePath = parts[0].trim();
            String blobHash = parts[1].trim();

            Path blobPath = Path.of("git/objects", blobHash);
            if (!Files.exists(blobPath)) {
                System.err.println("Missing blob: " + blobHash);
                continue;
            }

            String blobContents = Files.readString(blobPath);
            Path dest = Path.of(filePath);
            if (dest.getParent() != null) {
                Files.createDirectories(dest.getParent());
            }
            Files.writeString(dest, blobContents);
        }
    }
}
