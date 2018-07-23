package mibancooperacional.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;

import java.util.ArrayList;

import mibancooperacional.bd.MiBD;
import mibancooperacional.pojo.Cliente;

public class ClienteDAO implements PojoDAO {
    @Override
    public long add(Object obj) {
        ContentValues contentValues = new ContentValues();
        Cliente c = (Cliente) obj;
        contentValues.put("nif", c.getNif());
        contentValues.put("nombre", c.getNombre());
        contentValues.put("apellidos", c.getApellidos());
        contentValues.put("claveSeguridad", c.getClaveSeguridad());
        contentValues.put("email", c.getEmail());
        return MiBD.getDB().insert("clientes", null, contentValues);
    }

    @Override
    public int update(Object obj) {
        ContentValues contentValues = new ContentValues();
        Cliente c = (Cliente) obj;
        contentValues.put("nif", c.getNif());
        contentValues.put("nombre", c.getNombre());
        contentValues.put("apellidos", c.getApellidos());
        contentValues.put("claveSeguridad", c.getClaveSeguridad());
        contentValues.put("email", c.getEmail());

        String condicion = "id=" + String.valueOf(c.getId());
        return MiBD.getDB().update("clientes", contentValues, condicion, null);
    }

    @Override
    public void delete(Object obj) {
        Cliente c = (Cliente) obj;
        String condicion = "id=" + String.valueOf(c.getId());

        MiBD.getDB().delete("clientes", condicion, null);
    }

    @Override
    public Object search(Object obj) {
        Cliente c = (Cliente) obj;
        String condicion;
        if (TextUtils.isEmpty(c.getNif())) {
            condicion = "id=" + String.valueOf(c.getId());
        } else {
            condicion = "nif=" + "'" + c.getNif() + "'";
        }

        String[] columnas = {"id", "nif", "nombre", "apellidos", "claveseguridad", "email"};
        Cursor cursor = MiBD.getDB().query("clientes", columnas, condicion, null, null, null, null);
        Cliente nuevoCliente = null;
        if (cursor.moveToFirst()) {
            nuevoCliente = new Cliente();
            nuevoCliente.setId(cursor.getInt(0));
            nuevoCliente.setNif(cursor.getString(1));
            nuevoCliente.setNombre(cursor.getString(2));
            nuevoCliente.setApellidos(cursor.getString(3));
            nuevoCliente.setClaveSeguridad(cursor.getString(4));
            nuevoCliente.setEmail(cursor.getString(5));
        }
        cursor.close();
        return nuevoCliente;
    }

    @Override
    public ArrayList getAll() {
        ArrayList<Cliente> listaClientes = new ArrayList<>();
        String[] columnas = {"id", "nif", "nombre", "apellidos", "claveseguridad", "email"};
        Cursor cursor = MiBD.getDB().query("clientes", columnas, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Cliente c = new Cliente();
                c.setId(cursor.getInt(0));
                c.setNif(cursor.getString(1));
                c.setNombre(cursor.getString(2));
                c.setApellidos(cursor.getString(3));
                c.setClaveSeguridad(cursor.getString(4));
                c.setEmail(cursor.getString(5));
                listaClientes.add(c);

            } while (cursor.moveToNext());
        }
        cursor.close();
        return listaClientes;
    }
}