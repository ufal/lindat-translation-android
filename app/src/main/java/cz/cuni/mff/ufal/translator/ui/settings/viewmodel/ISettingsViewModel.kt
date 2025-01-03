package cz.cuni.mff.ufal.translator.ui.settings.viewmodel

import android.speech.tts.TextToSpeech
import cz.cuni.mff.ufal.translator.base.IBaseViewModel
import cz.cuni.mff.ufal.translator.interactors.preferences.data.AudioSpeechRecognizerSetting
import cz.cuni.mff.ufal.translator.interactors.preferences.data.DarkModeSetting
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

/**
 * @author Tomas Krabac
 */
interface ISettingsViewModel: IBaseViewModel {

    val agreeWithDataCollection: StateFlow<Boolean>

    fun saveAgreementDataCollection(agree: Boolean)

    val useNetworkTTS: StateFlow<Boolean>

    fun saveUseNetworkTTS(useOnlineVersion: Boolean)

    val engines: StateFlow<List<TextToSpeech.EngineInfo>>

    val selectedTtsEngine: StateFlow<String>

    fun saveTtsEngine(engine: String)

    val organizationName: StateFlow<String>

    fun saveOrganizationName(organizationName: String)

    val darkModeSetting: StateFlow<DarkModeSetting>

    fun saveDarkModeSetting(value: DarkModeSetting)

    val audioSpeechRecognizerSetting: StateFlow<AudioSpeechRecognizerSetting>

    fun saveAudioSpeechRecognizerSetting(value: AudioSpeechRecognizerSetting)

}