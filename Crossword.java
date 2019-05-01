import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static java.util.Collections.shuffle;


public class Crossword { //the class for generating the answers.
    private static int timesTried = 0; //the number of times a puzzle has been attempted. This is so that the program doesn't run forever when it can't generate a puzzle
    public static ArrayList<Character> alphabet = new ArrayList<>(Arrays.asList('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'));
    Character[][] grid; //stores the final grid
    Integer[][] numGrid; //stores the numbers for clues
    private ArrayList<Word> chosenWords = new ArrayList<Word>(); //the words selected for the puzzle
    ArrayList<placedWord> placedWords = new ArrayList<>(); //the words that have been placed
    Random rand; //the rand, created by seed


    public Crossword(ArrayList<Word> w, long seed, int size) { //constructor. Calls itself recursively if it fails to make a puzzle
        ArrayList<Word> chooseFrom = new ArrayList<>();
        for (Word word : w)
            chooseFrom.add(word);
        rand = new Random(seed);
        grid = new Character[1000][1000];
        int r;
        for (int i = 0; i < size; i++) {
            r = rand.nextInt(chooseFrom.size());
            chosenWords.add(chooseFrom.get(r));
            chooseFrom.remove(r);
        }
        shuffle(w, rand);
        Word f = w.get(0);
        placedWord.direction t;
        if (rand.nextBoolean())
            t = placedWord.direction.ACROSS;
        else t = placedWord.direction.DOWN;
        placeWord(f.getLetters(), t, 500, 500);
        placedWords.add(new placedWord(f.getLetters(), f.getClue(), 500, 500, 1, t));
        ArrayList<Integer> failedWords = new ArrayList<Integer>();
        boolean addedAny = true;
        for (int i = 1; i < size; i++)
            if (addWord(w.get(i)) == false) {
                System.out.println("failed to add " + w.get(i).getLetters());
                failedWords.add(i);
            }
        while (addedAny == true && failedWords.size() != 0) {
            addedAny = false;
            for (int i = 0; i < failedWords.size(); i++) {
                if (addWord(w.get(failedWords.get(i))) == false)
                    System.out.println("failed to add " + w.get(failedWords.get(i)).getLetters());
                else {
                    failedWords.remove(i);
                    addedAny = true;
                }
            }
        }
        Crossword next;
        if (failedWords.size() != 0) {
            System.out.println("failed to make puzzle. Word didn't fit");
            System.out.println("Creating new puzzle.");
            timesTried++;
            if (timesTried < 50) {
                next = new Crossword(w, rand.nextLong(), size);
                grid=next.grid;
                chosenWords=next.chosenWords;
                placedWords=next.placedWords;
            } else {
                System.out.println("Tried 50 times. Either start again, or check that a crossword is possible with these words.");
                System.exit(1);
            }
        }
        shrinkGrid();
        System.out.println(grid.length);
        numGrid = new Integer[grid.length][grid[0].length];
        int num = 1;
        for (int i = 0; i < grid.length; i++)
            for (int j = 0; j < grid[i].length; j++) {
                for (placedWord p : placedWords) {
                    if (p.getX() == j && p.getY() == i) {
                        if (numGrid[i][j] == null) {
                            numGrid[i][j] = num;
                            p.setCN(num);
                            num++;
                        } else p.setCN(num);
                    }
                }
            }
        System.out.println(grid.length);
    }


    public boolean addWord(Word w) {
        ArrayList<Character> letters = new ArrayList<Character>();
        ArrayList<Pair<Integer, Integer>> possibilities = new ArrayList<>();
        ArrayList<Integer> letNum = new ArrayList<>();
        for (int i = 0; i < w.getLength(); i++) {
            letters.add(w.getLetters().charAt(i));
            letNum.add(i);
        }
        shuffle(letNum, rand);
        Character c;
        for (int n : letNum) {
            c = letters.get(letNum.get(n));
            for (int i = 0; i < grid.length; i++)
                for (int j = 0; j < grid[0].length; j++) {
                    try {
                        if (grid[i][j].equals(c))
                            possibilities.add(new Pair<Integer, Integer>(i, j));
                    } catch (NullPointerException e) {
                    }
                }
            shuffle(possibilities, rand);
            ArrayList<Integer> occurrences = findLetter(w.getLetters(), c);
            shuffle(occurrences, rand);
            for (Pair<Integer, Integer> p : possibilities)
                for (Integer x : occurrences) {
                    if (rand.nextBoolean()) {
                        if (checkLocation(p.get1() - x, p.get2(), placedWord.direction.DOWN, w)) {
                            placeWord(w.getLetters(), placedWord.direction.DOWN, p.get1() - x, p.get2());
                            placedWords.add(new placedWord(w.getLetters(), w.getClue(), p.get1() - x, p.get2(), 3, placedWord.direction.DOWN));
                            return true;
                        }
                        if (checkLocation(p.get1(), p.get2() - x, placedWord.direction.ACROSS, w)) {
                            placeWord(w.getLetters(), placedWord.direction.ACROSS, p.get1(), p.get2() - x);
                            placedWords.add(new placedWord(w.getLetters(), w.getClue(), p.get1(), p.get2() - x, 3, placedWord.direction.ACROSS));
                            return true;
                        }
                    } else {
                        if (checkLocation(p.get1(), p.get2() - x, placedWord.direction.ACROSS, w)) {
                            placeWord(w.getLetters(), placedWord.direction.ACROSS, p.get1(), p.get2() - x);
                            placedWords.add(new placedWord(w.getLetters(), w.getClue(), p.get1(), p.get2() - x, 3, placedWord.direction.ACROSS));
                            return true;
                        }
                        if (checkLocation(p.get1() - x, p.get2(), placedWord.direction.DOWN, w)) {
                            placeWord(w.getLetters(), placedWord.direction.DOWN, p.get1() - x, p.get2());
                            placedWords.add(new placedWord(w.getLetters(), w.getClue(), p.get1() - x, p.get2(), 3, placedWord.direction.DOWN));
                            return true;
                        }
                    }
                }
        }
        return false;
    }


    public static ArrayList<Integer> findLetter(String s, Character c) { //method to find a particular letter in a String
        ArrayList<Integer> occurrences = new ArrayList<Integer>();
        String t = s;
        while (t.lastIndexOf(c) != -1) {
            occurrences.add(t.lastIndexOf(c));
            t = t.substring(0, t.lastIndexOf(c));
        }
        return occurrences;
    }

    public void shrinkGrid() { //shrinks the grid down to minimum possible square
        int YLowerBound = grid.length;
        int YUpperBound = 0;
        int XLowerBound = grid[0].length;
        int XUpperBound = 0;
        for (placedWord c : placedWords) {
            if (c.getDirection() == placedWord.direction.DOWN && c.getY() < YLowerBound)
                YLowerBound = c.getY();
            if (c.getDirection() == placedWord.direction.DOWN && c.getY() + c.getLength() > YUpperBound)
                YUpperBound = c.getY() + c.getLength();
            if (c.getDirection() == placedWord.direction.ACROSS && c.getX() < XLowerBound)
                XLowerBound = c.getX();
            if (c.getDirection() == placedWord.direction.ACROSS && c.getX() + c.getLength() > XUpperBound)
                XUpperBound = c.getX() + c.getLength();
        }

        grid = new Character[Math.max(YUpperBound - YLowerBound, XUpperBound - XLowerBound)][Math.max(YUpperBound - YLowerBound, XUpperBound - XLowerBound)];
        for (placedWord c : placedWords) {
            placeWord(c.getLetters(), c.getDirection(), c.getY() - YLowerBound, c.getX() - XLowerBound);
        }
    }

    public static <E> void printMatrix(E[][] m) { //method to print matrix, with null values being printed as spaces. Used for testing
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[i].length; j++)
                if (m[i][j] != null)
                    System.out.print(m[i][j]);
                else System.out.print(" ");
            System.out.println();
        }
    }

    public boolean checkLocation(int y, int x, placedWord.direction d, Word w) { //method to determine if a particular word can be placed in a location
        if (d == placedWord.direction.ACROSS) {
            if (grid[y][x - 1] != null || grid[y][x + w.getLength()] != null)
                return false;
            for (int i = 0; i < w.getLength(); i++)
                if (grid[y][x + i] == null) {
                    if (grid[y - 1][x + i] != null || grid[y + 1][x + i] != null)
                        return false;
                } else if (grid[y][x + i] != w.getLetters().charAt(i))
                    return false;
        }
        if (d == placedWord.direction.DOWN) {
            if (grid[y - 1][x] != null || grid[y + w.getLength()][x] != null)
                return false;
            for (int i = 0; i < w.getLength(); i++)
                if (grid[y + i][x] == null) {
                    if (grid[y + i][x - 1] != null || grid[y + i][x + 1] != null)
                        return false;
                } else if (grid[y + i][x] != w.getLetters().charAt(i))
                    return false;
        }
        return true;
    }

    public void placeWord(String letters, placedWord.direction dir, int yPos, int xPos) { //method that places a word on the grid
        if (dir == placedWord.direction.DOWN) {
            for (int i = 0; i < letters.length(); i++) {
                grid[yPos + i][xPos] = letters.charAt(i);
            }
        } else for (int i = 0; i < letters.length(); i++) {
            grid[yPos][xPos + i] = letters.charAt(i);
        }
    }

}