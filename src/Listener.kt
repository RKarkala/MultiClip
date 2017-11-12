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

val txtContent: MutableList<Any> = mutableListOf("Text Copies:")
val imgContent: MutableList<Any> = mutableListOf("Image Copies: ")
val toolkit : Toolkit = Toolkit.getDefaultToolkit()
val clipboard : Clipboard = toolkit.systemClipboard

fun listen():Unit{
    fun countSubstring(s: String, sub: String): Int = s.split(sub).size - 1
    val imageStrings : MutableList<String> = mutableListOf()
    val scheduler : Timer = Timer()
    var ok : Boolean = true
    scheduler.schedule(object : TimerTask() {
        override fun run() {
            try {
                var copiedString: Any = clipboard.getData(DataFlavor.stringFlavor)
                val bytes = copiedString.toString().toByteArray()
                copiedString = String(bytes, Charset.forName("UTF-8"))
                if(txtContent.size==0){
                    txtContent.add(copiedString)
                    println("Text Size: "+txtContent.size)

                }else if(!txtContent.contains(copiedString) || (copiedString.equals("Text Copies:" )&& ok == true)){
                    ok = false;
                    txtContent.add(copiedString)
                    println("Text Size: "+txtContent.size)

                }
            }catch(e : Exception){
                try {
                    val image: BufferedImage = clipboard.getData(DataFlavor.imageFlavor) as BufferedImage
                    val baos : ByteArrayOutputStream = ByteArrayOutputStream()
                    ImageIO.write(image, "png", baos)
                    val imageInByte = baos.toByteArray()
                    val encoded64 : String = Base64.getEncoder().encodeToString(imageInByte)
                        if (imgContent.size == 0) {
                            imgContent.add(encoded64)
                            println("Image Size: "+imgContent.size)
                        } else if (!imgContent.contains(encoded64)) {
                            imgContent.add(encoded64)
                            println("Image Size: "+imgContent.size)

                        }

                }catch(f : Exception){

                }
            }

        }
    }, 500, 500)

}



