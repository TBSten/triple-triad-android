package your.projectPackage.ui.designSystem

import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import your.projectPackage.common.lazyWithReceiver

interface AppColors {
    /* Add your colors */
    val primary: Color
}

internal object DefaultAppColors : AppColors {
    override val primary: Color = Color(0xFF45C1D0)
}

/**
 * AppColors を MaterialTheme.colorScheme に変換する拡張関数。
 */
internal val AppColors.asMaterial: androidx.compose.material3.ColorScheme by lazyWithReceiver {
    lightColorScheme(
        primary = this.primary,
    )
}
