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

Running the setup for the game multiple times to see how it handles different sizes, resolutions and shapes.

### Expected Test Result

I expect my program to build the screen dynamically based on the parameters given, and it should also not attempt to place anything out od bounds of the array, with all indexes being valid 2D coordinates so can be referenced later easily using screen[y][x].

---

## Maze Generation Testing

I am going to test the maze generation to see the reliability of my system for procedurally generating a maze that the player can navigate and reach the end.

To do this test I will run my game 10 times to see how well the maze generation algorithm works. 

### Test Data To Use

Running the game 10 times to see how it generates the maze and how well it does so.

### Expected Test Result

I expect to see a maze that looks like a typical square maze, that is solveable from the 10 runs of the script. I believe that my algorithm which isn't perfect has the capability to generate unreachable areas still, as i have seen small pockets that can't be accessed, but this is rare enough that it shouldn't be an occur, or be an extremely rare occurence. Because of how rare this is, I do not expect it to happen during my testing, and maze should generate a solvable path every time.

---

## Boundary Testing

I am going to test the maze generator, player actions and other placements to see how my program handles out of bounds data.

## Test Data To Use

Running the program multiple times and trying to move the player out of bounds in different ways, trying this on all 4 of the boundaries.

## Expected Test Result

I expect that my program will handle these well, not allowing actions that would result in out of bounds data, which would be invalid and throw an error.

---

## Input Error Checking

I am going to test user inputs that are not valid to see how my program handles them.

## Test Data To Use

Trying to enter inputs that are not parts of the game, and try to do invalid actions like moving through walls to see how it is handled.

## Expected Test Result

I am expecting to not encounter any errors with user inputs and think that my program will handle them correctly, ignoring bad inputs.

---

## Reaching Door (Win Condition)

I am going to test the trigger on the player reaching the exit door which is the win condition of the game.

### Test Data To Use

I will beat the game multiple times to ensure that the win condition always triggers correctly.

### Expected Test Result

The door should always generate, and upon reaching it, the player will disappear, the maze will fade and the win loop will be triggered, with an animation that then allows the player to exit on a button press.

---


