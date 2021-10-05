import net.sourceforge.tess4j.*
import net.sourceforge.tess4j.util.LoadLibs
import java.awt.Rectangle
import java.awt.Robot
import java.io.File
import javax.imageio.ImageIO

fun simpleScreenshot(args: Array<String>) {
    val number = if (args.isNotEmpty()) args[0].toIntOrNull() ?: 500 else 500
    val robot = Robot()
    val screenShot = robot.createScreenCapture(Rectangle(120, 1300, 1660, 900))
    val path = PATH
    val file = File("$path$number.png")
    ImageIO.write(screenShot, "PNG", file)
}

fun main() {
    val robot = Robot()
    val screenShot = robot.createScreenCapture(Rectangle(120, 1300, 1660, 900))
//    y=860-890
//    x=085-130
    val instance: ITesseract = Tesseract()
    val tessDataFolder: File = LoadLibs.extractTessResources("tessdata") // Maven build bundles English data
    instance.setDatapath(tessDataFolder.path)
    instance.setTessVariable("user_defined_dpi", "300")
    instance.setTessVariable("tessedit_char_whitelist", "0123456789")

//Muss wahrscheinlich noch verschoben werden
    val subimage = screenShot.getSubimage(50,850,50,50)
//    val subimage = screenShot.getSubimage(0,700,200,200)
//  Das ist ok  val subimage = screenShot.getSubimage(0,300,600,600)

    val result: String = instance.doOCR(subimage)
    val number = result.filter { it.isDigit() }.toIntOrNull() ?: 500
    println(number)
    val file = File("$PATH$number.png")
    ImageIO.write(subimage, "PNG", file)
}