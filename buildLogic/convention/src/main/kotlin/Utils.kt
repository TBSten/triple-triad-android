import java.io.File
import java.util.Properties
import org.gradle.api.Project

val Project.localProperties
    get() = loadProperties(filePath = "local.properties")

fun loadProperties(file: File) = Properties().apply { file.inputStream().use { load(it) } }

fun Project.loadProperties(filePath: String) = loadProperties(file = File(rootProject.rootDir, filePath))
