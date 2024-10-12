//Name: Nishan Hegde
//Reg. No: 230970139
//Section: C
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

// Login class
public class Login implements ActionListener {

    JFrame frame;
    JTextField nameField, passwordField;
    JLabel nameLabel, passwordLabel, titleLabel;
    JButton submitButton;

    Connection connection;

    // Constructor
    Login() {
        // Creating and setting up the login frame
        frame = new JFrame("Login Page");
        frame.setSize(1000, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        // Creating GUI components
        titleLabel = new JLabel("Candidate Page");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        nameField = new JTextField();
        passwordField = new JPasswordField(); // Changed to JPasswordField for password input
        nameLabel = new JLabel("Username");
        passwordLabel = new JLabel("Password");
        submitButton = new JButton("Submit");
        submitButton.addActionListener(this);

        // Setting the position and size of GUI components
        titleLabel.setBounds(400, 50, 300, 30);
        nameLabel.setBounds(400, 100, 100, 20);
        nameField.setBounds(500, 100, 150, 20);
        passwordLabel.setBounds(400, 150, 100, 20);
        passwordField.setBounds(500, 150, 150, 20);
        submitButton.setBounds(530, 200, 95, 30);

        // Adding GUI components to the frame
        frame.add(titleLabel);
        frame.add(nameLabel);
        frame.add(nameField);
        frame.add(passwordLabel);
        frame.add(passwordField);
        frame.add(submitButton);
        frame.setVisible(true);

        // Database connection initialization
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/jdbcdemo", "root", ""
            );
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    // ActionListener implementation
    public void actionPerformed(ActionEvent e) {
        String name = nameField.getText();
        String password = passwordField.getText();

        try {
            // Query to check if the username and password exist in the database
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM student WHERE Name='" + name + "' AND Password='" + password + "'");

            if (resultSet.next()) {
                System.out.println("Login Successful");
                // Close the login window after successful login
                frame.dispose();
                // Open the quiz window
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        new QuizFrame(connection, name); // Passing the name to quiz frame
                    }
                });
            } else {
                System.out.println("Invalid Username or Password");
                JOptionPane.showMessageDialog(frame, "Invalid Username or Password", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Main method
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Login();
            }
        });
    }
}

// QuizFrame class
class QuizFrame implements ActionListener {

    JFrame quizFrame;
    JPanel panel;
    JLabel lblTitle;
    JLabel[] lblQuestions;
    JRadioButton[][] radioButtons;
    ButtonGroup[] buttonGroups;
    JButton btnSubmit;
    Connection connection;
    String name;

    // Constructor
    QuizFrame(Connection connection, String name) {
        this.connection = connection;
        this.name = name;

        // Creating and setting up the quiz frame
        quizFrame = new JFrame("Quiz");
        quizFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        quizFrame.setSize(1000, 700); // Set the size of the quiz frame
        quizFrame.setLocationRelativeTo(null);
        quizFrame.setLayout(null);

        // Title label
        lblTitle = new JLabel("Quiz");
        lblTitle.setBounds(350, 10, 300, 30); // Adjusted the position and size of the title
        lblTitle.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitle.setOpaque(true);
        lblTitle.setBorder(new EmptyBorder(0, 10, 0, 0));

        // Panel to hold questions and options
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Array to hold question texts and options
        String[] questionTexts = {
                "Which of the following is NOT a valid declaration of the main method in Java?",
                "Which keyword is used to define a constant in Java?",
                "Which of the following data types is used to store a single character in Java?",
                "What does the StringBuilder class in Java provide?",
                "What is the purpose of the break statement in Java?"
        };
        String[][] options = {
                {"a) public static void main(String[] args)", "b) public static void main(String args[])", "c) public void main(String[] args)", "d) static public void main(String[] args)"},
                {"a) constant", "b) var", "c) final", "d) static"},
                {"a) char" , "b) string", "c) Character", "d) CharSequence"},
                {"a) Mutable sequence of characters", "b) Immutable sequence of characters", "c) Synchronized sequence of characters", "d) Sequential sequence of characters"},
                {"a) To terminate the loop or switch statement and transfer control to the next statement.", "b) To skip the execution of the current iteration and move to the next iteration in a loop.," ,"c) To throw an exception and exit the program.,," ,"d) To define a named block of code that can be exited with a goto statement."}
        };

        // Creating and adding question labels and radio buttons
        lblQuestions = new JLabel[5];
        radioButtons = new JRadioButton[5][4];
        buttonGroups = new ButtonGroup[5];

        for (int i = 0; i < 5; i++) {
            lblQuestions[i] = new JLabel((i + 1) + ") " + questionTexts[i]);
            lblQuestions[i].setFont(new Font("Arial", Font.BOLD, 20));
            lblQuestions[i].setBorder(new EmptyBorder(10, 10, 0, 0));
            panel.add(lblQuestions[i]);

            buttonGroups[i] = new ButtonGroup();

            for (int j = 0; j < 4; j++) {
                radioButtons[i][j] = new JRadioButton(options[i][j]);
                buttonGroups[i].add(radioButtons[i][j]);
                panel.add(radioButtons[i][j]);
            }
        }

        // Submit button
        btnSubmit = new JButton("SUBMIT");
        btnSubmit.addActionListener(this);
        panel.add(btnSubmit);

        // Scroll pane to scroll through the questions and options
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBounds(50, 50, 900, 500); // Adjusted the position and size of the scroll pane
        quizFrame.add(scrollPane);

        // Adding title label to the quiz frame
        quizFrame.add(lblTitle);
        quizFrame.setVisible(true);
    }

    // ActionListener implementation
    @Override
    public void actionPerformed(ActionEvent e) {
        int totalMarks = 0;
        String[] correctAnswers = {
                "c) public void main(String[] args)",
                "a) constant",
                "a) char",
                "a) Mutable sequence of characters",
                "a) To terminate the loop or switch statement and transfer control to the next statement."
        };

        // Calculating total marks
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 4; j++) {
                if (radioButtons[i][j].isSelected() && radioButtons[i][j].getText().equals(correctAnswers[i])) {
                    totalMarks++;
                    break;
                }
            }
        }

        // Closing the quiz frame
        quizFrame.dispose();

        try {
            // Insert marks in the database
            Statement statement = connection.createStatement();
            statement.executeUpdate("UPDATE student SET Mark=" + totalMarks + " WHERE Name='" + name + "'");
            statement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        // Creating and setting up the result frame
        JFrame resultFrame = new JFrame("Quiz Result");
        resultFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        resultFrame.setSize(300, 200);
        resultFrame.setLocationRelativeTo(null);

        // Displaying total score
        JLabel resultLabel = new JLabel("Total Score: " + totalMarks);
        resultLabel.setFont(new Font("Arial", Font.BOLD, 20));
        resultLabel.setHorizontalAlignment(SwingConstants.CENTER);
        resultFrame.add(resultLabel);

        resultFrame.setVisible(true);
    }
}
