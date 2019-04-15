public class Pair <E,K> {
    private E thing1;
    private K thing2;

    public Pair(E a, K b){
        thing1 = a;
        thing2 = b;
    }

    public E get1(){return thing1;}
    public K get2(){return thing2;}

}
