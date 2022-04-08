package cz.cuni.mff.ufal.translator.ui.history.screens.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cz.cuni.mff.ufal.translator.R
import cz.cuni.mff.ufal.translator.ui.theme.LindatTheme
import cz.cuni.mff.ufal.translator.ui.translations.screens.widgets.ActionItem

/**
 * @author Tomas Krabac
 */

@Composable
fun ActionsRow(
    isTextToSpeechAvailable: Boolean,
    isFavourite: Boolean,

    onFavouriteClicked: () -> Unit,
    copyToClipBoard: () -> Unit,
    textToSpeech: () -> Unit,
) {


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.background)
            .padding(horizontal = 16.dp),
    ) {
        Row(modifier = Modifier.align(Alignment.CenterStart)) {
            FavouriteItem(isFavourite) {
                onFavouriteClicked()
            }
        }

        Row(modifier = Modifier.align(Alignment.CenterEnd)) {

            ActionItem(
                drawableRes = R.drawable.ic_copy,
                contentDescriptionRes = R.string.copy_to_clipboard_cd
            ) {
                copyToClipBoard()
            }

            if (isTextToSpeechAvailable) {
                ActionItem(
                    drawableRes = R.drawable.ic_tts,
                    contentDescriptionRes = R.string.tts_cd
                ) {
                    textToSpeech()
                }
            }
        }
    }
}

@Composable
private fun FavouriteItem(isFavourite: Boolean, onClick: () -> Unit) {
    val iconRes = if (isFavourite) R.drawable.ic_full_star else R.drawable.ic_empty_star
    val contentDescriptionRes = if (isFavourite) R.string.add_to_favourites_cd else R.string.remove_from_favourites_cd

    IconButton(
        onClick = onClick,
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            tint = MaterialTheme.colors.primary,
            contentDescription = stringResource(id = contentDescriptionRes),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ActionsRowPreview() {
    LindatTheme {
        ActionsRow(
            isTextToSpeechAvailable = true,
            isFavourite = false,

            onFavouriteClicked = {},
            copyToClipBoard = {},
            textToSpeech = {},
        )
    }
}