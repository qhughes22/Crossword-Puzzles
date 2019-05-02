public class placedWord extends Word {
    private int Y; //Y position of word
    private int X; //X position of word
    private int clueNumber; //number the word is associated with on the grid

    enum direction {
        DOWN, ACROSS
    }

    direction d;


    public placedWord(String l, String s, int a, int b, direction d) {
        super(l, s);
        Y = a; // y position in grid
        X = b; //x position in grid
        this.d = d;
    }

    public direction getDirection() {
        return d;
    }

    @Deprecated
    public void setX(int a) {
        X = a;
    }

    @Deprecated
    public void setY(int a) {
        Y = a;
    }

    @Deprecated
    public void setDirection(direction a) {
        a = d;
    }

    public int getY() {
        return Y;
    }

    public int getX() {
        return X;
    }

    public void setCN(int a) {
        clueNumber = a;
    }

    public int getCN() {
        return clueNumber;
    }

}
