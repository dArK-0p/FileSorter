Section 1: Overview

    Purpose:

    "This application automates the organization of image and video files based on dates extracted from their embedded metadata."

    Features:
        -> Supports common image formats (JPG, JPEG, PNG, TIFF, etc.)

        -> Supports various video formats (MP4, MOV, AVI, etc.)

        -> Extracts creation and modification dates from metadata.

        -> Organizes files into folders based on the year and month of the extracted dates.

        -> Handles files without valid dates by placing them in a designated "noDateFound" directory.

Section 2: Project Structure

    -> FileSorter: The main entry point of the application. Initializes dependencies, lists files, and coordinates file processing.

    -> ImageFileProcessor: Handles image file operations – metadata extraction, date formatting, and file movement.

    -> VideoFileProcessor: Responsible for video file operations, similar to ImageFileProcessor.

    -> DirectoryManager: Manages the creation of the "noDateFound" directory and file movement into it.

    -> Helper: A utility class for date formatting and other helper functions.

Section 3: Dependencies

    -> Metadata-Extractor (version x.x.x): A library to extract metadata from image and video files.

    -> Java 8 (or later): The required Java Runtime Environment (JRE) or Java Development Kit (JDK).

Section 4: Getting Started

    -> Prerequisites:
        Java Development Kit (JDK) version 8 or later installed and configured on your system.

    -> Building (if applicable):
        Using Maven: Run mvn clean package in the project's root directory.

    -> Running:
        Command line: java FileSorter /path/to/your/directory
            Replace /path/to/your/directory with the actual path to the directory containing your image and video files.

Section 5: Usage

    -> File Formats:
        Images: JPG, JPEG, PNG, TIFF, GIF, BMP, etc. (List the specific formats supported by your ImageFileProcessor)
        Videos: MP4, MOV, AVI, MKV, etc. (List the specific formats supported by your VideoFileProcessor)

    -> Metadata:
        Images: Prioritizes "Date Taken" from EXIF metadata. Falls back to "Date Modified" if "Date Taken" is not found.
        Videos: Extracts "Creation Time" from MP4 metadata. Falls back to "Modification Time" if available.

    -> Output:
        The application will create subfolders within the source directory based on the extracted dates using the format YYYY/MMMM (e.g., "2023/November").
        Files without identifiable dates will be placed in a "noDateFound" subdirectory.