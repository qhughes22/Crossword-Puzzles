import java.util.ArrayList; //add a chosenwords class that extends this and has most of the functionality

public class Word {
    private String clue;
    private String letters;
    private int length;

    public Word(String l, String c) {
        clue = c;
        letters = l;
        length = letters.length();
    }

    public int getLength(){
        return length;
    }

    public String getClue() {
        return clue;
    }

    public String getLetters() {
        return letters;
    }




}
