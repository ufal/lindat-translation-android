package cz.cuni.mff.ufal.translator.interactors.asr.uk

import kotlinx.coroutines.flow.StateFlow

interface IAudioRecorder {

    val state: StateFlow<RecordingState>

    fun startRecord()

    fun stopRecord()

}