package cz.cuni.mff.ufal.translator.ui.settings.viewmodel

import android.speech.tts.TextToSpeech
import cz.cuni.mff.ufal.translator.interactors.preferences.data.AudioSpeechRecognizerSetting
import cz.cuni.mff.ufal.translator.interactors.preferences.data.DarkModeSetting
import cz.cuni.mff.ufal.translator.interactors.tts.TextToSpeechWrapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * @author Tomas Krabac
 */
class PreviewSettingsViewModel : ISettingsViewModel {

    override val agreeWithDataCollection = MutableStateFlow(true)
    override fun saveAgreementDataCollection(agree: Boolean) {}

    override val useNetworkTTS = MutableStateFlow(true)
    override fun saveUseNetworkTTS(useOnlineVersion: Boolean) {}

    override val selectedTtsEngine = MutableStateFlow(TextToSpeechWrapper.DEFAULT_TTS_ENGINE)
    override val engines = MutableStateFlow(emptyList<TextToSpeech.EngineInfo>())

    override fun saveTtsEngine(engine: String) {}

    override val organizationName = MutableStateFlow("organization")
    override fun saveOrganizationName(organizationName: String) {}

    override val darkModeSetting = MutableStateFlow(DarkModeSetting.System)
    override fun saveDarkModeSetting(darkModeSetting: DarkModeSetting) {}

    override val audioSpeechRecognizerSetting = MutableStateFlow(AudioSpeechRecognizerSetting.Google)

    override fun saveAudioSpeechRecognizerSetting(value: AudioSpeechRecognizerSetting) {}
}