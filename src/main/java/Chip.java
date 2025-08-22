import java.util.Scanner;
import java.util.ArrayList;

public class Chip {

    public static void main(String[] args) {
        String horizontalLine = "____________________________________________________________";

        System.out.println(horizontalLine);
        System.out.println(" Hello! I'm Chip");
        System.out.println(" What can I do for you?");
        System.out.println(horizontalLine);

        ArrayList<String> tasks = new ArrayList<>();

        Scanner in = new Scanner(System.in);
        while (true) {
            String command = in.nextLine();
            if (command.equals("bye")) {
                break;
            } else if (command.equals("list")) {
                System.out.println(horizontalLine);
                for (int i = 0; i < tasks.size(); i++) {
                    System.out.println(" " + (i + 1) + ". " + tasks.get(i));
                }
                System.out.println(horizontalLine);
            } else {
                tasks.add(command);
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