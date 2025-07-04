This project enables the identification of components of friendly polyominoids.<br/>
To make an example, run the program (according to instructions given below) with in input all polyominoids of size 4.<br/>
The program will output to separate files the various components, each containing polyominoids that are friends of each other, or friends of friends, etc.<br/>
<br/>
One polyominoid is defined to be the friend of another if the first can be transformed into the second by rotating, either 90 or 180 degrees, a subset of its squares about a hinge. NB - tearing is allowed!<br/>
<br/>
Polyominoids of sizes 1 through 8 are available in 8 txt files that can be found in the archive: data/input/polyominoids1-8.zip.<br/>
<br/>
The polyominoid text files are in the format defined in the OEIS sequence for polycubes: https://oeis.org/A365139.<br>
With respect to that definition:<br/>
1. The files are not correctly ordered.<br/>
2. Each line defines an unconnected polycube. The corresponding polyominoid may be deduced according to the following rules with respect to its coordinates:<br/>
x even, y even, z odd - horizontal square, parallel to z = 0<br/>
x odd, y even, z even - vertical square, parallel to x = 0<br/>
x even, y odd, z even - vertical square, parallel to y = 0<br/>
<br/>
To use the programs:<br/>
1. Compile the java source (there are more classes than necessary in the folder, that may be used in a future version). <br/>
2. Run the friendship finder program: java -jar "dist\PolyominoidFriendships.jar" stdinToRep repToForm:form=Polyominoid polyominoidtopology ominoid4.txt<br/> This will create in the current folder a file topology-4-n.txt, for n = 1 to 5, one for each component of the size 4 polyominoids.<br/>
3. Count the number of lines in any file with a line counting utility of your OS (like "wc -l"), or run java -jar "dist\PolyominoidFriendships.jar" stdinToRep  repToForm:form=Polyominoid ocount stdout topology-4-1.txt<br/>
4. Obtain an illustration of the first few of each component: java -jar "dist\PolyominoidFriendships.jar" stdinToRep lines:from=1,to=8 repToForm:form=Polyominoid graphics:horiz=4,prefix=ttopology4-1 topology-4-1.txt<br/>This will create one or more png files containing illustrations of the polyominoids in that component.<br/>
<br/>
Examples of output files may be found in the folder data/output.<br/>
Examples of illustrations may be found in data/png.<br/>

