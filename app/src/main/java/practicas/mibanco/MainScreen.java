package practicas.mibanco;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import mibancooperacional.bd.Constants;
import mibancooperacional.bd.MiBancoOperacional;
import mibancooperacional.pojo.Cliente;

public class MainScreen extends AppCompatActivity implements View.OnClickListener {
    private MiBancoOperacional mbo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        mbo = MiBancoOperacional.getInstance(this);

        Button globalPositionButton = findViewById(R.id.globalPositionButton);
        globalPositionButton.setOnClickListener(this);

        Button incomeButton = findViewById(R.id.incomeButton);
        incomeButton.setOnClickListener(this);

        Button transfersButton = findViewById(R.id.transfersButton);
        transfersButton.setOnClickListener(this);

        Button changeSecurityKeyButton = findViewById(R.id.changeSecurityKeyButton);
        changeSecurityKeyButton.setOnClickListener(this);

        Button closestATMsButton = findViewById(R.id.closestATMsButton);
        closestATMsButton.setOnClickListener(this);

        Button exitButton = findViewById(R.id.exitButton);
        exitButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.globalPositionButton:
                globalPositionScreen();
                break;
            case R.id.incomeButton:
                incomeScreen();
                break;
            case R.id.transfersButton:
                transfersScreen();
                break;
            case R.id.changeSecurityKeyButton:
                changeSecurityKey();
                break;
            case R.id.closestATMsButton:
                closestATMsScreen();
                break;
            case R.id.exitButton:
                exitApp();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                //
                break;
            case R.id.globalPosition:
                globalPositionScreen();
                break;
            case R.id.income:
                incomeScreen();
                break;
            case R.id.transfers:
                transfersScreen();
                break;
            case R.id.changeSecurityKeyButton:
                changeSecurityKey();
                break;
            case R.id.closestATMs:
                closestATMsScreen();
                break;
            case R.id.exitApp:
                exitApp();
                break;
            default:
                Intent myIntent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivityForResult(myIntent, 0);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        //MiBD.closeDB();
        super.onDestroy();
    }

    public void globalPositionScreen() {
        Intent i = new Intent(getBaseContext(), AccountsActivity.class);
        Constants.optionNumber = 1;
        startActivity(i);
    }

    public void incomeScreen() {
        Intent i = new Intent(getBaseContext(), AccountsActivity.class);
        Constants.optionNumber = 2;
        startActivity(i);
    }

    public void transfersScreen() {
        Intent i = new Intent(getBaseContext(), AccountsActivity.class);
        Constants.optionNumber = 3;
        startActivity(i);
    }

    public void changeSecurityKey() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        final EditText input = new EditText(this);
        LinearLayout layout = new LinearLayout(this);

        layout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(80, 0, 80, 0);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        layout.addView(input, params);

        alertDialog.setTitle(getString(R.string.enter_password))
                .setView(layout)
                .setIcon(R.drawable.key)
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String password = input.getText().toString();
                        if (!password.equals("")) {
                            Cliente a = mbo.search(new Cliente(Constants.nif));

                            if (a != null) {
                                a.setClaveSeguridad(password);
                                int result = mbo.changePassword(a);

                                if (Constants.password.equals(password)) {
                                    Toast.makeText(getApplicationContext(), getString(R.string.password_not_valid), Toast.LENGTH_SHORT).show();
                                } else {
                                    if (result == 1) {
                                        Toast.makeText(getApplicationContext(), getString(R.string.password_modified), Toast.LENGTH_SHORT).show();
                                        Constants.password = password;
                                    } else if (result == 0) {
                                        Toast.makeText(getApplicationContext(), getString(R.string.error_entering_password), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), getString(R.string.user_does_not_exist), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), getString(R.string.requirement), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        alertDialog.setPositiveButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).create().show();
    }

    public void closestATMsScreen() {
        Intent i = new Intent(getBaseContext(), AccountsActivity.class);
        Constants.optionNumber = 4;
        startActivity(i);
    }

    public void exitApp() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(getString(R.string.exit))
                .setMessage(getString(R.string.close_session))
                .setIcon(R.drawable.exit_to_app)
                .setPositiveButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
                }).create().show();
    }
}