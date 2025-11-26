package jksalcedo.foc

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.path
import com.github.ajalt.mordant.rendering.TextColors
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

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
                // Create folder if not exists
                if (!targetFolder.toFile().exists()) targetFolder.toFile().mkdirs()

                // Move logic with collision check
                moveFile(file, targetFolder)
                echo(TextColors.green("Moved ${file.name} to $category"))
            }
        }
    }

    private fun moveFile(file: File, targetFolder: Path) {
        try {
            // resolve the path and check if it exists
            var destination = targetFolder.resolve(file.name)
                var i = 1
                while (destination.toFile().exists()) {
                    val newName = "${file.nameWithoutExtension}_$i.${file.extension}"
                    destination = targetFolder.resolve(newName)
                    i++
                }

            // Move file
            Files.move(file.toPath(), destination, StandardCopyOption.REPLACE_EXISTING)
        } catch (e: Exception) {
            echo(e.message)
        }
    }

    fun determineCategory(extension: String): String {
        val rules = mapOf(
            "Images" to listOf(
                "jpg", "jpeg", "png", "gif", "svg", "webp", "bmp", "ico", "tiff",
                "tif", "heic", "heif", "raw", "cr2", "nef", "arw", "dng", "psd",
                "ai", "eps", "avif", "jfif"
            ),
            "Videos" to listOf(
                "mp4", "mkv", "mov", "avi", "wmv", "flv", "webm", "m4v", "mpg",
                "mpeg", "3gp", "3g2", "ogv", "ts", "vob", "mts", "m2ts", "f4v",
                "rmvb", "asf", "divx"
            ),
            "Audios" to listOf(
                "mp3", "wav", "flac", "aac", "ogg", "opus", "m4a", "wma", "aiff",
                "ape", "alac", "pcm", "3gp", "amr", "mid", "midi", "ra", "oga"
            ),
            "Documents" to listOf(
                "pdf", "doc", "docx", "txt", "rtf", "odt", "pages", "tex", "wpd",
                "xlsx", "xls", "csv", "ods", "xlsm", "xlsb", "numbers",
                "ppt", "pptx", "pps", "odp", "key",
                "md", "markdown", "log", "msg", "eml"
            ),
            "Installers" to listOf(
                "exe", "msi", "apk", "dmg", "pkg", "deb", "rpm", "appimage",
                "flatpak", "snap", "run", "app", "bat", "sh", "bin", "bundle",
                "x86_64", "aarch64", "arm64", "i386"
            ),
            "Archives" to listOf(
                "zip", "rar", "7z", "tar", "gz", "bz2", "xz", "tgz", "tbz2",
                "txz", "tar.gz", "tar.bz2", "tar.xz", "zipx", "cab", "iso",
                "lzh", "arj", "z", "lz", "lzma", "zst"
            ),
            "Code" to listOf(
                "java", "scala", "groovy", "clj", "cljs",
                "html", "htm", "css", "scss", "sass", "less", "js", "jsx", "ts",
                "tsx", "vue", "svelte",
                "c", "cpp", "cc", "cxx", "h", "hpp", "hxx", "rs", "go",
                "py", "rb", "php", "pl", "lua", "r", "jl",
                "hs", "elm", "fs", "fsx", "ml", "mli", "ex", "exs", "erl",
                "sh", "bash", "zsh", "fish", "ps1", "psm1",
                "json", "xml", "yaml", "yml", "toml", "ini", "conf", "cfg",
                "gradle", "maven", "cmake", "make", "mk",
                "swift", "m", "mm", "dart", "kt", "kts",
                "sql", "graphql", "proto", "vim", "asm", "s"
            ),
            "Fonts" to listOf(
                "ttf", "otf", "woff", "woff2", "eot", "fon", "dfont"
            ),
            "3D Models" to listOf(
                "obj", "fbx", "stl", "dae", "3ds", "blend", "gltf", "glb", "ply",
                "max", "ma", "mb", "c4d", "skp"
            ),
            "Databases" to listOf(
                "db", "sqlite", "sqlite3", "mdb", "accdb", "sql", "dbf", "sav"
            ),
            "Ebooks" to listOf(
                "epub", "mobi", "azw", "azw3", "fb2", "lit", "lrf", "cbr", "cbz"
            ),
            "CAD" to listOf(
                "dwg", "dxf", "dwf", "dgn", "rvt", "ifc", "step", "stp", "iges", "igs"
            ),
            "Disk Images" to listOf(
                "iso", "img", "dmg", "vdi", "vmdk", "vhd", "vhdx", "qcow2", "toast"
            ),
            "Spreadsheets" to listOf(
                "xlsx", "xls", "csv", "ods", "xlsm", "xlsb", "numbers", "tsv"
            ),
            "Presentations" to listOf(
                "ppt", "pptx", "pps", "ppsx", "odp", "key"
            ),
            "Certificates" to listOf(
                "pem", "crt", "cer", "p12", "pfx", "key", "csr", "p7b", "p7c"
            )
        )
        var category = ""

        for ((key, extensions) in rules) {
            if (extension.lowercase() in extensions) {
                category = key
            }
        }

        return category
    }
}

fun main(args: Array<String>) {
    Organizer().main(args)
}