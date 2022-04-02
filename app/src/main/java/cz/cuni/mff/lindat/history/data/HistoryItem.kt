package cz.cuni.mff.lindat.history.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import cz.cuni.mff.lindat.main.viewmodel.Language

/**
 * @author Tomas Krabac
 */
@Entity(tableName = "history_items", primaryKeys = ["text", "input_language", "output_language"])
data class HistoryItem(
    @ColumnInfo(name = "text") val text: String,
    @ColumnInfo(name = "input_language") val inputLanguage: Language,
    @ColumnInfo(name = "output_language") val outputLanguage: Language,
    @ColumnInfo(name = "is_favourite") val isFavourite: Boolean = false,
)