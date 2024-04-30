package com.fileSorter;

import java.io.IOException;

import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FileSorter {

     VideoFileProcessor vfp = new VideoFileProcessor();
     ImageFileProcessor ifp = new ImageFileProcessor();
     DirectoryManager dm = new DirectoryManager();

    /**
     * Initiates the file sorting process. Creates necessary directories, iterates through files
     * in the provided directory, and dispatches file processing tasks to a thread pool.
     *
     * @param directoryPath The path to the directory containing files to be sorted.
     */
     void sortFiles(String directoryPath) {
        try {
            Path dir = Paths.get(directoryPath);
            dm.createInitialDirectories(dir);

            DirectoryStream<Path> stream = Files.newDirectoryStream(dir);
            ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

            for (Path file : stream) {
                if (Files.isRegularFile(file))
                    executorService.execute(() -> processFile(file));
            }

            executorService.shutdown();
            stream.close();
        }
        catch (IOException e) { System.err.println("Error occurred while sorting files: " + e.getMessage()); }
    }

    /**
     * Handles the processing of a single file. Determines the file type based on its extension
     * and delegates the moving operation to the appropriate processor (ImageFileProcessor or VideoFileProcessor).
     *
     * @param filePath The full path of the file to be processed.
     */
    void processFile(Path filePath) {
        try {
            String extension = getFileExtension(filePath);

            if (ifp.isSupportedImage(extension))
                ifp.moveImageFile(filePath);
            else if (vfp.isSupportedVideo(extension))
                vfp.moveVideoFile(filePath);
        }
        catch (Exception e){ System.err.println("Error occurred while processing file " + filePath + ": " + e.getMessage()); }
    }

    /**
     * Extracts the file extension from a given file path.
     *
     * @param filePath The full path of the file.
     * @return The file extension (in lowercase) or an empty string if no extension is found.
     */
    String getFileExtension(Path filePath) {
        String fileName = filePath.getFileName().toString();
        int lastDotIndex = fileName.lastIndexOf(".");

        return (lastDotIndex != -1 && lastDotIndex < fileName.length() - 1) ? fileName.substring(lastDotIndex + 1).toLowerCase() : "";
    }
}