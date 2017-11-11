import java.awt.*
import java.awt.datatransfer.*
import java.util.*
import java.util.TimerTask



fun main(args: Array<String>) {
    val clips: MutableList<String> = mutableListOf()
    val t = Timer()
    t.schedule(object : TimerTask() {
        override fun run() {
            //println("Entered")
            val userClipBoard = Toolkit.getDefaultToolkit().systemClipboard
                val currentClip = userClipBoard.getData(DataFlavor.stringFlavor).toString()
                if (clips.size == 0) {
                    clips.add(currentClip)
                } else {
                    if (!clips[clips.size - 1].equals(currentClip)) {
                        clips.add(currentClip)
                    }
                }
            //clips.forEach { a -> print("$a,") }
        }
    }, 0, 1)

}

