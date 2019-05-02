// Aaron Jones

import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.Graphics;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

public class Puzzle3 extends JPanel implements ActionListener{

    public static final int WIDTH = 1200;
    public static final int HEIGHT =900;
    public static int NUM_SQUARES = 22;
    public static JFrame frame = new JFrame("Crossword!");
    public static Character[][] m;
    public static int[][] t;
    public static JButton hint = new JButton("Hint");
    public static JButton save = new JButton("Save");
 
    public void actionPerformed(ActionEvent e) {   
    	JTextField jt = (JTextField)e.getSource();
    	Character c = jt.getText().charAt(0); //Gets the text in the textfield
    	Point p = jt.getLocation(); //Gets location of the textfield as a point
    	int div = (900/NUM_SQUARES);
    	Double temp_x = p.getX()/div; //Gets x-coordinate of point (which square)
    	int x = temp_x.intValue();
    	Double temp_y = p.getY()/div; //Gets y-coordinate of point (which square)
    	int y = temp_y.intValue();
    	System.out.println(c);
    	System.out.println(x+ " "+y);
    	if(m[x][y]==c)
    	{
    		System.out.println("yay");
    	}
    }  


    public Puzzle3(){
	this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
    }


    public static void main(String[] args){

	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setContentPane(new Puzzle3());
	frame.pack();
	frame.setVisible(true);
    }
     @Override
	public void paintComponent(Graphics g) {
	m = createMatrix();
	t = testMatrix();
	super.paintComponent(g);
	g.setColor(Color.BLACK);
	g.fillRect(0,0,WIDTH,HEIGHT);

	hint.setBounds(1000,100,100,100);
	frame.add(hint);

	save.setBounds(1000,300,100,100);
	frame.add(save);

	ArrayList<JTextField> jt = new ArrayList<JTextField>();
	int counter = 0;
	for (int i=0; i<NUM_SQUARES; i++){              //DECIDE WHICH SQUARES NEED TO HAVE AN OPENING FOR A LETTER
	    for (int j = 0; j<NUM_SQUARES; j++){
		if(m[i][j] == null){
		    g.setColor(Color.BLACK);
	    
		}  else{
		    g.setColor(Color.WHITE);
		}

			if (g.getColor() == Color.WHITE){
			    
		    jt.add(new JTextField("",900/NUM_SQUARES));
		    jt.get(counter).setBounds(i*900/NUM_SQUARES, j*HEIGHT/NUM_SQUARES, 900/NUM_SQUARES,900/NUM_SQUARES);  
		    Font font = new Font("Courier",Font.BOLD,900/NUM_SQUARES);

		    jt.get(counter).setFont(font);
		    jt.get(counter).setHorizontalAlignment(JTextField.CENTER);
		    jt.get(counter).addActionListener(this);
		    frame.add(jt.get(counter));
		    counter++;
		    }

		else{
		    g.drawRect(i*900/NUM_SQUARES, j*HEIGHT/NUM_SQUARES, (i+1)*900/NUM_SQUARES, (j+1)*HEIGHT/NUM_SQUARES);   //Draw Black Squares For Null Characters
		g.fillRect(i*900/NUM_SQUARES, j*HEIGHT/NUM_SQUARES, (i+1)*900/NUM_SQUARES, (j+1)*HEIGHT/NUM_SQUARES);
 
		}

	    }
	}

	for (int i=0; i<NUM_SQUARES; i++){  //Assigning Numbers to Puzzle Row/Column
	    for (int j=0; j<NUM_SQUARES; j++){
		if(t[i][j] == 0){
		}
		else if (t[i][j-1] != 0){
		    g.setColor(Color.WHITE);
		    g.drawString(String.valueOf(t[i][j]),(i*900/NUM_SQUARES)-10, (j*900/NUM_SQUARES)+10);  //If Field Has Another Field Above It, Print Numbers On Side
		}
		else{
		    g.setColor(Color.WHITE);
		    g.drawString(String.valueOf(t[i][j]),i*900/NUM_SQUARES,j*HEIGHT/NUM_SQUARES);  //Otherwise Print Above Top Left Corner
		}
	    }
	}

     }
    

    public static Character[][] createMatrix(){  // TEMPORARY MATRIX TO TEST SQUARE MAKING
	Character[][] m = new Character [22][22];

	m[2][3]= 'd';
	m[2][4]= 'a';
	m[2][5]= 'y';
	m[3][4]='l';
	m[1][5]= 's';
	m[1][6]= 'e';
	m[1][7]= 'l';
	m[1][8]= 'l';
	m[5][5]='s';

	System.out.print(m.length);
	System.out.print(m[0].length);
	return m;
    }

    public static int[][] testMatrix(){ //TEMPORARY MATRIX TO TEST NUMBER ASSIGNMENT
	int[][] t = new int [22][22];
	t[2][3] = 1;
	t[2][4] = 2;

	return t;
    }
}



