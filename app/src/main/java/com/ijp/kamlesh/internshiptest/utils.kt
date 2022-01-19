package com.ijp.kamlesh.internshiptest

import android.graphics.Color
import java.util.*

public  class utils {
    companion object{
        fun getRandomColor(): Int {
            val rnd = Random()
            val color: Int = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
            return color
        }
    }
}