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
                Command action = Command.valueOf(parts[0].toUpperCase());
                System.out.println(horizontalLine);

                switch (action) {
                    case BYE:
                        System.out.println(" Bye. Hope to see you again soon!");
                        System.out.println(horizontalLine);
                        return;
                    case LIST:
                        System.out.println(" Here are the tasks in your list:");
                        for (int i = 0; i < tasks.size(); i++) {
                            System.out.println(" " + (i + 1) + "." + tasks.get(i));
                        }
                        break;
                    case MARK:
                        if (parts.length < 2) {
                            throw new ChipException("OOPS!!! Please specify which task to mark.");
                        }
                        int taskNumberToMark = Integer.parseInt(parts[1]);
                        Task taskToMark = tasks.get(taskNumberToMark - 1);
                        taskToMark.markAsDone();
                        System.out.println(" Nice! I've marked this task as done:");
                        System.out.println("   " + taskToMark);
                        break;
                    case UNMARK:
                        if (parts.length < 2) {
                            throw new ChipException("OOPS!!! Please specify which task to unmark.");
                        }
                        int taskNumberToUnmark = Integer.parseInt(parts[1]);
                        Task taskToUnmark = tasks.get(taskNumberToUnmark - 1);
                        taskToUnmark.markAsNotDone();
                        System.out.println(" OK, I've marked this task as not done yet:");
                        System.out.println("   " + taskToUnmark);
                        break;
                    case DELETE:
                        if (parts.length < 2) {
                            throw new ChipException("Please specify which task to delete.");
                        }
                        int taskNumberToDelete = Integer.parseInt(parts[1]);
                        Task removedTask = tasks.remove(taskNumberToDelete - 1);
                        System.out.println(" Noted. I've removed this task:");
                        System.out.println("   " + removedTask);
                        System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
                        break;
                    case TODO:
                        if (parts.length < 2) {
                            throw new ChipException("The description of a todo cannot be empty.");
                        }
                        Task newTodo = new Todo(parts[1]);
                        tasks.add(newTodo);
                        System.out.println(" Got it. I've added this task:");
                        System.out.println("   " + newTodo);
                        System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
                        break;
                    case DEADLINE:
                        if (parts.length < 2) {
                            throw new ChipException("The description of a deadline cannot be empty.");
                        }
                        String[] deadlineParts = parts[1].split(" /by ");
                        if (deadlineParts.length < 2) {
                            throw new ChipException("Please specify the deadline time using /by.");
                        }
                        Task newDeadline = new Deadline(deadlineParts[0], deadlineParts[1]);
                        tasks.add(newDeadline);
                        System.out.println(" Got it. I've added this task:");
                        System.out.println("   " + newDeadline);
                        System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
                        break;
                    case EVENT:
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
                        Task newEvent = new Event(eventParts[0], timeParts[0], timeParts[1]);
                        tasks.add(newEvent);
                        System.out.println(" Got it. I've added this task:");
                        System.out.println("   " + newEvent);
                        System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
                        break;
                }
            } catch (ChipException e) {
                System.out.println(e.getMessage());
            }  catch (IllegalArgumentException e) {
                System.out.println("I'm sorry, there is no such action.");
            } catch (Exception e) {
                System.out.println("An unexpected error occurred. Please check your command.");
            } finally {
            }
        }
    }
}