package org.example

import com.varabyte.kotter.foundation.*
import com.varabyte.kotter.foundation.anim.renderAnimOf
import com.varabyte.kotter.foundation.collections.LiveList
import com.varabyte.kotter.foundation.input.*
import com.varabyte.kotter.foundation.text.*
import com.varabyte.kotter.foundation.collections.liveListOf
import com.varabyte.kotter.foundation.timer.*
import java.awt.desktop.QuitEvent
import kotlin.concurrent.thread
import kotlin.random.Random
import kotlin.time.Duration.Companion.milliseconds

const val SCREENHEIGHT = 15
const val SCREENWIDTH = 15
const val EMPTY = " . "
const val PLAYER = " @ "
const val DOOR = " D "
const val VERTWALL = " | "
const val HORIWALL = "---"
const val EDGE = " + "

fun main() = session(){
    var screen = liveListOf<LiveList<String>>()
    screen.add(liveListOf(EDGE + HORIWALL.repeat(SCREENWIDTH) + EDGE) )
    repeat(SCREENHEIGHT){
        screen.add(liveListOf(VERTWALL + EMPTY.repeat(SCREENWIDTH) + VERTWALL))
    }
    screen.add(liveListOf(EDGE + HORIWALL.repeat(SCREENWIDTH) + EDGE) )

    section {
        p{
            for (screenY in screen){
                for (screenX in screenY){
                    textLine(screenX)
                }
            }

        }
    }.run {  }
}