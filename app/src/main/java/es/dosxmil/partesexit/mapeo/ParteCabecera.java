package es.dosxmil.partesexit.mapeo;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import es.dosxmil.partesexit.MainActivity;
import es.dosxmil.partesexit.utils.Utils;

public class ParteCabecera implements Parcelable {

    private long id;

    private int codigoEmpresa;
    private int ejercicioParte;
    private String serieParte;
    private int numeroParte;
    private String statusParte;
    private String tipoParte;
    private String descripcionTipoParte;
    private String codigoArticulo;
    private String descripcionArticulo;
    private String fechaParte;
    private String fechaEjecucion;
    private int codigoEmpleado;
    private String nombreCompleto;
    private int codigoProyecto;
    private String nombreProyecto;
    private String comentarioCierre;
    private String comentarioRecepcion;
    private String codigoCliente;
    private String nombre;
    private double importe;
    private double importeGastos;
    private double importeFacturable;
    private int codigoUsuario;
    private String fechaUltimaModificacion;
    private int plDescargado;
    private String fechaCierre;
    private double horaCierre;
    private double totalUnidades;
    private String nombreUsuarioCierre;
    private int codigoUsuarioCierre;
    private int sincroMovil;
    // TODO: FirmaParteEntrada (Imaxe)

    private static String[] columnas = {"_id","CodigoEmpresa","EjercicioParte","SerieParte","NumeroParte"
            ,"StatusParte","TipoParte","DescripcionTipoParte","CodigoArticulo","DescripcionArticulo","FechaParte",
            "FechaEjecucion", "CodigoEmpleado","NombreCompleto","CodigoProyecto","NombreProyecto","ComentarioCierre",
            "ComentarioRecepcion","CodigoCliente", "Nombre","Importe","ImporteGastos","ImporteFacturable","CodigoUsuario",
            "FechaUltimaModificacion","PLDescargado", "FechaCierre","HoraCierre","TotalUnidades","NombreUsuarioCierre",
            "CodigoUsuarioCierre", "SincroMovil"};


    public ParteCabecera(int codigoEmpresa, int ejercicioParte, String serieParte, int numeroParte,
                         String statusParte, String tipoParte, String descripcionTipoParte, String codigoArticulo,
                         String descripcionArticulo, String fechaParte, String fechaEjecucion, int codigoEmpleado,
                         String nombreCompleto, int codigoProyecto, String nombreProyecto, String comentarioCierre,
                         String comentarioRecepcion, String codigoCliente, String nombre, double importe, double importeGastos,
                         double importeFacturable, int codigoUsuario, String fechaUltimaModificacion, int plDescargado,
                         String fechaCierre, double horaCierre, double totalUnidades, String nombreUsuarioCierre,
                         int codigoUsuarioCierre, int sincroMovil) {
        this.codigoEmpresa = codigoEmpresa;
        this.ejercicioParte = ejercicioParte;
        this.serieParte = serieParte;
        this.numeroParte = numeroParte;
        this.statusParte = statusParte;
        this.tipoParte = tipoParte;
        this.descripcionTipoParte = descripcionTipoParte;
        this.codigoArticulo = codigoArticulo;
        this.descripcionArticulo = descripcionArticulo;
        this.fechaParte = fechaParte;
        this.fechaEjecucion = fechaEjecucion;
        this.codigoEmpleado = codigoEmpleado;
        this.nombreCompleto = nombreCompleto;
        this.codigoProyecto = codigoProyecto;
        this.nombreProyecto = nombreProyecto;
        this.comentarioCierre = comentarioCierre;
        this.comentarioRecepcion = comentarioRecepcion;
        this.codigoCliente = codigoCliente;
        this.nombre = nombre;
        this.importe = importe;
        this.importeGastos = importeGastos;
        this.importeFacturable = importeFacturable;
        this.codigoUsuario = codigoUsuario;
        this.fechaUltimaModificacion = fechaUltimaModificacion;
        this.plDescargado = plDescargado;
        this.fechaCierre = fechaCierre;
        this.horaCierre = horaCierre;
        this.totalUnidades = totalUnidades;
        this.nombreUsuarioCierre = nombreUsuarioCierre;
        this.codigoUsuarioCierre = codigoUsuarioCierre;
        this.sincroMovil = sincroMovil;
    }


    // CURSORES
    public static Cursor getAllCursor() {
        Cursor c = MainActivity.getDb().query("ParteCabecera", columnas, null,null,null, null, "EjercicioParte DESC, NumeroParte DESC");
        return c;
    }

    public static Cursor getStatusCursor(String status) {
        Cursor c = MainActivity.getDb().query("ParteCabecera", columnas,  "StatusParte=?", new String[]{status},null, null, "EjercicioParte DESC, NumeroParte DESC");
        return c;
    }

    public static Cursor partesPorKeyword(String query) {
        Cursor c = MainActivity.getDb().query("ParteCabecera",columnas,"Nombre LIKE ?", new String[]{"%"+ query+ "%"},null,null,"EjercicioParte DESC, NumeroParte DESC");
        return c;
    }


    // BÚSQUEDAS
    public static ParteCabecera getByID(long idParte) {
        ParteCabecera pc = null;

        Cursor c = MainActivity.getDb().query("ParteCabecera",columnas, "_id = ?", new String[]{idParte+""}, null, null, null);
        if (c.moveToFirst()) {
            pc = new ParteCabecera(
                    c.getInt(1),
                    c.getInt(2),
                    c.getString(3),
                    c.getInt(4),
                    c.getString(5),
                    c.getString(6),
                    c.getString(7),
                    c.getString(8),
                    c.getString(9),
                    c.getString(10),
                    c.getString(11),
                    c.getInt(12),
                    c.getString(13),
                    c.getInt(14),
                    c.getString(15),
                    c.getString(16),
                    c.getString(17),
                    c.getString(18),
                    c.getString(19),
                    c.getDouble(20),
                    c.getDouble(21),
                    c.getDouble(22),
                    c.getInt(23),
                    c.getString(24),
                    c.getInt(25),
                    c.getString(26),
                    c.getDouble(27),
                    c.getDouble(28),
                    c.getString(29),
                    c.getInt(30),
                    c.getInt(31)
            );
        }

        return pc;
    }

    public static int getLastNumeroParte() {
        int i = 0;
        Cursor c = MainActivity.getDb().rawQuery("SELECT max(NumeroParte) FROM ParteCabecera " +
                "WHERE CodigoEmpresa = 2 AND EjercicioParte = " +
                "(SELECT max(EjercicioParte) FROM ParteCabecera);", null);
        if (c.moveToNext())
            i = c.getInt(0);
        return i;
    }

    public static ArrayList<Pair<String, String>> buscarPorNombre(String pclave) {
        ArrayList<Pair<String, String>> resultado = new ArrayList<>();

        String sql = "SELECT CodigoCliente, Nombre FROM Clientes WHERE Nombre LIKE '%" + pclave + "%'";
        Cursor c = MainActivity.getDb().rawQuery(sql,null);
        while (c.moveToNext())
            resultado.add(new Pair<String, String>(c.getString(0),c.getString(1)));

        return resultado;
    }


    // GARDADO/MODIFICACIÓN
    public static void guardar(ParteCabecera pc) {

        ContentValues cv = new ContentValues();

        pc.putCv(cv);

        MainActivity.getDb().insert("ParteCabecera", null, cv);

    }

    public static void actualizar(ParteCabecera pc) {

        ContentValues cv = new ContentValues();
        pc.putCv(cv);

        MainActivity.getDb().update("ParteCabecera",cv,"_id=?",new String[]{pc.getId()+""});

    }

    public static void actualizaImportes(ParteCabecera pc) {
        Cursor c = ParteLinea.getAllCursor(pc.getCodigoEmpresa(),pc.getEjercicioParte(),pc.getSerieParte(),pc.getNumeroParte());

        double importe = 0, importeFacturable = 0, importeGastos = 0;

        while (c.moveToNext()) {
            ParteLinea pl = ParteLinea.getLineaPorID(c.getLong(0));
            importe += pl.getImporte();
            if (pl.getFacturable() != 0 ) importeFacturable += pl.getImporte();
            if (pl.getGastos() != 0 ) importeGastos += pl.getImporte();
        }

        pc.setImporte(importe);
        pc.setImporteFacturable(importeFacturable);
        pc.setImporteGastos(importeGastos);
        pc.setFechaUltimaModificacion(new Timestamp(new Date().getTime()).toString());
        ParteCabecera.actualizar(pc);
    }


    // BORRADOS
    // Para o borrado de un sólo parte: Dende DetallesParteActivity
    public static int borrarPartePorID(ArrayList<Long> lista) {
        int borrados = 0;
        for (long l:lista) {
            ParteCabecera pc = ParteCabecera.getByID(l);
            int lineasBorradas = MainActivity.getDb().delete("ParteLineas",
                    "CodigoEmpresa = ? AND EjercicioParte = ? AND SerieParte = ? AND NumeroParte = ? ",
                    new String[]{pc.getCodigoEmpresa()+"",pc.getEjercicioParte()+"",pc.getSerieParte(),pc.getNumeroParte()+""});
            Log.d("BORRADOPC",lineasBorradas+"");

            MainActivity.getDb().delete("ParteCabecera","_id = ?",new String[]{l+""});
            borrados++;
        }
        return borrados;
    }

    // Para o borrado de múltiples partes: Dende MainActivity
    public static void borrarPartePorID(long parteID) {
        ParteCabecera pc = ParteCabecera.getByID(parteID);
        MainActivity.getDb().delete("ParteLineas",
                "CodigoEmpresa = ? AND EjercicioParte = ? AND SerieParte = ? AND NumeroParte = ? ",
                new String[]{pc.getCodigoEmpresa()+"",pc.getEjercicioParte()+"",pc.getSerieParte(),pc.getNumeroParte()+""});

        MainActivity.getDb().delete("ParteCabecera","_id = ?",new String[]{parteID+""});
    }


    // SINCRONIZACIÓN
    public static ArrayList<ParteCabecera> partesSinSubir(String umodsrv) {
        ArrayList<ParteCabecera> pcs = new ArrayList<>();

        String sql = "SELECT * FROM ParteCabecera WHERE FechaUltimaModificacion > '" + umodsrv + "';";
        // String sql = "SELECT * FROM ParteCabecera WHERE SincroMovil = 0;";
        Log.d("SINCCOMP", "SQL: " + sql);
        Cursor c = MainActivity.getDb().rawQuery(sql,null);
        while (c.moveToNext())
            pcs.add(ParteCabecera.getByID(c.getLong(0)));

        return pcs;
    }

//    public static String getMaxFUM() {
//        String sql = "SELECT "
//    }




    // GETTERS
    public long getId() {
        Cursor c = MainActivity.getDb().query("ParteCabecera",columnas, "CodigoEmpresa = ? AND EjercicioParte = ? AND SerieParte = ? AND NumeroParte = ?", new String[]{codigoEmpresa+"",ejercicioParte+"",serieParte+"",numeroParte+""}, null, null, null);
        if (c.moveToFirst())
            id = c.getLong(0);
        else
            id = 0;

        return id;
    }


    public Pair<String, String>[] getArrayValores() {

        Log.d("SINC", "fechaCierre = " + fechaCierre);
        if (fechaCierre.equals("") || fechaCierre == null) fechaCierre = "1900-01-01 00:00:00";
        Log.d("SINC",fechaCierre );

        String estadoEjecucion = "";
        switch (this.statusParte) {
            case "A":
                estadoEjecucion= "En ejecucion";
                break;
            case "C":
                estadoEjecucion = "Finalizado";
                break;
        }

        Pair[] v = new Pair[]{
                new Pair<String, String>("CodigoEmpresa", this.codigoEmpresa+""),
                new Pair<String, String>("EjercicioParte",this.ejercicioParte+""),
                new Pair<String, String>("SerieParte",this.serieParte+""),
                new Pair<String, String>("NumeroParte",this.numeroParte+""),
                new Pair<String, String>("StatusParte",this.statusParte),
                new Pair<String, String>("CodigoArticulo",this.codigoArticulo),
                new Pair<String, String>("DescripcionArticulo",this.descripcionArticulo),
                new Pair<String, String>("FechaParte",Utils.FormatoSincronizacion(this.fechaParte)),
                new Pair<String, String>("CodigoEmpleado",this.codigoEmpleado+""),
                new Pair<String, String>("NombreCompleto",this.nombreCompleto),
                new Pair<String, String>("CodigoProyecto",this.codigoProyecto+""),
                new Pair<String, String>("NombreProyecto",this.nombreProyecto),
                new Pair<String, String>("ComentarioCierre",this.comentarioCierre),
                new Pair<String, String>("ComentarioRecepcion",this.comentarioRecepcion),
                new Pair<String, String>("CodigoCliente",this.codigoCliente),
                new Pair<String, String>("Importe",this.importe+""),
                new Pair<String, String>("ImporteGastos",this.importeGastos+""),
                new Pair<String, String>("ImporteFacturable",this.importeFacturable+""),
                new Pair<String, String>("FechaUltimaModificacion",Utils.FormatoSincronizacion(this.fechaUltimaModificacion)),
                new Pair<String, String>("FechaEjecucion",Utils.FormatoSincronizacion(this.fechaEjecucion)),
                new Pair<String, String>("EstadoEjecucion",estadoEjecucion),
                new Pair<String, String>("CodigoUsuarioCierre",this.codigoUsuarioCierre+""),
                new Pair<String, String>("FechaCierre",Utils.FormatoSincronizacion(fechaCierre)),
                new Pair<String, String>("HoraCierre",this.horaCierre+"")};

        return v;
    }

    public static String[] getColumnas() {
        return columnas;
    }
    public int getCodigoEmpresa() {
        return codigoEmpresa;
    }
    public int getEjercicioParte() {
        return ejercicioParte;
    }
    public String getSerieParte() {
        return serieParte;
    }
    public int getNumeroParte() {
        return numeroParte;
    }
    public String getStatusParte() {
        return statusParte;
    }
    public String getTipoParte() {
        return tipoParte;
    }
    public String getDescripcionTipoParte() {
        return descripcionTipoParte;
    }
    public String getCodigoArticulo() {
        return codigoArticulo;
    }
    public String getDescripcionArticulo() {
        return descripcionArticulo;
    }
    public String getFechaParte() {
        return fechaParte;
    }
    public String getFechaEjecucion() {
        return fechaEjecucion;
    }
    public int getCodigoEmpleado() {
        return codigoEmpleado;
    }
    public String getNombreCompleto() {
        return nombreCompleto;
    }
    public int getCodigoProyecto() {
        return codigoProyecto;
    }
    public String getNombreProyecto() {
        return nombreProyecto;
    }
    public String getComentarioCierre() {
        return comentarioCierre;
    }
    public String getComentarioRecepcion() {
        return comentarioRecepcion;
    }
    public String getCodigoCliente() {
        return codigoCliente;
    }
    public String getNombre() {
        return nombre;
    }
    public double getImporte() {
        return importe;
    }
    public double getImporteGastos() {
        return importeGastos;
    }
    public double getImporteFacturable() {
        return importeFacturable;
    }
    public int getCodigoUsuario() {
        return codigoUsuario;
    }
    public String getFechaUltimaModificacion() {
        return fechaUltimaModificacion;
    }
    public int getPlDescargado() {
        return plDescargado;
    }
    public double getHoraCierre() {
        return horaCierre;
    }
    public double getTotalUnidades() {
        return totalUnidades;
    }
    public String getNombreUsuarioCierre() {
        return nombreUsuarioCierre;
    }
    public int getCodigoUsuarioCierre() {
        return codigoUsuarioCierre;
    }
    public int getSincroMovil() {
            return sincroMovil;
    }

    // SETTERS
    public void setPlDescargado(int plDescargado) {
        this.plDescargado = plDescargado;
    }
    public void setStatusParte(String statusParte) {
        this.statusParte = statusParte;
    }
    public void setFechaParte(String fechaParte) {
        this.fechaParte = fechaParte;
    }
    public void setFechaEjecucion(String fechaEjecucion) {
        this.fechaEjecucion = fechaEjecucion;
    }
    public void setComentarioCierre(String comentarioCierre) {
        this.comentarioCierre = comentarioCierre;
    }
    public void setComentarioRecepcion(String comentarioRecepcion) {
        this.comentarioRecepcion = comentarioRecepcion;
    }
    public void setFechaUltimaModificacion(String fechaUltimaModificacion) {
        this.fechaUltimaModificacion = fechaUltimaModificacion;
    }
    public void setImporteFacturable(double importeFacturable) {
        this.importeFacturable = importeFacturable;
    }
    public void setImporteGastos(double importeGastos) {
        this.importeGastos = importeGastos;
    }
    public void setImporte(double importe) {
        this.importe = importe;
    }
    public String getFechaCierre() {
        return fechaCierre;
    }
    public void setFechaCierre(String fechaCierre) {
        this.fechaCierre = fechaCierre;
    }
    public void setHoraCierre(double horaCierre) {
        this.horaCierre = horaCierre;
    }
    public void setTotalUnidades(double totalUnidades) {
        this.totalUnidades = totalUnidades;
    }
    public void setNombreUsuarioCierre(String nombreUsuarioCierre) {
        this.nombreUsuarioCierre = nombreUsuarioCierre;
    }
    public void setCodigoUsuarioCierre(int codigoUsuarioCierre) {
        this.codigoUsuarioCierre = codigoUsuarioCierre;
    }
    public void setSincroMovil(int sincroMovil) {
        this.sincroMovil = sincroMovil;
    }

    // SERIALIZACIÓN
    protected ParteCabecera(Parcel in) {
        codigoEmpresa = in.readInt();
        ejercicioParte = in.readInt();
        serieParte = in.readString();
        numeroParte = in.readInt();
        statusParte = in.readString();
        tipoParte = in.readString();
        descripcionTipoParte = in.readString();
        codigoArticulo = in.readString();
        descripcionArticulo = in.readString();
        fechaParte = in.readString();
        fechaEjecucion = in.readString();
        codigoEmpleado = in.readInt();
        nombreCompleto = in.readString();
        codigoProyecto = in.readInt();
        nombreProyecto = in.readString();
        comentarioCierre = in.readString();
        comentarioRecepcion = in.readString();
        codigoCliente = in.readString();
        nombre = in.readString();
        importe = in.readDouble();
        importeGastos = in.readDouble();
        importeFacturable = in.readDouble();
        codigoUsuario = in.readInt();
        fechaUltimaModificacion = in.readString();
        plDescargado = in.readInt();
        fechaCierre = in.readString();
        horaCierre = in.readDouble();
        totalUnidades = in.readDouble();
        nombreUsuarioCierre = in.readString();
        codigoUsuarioCierre = in.readInt();
        sincroMovil = in.readInt();
    }

    public static final Creator<ParteCabecera> CREATOR = new Creator<ParteCabecera>() {
        @Override
        public ParteCabecera createFromParcel(Parcel in) {
            return new ParteCabecera(in);
        }

        @Override
        public ParteCabecera[] newArray(int size) {
            return new ParteCabecera[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(codigoEmpresa);
        dest.writeInt(ejercicioParte);
        dest.writeString(serieParte);
        dest.writeInt(numeroParte);
        dest.writeString(statusParte);
        dest.writeString(tipoParte);
        dest.writeString(descripcionTipoParte);
        dest.writeString(codigoArticulo);
        dest.writeString(descripcionArticulo);
        dest.writeString(fechaParte);
        dest.writeString(fechaEjecucion);
        dest.writeInt(codigoEmpleado);
        dest.writeString(nombreCompleto);
        dest.writeInt(codigoProyecto);
        dest.writeString(nombreProyecto);
        dest.writeString(comentarioCierre);
        dest.writeString(comentarioRecepcion);
        dest.writeString(codigoCliente);
        dest.writeString(nombre);
        dest.writeDouble(importe);
        dest.writeDouble(importeGastos);
        dest.writeDouble(importeFacturable);
        dest.writeInt(codigoUsuario);
        dest.writeString(fechaUltimaModificacion);
        dest.writeInt(plDescargado);
        dest.writeString(fechaCierre);
        dest.writeDouble(horaCierre);
        dest.writeDouble(totalUnidades);
        dest.writeString(nombreUsuarioCierre);
        dest.writeInt(codigoUsuarioCierre);
        dest.writeInt(sincroMovil);
    }


    // MÉTODOS PRIVADOS
    private void putCv(ContentValues cv) {
        cv.put(columnas[1], this.codigoEmpresa);
        cv.put(columnas[2], this.ejercicioParte);
        cv.put(columnas[3], this.serieParte);
        cv.put(columnas[4], this.numeroParte);
        cv.put(columnas[5], this.statusParte);
        cv.put(columnas[6], this.tipoParte);
        cv.put(columnas[7], this.descripcionTipoParte);
        cv.put(columnas[8], this.codigoArticulo);
        cv.put(columnas[9], this.descripcionArticulo);
        cv.put(columnas[10], this.fechaParte);
        cv.put(columnas[11], this.fechaEjecucion);
        cv.put(columnas[12], this.codigoEmpleado);
        cv.put(columnas[13], this.nombreCompleto);
        cv.put(columnas[14], this.codigoProyecto);
        cv.put(columnas[15], this.nombreProyecto);
        cv.put(columnas[16], this.comentarioCierre);
        cv.put(columnas[17], this.comentarioRecepcion);
        cv.put(columnas[18], this.codigoCliente);
        cv.put(columnas[19], this.nombre);
        cv.put(columnas[20], this.importe);
        cv.put(columnas[21], this.importeGastos);
        cv.put(columnas[22], this.importeFacturable);
        cv.put(columnas[23], this.codigoUsuario);
        cv.put(columnas[24], this.fechaUltimaModificacion);
        cv.put(columnas[25], this.plDescargado);
        cv.put(columnas[26], this.fechaCierre);
        cv.put(columnas[27], this.horaCierre);
        cv.put(columnas[28], this.totalUnidades);
        cv.put(columnas[29], this.nombreUsuarioCierre);
        cv.put(columnas[30], this.codigoUsuarioCierre);
        cv.put(columnas[31], this.sincroMovil);
    }
}
