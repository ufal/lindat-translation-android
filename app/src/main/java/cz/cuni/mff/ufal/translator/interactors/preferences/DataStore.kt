package cz.cuni.mff.ufal.translator.interactors.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import cz.cuni.mff.ufal.translator.interactors.languages.LanguagesManager
import cz.cuni.mff.ufal.translator.interactors.languages.LanguagesManager.Companion.DEFAULT_INPUT_LANGUAGE
import cz.cuni.mff.ufal.translator.interactors.languages.LanguagesManager.Companion.DEFAULT_OUTPUT_LANGUAGE
import cz.cuni.mff.ufal.translator.interactors.preferences.data.AudioSpeechRecognizerSetting
import cz.cuni.mff.ufal.translator.interactors.preferences.data.DarkModeSetting
import cz.cuni.mff.ufal.translator.interactors.tts.TextToSpeechWrapper.Companion.DEFAULT_TTS_ENGINE
import cz.cuni.mff.ufal.translator.ui.settings.screens.AudioSpeechRecognizerSettingItem
import cz.cuni.mff.ufal.translator.ui.translations.models.Language
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * @author Tomas Krabac
 */

class UserDataStore(private val context: Context) : IUserDataStore {

    private val Context.userDataStore: DataStore<Preferences> by preferencesDataStore(name = "user_settings")

    companion object {
        val HAS_FINISHED_ONBOARDING = booleanPreferencesKey("HAS_FINISHED_ONBOARDING")
        val AGREE_WITH_DATA_COLLECTION = booleanPreferencesKey("AGREE_WITH_DATA_COLLECTION")
        val USE_NETWORK_TTS = booleanPreferencesKey("USE_NETWORK_TTS")
        val TTS_ENGINE = stringPreferencesKey("TTS_ENGINE")
        val ORGANIZATION_NAME = stringPreferencesKey("ORGANIZATION_NAME")
        val DARK_MODE_SETTINGS = stringPreferencesKey("DARK_MODE_SETTINGS")
        val LAST_INPUT_LANGUAGE = stringPreferencesKey("LAST_INPUT_LANGUAGE")
        val LAST_OUTPUT_LANGUAGE = stringPreferencesKey("LAST_OUTPUT_LANGUAGE")
        val AUDIO_SPEECH_RECOGNIZER_SETTINGS = stringPreferencesKey("AUDIO_SPEECH_RECOGNIZER_SETTINGS")
    }

    override suspend fun setFinishedOnboarding() {
        context.userDataStore.edit {
            it[HAS_FINISHED_ONBOARDING] = true
        }
    }

    override val hasFinishedOnboarding = context.userDataStore.data.map {
        it[HAS_FINISHED_ONBOARDING] ?: false
    }

    override suspend fun saveAgreementDataCollection(agree: Boolean) {
        context.userDataStore.edit {
            it[AGREE_WITH_DATA_COLLECTION] = agree
        }
    }

    override val agreeWithDataCollection = context.userDataStore.data.map {
        it[AGREE_WITH_DATA_COLLECTION] ?: false
    }

    override val useNetworkTTS = context.userDataStore.data.map {
        it[USE_NETWORK_TTS] ?: true
    }

    override suspend fun saveUseNetworkTTS(useOnlineVersion: Boolean) {
        context.userDataStore.edit {
            it[USE_NETWORK_TTS] = useOnlineVersion
        }
    }

    override val ttsEngine = context.userDataStore.data.map {
        it[TTS_ENGINE] ?: DEFAULT_TTS_ENGINE
    }

    override suspend fun saveTTSengine(engine: String) {
        context.userDataStore.edit {
            it[TTS_ENGINE] = engine
        }
    }

    override val organizationName = context.userDataStore.data.map {
        it[ORGANIZATION_NAME] ?: ""
    }

    override suspend fun saveOrganizationName(organizationName: String) {
        context.userDataStore.edit {
            it[ORGANIZATION_NAME] = organizationName
        }
    }

    override val darkModeSetting = context.userDataStore.data.map {
        when (it[DARK_MODE_SETTINGS]) {
            DarkModeSetting.System.key -> DarkModeSetting.System
            DarkModeSetting.Enabled.key -> DarkModeSetting.Enabled
            DarkModeSetting.Disabled.key -> DarkModeSetting.Disabled
            else -> DarkModeSetting.System
        }
    }

    override suspend fun saveDarkModeSetting(value: DarkModeSetting) {
        context.userDataStore.edit {
            it[DARK_MODE_SETTINGS] = value.key
        }
    }

    override val lastInputLanguage = context.userDataStore.data.map {
        LanguagesManager.getLanguage(it[LAST_INPUT_LANGUAGE] ?: DEFAULT_INPUT_LANGUAGE.code) ?: DEFAULT_INPUT_LANGUAGE
    }

    override val lastOutputLanguage = context.userDataStore.data.map {
        LanguagesManager.getLanguage(it[LAST_OUTPUT_LANGUAGE] ?: DEFAULT_OUTPUT_LANGUAGE.code) ?: DEFAULT_OUTPUT_LANGUAGE
    }

    override suspend fun setLastInputLanguage(language: Language) {
        context.userDataStore.edit {
            it[LAST_INPUT_LANGUAGE] = language.code
        }
    }

    override suspend fun setLastOutputLanguage(language: Language) {
        context.userDataStore.edit {
            it[LAST_OUTPUT_LANGUAGE] = language.code
        }
    }

    override val audioSpeechRecognizerSetting = context.userDataStore.data.map {
        when (it[AUDIO_SPEECH_RECOGNIZER_SETTINGS]) {
            AudioSpeechRecognizerSetting.Google.key  -> AudioSpeechRecognizerSetting.Google
            AudioSpeechRecognizerSetting.CUNI.key  -> AudioSpeechRecognizerSetting.CUNI
            else ->  AudioSpeechRecognizerSetting.Google
        }
    }

    override suspend fun saveAudioSpeechRecognizerSetting(value: AudioSpeechRecognizerSetting) {
        context.userDataStore.edit {
            it[AUDIO_SPEECH_RECOGNIZER_SETTINGS] = value.key
        }
    }
}
