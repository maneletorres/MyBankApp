package practicas.mibanco.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import mibancooperacional.pojo.Cuenta;
import practicas.mibanco.R;

public class AccountAdapter extends ArrayAdapter<Cuenta> {
    public AccountAdapter(Context context, ArrayList<Cuenta> accounts) {
        super(context, 0, accounts);
    }

    @Override
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext())
                    .inflate(R.layout.income_list_item, parent, false);
        }

        Cuenta currentAccount = getItem(position);
        TextView accountNumberTextView = listItemView.findViewById(R.id.accountNumber);
        if (currentAccount != null) {
            accountNumberTextView.setText(currentAccount.getNumeroCuenta());
        }

        TextView accountBalanceTextView = listItemView.findViewById(R.id.accountBalance);
        float currentBalance = currentAccount.getSaldoActual();
        if (currentBalance < 0) {
            accountBalanceTextView.setTextColor(Color.parseColor("#FF0000"));
        } else {
            accountBalanceTextView.setTextColor(Color.parseColor("#000000"));
        }

        String currentBalanceFormatted = formatBalance(currentBalance);
        accountBalanceTextView.setText(currentBalanceFormatted + " €");

        return listItemView;
    }

    private String formatBalance(float valor) {
        DecimalFormat format = new DecimalFormat();
        format.setMinimumFractionDigits(2);
        format.setMaximumFractionDigits(2);
        return format.format(valor);
    }
}