package cz.cuni.mff.ufal.translator.interactors.preferences

import cz.cuni.mff.ufal.translator.interactors.preferences.data.AudioSpeechRecognizerSetting
import cz.cuni.mff.ufal.translator.interactors.preferences.data.DarkModeSetting
import cz.cuni.mff.ufal.translator.ui.translations.models.Language
import kotlinx.coroutines.flow.Flow

/**
 * @author Tomas Krabac
 */
interface IUserDataStore {

    suspend fun setFinishedOnboarding()

    val hasFinishedOnboarding: Flow<Boolean>

    suspend fun saveAgreementDataCollection(agree: Boolean)

    val agreeWithDataCollection: Flow<Boolean>

    val useNetworkTTS: Flow<Boolean>

    suspend fun saveUseNetworkTTS(useOnlineVersion: Boolean)

    val ttsEngine: Flow<String>

    suspend fun saveTTSengine(engine: String)

    val organizationName: Flow<String>

    suspend fun saveOrganizationName(organizationName: String)

    val darkModeSetting: Flow<DarkModeSetting>

    suspend fun saveDarkModeSetting(value: DarkModeSetting)

    val lastInputLanguage: Flow<Language>

    val lastOutputLanguage: Flow<Language>

    suspend fun setLastInputLanguage(language: Language)

    suspend fun setLastOutputLanguage(language: Language)

    val audioSpeechRecognizerSetting: Flow<AudioSpeechRecognizerSetting>

    suspend fun saveAudioSpeechRecognizerSetting(value: AudioSpeechRecognizerSetting)
}