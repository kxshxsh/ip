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
                details[0] = details[0].trim();
                int status = Integer.parseInt(details[1].trim());
                Task currTask;
                if (details[0].equals("T")) {
                    currTask = new Todo(details[2].trim());
                } else if (details[0].equals("D")) {
                    currTask = new Deadline(details[2].trim(), details[3].trim());
                } else {
                    currTask = new Event(details[2].trim(), details[3].trim());
                }

                if (status == 1) {
                    currTask.markDone();
                }

                TaskList.MANAGER.add(currTask);
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
            for (Task t : TaskList.MANAGER) {
                String output;
                if (t instanceof Todo) {
                    output = "T ; " + (t.isDone ? "1 ; " : "0 ; ") + t.description;
                } else if (t instanceof Deadline) {
                    output = "D ; " + (t.isDone ? "1 ; " : "0 ; ") + t.description + " ; " + ((Deadline) t).time;
                } else {
                    output = "E ; " + (t.isDone ? "1 ; " : "0 ; ") + t.description + " ; " + ((Event) t).time;
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
