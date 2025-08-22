import java.util.Scanner;

public class Chip {

    public static void main(String[] args) {
        String horizontalLine = "____________________________________________________________";

        System.out.println(horizontalLine);
        System.out.println(" Hello! I'm Chip");
        System.out.println(" What can I do for you?");
        System.out.println(horizontalLine);

        Scanner in = new Scanner(System.in);
        while (true) {
            String command = in.nextLine();
            if (command.equals("bye")) {
                break;
            }
            System.out.println(horizontalLine);
            System.out.println(" " + command);
            System.out.println(horizontalLine);
        }
        System.out.println(horizontalLine);
        System.out.println(" Bye. Hope to see you again soon!");
        System.out.println(horizontalLine);
    }
}