import java.awt.Rectangle
import java.awt.Robot
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

fun main(args: Array<String>) {
    val number = if (args.isNotEmpty()) args[0].toIntOrNull() ?: 500 else 500
    val robot = Robot()
    val screenShot = robot.createScreenCapture(Rectangle(120, 1300, 1660, 900))
    val file = File("$WRITEPATH$number.png")
    if (number != 500 && file.exists()) {
        println("Overwrite file $number.png?(y/j)")
        val userinput = readLine() ?: "not empty"
        if (userinput.isNotEmpty() && userinput in ("yj")) {
            ImageIO.write(screenShot, "PNG", file)
        } else doNotOverwrite(number, screenShot)
    } else doNotOverwrite(number, screenShot)
}

private fun doNotOverwrite(number: Int, screenShot: BufferedImage?) {
    var filename = "$number"
    var file1 = File(WRITEPATH, "$filename.png")
    while (file1.exists()) {
        filename = "${filename}a"
        file1 = File(WRITEPATH, "$filename.png")
    }
    ImageIO.write(screenShot, "PNG", file1)
}