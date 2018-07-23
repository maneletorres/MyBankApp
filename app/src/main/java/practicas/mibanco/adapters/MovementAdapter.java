package practicas.mibanco.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import mibancooperacional.pojo.Movimiento;
import practicas.mibanco.R;

public class MovementAdapter extends ArrayAdapter<Movimiento> {
    public MovementAdapter(Context context, ArrayList<Movimiento> movements) {
        super(context, 0, movements);
    }

    @Override
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext())
                    .inflate(R.layout.transfer_list_item, parent, false);
        }

        Movimiento currentMovement = getItem(position);
        TextView movementTextView = listItemView.findViewById(R.id.transferDescription);
        if (currentMovement != null) {
            movementTextView.setText(currentMovement.getDescripcion());
        }

        return listItemView;
    }
}