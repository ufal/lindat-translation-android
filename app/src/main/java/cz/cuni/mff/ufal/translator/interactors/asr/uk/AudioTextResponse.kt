package cz.cuni.mff.ufal.translator.interactors.asr.uk

data class AudioTextResponse(
    val text: String,
    val is_final: Boolean
)