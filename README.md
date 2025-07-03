This project enables the identification of components of friendly polyominoids.<br/>
To make an example, run the program (according to instructions given below) with in input all polyominoids of size 4.<br/>
The program will output to separate files the various components, each containing polyominoids that are friends of each other, or friends of friends, etc.<br/>
<br/>
One polyominoid is defined to the friend of another if the first can be transformed into the second by rotating, either 90 or 180 degrees, a subset of its squares about a hinge. NB - tearing is allowed!<br/>
<br/>
Polyominoids of sizes 1 through 8 are available in 8 txt files that can be found in the archive: data/input/polyominoids1-8.zip.<br/>
<br/>
The polyominoid text files are in the format defined in the OEIS sequence for polycubes: https://oeis.org/A365139.<br>
With respect to that definition:<br/>
1. The files are not correctly ordered.<br/>
2. Each line defines an unconnected polycube. The corresponding polyominoid may be deduced according to the following rules with respect to its coordinates:<br/>
x even, y even, z odd - flat square, parallel to z = 0<br/>
x odd, y even, z even - vertical square, parallel to x = 0<br/>
x even, y odd, z even - vertical square, parallel to y = 0<br/>
<br/>
To use the programs:
1. Compile the java source (there are more classes than necessary in the folder, that may be used in a future version). 
2. Create 2 empty folders, with names "Various" and "png".
3. 

