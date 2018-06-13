package es.dosxmil.partesexit.mapeo;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Pair;

import java.util.ArrayList;

import es.dosxmil.partesexit.MainActivity;
import es.dosxmil.partesexit.utils.Utils;

public class ParteLinea {

    private long id;

    private int CodigoEmpresa;
    private int EjercicioParte;
    private String SerieParte;
    private int NumeroParte;
    private int Orden;
    private String CodigoArticulo;
    private String DescripcionArticulo;
    private String DescripcionLinea;
    private String FechaParte;
    private String FechaRegistro;
    private String FechaEntrega;
    private int CodigoEmpleado;
    private String NombreCompleto;
    private double Precio;
    private double Importe;
    private int Gastos;
    private double Unidades;
    private int Facturable;

    private static String[] columnas = new String[]{"_id","CodigoEmpresa","EjercicioParte","SerieParte","NumeroParte",
                                            "Orden","CodigoArticulo","DescripcionArticulo", "DescripcionLinea","FechaParte",
                                            "FechaRegistro","MIL_FechaEntrega","CodigoEmpleado","NombreCompleto","Precio",
                                            "Importe","Gastos", "Unidades", "Facturable"};


    public ParteLinea(int codigoEmpresa, int ejercicioParte, String serieParte, int numeroParte, int orden,
                      String codigoArticulo, String descripcionArticulo, String descripcionLinea, String fechaParte,
                      String fechaRegistro, String fechaEntrega, int codigoEmpleado, String nombreCompleto,
                      double precio, double importe, int gastos, double unidades, int facturable) {
        this.CodigoEmpresa = codigoEmpresa;
        this.EjercicioParte = ejercicioParte;
        this.SerieParte = serieParte;
        this.NumeroParte = numeroParte;
        this.Orden = orden;
        this.CodigoArticulo = codigoArticulo;
        this.DescripcionArticulo = descripcionArticulo;
        this.DescripcionLinea = descripcionLinea;
        this.FechaParte = fechaParte;
        this.FechaRegistro = fechaRegistro;
        this.FechaEntrega = fechaEntrega;
        this.CodigoEmpleado = codigoEmpleado;
        this.NombreCompleto = nombreCompleto;
        this.Precio = precio;
        this.Importe = importe;
        this.Gastos = gastos;
        this.Unidades = unidades;
        this.Facturable = facturable;
    }

    public void setCodigoEmpresa(int codigoEmpresa) {
        this.CodigoEmpresa = codigoEmpresa;
    }

    public void setEjercicioParte(int ejercicioParte) {
        this.EjercicioParte = ejercicioParte;
    }

    public void setSerieParte(String serieParte) {
        this.SerieParte = serieParte;
    }

    public void setNumeroParte(int numeroParte) {
        this.NumeroParte = numeroParte;
    }

    public void setOrden(int orden) {
        this.Orden = orden;
    }

    public void setCodigoArticulo(String codigoArticulo) {
        this.CodigoArticulo = codigoArticulo;
    }

    public void setDescripcionArticulo(String descripcionArticulo) {
        this.DescripcionArticulo = descripcionArticulo;
    }

    public void setDescripcionLinea(String descripcionLinea) {
        this.DescripcionLinea = descripcionLinea;
    }

    public void setFechaParte(String fechaParte) {
        this.FechaParte = fechaParte;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.FechaRegistro = fechaRegistro;
    }

    public void setFechaEntrega(String fechaEntrega) {
        this.FechaEntrega = fechaEntrega;
    }

    public void setCodigoEmpleado(int codigoEmpleado) {
        this.CodigoEmpleado = codigoEmpleado;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.NombreCompleto = nombreCompleto;
    }

    public void setPrecio(double precio) {
        this.Precio = precio;
    }

    public void setImporte(double importe) {
        this.Importe = importe;
    }

    public void setGastos(int gastos) {
        this.Gastos = gastos;
    }

    public void setUnidades(double unidades) {
        this.Unidades = unidades;
    }

    public void setFacturable(int facturable) {
        this.Facturable = facturable;
    }

    public int getCodigoEmpresa() {
        return CodigoEmpresa;
    }

    public int getEjercicioParte() {
        return EjercicioParte;
    }

    public String getSerieParte() {
        return SerieParte;
    }

    public int getNumeroParte() {
        return NumeroParte;
    }

    public int getOrden() {
        return Orden;
    }

    public String getCodigoArticulo() {
        return CodigoArticulo;
    }

    public String getDescripcionArticulo() {
        return DescripcionArticulo;
    }

    public String getDescripcionLinea() { return DescripcionLinea; }

    public String getFechaParte() {
        return FechaParte;
    }

    public String getFechaRegistro() {
        return FechaRegistro;
    }

    public String getFechaEntrega() {
        return FechaEntrega;
    }

    public int getCodigoEmpleado() {
        return CodigoEmpleado;
    }

    public String getNombreCompleto() {
        return NombreCompleto;
    }

    public double getPrecio() {
        return Precio;
    }

    public double getImporte() {
        return Importe;
    }

    public int getGastos() {
        return Gastos;
    }

    public double getUnidades() {
        return Unidades;
    }

    public int getFacturable() {
        return Facturable;
    }

    public long getId() {
        Cursor c = MainActivity.getDb().query("ParteLineas",columnas,
                "CodigoEmpresa = ? AND EjercicioParte = ? AND SerieParte = ? AND NumeroParte = ? AND Orden = ?",
                new String[]{CodigoEmpresa +"", EjercicioParte +"", SerieParte +"",NumeroParte+"", Orden +""}, null, null, null);
        if (c.moveToFirst())
            id = c.getLong(0);
        else
            id = 0;

        return id;
    }

    public static Cursor getAllCursor(int codigoEmpresa, int ejercicioParte, String serieParte, int numeroParte) {
        return MainActivity.getDb().query("ParteLineas",columnas,
                "CodigoEmpresa = ? AND EjercicioParte = ? AND SerieParte = ? AND numeroParte = ?",
                new String[]{codigoEmpresa+"",ejercicioParte+"",serieParte,numeroParte+"" },
                null,null,null);
    }

    public static void guardar(ParteLinea pl) {

        ContentValues cv = new ContentValues();

        pl.putCv(cv);

        MainActivity.getDb().insert("ParteLineas", null, cv);

    }

    public static void actualizar(ParteLinea pl) {
        ContentValues cv = new ContentValues();
        pl.putCv(cv);
        MainActivity.getDb().update("ParteLineas",cv,"_id=?", new String[]{String.valueOf(pl.getId())});
    }

    public static ParteLinea getLineaPorID(long id) {
        Cursor c = MainActivity.getDb().query(
                "ParteLineas",columnas,"_id=?",new String[]{id+""},
                null,null,null);

            if (c.moveToFirst()) return new ParteLinea(c.getInt(1), c.getInt(2),
                    c.getString(3), c.getInt(4), c.getInt(5),
                    c.getString(6), c.getString(7), c.getString(8),
                    c.getString(9), c.getString(10), c.getString(11),
                    c.getInt(12), c.getString(13), c.getDouble(14),
                    c.getDouble(15), c.getInt(16), c.getDouble(17),
                    c.getInt(18));

            else return null;
    }


    public static int getLastOrdenPl(ParteCabecera pc) {
        Cursor c = MainActivity.getDb().rawQuery("SELECT max(Orden) FROM ParteLineas pl" +
                " WHERE pl.NumeroParte = ? AND pl.EjercicioParte = ? AND pl.SerieParte = ? AND CodigoEmpresa = ?;",
                new String[]{pc.getNumeroParte()+"",pc.getEjercicioParte()+"",pc.getSerieParte(),pc.getCodigoEmpresa()+""});

        if (c.moveToNext()) return c.getInt(0);
        else return 0;

    }

    public static int borrarPartePorID(ArrayList<Long> lista) {
        int borrados = 0;
        for (long l:lista) {
            MainActivity.getDb().delete("ParteLineas","_id = ?",new String[]{l+""});
            borrados++;
        }
        return borrados;
    }

    private void putCv(ContentValues cv) {
        cv.put(columnas[1], this.CodigoEmpresa);
        cv.put(columnas[2], this.EjercicioParte);
        cv.put(columnas[3], this.SerieParte);
        cv.put(columnas[4], this.NumeroParte);
        cv.put(columnas[5], this.Orden);
        cv.put(columnas[6], this.CodigoArticulo);
        cv.put(columnas[7], this.DescripcionArticulo);
        cv.put(columnas[8], this.DescripcionLinea);
        cv.put(columnas[9], this.FechaParte);
        cv.put(columnas[10], this.FechaRegistro);
        cv.put(columnas[11], this.FechaEntrega);
        cv.put(columnas[12], this.CodigoEmpleado);
        cv.put(columnas[13], this.NombreCompleto);
        cv.put(columnas[14], this.Precio);
        cv.put(columnas[15], this.Importe);
        cv.put(columnas[16], this.Gastos);
        cv.put(columnas[17], this.Unidades);
        cv.put(columnas[18], this.Facturable);
    }

    public Pair<String, String>[] getArrayValores() {

        Pair[] v = new Pair[]{
                new Pair<String, String>("CodigoEmpresa", this.CodigoEmpresa + ""),
                new Pair<String, String>("EjercicioParte", this.EjercicioParte + ""),
                new Pair<String, String>("SerieParte", this.SerieParte + ""),
                new Pair<String, String>("NumeroParte", this.NumeroParte + ""),
                new Pair<String, String>("Orden", this.Orden + ""),
                new Pair<String, String>("CodigoArticulo", this.CodigoArticulo),
                new Pair<String, String>("DescripcionArticulo", this.DescripcionArticulo),
                new Pair<String, String>("DescripcionLinea", this.DescripcionLinea),
                new Pair<String, String>("FechaParte", Utils.FormatoSincronizacion(this.FechaParte)),
                new Pair<String, String>("FechaRegistro", Utils.FormatoSincronizacion(this.FechaRegistro)),
                new Pair<String, String>("CodigoEmpleado", this.CodigoEmpleado + ""),
                new Pair<String, String>("NombreCompleto", this.NombreCompleto),
                new Pair<String, String>("Unidades", this.Unidades + ""),
                new Pair<String, String>("Precio", this.Precio + ""),
                new Pair<String, String>("Importe", this.Importe + ""),
                new Pair<String, String>("Facturable", this.Facturable + ""),
                new Pair<String, String>("Gastos", this.Gastos + ""),
                new Pair<String, String>("Mil_FechaEntrega", Utils.FormatoSincronizacion(this.FechaEntrega))
        };

        return v;
    }
}


