package com.example.orgs.database

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.registropressoes.database.dao.PressaoDAO
import com.example.registropressoes.model.Pressao

@Database(
    entities = [Pressao::class],
    version = 2,
    autoMigrations = [AutoMigration(from = 1, to = 2)],
    exportSchema = true
)
abstract class AppDataBase : RoomDatabase() {
    abstract fun pressaoDAO(): PressaoDAO

    companion object {
        @Volatile
        private lateinit var db: AppDataBase

        fun instancia(context: Context): AppDataBase {
            if (::db.isInitialized) return db
            return Room.databaseBuilder(
                context,
                AppDataBase::class.java,
                "registropressoes.db"
            ).build().also {
                db = it
            }
        }
    }
}