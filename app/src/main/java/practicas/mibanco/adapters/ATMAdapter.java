package practicas.mibanco.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import mibancooperacional.pojo.ATM;
import practicas.mibanco.R;

public class ATMAdapter extends ArrayAdapter<ATM> {
    public ATMAdapter(Context context, ArrayList<ATM> atms) {
        super(context, 0, atms);
    }

    @Override
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext())
                    .inflate(R.layout.transfer_list_item, parent, false);
        }

        ATM currentATM = getItem(position);
        TextView movementTextView = listItemView.findViewById(R.id.transferDescription);
        if (currentATM != null) {
            movementTextView.setText(currentATM.getName());
        }

        return listItemView;
    }
}