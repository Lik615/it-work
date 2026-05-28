package zhangziyan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

/**
 * 学生成绩管理系统
 * 实现学生成绩的录入、查询、排序、统计等核心功能
 *
 * @author 张子妍
 */
public class StudentGradeManager {

    // 学生信息内部类
    static class Student {
        private String id;
        private String name;
        private double mathScore;
        private double englishScore;
        private double javaScore;

        public Student(String id, String name, double mathScore,
                       double englishScore, double javaScore) {
            this.id = id;
            this.name = name;
            this.mathScore = mathScore;
            this.englishScore = englishScore;
            this.javaScore = javaScore;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getMathScore() {
            return mathScore;
        }

        public void setMathScore(double mathScore) {
            this.mathScore = mathScore;
        }

        public double getEnglishScore() {
            return englishScore;
        }

        public void setEnglishScore(double englishScore) {
            this.englishScore = englishScore;
        }

        public double getJavaScore() {
            return javaScore;
        }

        public void setJavaScore(double javaScore) {
            this.javaScore = javaScore;
        }

        // 计算总分
        public double getTotalScore() {
            return mathScore + englishScore + javaScore;
        }

        // 计算平均分
        public double getAverageScore() {
            return getTotalScore() / 3.0;
        }

        // 判断是否及格（所有科目 >= 60）
        public boolean isAllPassed() {
            return mathScore >= 60 && englishScore >= 60 && javaScore >= 60;
        }

        @Override
        public String toString() {
            return String.format(
                "学号: %s | 姓名: %s | 数学: %.1f | 英语: %.1f | Java: %.1f | 总分: %.1f | 平均: %.1f | %s",
                id, name, mathScore, englishScore, javaScore,
                getTotalScore(), getAverageScore(),
                isAllPassed() ? "合格" : "不合格"
            );
        }
    }

    // 学生列表
    private ArrayList<Student> studentList;

    public StudentGradeManager() {
        studentList = new ArrayList<>();
    }

    // 添加学生
    public void addStudent(String id, String name, double math,
                           double english, double java) {
        Student student = new Student(id, name, math, english, java);
        studentList.add(student);
        System.out.println("学生 " + name + " 添加成功！");
    }

    // 根据学号查询学生
    public Student findStudentById(String id) {
        for (Student s : studentList) {
            if (s.getId().equals(id)) {
                return s;
            }
        }
        return null;
    }

    // 根据姓名查询学生
    public ArrayList<Student> findStudentByName(String name) {
        ArrayList<Student> result = new ArrayList<>();
        for (Student s : studentList) {
            if (s.getName().contains(name)) {
                result.add(s);
            }
        }
        return result;
    }

    // 删除学生
    public boolean deleteStudent(String id) {
        Student s = findStudentById(id);
        if (s != null) {
            studentList.remove(s);
            System.out.println("学生 " + s.getName() + " 删除成功！");
            return true;
        }
        System.out.println("未找到学号为 " + id + " 的学生！");
        return false;
    }

    // 修改学生成绩
    public boolean updateScore(String id, String subject, double newScore) {
        Student s = findStudentById(id);
        if (s == null) {
            System.out.println("未找到学号为 " + id + " 的学生！");
            return false;
        }
        switch (subject) {
            case "数学":
                s.setMathScore(newScore);
                break;
            case "英语":
                s.setEnglishScore(newScore);
                break;
            case "Java":
                s.setJavaScore(newScore);
                break;
            default:
                System.out.println("科目不存在！");
                return false;
        }
        System.out.println("成绩修改成功！");
        return true;
    }

    // 按总分降序排序
    public void sortByTotalScore() {
        Collections.sort(studentList, new Comparator<Student>() {
            @Override
            public int compare(Student s1, Student s2) {
                return Double.compare(s2.getTotalScore(), s1.getTotalScore());
            }
        });
        System.out.println("已按总分降序排序！");
    }

    // 显示所有学生信息
    public void displayAllStudents() {
        if (studentList.isEmpty()) {
            System.out.println("暂无学生信息！");
            return;
        }
        System.out.println("\n========== 学生成绩列表 ==========");
        System.out.println("共 " + studentList.size() + " 名学生");
        System.out.println("------------------------------------");
        for (Student s : studentList) {
            System.out.println(s);
        }
        System.out.println("====================================\n");
    }

    // 统计信息
    public void displayStatistics() {
        if (studentList.isEmpty()) {
            System.out.println("暂无学生信息，无法统计！");
            return;
        }
        double totalMath = 0, totalEnglish = 0, totalJava = 0;
        double maxTotal = Double.MIN_VALUE;
        double minTotal = Double.MAX_VALUE;
        String maxStudent = "", minStudent = "";
        int passedCount = 0;

        for (Student s : studentList) {
            totalMath += s.getMathScore();
            totalEnglish += s.getEnglishScore();
            totalJava += s.getJavaScore();

            double total = s.getTotalScore();
            if (total > maxTotal) {
                maxTotal = total;
                maxStudent = s.getName();
            }
            if (total < minTotal) {
                minTotal = total;
                minStudent = s.getName();
            }
            if (s.isAllPassed()) {
                passedCount++;
            }
        }

        int n = studentList.size();
        System.out.println("\n========== 成绩统计信息 ==========");
        System.out.printf("学生总数: %d 人\n", n);
        System.out.printf("数学平均分: %.1f\n", totalMath / n);
        System.out.printf("英语平均分: %.1f\n", totalEnglish / n);
        System.out.printf("Java平均分: %.1f\n", totalJava / n);
        System.out.printf("最高总分: %.1f (%s)\n", maxTotal, maxStudent);
        System.out.printf("最低总分: %.1f (%s)\n", minTotal, minStudent);
        System.out.printf("全科合格人数: %d 人\n", passedCount);
        System.out.printf("全科合格率: %.1f%%\n", passedCount * 100.0 / n);
        System.out.println("==================================\n");
    }

    // 显示菜单
    public void showMenu() {
        System.out.println("\n========== 学生成绩管理系统 ==========");
        System.out.println("1. 添加学生成绩");
        System.out.println("2. 查询学生成绩（按学号）");
        System.out.println("3. 查询学生成绩（按姓名）");
        System.out.println("4. 修改学生成绩");
        System.out.println("5. 删除学生");
        System.out.println("6. 按总分排序");
        System.out.println("7. 显示所有学生");
        System.out.println("8. 成绩统计");
        System.out.println("0. 退出系统");
        System.out.println("=======================================");
        System.out.print("请输入您的选择: ");
    }

    // 运行系统
    public void run() {
        Scanner scanner = new Scanner(System.in);
        // 添加一些示例数据
        addStudent("2024001", "张三", 85, 78, 92);
        addStudent("2024002", "李四", 92, 88, 95);
        addStudent("2024003", "王五", 55, 62, 70);
        addStudent("2024004", "赵六", 78, 85, 80);
        addStudent("2024005", "孙七", 90, 91, 88);

        while (true) {
            showMenu();
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    System.out.print("请输入学号: ");
                    String id = scanner.nextLine();
                    System.out.print("请输入姓名: ");
                    String name = scanner.nextLine();
                    System.out.print("请输入数学成绩: ");
                    double math = Double.parseDouble(scanner.nextLine());
                    System.out.print("请输入英语成绩: ");
                    double english = Double.parseDouble(scanner.nextLine());
                    System.out.print("请输入Java成绩: ");
                    double java = Double.parseDouble(scanner.nextLine());
                    addStudent(id, name, math, english, java);
                    break;
                case "2":
                    System.out.print("请输入要查询的学号: ");
                    String searchId = scanner.nextLine();
                    Student found = findStudentById(searchId);
                    if (found != null) {
                        System.out.println(found);
                    } else {
                        System.out.println("未找到该学生！");
                    }
                    break;
                case "3":
                    System.out.print("请输入要查询的姓名: ");
                    String searchName = scanner.nextLine();
                    ArrayList<Student> results = findStudentByName(searchName);
                    if (results.isEmpty()) {
                        System.out.println("未找到匹配的学生！");
                    } else {
                        for (Student s : results) {
                            System.out.println(s);
                        }
                    }
                    break;
                case "4":
                    System.out.print("请输入要修改的学号: ");
                    String updateId = scanner.nextLine();
                    System.out.print("请输入科目（数学/英语/Java）: ");
                    String subject = scanner.nextLine();
                    System.out.print("请输入新成绩: ");
                    double newScore = Double.parseDouble(scanner.nextLine());
                    updateScore(updateId, subject, newScore);
                    break;
                case "5":
                    System.out.print("请输入要删除的学号: ");
                    String deleteId = scanner.nextLine();
                    deleteStudent(deleteId);
                    break;
                case "6":
                    sortByTotalScore();
                    displayAllStudents();
                    break;
                case "7":
                    displayAllStudents();
                    break;
                case "8":
                    displayStatistics();
                    break;
                case "0":
                    System.out.println("感谢使用学生成绩管理系统，再见！");
                    scanner.close();
                    return;
                default:
                    System.out.println("无效选择，请重新输入！");
            }
        }
    }

    // 程序入口
    public static void main(String[] args) {
        StudentGradeManager manager = new StudentGradeManager();
        manager.run();
    }
}
