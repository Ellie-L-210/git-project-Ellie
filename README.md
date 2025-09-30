# git-project-Ellie
initializeGit.java
    initialize()
        initaializes all parts of the repository in one method.
        initalizes the git and objects directories, and initializes 2 files in the git directory (index and HEAD).
        If the directories and files already exist, an already exists message is outputted, otherwise all objects are created and a creation message is outputted. 

    SHA1Hash()
        Utilizes SHA1 code from geeksforgeeks to create and return hash values for inputs of text.

    createBLOB()
        Utilizes SHA1Hash() to make a new file in the objects directory with the hash as the file name, does not make a new file if the file already exists. 

    blobExists()
        Returns true/false depending on if the blob exists. 

    deleteAllBlobs()
        Deletes all blobs in the objects directory.
    
    addToIndex()
        Adds sha value and file name to index file on a new line.
    
    deleteAllFiles()
        Deletes all blobs and clears index file for easier testing.

GPTester.java
    verifyAll()
        returns true if all directories (git and objects) and files (index and HEAD) exist.
    reset()
        deletes all files in the git and objects directories first, then deletes the now empty directories.