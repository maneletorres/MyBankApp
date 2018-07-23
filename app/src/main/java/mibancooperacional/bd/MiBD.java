package mibancooperacional.bd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import mibancooperacional.dao.CajeroDAO;
import mibancooperacional.dao.ClienteDAO;
import mibancooperacional.dao.CuentaDAO;
import mibancooperacional.dao.MovimientoDAO;

public class MiBD extends SQLiteOpenHelper {
    //Database name:
    private static final String database = "MiBanco";

    // Database version:
    private static final int version = 11;
    private static SQLiteDatabase db;
    private static MiBD instance = null;
    private static ClienteDAO clienteDAO;
    private static CuentaDAO cuentaDAO;
    private static MovimientoDAO movimientoDAO;
    private static CajeroDAO cajeroDAO;

    // Instrucción SQL para crear la tabla de Clientes:
    private String sqlCreacionClientes = "CREATE TABLE clientes ( id INTEGER PRIMARY KEY AUTOINCREMENT, nif STRING, nombre STRING, " +
            "apellidos STRING, claveSeguridad STRING, email STRING);";

    // Instruccion SQL para crear la tabla de Cuentas:
    private String sqlCreacionCuentas = "CREATE TABLE cuentas ( id INTEGER PRIMARY KEY AUTOINCREMENT, banco STRING, sucursal STRING, " +
            "dc STRING, numerocuenta STRING, saldoactual FLOAT, idcliente INTEGER);";

    // Instruccion SQL para crear la tabla de movimientos:
    private String sqlCreacionMovimientos = "CREATE TABLE movimientos ( id INTEGER PRIMARY KEY AUTOINCREMENT, tipo INTEGER, fechaoperacion LONG," +
            " descripcion STRING, importe FLOAT, idcuentaorigen INTEGER, idcuentadestino INTEGER);";

    // Instruccion SQL para crear la tabla de cajeros:
    private String sqlCreacionCajeros = "CREATE TABLE cajeros ( id INTEGER PRIMARY KEY AUTOINCREMENT, name STRING, " +
            " address STRING, postalCode STRING, location STRING, latitude FLOAT, longitude FLOAT);";

    /**
     * Constructor de clase
     */
    protected MiBD(Context context) {
        super(context, database, null, version);
    }

    public static MiBD getInstance(Context context) {
        if (instance == null) {
            instance = new MiBD(context);
            db = instance.getWritableDatabase();
            clienteDAO = new ClienteDAO();
            cuentaDAO = new CuentaDAO();
            movimientoDAO = new MovimientoDAO();
            cajeroDAO = new CajeroDAO();
        }
        return instance;
    }

    public static SQLiteDatabase getDB() {
        return db;
    }

    public static void closeDB() {
        db.close();
    }

    public ClienteDAO getClienteDAO() {
        return clienteDAO;
    }

    public CuentaDAO getCuentaDAO() {
        return cuentaDAO;
    }

    public MovimientoDAO getMovimientoDAO() {
        return movimientoDAO;
    }

    public CajeroDAO getCajeroDAO() {
        return cajeroDAO;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlCreacionClientes);
        db.execSQL(sqlCreacionCuentas);
        db.execSQL(sqlCreacionMovimientos);
        db.execSQL(sqlCreacionCajeros);
        insercionDatos(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("SQLite", "Control de versiones: Old Version=" + oldVersion + " New Version= " + newVersion);
        if (newVersion > oldVersion) {
            // The tables are deleted:
            db.execSQL("DROP TABLE IF EXISTS clientes");
            db.execSQL("DROP TABLE IF EXISTS cuentas");
            db.execSQL("DROP TABLE IF EXISTS movimientos");
            db.execSQL("DROP TABLE IF EXISTS cajeros");

            // The new tables are created:
            db.execSQL(sqlCreacionClientes);
            db.execSQL(sqlCreacionCuentas);
            db.execSQL(sqlCreacionMovimientos);
            db.execSQL(sqlCreacionCajeros);

            insercionDatos(db);
        }
    }

    private void insercionDatos(SQLiteDatabase db) {
        // Insertamos los clientes:
        db.execSQL("INSERT INTO clientes (id, nif, nombre, apellidos, claveSeguridad, email) VALUES (1, '11111111A', 'Filemón', 'Pí', '1234', 'filemon.pi@tia.es');");
        db.execSQL("INSERT INTO clientes (id, nif, nombre, apellidos, claveSeguridad, email) VALUES (2, '22222222B', 'Mortadelo', 'Ibáñez', '1234', 'mortadelo.ibanez@tia.es');");
        db.execSQL("INSERT INTO clientes (id, nif, nombre, apellidos, claveSeguridad, email) VALUES (3, '33333333C', 'Vicente', 'Mondragón', '1234', 'vicente.mondragon@tia.es');");
        db.execSQL("INSERT INTO clientes (id, nif, nombre, apellidos, claveSeguridad, email) VALUES (4, '44444444D', 'Ayrton', 'Senna', '1234', 'ayrton.senna@f1.es');");
        db.execSQL("INSERT INTO clientes (id, nif, nombre, apellidos, claveSeguridad, email) VALUES (5, 'B1111111A', 'Ibertrola', '-', '1234', '-');");
        db.execSQL("INSERT INTO clientes (id, nif, nombre, apellidos, claveSeguridad, email) VALUES (6, 'B2222222B', 'Gas Natural', '-', '1234', '-');");
        db.execSQL("INSERT INTO clientes (id, nif, nombre, apellidos, claveSeguridad, email) VALUES (7, 'B3333333C', 'Telefónica', '-', '1234', '-');");
        db.execSQL("INSERT INTO clientes (id, nif, nombre, apellidos, claveSeguridad, email) VALUES (8, 'B4444444D', 'Aguas de Valencia', '-', '1234', '-');");
        db.execSQL("INSERT INTO clientes (id, nif, nombre, apellidos, claveSeguridad, email) VALUES (9, 'B5555555E', 'Audi', '-', '1234', '-');");
        db.execSQL("INSERT INTO clientes (id, nif, nombre, apellidos, claveSeguridad, email) VALUES (10, 'B6666666F', 'BMW', '-', '1234', '-');");
        db.execSQL("INSERT INTO clientes (id, nif, nombre, apellidos, claveSeguridad, email) VALUES (11, 'B7777777G', 'PayPal', '-', '1234', '-');");
        db.execSQL("INSERT INTO clientes (id, nif, nombre, apellidos, claveSeguridad, email) VALUES (12, 'B8888888H', 'Ayuntamiento de Valencia', '-', '1234', '-');");

        // Insertamos las cuentas:
        db.execSQL("INSERT INTO cuentas (rowid, id, banco, sucursal, dc, numerocuenta, saldoactual, idcliente) VALUES (null, null, '1001', '1001', '11', '1000000001', 1500, 1);");
        db.execSQL("INSERT INTO cuentas (rowid, id, banco, sucursal, dc, numerocuenta, saldoactual, idcliente) VALUES (null, null, '1001', '1001', '11', '1000000003', -1200, 1);");
        db.execSQL("INSERT INTO cuentas (rowid, id, banco, sucursal, dc, numerocuenta, saldoactual, idcliente) VALUES (null, null, '1001', '1001', '11', '1000000002', 3500, 1);");
        db.execSQL("INSERT INTO cuentas (rowid, id, banco, sucursal, dc, numerocuenta, saldoactual, idcliente) VALUES (null, null, '1001', '1001', '11', '1000000004', 15340, 2);");
        db.execSQL("INSERT INTO cuentas (rowid, id, banco, sucursal, dc, numerocuenta, saldoactual, idcliente) VALUES (null, null, '1001', '1001', '11', '1000000005', 23729.23, 1);");
        db.execSQL("INSERT INTO cuentas (rowid, id, banco, sucursal, dc, numerocuenta, saldoactual, idcliente) VALUES (null, null, '1001', '1001', '11', '1000000006', 6500, 1);");
        db.execSQL("INSERT INTO cuentas (rowid, id, banco, sucursal, dc, numerocuenta, saldoactual, idcliente) VALUES (null, null, '1001', '1001', '11', '1000000007', 9500, 2);");
        db.execSQL("INSERT INTO cuentas (rowid, id, banco, sucursal, dc, numerocuenta, saldoactual, idcliente) VALUES (null, null, '1001', '1001', '11', '1000000008', 7500, 3);");
        db.execSQL("INSERT INTO cuentas (rowid, id, banco, sucursal, dc, numerocuenta, saldoactual, idcliente) VALUES (null, null, '1001', '1001', '11', '1000000009', 24650, 1);");
        db.execSQL("INSERT INTO cuentas (rowid, id, banco, sucursal, dc, numerocuenta, saldoactual, idcliente) VALUES (null, null, '1001', '1001', '11', '1000000010', -3500, 3);");
        db.execSQL("INSERT INTO cuentas (rowid, id, banco, sucursal, dc, numerocuenta, saldoactual, idcliente) VALUES (null, null, '1001', '2001', '22', '2000000001', 7530543.75, 5);");
        db.execSQL("INSERT INTO cuentas (rowid, id, banco, sucursal, dc, numerocuenta, saldoactual, idcliente) VALUES (null, null, '1001', '2001', '22', '2000000002', 15890345.87, 6);");
        db.execSQL("INSERT INTO cuentas (rowid, id, banco, sucursal, dc, numerocuenta, saldoactual, idcliente) VALUES (null, null, '1001', '2001', '22', '2000000003', 19396420.30, 7);");
        db.execSQL("INSERT INTO cuentas (rowid, id, banco, sucursal, dc, numerocuenta, saldoactual, idcliente) VALUES (null, null, '1001', '2001', '22', '2000000004', 1250345.23, 8);");
        db.execSQL("INSERT INTO cuentas (rowid, id, banco, sucursal, dc, numerocuenta, saldoactual, idcliente) VALUES (null, null, '1001', '2001', '22', '2000000005', 24387299.23, 9);");
        db.execSQL("INSERT INTO cuentas (rowid, id, banco, sucursal, dc, numerocuenta, saldoactual, idcliente) VALUES (null, null, '1001', '2001', '22', '2000000006', 15904387.45, 10);");
        db.execSQL("INSERT INTO cuentas (rowid, id, banco, sucursal, dc, numerocuenta, saldoactual, idcliente) VALUES (null, null, '1001', '2001', '22', '2000000007', 156398452.87, 18);");
        db.execSQL("INSERT INTO cuentas (rowid, id, banco, sucursal, dc, numerocuenta, saldoactual, idcliente) VALUES (null, null, '1001', '2001', '22', '2000000008', 2389463.98, 19);");

        // Insertamos los movimientos:
        db.execSQL("INSERT INTO movimientos (rowid, id, tipo, fechaoperacion, descripcion, importe, idcuentaorigen, idcuentadestino) VALUES (null, null, 0, 1420153380000, 'Recibo Iberdrola Diciembre 2014', -73.87, 1, 5);");
        db.execSQL("INSERT INTO movimientos (rowid, id, tipo, fechaoperacion, descripcion, importe, idcuentaorigen, idcuentadestino) VALUES (null, null, 0, 1420153380000, 'Recibo Iberdrola Diciembre 2014', -186.86, 2, 5);");
        db.execSQL("INSERT INTO movimientos (rowid, id, tipo, fechaoperacion, descripcion, importe, idcuentaorigen, idcuentadestino) VALUES (null, null, 0, 1420153380000, 'Recibo Iberdrola Diciembre 2014', -96.36, 3, 5);");
        db.execSQL("INSERT INTO movimientos (rowid, id, tipo, fechaoperacion, descripcion, importe, idcuentaorigen, idcuentadestino) VALUES (null, null, 0, 1420153380000, 'Recibo Iberdrola Diciembre 2014', -84.84, 4, 5);");
        db.execSQL("INSERT INTO movimientos (rowid, id, tipo, fechaoperacion, descripcion, importe, idcuentaorigen, idcuentadestino) VALUES (null, null, 0, 1420326180000, 'Recibo Gas Natural Diciembre 2014', -340.39, 1, 6);");
        db.execSQL("INSERT INTO movimientos (rowid, id, tipo, fechaoperacion, descripcion, importe, idcuentaorigen, idcuentadestino) VALUES (null, null, 1, 1420326180000, 'Devolución Telefónica Diciembre 2014', 30.76, 19, 1);");
        db.execSQL("INSERT INTO movimientos (rowid, id, tipo, fechaoperacion, descripcion, importe, idcuentaorigen, idcuentadestino) VALUES (null, null, 0, 1420412580000, 'Recibo BMW Diciembre 2014', -256.65, 1, 10);");
        db.execSQL("INSERT INTO movimientos (rowid, id, tipo, fechaoperacion, descripcion, importe, idcuentaorigen, idcuentadestino) VALUES (null, null, 2, 1420498980000, 'Reintegro cajero', -70, 1, -1);");
        db.execSQL("INSERT INTO movimientos (rowid, id, tipo, fechaoperacion, descripcion, importe, idcuentaorigen, idcuentadestino) VALUES (null, null, 1, 1420498980000, 'Ingreso Nómina Ayuntamiento Valencia Diciembre 2014', 2150.50, 19, 1);");
        db.execSQL("INSERT INTO movimientos (rowid, id, tipo, fechaoperacion, descripcion, importe, idcuentaorigen, idcuentadestino) VALUES (null, null, 0, 1420585380000, 'Recibo Telefónica Diciembre 2014', -96.32, 1, 7);");
        db.execSQL("INSERT INTO movimientos (rowid, id, tipo, fechaoperacion, descripcion, importe, idcuentaorigen, idcuentadestino) VALUES (null, null, 2, 1421103780000, 'Reintegro cajero', -150, 1, -1);");
        db.execSQL("INSERT INTO movimientos (rowid, id, tipo, fechaoperacion, descripcion, importe, idcuentaorigen, idcuentadestino) VALUES (null, null, 0, 1421362980000, 'Pago Paypal. ID: 2379646853', -98.47, 1, 18);");
        db.execSQL("INSERT INTO movimientos (rowid, id, tipo, fechaoperacion, descripcion, importe, idcuentaorigen, idcuentadestino) VALUES (null, null, 2, 1421622180000, 'Reintegro cajero', -150, 1, -1);");
        db.execSQL("INSERT INTO movimientos (rowid, id, tipo, fechaoperacion, descripcion, importe, idcuentaorigen, idcuentadestino) VALUES (null, null, 0, 1422054180000, 'Recibo IBI Ayuntamiento Valencia 2015', -358.36, 1, 18);");
        db.execSQL("INSERT INTO movimientos (rowid, id, tipo, fechaoperacion, descripcion, importe, idcuentaorigen, idcuentadestino) VALUES (null, null, 2, 1422486180000, 'Reintegro cajero', -70, 1, -1);");
        db.execSQL("INSERT INTO movimientos (rowid, id, tipo, fechaoperacion, descripcion, importe, idcuentaorigen, idcuentadestino) VALUES (null, null, 0, 1422831780000, 'Recibo Iberdrola Enero 2015', -95.34, 1, 5);");
        db.execSQL("INSERT INTO movimientos (rowid, id, tipo, fechaoperacion, descripcion, importe, idcuentaorigen, idcuentadestino) VALUES (null, null, 0, 1423004580000, 'Recibo Gas Natural Enero 2015', -65.34, 1, 6);");
        db.execSQL("INSERT INTO movimientos (rowid, id, tipo, fechaoperacion, descripcion, importe, idcuentaorigen, idcuentadestino) VALUES (null, null, 0, 1423090980000, 'Recibo BMW Enero 2015', -256.65, 1, 10);");
        db.execSQL("INSERT INTO movimientos (rowid, id, tipo, fechaoperacion, descripcion, importe, idcuentaorigen, idcuentadestino) VALUES (null, null, 2, 1423263780000, 'Reintegro cajero', -70, 1, -1);");
        db.execSQL("INSERT INTO movimientos (rowid, id, tipo, fechaoperacion, descripcion, importe, idcuentaorigen, idcuentadestino) VALUES (null, null, 1, 1423263780000, 'Ingreso Nómina Ayuntamiento Valencia Enero 2015', 2150.5, 19, 1);");

        // Insertamos los cajeros:
        db.execSQL("INSERT INTO cajeros(id, name, address, postalCode, location, latitude, longitude) VALUES(0, 'Banco Santander', 'Plaça Major, 34', '46713', 'Bellreguard', 38.9449123, -0.16153329999997368)");
        db.execSQL("INSERT INTO Cajeros(id, name, address, postalCode, location, latitude, longitude) VALUES(1, 'Bankia', 'Plaça Major, 7', '46713', 'Bellreguard', 38.9449642, -0.16115719999993416)");
        db.execSQL("INSERT INTO Cajeros(id, name, address, postalCode, location, latitude, longitude) VALUES(2, 'CaixaBank', 'C/Rei en Jaume I', '46713', 'Bellreguard', 38.9463754, -0.1611887999999908)");
        db.execSQL("INSERT INTO Cajeros(id, name, address, postalCode, location, latitude, longitude) VALUES(3, 'BBVA', 'C/Major, 58-66', '46701', 'Gandía', 38.9655136, -0.18114349999996193)");
        db.execSQL("INSERT INTO Cajeros(id, name, address, postalCode, location, latitude, longitude) VALUES(4, 'Banco de España', 'Carrer de les Barques, 6', '46002', 'Valencia', 39.4703012, -0.3746829000000389)");
        db.execSQL("INSERT INTO Cajeros(id, name, address, postalCode, location, latitude, longitude) VALUES(5, 'Banco Sabadell', 'Av. de Giorgeta, 17', '46007', 'Valencia', 39.4610881, -0.3855493999999453)");
        db.execSQL("INSERT INTO Cajeros(id, name, address, postalCode, location, latitude, longitude) VALUES(6, 'Banco Mediolanum', 'Avinguda de La Plata, 34', '46013', 'Valencia', 39.4561936, -0.3634346999999707)");
    }
}