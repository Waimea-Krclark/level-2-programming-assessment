/**
 * =====================================================================
 * Programming Project for NCEA Level 2, Standard 91896
 * ---------------------------------------------------------------------
 * Project Name:   MAZE GAME
 * Project Author: Kieran Clark
 * GitHub Repo:    https://github.com/Waimea-Krclark/level-2-programming-assessment
 * ---------------------------------------------------------------------
 * Notes:
 * Procedurally generated top down maze game, player is controlled with WASD in a terminal GUI
 * =====================================================================
 */

package org.example

//Imports for the Kotter Library
import com.varabyte.kotter.foundation.*
import com.varabyte.kotter.foundation.anim.renderAnimOf
import com.varabyte.kotter.foundation.anim.textAnimOf
import com.varabyte.kotter.foundation.collections.LiveList
import com.varabyte.kotter.foundation.input.*
import com.varabyte.kotter.foundation.text.*
import com.varabyte.kotter.foundation.collections.liveListOf
import com.varabyte.kotter.foundation.timer.*
import com.varabyte.kotter.runtime.Session
import kotlin.random.Random
import kotlin.system.exitProcess
import kotlin.time.Duration.Companion.milliseconds

//Constant variable declarations
const val SCREENHEIGHT = 30
const val SCREENWIDTH = 30
const val EMPTYTILE = "E "
const val FLOOR = "  "
const val BRANCH = "  "
const val PLAYER = "O "
const val DOOR = "D "
const val WALL = "# "
// Global variables
var playerY = 0
var playerX = 0
var mazeBuilt = false

//Main body function - runs setup and game loop
fun main() = session {
    /**
     * This project uses Kotter, which is a kotlin terminal gui library.
     * This allows me to do more with my code like consuming input, live updating, animations and colour.
     *
     * Main function builds the screen, then the maze, and adds the player and the door before running the main game loop for player movement
     */
    //Live Lists are a Kotter Collection that allows for the list to be updated without having to rerender the terminal
    //Live list of a Live list to create a 2D array that can be used with array[Y][X]
    val screen = liveListOf<LiveList<String>>()
    //Creates an animation of a list, cycling through
    val buildAnim = textAnimOf(listOf(".  ", ".. ", "..." ,"   "),300.milliseconds)

    section {
        //Displays if maze is not built
        if(!mazeBuilt){
            bold{yellow{textLine("Building Maze$buildAnim Please wait")}}
            textLine("===========================================")
            bold{textLine("How to Play:")}
            green{textLine("W: Move Up, S: Move Down, A: Move Left, D: Move Right")}
            green{textLine("Reach the door (D) to win!")}
        }

    }.runUntilSignal { //Builds the screen
        buildScreen(screen=screen)
        //Builds the maze and adds the player to the bottom and the door to the top
        placePlayer(screen=screen)
        generateMaze(screen=screen)
        placeDoor(screen=screen)
        mazeBuilt = true
        signal()
    }

    section {
        //for every tile in every row, prints the tile, and colours specific tiles (i.e. cyan tile for player)
        for (screenY in screen){
            for (screenX in screenY){
                when (screenX) {
                    PLAYER -> cyan{ text(screenX) }
                    DOOR -> green{ text(screenX) }
                    else -> magenta{ text(screenX) }
                }
            }
            //New Line to loop
            textLine()
        }
    }.runUntilSignal {
        rerender()
        //Run until signal creates a looped script that will run until the Signal() function is called, similar to a While loop
        var reachedDoor = false
        //Movement script
        onKeyPressed {
            //On any of these keys pressed, will run the move function which will attempt to move the player
            if (!reachedDoor){
                when(key) {
                    Keys.W -> reachedDoor = movePlayer(screen = screen, moveX = playerX, moveY = playerY - 1)
                    Keys.S -> reachedDoor = movePlayer(screen = screen, moveX = playerX, moveY = playerY + 1)
                    Keys.A -> reachedDoor = movePlayer(screen = screen, moveX = playerX - 1, moveY = playerY)
                    Keys.D -> reachedDoor = movePlayer(screen = screen, moveX = playerX + 1, moveY = playerY)
                }
                //Runs when the door is reached by the player, triggering the win condition
                if (reachedDoor){
                    //Creates a stepped timer for each row of the screen, making a wipe effect to remove the screen
                    for (i in 0..SCREENHEIGHT+2){
                        addTimer(((i+1)*50).milliseconds){
                            if (screen.isNotEmpty()){
                                screen.removeLast()
                            }
                            else{
                                screen.clear()
                                signal() // Signals the end, stopping the loop and allowing the program to move to the next section
                            }
                        }
                    }
                }
            }
        }
    }
    //Setup text animation and colour animation
    val winAnim = textAnimOf(listOf("!~~ WIN ~~!", "~!~ WIN ~!~", "~~! WIN !~~", "~!~ WIN ~!~"),100.milliseconds)
    val colorAnim = renderAnimOf(Color.entries.size, 400.milliseconds) { i ->
        if (Color.entries[i] != Color.BLACK) {
            color(Color.entries[i])
        }
    }
    //Win condition section
    section {
        colorAnim(this)
        textLine("===================")
        bold{textLine("### $winAnim ###")}
        textLine("===================")
        textLine()
        white{textLine("You have escaped the maze!")}
        textLine()
        white{textLine("----------------")}
        textLine()
        white{textLine("Press any key to Exit")}
    //On key press, signals the terminal to stop, and exits the program
    }.runUntilSignal{
        rerender()
        onKeyPressed {
            signal()
        }
    }
    //Exits the programs
    exitProcess(0)
}

//Creates the screen at the determined resolution
fun Session.buildScreen(screen: LiveList<LiveList<String>>){
    //Create top boundary
    screen.add(liveListOf(WALL + WALL.repeat(SCREENWIDTH) + WALL))
    var xCount = 1
    repeat(SCREENHEIGHT){
        //For each row, creates the edge wall and fills in the interior with empty tiles
        screen.add(liveListOf(WALL))
        repeat(SCREENWIDTH){
            screen[xCount].add(EMPTYTILE)
        }
        screen[xCount].add(WALL)
        xCount++
    }
    //Create bottom boundary
    screen.add(liveListOf(WALL + WALL.repeat(SCREENWIDTH) + WALL) )
}

fun generateMaze(screen:LiveList<LiveList<String>> ) {
    /**
     *  ----- Maze Generation Script -----
     * Uses a system based off the recursive backtracking maze generation algorithm,
     * but is not the same, using different means to achieve a navigable maze.
     */
    var mazeBuilt = false
    var randomStart = false
    var mazeHeaderY = playerY
    var mazeHeaderX = playerX
    val tileList = mutableListOf(Pair(playerY, playerX))

    //Runs repeatedly until maze is fully generated
    while (!mazeBuilt) {
        //Checks for empty characters
        var foundEmpty = false
        for (screenY in screen) {
            for (screenX in screenY) {
                if (screenX.contains(EMPTYTILE)) {
                    foundEmpty = true
                }
            }
        }
        if (!foundEmpty) {
            //If none found, maze is finished building
            mazeBuilt = true
        }
        //Building Maze branch from random spot
        if (randomStart) {
            val randomEmptyTile = mutableListOf<Pair<Int,Int>>()
            for (screenY in 1..SCREENHEIGHT) {
                for (screenX in 1..SCREENWIDTH) {
                    if (screen[screenY][screenX] == EMPTYTILE) {
                        //Adds all potential start tiles to a list
                        randomEmptyTile.add(Pair(screenY, screenX))
                    }
                }
            }
            //Adds a random tile from the list to the main list of usable tiles
            val randomSelectTile = Random.nextInt(tileList.size)
            if (randomSelectTile in randomEmptyTile.indices){
                tileList.add(randomEmptyTile[randomSelectTile])
            }

            //Picks a random tile from the usable list, sets the maze drawing to here and creates a branch
            val potentialTile = Random.nextInt(tileList.size)
            if (potentialTile in tileList.indices){
                val randomTile = tileList[Random.nextInt(tileList.size)]
                tileList.remove(randomTile)
                mazeHeaderY = randomTile.first
                mazeHeaderX = randomTile.second
                screen[mazeHeaderY][mazeHeaderX] = BRANCH
            }
        }
        //Resets fail counter
        var failCounter = 0
        while (true) {
            //Picks a random directions and sets maze building position
            val pickDirection = Random.nextInt(1, 5)
            var attemptedMoveY = mazeHeaderY
            var attemptedMoveX = mazeHeaderX

            //Random direction
            when (pickDirection) {
                1 -> attemptedMoveY++
                2 -> attemptedMoveY--
                3 -> attemptedMoveX++
                4 -> attemptedMoveX--
            }
            //If the suggested move is a valid coordinate
            if (attemptedMoveY in 1..SCREENHEIGHT && attemptedMoveX in 1..SCREENWIDTH) {
                if (screen[attemptedMoveY][attemptedMoveX] == EMPTYTILE) {
                    //Sets maze header to new position, turns it into a floor tile, runs the build wall function
                    mazeHeaderY = attemptedMoveY
                    mazeHeaderX = attemptedMoveX
                    screen[attemptedMoveY][attemptedMoveX] = FLOOR
                    buildWall(screen, pickDirection, attemptedMoveY, attemptedMoveX, tileList)
                } else {
                    //If not an empty tile, then add to fail
                    failCounter++
                }
                //If fail threshold reached, reset
                if (failCounter == 1000) {
                    randomStart = true
                    break
                }
            }
        }
        //Draw player
        screen[playerY][playerX] = PLAYER
    }
}


fun buildWall(screen: LiveList<LiveList<String>>,direction:Int, posY: Int, posX: Int, tileList: MutableList<Pair<Int,Int>>) {
    /**
     * Determines how a wall should be placed based off parameters like position and direction.
     */
    // Random wall types, larger bias toward building hallways
    when (Random.nextInt(1,8)) {
        1, 2, 3, 4, 5 -> {
            // Hallway. The values in the pair determine the direction of the hallway
            if (direction == 1) {
                doubleWall(screen, Pair(posY, posX), Pair(0, 1))
            } else {
                doubleWall(screen, Pair(posY, posX), Pair(1, 0))
            }
        }
        6 -> {
            // Branching hallway type 1
            if (direction == 1) {
                singleWall(screen, Pair(posY, posX), Pair(0, 1), tileList)
            } else {
                singleWall(screen, Pair(posY, posX), Pair(1, 0), tileList)
            }
        }
        7 -> {
            // Branching hallway type 2 (Opposite side)
            if (direction == 1) {
                singleWall(screen, Pair(posY, posX), Pair(0, -1), tileList)
            } else {
                singleWall(screen, Pair(posY, posX), Pair(-1, 0), tileList)
            }
        }
    }
}

fun checkCoordinate(pos:Pair<Int,Int>):Boolean{
    //Checks coordinates before attempting to use them to stop errors
    val (posY, posX) = pos
    // If the provided coordinates are valid indexes, then return trues
    return posY in 1..SCREENHEIGHT && posX in 1..SCREENWIDTH
}

fun doubleWall(screen: LiveList<LiveList<String>>, pos:Pair<Int,Int>, side:Pair<Int,Int>){
    //Draws a wall in a "#  #" format (Hallways)
    val (posY, posX) = pos
    val (sideY, sideX) = side
    //Checks if the coordinates are valid
    if (checkCoordinate(Pair(posY + sideY, posX + sideX)) && checkCoordinate(Pair(posY - sideY, posX - sideX))) {
        //Draws on either side if empty
        if (screen[posY + sideY][posX + sideX] == EMPTYTILE) {
            screen[posY + sideY][posX + sideX] = WALL
        }
        if (screen[posY - sideY][posX - sideX] == EMPTYTILE) {
            screen[posY - sideY][posX - sideX] = WALL
        }
    }
}

fun singleWall(screen: LiveList<LiveList<String>>, pos:Pair<Int,Int>, side:Pair<Int,Int>, tileList: MutableList<Pair<Int, Int>>) {
    //Draws a branching hallway with one side open for a new path "*  #"
    val (posY, posX) = pos
    val (sideY, sideX) = side
    //Draws a wall on one side
    if (checkCoordinate(Pair(posY + sideY, posX + sideX))) {
        if (screen[posY + sideY][posX + sideX] == EMPTYTILE) {
            screen[posY + sideY][posX + sideX] = WALL
        }
    }
    //Adds a branch point on the other
    if (checkCoordinate(Pair(posY - sideY, posX - sideX))) {
        tileList.add(Pair(posY - sideY, posX - sideX))
    }
}

fun placePlayer(screen: LiveList<LiveList<String>>){
    //Sets the player's Y coordinate to the bottom of the screen and the X to a random spot on the screen
    playerX = Random.nextInt(1, SCREENWIDTH+1)
    playerY = SCREENHEIGHT
    //Draws the player to the Coordinates
    screen[playerY][playerX] = PLAYER
}

fun placeDoor(screen: LiveList<LiveList<String>>):Pair<Int,Int>{
    //Sets the door's Y coordinate to the top of the screen and the X to a random spot on the screen
    val doorX = Random.nextInt(1, SCREENWIDTH+1)
    val doorY = 1
    //Draws door to screen and returns its coordinates
    screen[doorY][doorX] = DOOR
    return Pair(doorY,doorX)
}

fun movePlayer(screen: LiveList<LiveList<String>>, moveX:Int, moveY:Int):Boolean {
    if (moveY in 1..SCREENHEIGHT) {
        //If the passed coordinates are an empty tile, sets the player there and redraws the old tile
        if (screen[moveY][moveX] == FLOOR || screen[moveY][moveX] == BRANCH) {
            screen[playerY][playerX] = FLOOR
            playerY = moveY
            playerX = moveX
        } else if (screen[moveY][moveX] == DOOR) {
            //Clears the player from the screen and returns true, triggering the win condition
            screen[playerY][playerX] = FLOOR
            return true
        }
    }
    //Draws to player to its coordinates
    screen[playerY][playerX] = PLAYER
    return false
}
