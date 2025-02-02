package your.projectPackage.error

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApplicationErrorStateHolder @Inject constructor() : AbstractErrorStateHolder()
