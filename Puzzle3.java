// Aaron Jones

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.Graphics;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

public class Puzzle3 extends JPanel implements ActionListener {

    public static final int WIDTH = 1000;
    public static final int HEIGHT = 900;
    public static int NUM_SQUARES = 22;
    public static JFrame frame = new JFrame("Crossword!");
    public static Character[][] answerGrid;
    public static JButton save = new JButton("Save");
    public static JButton otherButton = new JButton("Other");
    private static Crossword c;
    private static ArrayList<JTextField> textFields = new ArrayList<>();

    public static JTextField createFilteredField(String text, int columns) {    //this method was adapted from code found here: https://stackoverflow.com/questions/24844559/jtextfield-using-document-filter-to-filter-integers-and-periods
        JTextField field = new JTextField(text, columns);
        AbstractDocument document = (AbstractDocument) field.getDocument();
        final int maxCharacters = 1;
        document.setDocumentFilter(new DocumentFilter() {
            public void replace(FilterBypass fb, int offs, int length,
                                String str, AttributeSet a) throws BadLocationException {
                String text = fb.getDocument().getText(0,
                        fb.getDocument().getLength());
                text += str;
                if ((fb.getDocument().getLength() + str.length() - length) <= maxCharacters
                        && (text.matches("[a-z]")||text.matches("[A-Z]"))) {
                    super.replace(fb, offs, length, str.toUpperCase(), a);
                } else {
                    Toolkit.getDefaultToolkit().beep();
                }
            }

            @Override
            public void insertString(FilterBypass fb, int offs, String str,
                                     AttributeSet a) throws BadLocationException {

                String text = fb.getDocument().getText(0,
                        fb.getDocument().getLength());
                text += str;
                if ((fb.getDocument().getLength() + str.length()) <= maxCharacters
                        && (text.matches("[a-z]")||text.matches("[A-Z]"))) {
                    super.insertString(fb, offs, str.toUpperCase(), a);
                } else {
                    Toolkit.getDefaultToolkit().beep();
                }
            }
        });
        return field;
    }

    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == save) {
            // Crossword.printMatrix(convertToMatrix()); //test code
            savetest.savePuzzle(new int[]{c.getSeed(), c.getSize()}, convertToMatrix(), "testsave.txt");
            System.out.println("Puzzle saved successfully");
        } else if (src == otherButton) {
            convertMatrixToGrid(savetest.loadSave("testsave.txt", c.goalSize + 2, c.goalSize + 2).get1());
            System.out.println("You pressed the other button.");
        } else {
            JTextField jt = (JTextField) e.getSource();
            Character c = jt.getText().charAt(0); //Gets the text in the textfield
            Point p = jt.getLocation(); //Gets location of the textfield as a point
            int div = (900 / NUM_SQUARES);
            Double temp_x = p.getX() / div; //Gets x-coordinate of point (which square)
            int x = temp_x.intValue();
            Double temp_y = p.getY() / div; //Gets y-coordinate of point (which square)
            int y = temp_y.intValue();
            System.out.println(c);
            System.out.println(x + " " + y);
            if (answerGrid[x][y] == c) {
                System.out.println("yay");
            }
        }
    }

    public Puzzle3() {
        save.addActionListener(this);
        otherButton.addActionListener(this);
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
    }


    public static void main(String[] args) {
        c = test.newPuzzle();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new Puzzle3());
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void paintComponent(Graphics g) {
        //System.out.println("paintComponent run.");       //test code
        answerGrid = c.getGrid();
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        ArrayList<JTextField> tempFields = new ArrayList<>();    //this temporary arraylist may not be necessary, but we included it to protect from changing textFields while it is in use
        save.setBounds(800, 100, 100, 100);
        otherButton.setBounds(800, 300, 100, 100);
        frame.add(save);
        frame.add(otherButton);
        int counter = 0;
        for (int i = 0; i < NUM_SQUARES; i++) {              //DECIDE WHICH SQUARES NEED TO HAVE AN OPENING FOR A LETTER
            for (int j = 0; j < NUM_SQUARES; j++) {
                if (answerGrid[i][j] == null) {
                    g.setColor(Color.BLACK);

                } else {
                    g.setColor(Color.WHITE);
                }

                if (g.getColor() == Color.WHITE) {
                    tempFields.add(createFilteredField("", 900/NUM_SQUARES));
                    tempFields.get(counter).setBounds(j * 900 / NUM_SQUARES, i * HEIGHT / NUM_SQUARES, 900 / NUM_SQUARES, 900 / NUM_SQUARES);
                    Font font = new Font("Courier", Font.BOLD, 900 / NUM_SQUARES);
                    tempFields.get(counter).setFont(font);
                    tempFields.get(counter).setHorizontalAlignment(JTextField.CENTER);
                    tempFields.get(counter).addActionListener(this);
                    frame.add(tempFields.get(counter));
                    counter++;
                } else {
                    g.drawRect(j * 900 / NUM_SQUARES, i * HEIGHT / NUM_SQUARES, (j + 1) * 900 / NUM_SQUARES, (i + 1) * HEIGHT / NUM_SQUARES);
                    g.fillRect(j * 900 / NUM_SQUARES, i * HEIGHT / NUM_SQUARES, (j + 1) * 900 / NUM_SQUARES, (i + 1) * HEIGHT / NUM_SQUARES);

                }
            }
        }
        textFields = tempFields;
        for (JTextField j : textFields)
            j.setEditable(true);
        int[][] t=c.getNumGrid();
        for (int i=1; i<NUM_SQUARES; i++){  //Assigning Numbers to Puzzle Row/Column
            for (int j=1; j<NUM_SQUARES; j++){
                if(t[i][j] == 0){
                }
                else if (textFieldHere(i-1,j)){
                    g.setColor(Color.WHITE);
                    g.drawString(String.valueOf(t[i][j]),(j*900/NUM_SQUARES)-10, (i*900/NUM_SQUARES)+10);  //If Field Has Another Field Above It, Print Numbers On Side
                }
                else{
                    g.setColor(Color.WHITE);
                    g.drawString(String.valueOf(t[i][j]),j*900/NUM_SQUARES,i*HEIGHT/NUM_SQUARES);  //Otherwise Print Above Top Left Corner
                }
            }
        }
    }

    public static boolean textFieldHere(int a, int b){
        for(JTextField jt: textFields) {
            Point p = jt.getLocation(); //Gets location of the textfield as a point
            int div = (900 / NUM_SQUARES);
            Double temp_x = p.getX() / div; //Gets x-coordinate of point (which square)
            int x = temp_x.intValue();
            Double temp_y = p.getY() / div; //Gets y-coordinate of point (which square)
            int y = temp_y.intValue();
            if (x==b&&y==a)
                return true;
        }
        return false;
    }

    @Deprecated
    public static Character[][] createMatrix() {  //TEMPORARY MATRIX TO TEST SQUARE MAKING
        Character[][] m = new Character[20][20];
        m[2][3] = 'd';
        m[2][4] = 'a';
        m[2][5] = 'y';
        m[1][5] = 's';
        m[1][6] = 'e';
        m[1][7] = 'l';
        m[1][8] = 'l';
        return m;
    }

    public static boolean checkBadOverlap(int i, int j) {
        for (JTextField jt : textFields) {
            Point p = jt.getLocation(); //Gets location of the textfield as a point
            int div = (900 / NUM_SQUARES);
            Double temp_x = p.getX() / div; //Gets x-coordinate of point (which square)
            int x = temp_x.intValue();
            Double temp_y = p.getY() / div; //Gets y-coordinate of point (which square)
            int y = temp_y.intValue();
            if (i == y && x == j)
                return false;
        }
        return true;
    }

    public static void convertMatrixToGrid(Character[][] matrix) {
        try {
            for (int i = 0; i < matrix.length; i++) //these loops take every non-null value in matrix and make sure that there is a textfield corresponding to its location
                for (int j = 0; j < matrix[0].length; j++)
                    if (matrix[i][j] != null) {
                        if (checkBadOverlap(i, j))
                            throw new BadLetterException();
                    }
        } catch (BadLetterException e) {
            System.out.println("Error. Load failed. Letter in matrix where it shouldn't be.\nYour save file is corrupted, sorry.");
            return;
        }
        for (JTextField jt : textFields) {
            Point p = jt.getLocation(); //Gets location of the textfield as a point
            int div = (900 / NUM_SQUARES);
            Double temp_x = p.getX() / div; //Gets x-coordinate of point (which square)
            int x = temp_x.intValue();
            Double temp_y = p.getY() / div; //Gets y-coordinate of point (which square)
            int y = temp_y.intValue();
            String t;
            if (matrix[y][x] == null) t = "";
            else t = matrix[y][x].toString();
            jt.setText(t);
        }
    }

    public static Character[][] convertToMatrix() {
        Character[][] toReturn = new Character[c.getGrid().length][c.getGrid()[0].length];
        System.out.println("convertToMatrix run.");
        for (JTextField jt : textFields) {
            if (!jt.getText().equals("")) {
                System.out.println("letter found."); //Test code
                Character c = jt.getText().charAt(0); //Gets the text in the textfield
                Point p = jt.getLocation(); //Gets location of the textfield as a point
                int div = (900 / NUM_SQUARES);
                Double temp_x = p.getX() / div; //Gets x-coordinate of point (which square)
                int x = temp_x.intValue();
                Double temp_y = p.getY() / div; //Gets y-coordinate of point (which square)
                int y = temp_y.intValue();
                toReturn[y][x] = c;
            }

        }
        return toReturn;
    }

}



