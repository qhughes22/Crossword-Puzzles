public class ChosenWord extends Word {
    private int Y;
    private int X;
    private int clueNumber;

    enum direction{
        DOWN, ACROSS
    }
    direction d;



    public ChosenWord(String l, String s, int a, int b, int c, direction d){
        super(l,s);
        Y =a;
        X =b;
        clueNumber=c;
        this.d=d;
    }

    public direction getDirection(){return d;}
    public void setX(int a){
        Y =a;}
    public void setY(int a){
        X =a;}
    public void setDirection(direction a){a=d;}

    public int getY(){return Y;}
    public int getX(){return X;}
    public void setCN(int a) {
        clueNumber = a;
    }

    public int getCN() {
        return clueNumber;
    }

}
