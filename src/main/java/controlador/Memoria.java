/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

/**
 *
 * @author Fabian
 */
public class Memoria {
    
    private int limite;
    private int secundaria;
    private Object[] contenido;

    public Memoria(Object[] contenido, int limite, int secundaria){
        this.contenido = contenido;
        this.limite = limite;
        this.secundaria = secundaria;
    }
    
    /**
     * @return the limite
     */
    public int getLimite() {
        return limite;
    }

    /**
     * @param limite the limite to set
     */
    public void setLimite(int limite) {
        this.limite = limite;
    }

    /**
     * @return the limite
     */
    public int getSecundaria() {
        return secundaria;
    }

    /**
     * @param limite the limite to set
     */
    public void setSecundaria(int secundaria) {
        this.secundaria = secundaria;
    }

    /**
     * @return the contenido
     */
    public Object[] getContenido() {
        return contenido;
    }

    /**
     * @param contenido the contenido to set
     */
    public void setContenido(Object[] contenido) {
        this.contenido = contenido;
    }
}
