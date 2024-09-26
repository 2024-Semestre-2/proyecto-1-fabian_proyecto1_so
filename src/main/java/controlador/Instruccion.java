/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

/**
 *
 * @author Fabian
 */
public class Instruccion {
    
    private String registroDestino;
    private String registroOrigen;
    private int valor;
    private int valor2;
    private int valor3;
    private String linea;
    private String operacion;
    private String llamada;
    private int cantidadParam;

    public Instruccion(String operacion, String registroDestino, String registroOrigen, String linea){
        this.registroDestino = registroDestino;
        this.registroOrigen = registroOrigen;
        this.linea = linea;
        this.operacion = operacion;
    }

    public Instruccion(String operacion, String registroDestino, int valor, String linea){
        this.registroDestino = registroDestino;
        this.valor = valor;
        this.linea = linea;
        this.operacion = operacion;
    }

    public Instruccion(String operacion, String valor, String linea){
        this.llamada = valor;
        this.linea = linea;
        this.operacion = operacion;
    }

    public Instruccion(String operacion, int valor, String linea){
        this.valor = valor;
        this.linea = linea;
        this.operacion = operacion;
    }

        /**
     * @return the registroDestino
     */
    public int getCantidadParam() {
        return cantidadParam;
    }

    /**
     * @param registroDestino the registroDestino to set
     */
    public void setCantidadParam(int cantidadParam) {
        this.cantidadParam = cantidadParam;
    }

    /**
     * @return the registroDestino
     */
    public String getOperacion() {
        return operacion;
    }

    /**
     * @param registroDestino the registroDestino to set
     */
    public void setOperacion(String operacion) {
        this.operacion = operacion;
    }

    /**
     * @return the registroDestino
     */
    public String getRegistroDestino() {
        return registroDestino;
    }

    /**
     * @param registroDestino the registroDestino to set
     */
    public void setRegistroDestino(String registroDestino) {
        this.registroDestino = registroDestino;
    }

    /**
     * @return the registroOrigen
     */
    public String getRegistroOrigen() {
        return registroOrigen;
    }

    /**
     * @param registroOrigen the registroOrigen to set
     */
    public void setRegistroOrigen(String registroOrigen) {
        this.registroOrigen = registroOrigen;
    }

    /**
     * @return the valor
     */
    public int getValor() {
        return valor;
    }

    /**
     * @param valor the valor to set
     */
    public void setValor(int valor) {
        this.valor = valor;
    }

    /**
     * @return the valor
     */
    public int getValor2() {
        return valor2;
    }

    /**
     * @param valor the valor to set
     */
    public void setValor2(int valor2) {
        this.valor2 = valor2;
    }/**
     * @return the valor
     */
    public int getValor3() {
        return valor3;
    }

    /**
     * @param valor the valor to set
     */
    public void setValor3(int valor3) {
        this.valor3 = valor3;
    }

    /**
     * @return the linea
     */
    public String getLinea() {
        return linea;
    }

    /**
     * @param linea the linea to set
     */
    public void setLinea(String linea) {
        this.linea = linea;
    }
    
    /**
     * @return the linea
     */
    public String getLlamada() {
        return llamada;
    }

    /**
     * @param linea the linea to set
     */
    public void setLlamada(String llamada) {
        this.llamada = llamada;
    }
}
