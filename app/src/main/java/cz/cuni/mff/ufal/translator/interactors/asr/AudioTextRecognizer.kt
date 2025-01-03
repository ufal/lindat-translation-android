@file:OptIn(ExperimentalCoroutinesApi::class)

package cz.cuni.mff.ufal.translator.interactors.asr

import cz.cuni.mff.ufal.translator.interactors.asr.google.GoogleAudioTextRecognizer
import cz.cuni.mff.ufal.translator.interactors.asr.uk.CharlesAudioTextRecognizer
import cz.cuni.mff.ufal.translator.interactors.preferences.IUserDataStore
import cz.cuni.mff.ufal.translator.interactors.preferences.data.AudioSpeechRecognizerSetting
import cz.cuni.mff.ufal.translator.ui.translations.models.Language
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class AudioTextRecognizer @Inject constructor(
    cuniAudioTextRecognizer: CharlesAudioTextRecognizer,
    googleAudioTextRecognizer: GoogleAudioTextRecognizer,
    userDataStore: IUserDataStore,
    scope: CoroutineScope,
) : IAudioTextRecognizer {

    private var selectedRecognizer = userDataStore.audioSpeechRecognizerSetting.map {
        if (it == AudioSpeechRecognizerSetting.Google) {
            googleAudioTextRecognizer
        } else {
            cuniAudioTextRecognizer
        }
    }.stateIn(scope, SharingStarted.WhileSubscribed(), googleAudioTextRecognizer)

    override val rmsdB = selectedRecognizer.flatMapLatest { it.rmsdB }.stateIn(scope, SharingStarted.WhileSubscribed(), 0f)

    override val isListening = selectedRecognizer.flatMapLatest { it.isListening }.stateIn(scope, SharingStarted.WhileSubscribed(), false)

    override val text = selectedRecognizer.flatMapLatest { it.text }.stateIn(scope, SharingStarted.WhileSubscribed(), "")

    override val activeLanguage = selectedRecognizer.flatMapLatest { it.activeLanguage }.stateIn(scope, SharingStarted.WhileSubscribed(), Language.Czech)

    override fun startRecognize(language: Language) {
        selectedRecognizer.value.startRecognize(language)
    }

    override fun stopRecognize() {
        selectedRecognizer.value.stopRecognize()
    }

    override fun clear() {
        selectedRecognizer.value.clear()
    }
}