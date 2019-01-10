// Yiran Ni
// 8/17/2017
// CSE 143
// TA: Jinying Chen
// Assignment #7
//
// The HuffmanCode class compresses a text file to a smaller text file with numbers 
// only 1 and 0. It can also decompress the compressed file back to the original file.

import java.util.*;
import java.io.*;

public class HuffmanCode {
   private HuffmanNode overallRoot;
   
   // construct leaves according to the different types (with or without frequency,
   // value, children). It is comparable to other leaves with each frequency.
   private class HuffmanNode implements Comparable<HuffmanNode>{
      public int freq;
      public int character;
      public HuffmanNode left;
      public HuffmanNode right;
      
      // empty leaf
      public HuffmanNode() {
         this(0, 0, null, null);
      }
      
      // leaf with frequencies of each character, value of the character
      public HuffmanNode(int freq, int character) {
         this(freq, character, null, null);
      }
      
      // leaf stores the frequencies of each character and two children
      public HuffmanNode(int freq, HuffmanNode left, HuffmanNode right) {
         this.freq = freq;
         this.left = left;
         this.right = right;
      }
      
      // leaf stores the frequencies of each character, value of the character, 
      // and two children
      public HuffmanNode(int freq, int character, HuffmanNode left, HuffmanNode right) {
         this.freq = freq;
         this.character = character;
         this.left = left;
         this.right = right;
      }
      
      public int compareTo(HuffmanNode other) {
         return this.freq - other.freq;
      }
   }
   
   // post: initialize a new HuffmanCode from the given array of frequencies,
   //       with the help of the priority queue.
   public HuffmanCode(int[] frequencies) {
      Queue<HuffmanNode> trees = new PriorityQueue<HuffmanNode>();
      for(int i = 0; i < frequencies.length; i++) {
         if(frequencies[i] > 0) {
            trees.add(new HuffmanNode(frequencies[i], i));
         }   
      } 
      overallRoot = frequenciesHelper( trees);
   }
   
   // post: combine all the trees in the priority queue into one tree.
   //       return the final tree.   
   private HuffmanNode frequenciesHelper(Queue<HuffmanNode> trees) {
      while(trees.size() > 1) {
         HuffmanNode tree1 = trees.remove();
         HuffmanNode tree2 = trees.remove();
         HuffmanNode newNode = new HuffmanNode(tree1.freq + tree2.freq, tree1, tree2);
         trees.add(newNode);

      } 
      return trees.remove(); 
   }
   
   // post: initialize a new HuffmanNode from a given code file that previously
   // created as input. The odd number of lines are asciiValues and the odd 
   // number of lines are code.
   public HuffmanCode(Scanner input) {
      this.overallRoot = null;
      while(input.hasNextLine()) {
         int asciiValue = Integer.parseInt(input.nextLine());
         String code = input.nextLine();
         this.overallRoot = ScannerHelper(this.overallRoot, asciiValue, code, 0);
      }
   }
   
   // post: add a root to the left if the number at the certain index of the code is 0;
   //       add a root to the right if the number at the certain index of the code is 1.
   //       return the root;
   public HuffmanNode ScannerHelper(HuffmanNode root, int asciiValue, String code, int index) {
      if(index == code.length()) {
         return new HuffmanNode(0, asciiValue);
      }else {
         if(root == null) {
            root = new HuffmanNode(0, null, null);
         }
         if(code.charAt(index) == '0') {
            root.left = ScannerHelper(root.left, asciiValue, code, index + 1);
         } else {
            root.right = ScannerHelper(root.right, asciiValue, code, index + 1);
         }
      }
      return root;
   }
   
   // post: store the current huffman code to the given output with 
   //       certain format
   public void save(PrintStream output) {
      save(output, overallRoot, "");
   }
   
   // pre: the current huffman code root is not null
   // post: print the result into the given output with the odd numbers of lines 
   //       are the asciiValue of the character, and even numbers of lines are 
   //       code (e.g. 011).
   private void save(PrintStream output, HuffmanNode root, String code) {
      if(root != null) {
         if(root.left == null && root.right == null) {
            output.println(root.character);
            output.println(code);
         }else {
            save(output, root.left, code + "0");
            save(output, root.right, code + "1");
         }
      }
   }
   
   // post: read individual bits from the input, write the corresponding 
   //       characters to the output.
   public void translate(BitInputStream input, PrintStream output) {
     while(input.hasNextBit()) {
         HuffmanNode current = this.overallRoot;   
         translate(input, output, current);
      }
   }  
   
   // pre: the current huffman code root is not null
   // post: write the character to the output
   private void translate(BitInputStream input, PrintStream output, HuffmanNode root) {
      if(root != null) {                
         while(!(root.left == null && root.right == null)) {
            int temp = input.nextBit();                    
            if(temp == 0) {
               root = root.left;
               } else {
               root = root.right;
               } 
         }  
         output.write(root.character);
      }                        
   }
}