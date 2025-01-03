package cz.cuni.mff.ufal.translator.interactors.preferences.data

import androidx.annotation.StringRes
import cz.cuni.mff.ufal.translator.R

/**
 * @author Tomas Krabac
 */
enum class AudioSpeechRecognizerSetting(
    val key: String,
    @StringRes val labelRes: Int,
) {
    Google("google", R.string.settings_asr_value_google),
    CUNI("cuni", R.string.settings_asr_value_cuni),
}