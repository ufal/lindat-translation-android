package cz.cuni.mff.ufal.translator.interactors.db

import androidx.room.*
import cz.cuni.mff.ufal.translator.ui.history.model.HistoryItem
import kotlinx.coroutines.flow.Flow

/**
 * @author Tomas Krabac
 */
@Dao
interface IHistoryDao {
    @Query(
        "SELECT * " +
                "FROM history_items " +
                "ORDER BY inserted_ms DESC"
    )
    fun getAll(): Flow<List<HistoryItem>>

    @Query(
        "SELECT * " +
                "FROM history_items " +
                "WHERE is_favourite = 1 " +
                "ORDER BY inserted_ms DESC "
    )
    fun getFavourites(): Flow<List<HistoryItem>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(item: HistoryItem)

    @Delete
    fun delete(item: HistoryItem)

    @Update
    fun update(item: HistoryItem)
}