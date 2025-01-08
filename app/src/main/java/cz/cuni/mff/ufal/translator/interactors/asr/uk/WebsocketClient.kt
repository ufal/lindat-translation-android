package cz.cuni.mff.ufal.translator.interactors.asr.uk

import android.util.Log
import cz.cuni.mff.ufal.translator.BuildConfig
import cz.cuni.mff.ufal.translator.extensions.logD
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.url
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.serialization.json.Json
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.inject.Inject

class WebsocketClient @Inject constructor() : IWebsocketClient {

    private val jsonDecoder = Json { ignoreUnknownKeys = true }

    private val client = HttpClient(CIO) {
        install(WebSockets)
        if (BuildConfig.DEBUG) {
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Log.d("WEBSOCKET:", message)
                    }
                }
                level = LogLevel.ALL
            }
        }
    }

    private var session: WebSocketSession? = null

    override fun getStateStream(): Flow<Pair<String, Boolean>> = flow {
        session = client.webSocketSession {
            url("wss://lindat.cz/services/cunispeech/socket.io:8082")
        }

        val messageStates = session!!.incoming
            .consumeAsFlow()
            .filterIsInstance<Frame.Text>()
            .mapNotNull {
                val receivedText = it.readText()
                logD("received message: $receivedText")
                parseBackendResponseSafe(receivedText)
            }

        emitAll(messageStates)
    }

    private fun parseBackendResponseSafe(json: String): Pair<String, Boolean>? {
        if(json == "Done!"){
            return null
        }

        return try {
            val response = jsonDecoder.decodeFromString<AudioTextResponse>(json)
            Pair(response.text, response.is_final)
        } catch (e: Throwable) {
            logD("Error parsing message: $e")
            null
        }
    }

    override suspend fun sendAudio(audioData: ByteArray) {
        logD("Sending audio")

        val samplesPerMessage = 8000 // 0.2 seconds at 16 kHz
        val secondsPerMessage = 0.2f

        // Convert ByteArray to FloatArray
        val shortBuffer = ByteBuffer.wrap(audioData)
            .order(ByteOrder.LITTLE_ENDIAN)
            .asShortBuffer()

        val floatSamples = FloatArray(shortBuffer.capacity()) { i ->
            shortBuffer.get(i) / 32768f
        }

        var start = 0
        while (start < floatSamples.size) {
            val end = (start + samplesPerMessage).coerceAtMost(floatSamples.size)
            val chunk = floatSamples.sliceArray(start until end)
            val byteBuffer = ByteBuffer.allocate(chunk.size * 4).order(ByteOrder.LITTLE_ENDIAN)
            chunk.forEach { byteBuffer.putFloat(it) }

            session?.outgoing?.send(Frame.Binary(true, byteBuffer.array()))
            delay((secondsPerMessage * 1000).toLong())
            start += samplesPerMessage
        }

        session?.outgoing?.send(Frame.Text("Done"))
        logD("Audio sent successfully.")
    }

    override suspend fun close() {
        session?.close()
        session = null
        logD("WebSocket session closed.")
    }
}
