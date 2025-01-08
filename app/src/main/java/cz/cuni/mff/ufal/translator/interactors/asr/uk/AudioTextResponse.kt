package cz.cuni.mff.ufal.translator.interactors.asr.uk

import kotlinx.serialization.Serializable

@Serializable
data class AudioTextResponse(
    val text: String,
    val is_final: Boolean
)