package es.dosxmil.partesexit;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ExitDBOpenHelper extends SQLiteOpenHelper {

    public ExitDBOpenHelper(Context context) {
        super(context, "exit.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE ParteCabecera ( " +
                "_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                "CodigoEmpresa INTEGER NOT NULL, " +
                "EjercicioParte INTEGER NOT NULL, " +
                "SerieParte TEXT NOT NULL, " +
                "NumeroParte INTEGER NOT NULL, " +
                "StatusParte TEXT NOT NULL, " +
                "TipoParte TEXT NOT NULL, " +
                "DescripcionTipoParte TEXT NOT NULL, " +
                "CodigoArticulo TEXT NOT NULL, " +
                "DescripcionArticulo TEXT NOT NULL, " +
                "FechaParte TEXT NOT NULL, " +
                "FechaEjecucion TEXT NOT NULL, " +
                "CodigoEmpleado INTEGER NOT NULL, " +
                "NombreCompleto TEXT NOT NULL, " +
                "CodigoProyecto INTEGER NOT NULL, " +
                "NombreProyecto TEXT NOT NULL, " +
                "ComentarioCierre TEXT NOT NULL, " +
                "ComentarioRecepcion TEXT NOT NULL, " +
                "CodigoCliente TEXT NOT NULL, " +
                "Nombre TEXT NOT NULL, " +
                "Importe REAL NOT NULL, " +
                "ImporteGastos REAL NOT NULL, " +
                "ImporteFacturable REAL NOT NULL, " +
                "CodigoUsuario INTEGER NOT NULL, " +
                "FechaUltimaModificacion TEXT NOT NULL, " +
                "PLDescargado INT NOT NULL DEFAULT 0, " +
                "FechaCierre TEXT, " +
                "HoraCierre REAL NOT NULL DEFAULT 0, " +
                "TotalUnidades REAL NOT NULL DEFAULT 0, " +
                "NombreUsuarioCierre TEXT NOT NULL DEFAULT '', " +
                "CodigoUsuarioCierre INT NOT NULL DEFAULT 0, " +
                "SincroMovil INT NOT NULL DEFAULT 0)" +
                "");

        // "FechaCierre","HoraCierre","TotalUnidades","NombreUsuarioCierre",
        //            "CodigoUsuarioCierre"

        db.execSQL("CREATE TABLE ParteLineas ( " +
                "_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                "CodigoEmpresa INTEGER NOT NULL, " +
                "EjercicioParte INTEGER NOT NULL, " +
                "SerieParte TEXT NOT NULL, " +
                "NumeroParte INTEGER NOT NULL, " +
                "Orden INTEGER NOT NULL, " +
                "CodigoArticulo TEXT NOT NULL, " +
                "DescripcionArticulo TEXT NOT NULL, " +
                "DescripcionLinea TEXT NOT NULL DEFAULT \"\", " +
                "FechaParte TEXT NOT NULL, " +
                "FechaRegistro TEXT NOT NULL, " +
                "MIL_FechaEntrega TEXT, " +
                "CodigoEmpleado INTEGER NOT NULL, " +
                "NombreCompleto TEXT NOT NULL, " +
                "Precio REAL NOT NULL, " +
                "Importe REAL NOT NULL, " +
                "Gastos INT NOT NULL, " +
                "Unidades REAL NOT NULL, " +
                "Facturable INT NOT NULL)");

        db.execSQL("CREATE TABLE Clientes (" +
                "_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                "CodigoEmpresa INTEGER NOT NULL, " +
                "CodigoCliente TEXT NOT NULL, " +
                "SiglaNacion TEXT NOT NULL, " +
                "CifDni TEXT NOT NULL, " +
                "CifEuropeo TEXT NOT NULL, " +
                "RazonSocial TEXT NOT NULL, " +
                "Nombre TEXT NOT NULL, " +
                "Domicilio TEXT NOT NULL, " +
                "CodigoCondiciones INT NOT NULL)");

        db.execSQL("CREATE TABLE Articulos (" +
                "_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "CodigoArticulo TEXT NOT NULL, " +
                "DescripcionArticulo TEXT NOT NULL, " +
                "PrecioNetoVenta REAL NOT NULL DEFAULT 0)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
