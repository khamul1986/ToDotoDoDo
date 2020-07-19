import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class TaskManager {

    static final String fileName = "tasks.csv";
    static String[][] toDo;
    static final String[] opt = {"add", "remove", "list", "exit"}; 

    public static void main(String[] args) {
        toDo = loadDataToTab(fileName);
        variants(opt);
        Scanner scan = new Scanner(System.in);
        while (scan.hasNextLine()) {
            String makeThat = scan.nextLine();
            switch (makeThat) {
                case "add":
                    goAddTask();
                    break;
                case "remove":
                    goDeleteTask(toDo, taskNum());
                    System.out.println("task removed");
                    break;
                case "list":
                    showList(toDo);
                    break;
                case "exit":
                    goSave(fileName, toDo);
                    System.out.println(ConsoleColors.RED + "Bye, bye.");
                    System.exit(0);
                    break;
                default:
                    System.out.println("No existing action selected. Please choose from");
            }
            variants(opt);

        }
    }

    public static void variants(String[] tabl) {
        System.out.println(ConsoleColors.BLUE + "Please select an option:" + ConsoleColors.RESET);
        for (String method : tabl) {
            System.out.println(method);
        }
    }

    public static String[][] loadDataToTab(String fileName) {
        Path path = Paths.get(fileName);
        String[][] tab = null;
        if (!Files.exists(path)) {
            System.out.println("File not exist.");
            System.exit(0);
        }
        try {
            List<String> strings = Files.readAllLines(path);
            tab = new String[strings.size()][strings.get(0).split(",").length];
            for (int i = 0; i < strings.size(); i++) {
                String[] load = strings.get(i).split(",");
                for (int j = 0; j < load.length; j++) {
                    tab[i][j] = load[j];
                }
            }
            System.out.println(strings);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tab;
    }

    private static void goAddTask() {
        Scanner scan = new Scanner(System.in);
        toDo = Arrays.copyOf(toDo, toDo.length + 1);
        toDo[toDo.length - 1] = new String[3];
        System.out.println("What You have to do");
        toDo[toDo.length - 1][0]= scan.nextLine();
        System.out.println("Deadline");
        toDo[toDo.length - 1][1] = scan.nextLine();
        System.out.println("Is it critical?: true/false");
        toDo[toDo.length - 1][2] =  scan.nextLine();
    }

    public static int taskNum() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Select the task number to remove it");
        String str = scan.nextLine();
        if (NumberUtils.isParsable(str)){
            return Integer.parseInt(str);
        }else {
            System.out.println("Not a correct number");
            System.out.println("Select correct number");

        }return -1;

    }

    public static void goDeleteTask(String[][] tab, int num) {
        try {

            toDo = ArrayUtils.remove(tab, num);

        } catch (IndexOutOfBoundsException ex) {
            System.out.println("Element not exist in tab");
            System.out.println("Select existing number");
            showList(tab);
            goDeleteTask(toDo, taskNum());

        }

    }

    public static void showList(String[][] tab) {
        for (int i = 0; i < tab.length; i++) {
            System.out.println(i + ":");
            for (int j = 0; j < tab[i].length; j++) {
                System.out.print(tab[i][j] + " ");
            }
            System.out.println();
        }

    }


    public static void goSave(String fileName, String[][] tab) {
        Path path = Paths.get(fileName);

        String[] lines = new String[toDo.length];
        for (int i = 0; i < tab.length; i++) {
            lines[i] = String.join(",", tab[i]);
        }

        try {
            Files.write(path, Arrays.asList(lines));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}