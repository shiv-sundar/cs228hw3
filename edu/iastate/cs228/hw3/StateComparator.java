package edu.iastate.cs228.hw3;

import java.util.Comparator;
import java.util.Scanner;

/**
 *  
 * @author Shivkarthi Sundar
 *
 */

/**
 * This method compares two states in the lexicographical order of the board configuration. 
 * The 3X3 array representing each board configuration is converted into a sequence of nine 
 * digits starting at the 0th row, and within each row, at the 0th column.  For example, the 
 * two states
 * 
 * 	   2 0 3        2 8 1 
 *     1 8 4        7 5 3 
 *     7 6 5        6 0 4 
 *
 * are converted into the sequences <2,0,3,1,8,4,7,6,5>, and <2,8,1,7,5,3,6,0,4>, respectively. 
 * By definition the first state is less than the second one.  
 * 
 * The comparator will be used for maintaining the CLOSED list used in the A* algorithm. 
 */
public class StateComparator implements Comparator<State> {
	@Override
	public int compare(State s1, State s2) {
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				if (s1.board[x][y] < s2.board[x][y]) {
					return -1;
				}
				
				if (s1.board[x][y] > s2.board[x][y]) {
					return 1;
				}
			}
		}
		
		return 0;
	}  		
}
