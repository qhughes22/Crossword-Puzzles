import java.util.ArrayList;
import java.util.Random;
import static java.util.Collections.shuffle;


public class Crossword {
    Character[][] grid;
    ArrayList<Word> words = new ArrayList<Word>();
    ArrayList<ChosenWord> placedWords = new ArrayList<>();
    Random rand;
    private int acrossCount = 0;
    private int downCount = 0;

    public Crossword(ArrayList<Word> w, long seed, int size) {
        ArrayList<Word> chooseFrom = new ArrayList<>();
        for (Word word : w)
            chooseFrom.add(word);
        rand = new Random(seed);
        grid = new Character[1000][1000];
        int r;
        for (int i = 0; i < size; i++) {
            r = rand.nextInt(chooseFrom.size());
            words.add(chooseFrom.get(r));
            chooseFrom.remove(r);
        }
        shuffle(w, rand);
        Word f = w.get(0);
        ChosenWord.direction t;
        if (rand.nextBoolean())
            t = ChosenWord.direction.ACROSS;
        else t = ChosenWord.direction.DOWN;
        placeWord(f.getLetters(), ChosenWord.direction.ACROSS, 500, 500);
        placedWords.add(new ChosenWord(f.getLetters(), f.getClue(), 500, 500, 1, ChosenWord.direction.ACROSS));
        ArrayList<Integer> failedWords = new ArrayList<Integer>();
        boolean addedAny=true;
        for (int i = 1; i < size; i++)
            if (addWord(w.get(i)) == false) {
                System.out.println("failed to add " + w.get(i).getLetters());
                failedWords.add(i);
            }
        while(addedAny==true&&failedWords.size()!=0) {
            addedAny=false;
            for (int i = 0; i < failedWords.size(); i++) {
                if (addWord(w.get(failedWords.get(i))) == false)
                    System.out.println("failed to add " + w.get(failedWords.get(i)).getLetters());
                else {
                    failedWords.remove(i);
                    addedAny = true;
                }
            }
        }
        if(failedWords.size()!=0){
            System.out.println("failed to make puzzle. Word didn't fit");
            System.out.println("Creating new puzzle.");
            new Crossword(w,rand.nextLong(),size);
        }
        else shrinkGrid();
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
                        if (checkLocation(p.get1() - x, p.get2(), ChosenWord.direction.DOWN, w)) {
                            placeWord(w.getLetters(), ChosenWord.direction.DOWN, p.get1() - x, p.get2());
                            placedWords.add(new ChosenWord(w.getLetters(), w.getClue(), p.get1() - x, p.get2(), 3, ChosenWord.direction.DOWN));
                            return true;
                        }
                        if (checkLocation(p.get1(), p.get2() - x, ChosenWord.direction.ACROSS, w)) {
                            placeWord(w.getLetters(), ChosenWord.direction.ACROSS, p.get1(), p.get2() - x);
                            placedWords.add(new ChosenWord(w.getLetters(), w.getClue(), p.get1(), p.get2() - x, 3, ChosenWord.direction.ACROSS));
                            return true;
                        }
                    } else {
                        if (checkLocation(p.get1(), p.get2() - x, ChosenWord.direction.ACROSS, w)) {
                            placeWord(w.getLetters(), ChosenWord.direction.ACROSS, p.get1(), p.get2() - x);
                            placedWords.add(new ChosenWord(w.getLetters(), w.getClue(), p.get1(), p.get2() - x, 3, ChosenWord.direction.ACROSS));
                            return true;
                        }
                        if (checkLocation(p.get1() - x, p.get2(), ChosenWord.direction.DOWN, w)) {
                            placeWord(w.getLetters(), ChosenWord.direction.DOWN, p.get1() - x, p.get2());
                            placedWords.add(new ChosenWord(w.getLetters(), w.getClue(), p.get1() - x, p.get2(), 3, ChosenWord.direction.DOWN));
                            return true;
                        }
                    }
                }
        }
        return false;
    }


    public static ArrayList<Integer> findLetter(String s, Character c) {
        ArrayList<Integer> occurrences = new ArrayList<Integer>();
        String t = s;
        while (t.lastIndexOf(c) != -1) {
            occurrences.add(t.lastIndexOf(c));
            t = t.substring(0, t.lastIndexOf(c));
        }
        return occurrences;
    }

    public void shrinkGrid() { //makes into squares.
        int YLowerBound = grid.length;
        int YUpperBound = 0;
        int XLowerBound = grid[0].length;
        int XUpperBound = 0;
        for (ChosenWord c : placedWords) {
            if (c.getDirection()== ChosenWord.direction.DOWN&&c.getY() < YLowerBound)
                YLowerBound = c.getY();
            if (c.getDirection()== ChosenWord.direction.DOWN&&c.getY() + c.getLength() > YUpperBound)
                YUpperBound = c.getY() + c.getLength();
            if (c.getDirection()== ChosenWord.direction.ACROSS&&c.getX() < XLowerBound)
                XLowerBound = c.getX();
            if (c.getDirection()== ChosenWord.direction.ACROSS&&c.getX() + c.getLength() > XUpperBound)
                XUpperBound = c.getX() + c.getLength();
        }

        grid = new Character[Math.max(YUpperBound - YLowerBound, XUpperBound - XLowerBound)][Math.max(YUpperBound - YLowerBound, XUpperBound - XLowerBound)];
        for (ChosenWord c : placedWords) {
            placeWord(c.getLetters(), c.getDirection(), c.getY() - YLowerBound, c.getX() - XLowerBound);
        }
        printMatrix(grid);
    }

    public static <E> void printMatrix(E[][] m) {
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[i].length; j++)
                if (m[i][j] != null)
                    System.out.print(m[i][j]);
                else System.out.print(" ");
            System.out.println();
        }
    }

    public boolean checkLocation(int y, int x, ChosenWord.direction d, Word w) {
        if (d == ChosenWord.direction.ACROSS) {
            if (grid[y][x - 1] != null || grid[y][x + w.getLength()] != null)
                return false;
            for (int i = 0; i < w.getLength(); i++)
                if (grid[y][x + i] == null) {
                    if (grid[y - 1][x + i] != null || grid[y + 1][x + i] != null)
                        return false;
                } else if (grid[y][x + i] != w.getLetters().charAt(i))
                    return false;
        }
        if (d == ChosenWord.direction.DOWN) {
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

    public void placeWord(String letters, ChosenWord.direction dir, int yPos, int xPos) {
        if (dir == ChosenWord.direction.DOWN) {
            for (int i = 0; i < letters.length(); i++) {
                grid[yPos + i][xPos] = letters.charAt(i);
            }
        } else for (int i = 0; i < letters.length(); i++) {
            grid[yPos][xPos + i] = letters.charAt(i);
        }
    }

    public void addAcross() {
        acrossCount++;
    }

    public void addDown() {
        downCount++;
    }
}