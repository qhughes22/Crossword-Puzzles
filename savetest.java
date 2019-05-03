//import java.io.*;
//import java.util.ArrayList;
//import java.util.Scanner;
//
//public class savetest {
//
//
//
//    public static void main(String[] args) {
//        Scanner keyboard=new Scanner(System.in);
//        int s = keyboard.nextInt();
//        if(s==1) {
//            Character[][] test = new Character[5][5];
//            test[0][1] = 'a';
//            test[4][0] = 'b';
//            Main.savePuzzle(new int[]{1, 2, 3}, test, "testsave.txt");
//        }
//        else {
//            Pair<Character[][], ArrayList<Integer>> loaded = Main.loadSave("testsave.txt", 5, 5);
//            Crossword.printMatrix(loaded.get1());
//            for (Integer n : loaded.get2())
//                System.out.println(n);
//        }
//    }
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
//                while (in.hasNextLine()) {
//                    intToReturn.add(Integer.parseInt(in.nextLine()));
//                }
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
