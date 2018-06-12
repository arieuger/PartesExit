package es.dosxmil.partesexit.mapeo;

import android.content.ContentValues;
import android.database.Cursor;

import es.dosxmil.partesexit.MainActivity;

public class Cliente {

    private int codigoEmpresa;
    private String codigoCliente;
    private String siglaNacion;
    private String cifDni;
    private String cifEuropeo;
    private String razonSocial;
    private String nombre;
    private String domicilio;
    private int codigoCondiciones;

    private static String[] columnas = new String[]{"_id","CodigoEmpresa","CodigoCliente","SiglaNacion",
            "CifDni","CifEuropeo","RazonSocial","Nombre","Domicilio", "CodigoCondiciones"};


    public Cliente(int codigoEmpresa, String codigoCliente, String siglaNacion, String cifDni, String cifEuropeo, String razonSocial, String nombre, String domicilio, int codigoCondiciones) {
        this.codigoEmpresa = codigoEmpresa;
        this.codigoCliente = codigoCliente;
        this.siglaNacion = siglaNacion;
        this.cifDni = cifDni;
        this.cifEuropeo = cifEuropeo;
        this.razonSocial = razonSocial;
        this.nombre = nombre;
        this.domicilio = domicilio;
        this.codigoCondiciones = codigoCondiciones;
    }

    public static Cliente clientePorCodigo(String codigo) {
        Cliente cliente = null;

        Cursor c = MainActivity.getDb().query("Clientes",columnas,"CodigoCliente = ?",new String[]{codigo+""},null,null,null);
        if (c.moveToFirst()) {
            cliente = new Cliente(c.getInt(1),
                    c.getString(2),
                    c.getString(3),
                    c.getString(4),
                    c.getString(5),
                    c.getString(6),
                    c.getString(7),
                    c.getString(8),
                    c.getInt(9));
        }

        return cliente;
    }

    public static Cliente clientePorCifDni(String cifDni) {
        Cliente cliente = null;

        Cursor c = MainActivity.getDb().query("Clientes",columnas,"CifDni = ?",new String[]{cifDni+""},null,null,null);
        if (c.moveToFirst()) {
            cliente = new Cliente(c.getInt(1),
                    c.getString(2),
                    c.getString(3),
                    c.getString(4),
                    c.getString(5),
                    c.getString(6),
                    c.getString(7),
                    c.getString(8),
                    c.getInt(9));
        }

        return cliente;
    }


    public static void guardar(Cliente c) {
        ContentValues cv = new ContentValues();
        cv.put(columnas[1], c.codigoEmpresa);
        cv.put(columnas[2], c.codigoCliente);
        cv.put(columnas[3], c.siglaNacion);
        cv.put(columnas[4], c.cifDni);
        cv.put(columnas[5], c.cifEuropeo);
        cv.put(columnas[6], c.razonSocial);
        cv.put(columnas[7], c.nombre);
        cv.put(columnas[8], c.domicilio);
        cv.put(columnas[9], c.codigoCondiciones);

        MainActivity.getDb().insert("Clientes",null,cv);
    }

    public int getCodigoEmpresa() {
        return codigoEmpresa;
    }

    public String getCodigoCliente() {
        return codigoCliente;
    }

    public String getSiglaNacion() {
        return siglaNacion;
    }

    public String getCifDni() {
        return cifDni;
    }

    public String getCifEuropeo() {
        return cifEuropeo;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public int getCodigoCondiciones() {
        return codigoCondiciones;
    }

    public static Cursor getAllCursor() {
        return MainActivity.getDb().query("Clientes",columnas,null,null,null,null,null);
    }
}
