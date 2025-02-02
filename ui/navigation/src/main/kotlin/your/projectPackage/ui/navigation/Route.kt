package your.projectPackage.ui.navigation

sealed interface Route
interface Navigation : Route
interface Screen : Route
interface Dialog : Route
