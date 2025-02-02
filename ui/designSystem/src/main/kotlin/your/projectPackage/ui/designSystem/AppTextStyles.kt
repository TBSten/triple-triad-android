package your.projectPackage.ui.designSystem

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import your.projectPackage.common.lazyWithReceiver

interface AppTextStyles {
    /* Add your text styles */
    val base: TextStyle
    val body: TextStyle
}

internal object DefaultAppTextStyles : AppTextStyles {
    override val base: TextStyle =
        TextStyle(
            fontSize = 16.sp,
            lineHeight = 19.sp,
        )
    override val body: TextStyle =
        base
}

/**
 * AppTextStyles を MaterialTheme.typography に変換する拡張関数。
 */
internal val AppTextStyles.asMaterial: Typography by lazyWithReceiver {
    Typography(
        bodyMedium = this.body,
    )
}
