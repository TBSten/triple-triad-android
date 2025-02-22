package me.tbsten.tripleTriad.ui.testing

import com.github.takahirom.roborazzi.ExperimentalRoborazziApi
import com.github.takahirom.roborazzi.InternalRoborazziApi
import com.github.takahirom.roborazzi.RoborazziRecordFilePathStrategy
import com.github.takahirom.roborazzi.provideRoborazziContext
import com.github.takahirom.roborazzi.roborazziRecordFilePathStrategy
import com.github.takahirom.roborazzi.roborazziSystemPropertyOutputDirectory

@OptIn(ExperimentalRoborazziApi::class, InternalRoborazziApi::class)
fun getRoborazziOutputFilePath(name: String): String {
    val isRelativePathFromCurrentDirectory =
        roborazziRecordFilePathStrategy() ==
            RoborazziRecordFilePathStrategy.RelativePathFromCurrentDirectory

    val pathPrefix =
        if (isRelativePathFromCurrentDirectory) {
            roborazziSystemPropertyOutputDirectory() + java.io.File.separator
        } else {
            ""
        }
    return "$pathPrefix$name.${provideRoborazziContext().imageExtension}"
}
