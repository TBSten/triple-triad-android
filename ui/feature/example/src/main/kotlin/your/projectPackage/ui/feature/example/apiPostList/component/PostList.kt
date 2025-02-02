package your.projectPackage.ui.feature.example.apiPostList.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import your.projectPackage.domain.example.post.Post
import your.projectPackage.domain.example.post.PostId
import your.projectPackage.domain.example.post.UserId
import your.projectPackage.ui.PreviewRoot
import your.projectPackage.ui.ValuesPreviewParameterProvider

@Composable
internal fun PostList(
    posts: List<Post>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier) {
        if (posts.isEmpty()) {
            item {
                EmptyListItem()
            }
        } else {
            items(posts) { post ->
                PostItem(post = post)
            }
        }
    }
}

@Composable
private fun EmptyListItem(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        Text(
            "No Post",
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun PostItem(
    post: Post,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .clickable { },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Text(
                post.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                post.body.take(30),
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
            )
            Text("test")
        }
        HorizontalDivider()
    }
}

private class PostListPreviewParameters :
    ValuesPreviewParameterProvider<List<Post>>(
        emptyList(),
        listOf(
            Post(
                id = PostId(123),
                userId = UserId(456),
                title = "Preview Post 1",
                body = "This is a Preview Post 1.",
            ),
            Post(
                id = PostId(123),
                userId = UserId(456),
                title = "Very Long Title Preview Post".repeat(5),
                body = "This is a Preview Post 1. ".repeat(100),
            ),
        ),
        List(10) {
            Post(
                id = PostId(12300 + it),
                userId = UserId(456 + it / 3),
                title = "Preview Post $it",
                body = "This is a Preview Post $it.",
            )
        },
    )

@Preview(showBackground = true)
@Composable
private fun PostListPreview(
    @PreviewParameter(PostListPreviewParameters::class)
    posts: List<Post>,
) = PreviewRoot {
    PostList(
        posts = posts,
    )
}

@Preview(showBackground = true)
@Composable
private fun PostListItemPreview() = PreviewRoot {
    PostItem(
        post = Post(
            id = PostId(123),
            userId = UserId(456),
            title = "Preview Post",
            body = "This is a Preview Post.",
        ),
    )
}
