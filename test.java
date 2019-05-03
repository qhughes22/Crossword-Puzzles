import java.io.*;
//import java.util.*;
//
//public class test {
//
//    public static Crossword main(String[] args) { //our test method. Creates a crossword
//        System.out.println("Welcome to Aaron and Quincy's crossword puzzle!");
//        System.out.println("Do you want to start a new puzzle or otherButton an existing one?");
//        int gameType = menu(new String[]{"new", "otherButton"});
//        if (gameType == 0)
//            newPuzzle();
//     //   if (gameType==1)
//       //     loadPuzzle();
//    }
////
////    public static Crossword loadPuzzle(){
//        Scanner keyboard = new Scanner(System.in);
//        System.out.println("Select a savegame to load.");
//        String fileChosen = selectFile("savegames");
//        ArrayList<Word> tester = makeWordsList("savegames/" + fileChosen);
//
//    }
//
//
//    public static Crossword newPuzzle() {  //method for creating a new puzzle
//        Scanner keyboard = new Scanner(System.in);
//        String fileChosen = selectFile("wordfiles");
//        ArrayList<Word> tester = makeWordsList("wordfiles/" + fileChosen);
//        int seed = 0;  //the seed to be used in the Random
//        boolean gotSeed = false; //a boolean for the while loop immediately after
//        while (gotSeed == false) {
//            try {
//                System.out.println("Enter in an integer seed.");
//                seed = keyboard.nextInt();
//                gotSeed = true;
//            } catch (InputMismatchException e) {
//                System.out.println("Bad input. Try again.");
//                keyboard.next();
//            }
//        }
//        int size = 0; //the size of the puzzle
//        boolean gotSize = false; // a boolean for the while loop immediately after
//        while (gotSize == false) {
//            try {
//                System.out.println("Enter in how many words you want.");
//                size = keyboard.nextInt();
//                if (size < 1) {
//                    System.out.println("Size must be greater than 1.");
//                } else if (size > tester.size()) {
//                    System.out.println("Size is too high, not enough words in file.");
//                } else gotSize = true;
//            } catch (InputMismatchException e) {
//                System.out.println("Bad input. Try again.");
//                keyboard.next();
//            }
//        }
//        System.out.println("Got it. Making your puzzle now.");
//        Crossword c = makeFullCrossWord(tester, seed, size);
//        Crossword.printMatrix(c.getGrid());              //test code
//        System.out.println(c.getGrid().length);
//        System.out.println(c.getGrid()[0].length);
////        for (placedWord p : c.placedWords)          //test code
////           System.out.println(p.getCN());          //test code
//        return c;
//    }
//
//    public static String selectFile(String t) { //method that prompts the user to select a file and returns the filename.
//        System.out.println("Select a file to read the list of words from. \nIf you don't see a file that should be there, make sure it's stored in wordfiles.");
//        Scanner keyboard = new Scanner(System.in);
//        File folder = new File(t);
//        File[] files = folder.listFiles();
//        if (files.length == 0) {
//            System.out.println("No files found in wordfiles.");
//            System.exit(1);
//        }
//        if (files.length == 1)
//            return files[0].getName();
//        String toReturn = menu(files);
//        if (toReturn.charAt(toReturn.length() - 1) != 't' || toReturn.charAt(toReturn.length() - 2) != 'x' || toReturn.charAt(toReturn.length() - 3) != 't' || toReturn.charAt(toReturn.length() - 4) != '.') {
//            System.out.println("Error. File must be a .txt file.");
//            System.exit(1);
//        }
//        return toReturn;
//    }
//
//
//    public static String menu(File[] s) { //creates a menu of files and returns the filename chosen
//        String[] toReturn = new String[s.length];
//        for (int i = 0; i < s.length; i++)
//            toReturn[i] = s[i].getName();
//        return toReturn[menu(toReturn)];
//    }
//
//    public static Crossword makeFullCrossWord(ArrayList<Word> w, int seed, int size) { //this method keeps making crosswords until it creates one that uses all words.
//        int timesTried = 0; //the number of times a puzzle has been attempted. This is so that the program doesn't run forever when it can't generate a puzzle
//        Crossword c = new Crossword(w, seed, size);
//        while (c.failed == true) {
//            System.out.print("failed to make puzzle.");
//            if(c.failedWords>0)
//                System.out.println(" Words didn't fit.");
//            else System.out.println(" Puzzle was too big.");
//            System.out.println("Creating new puzzle.");
//            timesTried++;
//            if (timesTried < 50) {
//                c = new Crossword(w, c.rand.nextInt(), size); //the fact that a new seed is made means that when saving a puzzle, the int stored as seed might not be what the user inputted
//            } else {
//                System.out.println("Tried 50 times. Either try again, or check that a crossword is possible with these words.");
//                System.exit(1);
//
//            }
//        }
//        return c;
//    }
//
//    @Deprecated
//    public static Character[][] convertToBlankMatrix(Character[][] m) { //this method was used in testing
//        for (int i = 0; i < m.length; i++) {
//            for (int j = 0; j < m[i].length; j++)
//                if (m[i][j] != null)
//                    m[i][j] = ' ';
//        }
//        return m;
//    }
//
//    public static int menu(String[] a) { //creates a menu out of an array of Strings and returns the number index of the user's choice
//        Scanner keyboard = new Scanner(System.in);
//        int n = 0;
//        try {
//            showOptions(a);
//            n = keyboard.nextInt() - 1;
//            while (n < 0 || n > a.length - 1) {
//                System.out.println("Bad input. Try again.");
//                showOptions(a);
//                n = keyboard.nextInt() - 1;
//            }
//        } catch (InputMismatchException e) {
//            System.out.println("Bad input. Try again.");
//            menu(a);
//        }
//        return n;
//    }
//
//    public static void showOptions(String[] a) { //part of the menu function that displays the list
//        for (int i = 0; i < a.length; i++)
//            System.out.println(i + 1 + ": " + a[i]);
//    }
//
//    public static ArrayList<Word> makeWordsList(String filename) { //method that reads from a file to create a list of words
//        ArrayList<Word> fullList = new ArrayList<Word>();
//        String t;
//        try {
//            Scanner in = new Scanner(new FileReader(filename));
//            while (in.hasNextLine()) {
//                t = in.nextLine();
//                fullList.add(new Word(t.split(": ")[0], t.split(": ")[1]));
//            }
//        } catch (
//                FileNotFoundException e) {
//            System.out.println("File not found. Make sure you spelled the filename right.");
//            System.exit(1);
//        } catch(ArrayIndexOutOfBoundsException e){
//            System.out.println("Looks like you don't have colons between words and definitions. \nOr there's another bug in the word file.");
//            System.exit(1);
//        }
//        return fullList;
//    }
//
//
//
//    public static void savePuzzle(int[] a, Character[][] toSave, String filename) { //method that saves a puzzle to a txt file.
//        try {
//            Writer save = new FileWriter(filename, false);
//            for (int i = 0; i < toSave.length; i++) {
//                for (int j = 0; j < toSave[i].length; j++) {
//                    if (toSave[i][j] == null)
//                        save.write('0');
//                    else save.write(toSave[i][j]);
//                }
//                save.write("\n");
//            }
//            for(int i=0;i<a.length;i++) {
//                System.out.println();
//                save.write(String.valueOf(a[i]));
//                save.write("\n");
//            }
//            save.close();
//        } catch (IOException e) {
//            System.out.println("Error! IOException, failed to save.");
//        }
//    }
//
//    public static Pair<Character[][], ArrayList<Integer>> loadSave(String filename, int x, int y){ //method that creates a puzzle from a savefile.
//        Character[][] charToReturn = new Character[x][y];
//        ArrayList<Integer> intToReturn = new ArrayList<>();
//        String t;
//        try {
//            Scanner in = new Scanner(new FileReader(filename));
//            for (int i = 0; i < x; i++) {
//                t = in.nextLine();
//                for (int j = 0; j < y; j++) {
//                    if (t.charAt(j) == '0')
//                        charToReturn[i][j] = null;
//                    else if(!Character.toString(t.charAt(j)).matches("[A-Z]"))
//                        throw new IllegalArgumentException();
//                    else charToReturn[i][j] = t.charAt(j);
//                }
//            }
//            while (in.hasNextLine()) {
//                intToReturn.add(Integer.parseInt(in.nextLine()));
//            }
//        } catch (FileNotFoundException e) {
//            System.out.println("Error! File not found.");
//            System.exit(1);
//        }
//        catch(StringIndexOutOfBoundsException e){
//            System.out.println("Error in grid in saved file (too few characters). \nSorry, but something seems to have corrupted it.");
//            System.exit(1);
//        }
//        catch(IllegalArgumentException e){
//            System.out.println("Error in grid in saved file (found a character that shouldn't be there). \nSorry, but something seems to have corrupted it.");
//            System.exit(1);
//        }
//        return new Pair<Character[][], ArrayList<Integer>>(charToReturn, intToReturn);
//    }
//
//}
//
//
//
