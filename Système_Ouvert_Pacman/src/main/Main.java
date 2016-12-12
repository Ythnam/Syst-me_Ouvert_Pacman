package main;

import javax.swing.JFrame;
import model.Map;
import view.Field;

public class Main{

	static JFrame frame;
	public static void main(String[] args) {
		Map wall = new Map();
		frame = new JFrame();
		Field field = new Field(frame);
		frame.setResizable(true);
		frame.setLocation(100, 100);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setContentPane(field);
		frame.setVisible(true);
		frame.pack();		
	}
}
