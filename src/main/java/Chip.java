import java.util.Scanner;
import java.util.ArrayList;

public class Chip {

    public static void main(String[] args) {
        String horizontalLine = "____________________________________________________________";

        System.out.println(horizontalLine);
        System.out.println(" Hello! I'm Chip");
        System.out.println(" What can I do for you?");
        System.out.println(horizontalLine);

        ArrayList<Task> tasks = new ArrayList<>();

        Scanner in = new Scanner(System.in);
        while (true) {
            String command = in.nextLine();
            String[] parts = command.split(" ", 2);
            String action = parts[0];

            System.out.println(horizontalLine);

            if (action.equals("bye")) {
                break;
            } else if (action.equals("list")) {
                System.out.println(horizontalLine);
                for (int i = 0; i < tasks.size(); i++) {
                    System.out.println(" " + (i + 1) + ". " + tasks.get(i));
                }
                System.out.println(horizontalLine);
            } else if (action.equals("mark")) {
                int taskNumber = Integer.parseInt(parts[1]);
                Task task = tasks.get(taskNumber - 1);
                task.markAsDone();
                System.out.println(" Nice! I've marked this task as done:");
                System.out.println("   " + task);
            } else if (action.equals("unmark")) {
                int taskNumber = Integer.parseInt(parts[1]);
                Task task = tasks.get(taskNumber - 1);
                task.markAsNotDone();
                System.out.println(" OK, I've marked this task as not done yet:");
                System.out.println("   " + task);
            } else {
                Task newtask = new Task(command);
                tasks.add(newtask);
                System.out.println(horizontalLine);
                System.out.println(" added: " + command);
                System.out.println(horizontalLine);
            }
        }
        System.out.println(horizontalLine);
        System.out.println(" Bye. Hope to see you again soon!");
        System.out.println(horizontalLine);
    }
}