import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by David on 1/23/16.
 */

public class scheduleGenerator {

    public static String filename = "Courses.txt";
    public static List<Course> courses = new ArrayList<Course>();
    public static List<Course> database = new ArrayList<Course>();
    public static ArrayList<Course> options = new ArrayList<Course>();
    public static List<Course[]> schedules = new ArrayList<Course[]>();
    public static List<ArrayList<Course>> schedules2 = new ArrayList<ArrayList<Course>>();
    public static List<Course[]> schedules3 = new ArrayList<Course[]>();
    public static Course[] tester;
    public static int classes;
    public static int test;
    public static int schedulenumber;
    public static int notavailables;


    public static void main(String[] args) throws FileNotFoundException, IOException {
        classes = args.length;
        String line;
        int[] list = new int[3];
        for (int i = 0; i < classes; i++) {
            line = args[i];
            list[0] = line.indexOf("/");
            list[1] = line.indexOf("/", list[0] + 1);
            list[2] = line.indexOf("/", list[1] + 1);
            courses.add(new Course(line.substring(0, list[0]), line.substring(list[0] + 1, list[1]),
                    line.substring(list[1] + 1, list[2]), line.substring(list[2] + 1), true, 0));
        }
        String line2 = "";
        try {
            FileReader fileReader = new FileReader(filename);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while ((line2 = bufferedReader.readLine()) != null) {
                int[] list2 = new int[4];
                list2[0] = line2.indexOf("/");
                list2[1] = line2.indexOf("/", list2[0] + 1);
                list2[2] = line2.indexOf("/", list2[1] + 1);
                list2[3] = line2.indexOf("/", list2[2] + 1);
                database.add(new Course(line2.substring(0, list2[0]), line2.substring(list2[0] + 1, list2[1]),
                        line2.substring(list2[1] + 1, list2[2]), line2.substring(list2[2] + 1, list2[3]), true,
                        Integer.parseInt(line2.substring(list2[3] + 1))));
            }
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            throw e;
        }

        for (int i = 0; i < courses.size(); i++) {
            if (courses.get(i).getTime().equals("NP")) {
                if (courses.get(i).getDate().equals("NP")) {
                    if (courses.get(i).getProfessor().equals("NP")) {
                        // TIME NP, DATE NP, Professor NP
                        for (int j = 0; j < database.size(); j++) {
                            if (courses.get(i).getName().equals(database.get(j).getName())) {
                                options.add(database.get(j));
                            }
                        }
                    } else {
                        // TIME NP, DATE NP
                        for (int j = 0; j < database.size(); j++) {
                            if (courses.get(i).getName().equals(database.get(j).getName()) &&
                                    courses.get(i).getProfessor().equals(database.get(j).getProfessor())) {
                                options.add(database.get(j));
                            }
                        }
                    }
                } else if (courses.get(i).getProfessor().equals("NP")) {
                    // TIME NP, Professor NP
                    for (int j = 0; j < database.size(); j++) {
                        if (courses.get(i).getName().equals(database.get(j).getName()) &&
                                courses.get(i).getDate().equals(database.get(j).getDate())) {
                            options.add(database.get(j));
                        }
                    }
                } else {
                    // Time NP
                    for (int j = 0; j < database.size(); j++) {
                        if (courses.get(i).getName().equals(database.get(j).getName()) &&
                                courses.get(i).getProfessor().equals(database.get(j).getProfessor()) &&
                                courses.get(i).getDate().equals(database.get(j).getDate())) {
                            options.add(database.get(j));
                        }
                    }
                }
            } else if (courses.get(i).getDate().equals("NP")) {
                if (courses.get(i).getProfessor().equals("NP")) {
                    // Date NP, Professor NP
                    for (int j = 0; j < database.size(); j++) {
                        if (courses.get(i).getName().equals(database.get(j).getName()) &&
                                courses.get(i).getTime().equals(database.get(j).getTime())) {
                            options.add(database.get(j));
                        }
                    }
                } else {
                    // Date NP
                    for (int j = 0; j < database.size(); j++) {
                        if (courses.get(i).getName().equals(database.get(j).getName()) &&
                                courses.get(i).getProfessor().equals(database.get(j).getProfessor()) &&
                                courses.get(i).getTime().equals(database.get(j).getTime())) {
                            options.add(database.get(j));
                        }
                    }
                }
            } else if (courses.get(i).getProfessor().equals("NP")) {
                // Professor NP
                for (int j = 0; j < database.size(); j++) {
                    if (courses.get(i).getName().equals(database.get(j).getName()) &&
                            courses.get(i).getDate().equals(database.get(j).getDate()) &&
                            courses.get(i).getTime().equals(database.get(j).getTime())) {
                        options.add(database.get(j));
                    }
                }
            } else {
                // nothing,
                for (int j = 0; j < database.size(); j++) {
                    if (courses.get(i).getName().equals(database.get(j).getName()) &&
                            courses.get(i).getProfessor().equals(database.get(j).getProfessor()) &&
                            courses.get(i).getDate().equals(database.get(j).getDate()) &&
                            courses.get(i).getTime().equals(database.get(j).getTime())) {
                        options.add(database.get(j));
                    }
                }
            }
        }

        Combination a = new Combination();
        Combination.combination(options.size(), classes);
        ArrayList<int[]> c = a.thelist;
        Course[] temporal = new Course[classes];

        for (int i = 0; i < c.size(); i++) {
            for (int j = 0; j < c.get(i).length; j++) {
                temporal[j] = options.get(c.get(i)[j]);
            }
            Course[] temporal4 = new Course[classes];
            boolean add = true;
            for (int k = 0; k < classes; k++) {
                temporal4[k] = temporal[k];
                for (int l = 0; l < k; l++) {
                    if (!temporal4[l].checkAvailability(temporal4[k])) {
                        add = false;
                    }
                }
            }
            if (add) {
                schedules3.add(temporal4);
            }
        }

        for (int i = 0; i < schedules3.size(); i++) {
            for (int j = 0; j < schedules3.get(i).length; j++) {
                System.out.print(schedules3.get(i)[j]);
                if(!(j == schedules3.get(i).length - 1)){
                    System.out.print("?");
                }
            }
            if(!(i == schedules3.size()-1)){
                System.out.println("__________");
            }
        }
    }
}
