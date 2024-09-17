/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Fabian
 */
public class BloqueProceso {
    
    private static int id = 0;
    private String estado;
    private String ir;
    private Map<String, Integer> registros = new HashMap<>();
    private int inicioMemoria;
    private int finalMemoria;
    
    public BloqueProceso(String estado, String ir, int inicioMemoria, int finalMemoria){
        this.estado = estado;
        this.inicioMemoria = inicioMemoria;
        this.finalMemoria = finalMemoria;
        this.ir = ir;
    }

    /**
     * @return the id
     */
    public static int getId() {
        return id;
    }

    /**
     * @param aId the id to set
     */
    public static void setId(int aId) {
        id = aId;
    }

    /**
     * @return the estado
     */
    public String getEstado() {
        return estado;
    }

    /**
     * @param estado the estado to set
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * @return the pc
     */
    public String getIr() {
        return ir;
    }

    /**
     * @param pc the pc to set
     */
    public void setPc(String ir) {
        this.ir = ir;
    }

    /**
     * @return the registros
     */
    public Map<String, Integer> getRegistros() {
        return registros;
    }

    /**
     * @param registros the registros to set
     */
    public void setRegistros(Map<String, Integer> registros) {
        this.registros = registros;
    }

    /**
     * @return the inicioMemoria
     */
    public int getInicioMemoria() {
        return inicioMemoria;
    }

    /**
     * @param inicioMemoria the inicioMemoria to set
     */
    public void setInicioMemoria(int inicioMemoria) {
        this.inicioMemoria = inicioMemoria;
    }

    /**
     * @return the finalMemoria
     */
    public int getFinalMemoria() {
        return finalMemoria;
    }

    /**
     * @param finalMemoria the finalMemoria to set
     */
    public void setFinalMemoria(int finalMemoria) {
        this.finalMemoria = finalMemoria;
    }
}
