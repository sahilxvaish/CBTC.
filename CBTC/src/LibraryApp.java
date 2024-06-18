import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Database Helper Class
class DatabaseHelper {
    private static final String URL = "jdbc:sqlite:library.db";

    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public static void createNewDatabase() {
        try (Connection conn = connect()) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS books (\n"
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + " title TEXT NOT NULL,\n"
                + " author TEXT NOT NULL\n"
                + ");";

        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}

// Book Model Class
class Book {
    private int id;
    private String title;
    private String author;

    public Book(String title, String author) {
        this.title = title;
        this.author = author;
    }

    public Book(int id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }
}

// BookDAO Class for Data Access
class BookDAO {
    private Connection connect() {
        return DatabaseHelper.connect();
    }

    public void addBook(Book book) {
        String sql = "INSERT INTO books(title, author) VALUES(?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void removeBook(int id) {
        String sql = "DELETE FROM books WHERE id = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public Book getBook(int id) {
        String sql = "SELECT id, title, author FROM books WHERE id = ?";
        Book book = null;

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                book = new Book(rs.getInt("id"), rs.getString("title"), rs.getString("author"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return book;
    }

    public List<Book> getAllBooks() {
        String sql = "SELECT id, title, author FROM books";
        List<Book> books = new ArrayList<>();

        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                books.add(new Book(rs.getInt("id"), rs.getString("title"), rs.getString("author")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return books;
    }
}

// Main Class
public class LibraryApp {
    private static BookDAO bookDAO = new BookDAO();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        DatabaseHelper.createNewDatabase();
        DatabaseHelper.createTable();

        boolean running = true;

        while (running) {
            System.out.println("Library Management System");
            System.out.println("1. Add Book");
            System.out.println("2. Remove Book");
            System.out.println("3. Search Book");
            System.out.println("4. List All Books");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    addBook();
                    break;
                case 2:
                    removeBook();
                    break;
                case 3:
                    searchBook();
                    break;
                case 4:
                    listAllBooks();
                    break;
                case 5:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void addBook() {
        System.out.print("Enter book title: ");
        String title = scanner.nextLine();
        System.out.print("Enter book author: ");
        String author = scanner.nextLine();

        Book book = new Book(title, author);
        bookDAO.addBook(book);
        System.out.println("Book added successfully.");
    }

    private static void removeBook() {
        System.out.print("Enter book ID to remove: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // consume newline

        bookDAO.removeBook(id);
        System.out.println("Book removed successfully.");
    }

    private static void searchBook() {
        System.out.print("Enter book ID to search: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // consume newline

        Book book = bookDAO.getBook(id);
        if (book != null) {
            System.out.println("Book ID: " + book.getId());
            System.out.println("Title: " + book.getTitle());
            System.out.println("Author: " + book.getAuthor());
        } else {
            System.out.println("Book not found.");
        }
    }

    private static void listAllBooks() {
        List<Book> books = bookDAO.getAllBooks();
        if (books.isEmpty()) {
            System.out.println("No books available.");
        } else {
            for (Book book : books) {
                System.out.println("Book ID: " + book.getId());
                System.out.println("Title: " + book.getTitle());
                System.out.println("Author: " + book.getAuthor());
                System.out.println();
            }
        }
    }
}
