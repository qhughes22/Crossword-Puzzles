// Aaron Jones and Quincy Hughes

import java.io.*;
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

public class Puzzle extends JPanel implements ActionListener {

    private static final int WIDTH = 1200;
    private static Random rand = new Random();
    private static final int HEIGHT = 900;
    private static int NUM_SQUARES = 22;
    private static JFrame frame = new JFrame("Crossword!");
    private static Character[][] answerGrid;
    private static JButton save = new JButton("Save");
    private static JButton otherButton = new JButton("Check Answers");
    private static JButton Button3 = new JButton("Help Me");
    private static boolean loaded = false;
    private static Character[][] loadedMatrix;
    private static Crossword c;
    private static ArrayList<JTextField> textFields = new ArrayList<>();
    private static int timeStarted;
    private static JTextField name = new JTextField();
    private static int hintCount;

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
                        && (text.matches("[a-z]") || text.matches("[A-Z]"))) {
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
                        && (text.matches("[a-z]") || text.matches("[A-Z]"))) {
                    super.insertString(fb, offs, str.toUpperCase(), a);
                } else {
                    Toolkit.getDefaultToolkit().beep();
                }
            }
        });
        return field;
    }

    public void actionPerformed(ActionEvent e) {
        for (JTextField jt : textFields)
            jt.setForeground(Color.BLACK);
        Object src = e.getSource();
        if (src == save) {
            String saveTo;
            if (name.getText().length() == 0)
                saveTo = "default";
            else
                saveTo = name.getText();
            savePuzzle(new int[]{Crossword.originalSeed, c.getSize(), (int) (System.currentTimeMillis()) - timeStarted, hintCount}, convertToMatrix(), "savegames/" + saveTo + ".txt");
            System.out.println("Puzzle saved (unless something else says otherwise).");
        } else if (src == otherButton) {
            System.out.println(checkAnswers());
        } else if (src == Button3) {
            giveHint();
        } else {
            JTextField jt = (JTextField) e.getSource();
        }
    }

    public static void giveHint() {
        ArrayList<JTextField> empty = new ArrayList<>();
        for (JTextField jt : textFields)
            if (jt.getText().length() == 0)
                empty.add(jt);
        if (empty.size() == 0) {
            System.out.println("Puzzle full, can't give hint.");
            return;
        }
        JTextField jt = empty.get(rand.nextInt(empty.size()));
        int x = getCoords(jt)[0];
        int y = getCoords(jt)[1];
        jt.setText(answerGrid[y][x].toString());
        jt.setForeground(Color.GREEN);
        hintCount++;
        System.out.println("You asked for hint number " + hintCount + ".");
    }


    public static int[] getCoords(JTextField jt) {
        Point p = jt.getLocation(); //Gets location of the textfield as a point
        int div = (900 / NUM_SQUARES);
        Double temp_x = p.getX() / div; //Gets x-coordinate of point (which square)
        int x = temp_x.intValue();
        Double temp_y = p.getY() / div; //Gets y-coordinate of point (which square)
        int y = temp_y.intValue();
        return new int[]{x, y};
    }

    public Puzzle() {
        save.addActionListener(this);
        otherButton.addActionListener(this);
        Button3.addActionListener(this);
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
    }


    public static void doPuzzle() {
        System.out.println("Do you want to start a new puzzle or load an existing one?");
        int gameType = menu(new String[]{"new", "load"});
        if (gameType == 0)
            newPuzzle();
        else loadPuzzle();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                for (JTextField jt : textFields)
                    jt.setForeground(Color.BLACK);
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        frame.setContentPane(new Puzzle());
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void paintComponent(Graphics g) {
        //   System.out.println("paintComponent run.");       //test code
        answerGrid = c.getGrid();
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        ArrayList<JTextField> tempFields = new ArrayList<>();    //this temporary arraylist may not be necessary, but we included it to protect from changing textFields while it is in use
        save.setBounds(950, 250, 200, 100);
        otherButton.setBounds(950, 150, 200, 100);
        Button3.setBounds(950, 50, 200, 100);
        frame.add(save);
        frame.add(otherButton);
        frame.add(Button3);
        int counter = 0;
        for (int i = 0; i < NUM_SQUARES; i++) {              //DECIDE WHICH SQUARES NEED TO HAVE AN OPENING FOR A LETTER
            for (int j = 0; j < NUM_SQUARES; j++) {
                if (answerGrid[i][j] == null) {
                    g.setColor(Color.BLACK);

                } else {
                    g.setColor(Color.WHITE);
                }

                if (g.getColor() == Color.WHITE) {
                    tempFields.add(createFilteredField("", 900 / NUM_SQUARES));
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
            name.setBounds(950, 375, 200, 30);
            frame.add(name);
        }
        textFields = tempFields;
        for (JTextField j : textFields)
            j.setEditable(true);
        int[][] t = c.getNumGrid();
        for (int i = 1; i < NUM_SQUARES; i++) {  //Assigning Numbers to Puzzle Row/Column
            for (int j = 1; j < NUM_SQUARES; j++) {
                if (t[i][j] == 0) {
                } else if (textFieldHere(i - 1, j)) {
                    g.setColor(Color.WHITE);
                    g.drawString(String.valueOf(t[i][j]), (j * 900 / NUM_SQUARES) - 15, (i * 900 / NUM_SQUARES) + 10);  //If Field Has Another Field Above It, Print Numbers On Side
                } else {
                    g.setColor(Color.WHITE);
                    g.drawString(String.valueOf(t[i][j]), j * 900 / NUM_SQUARES, (i * HEIGHT / NUM_SQUARES) - 5);  //Otherwise Print Above Top Left Corner
                }
            }
        }
        if (loaded == true) convertMatrixToGrid(loadedMatrix);
    }


    public static ArrayList<Pair<Integer, String>> getClues(placedWord.direction d) {
        ArrayList<Pair<Integer, String>> clueList = new ArrayList<>();
        for (placedWord p : c.getWordsPlaced())
            if (p.getDirection() == d)
                clueList.add(new Pair<Integer, String>(p.getCN(), p.getClue()));
        for (Pair<Integer, String> p : clueList)
            System.out.println(p.get1());
        Collections.sort(clueList, new Comparator<Pair<Integer, String>>() {
            @Override
            public int compare(Pair<Integer, String> o1, Pair<Integer, String> o2) {
                if (o1.get1() < o2.get1())
                    return -1;
                if (o1.get1() > o2.get1())
                    return 1;
                else return 0;
            }
        });
        for (Pair<Integer, String> p : clueList)
            System.out.println(p.get1());
        return clueList;
    }

    public static boolean textFieldHere(int a, int b) {
        for (JTextField jt : textFields) {
            int x = getCoords(jt)[0];
            int y = getCoords(jt)[1];
            if (x == b && y == a)
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
            int x = getCoords(jt)[0];
            int y = getCoords(jt)[1];
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
            System.out.println("Error. Load failed. Letter in matrix where it shouldn't be.\nYour save file is corrupted or you used a different wordfile from the original.");
            return;
        }
        for (JTextField jt : textFields) {
            int x = getCoords(jt)[0];
            int y = getCoords(jt)[1];
            String t;
            if (matrix[y][x] == null) t = "";
            else t = matrix[y][x].toString();
            jt.setText(t);
        }
    }

    public static Character[][] convertToMatrix() {
        Character[][] toReturn = new Character[c.getGrid().length][c.getGrid()[0].length];
    //    System.out.println("convertToMatrix run."); //test code
        for (JTextField jt : textFields) {
            if (!jt.getText().equals("")) {
    //            System.out.println("letter found."); //Test code
                Character c = jt.getText().charAt(0); //Gets the text in the textfield
                int x = getCoords(jt)[0];
                int y = getCoords(jt)[1];
                toReturn[y][x] = c;
            }

        }
        return toReturn;
    }

    public static void loadPuzzle() {
        loaded = true;
        System.out.println("Select a file to read from. It must be the same file as the original savegame.");
        String fileChosen = selectFile("wordfiles");
        ArrayList<Word> tester = makeWordsList("wordfiles/" + fileChosen);
        System.out.println("Select a savegame to load.");
        String saveFile = selectFile("savegames");
        Pair<Character[][], ArrayList<Integer>> loaded = loadSave("savegames/" + saveFile, Crossword.goalSize + 2, Crossword.goalSize + 2);
        ArrayList<Integer> loadedInts = loaded.get2();
        c = makeFullCrossWord(tester, loadedInts.get(0), loadedInts.get(1));
        try {
            System.out.println(loadedInts.get(0));
            System.out.println(loadedInts.get(1));
            System.out.println(loadedInts.get(2));
            System.out.println(loadedInts.get(3));
            hintCount = loadedInts.get(3);
        } catch (ArrayIndexOutOfBoundsException e) {
            hintCount = 0;
        }
        loadedMatrix = loaded.get1();
        timeStarted = timeStarted - loadedInts.get(2);
    }


    public static void newPuzzle() {  //method for creating a new puzzle
        Scanner keyboard = new Scanner(System.in);
        String fileChosen = selectFile("wordfiles");
        ArrayList<Word> tester = makeWordsList("wordfiles/" + fileChosen);
        int seed = 0;  //the seed to be used in the Random
        boolean gotSeed = false; //a boolean for the while loop immediately after
        while (gotSeed == false) {
            try {
                System.out.println("Enter in an integer seed.");
                seed = keyboard.nextInt();
                gotSeed = true;
            } catch (InputMismatchException e) {
                System.out.println("Bad input. Try again.");
                keyboard.next();
            }
        }
        int size = 0; //the size of the puzzle
        boolean gotSize = false; // a boolean for the while loop immediately after
        while (gotSize == false) {
            try {
                System.out.println("Enter in how many words you want.");
                size = keyboard.nextInt();
                if (size < 1) {
                    System.out.println("Size must be greater than 1.");
                } else if (size > tester.size()) {
                    System.out.println("Size is too high, not enough words in file.");
                } else gotSize = true;
            } catch (InputMismatchException e) {
                System.out.println("Bad input. Try again.");
                keyboard.next();
            }
        }
        System.out.println("Got it. Making your puzzle now.");
        c = makeFullCrossWord(tester, seed, size);
        timeStarted = (int) (System.currentTimeMillis());
        //      Crossword.printMatrix(c.getGrid());              //test code
        //   System.out.println(c.getGrid().length);
        //   System.out.println(c.getGrid()[0].length);
//        for (placedWord p : c.placedWords)          //test code
//           System.out.println(p.getCN());          //test code
    }

    public static String selectFile(String t) { //method that prompts the user to select a file and returns the filename.
        File[] files = new File[0];
        File folder;
        try {
            System.out.println("Select a file to read the list of words from. \nIf you don't see a file that should be there, make sure it's stored in wordfiles.");
            folder = new File(t);
            files = folder.listFiles();
        } catch (NullPointerException e) {
            System.out.println("Seems like wordfiles doesn't exist. That's not good.");
            System.exit(1);
        }
        if (files.length == 0) {
            System.out.println("No files found in wordfiles.");
            System.exit(1);
        }
        if (files.length == 1)
            return files[0].getName();
        String toReturn = menu(files);
        if (toReturn.charAt(toReturn.length() - 1) != 't' || toReturn.charAt(toReturn.length() - 2) != 'x' || toReturn.charAt(toReturn.length() - 3) != 't' || toReturn.charAt(toReturn.length() - 4) != '.') {
            System.out.println("Error. File must be a .txt file.");
            System.exit(1);
        }
        return toReturn;
    }


    public static String menu(File[] s) { //creates a menu of files and returns the filename chosen
        String[] toReturn = new String[s.length];
        for (int i = 0; i < s.length; i++)
            toReturn[i] = s[i].getName();
        return toReturn[menu(toReturn)];
    }

    public static Crossword makeFullCrossWord(ArrayList<Word> w, int seed, int size) { //this method keeps making crosswords until it creates one that uses all words.
        int timesTried = 0; //the number of times a puzzle has been attempted. This is so that the program doesn't run forever when it can't generate a puzzle
        Crossword.originalSeed = seed;
        Crossword c = new Crossword(w, seed, size);
        while (c.failed == true) {
            System.out.print("failed to make puzzle.");
            if (c.failedWords > 0)
                System.out.println(" Words didn't fit.");
            else System.out.println(" Puzzle was too big.");
            System.out.println("Creating new puzzle.");
            timesTried++;
            if (timesTried < 50) {
                c = new Crossword(w, c.rand.nextInt(), size); //the fact that a new seed is made means that when saving a puzzle, the int stored as seed might not be what the user inputted
            } else {
                System.out.println("Tried 50 times. Either try again, or check that a crossword is possible with these words.");
                System.exit(1);

            }
        }
        return c;
    }

    @Deprecated
    public static Character[][] convertToBlankMatrix(Character[][] m) { //this method was used in testing
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[i].length; j++)
                if (m[i][j] != null)
                    m[i][j] = ' ';
        }
        return m;
    }

    public static int menu(String[] a) { //creates a menu out of an array of Strings and returns the number index of the user's choice
        Scanner keyboard = new Scanner(System.in);
        int n = 0;
        try {
            showOptions(a);
            n = keyboard.nextInt() - 1;
            while (n < 0 || n > a.length - 1) {
                System.out.println("Bad input. Try again.");
                showOptions(a);
                n = keyboard.nextInt() - 1;
            }
        } catch (InputMismatchException e) {
            System.out.println("Bad input. Try again.");
            menu(a);
        }
        return n;
    }

    public static boolean checkAnswers() {
        boolean allRight = true;
        for (JTextField jt : textFields) {
            int x = getCoords(jt)[0];
            int y = getCoords(jt)[1];
            if (jt.getText().length() == 0)
                allRight = false;
            else if (jt.getText().charAt(0) != answerGrid[y][x]) {
                jt.setForeground(Color.RED);
                allRight = false;
                System.out.println("One incorrect");
            }
        }
        return allRight;
    }

    public static void showOptions(String[] a) { //part of the menu function that displays the list
        for (int i = 0; i < a.length; i++)
            System.out.println(i + 1 + ": " + a[i]);
    }

    public static ArrayList<Word> makeWordsList(String filename) { //method that reads from a file to create a list of words
        ArrayList<Word> fullList = new ArrayList<Word>();
        String t;
        try {
            Scanner in = new Scanner(new FileReader(filename));
            while (in.hasNextLine()) {
                t = in.nextLine();
                String word = t.split(": ")[0];
                String clue = t.split(": ")[1];
                if (word.length() > Crossword.goalSize)
                    throw new IllegalArgumentException();
                if (clue.length() > 40) {
                    clue = clue.substring(0, 39);
                    System.out.println("A clue was too long and was truncated.");
                }
                fullList.add(new Word(t.split(": ")[0], t.split(": ")[1]));
            }
        } catch (
                FileNotFoundException e) {
            System.out.println("File not found. Make sure you spelled the filename right.");
            System.exit(1);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Looks like you don't have colons between words and definitions. \nOr there's another bug in the word file.");
            System.exit(1);
        } catch (IllegalArgumentException e) {
            System.out.println("Looks like one of your words is too big. Check the file.");
            System.exit(1);
        }
        return fullList;
    }


    public static void savePuzzle(int[] a, Character[][] toSave, String filename) { //method that saves a puzzle to a txt file.
        try {
            Writer save = new FileWriter(filename, false);
            for (int i = 0; i < toSave.length; i++) {
                for (int j = 0; j < toSave[i].length; j++) {
                    if (toSave[i][j] == null)
                        save.write('0');
                    else save.write(toSave[i][j]);
                }
                save.write("\n");
            }
            for (int i = 0; i < a.length; i++) {
                System.out.println();
                save.write(String.valueOf(a[i]));
                save.write("\n");
            }
            save.close();
        } catch (IOException e) {
            System.out.println("Error! IOException, failed to save.\nCheck that the filename you wrote has no punctuation or strange symbols.");
        }
    }

    public static Pair<Character[][], ArrayList<Integer>> loadSave(String filename, int x, int y) { //method that creates a puzzle from a savefile.
        Character[][] charToReturn = new Character[x][y];
        ArrayList<Integer> intToReturn = new ArrayList<>();
        String t;
        try {
            Scanner in = new Scanner(new FileReader(filename));
            for (int i = 0; i < x; i++) {
                t = in.nextLine();
                for (int j = 0; j < y; j++) {
                    if (t.charAt(j) == '0')
                        charToReturn[i][j] = null;
                    else if (!Character.toString(t.charAt(j)).matches("[A-Z]"))
                        throw new IllegalArgumentException();
                    else charToReturn[i][j] = t.charAt(j);
                }
            }

            while (in.hasNextLine()) {
                intToReturn.add(Integer.parseInt(in.nextLine()));
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error! File not found.");
            System.exit(1);
        } catch (StringIndexOutOfBoundsException e) {
            System.out.println("Error in grid in saved file (too few characters). \nSorry, but something seems to have corrupted it.");
            System.exit(1);
        } catch (IllegalArgumentException e) {
            System.out.println("Error in grid in saved file (found a character that shouldn't be there). \nSorry, but something seems to have corrupted it.");
            System.exit(1);
        }
     //   Crossword.printMatrix(charToReturn); //test code
     //   System.out.println(intToReturn.size()); //test code
        return new Pair<>(charToReturn, intToReturn);
    }
}