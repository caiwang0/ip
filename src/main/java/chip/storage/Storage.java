package chip.storage;

import chip.task.*;
import chip.ChipException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Handles loading and saving of tasks to and from a file.
 * Manages file I/O operations and data persistence for the task management system.
 */
public class Storage {
    private final String filePath;

    /**
     * Constructs a Storage instance with the specified file path.
     *
     * @param filePath the path to the file for storing task data
     */
    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads tasks from the storage file.
     * Parses the file format and creates appropriate Task objects.
     *
     * @return ArrayList of tasks loaded from the file, empty list if file doesn't exist
     * @throws ChipException if file is corrupted or cannot be read
     */
    public ArrayList<Task> load() throws ChipException {
        ArrayList<Task> tasks = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) {
            return tasks;
        }

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                String[] parts = line.split(" \\| ");
                Task task = null;

                switch (parts[0]) {
                    case "T":
                        task = new Todo(parts[2]);
                        break;
                    case "D":
                        task = new Deadline(parts[2], parts[3]);
                        break;
                    case "E":
                        task = new Event(parts[2], parts[3], parts[4]);
                        break;
                }

                if (task != null) {
                    if (parts[1].equals("1")) {
                        task.markAsDone();
                    }
                    tasks.add(task);
                }
            }
        } catch (FileNotFoundException e) {
            throw new ChipException("Data file not found. Starting fresh.");
        } catch (Exception e) {
            throw new ChipException("Error loading tasks from file. The file might be corrupted.");
        }
        return tasks;
    }

    /**
     * Saves the list of tasks to the storage file.
     * Creates the directory if it doesn't exist and writes tasks in the proper format.
     *
     * @param tasks the list of tasks to save
     * @throws ChipException if an I/O error occurs during saving
     */
    public void save(ArrayList<Task> tasks) throws ChipException {
        try {
            File file = new File(filePath);
            File directory = file.getParentFile();
            if (directory != null && !directory.exists()) {
                directory.mkdirs();
            }

            try (FileWriter writer = new FileWriter(file)) {
                for (Task task : tasks) {
                    writer.write(task.toFileString() + System.lineSeparator());
                }
            }
        } catch (IOException e) {
            throw new ChipException("An error occurred while saving tasks: " + e.getMessage());
        }
    }
}