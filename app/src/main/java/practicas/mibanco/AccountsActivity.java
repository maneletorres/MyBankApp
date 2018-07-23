package practicas.mibanco;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

import mibancooperacional.bd.Constants;
import mibancooperacional.bd.MiBancoOperacional;
import mibancooperacional.pojo.ATM;
import mibancooperacional.pojo.Cliente;
import mibancooperacional.pojo.Cuenta;
import practicas.mibanco.adapters.ATMAdapter;
import practicas.mibanco.adapters.AccountAdapter;

import static android.view.View.GONE;

public class AccountsActivity extends AppCompatActivity {
    AlertDialog alert = null;
    private double longitudeClosestATM = 0, latitudeClosestATM = 0;
    private String nameClosestATM = "";
    private ListView listView;
    private MiBancoOperacional mbo;
    private Cliente a;
    private LocationManager locationManager;
    private LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mbo = MiBancoOperacional.getInstance(this);

        TextView noDataTextView = findViewById(R.id.noDataTextView);
        TextView headboardTextView = findViewById(R.id.headboardTextView);

        listView = findViewById(R.id.list);
        switch (Constants.optionNumber) {
            case 1:
                setTitle(getString(R.string.global_position));
                headboardTextView.setText(getString(R.string.accounts_and_balances));

                a = mbo.search(new Cliente(Constants.nif));
                ArrayList<Cuenta> accounts = mbo.getCuentas(a);

                AccountAdapter accountAdapter = new AccountAdapter(this, accounts);
                listView.setAdapter(accountAdapter);
                break;
            case 2:
                setTitle(getString(R.string.income));
                headboardTextView.setText(getString(R.string.income_accounts));
                preparingTransfers();
                break;
            case 3:
                setTitle(getString(R.string.transfers));
                headboardTextView.setText(getString(R.string.transfer_accounts));
                preparingTransfers();
                break;
            case 4:
                setTitle(getString(R.string.atms));
                headboardTextView.setText(getString(R.string.atms_header));
                preparingATMs();
                break;
        }

        if (listView.getCount() == 0) {
            listView.setVisibility(GONE);
            noDataTextView.setVisibility(View.VISIBLE);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager != null && locationListener != null) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_options, menu);

        for (int i = 0; i < menu.size(); i++) {
            MenuItem menuItem = menu.getItem(i);
            int itemId = menuItem.getItemId();

            switch (Constants.optionNumber) {
                case 3:
                    if (!(itemId == R.id.action_add || itemId == R.id.action_visibility)) {
                        menuItem.setVisible(false);
                    }
                    break;
                case 4:
                    if (!(itemId == R.id.action_add || itemId == R.id.action_gps)) {
                        menuItem.setVisible(false);
                    }
                    break;
                default:
                    menuItem.setVisible(false);
            }
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;
        switch (item.getItemId()) {
            case R.id.action_gps:
                calculateClosestATM();
                alertNoGps();
                break;
            case R.id.action_visibility:
                if (listView.getVisibility() == View.VISIBLE) {
                    listView.setVisibility(GONE);
                } else {
                    listView.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.action_add:
                if (Constants.optionNumber == 3) {
                    i = new Intent(getApplicationContext(), NewTransfer.class);
                    startActivity(i);
                } else if (Constants.optionNumber == 4) {
                    i = new Intent(getApplicationContext(), NewATM.class);
                    i.putExtra("ATMid", "0");
                    i.putExtra("ATMoption", "1");
                    startActivity(i);
                }
                break;
            default:
                Intent myIntent = new Intent(getApplicationContext(), MainScreen.class);
                startActivityForResult(myIntent, 0);
        }

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
                locationManager.removeUpdates(locationListener);
            }
        } else {
            locationManager.removeUpdates(locationListener);
        }
    }

    @Override
    protected void onDestroy() {
        //MiBD.closeDB();
        if (alert != null) {
            alert.dismiss();
        }
        super.onDestroy();
    }

    public void preparingTransfers() {
        a = mbo.search(new Cliente(Constants.nif));
        if (a != null) {
            final ArrayList<Cuenta> accounts = mbo.getCuentas(a);
            ArrayList<String> ATMsName = new ArrayList<>();
            for (int i = 0; i < accounts.size(); i++) {
                ATMsName.add(accounts.get(i).getNumeroCuenta());
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, ATMsName);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Cuenta cuenta = accounts.get(position);
                    Intent i = new Intent(getApplicationContext(), TransfersActivity.class);
                    i.putExtra("idAccount", cuenta.getId());

                    switch (Constants.optionNumber) {
                        case 2:
                            i.putExtra("movementType", 1);
                            break;
                        case 3:
                            i.putExtra("movementType", 2);
                            break;
                    }
                    startActivity(i);
                }
            });
        }
    }

    public void preparingATMs() {
        a = mbo.search(new Cliente(Constants.nif));
        if (a != null) {
            ArrayList<ATM> atms = mbo.getATMs();
            ATMAdapter adapter = new ATMAdapter(this, atms);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ATM atm = (ATM) parent.getItemAtPosition(position);
                    Intent i = new Intent(getApplicationContext(), NewATM.class);
                    i.putExtra("ATMid", atm.getId() + "");
                    i.putExtra("ATMoption", "0");
                    startActivity(i);
                }
            });
        }
    }

    public void calculateClosestATM() {
        final Location lastLocation = new Location("");

        FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
                mFusedLocationClient.getLastLocation()
                        .addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location loc) {
                                if (loc != null) {
                                    lastLocation.setLatitude(loc.getLatitude());
                                    lastLocation.setLongitude(loc.getLongitude());
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                e.printStackTrace();
                            }
                        });
            }
        }

        ArrayList<ATM> atms = mbo.getATMs();
        if (atms.size() > 0) {
            float minimumDistanceMeters = 0;
            float currentDistanceMeters;

            Location location = new Location("");

            for (int i = 0; i < atms.size(); i++) {
                ATM currentATM = atms.get(i);

                location.setLatitude(currentATM.getLatitude());
                location.setLongitude(currentATM.getLongitude());
                currentDistanceMeters = location.distanceTo(lastLocation);

                if (i == 0 || currentDistanceMeters < minimumDistanceMeters) {
                    minimumDistanceMeters = currentDistanceMeters;
                    latitudeClosestATM = currentATM.getLatitude();
                    longitudeClosestATM = currentATM.getLongitude();
                    nameClosestATM = currentATM.getName();
                }
            }
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.no_stored_ATMs), Toast.LENGTH_SHORT).show();
        }
    }

    private void alertNoGps() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (locationManager != null) {
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                    || !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.information))
                        .setMessage(getString(R.string.activate_gps))
                        .setCancelable(false)
                        .setIcon(R.drawable.crosshairs_gps)
                        .setPositiveButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                            public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                //startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                Uri gmmIntentUri = Uri.parse("geo:" + latitudeClosestATM + "," + longitudeClosestATM + "?q=" + latitudeClosestATM + "," + longitudeClosestATM + "(" + nameClosestATM + ")");
                                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                mapIntent.setPackage("com.google.android.apps.maps");
                                startActivity(mapIntent);
                            }
                        }).create().show();
            } else {
                Uri gmmIntentUri = Uri.parse("geo:" + latitudeClosestATM + "," + longitudeClosestATM + "?q=" + latitudeClosestATM + "," + longitudeClosestATM + "(" + nameClosestATM + ")");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        }
    }
}