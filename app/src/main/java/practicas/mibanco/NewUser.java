package practicas.mibanco;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import java.util.ArrayList;

import mibancooperacional.bd.Constants;
import mibancooperacional.bd.MiBancoOperacional;
import mibancooperacional.pojo.Cliente;

public class NewUser extends AppCompatActivity {
    private EditText nifEditText, nameEditText, surnameEditText, securityKeyEditText, emailEditText;
    private MiBancoOperacional mbo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mbo = MiBancoOperacional.getInstance(this);

        nifEditText = findViewById(R.id.nifEditText);
        nameEditText = findViewById(R.id.nameEditText);
        surnameEditText = findViewById(R.id.surnameEditText);
        securityKeyEditText = findViewById(R.id.securityKeyEditText);
        emailEditText = findViewById(R.id.emailEditText);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                addNewUser();
                break;
            default:
                Intent myIntent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivityForResult(myIntent, 0);
        }

        return true;
    }

    @Override
    protected void onDestroy() {
        //MiBD.closeDB();
        super.onDestroy();
    }

    public void addNewUser() {
        AlertDialog.Builder alertDiagloBuilder = new AlertDialog.Builder(this);
        AlertDialog alertDialog = Constants.createAlert(alertDiagloBuilder, this);

        String nif = nifEditText.getText().toString();
        String name = nameEditText.getText().toString();
        String surname = surnameEditText.getText().toString();
        String securityKey = securityKeyEditText.getText().toString();
        String email = emailEditText.getText().toString();

        if (nif.equals("") || name.equals("") || surname.equals("") || securityKey.equals("") || email.equals("")) {
            Constants.alertOption(alertDialog, 0, this);
            alertDialog.show();
        } else if (!(nif.length() == 9 && Constants.userFormatAccepted(nif))) {
            Constants.alertOption(alertDialog, 3, this);
            alertDialog.show();
        } else {
            boolean condition = false;
            ArrayList<Cliente> costumers = mbo.getClientes();
            for (int i = 0; i < costumers.size() && !condition; i++) {
                Cliente currentCliente = costumers.get(i);
                if (currentCliente.getNif().equals(nif)) {
                    condition = true;
                }
            }

            if (!condition) {
                int id = costumers.size();

                Cliente cliente = mbo.addNewCostumer(new Cliente(id, nif, name, surname, securityKey, email));
                if (cliente != null) {
                    alertDiagloBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                        }
                    }).setMessage(getString(R.string.client_entered)).show();
                } else {
                    Constants.alertOption(alertDialog, 5, this);
                    alertDialog.show();
                }
            } else {
                Constants.alertOption(alertDialog, 6, this);
                alertDialog.show();
            }
        }
    }
}