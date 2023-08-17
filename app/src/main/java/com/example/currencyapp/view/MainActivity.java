package com.example.currencyapp.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.currencyapp.R;
import com.example.currencyapp.model.retrofit.APIRequest;
import com.example.currencyapp.model.retrofit.RetrofitRequest;
import com.example.currencyapp.model.room.Currency;
import com.example.currencyapp.model.room.CurrencyAdapter;
import com.example.currencyapp.viewModel.CurrencyViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private CurrencyViewModel currencyViewModel;
    private RecyclerView recyclerView;
    private CurrencyAdapter currencyAdapter;

    private FloatingActionButton floatingActionButton;

    private APIRequest apiRequest;
    private Call<JsonObject> apiResponseLiveData;

    private Button convertBtn;
    private TextView currencyTextView, amountTextView, selection;
    private String[] currencyList = {"USD", "INR", "CAD", "CNY", "GBP"};

    private EditText amountEditText;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(MainActivity.this, new String []{Manifest.permission.POST_NOTIFICATIONS},101);

        }

        recyclerView = findViewById(R.id.recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        currencyTextView = findViewById(R.id.currencyTextView);
        amountTextView = findViewById(R.id.amountTextView);
        amountEditText = findViewById(R.id.editText);

        if (isLargeScreen()) {
            recyclerView.setBackgroundColor(getResources().getColor(R.color.primary_color_lg));
        }

        currencyAdapter = new CurrencyAdapter();
        recyclerView.setAdapter(currencyAdapter);

        currencyViewModel = new ViewModelProvider(this).get(CurrencyViewModel.class);

        currencyViewModel.getAllCurrency().observe(this, new Observer<List<Currency>>() {
            @Override
            public void onChanged(List<Currency> currency) {
                currencyAdapter.setCurrency(currency);
            }
        });

        floatingActionButton = findViewById(R.id.button_add_note);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ManageCurrency.class);
                //startActivity(intent);
                addCurrencyLauncher.launch(intent);
            }
        });

        floatingActionButton = findViewById(R.id.button_set_note);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, setAlert.class);
                //startActivity(intent);
                addCurrencyAlert.launch(intent);
            }
        });

        currencyAdapter.setOnItemClickListener(new CurrencyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Currency currency) {
                selection = findViewById(R.id.selection);
                String str = currency.getCurrency();
                selection.setText(str);

            }
        });

        convertBtn = findViewById(R.id.convertBtn);
        convertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (amountEditText == null || selection == null) {
                    Toast.makeText(getApplicationContext(), "Pick a currency and Enter Amount", Toast.LENGTH_SHORT).show();
                } else {
                    List<Currency> updatedDataList = currencyAdapter.getCurrencies();

                    String currencyToConvert = selection.getText().toString();
                    Double amount = Double.parseDouble(amountEditText.getText().toString());
                    updateAmount(currencyToConvert, amount);
                }
            }
        });
        updateRates("USD");
        //checkAlert();
    }


    private void updateRates(String baseCurrency) {
        apiRequest = RetrofitRequest.getRetrofitInstance().create(APIRequest.class);
        apiResponseLiveData = apiRequest.getCurrency(baseCurrency);
        apiResponseLiveData.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject res = response.body();
                JsonObject rates = res.getAsJsonObject("conversion_rates");


                for (int i = 0; i < currencyAdapter.getCurrencies().size(); i++) {
                    Currency tempCurrency = new Currency(currencyAdapter.getCurrencies().get(i).getCurrency(), Double.parseDouble(rates.get(currencyAdapter.getCurrencies().get(i).getCurrency()).toString()), currencyAdapter.getCurrencies().get(i).getAmount(), currencyAdapter.getCurrencies().get(i).getAlert());
                    currencyViewModel.update(tempCurrency);
                }
                Toast.makeText(getApplicationContext(), "Rate Updated", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("error", t.getMessage());
                Toast.makeText(getApplicationContext(), "No Response", Toast.LENGTH_LONG).show();
            }
        });
    }

    private ActivityResultLauncher<Intent> addCurrencyLauncher = registerForActivityResult
            (new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    try {
                        String currencyToAdd = data.getStringExtra("Currency");
                        Currency currency = new Currency(currencyToAdd, 1, 1, 100);
                        currencyViewModel.insert(currency);
                        updateRates("USD");
                    } catch (Exception e) {
                        Toast.makeText(this, "Currency could not duplicate", Toast.LENGTH_LONG).show();
                        throw new RuntimeException(e);
                    }

                } else if (result.getResultCode() == 2) {
                    currencyViewModel.deleteAllCurrency();
                } else if (result.getResultCode() == 3) {
                    Intent data = result.getData();
                    String currencyToAdd = data.getStringExtra("Currency");
                    Currency currency = new Currency(currencyToAdd, 1, 1, 100);
                    currencyViewModel.delete(currency);
                    updateRates("USD");
                }

            });

    private ActivityResultLauncher<Intent> addCurrencyAlert = registerForActivityResult
            (new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    try {

                        String currencyToAdd = data.getStringExtra("Currency");
                        Double rate = data.getDoubleExtra("Rate",-1);
                        int position = 0;
                        for (int i = 0; i < currencyAdapter.getCurrencies().size(); i++) {
                            Currency currency = currencyAdapter.getCurrencies().get(i);
                            if (currency.getCurrency().equals(currencyToAdd)) {
                                position = i;
                                break;
                            }
                        }
                        Currency currency = new Currency(currencyToAdd, currencyAdapter.getCurrencies().get(position).getRate(), currencyAdapter.getCurrencies().get(position).getAmount(), rate);
                        currencyViewModel.update(currency);
                        updateRates("USD");
                    } catch (Exception e) {
                        Toast.makeText(this, "Currency could not duplicate", Toast.LENGTH_LONG).show();
                        throw new RuntimeException(e);
                    }

                }
                checkAlert();

            });

    private void updateAmount(String baseCurrency, double amount) {
        int position = 0;
        for (int i = 0; i < currencyAdapter.getCurrencies().size(); i++) {
            Currency currency = currencyAdapter.getCurrencies().get(i);
            if (currency.getCurrency().equals(baseCurrency)) {
                position = i;
                break;
            }
        }
        for (int i = 0; i < currencyAdapter.getCurrencies().size(); i++) {

            Currency tempCurrency = new Currency(currencyAdapter.getCurrencies().get(i).getCurrency(), currencyAdapter.getCurrencies().get(i).getRate(), Math.round(amount / currencyAdapter.getCurrencies().get(position).getRate() * currencyAdapter.getCurrencies().get(i).getRate() * 10000.0) / 10000.0, currencyAdapter.getCurrencies().get(i).getAlert());
            currencyViewModel.update(tempCurrency);

        }
        updateRates("USD");
        checkAlert();
    }

    private boolean isLargeScreen() {
        int screeSize = getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
        return screeSize == Configuration.SCREENLAYOUT_SIZE_LARGE || screeSize == Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    @SuppressLint("MissingPermission")
    private void buildNotification(String currency, Double alertPrice) {
        int notificationId = 1; // Unique ID for the notification
        String channelId = "my_channel_id"; // Channel ID created in the previous step

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_notification) // Replace with your notification icon
                .setContentTitle("Price Reached!")
                .setContentText("The price of " + currency + " to 1 dollar USD has reached" + alertPrice)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT).setAutoCancel(true);


// Create an explicit intent to launch an activity when the notification is clicked
        Intent intent = new Intent(this, NotificationDetailsActivity.class);
        //pass data here if needed


        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE);

        builder.setContentIntent(pendingIntent);


        //create notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channelName = "My Channel";
            String channelDescription = "My Notification Channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            channel.setDescription(channelDescription);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        // Create the notification and display it
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        notificationManager.notify(notificationId, builder.build());
    }
    private void checkAlert (){


        for (int i = 0; i < currencyViewModel.getAllCurrency().getValue().size() ; i++) {
            if (currencyViewModel.getAllCurrency().getValue().get(i).getRate() > currencyViewModel.getAllCurrency().getValue().get(i).getAlert()){
                buildNotification(currencyViewModel.getAllCurrency().getValue().get(i).getCurrency(),currencyViewModel.getAllCurrency().getValue().get(i).getAlert());
            }
        }
    }
}

