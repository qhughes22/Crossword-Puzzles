import java.io.FileNotFoundException;
import java.io.FileReader; //need to implement timer? speed solving.
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class test {

    public static void main(String[] args) { //our test method. Creates a crossword
        System.out.println("Welcome to Aaron and Quincy's crossword puzzle!");
        System.out.println("Do you want to start a new puzzle or load an existing one?");
        int gameType = menu(new String[]{"new","load"});
        if(gameType==0)
            newPuzzle();
   //     checkAnswers(c.grid, c.grid);
    }

    public static void newPuzzle(){  //method for creating a new puzzle
        Scanner keyboard = new Scanner(System.in);
        int seed=0;  //the seed to be used in the Random
        boolean gotSeed = false; //a boolean for the while loop immediately after
        while(gotSeed==false) {
            try {
                System.out.println("Enter in an integer seed.");
                seed = keyboard.nextInt();
                gotSeed=true;
            } catch (InputMismatchException e) {
                System.out.println("Bad input. Try again.");
                keyboard.next();
            }
        }
        int size = 0; //the size of the puzzle
        boolean gotSize = false; // a boolean for the while loop immediately after
        while(gotSize==false) {
            try {
                System.out.println("Enter in how many words you want.");
                size = keyboard.nextInt();
                if(size>1)
                    gotSize=true;
                else System.out.println("Size must be greater than 1.");
            } catch (InputMismatchException e) {
                System.out.println("Bad input. Try again.");
            }
        }
        ArrayList<Word> tester = makeWordsList("src/testwords.txt");
        Crossword c = new Crossword(tester, seed, size);
//        Crossword.printMatrix(c.grid);
  //      Crossword.printMatrix(c.numGrid);
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

    public static int menu(String[] a) {
        Scanner keyboard = new Scanner(System.in);
        int n = 0;
        try {
            showOptions(a);
            n = keyboard.nextInt() - 1;
            while (n < 0 || n > a.length- 1) {
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

    public static void showOptions(String[] a) {
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

public static void getHint(Character[][] a, Character[][] b){


}


}

