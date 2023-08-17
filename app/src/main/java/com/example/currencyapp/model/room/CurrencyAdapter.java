package com.example.currencyapp.model.room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.currencyapp.R;

import java.util.ArrayList;
import java.util.List;

public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.CurrencyHolder> {

    public List<Currency> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(List<Currency> currencies) {
        this.currencies = currencies;
    }

    private List<Currency> currencies = new ArrayList<>();
    private OnItemClickListener listener;

    @NonNull
    @Override
    public CurrencyAdapter.CurrencyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);

        return new CurrencyHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CurrencyAdapter.CurrencyHolder holder, int position) {

        Currency currentCurrency = currencies.get(position);
        if (currentCurrency.getCurrency().equals("USD")){
            holder.imgView.setImageResource(R.drawable.usd);
        }
        else if (currentCurrency.getCurrency().equals("CAD")){
            holder.imgView.setImageResource(R.drawable.cad);
        }
        else if (currentCurrency.getCurrency().equals("CNY")){
            holder.imgView.setImageResource(R.drawable.cny);
        }
        else if (currentCurrency.getCurrency().equals("GBP")){
            holder.imgView.setImageResource(R.drawable.gbp);
        }
        else if (currentCurrency.getCurrency().equals("INR")){
            holder.imgView.setImageResource(R.drawable.inr);
        }
        holder.currencyTextView.setText(currentCurrency.getCurrency());
        holder.amountTextView.setText(Double.toString(currentCurrency.getAmount()));

    }

    @Override
    public int getItemCount() {
        return currencies.size();
    }

    public void setCurrency(List<Currency> currencies)
    {
        this.currencies = currencies;
        notifyDataSetChanged();
    }

    public class CurrencyHolder extends RecyclerView.ViewHolder {
        private ImageView imgView;
        private TextView currencyTextView, amountTextView;
        public CurrencyHolder(@NonNull View itemView) {
            super(itemView);
            imgView = itemView.findViewById(R.id.imageView);
            currencyTextView = itemView.findViewById(R.id.currencyTextView);
            amountTextView = itemView.findViewById(R.id.amountTextView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        Currency currency = currencies.get(position);
                        listener.onItemClick(currency);
                    }
                }
            });

        }
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = (OnItemClickListener) listener;
    }
    public interface OnItemClickListener {
        void onItemClick(Currency currency);
    }


}
