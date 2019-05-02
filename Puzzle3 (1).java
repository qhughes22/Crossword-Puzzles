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

public class Puzzle3 extends JPanel implements  ActionListener{

    public static final int WIDTH = 1000;
    public static final int HEIGHT = 1000;
    public static final int NUM_SQUARES = 10;
    public static JFrame frame = new JFrame("Crossword!");
    public static Character[][] m;

   
    public void actionPerformed(ActionEvent e) {   
    	JTextField jt = (JTextField)e.getSource();
    	Character c = jt.getText().charAt(0); //Gets the text in the textfield
    	Point p = jt.getLocation(); //Gets location of the textfield as a point
    	int div = (WIDTH/NUM_SQUARES);
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

	//JFrame frame = new JFrame("Crossword!!!");

	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setContentPane(new Puzzle3());
	frame.pack();
	frame.setVisible(true);
	//createMatrix();
    }
     @Override
	public void paintComponent(Graphics g) {
	m = createMatrix();
	super.paintComponent(g);
	g.setColor(Color.BLACK);
	g.fillRect(0,0,WIDTH,HEIGHT);
	ArrayList<JTextField> jt = new ArrayList<JTextField>();
	int counter = 0;
	for (int i=0; i<NUM_SQUARES; i++){              //DECIDE WHICH SQUARES NEED TO HAVE AN OPENING FOR A LETTER
	    for (int j = 0; j<NUM_SQUARES; j++){
		if(m[i][j] == 0){
		    g.setColor(Color.BLACK);
	    
		}  else{
		    g.setColor(Color.WHITE);
		}

		if (g.getColor() == Color.WHITE){

			jt.add(new JTextField("",WIDTH/NUM_SQUARES));
			jt.get(counter).setBounds(i*WIDTH/NUM_SQUARES, j*HEIGHT/NUM_SQUARES,WIDTH/NUM_SQUARES,WIDTH/NUM_SQUARES);
			Font font = new Font("Courier",Font.BOLD,WIDTH/NUM_SQUARES);
			jt.get(counter).setFont(font);
			jt.get(counter).setHorizontalAlignment(JTextField.CENTER);
			jt.get(counter).addActionListener(this);
		    frame.add(jt.get(counter));
		    counter++;
		}
		else{


		g.drawRect(i*WIDTH/NUM_SQUARES, j*HEIGHT/NUM_SQUARES, (i+1)*WIDTH/NUM_SQUARES, (j+1)*HEIGHT/NUM_SQUARES);
		g.fillRect(i*WIDTH/NUM_SQUARES, j*HEIGHT/NUM_SQUARES, (i+1)*WIDTH/NUM_SQUARES, (j+1)*HEIGHT/NUM_SQUARES);

		}
	    }
	}

	g.drawLine(0,50,WIDTH,50);    //CREATE THE GRID PATTERN
	g.drawLine(0,100,WIDTH,100);
	g.drawLine(0,150,WIDTH,150);
	g.drawLine(0,200,WIDTH,200);
	g.drawLine(0,250,WIDTH,250);
	g.drawLine(0,300,WIDTH,300);
	g.drawLine(0,350,WIDTH,350);
	g.drawLine(0,400,WIDTH,400);
	g.drawLine(0,450,WIDTH,450);
	g.drawLine(0,HEIGHT,WIDTH,HEIGHT);


	g.drawLine(50,0,50,HEIGHT); 
	g.drawLine(100,0,100,HEIGHT);
	g.drawLine(150,0,150,HEIGHT);
	g.drawLine(200,0,200,HEIGHT);
	g.drawLine(250,0,250,HEIGHT);
	g.drawLine(300,0,300,HEIGHT);
	g.drawLine(350,0,350,HEIGHT);
	g.drawLine(400,0,400,HEIGHT);
	g.drawLine(450,0,450,HEIGHT);
	g.drawLine(WIDTH,0,WIDTH,HEIGHT);

    }
    

    public static Character[][] createMatrix(){  //TEMPORARY MATRIX TO TEST SQUARE MAKING
	Character[][] m = { {0,0,'s',0,0,'d',0,0,0,0}, {0,0,'s','a','s','a','a','s',0,0}, {0,0,'a',0,0,'y',0,0,0,0}, {0,0,'a',0,0,'a',0,0,0,0},{'d','a','y',0,0,'a',0,'a',0,0}, {0,0,'a',0,0,'a','a','a','a',0}, {0,0,0,0,0,0,0,'a',0,0}, {0,0,0,0,0,0,0,'a',0,0}, {0,0,0,0,0,0,0,'a',0,0}, {0,0,0,0,0,0,0,'a',0,0} };
	System.out.println(m[0][0]);
	return m;
    }


    
}

