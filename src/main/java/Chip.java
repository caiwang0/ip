import java.util.Scanner;
import java.util.ArrayList;

public class Chip {

    public static void main(String[] args) {
        String horizontalLine = "____________________________________________________________";

        System.out.println(horizontalLine);
        System.out.println(" Hello! I'm Chip");
        System.out.println(" What can I do for you?");

        ArrayList<Task> tasks = new ArrayList<>();

        Scanner in = new Scanner(System.in);
        while (true) {
            String command = in.nextLine();
            try {
                String[] parts = command.split(" ", 2);
                String action = parts[0];

                System.out.println(horizontalLine);

                if (action.equals("bye")) {
                    System.out.println(" Bye. Hope to see you again soon!");
                    System.out.println(horizontalLine);
                    break;
                } else if (action.equals("list")) {
                    System.out.println(" Here are the tasks in your list:");
                    for (int i = 0; i < tasks.size(); i++) {
                        System.out.println(" " + (i + 1) + "." + tasks.get(i));
                    }
                } else if (action.equals("mark")) {
                    if (parts.length < 2) {
                        throw new ChipException("OOPS!!! Please specify which task to mark.");
                    }
                    int taskNumber = Integer.parseInt(parts[1]);
                    Task task = tasks.get(taskNumber - 1);
                    task.markAsDone();
                    System.out.println(" Nice! I've marked this task as done:");
                    System.out.println("   " + task);
                } else if (action.equals("unmark")) {
                    if (parts.length < 2) {
                        throw new ChipException("OOPS!!! Please specify which task to unmark.");
                    }
                    int taskNumber = Integer.parseInt(parts[1]);
                    Task task = tasks.get(taskNumber - 1);
                    task.markAsNotDone();
                    System.out.println(" OK, I've marked this task as not done yet:");
                    System.out.println("   " + task);
                } else if (action.equals("todo")) {
                    if (parts.length < 2) {
                        throw new ChipException("The description of a todo cannot be empty.");
                    }
                    Task newTask = new Todo(parts[1]);
                    tasks.add(newTask);
                    System.out.println(" Got it. I've added this task:");
                    System.out.println("   " + newTask);
                    System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
                } else if (action.equals("deadline")) {
                    if (parts.length < 2) {
                        throw new ChipException("The description of a deadline cannot be empty.");
                    }
                    String[] deadlineParts = parts[1].split(" /by ");
                    if (deadlineParts.length < 2) {
                        throw new ChipException("Please specify the deadline time using /by.");
                    }
                    Task newTask = new Deadline(deadlineParts[0], deadlineParts[1]);
                    tasks.add(newTask);
                    System.out.println(" Got it. I've added this task:");
                    System.out.println("   " + newTask);
                    System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
                } else if (action.equals("event")) {
                    if (parts.length < 2) {
                        throw new ChipException("The description of an event cannot be empty.");
                    }
                    String[] eventParts = parts[1].split(" /from ");
                    if (eventParts.length < 2) {
                        throw new ChipException("Please specify the event start time using /from.");
                    }
                    String[] timeParts = eventParts[1].split(" /to ");
                    if (timeParts.length < 2) {
                        throw new ChipException("Please specify the event end time using /to.");
                    }
                    Task newTask = new Event(eventParts[0], timeParts[0], timeParts[1]);
                    tasks.add(newTask);
                    System.out.println(" Got it. I've added this task:");
                    System.out.println("   " + newTask);
                    System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
                } else {
                    throw new ChipException("I'm sorry, there is no such action.");
                }
            } catch (ChipException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                System.out.println("An unexpected error occurred. Please check your command.");
            } finally {
            }
        }
    }
}