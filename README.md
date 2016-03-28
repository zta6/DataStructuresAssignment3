# DataStructuresAssignment3
This code was written by Zachary Adam in February 2016 for Data Structures at the University of Pittsburgh. It uses recursive 
backtracking to find an assignment of coupons to a group of friends in a social media. The intent is to find an assignment of a given 
number of coupons that does not give two friends the same coupon. The friend group is represented by a matrix of 1's and 0's stored in 
a text file. Each row/column represents a person, each entry in the row indicates whether or not they are friends with another person. A
'1' indicates that the two people are friends, and a '0' indicates that they are not. A sample text file is included in the repository. 
The program takes the command line arguments "file.txt MaximumNumberOfCoupons"
