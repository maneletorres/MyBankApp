package mibancooperacional.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;

import java.util.ArrayList;

import mibancooperacional.bd.Constants;
import mibancooperacional.bd.MiBD;
import mibancooperacional.pojo.ATM;

public class CajeroDAO implements PojoDAO {
    @Override
    public long add(Object obj) {
        ContentValues contentValues = new ContentValues();
        ATM a = (ATM) obj;
        contentValues.put(Constants.FIELD_NAME, a.getName());
        contentValues.put(Constants.FIELD_ADRESS, a.getAddress());
        contentValues.put(Constants.FIELD_POSTAL_CODE, a.getPostalCode());
        contentValues.put(Constants.FIELD_LOCATION, a.getLocation());
        contentValues.put(Constants.FIELD_LATITUDE, a.getLatitude());
        contentValues.put(Constants.FIELD_LONGITUDE, a.getLongitude());

        return MiBD.getDB().insert(Constants.CAJEROS_TABLE, null, contentValues);
    }

    @Override
    public int update(Object obj) {
        ContentValues contentValues = new ContentValues();
        ATM a = (ATM) obj;
        contentValues.put("name", a.getName());
        contentValues.put("address", a.getAddress());
        contentValues.put("postalCode", a.getPostalCode());
        contentValues.put("location", a.getLocation());
        contentValues.put("latitude", a.getLatitude());
        contentValues.put("longitude", a.getLongitude());

        String condition = "id=" + String.valueOf(a.getId());
        return MiBD.getDB().update("cajeros", contentValues, condition, null);
    }

    @Override
    public void delete(Object obj) {
        ATM a = (ATM) obj;
        String condition = "id=" + String.valueOf(a.getId());

        MiBD.getDB().delete("cajeros", condition, null);
    }

    @Override
    public Object search(Object obj) {
        ATM a = (ATM) obj;
        String condition;
        if (TextUtils.isEmpty(a.getName())) {
            condition = "id=" + String.valueOf(a.getId());
        } else {
            condition = "name=" + "'" + a.getName() + "'";
        }

        String[] columns = {"id", "name", "address", "postalCode", "location", "latitude", "longitude"};
        Cursor cursor = MiBD.getDB().query("cajeros", columns, condition, null, null, null, null);
        ATM newATM = null;
        if (cursor.moveToFirst()) {
            newATM = new ATM();
            newATM.setId(cursor.getInt(0));
            newATM.setName(cursor.getString(1));
            newATM.setAddress(cursor.getString(2));
            newATM.setPostalCode(cursor.getString(3));
            newATM.setLocation(cursor.getString(4));
            newATM.setLatitude(cursor.getFloat(5));
            newATM.setLongitude(cursor.getFloat(6));
        }
        cursor.close();
        return newATM;
    }

    @Override
    public ArrayList getAll() {
        ArrayList<ATM> ATMsList = new ArrayList<>();
        String[] columnas = {
                "id", "name", "address", "postalCode", "location", "latitude", "longitude"
        };
        Cursor cursor = MiBD.getDB().query("cajeros", columnas, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                ATM atm = new ATM();
                atm.setId(cursor.getInt(0));
                atm.setName(cursor.getString(1));
                atm.setAddress(cursor.getString(2));
                atm.setPostalCode(cursor.getString(3));
                atm.setLocation(cursor.getString(4));
                atm.setLatitude(cursor.getFloat(5));
                atm.setLongitude(cursor.getFloat(6));
                ATMsList.add(atm);

            } while (cursor.moveToNext());
        }
        cursor.close();
        return ATMsList;
    }
}