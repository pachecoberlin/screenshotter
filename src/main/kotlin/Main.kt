import java.awt.Rectangle
import java.awt.Robot
import java.io.File
import javax.imageio.ImageIO

fun main(args: Array<String>) {
    val number = if (args.isNotEmpty()) args[0].toIntOrNull() ?: 500 else 500
    val robot = Robot()
    val screenShot = robot.createScreenCapture(Rectangle(120, 1300, 1660, 900))
    val path = PATH
    val file = File("$path$number.jpg")
    ImageIO.write(screenShot, "JPG", file)
}