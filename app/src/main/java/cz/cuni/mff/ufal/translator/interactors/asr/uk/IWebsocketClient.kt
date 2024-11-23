package cz.cuni.mff.ufal.translator.interactors.asr.uk

import kotlinx.coroutines.flow.Flow

interface IWebsocketClient {
    fun getStateStream(): Flow<Pair<String, Boolean>>
    suspend fun sendAudio(audioData: ByteArray)
    suspend fun close()
}