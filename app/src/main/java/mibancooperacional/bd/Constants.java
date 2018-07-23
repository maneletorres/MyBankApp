package mibancooperacional.bd;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import practicas.mibanco.R;

public class Constants {
    public static final String FIELD_NAME = "name";
    public static final String FIELD_ADRESS = "address";
    public static final String FIELD_POSTAL_CODE = "postalCode";
    public static final String FIELD_LOCATION = "location";
    public static final String FIELD_LATITUDE = "latitude";
    public static final String FIELD_LONGITUDE = "longitude";
    public static final String CAJEROS_TABLE = "cajeros";
    private static final String FIELD_CAJEROS_ID = "id";
    public static final String[] CAJEROS_COLUMNS = new String[]{FIELD_CAJEROS_ID, FIELD_NAME,
            FIELD_ADRESS, FIELD_POSTAL_CODE, FIELD_LOCATION, FIELD_LATITUDE, FIELD_LONGITUDE};

    public static String nif, password;
    public static int optionNumber;

    public static AlertDialog createAlert(AlertDialog.Builder dialog, Context context) {
        dialog.setTitle(R.string.information);
        dialog.setIcon(R.drawable.information);

        dialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        return dialog.create();
    }

    public static void alertOption(AlertDialog alertDialog, int alertOption, Context context) {
        switch (alertOption) {
            case 0:
                alertDialog.setMessage(context.getString(R.string.requirement));
                break;
            case 1:
                alertDialog.setMessage(context.getString(R.string.password_not_valid));
                break;
            case 2:
                alertDialog.setMessage(context.getString(R.string.user_does_not_exist));
                break;
            case 3:
                alertDialog.setMessage(context.getString(R.string.format_user_accepted));
                break;
            case 4:
                alertDialog.setMessage(context.getString(R.string.client_entered));
                break;
            case 5:
                alertDialog.setMessage(context.getString(R.string.client_not_entered));
                break;
            case 6:
                alertDialog.setMessage(context.getString(R.string.user_exists));
                break;
            case 7:
                alertDialog.setMessage(context.getString(R.string.zeroAmount));
                break;
            case 8:
                alertDialog.setMessage(context.getString(R.string.originAccountDoesNotExist));
                break;
            case 9:
                alertDialog.setMessage(context.getString(R.string.destinationAccountDoesNotExist));
                break;
            case 10:
                alertDialog.setMessage(context.getString(R.string.repeated_accounts));
                break;
            case 11:
                alertDialog.setMessage(context.getString(R.string.transfer_executed));
                break;
            case 12:
                alertDialog.setMessage(context.getString(R.string.transfer_canceled));
                break;
            case 13:
                alertDialog.setMessage(context.getString(R.string.insufficientBalance));
                break;
        }
    }

    public static boolean userFormatAccepted(String string) {
        return (!string.matches("\\d+(?:\\.\\d+)?"));
    }
}