package practicas.mibanco;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import mibancooperacional.bd.Constants;
import mibancooperacional.bd.MiBancoOperacional;
import mibancooperacional.pojo.Cliente;
import mibancooperacional.pojo.Cuenta;
import mibancooperacional.pojo.Movimiento;

public class NewTransfer extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private MiBancoOperacional mbo;
    private EditText destinationAccountEditText, conceptEditText, quantityEditText;
    private String selectedItem;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_transfer);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mbo = MiBancoOperacional.getInstance(this);

        destinationAccountEditText = findViewById(R.id.destinationAccountEditText);
        conceptEditText = findViewById(R.id.conceptEditText);
        quantityEditText = findViewById(R.id.quantityEditText);

        spinner = findViewById(R.id.originAccountSpinner);
        spinner.setOnItemSelectedListener(this);
        prepareSpinner();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_options, menu);

        for (int i = 0; i < menu.size(); i++) {
            MenuItem menuItem = menu.getItem(i);
            if (!(menuItem.getItemId() == R.id.action_save)) {
                menuItem.setVisible(false);
            }
        }

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        selectedItem = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                newTransfer();
                break;
            default:
                Intent myIntent = new Intent(getApplicationContext(), AccountsActivity.class);
                startActivityForResult(myIntent, 0);
        }

        return true;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //
    }

    @Override
    protected void onDestroy() {
        //MiBD.closeDB();
        super.onDestroy();
    }

    public void prepareSpinner() {
        ArrayList<String> accountNumbers = new ArrayList<>();

        Cliente b = mbo.search(new Cliente(Constants.nif));
        ArrayList<Cuenta> accounts = mbo.getCuentas(b);
        for (int i = 0; i < accounts.size(); i++) {
            accountNumbers.add(accounts.get(i).getNumeroCuenta());
        }

        spinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, accountNumbers));
    }

    /**
     * Operación transferencia:
     */
    public void newTransfer() {
        AlertDialog alertDialog = Constants.createAlert(new AlertDialog.Builder(this), this);

        String destinationAccountNumber = destinationAccountEditText.getText().toString();
        String concept = conceptEditText.getText().toString();
        String quantityString = quantityEditText.getText().toString();

        if (destinationAccountNumber.equals("") || concept.equals("")
                || quantityString.equals("")) {
            Constants.alertOption(alertDialog, 0, this);
            alertDialog.show();
        } else {
            float quantity = Integer.parseInt(quantityString);
            if (quantity <= 0) {
                Constants.alertOption(alertDialog, 7, this);
                alertDialog.show();
            } else {
                Cliente a = mbo.search(new Cliente(Constants.nif));

                ArrayList<Cuenta> accounts = mbo.getCuentas(a);
                Cuenta originAccount = null;
                Cuenta destinationAccount = null;

                for (int i = 0; i < accounts.size(); i++) {
                    Cuenta currentAccount = accounts.get(i);
                    if (currentAccount.getNumeroCuenta().equals(selectedItem)) {
                        originAccount = currentAccount;
                    }
                    if (currentAccount.getNumeroCuenta().equals(destinationAccountNumber)) {
                        destinationAccount = currentAccount;
                    }
                }

                if (originAccount == null) {
                    Constants.alertOption(alertDialog, 8, this);
                    alertDialog.show();
                } else if (destinationAccount == null) {
                    Constants.alertOption(alertDialog, 9, this);
                    alertDialog.show();
                } else if (originAccount.equals(destinationAccount)) {
                    Constants.alertOption(alertDialog, 10, this);
                    alertDialog.show();
                } else if (originAccount.getSaldoActual() >= quantity) {
                    ArrayList<Movimiento> movementsOrigin = mbo.getMovimientos(originAccount);
                    int idOri = movementsOrigin.size();

                    ArrayList<Movimiento> movementsDestination = mbo.getMovimientos(destinationAccount);
                    int idDest = movementsDestination.size();

                    Date date = Calendar.getInstance().getTime();
                    Movimiento newOriginMovement = new Movimiento(idOri, 0, date, concept, quantity * -1, originAccount, destinationAccount);
                    Movimiento newDestinationMovement = new Movimiento(idDest, 1, date, concept, quantity, destinationAccount, originAccount);

                    originAccount.setSaldoActual(originAccount.getSaldoActual() - quantity);
                    destinationAccount.setSaldoActual(destinationAccount.getSaldoActual() + quantity);

                    Movimiento originResult = mbo.addNewTransfer(newOriginMovement, originAccount);
                    Movimiento destinationResult = mbo.addNewTransfer(newDestinationMovement, destinationAccount);
                    if (originResult != null && destinationResult != null) {
                        cleanScreen();
                        Constants.alertOption(alertDialog, 11, this);
                        alertDialog.show();
                    } else {
                        cleanScreen();
                        Constants.alertOption(alertDialog, 12, this);
                        alertDialog.show();
                    }
                } else {
                    Constants.alertOption(alertDialog, 13, this);
                    alertDialog.show();
                }
            }
        }
    }

    public void cleanScreen() {
        destinationAccountEditText.setText("");
        conceptEditText.setText("");
        quantityEditText.setText("");
    }
}