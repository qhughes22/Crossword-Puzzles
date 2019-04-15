import java.io.FileNotFoundException; //need to implement our own data structure
import java.io.FileReader; //need to implement timer? speed solving.
import java.util.ArrayList;
import java.util.Scanner;

public class test {

    public static void main(String[] args) {
        Character[][] a = new Character[4][5];
        a[0][1] = 'a';
        a[0][2] = 'b';
        a[1][2] = 'd';
        Crossword.printMatrix(convertToBlankMatrix(a));
        ArrayList<Word> tester = makeWordsList("src/testwords.txt");
        for(Word w: tester)
            System.out.println(w.getClue());
        Crossword c = new Crossword(tester, 1, 2);
        ArrayList<Integer> b = Crossword.findLetter("tfgja", 'a');
    }


    public static Character[][] convertToBlankMatrix(Character[][] m) {
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[i].length; j++)
                if (m[i][j] != null)
                    m[i][j] = ' ';
        }
        return m;
    }

    public static ArrayList<Word> makeWordsList(String filename) {
        ArrayList<Word> fullList = new ArrayList<Word>();
        String t;
        try {
            Scanner in = new Scanner(new FileReader(filename));
            while(in.hasNextLine()){
                t = in.nextLine();
                fullList.add(new Word(t.split(": ")[0],t.split(": ")[1]));
            }
        } catch (
                FileNotFoundException e) {
            System.out.println("File not found. Fuck off");
            System.exit(1);
        }
        return fullList;
    }
}