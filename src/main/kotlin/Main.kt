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
const val BRANCH = "  "
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
    var mazeBuilt = false
    var randomStart = false
    var mazeHeaderY = playerY
    var mazeHeaderX = playerX
    val tileList = mutableListOf<Pair<Int, Int>>(Pair(playerY, playerX))

    while (!mazeBuilt) {
        var foundEmpty = false
        for (screenY in screen) {
            for (screenX in screenY) {
                if (screenX.contains(EMPTYTILE)) {
                    foundEmpty = true
                }
            }
        }
        if (!foundEmpty) {
            mazeBuilt = true
        }
        if (randomStart) {
            val randomEmptyTile = mutableListOf<Pair<Int,Int>>()
            for (screenY in 1..SCREENHEIGHT) {
                for (screenX in 1..SCREENWIDTH) {
                    if (screen[screenY][screenX] == EMPTYTILE) {
                        randomEmptyTile.add(Pair(screenY, screenX))

                    }
                }
            }
            try {
                tileList.add(randomEmptyTile[Random.nextInt(tileList.size)])
            }catch (e:Exception){}
            try {
                val randomTile = tileList[Random.nextInt(tileList.size)]
                tileList.remove(randomTile)
                mazeHeaderY = randomTile.first
                mazeHeaderX = randomTile.second
                screen[mazeHeaderY][mazeHeaderX] = BRANCH
            } catch (e: Exception) { }
        }

        var failCounter = 0
        while (true) {
            val pickDirection = Random.nextInt(1, 5)
            var attemptedMoveY = mazeHeaderY
            var attemptedMoveX = mazeHeaderX
            var wallPlacement = 0

            when (pickDirection) {
                1 -> {
                    attemptedMoveY++
                    wallPlacement = 1
                }
                2 -> {
                    attemptedMoveY--
                    wallPlacement = 1
                }
                3 -> {
                    attemptedMoveX++
                    wallPlacement = 0
                }
                4 -> {
                    attemptedMoveX--
                    wallPlacement = 0
                }
            }
            if (attemptedMoveY in 1..SCREENHEIGHT && attemptedMoveX in 1..SCREENWIDTH) {
                if (screen[attemptedMoveY][attemptedMoveX] == EMPTYTILE) {
                    mazeHeaderY = attemptedMoveY
                    mazeHeaderX = attemptedMoveX
                    screen[attemptedMoveY][attemptedMoveX] = FLOOR
                    if (wallPlacement == 1) {
                        buildWall(screen, pickDirection, attemptedMoveY, attemptedMoveX, tileList)
                    } else {
                        buildWall(screen, pickDirection, attemptedMoveY, attemptedMoveX, tileList)
                    }
                } else {
                    failCounter++
                }
                if (failCounter == 1000) {
                    randomStart = true
                    break
                }
            }

        }
        screen[playerY][playerX] = PLAYER
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

fun checkCoordinate(pos:Pair<Int,Int>):Boolean{
    var (posY, posX) = pos
    if (posY in 1..SCREENHEIGHT && posX in 1..SCREENWIDTH) {
        return true
    }
    return false
}

fun buildWall(screen: LiveList<LiveList<String>>,direction:Int, posY: Int, posX: Int, tileList : MutableList<Pair<Int,Int>>) {
    when (Random.nextInt(1,8)) {
        1 , 2, 3, 4, 5 -> {
            if (direction == 1) {
                doubleWall(screen, Pair(posY, posX), Pair(0, 1))
            } else {
                doubleWall(screen, Pair(posY, posX), Pair(1, 0))
            }
        }
        6 -> {
            if (checkCoordinate(Pair(posY, posX+1))){
                if (screen[posY][posX + 1] == EMPTYTILE) {
                    screen[posY][posX + 1] = WALL
                }
            }
            if (checkCoordinate(Pair(posY, posX-1))) {
                tileList.add(Pair(posY, posX - 1))
            }
        }
        7 -> {
            if (checkCoordinate(Pair(posY, posX-1))){
                if (screen[posY][posX - 1] == EMPTYTILE) {
                    screen[posY][posX - 1] = WALL
                }
            }
            if (checkCoordinate(Pair(posY, posX + 1))) {
                tileList.add(Pair(posY, posX + 1))
            }
        }
    }
}

fun doubleWall(screen: LiveList<LiveList<String>>, pos:Pair<Int,Int>, side:Pair<Int,Int>){
    val (posY, posX) = pos
    val (sideY, sideX) = side
    if (checkCoordinate(Pair(posY + sideY, posX + sideX)) && checkCoordinate(Pair(posY - sideY, posX - sideX))) {
        if (screen[posY + sideY][posX + sideX] == EMPTYTILE) {
            screen[posY + sideY][posX + sideX] = WALL
        }
        if (screen[posY - sideY][posX - sideX] == EMPTYTILE) {
            screen[posY - sideY][posX - sideX] = WALL
        }
    }
}