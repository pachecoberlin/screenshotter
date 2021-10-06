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
        } else doNotOverwrite(file, number, screenShot)
    } else doNotOverwrite(file, number, screenShot)
}

private fun doNotOverwrite(file: File, number: Int, screenShot: BufferedImage?) {
    var file1 = file
    var number1 = number
    while (file1.exists()) {
        file1 = File(WRITEPATH, "${++number1}.png")
    }
    ImageIO.write(screenShot, "PNG", file1)
}