import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class OnlineExamApp {

    public static void main(String[] args) {
        Exam exam = new Exam("Java Basics");

        String[] options1 = {"OOP", "Procedural", "Functional", "None"};
        exam.addQuestion(new Question("Java is an _____ language.", options1, 0));

        String[] options2 = {"int", "String", "double", "char"};
        exam.addQuestion(new Question("Which data type is used to create a variable that should store text?", options2, 1));

        String[] options3 = {"Object", "Class", "Method", "Interface"};
        exam.addQuestion(new Question("In Java, every application must have at least one _____ method.", options3, 2));

        exam.start();
    }
}

class Exam {
    private String title;
    private List<Question> questions;

    public Exam(String title) {
        this.title = title;
        this.questions = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public void addQuestion(Question question) {
        questions.add(question);
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void start() {
        int score = 0;
        for (Question question : questions) {
            System.out.println(question.getQuestionText());
            for (int i = 0; i < question.getOptions().length; i++) {
                System.out.println((i + 1) + ": " + question.getOptions()[i]);
            }
            int answer = ScannerUtil.getIntInput("Your answer: ");
            if (question.isCorrect(answer - 1)) {
                score++;
            }
        }
        System.out.println("Your score: " + score + "/" + questions.size());
    }
}

class Question {
    private String questionText;
    private String[] options;
    private int correctOption;

    public Question(String questionText, String[] options, int correctOption) {
        this.questionText = questionText;
        this.options = options;
        this.correctOption = correctOption;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String[] getOptions() {
        return options;
    }

    public boolean isCorrect(int userAnswer) {
        return userAnswer == correctOption;
    }
}

class ScannerUtil {
    private static Scanner scanner = new Scanner(System.in);

    public static int getIntInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.print("Invalid input. " + prompt);
            scanner.next();
        }
        return scanner.nextInt();
    }
}

