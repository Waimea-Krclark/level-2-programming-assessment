# Plan for Testing the Program

The test plan lays out the actions and data I will use to test the functionality of my program.

Terminology:

- **VALID** data values are those that the program expects
- **BOUNDARY** data values are at the limits of the valid range
- **INVALID** data values are those that the program should reject

---

## Screen building

I am going to test the screen setup in the beginning, to see if it dynamically creates a screen at the correct resolution, with it being a 2D array so i can easily access and edit any part of the screen

### Test Data To Use

The data I am using is the Constants for the screen size, SCREENHEIGHT and SCREENWIDTH, these will determine the resolution of the screen. A 2D array with of the actual screen will be used as well to actually build the play area in. The screen setup function needs to be run to build the screen.
I will try different values for the resolution to see if it generates correctly at any size.

### Expected Test Result

I expect my program to build the screen dynamically based on the parameters given, and it should also not attempt to place anything out od bounds of the array, with all indexes being valid 2D coordinates so can be referenced later easily using screen[y][x].

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


