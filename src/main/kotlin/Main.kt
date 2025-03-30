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
import com.varabyte.kotter.foundation.collections.LiveList
import com.varabyte.kotter.foundation.input.*
import com.varabyte.kotter.foundation.text.*
import com.varabyte.kotter.foundation.collections.liveListOf
import com.varabyte.kotter.foundation.timer.*
import com.varabyte.kotter.runtime.Session
import kotlin.concurrent.timer
import kotlin.random.Random

//Constant variable declarations
const val SCREENHEIGHT = 30
const val SCREENWIDTH = 30
const val EMPTYTILE = "E "
const val FLOOR = "  "
const val PLAYER = "O "
const val DOOR = "D "
const val WALL = "# "
// Global variables
var playerY = 0
var playerX = 0

//Main Function
fun main() = session {
    //Live Lists are a Kotter Collection that allows for the list to be updated without having to rerender the terminal
    //Live list of a Live list to create a 2D array that can be used with array[Y][X]
    val screen = liveListOf<LiveList<String>>()

    //Builds the screen
    buildScreen(screen=screen)
    //Builds the maze and adds the player to the bottom and the door to the top
    placePlayer(screen=screen)
    generateMaze(screen=screen)
    var (doorY,doorX) = placeDoor(screen=screen)

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
        //Run until signal creates a looped script that will run until the Signal() function is called, similar to a While loop
        var reachedDoor = false
        //Movement script
        onKeyPressed {
            if (!reachedDoor){
                when(key) {
                    Keys.W -> reachedDoor = movePlayer(screen = screen, moveX = playerX, moveY = playerY - 1)
                    Keys.S -> reachedDoor = movePlayer(screen = screen, moveX = playerX, moveY = playerY + 1)
                    Keys.A -> reachedDoor = movePlayer(screen = screen, moveX = playerX - 1, moveY = playerY)
                    Keys.D -> reachedDoor = movePlayer(screen = screen, moveX = playerX + 1, moveY = playerY)
                }
                if (reachedDoor){
                    signal()
                }
            }
        }
    }
}

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


fun Session.generateMaze(screen:LiveList<LiveList<String>> ) {
    /**
     * Maze Generation Script
     */

    //NEEDS TO BE REWROTE
    //Use a cell visiting system
    //Goes to empty neighbouring cells, if none backtracks until there is none
    //If no empty cells left then maze it generated
    var mazeBuilt = false
    var randomStart = false
    var mazeHeaderY = playerY
    var mazeHeaderX = playerX
    while (screen[1].contains(EMPTYTILE)) {
        if (randomStart) {
            var foundTile = false
            while (!foundTile) {
                for (screenY in screen) {
                    for (screenX in screenY) {
                        if (screenX == EMPTYTILE) {
                            mazeHeaderY = screen.indexOf(screenY)
                            mazeHeaderX = screen[mazeHeaderY].indexOf(screenX)
                            println("$mazeHeaderX , $mazeHeaderY")
                            screen[mazeHeaderY][mazeHeaderX] = FLOOR
                            foundTile = true
                            break
                        }
                    }
                    if (foundTile) {
                        break
                    }
                    println("Running Through")
                }
            }
        }
        println("Attempting Path")
        var failCounter = 0
        while (true) {

            val pickDirection = Random.nextInt(1, 5)
            var attemptedMoveY = mazeHeaderY
            var attemptedMoveX = mazeHeaderX

            var wallPlacement = 0
            //println()
            //println()
            for (screenY in screen) {
                for (screenX in screenY) {
                    //print(screenX)
                }
                //println()
            }

            when (pickDirection) {
                1 -> {
                    attemptedMoveY++
                    wallPlacement = 0
                }

                2 -> {
                    attemptedMoveY--
                    wallPlacement = 0
                }

                3 -> {
                    attemptedMoveX++
                    wallPlacement = 1
                }

                4 -> {
                    attemptedMoveX--
                    wallPlacement = 1
                }

            }
            if (attemptedMoveY in 1..SCREENHEIGHT && attemptedMoveX in 1..SCREENWIDTH) {

                if (screen[attemptedMoveY][attemptedMoveX] == EMPTYTILE) {
                    mazeHeaderY = attemptedMoveY
                    mazeHeaderX = attemptedMoveX
                    screen[attemptedMoveY][attemptedMoveX] = FLOOR
                    if (wallPlacement == 1) {
                        try {
                            if (screen[attemptedMoveY][attemptedMoveX + 1] == EMPTYTILE) {
                                screen[attemptedMoveY][attemptedMoveX + 1] = WALL
                            }
                        } catch (e: Exception) {
                            break
                        }
                    } else {
                        try {
                            if (screen[attemptedMoveY + 1][attemptedMoveX] == EMPTYTILE) {
                                screen[attemptedMoveY + 1][attemptedMoveX] = WALL
                            }

                        } catch (e: Exception) {
                            break
                        }
                    }

                } else {
                    failCounter++
                }

                if (failCounter == 1000) {
                    println("Failed")
                    randomStart = true
                    break
                }
            }

        }

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
        if (screen[moveY][moveX] == FLOOR) {
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