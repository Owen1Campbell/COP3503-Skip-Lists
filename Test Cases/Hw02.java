/*=============================================================================
 * |   Assignment:  HW 02 - Building and managing a Skiplist
 * |
 * |       Author:  Owen Campbell
 * |     Language:  Java
 * |
 * |   To Compile:  javac Hw02.java
 * |
 * |   To Execute:  java Hw02 filename
 * |                     where filename is in the current directory and contains
 * |                           commands to insert, search, delete, print & quit.
 * |
 * |        Class:  COP3503 - CS II Spring 2021
 * |   Instructor:  McAlpin
 * |     Due Date:  07-21-2021
 * |
 * +=============================================================================*/

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Random;

public class Hw02 {
	
	// classes
	static class Node {
		public int key, level;
		public Node left, right, up, down;
		
		public Node(int val) {
			key = val;
			left = right = up = down = null;
			level = 0;
		}
		public Node(int val, Node prev) {
			key = val;
			left = prev;
			right = up = down = null;
			level = 0;
		}
		public Node(int val, Node prev, Node next) {
			key = val;
			left = prev;
			right = next;
			up = down = null;
			level = 0;
		}
	}

	static class SkipList {
		public Node head;
		
		public SkipList() {
			head = new Node(NEG_INF);
			head.right = new Node(POS_INF, head);
			head.down = null;
		}
	}
	
	static Random rndm = new Random();
	static final int NEG_INF = -1;
	static final int POS_INF = 700001;
	
	public static void complexityIndicator() {
		// print to STDERR: "ow376671;difficulty;hours_spent"
		// hrs: 10 @ 3:17 p.m. 2021-07-22
		System.err.println("ow376671;4.0;XX");
	}
	
	// functions
	private static Node insert(Node n, int key) {
		// iterate to farthest right node less than key, then go to next row
		while (n.down != null) {
			// find greatest value less than key at this level
			// if key value is less than the next value, iterate past it
			while (key > n.right.key) {
				n = n.right;
			}
			n = n.down;
		}
		// loop right once more at lowest level
		while (key >= n.right.key) {
			n = n.right;
			if (n.key == key)
				return n;
		}
		Node newNode = new Node(key, n, n.right);
		// adjusts position references to account for inserted node
		newNode.right.left = newNode;
		newNode.left.right = newNode;
		newNode = promote(newNode);
		
		return n;
	}
	
	private static Node promote(Node n) {
		// when heads, increase level. else exit
		int coin = Math.abs(rndm.nextInt() % 2);
		//System.out.println("Coin flip = " + coin);
		Node prevLvl = n;
		while (coin == 1) {
			
			Node newLvl = new Node(prevLvl.key);
			newLvl.level = prevLvl.level++;
			newLvl.down = prevLvl;
			prevLvl.up = newLvl;
			Node search = prevLvl.right;
			// iterate forward to find next right node with a higher level
			while (search.up == null) {
				if (search.key == POS_INF) {
					// create new node at level w/ left connected
					search.up = new Node(POS_INF, newLvl);
					search.up.down = search;
					search.up.level = newLvl.level;
					break;
				}
				search = search.right;
			}
			newLvl.right = search.up;
			search = prevLvl.left;
			// iterate backward to find left level
			while (search.up == null) {
				if (search.key == NEG_INF) {
					// create new node at level w/ right connected & left null
					search.up = new Node(NEG_INF, null, newLvl);
					search.up.down = search;
					search.up.level = newLvl.level;
					break;
				}
				search = search.left;
			}
			newLvl.left = search.up;
			prevLvl = newLvl;
			coin = Math.abs(rndm.nextInt() % 2);
			//System.out.println("Coin flip = " + coin);
		}
		return n;
	}
	
	private static Node delete(Node n, int key) {
		// gets node to be deleted
		Node toDel = getNode(n, key);
		// deletes node
		if (toDel != null) {
			erase(toDel);
			System.out.println(key + " deleted");
		}
		
		return n;
	}
	
	private static void erase(Node n) {
		if (n.down != null) {
			erase(n.down);
		}
		// erases node and changes references of adjacent nodes
		n.left.right = n.right;
		n.right.left = n.left;
		n = null;
	}
	
	private static Node getNode(Node n, int key) {
		// searches using method from insert
		while (n.down != null) {
			// find greatest value less than key at this level
			// if key value is less than the next value, iterate past it
			while (key > n.right.key) {
				n = n.right;
			}
			n = n.down;
		}
		// loop right once more at lowest level
		while (key >= n.right.key) {
			n = n.right;
			if (n.key == key)
				return n;
		}
		if (n.down == null && n.right.key > key) {
			System.out.println(key + " integer not found - delete not successful");
			return null;
		}
		return null;
	}

	private static void search(Node n, int key) {
		// success case
		if (n.key == key) {
			System.out.println(key + " found");
			return;
		}
		// fail when lowest level passes node position
		if (n.down == null && n.right.key > key) {
			System.out.println(key + " NOT FOUND");
			return;
		}
		// check current level
		if (n.right.key <= key) {
			search(n.right, key);
		}
		// move down level
		if (n.right.key > key && n.down != null) {
			search(n.down, key);
		}
	}
	
	private static void printAll(Node n) {
		while (n.down != null)
			n = n.down;
		System.out.println("---infinity");
		n = n.right;
		while (n.key != POS_INF) {
			System.out.printf(" %d; ", n.key);
			if (n.up != null) {
				Node up = n.up;
				System.out.printf(" %d; ", up.key);
				// iterate through upper levels
				while (up.up != null) {
					System.out.printf(" %d; ", up.key);
					up = up.up;
				}
			}
			// new line for formatting
			System.out.println();
			n = n.right;
		}
		System.out.println("+++infinity");
		System.out.println("---End of Skip List---");
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		// get file input
		File fInput = new File(args[0]);
		Scanner scnr = new Scanner(fInput);
		
		// get first line
		String cmd = scnr.nextLine();
		char act = cmd.charAt(0);
		
		// build skiplist
		SkipList list = new SkipList();

		System.out.println("For the input file named " + args[0]);
		
		// determine seed
		long seed = 42;
		// if array has an additional operator, check if it is r
		if (args.length > 1) {
			if (args[1].charAt(0) == 'r' || args[1].charAt(0) == 'R') {
				seed = System.currentTimeMillis();
				System.out.println("With the RNG seeded " + seed + ",");
			}
			else {
				seed = 42;
				System.out.println("With the RNG unseeded,");
			}
		}
		else {
			seed = 42;
			System.out.println("With the RNG unseeded,");
		}
		
		rndm.setSeed(seed);
		
		while (act != 'q' && scnr.hasNext()) {
			switch (act) {
			case 'i':
				int toInsert;
				// get int to insert from string; check for numeric input
				try {
					toInsert = Integer.parseInt(cmd.substring(2));
				} 
				catch(Exception e) {
					System.out.println("missing numeric parameter");
					break;
				}
				insert(list.head, toInsert);
				while (list.head.up != null)
					list.head = list.head.up;
				break;
			case 'd':
				int toDelete;
				// get int to delete from string
				try {
					toDelete = Integer.parseInt(cmd.substring(2));
				}
				catch(Exception e) {
					System.out.println("missing numeric parameter");
					break;
				}
				list.head = delete(list.head, toDelete);
				break;
			case 's':
				int toSearch;
				// get int to search from string
				try {
					toSearch = Integer.parseInt(cmd.substring(2));
				} 
				catch(Exception e) {
					System.out.println("missing numeric parameter");
					break;
				}
				search(list.head, toSearch);
				break;
			case 'p':
				System.out.println("the current Skip List is shown below: ");
				if (list.head != null)
					printAll(list.head);
				else
					System.out.print("skip list is empty");
				break;
			default:
				System.out.println("invalid command");
			}
			cmd = scnr.nextLine();
			act = cmd.charAt(0);
		}
		
		// get command
		
		// seed random
			// if (no seed)
			// seed = System.currentTimeMillis();
			// promotion on heads
		
		scnr.close();
	}

}
