import GUI.dontCreate
import java.awt.Image
import java.awt.Toolkit
import java.awt.datatransfer.*
import java.awt.image.BufferedImage
import java.util.*
import java.util.TimerTask
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.UnsupportedFlavorException
import java.awt.datatransfer.Transferable
import java.io.ByteArrayOutputStream
import java.nio.charset.Charset
import javax.imageio.ImageIO

val allContents: MutableList<Any> = mutableListOf()
val toolkit : Toolkit = Toolkit.getDefaultToolkit()
val clipboard : Clipboard = toolkit.systemClipboard

fun listen():Unit{
    val imageStrings : MutableList<String> = mutableListOf()
    val scheduler : Timer = Timer()

    scheduler.schedule(object : TimerTask() {
        override fun run() {
            try {
                var copiedString: Any = clipboard.getData(DataFlavor.stringFlavor)
                val bytes = copiedString.toString().toByteArray()
                copiedString = String(bytes, Charset.forName("UTF-8"))
                if(allContents.size==0){
                    allContents.add(copiedString)
                    allContents.forEach { a -> print("$a, ") }
                    println()
                }else if(!allContents.contains(copiedString)){
                    allContents.add(copiedString)
                    allContents.forEach { a -> print("$a, ") }
                    println()
                }
            }catch(e : Exception){
                try {
                    val image: BufferedImage = clipboard.getData(DataFlavor.imageFlavor) as BufferedImage
                    val baos : ByteArrayOutputStream = ByteArrayOutputStream()
                    ImageIO.write(image, "png", baos)
                    val imageInByte = baos.toByteArray()
                    val encoded64 : String = Base64.getEncoder().encodeToString(imageInByte)
                        if (allContents.size == 0) {
                            allContents.add(encoded64)
                           // println(encoded64);
                            // allContents.forEach { a -> print("$a, ") }
                            // println()
                        } else if (!allContents.contains(encoded64)) {
                            allContents.add(encoded64)
                            //println(encoded64);
                            // allContents.forEach { a -> print("$a, ") }
                            // println()
                        }

                }catch(f : Exception){

                }
            }

        }
    }, 500, 500)

}



