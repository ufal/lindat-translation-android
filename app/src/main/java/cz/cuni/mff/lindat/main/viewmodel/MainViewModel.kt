package cz.cuni.mff.lindat.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.michaeltroger.latintocyrillic.Alphabet
import com.michaeltroger.latintocyrillic.LatinCyrillicFactory
import cz.cuni.mff.lindat.api.Api
import cz.cuni.mff.lindat.api.IApi
import cz.cuni.mff.lindat.extensions.logE
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

/**
 * @author Tomas Krabac
 */
class MainViewModel : IMainViewModel, ViewModel() {

    private val api: IApi = Api()
    private var job: Job? = null

    private var lastRequestMs = 0L
    private var minIntervalMS = 500

    private val latinCyrillic = LatinCyrillicFactory.create(Alphabet.UkrainianIso9)

    override val inputText = MutableStateFlow("")
    override val outputText = MutableStateFlow("")
    override val outputTextLatin = MutableStateFlow("")
    override val inputLanguage = MutableStateFlow(Language.Czech)
    override val outputLanguage = MutableStateFlow(Language.Ukrainian)


    override fun setInputText(text: String) {
        inputText.value = text
        translate()
    }

    override fun setInputLanguage(language: Language) {
        inputLanguage.value = language
    }

    override fun swapLanguages() {
        val tmpInputLanguage = inputLanguage.value
        inputLanguage.value = outputLanguage.value
        outputLanguage.value = tmpInputLanguage

        outputText.value = ""
        translate()
    }

    private fun translate() {
        if (inputText.value.isBlank()) {
            job?.cancel()
            outputText.value = ""
            outputTextLatin.value = ""
            return
        }

        val currentTime = System.currentTimeMillis()
        if (currentTime - lastRequestMs < minIntervalMS) {
            return
        }

        job?.cancel()
        job = viewModelScope.launch {
            api.translate(
                inputLanguage = inputLanguage.value,
                outputLanguage = outputLanguage.value,
                text = inputText.value.trim()
            ).onSuccess {
                lastRequestMs = System.currentTimeMillis()
                outputText.value = it
                outputTextLatin.value = if (outputLanguage.value == Language.Ukrainian) {
                    latinCyrillic.cyrillicToLatin(it)
                } else {
                    ""
                }

            }.onFailure {
                logE("error $it")
            }
        }
    }
}