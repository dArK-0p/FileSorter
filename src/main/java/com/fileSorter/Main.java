package com.fileSorter;

public class Main {
    /**
     * Main entry point of the FileSorter application. Initializes a FileSorter object and
     *  calls the sortFiles method to start the sorting process. Handles potential exceptions
     *  and command-line argument errors.
     *
     * @param args Command-line arguments. The first element (args[0]) is expected to be the directory path.
     */
    public static void main(String[] args) {
        FileSorter fs = new FileSorter();

        try {
            if (args.length != 1) {
                System.err.println("Usage: java -jar <jar_file_name.jar> <directory_path>");
                return;
            }

            String defaultDirectoryPath = args[0];
            fs.sortFiles(defaultDirectoryPath);
        }
        catch (Exception e) { System.err.println("An error occurred during file sorting: " + e.getMessage()); }
    }
} 