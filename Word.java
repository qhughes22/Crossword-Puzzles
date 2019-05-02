public class Word {
    private String clue; //the clue associated with the word
    private String letters; //the word itself
    private int length; //the length of the word

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
