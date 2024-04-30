package com.fileSorter;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import java.util.Date;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.mp4.Mp4Directory;

public class VideoFileProcessor {

    DirectoryManager dm = new DirectoryManager();
    Helper help = new Helper();

    /**
     * Checks if a file has a supported video extension.
     *
     * @param extension The file extension (in lowercase).
     * @return True if the extension is supported, false otherwise.
     */
    boolean isSupportedVideo(String extension) {
        return extension.equalsIgnoreCase("mp4") || extension.equalsIgnoreCase("mov") ||
                extension.equalsIgnoreCase("avi") || extension.equalsIgnoreCase("mkv") ||
                extension.equalsIgnoreCase("wmv") || extension.equalsIgnoreCase("flv") ||
                extension.equalsIgnoreCase("3gp") || extension.equalsIgnoreCase("mpeg") ||
                extension.equalsIgnoreCase("mpg") || extension.equalsIgnoreCase("m4v");
    }

    /**
     * Moves a video file to a destination directory based on its creation or modification time.
     * If no valid time is found, moves the file to the "noDateFound" directory.
     *
     * @param filePath The full path of the video file.
     */
    void moveVideoFile(Path filePath) {
        String creationTime = getCreationTimeFromVideo(filePath);
        String modifiedTime = getModificationTimeFromVideo(filePath);
        String validTime = (creationTime != null) ? creationTime : modifiedTime;

        if (validTime == null) {
            dm.moveFileToNoDateFound(filePath);
            return;
        }

        try {
            Path destination = Paths.get(filePath.getParent().toString(), validTime, filePath.getFileName().toString());

            Files.createDirectories(destination.getParent());
            Files.move(filePath, destination, StandardCopyOption.REPLACE_EXISTING);
        }
        catch (IOException e) { System.err.println("Error occurred while moving video file: " + e.getMessage()); }
    }

    /**
     * Attempts to extract the creation time from a video file's metadata (MP4 format).
     * Formats the extracted date if successful.
     *
     * @param filePath The full path of the video file.
     * @return A formatted date string in the "yyyy/MMMM" pattern (UTC timezone), or null if an error occurs or no creation time is found.
     */
    String getCreationTimeFromVideo(Path filePath) {
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(filePath.toFile());
            Mp4Directory directory = metadata.getFirstDirectoryOfType(Mp4Directory.class);

            if (directory != null && directory.containsTag(Mp4Directory.TAG_CREATION_TIME)) {
                Date legacyDate = directory.getDate(Mp4Directory.TAG_CREATION_TIME);

                return help.getFormattedDate(legacyDate);
            }
        }
        catch (Exception e) { System.err.println("Error occurred while extracting creation time from video: " + e.getMessage()); }

        return null;
    }

    /**
     * Attempts to extract the modification time from a video file's metadata (MP4 format).
     * Formats the extracted date if successful.
     *
     * @param filePath The full path of the video file.
     * @return A formatted date string in the "yyyy/MMMM" pattern (UTC timezone), or null if an error occurs or no modification time is found.
     */
    String getModificationTimeFromVideo(Path filePath) {
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(filePath.toFile());
            Mp4Directory directory = metadata.getFirstDirectoryOfType(Mp4Directory.class);

            if (directory != null && directory.containsTag(Mp4Directory.TAG_MODIFICATION_TIME)) {
                Date legacyDate = directory.getDate(Mp4Directory.TAG_MODIFICATION_TIME);

                return help.getFormattedDate(legacyDate);
            }
        }
        catch (Exception e) { System.err.println("Error occurred while extracting modification time from video: " + e.getMessage()); }

        return null;
    }
}