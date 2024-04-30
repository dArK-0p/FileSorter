package com.fileSorter;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import java.util.Date;

public class ImageFileProcessor {

    DirectoryManager dm = new DirectoryManager();
    Helper help = new Helper();

    /**
     * Checks if a file has a supported image extension.
     *
     * @param extension The file extension (in lowercase).
     * @return True if the extension is supported, false otherwise.
     */
    boolean isSupportedImage(String extension) {
        return extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("jpeg") ||
                extension.equalsIgnoreCase("gif") || extension.equalsIgnoreCase("bmp") ||
                extension.equalsIgnoreCase("tiff") || extension.equalsIgnoreCase("webp");
    }

    /**
     * Moves an image file to a destination directory based on its 'date taken' or modification time.
     * If no valid time is found, moves the file to the "noDateFound" directory.
     *
     * @param filePath The full path of the image file.
     */
    void moveImageFile(Path filePath) {
        String dateTaken = getDateTakenFromImage(filePath);
        String dateModified = getDateModifiedFromImage(filePath);
        String validTime = (dateTaken != null) ? dateTaken : dateModified;

        if (validTime == null) {
            dm.moveFileToNoDateFound(filePath);
            return;
        }

        try {
            Path destination = Paths.get(filePath.getParent().toString(), validTime, filePath.getFileName().toString());

            Files.createDirectories(destination.getParent());
            Files.move(filePath, destination, StandardCopyOption.REPLACE_EXISTING);
        }
        catch (IOException e) { System.err.println("Error occurred while moving image file: " + e.getMessage()); }
    }

    /**
     * Attempts to extract the 'date taken' from an image file's EXIF metadata.
     * Formats the extracted date if successful.
     *
     * @param filePath The full path of the image file.
     * @return A formatted date string in the "yyyy/MMMM" pattern (UTC timezone), or null if an error occurs or no 'date taken' is found.
     */
    String getDateTakenFromImage(Path filePath) {
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(filePath.toFile());
            ExifSubIFDDirectory directory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);

            if (directory != null && directory.containsTag(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL)) {
                Date legacyDate = directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);

                return help.getFormattedDate(legacyDate);
            }
        }
        catch (Exception e) { System.err.println("Error occurred while extracting date taken from image: " + e.getMessage()); }

        return null;
    }

    /**
     * Attempts to extract the modification time from an image file's EXIF metadata.
     * Formats the extracted date if successful.
     *
     * @param filePath The full path of the image file.
     * @return A formatted date string in the "yyyy/MMMM" pattern (UTC timezone), or null if an error occurs or no modification time is found.
     */
    String getDateModifiedFromImage(Path filePath) {
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(filePath.toFile());
            ExifSubIFDDirectory directory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);

            if (directory != null && directory.containsTag(ExifSubIFDDirectory.TAG_DATETIME)) {
                Date legacyDate = directory.getDate(ExifSubIFDDirectory.TAG_DATETIME);
                return help.getFormattedDate(legacyDate);
            }
        }
        catch (Exception e) { System.err.println("Error occurred while extracting date taken from image: " + e.getMessage()); }

        return null;
    }
}