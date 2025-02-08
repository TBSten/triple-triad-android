package me.tbsten.tripleTriad.ui.designSystem.components.textfield

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import me.tbsten.tripleTriad.ui.designSystem.TripleTriadTheme
import me.tbsten.tripleTriad.ui.designSystem.components.textfield.base.CommonDecorationBox
import me.tbsten.tripleTriad.ui.designSystem.components.textfield.base.FocusedOutlineThickness
import me.tbsten.tripleTriad.ui.designSystem.components.textfield.base.HorizontalIconPadding
import me.tbsten.tripleTriad.ui.designSystem.components.textfield.base.LabelBottomPadding
import me.tbsten.tripleTriad.ui.designSystem.components.textfield.base.SupportingTopPadding
import me.tbsten.tripleTriad.ui.designSystem.components.textfield.base.TextFieldColors
import me.tbsten.tripleTriad.ui.designSystem.components.textfield.base.TextFieldHorizontalPadding
import me.tbsten.tripleTriad.ui.designSystem.components.textfield.base.TextFieldMinHeight
import me.tbsten.tripleTriad.ui.designSystem.components.textfield.base.TextFieldVerticalPadding
import me.tbsten.tripleTriad.ui.designSystem.components.textfield.base.UnfocusedOutlineThickness
import me.tbsten.tripleTriad.ui.designSystem.components.textfield.base.containerOutline

@Composable
fun OutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = TripleTriadTheme.typography.input,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    placeholder: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    label: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    shape: Shape = OutlinedTextFieldDefaults.Shape,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(),
    cursorBrush: Brush = SolidColor(colors.cursorColor(isError).value),
) {
    val textColor =
        textStyle.color.takeOrElse {
            colors.textColor(enabled, isError, interactionSource).value
        }
    val mergedTextStyle = textStyle.merge(TextStyle(color = textColor))

    CompositionLocalProvider(LocalTextSelectionColors provides colors.selectionColors) {
        BasicTextField(
            modifier =
            modifier
                .defaultMinSize(
                    minHeight = OutlinedTextFieldDefaults.MinHeight,
                )
                .fillMaxWidth(),
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
            readOnly = readOnly,
            textStyle = mergedTextStyle,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = singleLine,
            maxLines = maxLines,
            minLines = minLines,
            visualTransformation = visualTransformation,
            onTextLayout = onTextLayout,
            interactionSource = interactionSource,
            cursorBrush = cursorBrush,
            decorationBox = @Composable { innerTextField ->
                OutlinedTextFieldDefaults.DecorationBox(
                    value = value,
                    innerTextField = innerTextField,
                    visualTransformation = visualTransformation,
                    label = label,
                    placeholder = placeholder,
                    leadingIcon = leadingIcon,
                    trailingIcon = trailingIcon,
                    prefix = prefix,
                    suffix = suffix,
                    supportingText = supportingText,
                    enabled = enabled,
                    isError = isError,
                    interactionSource = interactionSource,
                    colors = OutlinedTextFieldDefaults.colors(),
                    shape = shape,
                )
            },
        )
    }
}

@Composable
fun OutlinedTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = TripleTriadTheme.typography.input,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    placeholder: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    label: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    shape: Shape = OutlinedTextFieldDefaults.Shape,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(),
    cursorBrush: Brush = SolidColor(colors.cursorColor(isError).value),
) {
    val textColor =
        textStyle.color.takeOrElse {
            colors.textColor(enabled, isError, interactionSource).value
        }
    val mergedTextStyle = textStyle.merge(TextStyle(color = textColor))

    CompositionLocalProvider(LocalTextSelectionColors provides colors.selectionColors) {
        BasicTextField(
            modifier =
            modifier
                .defaultMinSize(
                    minHeight = OutlinedTextFieldDefaults.MinHeight,
                )
                .fillMaxWidth(),
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
            readOnly = readOnly,
            textStyle = mergedTextStyle,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = singleLine,
            maxLines = maxLines,
            minLines = minLines,
            visualTransformation = visualTransformation,
            onTextLayout = onTextLayout,
            interactionSource = interactionSource,
            cursorBrush = cursorBrush,
            decorationBox = @Composable { innerTextField ->
                OutlinedTextFieldDefaults.DecorationBox(
                    value = value.text,
                    innerTextField = innerTextField,
                    visualTransformation = visualTransformation,
                    label = label,
                    placeholder = placeholder,
                    leadingIcon = leadingIcon,
                    trailingIcon = trailingIcon,
                    prefix = prefix,
                    suffix = suffix,
                    supportingText = supportingText,
                    enabled = enabled,
                    isError = isError,
                    interactionSource = interactionSource,
                    colors = OutlinedTextFieldDefaults.colors(),
                    shape = shape,
                )
            },
        )
    }
}

@Immutable
internal object OutlinedTextFieldDefaults {
    val MinHeight = TextFieldMinHeight
    val Shape: Shape = RoundedCornerShape(8.dp)

    private fun contentPadding(
        start: Dp = TextFieldHorizontalPadding,
        end: Dp = TextFieldHorizontalPadding,
        top: Dp = TextFieldVerticalPadding,
        bottom: Dp = TextFieldVerticalPadding,
    ): PaddingValues = PaddingValues(start, top, end, bottom)

    private fun labelPadding(
        start: Dp = 0.dp,
        top: Dp = 0.dp,
        end: Dp = 0.dp,
        bottom: Dp = LabelBottomPadding,
    ): PaddingValues = PaddingValues(start, top, end, bottom)

    private fun supportingTextPadding(
        start: Dp = 0.dp,
        top: Dp = SupportingTopPadding,
        end: Dp = TextFieldHorizontalPadding,
        bottom: Dp = 0.dp,
    ): PaddingValues = PaddingValues(start, top, end, bottom)

    @Composable
    private fun leadingIconPadding(
        start: Dp = HorizontalIconPadding,
        top: Dp = 0.dp,
        end: Dp = 0.dp,
        bottom: Dp = 0.dp,
    ): PaddingValues = PaddingValues(start, top, end, bottom)

    @Composable
    private fun trailingIconPadding(
        start: Dp = 0.dp,
        top: Dp = 0.dp,
        end: Dp = HorizontalIconPadding,
        bottom: Dp = 0.dp,
    ): PaddingValues = PaddingValues(start, top, end, bottom)

    @Composable
    fun containerBorderThickness(
        interactionSource: InteractionSource,
    ): Dp {
        val focused by interactionSource.collectIsFocusedAsState()

        return if (focused) FocusedOutlineThickness else UnfocusedOutlineThickness
    }

    @Composable
    fun DecorationBox(
        value: String,
        innerTextField: @Composable () -> Unit,
        enabled: Boolean,
        visualTransformation: VisualTransformation,
        interactionSource: InteractionSource,
        isError: Boolean = false,
        label: @Composable (() -> Unit)? = null,
        placeholder: @Composable (() -> Unit)? = null,
        leadingIcon: @Composable (() -> Unit)? = null,
        trailingIcon: @Composable (() -> Unit)? = null,
        prefix: @Composable (() -> Unit)? = null,
        suffix: @Composable (() -> Unit)? = null,
        supportingText: @Composable (() -> Unit)? = null,
        shape: Shape = Shape,
        colors: TextFieldColors = colors(),
        container: @Composable () -> Unit = {
            ContainerBox(enabled, isError, interactionSource, colors, shape = shape)
        },
    ) {
        CommonDecorationBox(
            value = value,
            innerTextField = innerTextField,
            visualTransformation = visualTransformation,
            placeholder = placeholder,
            label = label,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            prefix = prefix,
            suffix = suffix,
            supportingText = supportingText,
            enabled = enabled,
            isError = isError,
            interactionSource = interactionSource,
            colors = colors,
            contentPadding = contentPadding(),
            labelPadding = labelPadding(),
            supportingTextPadding = supportingTextPadding(),
            leadingIconPadding = leadingIconPadding(),
            trailingIconPadding = trailingIconPadding(),
            container = container,
        )
    }

    @Composable
    fun ContainerBox(
        enabled: Boolean,
        isError: Boolean,
        interactionSource: InteractionSource,
        colors: TextFieldColors,
        modifier: Modifier = Modifier,
        shape: Shape = Shape,
        borderThickness: Dp = containerBorderThickness(interactionSource),
    ) {
        Box(
            modifier
                .background(colors.containerColor(enabled, isError, interactionSource).value, shape)
                .containerOutline(enabled, isError, interactionSource, colors, borderThickness, shape),
        )
    }

    @Composable
    fun colors(): TextFieldColors = TextFieldColors(
        focusedTextColor = TripleTriadTheme.colors.text,
        unfocusedTextColor = TripleTriadTheme.colors.text,
        disabledTextColor = TripleTriadTheme.colors.onDisabled,
        errorTextColor = TripleTriadTheme.colors.text,
        focusedContainerColor = TripleTriadTheme.colors.transparent,
        unfocusedContainerColor = TripleTriadTheme.colors.transparent,
        disabledContainerColor = TripleTriadTheme.colors.transparent,
        errorContainerColor = TripleTriadTheme.colors.transparent,
        cursorColor = TripleTriadTheme.colors.primary,
        errorCursorColor = TripleTriadTheme.colors.error,
        textSelectionColors = LocalTextSelectionColors.current,
        focusedOutlineColor = TripleTriadTheme.colors.primary,
        unfocusedOutlineColor = TripleTriadTheme.colors.secondary,
        disabledOutlineColor = TripleTriadTheme.colors.disabled,
        errorOutlineColor = TripleTriadTheme.colors.error,
        focusedLeadingIconColor = TripleTriadTheme.colors.primary,
        unfocusedLeadingIconColor = TripleTriadTheme.colors.primary,
        disabledLeadingIconColor = TripleTriadTheme.colors.onDisabled,
        errorLeadingIconColor = TripleTriadTheme.colors.primary,
        focusedTrailingIconColor = TripleTriadTheme.colors.primary,
        unfocusedTrailingIconColor = TripleTriadTheme.colors.primary,
        disabledTrailingIconColor = TripleTriadTheme.colors.onDisabled,
        errorTrailingIconColor = TripleTriadTheme.colors.primary,
        focusedLabelColor = TripleTriadTheme.colors.primary,
        unfocusedLabelColor = TripleTriadTheme.colors.primary,
        disabledLabelColor = TripleTriadTheme.colors.textDisabled,
        errorLabelColor = TripleTriadTheme.colors.error,
        focusedPlaceholderColor = TripleTriadTheme.colors.textSecondary,
        unfocusedPlaceholderColor = TripleTriadTheme.colors.textSecondary,
        disabledPlaceholderColor = TripleTriadTheme.colors.textDisabled,
        errorPlaceholderColor = TripleTriadTheme.colors.textSecondary,
        focusedSupportingTextColor = TripleTriadTheme.colors.primary,
        unfocusedSupportingTextColor = TripleTriadTheme.colors.primary,
        disabledSupportingTextColor = TripleTriadTheme.colors.textDisabled,
        errorSupportingTextColor = TripleTriadTheme.colors.error,
        focusedPrefixColor = TripleTriadTheme.colors.primary,
        unfocusedPrefixColor = TripleTriadTheme.colors.primary,
        disabledPrefixColor = TripleTriadTheme.colors.onDisabled,
        errorPrefixColor = TripleTriadTheme.colors.primary,
        focusedSuffixColor = TripleTriadTheme.colors.primary,
        unfocusedSuffixColor = TripleTriadTheme.colors.primary,
        disabledSuffixColor = TripleTriadTheme.colors.onDisabled,
        errorSuffixColor = TripleTriadTheme.colors.primary,
    )
}
