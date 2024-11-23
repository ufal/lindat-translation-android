package cz.cuni.mff.ufal.translator.interactors.asr.uk

import cz.cuni.mff.ufal.translator.extensions.logD
import cz.cuni.mff.ufal.translator.interactors.asr.IAudioTextRecognizer
import cz.cuni.mff.ufal.translator.ui.translations.models.Language
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class CharlesAudioTextRecognizer @Inject constructor(
    private val webSocketClient: IWebsocketClient,
    private val audioRecorder: IAudioRecorder,
    private val scope: CoroutineScope,
) : IAudioTextRecognizer {
    private var finalText = ""

    override val rmsdB = MutableStateFlow(0.0f)
    override val isListening = MutableStateFlow(false)
    override val text = MutableStateFlow("")
    override var activeLanguage = MutableStateFlow(Language.Czech)

    override fun startRecognize(language: Language) {
        if (isListening.value) {
            return
        }

        activeLanguage.value = language
        isListening.value = true
        text.value = ""
        finalText = ""

        webSocketClient.getStateStream().onEach {
            logD("received $it")

            text.value = finalText + it.first

            if (it.second) {
                finalText = finalText + it.first
            }

        }.launchIn(scope)

        audioRecorder.state.onEach { state ->
            logD("state $state")
            if (state is RecordingState.Completed) {
                webSocketClient.sendAudio(state.data)
            }

        }.launchIn(scope)


        scope.launch {
            logD("start record")
            audioRecorder.startRecord()
        }

    }

    override fun stopRecognize() {
        if (!isListening.value) {
            return
        }

        isListening.value = false
        audioRecorder.stopRecord()
    }

    override fun clear() {
        stopRecognize()
        scope.launch {
            webSocketClient.close()
        }

    }
}