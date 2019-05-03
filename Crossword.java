import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static java.util.Collections.shuffle;


public class Crossword { //the class for generating the answers.
    public static ArrayList<Character> alphabet = new ArrayList<>(Arrays.asList('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'));
    private Character[][] grid; //stores the final grid
    private int[][] numGrid; //stores the numbers for clues
    private ArrayList<Word> wordsChosen = new ArrayList<Word>(); //the words selected for the puzzle
    private ArrayList<placedWord> wordsPlaced = new ArrayList<>(); //the words that have been placed
    Random rand; //the rand, created by seed
    int failedWords = 0; //how many words were not added
    boolean failed = false; //whether it failed, either due to words not added or the grid being too big
    final static int goalSize = 20; //this is the size of the matrix that the graphics is expecting to receive
    public static int originalSeed;
    private int seed; //stores the seed. Need to store for save/otherButton function

    public Crossword(ArrayList<Word> w, int seed, int size) { //constructor. Potentially will fail to add every word and simply returns before finishing if that happens.
        this.seed=seed;
        ArrayList<Word> chooseFrom = new ArrayList<>();
        for (Word word : w)
            chooseFrom.add(word);
        rand = new Random(seed);
        grid = new Character[1000][1000];
        int r;
        for (int i = 0; i < size; i++) {
            r = rand.nextInt(chooseFrom.size());
            wordsChosen.add(chooseFrom.get(r));
            chooseFrom.remove(r);
        }
        shuffle(w, rand);
        Word f = w.get(0);
        placedWord.direction t;
        if (rand.nextBoolean())
            t = placedWord.direction.ACROSS;
        else t = placedWord.direction.DOWN;
        placeWord(f.getLetters(), t, 500, 500);
        wordsPlaced.add(new placedWord(f.getLetters(), f.getClue(), 500, 500, t));
        ArrayList<Integer> failedToAdd = new ArrayList<Integer>();
        boolean addedAny = true;
        for (int i = 1; i < size; i++)
            if (addWord(w.get(i)) == false) {
                System.out.println("failed to add " + w.get(i).getLetters());
                failedToAdd.add(i);
            }
        while (addedAny == true && failedToAdd.size() != 0) {
            addedAny = false;
            for (int i = 0; i < failedToAdd.size(); i++) {
                if (addWord(w.get(failedToAdd.get(i))) == false)
                    System.out.println("failed to add " + w.get(failedToAdd.get(i)).getLetters());
                else {
                    failedToAdd.remove(i);
                    addedAny = true;
                }
            }
            failedWords = failedToAdd.size();
            if (failedWords > 0) {
                failed = true;
                return;
            }
        }
        shrinkGrid();
//        System.out.println(grid.length);   //test code
//        System.out.println(grid[0].length);  //test code
        if (grid.length > goalSize || grid[0].length > goalSize) {
            failed = true;
            return;
        }
        growGrid();
        numGrid = new int[grid.length][grid[0].length];
        int num = 0;
        for (int i = 0; i < grid.length; i++)
            for (int j = 0; j < grid[i].length; j++) {
                for (placedWord p : wordsPlaced) {
                    if (p.getX() == j && p.getY() == i) {
                        if (numGrid[i][j] == 0) {
                            num++;
                            numGrid[i][j] = num;
                            p.setCN(num);
                        } else p.setCN(num);
                    }
                }
            }
        }


    private void growGrid() {//grows the grid into the correct size. Leaves the first row and first column empty as buffer
        Character[][] grownGrid = new Character[goalSize+2][goalSize+2];
        for(int i=0;i<grid.length;i++)
            for(int j=0;j<grid[0].length;j++)
                grownGrid[i+1][j+1]=grid[i][j];
        grid=grownGrid;
        for(placedWord p: wordsPlaced) {
            p.setX(p.getX() + 1);
            p.setY(p.getY()+1);
        }
    }

    private boolean addWord(Word w) {
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
            for (Pair<Integer, Integer> p : possibilities) //this section will place the word if possible in every potential location. There is duplicate code because it randomizes whether it tries to place down or across first
                for (Integer x : occurrences) {
                    if (rand.nextBoolean()) {
                        if (checkLocation(p.get1() - x, p.get2(), placedWord.direction.DOWN, w)) {
                            placeWord(w.getLetters(), placedWord.direction.DOWN, p.get1() - x, p.get2());
                            wordsPlaced.add(new placedWord(w.getLetters(), w.getClue(), p.get1() - x, p.get2(), placedWord.direction.DOWN));
                            return true;
                        }
                        if (checkLocation(p.get1(), p.get2() - x, placedWord.direction.ACROSS, w)) {
                            placeWord(w.getLetters(), placedWord.direction.ACROSS, p.get1(), p.get2() - x);
                            wordsPlaced.add(new placedWord(w.getLetters(), w.getClue(), p.get1(), p.get2() - x, placedWord.direction.ACROSS));
                            return true;
                        }
                    } else {
                        if (checkLocation(p.get1(), p.get2() - x, placedWord.direction.ACROSS, w)) {
                            placeWord(w.getLetters(), placedWord.direction.ACROSS, p.get1(), p.get2() - x);
                            wordsPlaced.add(new placedWord(w.getLetters(), w.getClue(), p.get1(), p.get2() - x, placedWord.direction.ACROSS));
                            return true;
                        }
                        if (checkLocation(p.get1() - x, p.get2(), placedWord.direction.DOWN, w)) {
                            placeWord(w.getLetters(), placedWord.direction.DOWN, p.get1() - x, p.get2());
                            wordsPlaced.add(new placedWord(w.getLetters(), w.getClue(), p.get1() - x, p.get2(), placedWord.direction.DOWN));
                            return true;
                        }
                    }
                }
        }
        return false;
    }


    private static ArrayList<Integer> findLetter(String s, Character c) { //method to find occurrences of a particular character in a String
        ArrayList<Integer> occurrences = new ArrayList<Integer>();
        String t = s;
        while (t.lastIndexOf(c) != -1) {
            occurrences.add(t.lastIndexOf(c));
            t = t.substring(0, t.lastIndexOf(c));
        }
        return occurrences;
    }

    private void shrinkGrid() { //shrinks the grid down to minimum possible square
        int YLowerBound = grid.length;
        int YUpperBound = 0;
        int XLowerBound = grid[0].length;
        int XUpperBound = 0;
        for (placedWord c : wordsPlaced) {
            if (c.getDirection() == placedWord.direction.DOWN && c.getY() < YLowerBound)
                YLowerBound = c.getY();
            if (c.getDirection() == placedWord.direction.DOWN && c.getY() + c.getLength() > YUpperBound)
                YUpperBound = c.getY() + c.getLength();
            if (c.getDirection() == placedWord.direction.ACROSS && c.getX() < XLowerBound)
                XLowerBound = c.getX();
            if (c.getDirection() == placedWord.direction.ACROSS && c.getX() + c.getLength() > XUpperBound)
                XUpperBound = c.getX() + c.getLength();
        }

        grid = new Character[YUpperBound - YLowerBound][XUpperBound - XLowerBound];
        ArrayList<placedWord> shrunkPlaced = new ArrayList<>();
        for (placedWord c : wordsPlaced) {
            placeWord(c.getLetters(), c.getDirection(), c.getY() - YLowerBound, c.getX() - XLowerBound);
            shrunkPlaced.add(new placedWord(c.getLetters(), c.getClue(), c.getY() - YLowerBound, c.getX() - XLowerBound, c.getDirection()));
        }
        wordsPlaced = shrunkPlaced;
    }

    public static <E> void printMatrix(E[][] m) { //method to print matrix, with null values being printed as spaces. Used for testing
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[i].length; j++)
                if (m[i][j] != null)
                    System.out.print(m[i][j]);
                else System.out.print("0");
            System.out.println();
        }
    }

    private boolean checkLocation(int y, int x, placedWord.direction d, Word w) { //method to determine if a particular word can be placed in a location
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

    private void placeWord(String letters, placedWord.direction dir, int yPos, int xPos) { //method that places a word on the grid
        if (dir == placedWord.direction.DOWN) {
            for (int i = 0; i < letters.length(); i++) {
                grid[yPos + i][xPos] = letters.charAt(i);
            }
        } else for (int i = 0; i < letters.length(); i++) {
            grid[yPos][xPos + i] = letters.charAt(i);
        }
    }


    public Character[][] getGrid() {
        return grid;
    }

    public int[][] getNumGrid() {
        return numGrid;
    }

    public int getSize(){
        return wordsPlaced.size();
    }

    public int getSeed(){
        return seed;
    }

    public ArrayList<placedWord> getWordsPlaced(){return wordsPlaced;
    }
}

