package your.projectPackage.ui.feature.example.localDbUserList.component

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
import your.projectPackage.domain.example.user.User
import your.projectPackage.domain.example.user.UserId
import your.projectPackage.ui.PreviewRoot
import your.projectPackage.ui.error.handleUiEvent

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
