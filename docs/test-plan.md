# Plan for Testing the Program

The test plan lays out the actions and data I will use to test the functionality of my program.

Terminology:

- **VALID** data values are those that the program expects
- **BOUNDARY** data values are at the limits of the valid range
- **INVALID** data values are those that the program should reject

---

## Maze Generation Testing

I am going to test the maze generation to see the reliability of my system for procedurally generating a maze that the player can navigate and reach the end.

To do this test I will run my game 10 times to see how well the maze generation algorithm works. 

### Test Data To Use

The data I am using for this will be the Maze generation function, along with the other functions it uses within. The 2D array variable for the Screen will be used, with the walls and floors needing to be generated in the bounds of this array. Most parts of the script are at least referenced or need to be run to build the maze, excluding on things like player movement and the win conditions.

### Expected Test Result

I expect to see a maze that looks like a typical square maze, that is solveable from the 10 runs of the script. I believe that my algorithm which isn't perfect has the capability to generate unreachable areas still, as i have seen small pockets that can't be accessed, but this is rare enough that it shouldn't be an occur, or be an extremely rare occurence. Because of how rare this is, I do not expect it to happen during my testing, and maze should generate a solvable path every time.

---

## Example Test Name

Example test description. Example test description. Example test description. Example test description. Example test description. Example test description.

### Test Data To Use

Details of test data and reasons for selection. Details of test data and reasons for selection. Details of test data and reasons for selection.

### Expected Test Result

Statement detailing what should happen. Statement detailing what should happen. Statement detailing what should happen. Statement detailing what should happen. 

---


