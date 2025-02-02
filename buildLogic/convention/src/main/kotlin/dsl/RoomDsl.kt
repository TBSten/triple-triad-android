package dsl

import androidx.room.gradle.RoomExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

fun Project.room(action: RoomExtension.() -> Unit) {
    extensions.configure(action)
}
