package es.dosxmil.partesexit.mapeo;

public class Empresa {

    private int codigoEmpresa;
    private String empresa;
    private String nombreEmpleado;
    public Empresa(int codigoEmpresa, String empresa, String nombreEmpleado) {
        this.setCodigoEmpresa(codigoEmpresa);
        this.setEmpresa(empresa);
        this.setNombreEmpleado(nombreEmpleado);
    }

    public String getNombreEmpleado() {
        return nombreEmpleado;
    }

    public void setNombreEmpleado(String nombreEmpleado) {
        this.nombreEmpleado = nombreEmpleado;
    }

    public int getCodigoEmpresa() {
        return codigoEmpresa;
    }

    public void setCodigoEmpresa(int codigoEmpresa) {
        this.codigoEmpresa = codigoEmpresa;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    @Override
    public String toString() {
        return empresa;
    }
}
