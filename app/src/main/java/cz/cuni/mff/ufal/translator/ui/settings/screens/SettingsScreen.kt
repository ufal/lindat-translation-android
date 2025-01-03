package cz.cuni.mff.ufal.translator.ui.settings.screens

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import cz.cuni.mff.ufal.translator.R
import cz.cuni.mff.ufal.translator.base.BaseScreen
import cz.cuni.mff.ufal.translator.interactors.crashlytics.Screen
import cz.cuni.mff.ufal.translator.interactors.preferences.data.AudioSpeechRecognizerSetting
import cz.cuni.mff.ufal.translator.interactors.preferences.data.DarkModeSetting
import cz.cuni.mff.ufal.translator.main.controller.IMainController
import cz.cuni.mff.ufal.translator.main.controller.PreviewIMainController
import cz.cuni.mff.ufal.translator.ui.common.widgets.BaseToolbar
import cz.cuni.mff.ufal.translator.ui.settings.screens.widgets.*
import cz.cuni.mff.ufal.translator.ui.settings.viewmodel.ISettingsViewModel
import cz.cuni.mff.ufal.translator.ui.settings.viewmodel.PreviewSettingsViewModel
import cz.cuni.mff.ufal.translator.ui.settings.viewmodel.SettingsViewModel
import cz.cuni.mff.ufal.translator.ui.theme.LindatTheme
import cz.cuni.mff.ufal.translator.ui.theme.LindatThemePreview

/**
 * @author Tomas Krabac
 */
@Composable
fun SettingsScreen(
    viewModel: ISettingsViewModel = hiltViewModel<SettingsViewModel>(),
    mainController: IMainController
) {
    BaseScreen(
        screen = Screen.Settings,
        darkModeSetting = mainController.darkModeSetting,
        viewModel = viewModel,
    ) {
        Content(
            viewModel = viewModel,
            mainController = mainController,
        )
    }
}

@Composable
private fun Content(viewModel: ISettingsViewModel, mainController: IMainController) {
    val agreeWithDataCollection by viewModel.agreeWithDataCollection.collectAsState()
    val useNetworkTTS by viewModel.useNetworkTTS.collectAsState()
    val selectedTtsEngineName by viewModel.selectedTtsEngine.collectAsState()
    val ttsEngines by viewModel.engines.collectAsState()
    val organizationName by viewModel.organizationName.collectAsState()
    val darkModeSetting by viewModel.darkModeSetting.collectAsState()
    val darkModeSettings = remember {
        listOf(
            DarkModeSetting.System,
            DarkModeSetting.Enabled,
            DarkModeSetting.Disabled,
        )
    }
    val audioSpeechRecognizerSetting by viewModel.audioSpeechRecognizerSetting.collectAsState()
    val audioSpeechRecognizerSettings = remember {
        listOf(
            AudioSpeechRecognizerSetting.Google,
            AudioSpeechRecognizerSetting.CUNI,
        )
    }

    val selectedEngine = ttsEngines.find { it.name == selectedTtsEngineName } ?: ttsEngines.firstOrNull()

    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        BaseToolbar(titleRes = R.string.settings_title) {
            mainController.onBackPressed()
        }

        SettingSwitchItem(
            titleRes = R.string.settings_data_collection_title,
            descriptionRes = R.string.settings_data_collection_description,
            isChecked = agreeWithDataCollection,

            onCheckedChange = viewModel::saveAgreementDataCollection
        )

        Spacer(modifier = Modifier.height(8.dp))

        SettingEditTextItem(
            labelRes = R.string.settings_organization_name_label,
            placeholderRes = R.string.settings_organization_name_placeholder,
            value = organizationName,
            onValueChange = viewModel::saveOrganizationName
        )

        SettingsDivider()

        if (ttsEngines.isNotEmpty()) {
            SettingSwitchItem(
                titleRes = R.string.settings_network_tts_title,
                descriptionRes = R.string.settings_network_tts_description,
                isChecked = useNetworkTTS,

                onCheckedChange = viewModel::saveUseNetworkTTS
            )

            SettingsDivider()
        }

        if (ttsEngines.size > 1 && selectedEngine != null) {
            TtsEngineSettingItem(
                selectedEngine = selectedEngine,
                ttsEngines = ttsEngines,
            ) {
                viewModel.saveTtsEngine(it.name)
            }

            SettingsDivider()
        }

        DarkModeSettingItem(
            selectedSetting = darkModeSetting,
            values = darkModeSettings,
        ) {
            viewModel.saveDarkModeSetting(it)
        }

        SettingsDivider()

        AudioSpeechRecognizerSettingItem(
            selectedSetting = audioSpeechRecognizerSetting,
            values = audioSpeechRecognizerSettings,
        ){
            viewModel.saveAudioSpeechRecognizerSetting(it)
        }

        SettingsDivider()
    }
}

@Composable
fun TtsEngineSettingItem(
    selectedEngine: TextToSpeech.EngineInfo,
    ttsEngines: List<TextToSpeech.EngineInfo>,

    saveTtsEngine: (TextToSpeech.EngineInfo) -> Unit,
) {
    val dialogState = rememberDialogState()

    SettingSingleItem(
        titleRes = R.string.settings_tts_engine_title,
        value = selectedEngine.label
    ) {
        dialogState.show()
    }

    SingleSelectDialog(
        title = stringResource(id = R.string.settings_tts_engine_title),
        optionsList = ttsEngines.map { it.label },
        selectedItem = selectedEngine.label,
        submitButtonText = stringResource(id = android.R.string.ok),
        dialogState = dialogState,
    ) { selectedIndex ->
        val engine = ttsEngines[selectedIndex]
        saveTtsEngine(engine)
    }
}

@Composable
fun DarkModeSettingItem(
    selectedSetting: DarkModeSetting,
    values: List<DarkModeSetting>,

    saveSetting: (DarkModeSetting) -> Unit,
) {
    val dialogState = rememberDialogState()

    SettingSingleItem(
        titleRes = R.string.settings_dark_mode_title,
        value = stringResource(id = selectedSetting.labelRes)
    ) {
        dialogState.show()
    }

    SingleSelectDialog(
        title = stringResource(id = R.string.settings_dark_mode_title),
        optionsList = values.map { stringResource(id = it.labelRes) },
        selectedItem = stringResource(id = selectedSetting.labelRes),
        submitButtonText = stringResource(id = android.R.string.ok),
        dialogState = dialogState,
    ) { selectedIndex ->
        val selectedItem = values[selectedIndex]
        saveSetting(selectedItem)
    }
}

@Composable
fun AudioSpeechRecognizerSettingItem(
    selectedSetting: AudioSpeechRecognizerSetting,
    values: List<AudioSpeechRecognizerSetting>,

    saveSetting: (AudioSpeechRecognizerSetting) -> Unit,
) {
    val dialogState = rememberDialogState()

    SettingSingleItem(
        titleRes = R.string.settings_asr_title,
        value = stringResource(id = selectedSetting.labelRes)
    ) {
        dialogState.show()
    }

    SingleSelectDialog(
        title = stringResource(id = R.string.settings_asr_title),
        optionsList = values.map { stringResource(id = it.labelRes) },
        selectedItem = stringResource(id = selectedSetting.labelRes),
        submitButtonText = stringResource(id = android.R.string.ok),
        dialogState = dialogState,
    ) { selectedIndex ->
        val selectedItem = values[selectedIndex]
        saveSetting(selectedItem)
    }
}

@Composable
private fun SettingsDivider() {
    Spacer(modifier = Modifier.height(16.dp))

    Divider(color = LindatTheme.colors.primary)
}

@Preview(showBackground = true)
@Composable
private fun SettingsScreenPreview() {
    LindatThemePreview {
        SettingsScreen(
            viewModel = PreviewSettingsViewModel(),
            mainController = PreviewIMainController(),
        )
    }
}



