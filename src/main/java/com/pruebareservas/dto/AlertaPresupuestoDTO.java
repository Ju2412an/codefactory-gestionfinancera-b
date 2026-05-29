package com.pruebareservas.dto;

public class AlertaPresupuestoDTO {

    private boolean alertaActiva;
    private double porcentajeGastado;
    private double totalGastado;
    private double presupuestoActual;
    private String mensaje;

    public boolean isAlertaActiva() {
        return alertaActiva;
    }

    public void setAlertaActiva(boolean alertaActiva) {
        this.alertaActiva = alertaActiva;
    }

    public double getPorcentajeGastado() {
        return porcentajeGastado;
    }

    public void setPorcentajeGastado(double porcentajeGastado) {
        this.porcentajeGastado = porcentajeGastado;
    }

    public double getTotalGastado() {
        return totalGastado;
    }

    public void setTotalGastado(double totalGastado) {
        this.totalGastado = totalGastado;
    }

    public double getPresupuestoActual() {
        return presupuestoActual;
    }

    public void setPresupuestoActual(double presupuestoActual) {
        this.presupuestoActual = presupuestoActual;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}