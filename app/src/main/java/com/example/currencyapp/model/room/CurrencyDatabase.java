package com.example.currencyapp.model.room;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Currency.class}, version = 1)
public abstract class CurrencyDatabase extends RoomDatabase{
    private static CurrencyDatabase instance;
    public abstract CurrencyDao currencyDao();

    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(4);

    public static synchronized CurrencyDatabase getInstance(Context context)
    {
        if(instance == null)
        {
            instance = Room.databaseBuilder(context.getApplicationContext(), CurrencyDatabase.class, "currency_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriteExecutor.execute(() -> {
                CurrencyDao currencyDao = instance.currencyDao();
                currencyDao.insert(new Currency("USD", 0, 0, 0));
                currencyDao.insert(new Currency("CAD", 0, 0, 0));
            });
        }
    };

}
