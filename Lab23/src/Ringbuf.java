/*****************************************************************
 * filename: Ringbuf.java
 * author: Paul Haskell and Leisha Figuerres for student methods
 * Implement a doubly linked ring that stores char values.
 * The main() method creates a ring, manipulates it, and prints it.
 ****************************************************************/


/** class DoubleRing
 *
 * class DoubleRing implements an actual doubly linked ring.
 * The ring has the concept of a "current" element.
 * When the user adds to or removes ring elements,
 * the operations are done at the "current" position.
 *
 * There are several methods below that must be implemented by the student.
 */
class DoubleRing {
	/** class Node - a member class of DoubleRing
	 *
	 * class Node represents one element in a doubly linked ring.
	 * It stores a char value and has references ('prev' and 'next')
	 * to other Nodes in a ring.
	 */
	private class Node {
		private final char m_val;
		Node prev;
		Node next;

		Node(char c) { m_val = c; prev = null; next = null; }
		public char val() { return m_val; }
	}

////
//// Class member variables
////
	// Node that tracks the 'current' i.e. currently-selected Node in this ring.
	private Node m_current;

////
//// Class methods
////
	DoubleRing () { m_current = null; }

	// Return current node's value
	char current() {
		if (m_current == null) {
			return (char) 0;
		}
		return m_current.val();
	}

	// Advance 'current' to point to the following node.
	//
	// If next() is called repeatedly, current will eventually
	// wrap all the way around the ring.
	void next() {
		if (m_current != null) {
			m_current = m_current.next;
		}
	}
	
	// add()
	//
	// Insert 'n' into this ring, after current and before current.next .
	// Leave m_current pointing to the same node as before the
	// call to add().
	//
	// Be sure to handle if the ring is empty.
	void add(Node n){
		// STUDENTS COMPLETE THIS METHOD.
		Node newNode = n;
		if(m_current != null) { // while the ring is not empty, add the new node in and setting it's prev and next's to current and current.next then update those pointers
			// taking care of the current, current prev, and current next as well as the new node
			newNode.prev = m_current;
			newNode.next =  m_current.next;
			m_current.next.prev = newNode; // previous of current.next
			m_current.next = newNode;
		}else { // the current
			m_current = newNode;
			m_current.prev = newNode;
			m_current.next = newNode;
		}	
	}

	// Helper function so we can add chars rather than Nodes to this ring.
	void add(char c) {
		add(new Node(c));
	}

	// remove()
	//
	// Remove current node.
	// Afterwards m_current should point to the successor of the old 'current'.
	//
	// Be sure to handle cases of:
	//	only 1 element left in the ring - make the ring empty
	//	ring is entirely empty - throw an IndexOutOfBoundsException
	void remove()
		throws
			IndexOutOfBoundsException
	{
		// STUDENTS COMPLETE THIS METHOD.
		if(m_current == null) {
			throw new IndexOutOfBoundsException("Ring is already empty");
		}
		if(m_current.prev == m_current && m_current.next == m_current) { // only one element in list
			m_current.prev = null;
			m_current.next = null;
			m_current = null;
		}else { // when there's more than one ring, pay attention to the currents prev and next pointing to eachother and then update current to point to successor
			m_current.prev.next = m_current.next;
			m_current.next.prev = m_current.prev;
			m_current = m_current.next;
			
		}
		
	
	}
	
	// PrintRing()
	//
	// Print all char values in the ring in order,
	// starting from current.
	// Do not keep on printing forever!
	void PrintRing() {
		// STUDENTS COMPLETE THIS METHOD.
		Node currentTemp = m_current;
		if(m_current == null) {
			return;
		}else {
			do {
				System.out.print(currentTemp.val() + " ");
				currentTemp = currentTemp.next;
			} while (currentTemp != m_current);
		}
		
		
	}
} // end class DoubleRing


/** class Ringbuf
 *
 * This class implements the program's main() function, which exercises class DoubleRing
 * and generates output.
 */
public class Ringbuf {

	public static void main(String[] args) {
		// Create an empty doubly linked ring.
		DoubleRing ring = new DoubleRing();

		// Put the letters 'a' through 'z' into the ring.
		for(char c = 'a'; c <= 'z'; c++) {
			ring.add(c);
			ring.next();
		}
		// Go through a sequence of ring operations.
		// These should not cause any errors.
		ring.add('!');
		ring.remove();

		ring.next();
		ring.remove();

		ring.next();
		ring.remove();

		ring.add('@');
		ring.next();
		ring.next();

		ring.add('#');
		ring.remove();
		ring.next();
		ring.add('$');
		ring.next();
		ring.remove();
		ring.next();
		ring.next();
		ring.next();
		ring.next();
		ring.next();
		ring.next();
		ring.remove();
		ring.next();
		ring.next();
		
		// Output the final ring.
		ring.PrintRing();
	} // end main()

} // end class Ringbuf