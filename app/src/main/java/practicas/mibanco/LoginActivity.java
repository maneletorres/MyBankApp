package practicas.mibanco;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import mibancooperacional.bd.Constants;
import mibancooperacional.bd.MiBancoOperacional;
import mibancooperacional.pojo.Cliente;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText userEditText, passwordEditText;
    private MiBancoOperacional mbo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mbo = MiBancoOperacional.getInstance(this);

        userEditText = findViewById(R.id.userEditText);
        passwordEditText = findViewById(R.id.passwordEditText);

        Button enterButton = findViewById(R.id.enterButton);
        enterButton.setOnClickListener(this);

        Button exitButton = findViewById(R.id.exitButton);
        exitButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.enterButton:
                login();
                break;
            case R.id.exitButton:
                finishAffinity();
                System.exit(0);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_options, menu);

        for (int i = 0; i < menu.size(); i++) {
            MenuItem menuItem = menu.getItem(i);
            switch (menuItem.getItemId()) {
                case R.id.action_add:
                case R.id.action_preferences:
                    break;
                default:
                    menuItem.setVisible(false);
            }
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent i;
        switch (id) {
            case R.id.action_add:
                i = new Intent(getBaseContext(), NewUser.class);
                startActivity(i);
                break;
            case R.id.action_preferences:
                i = new Intent(getApplicationContext(), OptionsActivity.class);
                startActivity(i);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        //MiBD.closeDB();
        super.onDestroy();
    }

    public void login() {
        AlertDialog alertDialog = Constants.createAlert(new AlertDialog.Builder(this), this);

        String nif = userEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (nif.equals("") || password.equals("")) {
            Constants.alertOption(alertDialog, 0, this);
            alertDialog.show();
        } else {
            if (nif.length() == 9 && Constants.userFormatAccepted(nif)) {
                Cliente a = mbo.search(new Cliente(nif));
                if (a == null) {
                    Constants.alertOption(alertDialog, 2, this);
                    alertDialog.show();
                } else if (!a.getClaveSeguridad().equals(password)) {
                    Constants.alertOption(alertDialog, 1, this);
                    alertDialog.show();
                } else {
                    a = mbo.login(a);
                    Constants.nif = a.getNif();
                    Constants.password = a.getClaveSeguridad();
                    Intent i = new Intent(getBaseContext(), MainScreen.class);
                    startActivity(i);
                }
            } else {
                Constants.alertOption(alertDialog, 3, this);
                alertDialog.show();
            }
        }
    }
}