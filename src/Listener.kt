import java.awt.Toolkit
import java.awt.datatransfer.*
import java.util.*
import java.util.TimerTask



fun main(args: Array<String>) {
    var allContents: MutableList<String> = mutableListOf()

    val scheduler : Timer = Timer()
    scheduler.schedule(object : TimerTask() {
        override fun run() {
            val toolkit : Toolkit = Toolkit.getDefaultToolkit()
            val clipboard : Clipboard = toolkit.systemClipboard
            val copiedString : String = clipboard.getData(DataFlavor.stringFlavor).toString()
            if(allContents.size==0){
                allContents.add(copiedString);
                allContents.forEach { a -> print("$a, ") }
                println()
            }else if(!allContents.contains(copiedString)){
                allContents.add(copiedString);
                allContents.forEach { a -> print("$a, ") }
                println()
            }
        }
    }, 100, 100)


}



