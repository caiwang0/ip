public class Parser {

    public static void parse(String fullCommand, TaskList tasks, Ui ui, Storage storage) throws ChipException {
        String[] parts = fullCommand.split(" ", 2);
        Command action = Command.valueOf(parts[0].toUpperCase());

        switch (action) {
            case LIST:
                showTaskList(tasks, ui);
                break;
            case MARK:
                markTask(parts, tasks, ui, storage);
                break;
            case UNMARK:
                unmarkTask(parts, tasks, ui, storage);
                break;
            case DELETE:
                deleteTask(parts, tasks, ui, storage);
                break;
            case TODO:
                addTodo(parts, tasks, ui, storage);
                break;
            case DEADLINE:
                addDeadline(parts, tasks, ui, storage);
                break;
            case EVENT:
                addEvent(parts, tasks, ui, storage);
                break;
        }
    }

    private static void showTaskList(TaskList tasks, Ui ui) {
        ui.showMessage("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            ui.showMessage(" " + (i + 1) + "." + tasks.getTask(i));
        }
    }

    private static void markTask(String[] parts, TaskList tasks, Ui ui, Storage storage) throws ChipException {
        if (parts.length < 2) {
            throw new ChipException("Please specify which task to mark.");
        }
        int taskNumber = Integer.parseInt(parts[1]) - 1;
        Task task = tasks.getTask(taskNumber);
        task.markAsDone();
        ui.showMessage("Nice! I've marked this task as done:");
        ui.showMessage("   " + task);
        storage.save(tasks.getTasks());
    }

    private static void unmarkTask(String[] parts, TaskList tasks, Ui ui, Storage storage) throws ChipException {
        if (parts.length < 2) {
            throw new ChipException("Please specify which task to unmark.");
        }
        int taskNumber = Integer.parseInt(parts[1]) - 1;
        Task task = tasks.getTask(taskNumber);
        task.markAsNotDone();
        ui.showMessage("OK, I've marked this task as not done yet:");
        ui.showMessage("   " + task);
        storage.save(tasks.getTasks());
    }

    private static void deleteTask(String[] parts, TaskList tasks, Ui ui, Storage storage) throws ChipException {
        if (parts.length < 2) {
            throw new ChipException("Please specify which task to delete.");
        }
        int taskNumber = Integer.parseInt(parts[1]) - 1;
        Task removedTask = tasks.deleteTask(taskNumber);
        ui.showMessage("Noted. I've removed this task:");
        ui.showMessage("   " + removedTask);
        ui.showMessage("Now you have " + tasks.size() + " tasks in the list.");
        storage.save(tasks.getTasks());
    }

    private static void addTodo(String[] parts, TaskList tasks, Ui ui, Storage storage) throws ChipException {
        if (parts.length < 2) {
            throw new ChipException("The description of a todo cannot be empty.");
        }
        Task newTodo = new Todo(parts[1]);
        tasks.addTask(newTodo);
        ui.showMessage("Got it. I've added this task:");
        ui.showMessage("   " + newTodo);
        ui.showMessage("Now you have " + tasks.size() + " tasks in the list.");
        storage.save(tasks.getTasks());
    }

    private static void addDeadline(String[] parts, TaskList tasks, Ui ui, Storage storage) throws ChipException {
        if (parts.length < 2) {
            throw new ChipException("The description of a deadline cannot be empty.");
        }
        String[] deadlineParts = parts[1].split(" /by ");
        if (deadlineParts.length < 2) {
            throw new ChipException("Please specify the deadline time using /by.");
        }
        Task newDeadline = new Deadline(deadlineParts[0], deadlineParts[1]);
        tasks.addTask(newDeadline);
        ui.showMessage("Got it. I've added this task:");
        ui.showMessage("   " + newDeadline);
        ui.showMessage("Now you have " + tasks.size() + " tasks in the list.");
        storage.save(tasks.getTasks());
    }

    private static void addEvent(String[] parts, TaskList tasks, Ui ui, Storage storage) throws ChipException {
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
        tasks.addTask(newEvent);
        ui.showMessage("Got it. I've added this task:");
        ui.showMessage("   " + newEvent);
        ui.showMessage("Now you have " + tasks.size() + " tasks in the list.");
        storage.save(tasks.getTasks());
    }

    public static void main(String[] args) {
        new Chip("./data/chip.txt").run();
    }
}