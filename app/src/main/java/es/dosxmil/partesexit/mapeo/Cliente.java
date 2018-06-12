package es.dosxmil.partesexit.mapeo;

import android.content.ContentValues;
import android.database.Cursor;

import es.dosxmil.partesexit.MainActivity;

public class Cliente {

    private int CodigoEmpresa;
    private String CodigoCliente;
    private String SiglaNacion;
    private String CifDni;
    private String CifEuropeo;
    private String RazonSocial;
    private String Nombre;
    private String Domicilio;
    private int CodigoCondiciones;

    private static String[] columnas = new String[]{"_id","CodigoEmpresa","CodigoCliente","SiglaNacion",
            "CifDni","CifEuropeo","RazonSocial","Nombre","Domicilio", "CodigoCondiciones"};


    public Cliente(int codigoEmpresa, String codigoCliente, String siglaNacion, String cifDni, String cifEuropeo, String razonSocial, String nombre, String domicilio, int codigoCondiciones) {
        this.CodigoEmpresa = codigoEmpresa;
        this.CodigoCliente = codigoCliente;
        this.SiglaNacion = siglaNacion;
        this.CifDni = cifDni;
        this.CifEuropeo = cifEuropeo;
        this.RazonSocial = razonSocial;
        this.Nombre = nombre;
        this.Domicilio = domicilio;
        this.CodigoCondiciones = codigoCondiciones;
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
        cv.put(columnas[1], c.CodigoEmpresa);
        cv.put(columnas[2], c.CodigoCliente);
        cv.put(columnas[3], c.SiglaNacion);
        cv.put(columnas[4], c.CifDni);
        cv.put(columnas[5], c.CifEuropeo);
        cv.put(columnas[6], c.RazonSocial);
        cv.put(columnas[7], c.Nombre);
        cv.put(columnas[8], c.Domicilio);
        cv.put(columnas[9], c.CodigoCondiciones);

        MainActivity.getDb().insert("Clientes",null,cv);
    }

    public int getCodigoEmpresa() {
        return CodigoEmpresa;
    }

    public String getCodigoCliente() {
        return CodigoCliente;
    }

    public String getSiglaNacion() {
        return SiglaNacion;
    }

    public String getCifDni() {
        return CifDni;
    }

    public String getCifEuropeo() {
        return CifEuropeo;
    }

    public String getRazonSocial() {
        return RazonSocial;
    }

    public String getNombre() {
        return Nombre;
    }

    public String getDomicilio() {
        return Domicilio;
    }

    public int getCodigoCondiciones() {
        return CodigoCondiciones;
    }

    public static Cursor getAllCursor() {
        return MainActivity.getDb().query("Clientes",columnas,null,null,null,null,null);
    }
}
