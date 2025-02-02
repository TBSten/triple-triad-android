import org.gradle.api.Project
import org.gradle.api.file.Directory
import org.gradle.api.file.RegularFile

internal class BuildOutputFiles(private val rootDir: Directory) {
    constructor(rootProject: Project) : this(rootProject.layout.buildDirectory.dir("build-outputs").get())

    private fun projectDir(project: Project) = project.path
        .split(":")
        .filter { it.isNotEmpty() }
        .joinToString("/")

    fun ktlintReport(project: Project): Directory = rootDir
        .dir(projectDir(project))
        .dir("ktlint-report")

    fun androidLintReport(project: Project, type: AndroidLintReportType): RegularFile = rootDir
        .dir(projectDir(project))
        .dir("android-lint-report")
        .file("lint-result.${type.extension}")
}

internal enum class AndroidLintReportType(val extension: String) {
    Sarif("sarif"),
    Html("html"),
}
