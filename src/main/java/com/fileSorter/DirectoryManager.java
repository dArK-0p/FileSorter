package com.fileSorter;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class DirectoryManager {
    /**
     * Creates a "noDateFound" directory within the provided directory path
     * to store files without identifiable dates.
     *
     * @param directory The base directory where the "noDateFound" directory will be created.
     */
    void createInitialDirectories(Path directory) {
        try {
            Path noDateFound = directory.resolve("noDateFound");

            Files.createDirectories(noDateFound);
        }
        catch (IOException e) { System.err.println("Error occurred while creating initial directories: " + e.getMessage()); }
    }

    /**
     * Moves a file to a "noDateFound" subdirectory. Creates the destination path if necessary.
     * Handles potential overwriting of existing files.
     *
     * @param filePath The full path of the file to be moved.
     */
    void moveFileToNoDateFound(Path filePath) {
        try {
            Path destination = Paths.get(filePath.getParent().toString(), "noDateFound", filePath.getFileName().toString());

            Files.createDirectories(destination.getParent());
            Files.move(filePath, destination, StandardCopyOption.REPLACE_EXISTING);
        }
        catch (IOException e) { System.err.println("Error occurred while moving file to noDateFound: " + e.getMessage()); }
    }
}