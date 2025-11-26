package jksalcedo.foc

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.path
import com.github.ajalt.mordant.rendering.TextColors
import java.nio.file.Paths

class Organizer : CliktCommand() {
    val path by option("-p", "--path", help="Path to organize").path().default(Paths.get("."))
    val dryRun by option("--dry-run", help="Preview changes without moving").flag()

    override fun run() {
        echo("Scanning directory: $path")
        
        val files = path.toFile().listFiles()?.filter { it.isFile } ?: return

        files.forEach { file ->
            val extension = file.extension
            val category = determineCategory(extension)
            val targetFolder = path.resolve(category)
            
            if (dryRun) {
                echo(TextColors.yellow("Would move ${file.name} to $category"))
            } else {
                // Ensure folder exists
                if (!targetFolder.toFile().exists()) targetFolder.toFile().mkdirs()
                
                // Move logic with collision check
                //moveFileSafe(file, targetFolder)
                echo(TextColors.green("Moved ${file.name} to $category"))
            }
        }
    }

    fun determineCategory(extension: String): String {
        val rules = mapOf(
            "Images" to listOf("jpg", "png", "svg", "webp", "jpeg"),
            "Videos" to listOf("mp4", "mkv", "mov", "gif"),
            "Documents" to listOf("pdf", "docx", "txt", "xlsx"),
            "Installers" to listOf("exe", "msi", "apk", "dmg"),
            "Archives" to listOf("zip", "rar", "7z"),
            "Kotlin Files" to listOf("kt", "kts")
        )
        var category = ""

        for ((key, extensions) in rules) {
            if (extension in extensions) {
                category = key

            }
        }

        return category
    }
}

fun main(args: Array<String>) {
    Organizer().main(args)
}