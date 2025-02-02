package me.tbsten.tripleTriad.ui.feature.example.localDbUserList.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import me.tbsten.tripleTriad.domain.example.user.User
import me.tbsten.tripleTriad.domain.example.user.UserId
import me.tbsten.tripleTriad.ui.PreviewRoot
import me.tbsten.tripleTriad.ui.error.handleUiEvent

@Composable
internal fun UserListItem(
    user: User,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(Modifier.weight(1f)) {
            Text("${user.uid}", fontSize = 14.sp)
            Text(user.name ?: "<none>", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
        IconButton(onClick = handleUiEvent(onDelete)) {
            Icon(Icons.Default.Delete, contentDescription = "削除")
        }
    }
}

@Preview
@Composable
private fun UserListItemPreview() = PreviewRoot {
    UserListItem(
        user = User(
            uid = UserId(123),
            name = "test",
        ),
        onDelete = { },
    )
}
