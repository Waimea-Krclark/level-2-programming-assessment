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
import com.varabyte.kotter.runtime.render.RenderScope
import java.awt.Event.ESCAPE
import java.awt.desktop.QuitEvent
import kotlin.concurrent.thread
import kotlin.random.Random
import kotlin.time.Duration.Companion.milliseconds

//Constant variable declarations
const val SCREENHEIGHT = 30
const val SCREENWIDTH = 30
const val EMPTY = "  "
const val PLAYER = "O "
const val DOOR = "D "
const val WALL = "# "
// Global variables
var playerY = 0
var playerX = 0

//Main Function
fun main() = session(){
    //Live Lists are a Kotter Collection that allows for the list to be updated without having to rerender the terminal
    //Live list of a Live list to create a 2D array that can be used with array[Y][X]
    var screen = liveListOf<LiveList<String>>()

    //Builds the screen
    buildScreen(screen=screen)
    //Builds the maze and adds the player to the bottom
    generateMaze(screen=screen)
    placePlayer(screen=screen)
    var (doorY,doorX) = placeDoor(screen=screen)

    section {
        p{
            //for every tile in every row, prints the tile, and colours specific tiles (i.e green tile for player)
            for (screenY in screen){
                for (screenX in screenY){
                    if (screenX == PLAYER){
                        cyan{
                            text(screenX)
                        }
                    }
                    else if (screenX == DOOR){
                        green{
                            text(screenX)
                        }
                    }
                    else{
                        magenta {
                            text(screenX)
                        }
                    }
                }
                //New Line
                textLine()
            }
        }
    }.runUntilSignal {
        //Run until signal creates a looped script that will run until the Signal() function is called, similar to a While loop
        var canMove = true
        //Movement script
        onKeyPressed {
            when(key){
                Keys.W -> movePlayer(screen=screen, moveX=playerX, moveY=playerY-1)
                Keys.S -> movePlayer(screen=screen, moveX=playerX, moveY=playerY+1)
                Keys.A -> movePlayer(screen=screen, moveX=playerX-1, moveY=playerY)
                Keys.D -> movePlayer(screen=screen, moveX=playerX+1, moveY=playerY)
            }
            //Draws to player to its coordinates
            screen[playerY][playerX] = PLAYER
        }
    }
}

fun Session.buildScreen(screen: LiveList<LiveList<String>>){
    screen.add(liveListOf(WALL + WALL.repeat(SCREENWIDTH) + WALL))
    var xCount = 1
    repeat(SCREENHEIGHT){
        //For each row, creates the edge wall and fills in the interior with empty tiles
        screen.add(liveListOf(WALL))
        repeat(SCREENWIDTH){
            screen[xCount].add(EMPTY)
        }
        screen[xCount].add(WALL)
        xCount++
    }
    screen.add(liveListOf(WALL + WALL.repeat(SCREENWIDTH) + WALL) )

}


fun generateMaze(screen:LiveList<LiveList<String>> ){
    repeat(400){
        val randomY = Random.nextInt(1, SCREENHEIGHT+1)
        val randomX = Random.nextInt(1, SCREENWIDTH+1)
        if (screen[randomY][randomX] == EMPTY) {
            screen[randomY][randomX] = WALL
        }

    }
}

fun placePlayer(screen: LiveList<LiveList<String>>){
    playerX = Random.nextInt(1, SCREENWIDTH+1)
    playerY = SCREENHEIGHT
    screen[playerY][playerX] = PLAYER
}

fun placeDoor(screen: LiveList<LiveList<String>>):Pair<Int,Int>{
    val doorX = Random.nextInt(1, SCREENWIDTH+1)
    val doorY = 1
    screen[doorY][doorX] = DOOR
    return Pair(doorY,doorX)
}

fun movePlayer(screen: LiveList<LiveList<String>>, moveX:Int, moveY:Int) {

    if (screen[moveY][moveX] == EMPTY) {
        screen[playerY][playerX] = EMPTY
        playerY = moveY
        playerX = moveX
    }

}