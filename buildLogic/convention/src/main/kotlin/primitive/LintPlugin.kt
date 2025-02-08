package primitive

import BuildOutputFiles
import dsl.alias
import dsl.detekt
import dsl.ktlint
import dsl.libs
import dsl.plugin
import dsl.plugins
import dsl.version
import io.gitlab.arturbosch.detekt.Detekt
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType
import org.jlleitschuh.gradle.ktlint.tasks.GenerateReportsTask

open class LintPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            configureKtlint()
            configureDetekt()
        }
    }
}

private fun Project.configureKtlint() {
    plugins {
        alias(libs.plugin("ktlint"))
    }

    ktlint {
        version.set("1.5.0")
        reporters {
            reporter(ReporterType.SARIF)
        }
    }

    tasks.withType(GenerateReportsTask::class.java) {
        reportsOutputDirectory.set(
            BuildOutputFiles(rootProject)
                .ktlintReport(this@configureKtlint),
        )
    }
}

private fun Project.configureDetekt() {
    plugins {
        alias(libs.plugin("detekt"))
    }
    detekt {
        toolVersion = libs.version("detekt")
        config.setFrom(rootProject.layout.projectDirectory.file("config/detekt/detekt.yml"))
        buildUponDefaultConfig = true
    }

    tasks.withType<Detekt>().configureEach {
        reports {
            sarif.required.set(true)
            md.required.set(true)
            html.required.set(true)
            txt.required.set(true)
            xml.required.set(true)
        }
    }
}
