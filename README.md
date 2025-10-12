# git-project-Ellie
initializeGit.java
    initialize()
        initaializes all parts of the repository in one method.
        initalizes the git and objects directories, and initializes 2 files in the git directory (index and HEAD).
        If the directories and files already exist, an already exists message is outputted, otherwise all objects are created and a creation message is outputted. 

    SHA1Hash(File original)
        Reads file contents from original and stores in input variable, then
        utilizes SHA1 code from geeksforgeeks to create and return hash values for inputs of text.

    createBLOB(File original)
        Gets hash from original's file contents via SHA1Hash(original) and stores in hash variable. If blobExists(hash, original) returns false, a new blob is added to the objects directory with the file name being the hash, and the contents from original copied into it. Does not make a new blob if blobExists(hash, original) returns true.

    blobExists(String hash, File original)
        Returns true if a blob has been added to the index file with the same path and hash. Returns false in any other instance, including if a listed file in the index has the same path but different hash. 

    isIndexed(String hash, File original)
        Reads through the index file with a BufferedReader, returning true if an indexed file with the same path and hash as the parameters are found. Returns false in all other instances, including if a file with a different path or name exists with the same hash as the parameter. Does not check for updated files with new hash values, as this is checked in addToIndex().

    addToIndex(String hash, File original)
        Makes a temporary text file called temporary.txt in case a file which has already been indexed has been changed (e.g. change in the file's contents). If file has not been already indexed via a false from isIndexed(hash, original), reads through each line of the index file copying each line to temporary.txt unless the read line contains the same path but different hash than the parameters. If this line is found, the boolean indexChanged will be changed to true. If indexChanged is true, temporary.txt will become the index file. In any other case, the standard index entry format will be used to add the inputted hash and original file from the parameters, and temporary.txt will be deleted.

    deleteAllBlobs()
        Deletes all blobs in the objects directory.
    
    deleteAllFiles()
        Deletes all blobs via deleteAllBlobs() and clears index file for easier testing.

GPTester.java
    verifyAll()
        returns true if all directories (git and objects) and files (index and HEAD) exist.
    reset()
        deletes all files in the git and objects directories first, then deletes the now empty directories.