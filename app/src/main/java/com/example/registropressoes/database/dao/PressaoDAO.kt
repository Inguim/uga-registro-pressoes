package com.example.registropressoes.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.registropressoes.model.Pressao
import kotlinx.coroutines.flow.Flow

@Dao
interface PressaoDAO {
    @Query("SELECT * FROM Pressao ORDER BY data DESC")
    fun listar(): Flow<List<Pressao>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun adicionar(pressao: Pressao)

    @Update
    suspend fun atualizar(pressao: Pressao)

    @Delete
    suspend fun remover(pressao: Pressao)

    @Query("SELECT * FROM Pressao WHERE id = :id")
    fun listarPorId(id: Long): Flow<Pressao?>
}