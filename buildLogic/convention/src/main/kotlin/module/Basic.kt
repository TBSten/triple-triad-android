package module

import dsl.implementation
import dsl.testImplementation
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * 全てのモジュールに適用する設定
 */
internal fun Project.configureBasic() {
    dependencies {
        if (!path.startsWith(":common")) {
            implementation(project(":common"))
        }

        if (!path.startsWith(":common:testing")) {
            testImplementation(project(":common:testing"))
        }
    }
}
