package cz.cuni.mff.lindat.history.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.statusBarsPadding
import cz.cuni.mff.lindat.R
import cz.cuni.mff.lindat.base.BaseScreen
import cz.cuni.mff.lindat.history.data.HistoryItem
import cz.cuni.mff.lindat.history.viewmodel.IHistoryViewModel
import cz.cuni.mff.lindat.history.viewmodel.PreviewHistoryViewModel
import cz.cuni.mff.lindat.main.controller.IController
import cz.cuni.mff.lindat.main.controller.PreviewIController
import cz.cuni.mff.lindat.ui.common.FlagItem
import cz.cuni.mff.lindat.ui.theme.LindatTheme

/**
 * @author Tomas Krabac
 */
@Composable
fun HistoryScreen(viewModel: IHistoryViewModel, controller: IController) {
    BaseScreen(viewModel = viewModel) {
        Content(
            viewModel = viewModel,
            controller = controller,
        )
    }
}

@Composable
private fun Content(viewModel: IHistoryViewModel, controller: IController) {
    val historyItems by viewModel.historyItems.collectAsState()

    Column {
        Toolbar {
            controller.onBackPressed()
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth(),
            state = rememberLazyListState(),
        ) {
            historyItems.forEachIndexed { index, historyItem ->
                item(index) {
                    HistoryRowItem(historyItem)

                    Divider(
                        color = MaterialTheme.colors.secondary,
                    )
                }
            }
        }
    }
}

@Composable
fun Toolbar(
    onBackPressed: () -> Unit,
) {
    TopAppBar(
        backgroundColor = MaterialTheme.colors.primary,
        navigationIcon = {
            IconButton(onClick = onBackPressed) {
                Icon(
                    painter = painterResource(R.drawable.ic_back),
                    tint = MaterialTheme.colors.onPrimary,
                    contentDescription = ""
                )
            }
        },
        title = {
            Text(
                text = stringResource(id = R.string.history_title),
                color = MaterialTheme.colors.onPrimary,
            )
        },
        modifier = Modifier.statusBarsPadding(),
        elevation = 4.dp,
    )
}

@Composable
private fun HistoryRowItem(item: HistoryItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Spacer(modifier = Modifier.width(16.dp))

        FlagItem(item.inputLanguage)

        Spacer(modifier = Modifier.width(4.dp))

        Icon(
            painter = painterResource(id = R.drawable.ic_arrow_forward),
            tint = MaterialTheme.colors.primary,
            contentDescription = "",
        )

        Spacer(modifier = Modifier.width(4.dp))

        FlagItem(item.outputLanguage)

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            modifier = Modifier.weight(1f),
            text = item.text,
            color = MaterialTheme.colors.onBackground,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        FavouriteItem(item.isFavourite)

        Spacer(modifier = Modifier.width(16.dp))
    }
}


@Composable
private fun FavouriteItem(isFavourite: Boolean) {
    val iconRes = if (isFavourite) R.drawable.ic_full_star else R.drawable.ic_empty_star
    val contentDescriptionRes = if (isFavourite) R.string.add_to_favourites_cd else R.string.remove_from_favourites_cd

    Icon(
        painter = painterResource(id = iconRes),
        tint = MaterialTheme.colors.primary,
        contentDescription = stringResource(id = contentDescriptionRes),
    )
}


@Preview(showBackground = true)
@Composable
private fun HistoryScreenPreview() {
    LindatTheme {
        HistoryScreen(
            viewModel = PreviewHistoryViewModel(),
            controller = PreviewIController(),
        )
    }
}