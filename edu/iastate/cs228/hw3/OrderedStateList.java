package edu.iastate.cs228.hw3;

/**
 *  
 * @author Shivkarthi Sundar
 *
 */

/**
 * This class describes a circular doubly-linked list of states to represent both the OPEN and CLOSED lists
 * used by the A* algorithm.  The states on the list are sorted in the  
 * 
 *     a) order of non-decreasing cost estimate for the state if the list is OPEN, or 
 *     b) lexicographic order of the state if the list is CLOSED.  
 * 
 */
public class OrderedStateList {
	/**
	 * Implementation of a circular doubly-linked list with a dummy head node.
	 */
	private State head; 
	private int size = 0;
	private boolean isOPEN;

	/**
	 *  Default constructor constructs an empty list. Initialize heuristic. Set the fields next and 
	 *  previous of head to the node itself. 
	 * 
	 * @param h 
	 * @param isOpen   
	 */
	public OrderedStateList(Heuristic h, boolean isOpen) {
		State.heu = h;
		this.isOPEN = isOpen;
		head = new State(new int[3][3]);
		head.next = head;
		head.previous = head;
	}


	public int size() {
		return size; 
	}

	/**
	 * A new state is added to the sorted list.  Traverse the list starting at head.  Stop 
	 * right before the first state t such that compareStates(s, t) <= 0, and add s before t. If 
	 * no such state exists, simply add s to the end of the list. 
	 * 
	 * Precondition: s does not appear on the sorted list. 
	 * 
	 * @param s
	 */
	public void addState(State s) {
		if (size() == 0) {
			head.next = s;
			head.previous = s;
			s.next = head;
			s.previous = head;
			size++;
		}

		else {
			State test = head.next;
			while (compareStates(s, test) > 0) {
				if (test.next != head) {
					test = test.next;
				}

				else {
					head.previous = s;
					test.next = s;
					s.next = head;
					s.previous = test;
					size++;
				}
			}

			if (findState(s) == null) {
				s.next = test;
				s.previous = test.previous;
				test.previous.next = s;
				test.previous = s;
				size++;
			}
		}
	}


	/**
	 * Conduct a sequential search on the list for a state that has the same board configuration 
	 * as the argument state s.  
	 * 
	 * Calls equals() from the State class. 
	 * 
	 * @param s
	 * @return the state on the list if found
	 *         null if not found 
	 */
	public State findState(State s) {
		State toReturn = head.next;
		if (toReturn == head) {
			return null;
		}
		
		StateComparator sc = new StateComparator();
		while (sc.compare(toReturn, s) != 0) {
			if (toReturn == head) {
				return null;
			}

			else {
				toReturn = toReturn.next;
			}
		}
		
		return toReturn;
	}


	/**
	 * Remove the argument state s from the list.  It is used by the A* algorithm in maintaining 
	 * both the OPEN and CLOSED lists. 
	 * 
	 * @param s
	 * @throws IllegalStateException if s is not on the list 
	 */
	public void removeState(State s) throws IllegalStateException {
		State newState = findState(s);
		if (newState == null) {
			throw new IllegalStateException();
		}

		newState.previous.next = newState.next;
		newState.next.previous = newState.previous;
		size--;
	}


	/**
	 * Remove the first state on the list and return it.  This is used by the A* algorithm in maintaining
	 * the OPEN list. 
	 * 
	 * @return  
	 */
	public State remove() {
		State s = head.next;
		s.next.previous = head;
		head.next = s.next;
		size--;
		return s;
	}

	/**
	 * Compare two states depending on whether this OrderedStateList object is the list OPEN 
	 * or the list CLOSE used by the A* algorithm.  More specifically,  
	 * 
	 *     a) call the method compareTo() of the State if isOPEN == true, or 
	 *     b) create a StateComparator object to call its compare() method if isOPEN == false. 
	 * 
	 * @param s1
	 * @param s2
	 * @return
	 */
	private int compareStates(State s1, State s2) {
		if (isOPEN) {
			return s1.compareTo(s2);
		}

		else {
			StateComparator newComp = new StateComparator();
			return newComp.compare(s1, s2);
		}
	}
}
