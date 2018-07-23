package practicas.mibanco;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import mibancooperacional.bd.Constants;
import mibancooperacional.bd.MiBancoOperacional;
import mibancooperacional.pojo.Cliente;
import mibancooperacional.pojo.Cuenta;
import mibancooperacional.pojo.Movimiento;
import practicas.mibanco.adapters.MovementAdapter;

import static android.view.View.GONE;

public class TransfersActivity extends AppCompatActivity {
    private ArrayList<Movimiento> movements = null;
    private TextView noDataTextView;
    private int idCuenta, movementType;
    private MiBancoOperacional mbo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mbo = MiBancoOperacional.getInstance(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            idCuenta = extras.getInt("idAccount");
            movementType = extras.getInt("movementType");
        }

        TextView headboardTextView = findViewById(R.id.headboardTextView);
        noDataTextView = findViewById(R.id.noDataTextView);
        headboardTextView.setText(getText(R.string.transfers_header));

        preparingListView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent = new Intent(getApplicationContext(), AccountsActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }

    @Override
    protected void onDestroy() {
        //MiBD.closeDB();
        super.onDestroy();
    }

    public void preparingListView() {
        Cliente a = mbo.search(new Cliente(Constants.nif));

        ArrayList<Cuenta> accounts = mbo.getCuentas(a);
        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).getId() == idCuenta) {
                movements = mbo.getMovimientos(accounts.get(i));
            }
        }

        ArrayList<Movimiento> moves = new ArrayList<>();
        Movimiento movement;
        for (int i = 0; i < movements.size(); i++) {
            movement = movements.get(i);
            if (movementType == 1) {
                if (movement.getTipo() == 1) {
                    moves.add(movement);
                }
            } else if (movementType == 2) {
                moves.add(movement);
            }
        }

        ListView listView = findViewById(R.id.list);
        MovementAdapter adapter = new MovementAdapter(this, moves);
        listView.setAdapter(adapter);

        if (listView.getCount() == 0) {
            listView.setVisibility(GONE);
            noDataTextView.setVisibility(View.VISIBLE);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Movimiento movimiento = (Movimiento) adapterView.getItemAtPosition(i);
                String transferResum = formatTransferResume(movimiento);

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(TransfersActivity.this);
                alertDialog.setTitle(getString(R.string.transfers_resume))
                        .setMessage(transferResum)
                        .setIcon(R.drawable.exit_to_app)
                        .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).show();
            }
        });
    }

    public String formatTransferResume(Movimiento movimiento) {
        int id = movimiento.getId();
        int type = movimiento.getTipo();
        Date date = movimiento.getFechaOperacion();
        String description = movimiento.getDescripcion();
        float amount = movimiento.getImporte();
        String numberOriginAccount = movimiento.getCuentaOrigen().getNumeroCuenta();
        String numberDestinationAccount = movimiento.getCuentaDestino().getNumeroCuenta();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String typeString = "";
        switch (type) {
            case 0:
                typeString = getString(R.string.discount);
                break;
            case 1:
                typeString = getString(R.string.entry);
                break;
            case 2:
                typeString = getString(R.string.refund);
                break;
        }
        return "ID: " + id + "\n" + getString(R.string.transfer_type) + " " + typeString + "\n" +
                getString(R.string.transfer_date) + " " + formatter.format(date) + "\n" +
                getString(R.string.transfer_description) + " " + description + "\n" +
                getString(R.string.transfer_amount) + " " + amount + " €\n" +
                getString(R.string.transfer_origin_account) + " " + numberOriginAccount
                + "\n" + getString(R.string.transfer_destination_account) + " " + numberDestinationAccount;
    }
}