package primitive

import dsl.alias
import dsl.implementation
import dsl.ktlint
import dsl.library
import dsl.libs
import dsl.plugin
import dsl.plugins
import io.gitlab.arturbosch.detekt.Detekt
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.exclude
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.withType
import org.openapitools.generator.gradle.plugin.tasks.GenerateTask

open class OpenApiPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            plugins {
                alias(libs.plugin("openApi"))
                apply(KotlinxSerializationPlugin::class)
            }

            val workGenerateDir = target.layout.buildDirectory.dir("openApiGenerate")

            tasks.named<GenerateTask>("openApiGenerate") {
                generatorName = "kotlin"
                library = "jvm-retrofit2"
                inputSpec = "${target.layout.projectDirectory}/openapi.yaml"
                outputDir = workGenerateDir.get().asFile.absolutePath
                configOptions = mapOf(
                    "dateLibrary" to "java8",
                    "serializationLibrary" to "kotlinx_serialization",
                )
                additionalProperties = mapOf(
                    "useCoroutines" to "true",
                )
                generateModelTests = false
                generateApiTests = false

                apiPackage = packageName.map { "$it.api" }
                modelPackage = packageName.map { "$it.model" }
            }

            val packageNameDir = provider {
                val packageName = tasks.named<GenerateTask>("openApiGenerate").get().packageName
                packageName.map { it.replace(".", "/") }.get()
            }

            tasks.register("buildApi") {
                dependsOn("openApiGenerate")
                doLast {
                    val generatedSrcDir = workGenerateDir
                        .map {
                            it.dir("src/main/kotlin/${packageNameDir.get()}")
                        }
                        .get().asFile
                    val targetSrcDir = target.layout.projectDirectory.dir("src/main/kotlin/${packageNameDir.get()}").asFile
                    generatedSrcDir.copyRecursively(targetSrcDir, overwrite = true)
                }
            }

            ktlint {
                filter {
                    exclude {
                        it.path.matches(Regex(".*${packageNameDir.get()}.*"))
                    }
                }
            }

            tasks.withType<Detekt>().configureEach {
                exclude {
                    it.path.matches(Regex(".*${packageNameDir.get()}.*"))
                }
            }

            dependencies {
                implementation(libs.library("okhttp"))
                implementation(libs.library("okhttpLoggingInterceptor"))
                implementation(libs.library("retrofit"))
                implementation(libs.library("retrofitConverterScalars"))
                implementation(libs.library("retrofitConverterKotlinxSerialization"))
                // 自動生成されるコードに必要な依存関係, ライブラリ側の問題で common を exclude しないと duplicate class error が発生する。
                "implementation"("org.apache.oltu.oauth2:org.apache.oltu.oauth2.client:1.0.2") {
                    exclude("org.apache.oltu.oauth2", "org.apache.oltu.oauth2.common")
                }
            }
        }
    }
}
