package com.example.registropressoes.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.registropressoes.model.Pressao
import kotlinx.coroutines.flow.Flow

data class MediaPressao (
    val avg_maxima: Double,
    val avg_minima: Double
)

@Dao
interface PressaoDAO {
    @Query("SELECT * FROM Pressao WHERE data BETWEEN :dataStart AND :dataEnd ORDER BY data DESC")
    fun listarPeriodo(dataStart: Long, dataEnd: Long): Flow<List<Pressao>>

    @Query("SELECT * FROM Pressao ORDER BY data DESC")
    fun listar(): Flow<List<Pressao>>

    @Query("SELECT * FROM Pressao WHERE importado = '1' ORDER BY data DESC")
    fun listarImportados(): Flow<List<Pressao>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun adicionar(pressao: Pressao)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun multiplaAdicao(pressoes: List<Pressao>)

    @Update
    suspend fun atualizar(pressao: Pressao)

    @Delete
    suspend fun remover(pressao: Pressao)

    @Query("SELECT * FROM Pressao WHERE id = :id")
    fun listarPorId(id: Long): Flow<Pressao?>

    @Query("SELECT * FROM Pressao ORDER BY data DESC LIMIT 1")
    suspend fun listarUltimaMedicao(): List<Pressao>

    @Query("""SELECT * FROM Pressao 
        WHERE
        CASE 
                WHEN :dataStart and :dataEnd IS NOT NULL THEN data BETWEEN :dataStart and :dataEnd
                ELSE 1 = 1
        END 
        ORDER BY maxima DESC LIMIT 1""")
    suspend fun listarMaiorMedicao(dataStart: Long? = null, dataEnd: Long? = null): Pressao?

    @Query("""SELECT * FROM Pressao 
        WHERE
        CASE 
                WHEN :dataStart and :dataEnd IS NOT NULL THEN data BETWEEN :dataStart and :dataEnd
                ELSE 1 = 1
        END 
        ORDER BY maxima ASC LIMIT 1""")
    suspend fun listarMenorMedicao(dataStart: Long? = null, dataEnd: Long? = null): Pressao?

    @Query("""SELECT AVG(maxima) as avg_maxima, AVG(minima) as avg_minima FROM PRESSAO
         WHERE
         CASE 
                WHEN :dataStart and :dataEnd IS NOT NULL THEN data BETWEEN :dataStart and :dataEnd
                ELSE 1 = 1
         END""")
    suspend fun listarMediaMedicoes(dataStart: Long? = null, dataEnd: Long? = null): MediaPressao

}