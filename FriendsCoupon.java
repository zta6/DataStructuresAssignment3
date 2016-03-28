//Zachary Adam Data Structures Assignment 3/////
import java.io.*;
import java.util.Scanner;
import java.util.Arrays;


public class FriendsCoupon{

	private static int numCoupons;
	private static String filename;
	private static int[][] friends;

	public static void main(String[] args){

		readCommandLineArgs(args);//read in command line arguments and assign to class variables
		readFromFile(filename);//read array from file and save in friends array

		//testIsFullSolution();     //////********uncomment to run test methods, run with command-line args: small.txt 4************************
		//stestReject();
		//testExtend();
		//testNext();

		char[] start = createArray();
	
		if(!solve(start)){
			System.out.println("No solution possible");
		}

	}

	public static char[] createArray(){//helper method to create an empty array to pass into the first call of the solve method

		char[] temp = new char[friends.length];
		for(int i = 0; i < friends.length; i++){
			temp[i] = (char) 0;
		}
		return temp;

	}
	public static void readCommandLineArgs(String[] arguments){//method to read in command line arguments
		if(arguments.length < 2 || arguments.length > 2){//check for proper number of args, print error and exit if incorrect
			System.out.println("Improper number of command line arguments, required: 2");
			System.exit(0);
		}
		else{//otherwise try to assign each argument to it's class variable
			filename = arguments[0];
			try{
				numCoupons = Integer.parseInt(arguments[1]);
			}
			catch(Exception e){
				System.out.println("Improper argument format, required: String Integer");//print error and quit
				System.exit(0);
			}
		}
	}

	 public static boolean solve(char[] partial) {/////////////////////////////////////////SOLVE METHOD///// (slightly modified from template to find only one solution
	 	)
	 	if (isFullSolution(partial)){
        	System.out.println("Solution: ");//print out solution
        	System.out.println(Arrays.toString(partial));
        	return true;
        } 
        else if (reject(partial)){
        	//System.out.println("rejecting");
        	return false;
        }

        char[] attempt = extend(partial);
        //System.out.println("extended to: " + Arrays.toString(attempt));
       
        while (attempt != null) {
            if(solve(attempt)){
            	return true;
            }
            attempt = next(attempt);
        }
       	return false;
    }

	public static boolean isFullSolution(char[] partial){////////////////////////////////////test whether or not it is a full solution///////////////////

		for(int i = 0; i < partial.length; i++){
			for(int j = 0; j < partial.length; j++){//search through coupon array
				if((int)partial[i] != 0 && friends[i][j] == 1){//if this coupon has been assigned and this spot in the friends array indicates that i and j are friends	
					if(partial[i] == partial[j]){
						return false;//check if the coupons for these two people are the same. If they are, then return false, this is not a full solution
					}
				}
			}
		}//if the solution passed through these loops without returning false, we just have to check if the solution has a coupon in every location

		int count = 0;
		for(int i = 0; i < partial.length; i++){
			if((int)partial[i] != 0){
				count++;//count the number of coupons in the array
			}
		}
		return (count==partial.length);//return true if there are as many coupons as there are elements in the array, false otherwise

	}

	public static void testIsFullSolutionUnit(char[] test){//unit test for isFullSolution

		if(isFullSolution(test)){
			System.out.println(Arrays.toString(test) + " is a full solution");
		}
		else{
			System.out.println(Arrays.toString(test) + " is NOT a full solution");
		}

	}

	public static void testIsFullSolution(){//test isFullSolution****tests work with command line arguments: small.txt 4*****
		
		System.out.println("Testing isFullSolution method");
		System.out.println();

		//is a solution
		System.out.println("Should be solutions");
		testIsFullSolutionUnit(new char[] {'A','B','C','D'});
		testIsFullSolutionUnit(new char[] {'A','B','C','A'});
		testIsFullSolutionUnit(new char[] {'D','C','B','A'});
		testIsFullSolutionUnit(new char[] {'B','C','D','A'});

		//is not a solution
		System.out.println("Should NOT be solutions");
		testIsFullSolutionUnit(new char[] {'A','A','A','A'});
		testIsFullSolutionUnit(new char[] {'A','A','B','C'});
		testIsFullSolutionUnit(new char[] {'A','B','A','D'});
		testIsFullSolutionUnit(new char[] {'A','C','A','B'});
		testIsFullSolutionUnit(new char[] {'A','B','C',0});
		testIsFullSolutionUnit(new char[] {'A','B',0,0});
		testIsFullSolutionUnit(new char[] {'A',0,0,0});
		testIsFullSolutionUnit(new char[] {'A',0,'C','D'});
		testIsFullSolutionUnit(new char[] {0,0,'A','B'});
		testIsFullSolutionUnit(new char[] {'A','B',0,'D'});	
		testIsFullSolutionUnit(new char[] {0,0,0,0});
				
	}
	
	public static void testRejectUnit(char[] test){//unit test for reject method
		
		if(reject(test)){
			System.out.println(Arrays.toString(test) + " has been rejected, it CANNOT be extended into a possible solution.");
		}
		else{
			System.out.println(Arrays.toString(test) + " has been accepted, it can be extended into a possible solution");
		}

	}
	public static void testReject(){//test for reject method *****tests work with command line arguments: small.txt 4******
		System.out.println("Testing reject method");
		System.out.println();

		//should reject
		System.out.println("Should reject: ");
		testRejectUnit(new char[] {'A','A',0,0});//'A','A' is incorrect
		testRejectUnit(new char[] {'A','B','A',0});//'1st and third coupons must be different'
		testRejectUnit(new char[] {'A','B','C','D'});//full solution, should still be rejected, because it cannot be extended
		testRejectUnit(new char[] {'D','C','B','A'});//full solution, should still reject
		testRejectUnit(new char[] {0,'A','B','C'});//cannot be extended to full solution because extend adds to right end of array
		testRejectUnit(new char[] {0,'A','A',0});//cannot be extended to a full solution because the first entry is a zero

		//shouldn't reject
		System.out.println("Should accept: ");
		testRejectUnit(new char[] {'A','B',0,0});
		testRejectUnit(new char[] {'A','B','C',0});
		testRejectUnit(new char[] {'A','B',0,0});
		testRejectUnit(new char[] {'A',0,0,0});
		testRejectUnit(new char[] {0,0,0,0});
	}

	public static boolean reject(char[] partial){////////////////////////////////////////reject method//////////////(tests whether or not a partial solution can be extended into a full one)

		for(int i = 0; i < partial.length; i++){
			for(int j = 0; j < partial.length; j++){//search through coupon array
				if((int)partial[i] != 0 && friends[i][j] == 1){//if this coupon has been assigned and this spot in the friends array indicates that i and j are friends	
					if(partial[i] == partial[j]){
						return true;//check if the coupons for these two people are the same. If they are, then return true, reject the solution
					}
				}
			}
		}



		if((int) partial[partial.length - 1] != 0){//if the last spot in the array is filled it cannot be extended to a solution, reject, return true
			return true;
		}


		int index = -1;
		for(int i = 0; i < partial.length; i++){//find index of the last placed coupon
			if((int)partial[i] != 0){
				index = i;
			}
		}

		int indexOfLastZero = partial.length+2;
		for(int i = 0; i < index; i++){//find index of the last zero space before the last entry in the array **to handle cases where there is a zero before a valid coupon placement**
			if((int)partial[i] == 0){
				indexOfLastZero = i;
			}
		}
		
		if(indexOfLastZero < index){
			return true;
		}

		return false;//if none of the above conditions were met then the solution can be extended to a full solution, dont reject, return false
	}
	
	public static void testExtend(){//test extend method, tests work with command line arguments: small.txt 4******

		System.out.println("Testing extend: ");

		char[] temp = {0,0,0,0};
		System.out.println("Extending: " + Arrays.toString(temp) + ", should return: " + "[A,  ,  ,  ]");
		System.out.println("Actually returned: " + Arrays.toString(extend(temp)));
		System.out.println();

		temp[0] = 'A';				////array is now: ['A',  ,  ,  ]
		System.out.println("Extending: " + Arrays.toString(temp) + ", should return: " + "[A, A,  ,  ]");
		System.out.println("Actually returned: " + Arrays.toString(extend(temp)));
		System.out.println();

		temp[1] = 'A';				////array is now: ['A','A',  ,  ]
		System.out.println("Extending: " + Arrays.toString(temp) + ", should return: " + "[A, A, A,  ]");
		System.out.println("Actually returned: " + Arrays.toString(extend(temp)));
		System.out.println();


		temp[2] = 'A';				////array is now: ['A','A', 'A',  ]
		System.out.println("Extending: " + Arrays.toString(temp) + ", should return: " + "[A, A, A, A]");
		System.out.println("Actually returned: " + Arrays.toString(extend(temp)));
		System.out.println();

		temp[3] = 'A';				////array is now: ['A','A', 'A','A']
		System.out.println("Extending: " + Arrays.toString(temp) + ", should return: " + "null");
		System.out.println("Actually returned: " + Arrays.toString(extend(temp)));
		System.out.println();
		
		temp[0] = 0;				////array is now: [  ,'A', 'A','A']
		System.out.println("Extending: " + Arrays.toString(temp) + ", should return: " + "null");//will return null because extend tries to append to end of array
		System.out.println("Actually returned: " + Arrays.toString(extend(temp)));
		System.out.println();
		
	}

	public static char[] extend(char[] input){////////extend method///////(adds new choice to the end of partial solution)

		int index = -1;
		char[] partial = Arrays.copyOf(input, input	.length);

		for(int i = 0; i < partial.length; i++){//find index of the last placed coupon
			if((int)partial[i] != 0){
				index = i;
			}
		}

		if(index < (partial.length - 1)){
			partial[index + 1] = 'A';//if the index of the last placed coupon was found add 'A' to the next index, as long as the last coupon wasn't at the end of the array
			return partial;
		}
		else{
			return null;//otherwise return null
		}

	}

	public static char[] next(char[] input){///////////Next method//////(change the last decision in the partial solution to it's next possible option)

		int index = 0;
		char[] partial = Arrays.copyOf(input,input.length);

		for(int i = 0; i < partial.length; i++){//find the last index in the array that has a character in it
			if((int)partial[i] != 0){
				index = i;
			}
		}

		char temp = getNextLetter(partial[index], numCoupons);//get the next choice for this letter

		if(temp == '/'){//if getNextLetter returns special character '/' then no more choices are available, return null
			return null;
		}
		else{
			partial[index] = temp;//otherwise assign the character given by getNextLetter to the spot of the last choice
			return partial; 
		}

	}

	public static void testNext(){//test for next method, *****tests work with command line arguments: small.txt 4****

		System.out.println("Testing next() method: ");
		System.out.println();

		char[] temp = {0,0,0,0};
		System.out.println("Getting next choice for: " + Arrays.toString(temp) + ", should return: [A,  ,  ,  ]");
		System.out.println("Actually returned: " + Arrays.toString(next(temp)));
		System.out.println();

		temp[0] = 'A';
		System.out.println("Getting next choice for: " + Arrays.toString(temp) + ", should return: [B,  ,  ,  ]");
		System.out.println("Actually returned: " + Arrays.toString(next(temp)));
		System.out.println();

		temp[0] = 'B';		
		System.out.println("Getting next choice for: " + Arrays.toString(temp) + ", should return: [C,  ,  ,  ]");
		System.out.println("Actually returned: " + Arrays.toString(next(temp)));
		System.out.println();


		temp[0] = 'C';		
		System.out.println("Getting next choice for: " + Arrays.toString(temp) + ", should return: [D,  ,  ,  ]");
		System.out.println("Actually returned: " + Arrays.toString(next(temp)));
		System.out.println();

		temp[0] = 'D';		
		System.out.println("Getting next choice for: " + Arrays.toString(temp) + ", should return: null");
		System.out.println("Actually returned: " + Arrays.toString(next(temp)));
		System.out.println();
			
		temp[1] = 'A';
		temp[2] = 'B';
		System.out.println("Getting next choice for: " + Arrays.toString(temp) + ", should return: [D, A, C,  ]");
		System.out.println("Actually returned: " + Arrays.toString(next(temp)));
		System.out.println();

		temp[3] = 'B';
		System.out.println("Getting next choice for: " + Arrays.toString(temp) + ", should return: [D, A, B, C]");
		System.out.println("Actually returned: " + Arrays.toString(next(temp)));
		System.out.println();		

		temp[3] = 'D';
		System.out.println("Getting next choice for: " + Arrays.toString(temp) + ", should return: null");
		System.out.println("Actually returned: " + Arrays.toString(next(temp)));
		System.out.println();		
	
		temp[3] = 'F'; //letter mysteriously set to be past letter limit
		System.out.println("Getting next choice for: " + Arrays.toString(temp) + ", should return: null");
		System.out.println("Actually returned: " + Arrays.toString(next(temp)));
		System.out.println();		
			
	}
	
	public static char getNextLetter(char thisChar, int numCoupons){//helper method to get the next letter of the alphabet
		String alpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		int index = alpha.indexOf(thisChar);

		if(index == 25 || index >= (numCoupons-1)){
			return '/';
		}
		else{
			return alpha.charAt(index+1);
		}

	}

	public static void readFromFile(String filename){//method to read in the friends array from a file
		File inFile = new File(filename);
		Scanner fileRead = null;
		int size = 0, entries = 0;
		
		try{
			fileRead = new Scanner(inFile);
		}
		catch(FileNotFoundException f){
			System.out.println("This file does not exist. Please try again with a new file");
			System.exit(0);
		}

		while(fileRead.hasNextLine()){
		    size++;//increment the number of rows in the array
	    
	    	Scanner colReader = new Scanner(fileRead.nextLine());

	    		while(colReader.hasNextInt()){//count the number of entries in the entire matrix
       				colReader.nextInt();
       				++entries;
    			}
		}

		if(size*size != entries){//if rows*rows does not equal the total number of entries then the matrix must not be square. 
			System.out.println("Array is not square. Please try again with a square matrix");
			System.exit(0);
		}

		friends = new int[size][size];//create friends matrix
		Scanner newReader = null;//create a new scanner to read the values from the file

		try{
			newReader = new Scanner(inFile);
		}
		catch(FileNotFoundException fi){
			System.out.println("This file does not exist. Please try again with a new file");
			System.exit(0);
		}

		for(int i = 0; i < size; i++){//loop through the file and read all of the values in the file into friends array
			for(int j = 0; j < size; j++){
				friends[i][j] = newReader.nextInt();
			}
		}
		
		fileRead.close();

		checkDiagonal(friends);//check that the diagonal is all 0's, i.e. that no one is indicated to be friends with themselves
		checkSymmetry(friends);//check that each row is symmetrical to each column. to make sure that the friends matrix indicates an undirectional relationship
		checkOnesAndZeroes(friends);
	}

	public static void checkDiagonal(int[][] checkArray){//check the diagonal of the array to make sure it does not indicate that anyone is friends with themself
		for(int i = 0; i < checkArray.length; i++){
			if(checkArray[i][i] != 0){
				System.out.println("This array indicates that one of the users is friends with his/herself.");
				System.out.println("Please try again with a properly formatted array");
				System.exit(0);
			}
		}
	}

	public static void checkSymmetry(int[][] checkArray){//check to make sure friend-relationship is undirectional
		for(int i = 0; i < checkArray.length; i ++){
			for(int j = 0; j < checkArray[i].length; j++){
				if(checkArray[i][j] != checkArray[j][i]){
					System.out.println("This matrix is not symmetrical, i.e. some users are friends with");
					System.out.println("people who aren't friends with them. ");
					System.out.println("Please try again with a symmetrical array");
					System.exit(0);
				}
			}
		}
	}

	public static void checkOnesAndZeroes(int[][] checkArray){//check to make sure the input array contains only ones and zeroes. 
		for(int i = 0; i < checkArray.length; i ++){
			for(int j = 0; j < checkArray[i].length; j++){
				if(checkArray[i][j] != 1 && checkArray[i][j] != 0){//if an entry is not a one or a zero, print an error message and end program
					System.out.println("This matrix contains values other than '1' and '0'.");
					System.out.println("Please try again with an array that only contains '1's and 0's");
					System.exit(0);
				}
			}
		}		
	}
}
