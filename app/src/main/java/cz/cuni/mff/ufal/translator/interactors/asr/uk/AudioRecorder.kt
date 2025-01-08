package cz.cuni.mff.ufal.translator.interactors.asr.uk

import android.annotation.SuppressLint
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import javax.inject.Inject

class AudioRecorder @Inject constructor(
    private val scope: CoroutineScope,
) : IAudioRecorder {

    private var audioRecord: AudioRecord? = null
    private var recordingJob: Job? = null
    private val _recordingState = MutableStateFlow<RecordingState>(RecordingState.Idle)
    override val state = _recordingState.asStateFlow()

    // Buffer velikost a konfigurace
    private val sampleRate = 44100
    private val channelConfig = AudioFormat.CHANNEL_IN_MONO
    private val audioFormat = AudioFormat.ENCODING_PCM_16BIT
    private val bufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat)

    private val audioBuffer = ByteArray(bufferSize)
    private val outputStream = ByteArrayOutputStream()

    @SuppressLint("MissingPermission")
    override fun startRecord() {
        if (audioRecord != null && _recordingState.value is RecordingState.Recording) {
            throw IllegalStateException("Recording is already in progress!")
        }

        // Inicializace AudioRecord
        audioRecord = AudioRecord(
            MediaRecorder.AudioSource.MIC,
            16000, // Frekvence vzorkování
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            bufferSize
        )

        audioRecord?.startRecording()
        _recordingState.value = RecordingState.Recording

        recordingJob = scope.launch(Dispatchers.IO) {
            try {
                while (isActive) {
                    val bytesRead = audioRecord?.read(audioBuffer, 0, audioBuffer.size) ?: 0
                    if (bytesRead > 0) {
                        synchronized(outputStream) {
                            outputStream.write(audioBuffer, 0, bytesRead)
                        }
                    }
                }
            } catch (e: Exception) {
                _recordingState.value = RecordingState.Error(e.message ?: "Unknown error")
            }
        }
    }

    override fun stopRecord() {
        if (_recordingState.value !is RecordingState.Recording) {
            throw IllegalStateException("Recording is not in progress!")
        }

        recordingJob?.cancel()
        audioRecord?.stop()
        audioRecord?.release()
        audioRecord = null

        synchronized(outputStream) {
            _recordingState.value = RecordingState.Completed(outputStream.toByteArray())
            outputStream.reset()
        }
    }
}

sealed class RecordingState {
    object Idle : RecordingState()
    object Recording : RecordingState()
    data class Completed(val data: ByteArray) : RecordingState()
    data class Error(val message: String) : RecordingState()
}