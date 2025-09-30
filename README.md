# git-project-Ellie
initializeGit.java
    initialize()
        initaializes all parts of the repository in one method.
        initalizes the git and objects directories, and initializes 2 files in the git directory (index and HEAD).
        If the directories and files already exist, an already exists message is outputted, otherwise all objects are created and a creation message is outputted. 

GPTester.java
    verifyAll()
        returns true if all directories (git and objects) and files (index and HEAD) exist.
    reset()
        deletes all files in the git and objects directories first, then deletes the now empty directories.