package duke;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Storage {
    private final File f;

    public Storage(String filePath) {
        this.f = new File(filePath);
    }

    public void load() throws CustomException {
        try {
            if (!(f.exists() && !f.isDirectory())) {
                f.getParentFile().mkdirs();
                f.createNewFile();
            }

            Scanner sc = new Scanner(f);
            while (sc.hasNextLine()) {
                String input = sc.nextLine();
                String[] details = input.split(";", 4);
                Task currTask = Parser.parseCommandFromFile(details);
                TaskList.manager.add(currTask);
            }
            sc.close();
        } catch (IOException e) {
            throw new CustomException("File cannot be created");
        }
    }

    public void addToFile() {
        try {
            if (!(f.exists() && !f.isDirectory())) {
                f.getParentFile().mkdirs();
                f.createNewFile();
            }
            FileWriter writer = new FileWriter(f);
            for (Task t : TaskList.manager) {
                String output;
                if (t instanceof Todo) {
                    output = "T ; " + (t.isDone ? "1 ; " : "0 ; ") + t.description;
                } else if (t instanceof Deadline) {
                    output = "D ; " + (t.isDone ? "1 ; " : "0 ; ") + t.description + " ; "
                            + ((Deadline) t).displayTimeInOriginalFormat();
                } else {
                    output = "E ; " + (t.isDone ? "1 ; " : "0 ; ") + t.description + " ; "
                            + ((Event) t).displayTimeInOriginalFormat();
                }
                writer.write(output + "\n");
                writer.flush();
            }
            writer.close();
        } catch (IOException e) {
            System.out.print(e.getMessage());
        }
    }
}
