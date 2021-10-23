import net.sourceforge.tess4j.ITesseract
import net.sourceforge.tess4j.Tesseract
import net.sourceforge.tess4j.util.LoadLibs
import java.awt.Color
import java.awt.Rectangle
import java.awt.Robot
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

/**
 * Playing with Screenshots and Tess4J
 */
fun readAllFilesAsImagesAndSaveSubImage() {
//    val file = File("${READPATH}1.png")
//    println(file.exists())
//    val screenShot: BufferedImage = ImageIO.read(file)
//    val subimage = screenShot.getSubimage(150, 250, 1770, 750)
//    ImageIO.write(subimage, "PNG", File("${WRITEPATH}test.png"))
    File(READPATH).walk().forEach {
        if (!it.isDirectory) {
            val screenShot: BufferedImage = ImageIO.read(it)
            val subimage = screenShot.getSubimage(150, 250, 1770, 750)
            ImageIO.write(subimage, "PNG", File(WRITEPATH + it.name))
        }
    }
}

//fun simpleScreenshot(args: Array<String>) {
fun simpleScreenshot() {
    var number = 0 //if (args.isNotEmpty()) args[0].toIntOrNull() ?: magicNumber else magicNumber
    val robot = Robot()
    while (true) {
        val screenShot = robot.createScreenCapture(Rectangle(0, 1200, 1920, 1080))
        ImageIO.write(screenShot, "PNG", File("$WRITEPATH$number.png"))
        val screenShot2 = robot.createScreenCapture(Rectangle(0, 0, 1920, 1080))
        ImageIO.write(screenShot2, "PNG", File("${WRITEPATH}_$number.png"))
        number++
        Thread.sleep(10000)
    }
}

fun printNumbersOnScreenshot() {
    val tesseract: ITesseract = setupTesseract()
    val robot = Robot()
    val screenShot = robot.createScreenCapture(Rectangle(120, 1300, 1660, 900))
    val subimage = screenShot.getSubimage(50, 850, 100, 50)
    val number = scanPicForNumber(subimage, tesseract)
    println(number)
}

fun String.intOrNull(): Int? {
    return filter { it.isDigit() }.toIntOrNull()
}

private fun scanPicForNumber(file: File?, tesseract: ITesseract): Int {
    return scanPicForNumber(fileInput = file, tesseract = tesseract)
}

private fun scanPicForNumber(image: BufferedImage?, tesseract: ITesseract): Int {
    return scanPicForNumber(imageInput = image, tesseract = tesseract)
}

val magicNumber = 600
private fun scanPicForNumber(imageInput: BufferedImage? = null, fileInput: File? = null, tesseract: ITesseract): Int {
    return if (imageInput != null) {
        tesseract.doOCR(binarize(imageInput)).intOrNull() ?: tesseract.doOCR(imageInput).intOrNull() ?: magicNumber
    } else {
        tesseract.doOCR(binarize(ImageIO.read(fileInput))).intOrNull() ?: tesseract.doOCR(fileInput).intOrNull()
        ?: magicNumber
    }
}

//Tried to constantly check number in the corner of the screenshot and name file like the number
fun test() {
    val tesseract: ITesseract = setupTesseract()
    val robot = Robot()
    var lastNumber = 0
    while (true) {
        val screenShot = robot.createScreenCapture(Rectangle(120, 1300, 1660, 900))
//    val subimage = screenShot.getSubimage(101,871,18,14)//Exakt zwei Zahlen
//  val subimage = screenShot.getSubimage(90,870,30,16) //  80,82 ,aber nicht 88
//        val subimage = screenShot.getSubimage(90, 870, 30, 16)// 80 und 82 oder 88
//        val subimage = screenShot.getSubimage(10, 770, 180, 130)// 80 und 82 oder 88
//        val subimage = screenShot.getSubimage(10, 770, 180, 130)// 80 und 82 oder 88
//        val subimage = screenShot.getSubimage(0, 800, 180, 50)// das geht
        val subimage = screenShot.getSubimage(130, 825, 50, 25)// das geht
        val number = scanPicForNumber(subimage, tesseract)
        println(number)
        val sleepTime = 30000L
        if (number == lastNumber && number != magicNumber) {
//            println("Saved $number.png")
            ImageIO.write(screenShot, "PNG", File(AUTOPATH, "$number.png"))
            Thread.sleep(sleepTime)
            continue
        } else {
            lastNumber = number
        }
        var filename = "$number"
        var file = File(AUTOPATH, "$filename.png")
        while (file.exists()) {
            filename = "${filename}a"
            file = File(AUTOPATH, "$filename.png")
        }
//        println("Saved $filename.png")
        ImageIO.write(screenShot, "PNG", file)
//        ImageIO.write(subimage, "PNG", File(AUTOPATH, "test.png"))
        Thread.sleep(sleepTime)
    }
}

private fun setupTesseract(): ITesseract {
    val tesseract: ITesseract = Tesseract()
    val tessDataFolder: File = LoadLibs.extractTessResources("tessdata") // Maven build bundles English data
    tesseract.setDatapath(tessDataFolder.path)
    tesseract.setTessVariable("user_defined_dpi", "300")
    tesseract.setTessVariable("tessedit_char_whitelist", "0123456789")
    return tesseract
}

fun binarize(img: BufferedImage): BufferedImage {
    val bufferedImage = BufferedImage(img.width, img.height, BufferedImage.TYPE_INT_RGB)
    for (i in 0 until img.width) {
        for (j in 0 until img.height) {
            val rgbValues: Int = img.getRGB(i, j)
            //An int can be represented with 8 hex numbers. The first two are the alpha value,
            // which we will ignore within this calculation followed by two hex numbers for red,
            // two for green and two for blue
            val r = 0x00ff0000 and rgbValues shr 16
            val g = 0x0000ff00 and rgbValues shr 8
            val b = 0x000000ff and rgbValues
            val m = r + g + b
            //(255+255+255)/2 = 383 middle of dark and light
            bufferedImage.setRGB(i, j, if (m >= 383) Color.WHITE.rgb else 0)
        }
    }
    return bufferedImage
}

fun oldmain(args: Array<String>) {
    var number = if (args.isNotEmpty()) args[0].toIntOrNull() ?: 500 else 500
    while (true) {
        println("going to save $number.png, type q to quit or type other name:")
        var name = readLine() ?: ""
        if (name == "q") {
            break
        }

        name = if (name.isEmpty()) number.toString() else name

        number = name.toIntOrNull() ?: ++number

        val robot = Robot()
        val screenShot = robot.createScreenCapture(Rectangle(120, 1300, 1660, 900))
        val file = File("$AUTOPATH$name.png")
        if (number != 500 && file.exists()) {
            println("Overwrite file $name.png?(y/j)")
            val userinput = readLine() ?: "not empty"
            if (userinput.isNotEmpty() && userinput in ("yj")) {
                ImageIO.write(screenShot, "PNG", file)
            } else doNotOverwrite(number, screenShot)
        } else doNotOverwrite(number, screenShot)
        number++
    }
}