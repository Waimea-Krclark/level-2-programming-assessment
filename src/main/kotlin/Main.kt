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
import java.awt.Event.ESCAPE
import java.awt.desktop.QuitEvent
import kotlin.concurrent.thread
import kotlin.random.Random
import kotlin.time.Duration.Companion.milliseconds

//Constant variable declarations
const val SCREENHEIGHT = 15
const val SCREENWIDTH = 15
const val EMPTY = ". "
const val PLAYER = "@ "
const val DOOR = "D "
const val WALL = "# "

//Main Function
fun main() = session(){
    //Live Lists are a Kotter Collection that allows for the list to be updated without having to rerender the terminal
    //Live list of a Live list to create a 2D array that can be used with array[Y][X]
    var screen = liveListOf<LiveList<String>>()

    //Builds the screen
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

    //Builds the maze and adds the player to the bottom
    generateMaze(screen=screen)
    var (playerY,playerX) = placePlayer(screen=screen)

    section {
        p{
            //for every tile in every row, prints the tile, and colours specific tiles (i.e green tile for player)
            for (screenY in screen){
                for (screenX in screenY){
                    if (screenX == PLAYER){
                        green{
                            text(screenX)
                        }
                    }
                    else{
                        text(screenX)
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
                Keys.W -> if (screen[playerY-1][playerX] == EMPTY){
                    playerY = playerY-1
                    screen[playerY+1][playerX] = EMPTY
                }
                Keys.S -> if (screen[playerY+1][playerX] == EMPTY){
                    playerY = playerY+1
                    screen[playerY-1][playerX] = EMPTY
                }
                Keys.A -> if (screen[playerY][playerX-1] == EMPTY){
                    playerX = playerX-1
                    screen[playerY][playerX+1] = EMPTY
                }
                Keys.D -> if (screen[playerY][playerX+1] == EMPTY){
                    playerX = playerX+1
                    screen[playerY][playerX-1] = EMPTY
                }

            }
            //Draws to player to its coordinates
            screen[playerY][playerX] = PLAYER
        }
    }
}

fun generateMaze(screen:LiveList<LiveList<String>> ){
    repeat(100){
        val randomY = Random.nextInt(1, SCREENHEIGHT+1)
        val randomX = Random.nextInt(1, SCREENWIDTH+1)
        if (screen[randomY][randomX] == EMPTY) {
            screen[randomY][randomX] = WALL
        }

    }
}

fun placePlayer(screen: LiveList<LiveList<String>>):Pair<Int,Int>{
    val playerX = Random.nextInt(1, SCREENWIDTH+1)
    val playerY = SCREENHEIGHT
    screen[playerY][playerX] = PLAYER
    return Pair(playerY,playerX)
}