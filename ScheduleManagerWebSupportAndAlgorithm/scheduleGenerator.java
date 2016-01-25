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
    public static List<Course[]> schedules2 = new ArrayList<Course[]>();
    public static Course[] tester;
    public static int classes;
    public static int test;
    public static int schedulenumber;
    public static int notavailables;


    public static void main(String[] args) throws FileNotFoundException, IOException{
        classes = args.length;
        String line;
        int[] list= new int[3];
        for (int i = 0; i < classes; i++) {
            line = args[i];
            list[0] = line.indexOf("/");
            list[1] = line.indexOf("/", list[0] + 1);
            list[2] = line.indexOf("/", list[1] + 1);
            courses.add(new Course(line.substring(0,list[0]), line.substring(list[0] + 1, list[1]),
                    line.substring(list[1] + 1, list[2]), line.substring(list[2] + 1), true, 0));
        }
        String line2 = "";
        try {
            FileReader fileReader = new FileReader(filename);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while((line2 = bufferedReader.readLine()) != null) {
                int[] list2= new int[4];
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

//        Course[] option = options.toArray(new Course[options.size()]);
//        Combination a = new Combination();
//        int[][] posibilities = a.combination(option, classes);
//        Course[] testers = new Course[classes];
//        System.out.println(posibilities.length);
//        System.out.println(posibilities[1].length);
//
//        for (int i = 0; i < posibilities.length; i++) {
//            for (int j = 0; j < posibilities[i].length; j++) {
//                System.out.println(posibilities[i][j]);
//            }
//            System.out.println("_____");
//        }

//        for (int i = 0; i < posibilities.size(); i++) {
//                for (int k = i; k < posibilities.get(i).length; k++) {
//                    testers[k] = options.get(posibilities.get(i)[k]);
//                }
//                schedules.add(testers);
//
//        }
//
//        for (int i = 0; i < schedules.size(); i++) {
//                for (int c = 0; c < schedules.get(i).length; c++) {
//                    for (int b = c; b < schedules.get(i).length; b++) {
//                        if (!schedules.get(i)[c].checkAvailability(schedules.get(i)[b])) {
//                            schedules.remove(i);
//                        }
//                    }
//                System.out.println(schedules.get(i)[c]);
//            }
//            System.out.println("-------");
//        }


//        combinationUtil combi = new combinationUtil();
//        Course[] option = options.toArray(new Course[options.size()]);
//        schedules = combi.combinationUtils(option, new Course[options.size() + 1], 0, option.length, 0, classes);
//
//        schedules2 = schedules;
//
//        for (int i = 0; i < schedules2.size(); i++) {
//            for (int j = 0; j < schedules2.get(i).length; j++) {
//                for (int k = j + 1; k < schedules2.get(i).length; k++) {
//                    if (!schedules2.get(i)[j].checkAvailability(schedules2.get(i)[k])) {
//                        schedules.remove(schedules2.get(i)[j]);
//                    }
//                }
//            }
//        }
//
//        for (int i = 0; i < schedules.size(); i++) {
//            for (int j = 0; j < schedules.get(i).length; j++) {
//                System.out.println(schedules.get(i)[j]);
//            }
//            System.out.println("::::::::::::");
//        }

        for (int i = 0; i < options.size(); i++) {
            for (int a = 0; a < options.size(); a++) {
                options.get(a).setAvailable(true);
            }
            notavailables = 0;
            schedulenumber = 0;
            tester = new Course[args.length];
            tester[0] = options.get(0);
            test = 1;
            for (int j = 0; j < options.size(); j++) {
                possschedules(i, j);
            }
            options.remove(0);
        }

        for (int i = 0; i < schedules.size(); i++) {
            for (int j = 0; j < schedules.get(i).length; j++) {
                System.out.print(schedules.get(i)[j] + "?");
            }
            if (i < schedules.size() - 1) {
                System.out.print("-------");
            }
        }
        System.out.println();
    }

    public static void schedulecombinations () {}




    public static void possschedules(int j, int factor) {
        if (test == classes) {
            if (!schedules.contains(tester)) {
                schedules.add(tester);
                factor++;
            }
        } else if (test < classes) {
            if (j < options.size()) {
                for (int k = j + factor; k < options.size(); k++) {
                    boolean temporal = true;
                    for (int i = 0; i < test; i++) {
                        if (!tester[i].checkAvailability(options.get(k))) {
                            temporal = false;
                            factor++;
                        }
                    }
                    if (temporal) {
                        tester[test] = options.get(k);
                        test ++;
                        factor++;
                        possschedules(j + 1, factor);
                    }
                }
            }

        }
    }
}
