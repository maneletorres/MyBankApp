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

import mibancooperacional.bd.MiBancoOperacional;
import mibancooperacional.pojo.ATM;

public class NewATM extends AppCompatActivity {
    private String ATMoption, name, address, postalCode, location, latitudeString, lengthString;
    private int ATMid;
    private EditText nameEditText, addressEditText, postalCodeEditText, locationEditText,
            latitudeEditText, lengthEditText;
    private MiBancoOperacional mbo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_atm);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mbo = MiBancoOperacional.getInstance(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            ATMid = Integer.parseInt(extras.getString("ATMid"));
            ATMoption = extras.getString("ATMoption");
        }

        nameEditText = findViewById(R.id.name);
        addressEditText = findViewById(R.id.address);
        postalCodeEditText = findViewById(R.id.postal_code);
        locationEditText = findViewById(R.id.location);
        latitudeEditText = findViewById(R.id.latitude);
        lengthEditText = findViewById(R.id.longitude);

        displayMode();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_options, menu);

        for (int i = 0; i < menu.size(); i++) {
            MenuItem menuItem = menu.getItem(i);
            int idItem = menuItem.getItemId();
            if (ATMoption.equals("0")) {
                if (!(idItem == R.id.action_update || idItem == R.id.action_delete || idItem == R.id.action_save)) {
                    menuItem.setVisible(false);
                }
            } else if (ATMoption.equals("1")) {
                if (!(idItem == R.id.action_save)) {
                    menuItem.setVisible(false);
                }
            }
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_update:
                updateATM();
                break;
            case R.id.action_delete:
                deleteATM();
                break;
            case R.id.action_save:
                addATM();
                break;
            default:
                Intent myIntent = new Intent(getApplicationContext(), AccountsActivity.class);
                startActivityForResult(myIntent, 0);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        //MiBD.closeDB();
        super.onDestroy();
    }

    public void displayMode() {
        if (ATMoption.equals("0")) {
            boolean condition = false;
            ArrayList<ATM> atms = mbo.getATMs();

            ATM finalATM = new ATM();
            finalATM.setId(ATMid);

            for (int i = 0; i < atms.size() && !condition; i++) {
                ATM currentATM = atms.get(i);
                if (atms.get(i).getId() == ATMid) {
                    condition = true;
                    finalATM = currentATM;
                }
            }

            if (finalATM != null) {
                nameEditText.setText(finalATM.getName());
                addressEditText.setText(finalATM.getAddress());
                postalCodeEditText.setText(finalATM.getPostalCode());
                locationEditText.setText(finalATM.getLocation());
                latitudeEditText.setText(String.valueOf(finalATM.getLatitude()));
                lengthEditText.setText(String.valueOf(finalATM.getLongitude()));
                setEnabledEditText(false);
            }
        }
    }

    public void addATM() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.information)
                .setTitle(R.string.information).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        }).create();

        name = nameEditText.getText().toString();
        address = addressEditText.getText().toString();
        postalCode = postalCodeEditText.getText().toString();
        location = locationEditText.getText().toString();
        latitudeString = latitudeEditText.getText().toString();
        lengthString = lengthEditText.getText().toString();

        if (name.equals("") || address.equals("") || postalCode.equals("") || location.equals("")
                || latitudeString.equals("") || lengthString.equals("")) {
            builder.setMessage(getString(R.string.requirement)).show();
        } else {
            if (ATMoption.equals("1")) {

                float latitude = Float.parseFloat(latitudeString);
                float length = Float.parseFloat(lengthString);

                ArrayList<ATM> atms = mbo.getATMs();
                ATM atm = mbo.addNewATM(new ATM(atms.size(), name, address, postalCode, location, latitude, length));
                if (atm != null) {
                    builder.setMessage(getString(R.string.atm_entered))
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(getApplicationContext(), AccountsActivity.class);
                                    startActivity(intent);
                                }
                            }).create().show();
                } else {
                    builder.setMessage(getString(R.string.atm_not_entered)).show();
                }
            } else if (ATMoption.equals("2")) {
                ATM atm = prepareATM();
                int result = mbo.updateATM(atm);

                if (result == 1) {
                    builder.setMessage(getString(R.string.atm_entered))
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(getApplicationContext(), AccountsActivity.class);
                                    startActivity(intent);
                                }
                            }).show();
                } else {
                    builder.setMessage(getString(R.string.atm_not_entered))
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(getApplicationContext(), AccountsActivity.class);
                                    startActivity(intent);
                                }
                            }).show();
                }

            }
        }
    }

    public void updateATM() {
        ATMoption = "2";
        setEnabledEditText(true);
    }

    public void deleteATM() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.information))
                .setMessage(R.string.delete_atm).
                setPositiveButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        builder.setNegativeButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ATM newATM = new ATM();
                newATM.setId(ATMid);
                mbo.deleteATM(newATM);
                Intent intent = new Intent(getApplicationContext(), AccountsActivity.class);
                startActivity(intent);
            }
        }).show();
    }

    public void setEnabledEditText(boolean condition) {
        nameEditText.setEnabled(condition);
        addressEditText.setEnabled(condition);
        postalCodeEditText.setEnabled(condition);
        locationEditText.setEnabled(condition);
        latitudeEditText.setEnabled(condition);
        lengthEditText.setEnabled(condition);
    }

    public ATM prepareATM() {
        ATM atm = new ATM();
        atm.setId(ATMid);
        atm = mbo.search(atm);

        atm.setId(ATMid);
        atm.setName(name);
        atm.setAddress(address);
        atm.setPostalCode(postalCode);
        atm.setLocation(location);
        atm.setLatitude(Float.parseFloat(latitudeString));
        atm.setLongitude(Float.parseFloat(lengthString));
        return atm;
    }
}