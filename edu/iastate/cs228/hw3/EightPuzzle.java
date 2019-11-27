package edu.iastate.cs228.hw3;

import java.util.ArrayList;
import java.util.EnumSet;

/**
 *  
 * @author Shivkarthi Sundar
 *
 */

public class EightPuzzle {
	/**
	 * This static method solves an 8-puzzle with a given initial state using two heuristics which 
	 * compare the board configuration with the goal configuration by the number of mismatched tiles, 
	 * and by the Manhattan distance, respectively.  The goal configuration is set for all puzzles as
	 * 
	 * 			1 2 3
	 * 			8   4
	 * 			7 6 5
	 * 
	 * @param s0
	 * @return
	 */
	public static String solve8Puzzle(State s0) {
		if (!s0.solvable()) {
			return "No solution exists for the following initial state: " + '\n' + s0.toString();
		}

		Heuristic h[] = {Heuristic.ManhattanDist, Heuristic.TileMismatch}; 
		String[] moves = new String[2]; 
		for (int i = 0; i < 2; i++)
		{
			moves[i] = AStar(s0, h[i]); 
		}

		String toReturn = new String();
		toReturn += "Heuristic: " + h[0].toString() + '\n' + moves[0] + '\n';
		toReturn += "Heuristic: " + h[1].toString() + '\n' + moves[1] + '\n';
		return toReturn;
	}


	/**
	 * This method implements the A* algorithm to solve the 8-puzzle with an input initial state s0. 
	 * The algorithm is described in Section 3 of the project description. 
	 * 
	 * Precondition: the puzzle is solvable with the initial state s0.
	 * 
	 * @param s0  initial state
	 * @param h   heuristic 
	 * @return    solution string 
	 */
	public static String AStar(State s0, Heuristic h) { 
		OrderedStateList OPEN = new OrderedStateList(h, true); 
		OrderedStateList CLOSE = new OrderedStateList(h, false);
		EightPuzzle e = new EightPuzzle();
		if (!s0.solvable()) {
			return null;
		}

		OPEN.addState(s0);
		while (OPEN.size() != 0) {
			State s = OPEN.remove();
			CLOSE.addState(s);
			if (s.isGoalState()) {
				return e.solutionPath(s);
			}

			State[] sArray = new State[4];
			EnumSet<Move> moves = EnumSet.allOf(Move.class);
			Move[] m = new Move[4];
			moves.toArray(m);
			for (int x = 0; x < 4; x++) {
				try {
					sArray[x] = s.successorState(m[x]);
				}

				catch (IllegalArgumentException q) {

				}
			}

			for (State s1 : sArray) {
				if (s1 != null) {
					if (OPEN.findState(s1) == null && CLOSE.findState(s1) == null) {
						OPEN.addState(s1);
					}

					else if (OPEN.findState(s1) != null) {
						if (s1.compareTo(OPEN.findState(s1)) < 0) {
							OPEN.findState(s1).predecessor = s1.predecessor;
							OPEN.findState(s1).numMoves = s1.numMoves;
						}
					}

					else if (CLOSE.findState(s1) != null) {
						if (s1.compareTo(CLOSE.findState(s1)) < 0) {
							CLOSE.findState(s1).predecessor = s1.predecessor;
							CLOSE.findState(s1).numMoves = s1.numMoves;
							OPEN.addState(CLOSE.findState(s1));
							CLOSE.removeState(s1);
						}
					}
				}
			}
		}

		return null;
	}



	/**
	 * From a goal state, follow the predecessor link to trace all the way back to the initial state. 
	 * Meanwhile, generate a string to represent board configurations in the reverse order, with 
	 * the initial configuration appearing first. Between every two consecutive configurations 
	 * is the move that causes their transition. A blank line separates a move and a configuration.  
	 * In the string, the sequence is preceded by the total number of moves and a blank line. 
	 * 
	 * See Section 5 in the projection description for an example. 
	 * 
	 * Call the toString() method of the State class. 
	 * 
	 * @param goal
	 * @return
	 */
	private String solutionPath(State goal) {
		String toReturn = "Total Moves: ";
		int moves = 0;
		State newState = goal;
		String configsAndMoves = new String();
		while (newState.predecessor != null) {
			configsAndMoves = newState.move.toString() + '\n' + '\n' + newState.toString() + '\n' + '\n' + configsAndMoves;
			moves += 1;
			newState = newState.predecessor;
		}

		configsAndMoves = newState.toString() + '\n' + '\n' + configsAndMoves;
		toReturn += Integer.toString(moves) + '\n' + '\n';
		return toReturn + configsAndMoves;
	}
}
