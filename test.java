import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader; //need to implement timer? speed solving.
import java.util.*;

public class test {

    public static void main(String[] args) { //our test method. Creates a crossword
        System.out.println("Welcome to Aaron and Quincy's crossword puzzle!");
        System.out.println("Do you want to start a new puzzle or load an existing one?");
        int gameType = menu(new String[]{"new", "load"});
        if (gameType == 0)
            newPuzzle();
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
        Crossword c = makeFullCrossWord(tester, seed, size);
        Crossword.printMatrix(c.getGrid());              //test code
        Crossword.printMatrix(c.getNumGrid());           //test code
//        for (placedWord p : c.placedWords)          //test code
//           System.out.println(p.getCN());          //test code
    }

    public static String selectFile(String t) { //method that prompts the user to select a file and returns the filename.
        System.out.println("Select a file to read the list of words from. \nIf you don't see a file that should be there, make sure it's stored in wordfiles.");
        Scanner keyboard = new Scanner(System.in);
        File folder = new File(t);
        File[] files = folder.listFiles();
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

    public static Crossword makeFullCrossWord(ArrayList<Word> w, long seed, int size) { //this method keeps making crosswords until it creates one that uses all words.
        int timesTried = 0; //the number of times a puzzle has been attempted. This is so that the program doesn't run forever when it can't generate a puzzle
        Crossword c = new Crossword(w, seed, size);
        while (c.failedWords != 0) {
            System.out.println("failed to make puzzle. Word(s) didn't fit");
            System.out.println("Creating new puzzle.");
            timesTried++;
            if (timesTried < 50) {
                c = new Crossword(w, c.rand.nextLong(), size);
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
                fullList.add(new Word(t.split(": ")[0], t.split(": ")[1]));
            }
        } catch (
                FileNotFoundException e) {
            System.out.println("File not found. Make sure you spelled the filename right.");
            System.exit(1);
        }
        return fullList;
    }


//    public static boolean checkAnswers(Character[][] a, Character[][] b) {
//        boolean allGood = true;
//        if (a.length != b.length || a[0].length != b[0].length)
//            throw (new IllegalArgumentException("Matrices have different length"));
//        for (int i = 0; i < a.length; i++)
//            for (int j = 0; j < a[i].length; j++) {
//                if (a[i][j] != b[i][j]) {
//                    changeColor(i, j, Color.RED);
//                    allGood = false;
//                }
//
//            }
//        return allGood;
//    }


}

