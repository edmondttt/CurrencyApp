package com.example.currencyapp.model.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CurrencyDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE) // IGNORE onConflict strategy
    void insert(Currency currency);

    @Update
    void update(Currency currency);

    @Delete
    void delete(Currency currency);

    @Query("DELETE FROM currency")
    void deleteAllCurrency();

    @Query("SELECT * FROM currency")
    LiveData<List<Currency>> getAllCurrency();

    @Query("SELECT * FROM Currency WHERE currency = :currency")
    List<Currency> getSpecificCurrency(String currency);
}
