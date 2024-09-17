/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;
import Vista.PantallaConfiguracion;
import Vista.PantallaMemoria;
import Vista.PantallaPrincipal;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.FlowLayout;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;
import java.io.*;
import java.io.IOException;
import java.awt.Desktop;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import java.util.Vector;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 *
 * @author Fabian
 */
public class Controlador{

    private PantallaPrincipal pp;
    private PantallaConfiguracion pconfig;
    private PantallaMemoria pm;
    private Archivo archivoActual;
    private Memoria memoria;
    private BloqueProceso bcp;
    private Registro[] registros;
    private String[] identificadores;
    private String[] operaciones;
    private String ir;
    private Pattern patron;
    private String regex;
    DefaultTableModel dtm = new DefaultTableModel(0, 0);
    DefaultTableModel dtm2 = new DefaultTableModel(0, 0);

    public void gestionarBotones(){
        pp.getBtnSeleccionar().addActionListener(btnSeleccionar_ActionPerformed);
        pp.getBtnCargar().addActionListener(btnCargar_ActionPerformed);
        pp.getBtnInstruccion().addActionListener(btnInstruccion_ActionPerformed);
        pp.getBtnMemoria().addActionListener(btnMemoria_ActionPerformed);
        pp.getBtnConfiguracion().addActionListener(btnConfiguracion_ActionPerformed);
        pm.getBtnCerrar().addActionListener(btnCerrar_ActionPerformed);
        pconfig.getBtnVolver().addActionListener(btnVolver_ActionPerformed);
        pconfig.getBtnGuardar().addActionListener(btnGuardar_ActionPerformed);
    }

    ActionListener btnInstruccion_ActionPerformed = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            int posicion = registros[buscarRegistro("PC")].getValor();
            if(posicion<bcp.getFinalMemoria()){

                Instruccion instruccionActual = (Instruccion) memoria.getContenido()[posicion];
                String operacion = instruccionActual.getOperacion();
                int valor;
                int valorActual;
                int posicionRegistro1;
                int posicionRegistro2;
                String registroOrigen;
                String registroDestino;
                String llamada;
                int param1;
                int param2;
                int param3;


                switch(operacion) {
                    case "MOV":
                        if(instruccionActual.getRegistroOrigen() == null){
                            valor = instruccionActual.getValor();
                            registroDestino = instruccionActual.getRegistroDestino();
                            posicionRegistro1 = buscarRegistro(registroDestino);
                            registros[posicionRegistro1].setValor(valor);
                        }else{
                            registroOrigen = instruccionActual.getRegistroOrigen();
                            posicionRegistro1 = buscarRegistro(registroOrigen);
                            valor = registros[posicionRegistro1].getValor();
                            registroDestino = instruccionActual.getRegistroDestino();
                            posicionRegistro2 = buscarRegistro(registroDestino);
                            registros[posicionRegistro2].setValor(valor);
                        }
                        break;
                    case "ADD":
                        registroOrigen = instruccionActual.getRegistroOrigen();
                        posicionRegistro1 = buscarRegistro(registroOrigen);
                        valor = registros[posicionRegistro1].getValor();
                        registroDestino = instruccionActual.getRegistroDestino();
                        posicionRegistro2 = buscarRegistro(registroDestino);
                        valorActual = registros[posicionRegistro2].getValor();
                        registros[posicionRegistro2].setValor(valorActual+valor);
                        break;
                    case "SUB":
                        registroOrigen = instruccionActual.getRegistroOrigen();
                        posicionRegistro1 = buscarRegistro(registroOrigen);
                        valor = registros[posicionRegistro1].getValor();
                        registroDestino = instruccionActual.getRegistroDestino();
                        posicionRegistro2 = buscarRegistro(registroDestino);
                        valorActual = registros[posicionRegistro2].getValor();
                        registros[posicionRegistro2].setValor(valorActual-valor);
                        break;
                    case "STORE":
                        registroOrigen = instruccionActual.getRegistroDestino();
                        posicionRegistro1 = buscarRegistro(registroOrigen);
                        valor = registros[posicionRegistro1].getValor();
                        registroDestino = instruccionActual.getRegistroOrigen();
                        posicionRegistro2 = buscarRegistro(registroDestino);
                        registros[posicionRegistro2].setValor(valor);
                        break;
                    case "LOAD":
                        registroOrigen = instruccionActual.getRegistroOrigen();
                        posicionRegistro1 = buscarRegistro(registroOrigen);
                        valor = registros[posicionRegistro1].getValor();
                        registroDestino = instruccionActual.getRegistroDestino();
                        posicionRegistro2 = buscarRegistro(registroDestino);
                        registros[posicionRegistro2].setValor(valor);
                        break;
                    case "INC":
                        valor = instruccionActual.getValor();
                        registroDestino = instruccionActual.getRegistroDestino();
                        posicionRegistro2 = buscarRegistro(registroDestino);
                        valorActual = registros[posicionRegistro2].getValor();
                        registros[posicionRegistro2].setValor(valorActual+valor);
                        break;
                    case "DEC":
                        valor = instruccionActual.getValor();
                        registroDestino = instruccionActual.getRegistroDestino();
                        posicionRegistro2 = buscarRegistro(registroDestino);
                        valorActual = registros[posicionRegistro2].getValor();
                        registros[posicionRegistro2].setValor(valorActual-valor);
                        break;
                    case "SWAP":
                        registroOrigen = instruccionActual.getRegistroOrigen();
                        posicionRegistro1 = buscarRegistro(registroOrigen);
                        valor = registros[posicionRegistro1].getValor();
                        registroDestino = instruccionActual.getRegistroDestino();
                        posicionRegistro2 = buscarRegistro(registroDestino);
                        registros[posicionRegistro2].setValor(valor);
                        registros[posicionRegistro2].setValor(valorActual);
                        break;
                    case "INT":
                        nuevaInstruccion = new Instruccion(division[0], division[1], actual);
                        instrucciones.add(nuevaInstruccion);
                        break;
                    case "JMP":
                        valor = Integer.parseInt(division[1]);
                        nuevaInstruccion = new Instruccion(division[0], valor, actual);
                        instrucciones.add(nuevaInstruccion);
                        break;
                    case "CMP":
                        nuevaInstruccion = new Instruccion(division[0], division[1], division[2], actual);
                        instrucciones.add(nuevaInstruccion);
                        break;
                    case "JE":
                        valor = Integer.parseInt(division[1]);
                        nuevaInstruccion = new Instruccion(division[0], valor, actual);
                        instrucciones.add(nuevaInstruccion);
                        break;
                    case "JNE":
                        valor = Integer.parseInt(division[1]);
                        nuevaInstruccion = new Instruccion(division[0], valor, actual);
                        instrucciones.add(nuevaInstruccion);
                        break;
                    case "PARAM":
                        param1 = Integer.parseInt(division[1]);
                        nuevaInstruccion = new Instruccion(division[0], valor, actual);
                        if(division.length == 4){
                            param2 = Integer.parseInt(division[2]);
                            param3 = Integer.parseInt(division[3]);
                            nuevaInstruccion = new Instruccion(division[0], param1, actual);
                            nuevaInstruccion.setValor2(param2);
                            nuevaInstruccion.setValor3(param3);
                        }else if(division.length == 3){
                            param2 = Integer.parseInt(division[2]);
                            nuevaInstruccion = new Instruccion(division[0], param1, actual);
                            nuevaInstruccion.setValor2(param2);
                        }else{
                            nuevaInstruccion = new Instruccion(division[0], param1, actual);
                        }
                        break;
                    case "PUSH":
                        nuevaInstruccion = new Instruccion(division[0], division[1], actual);
                        instrucciones.add(nuevaInstruccion);
                        break;
                    case "POP":
                        nuevaInstruccion = new Instruccion(division[0], division[1], actual);
                        instrucciones.add(nuevaInstruccion);
                        break;
                    default:
                        System.out.println("A");
                        JOptionPane.showMessageDialog (null, "Error semantico", "Error", JOptionPane.ERROR_MESSAGE);
                }

                if(bcp.getEstado().equals("Preparado")){
                    bcp.setEstado("Ejecutando");
                }
                if(instruccionActual.getOperacion().equals("MOV")){
                    registros[buscarRegistro(instruccionActual.getRegistroDestino())].setValor(instruccionActual.getValor());
                }else if(instruccionActual.getOperacion().equals("ADD")){
                    int posicion2 = buscarRegistro("AC");
                    int posicion3 = buscarRegistro(instruccionActual.getRegistroOrigen());
                    int valorActual = registros[posicion2].getValor();
                    valorActual+=registros[posicion3].getValor();
                    registros[buscarRegistro(instruccionActual.getRegistroDestino())].setValor(valorActual);
                }else if(instruccionActual.getOperacion().equals("SUB")){
                    int posicion2 = buscarRegistro("AC");
                    int posicion3 = buscarRegistro(instruccionActual.getRegistroOrigen());
                    int valorActual = registros[posicion2].getValor();
                    valorActual-=registros[posicion3].getValor();
                    registros[buscarRegistro(instruccionActual.getRegistroDestino())].setValor(valorActual);
                }else if(instruccionActual.getOperacion().equals("LOAD")){
                    int posicion3 = buscarRegistro(instruccionActual.getRegistroOrigen());
                    int valorActual = registros[posicion3].getValor();
                    registros[buscarRegistro(instruccionActual.getRegistroDestino())].setValor(valorActual);
                }else{
                    int posicion2 = buscarRegistro("AC");
                    int valorActual = registros[posicion2].getValor();
                    registros[buscarRegistro(instruccionActual.getRegistroOrigen())].setValor(valorActual);
                }
                posicion++;
                registros[buscarRegistro("PC")].setValor(posicion);
                if(posicion==bcp.getFinalMemoria()){
                    bcp.setEstado("Terminado");
                }else{
                    Instruccion siguienteInstruccion = (Instruccion) memoria.getContenido()[posicion];
                    ir = siguienteInstruccion.getLinea();
                }
                actualizarBcp();
                actualizarMemoriaSO();
                actualizarTabla();
            }else{
                inicializarRegistros();
                actualizarMemoriaSO();
                actualizarTabla();
            }
        }
    };

    ActionListener btnSeleccionar_ActionPerformed = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String contenido = "";
            String formato = "";
            JFileChooser fileChooser = new JFileChooser();
			fileChooser.setCurrentDirectory(new File(".")); //sets current directory
			int response = fileChooser.showOpenDialog(null); //select file to open
			if(response == JFileChooser.APPROVE_OPTION) {
				File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                int i = file.getName().lastIndexOf('.');
                if (i > 0) {
                    formato = file.getName().substring(i + 1);
                }
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String linea;
                    while ((linea = reader.readLine()) != null) {
                        contenido += linea + "\n";
                    }
                } catch (IOException ee) {
                    ee.printStackTrace();
                }
				System.out.println(formato);
			}
            archivoActual = new Archivo(contenido, formato);
            pp.getjTextArea1().setText(contenido);
        }
    };

    ActionListener btnMemoria_ActionPerformed = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            pp.setVisible(false);
            pm.setVisible(true);
        }
    };
    ActionListener btnConfiguracion_ActionPerformed = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            pp.setVisible(false);
            pconfig.setVisible(true);
        }
    };

    ActionListener btnCerrar_ActionPerformed = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            pp.setVisible(true);
            pm.setVisible(false);
        }
    };

    ActionListener btnVolver_ActionPerformed = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            pp.setVisible(true);
            pconfig.setVisible(false);
        }
    };

    ActionListener btnGuardar_ActionPerformed = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            int valor = Integer.parseInt(pconfig.getjTextField1().getText());
            memoria.setLimite(valor);
            pp.setVisible(true);
            pm.setVisible(false);
        }
    };

    ActionListener btnCargar_ActionPerformed = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            ArrayList<Instruccion> instrucciones;
            String[] listaTemporal = construirLista();
            boolean validacion = validarInstruccion(listaTemporal);
            if(validacion){
                instrucciones = construirListaInstrucciones(listaTemporal);
                Random random = new Random();
                int min = memoria.getLimite();
                int max = 1024;
                int posicionAleatoria = random.nextInt(max - min + 1) + min;
                for(int i = 0; i<instrucciones.size(); i++){
                    memoria.getContenido()[posicionAleatoria]=instrucciones.get(i);
                    posicionAleatoria++;
                }
                posicionAleatoria-=instrucciones.size();
                bcp.setInicioMemoria(posicionAleatoria);
                bcp.setFinalMemoria(posicionAleatoria+instrucciones.size());
                ir = instrucciones.get(0).getLinea();
                registros[buscarRegistro("PC")].setValor(posicionAleatoria);
                bcp.getRegistros().replace("PC", posicionAleatoria);
                bcp.setEstado("Preparado");
                actualizarMemoriaSO();
                actualizarTabla();
            }else{
                System.out.println("B");
                JOptionPane.showMessageDialog (null, "Error semantico", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    };

    public Controlador(){
        this.pp = new PantallaPrincipal();
        this.pconfig = new PantallaConfiguracion();
        this.pm = new PantallaMemoria();
        this.memoria = new Memoria();
        this.ir = "-";
        this.regex = "^(LOAD\\s+(AX|BX|CX|DX)|" +
               "STORE\\s+(AX|BX|CX|DX)|" +
               "MOV\\s+(AX|BX|CX|DX)\\s+(AX|BX|CX|DX)|" +
               "MOV\\s+(AX|BX|CX|DX)\\s+-?\\d+|" +
               "ADD\\s+(AX|BX|CX|DX)|" +
               "SUB\\s+(AX|BX|CX|DX)|" +
               "INC(\\s+(AX|BX|CX|DX))?|" +
               "DEC(\\s+(AX|BX|CX|DX))?|" +
               "SWAP\\s+(AX|BX|CX|DX)\\s+(AX|BX|CX|DX)|" +
               "INT\\s+\\d{2}H|" +
               "JMP\\s+[+-]?\\d+|" +
               "CMP\\s+(AX|BX|CX|DX)\\s+(AX|BX|CX|DX)|" +
               "JE|JNE\\s+[+-]?\\d+|" +
               "PARAM\\s+-?\\d+(\\s+-?\\d+){0,2}|" +
               "PUSH\\s+(AX|BX|CX|DX)|" +
               "POP\\s+(AX|BX|CX|DX))\\s*$";
    
        // Compilar el patrón una sola vez para mejor rendimiento
        this.patron = Pattern.compile(regex);
        this.identificadores = new String[]{"AX", "BX", "CX", "DX", "AC", "PC"};
        this.operaciones = new String[]{"MOV", "LOAD", "STORE", "ADD", "SUB"};
        this.registros = new Registro[6];
        this.bcp = new BloqueProceso("nuevo","-", 0, 0);
        inicializarRegistros();
        gestionarBotones();
        String columnas[] = new String[] {"ID", "Estado","AX", "BX", "CX", "DX", "AC", "PC", "IR", "Inicio", "Final"};
        dtm.setColumnIdentifiers(columnas);
        pp.getjTable1().setModel(dtm);
        pp.getJScrollPane2().setViewportView(pp.getjTable1());
        actualizarTabla();
        String columnas2[] = new String[] {"Memoria"};
        dtm2.setColumnIdentifiers(columnas2);
        pm.getjTable1().setModel(dtm2);
        pm.getjScrollPane1().setViewportView(pm.getjTable1());
        this.pp.setVisible(true);
    }

    public ArrayList<Instruccion> construirListaInstrucciones(String[] listaTemporal){
        ArrayList<Instruccion> instrucciones = new ArrayList<>();
        for(int i = 0; i < listaTemporal.length; i++){
            System.out.println("Entro al for");
            String actual = listaTemporal[i];
            String[] division = actual.split("\\s+");
            String operacion = division[0];
            Instruccion nuevaInstruccion;
            int valor = 0;
            int param3;
            int param1;
            int param2;

            switch(operacion) {
                case "MOV":
                    boolean clasificacion = esNumerico(division[2]);
                    if(clasificacion){
                        valor = Integer.parseInt(division[2]);
                        Instruccion nuevInstruccion = new Instruccion(division[0], division[1], valor, actual);
                        instrucciones.add(nuevInstruccion);
                    }else{
                        Instruccion nuevInstruccion = new Instruccion(division[0], division[1], division[2], actual);
                        instrucciones.add(nuevInstruccion);
                    }
                    break;
                case "ADD":
                    nuevaInstruccion = new Instruccion(division[0], "AC", division[1], actual);
                    instrucciones.add(nuevaInstruccion);
                    break;
                case "SUB":
                    nuevaInstruccion = new Instruccion(division[0], "AC", division[1], actual);
                    instrucciones.add(nuevaInstruccion);
                    break;
                case "STORE":
                    nuevaInstruccion = new Instruccion(division[0], "AC", division[1], actual);
                    instrucciones.add(nuevaInstruccion);
                    break;
                case "LOAD":
                    nuevaInstruccion = new Instruccion(division[0], "AC", division[1], actual);
                    instrucciones.add(nuevaInstruccion);
                    break;
                case "INC":
                    if(division.length == 1){
                        nuevaInstruccion = new Instruccion(division[0], "AC", 1, actual);
                        instrucciones.add(nuevaInstruccion);
                    }else{
                        nuevaInstruccion = new Instruccion(division[0], division[1], 1, actual);
                        instrucciones.add(nuevaInstruccion);
                    }
                    break;
                case "DEC":
                    if(division.length == 1){
                        nuevaInstruccion = new Instruccion(division[0], "AC", 1, actual);
                        instrucciones.add(nuevaInstruccion);
                    }else{
                        nuevaInstruccion = new Instruccion(division[0], division[1], 1, actual);
                        instrucciones.add(nuevaInstruccion);
                    }
                    break;
                case "SWAP":
                    nuevaInstruccion = new Instruccion(division[0], division[1], division[2], actual);
                    instrucciones.add(nuevaInstruccion);
                    break;
                case "INT":
                    nuevaInstruccion = new Instruccion(division[0], division[1], actual);
                    instrucciones.add(nuevaInstruccion);
                    break;
                case "JMP":
                    valor = Integer.parseInt(division[1]);
                    nuevaInstruccion = new Instruccion(division[0], valor, actual);
                    instrucciones.add(nuevaInstruccion);
                    break;
                case "CMP":
                    nuevaInstruccion = new Instruccion(division[0], division[1], division[2], actual);
                    instrucciones.add(nuevaInstruccion);
                    break;
                case "JE":
                    valor = Integer.parseInt(division[1]);
                    nuevaInstruccion = new Instruccion(division[0], valor, actual);
                    instrucciones.add(nuevaInstruccion);
                    break;
                case "JNE":
                    valor = Integer.parseInt(division[1]);
                    nuevaInstruccion = new Instruccion(division[0], valor, actual);
                    instrucciones.add(nuevaInstruccion);
                    break;
                case "PARAM":
                    param1 = Integer.parseInt(division[1]);
                    nuevaInstruccion = new Instruccion(division[0], valor, actual);
                    if(division.length == 4){
                        param2 = Integer.parseInt(division[2]);
                        param3 = Integer.parseInt(division[3]);
                        nuevaInstruccion = new Instruccion(division[0], param1, actual);
                        nuevaInstruccion.setValor2(param2);
                        nuevaInstruccion.setValor3(param3);
                    }else if(division.length == 3){
                        param2 = Integer.parseInt(division[2]);
                        nuevaInstruccion = new Instruccion(division[0], param1, actual);
                        nuevaInstruccion.setValor2(param2);
                    }else{
                        nuevaInstruccion = new Instruccion(division[0], param1, actual);
                    }
                    break;
                case "PUSH":
                    nuevaInstruccion = new Instruccion(division[0], division[1], actual);
                    instrucciones.add(nuevaInstruccion);
                    break;
                case "POP":
                    nuevaInstruccion = new Instruccion(division[0], division[1], actual);
                    instrucciones.add(nuevaInstruccion);
                    break;
                default:
                    System.out.println("A");
                    JOptionPane.showMessageDialog (null, "Error semantico", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        return instrucciones;
    }

    public String[] construirLista(){
        String contenido = this.archivoActual.getContenido();
        String contenido2 = contenido.replaceAll(",", " ");
        String correccion = contenido2.trim();
        String correccion2 = correccion.toUpperCase();
        String[] resultado = correccion2.split("\\n+");
        return resultado;
    }

    // Método que recibe una lista de instrucciones y valida si todas son correctas
    public boolean validarInstruccion(String[] instrucciones) {
        for (String instruccion : instrucciones) {
            System.out.println(instruccion + "\n");
            Matcher matcher = this.patron.matcher(instruccion);
            if (!matcher.matches()) {
                return false; // Si alguna no coincide, retorna false
            }
        }
        System.out.println("salio");
        return true; // Si todas coinciden, retorna true
    }

    public void actualizarTablaMemoria(){
        dtm2 = new DefaultTableModel(0, 0);
        String columnas2[] = new String[] {"Memoria"};
        dtm2.setColumnIdentifiers(columnas2);
        pm.getjTable1().setModel(dtm2);
        pm.getjScrollPane1().setViewportView(pm.getjTable1());
        for(int i = 0; i<memoria.getContenido().length; i++){
            Vector<Object> vector = new Vector<>();
            vector.add(memoria.getContenido()[i]);
            dtm2.addRow(vector);
        }
    }

    public void inicializarRegistros(){
        int cont = 0;
        for(String i : identificadores){
            if(i.equals("PC")){
                Registro registroActual = new Registro(i, "contador", 0);
                registros[cont]=registroActual;
                cont++;
                bcp.getRegistros().put(i, 0);
            }else if(i.equals("AC")){
                Registro registroActual = new Registro(i, "acumulador", 0);
                registros[cont]=registroActual;
                cont++;
                bcp.getRegistros().put(i, 0);
            }else{
                Registro registroActual = new Registro(i, "general", 0);
                registros[cont]=registroActual;
                cont++;
                bcp.getRegistros().put(i, 0);
            }
        }
    }

    public void actualizarMemoriaSO(){
        Arrays.fill(memoria.getContenido(), 0, memoria.getLimite(), null);
        Random random = new Random();
        int max = memoria.getLimite();
        int min = 0;
        int posicionAleatoria = random.nextInt(max - min + 1) + min;
        memoria.getContenido()[posicionAleatoria] = bcp.getId();
        posicionAleatoria++;
        memoria.getContenido()[posicionAleatoria] = bcp.getEstado();
        posicionAleatoria++;
        memoria.getContenido()[posicionAleatoria] = bcp.getInicioMemoria();
        posicionAleatoria++;
        memoria.getContenido()[posicionAleatoria] = bcp.getFinalMemoria();
        posicionAleatoria++;
        for (Map.Entry<String, Integer> entry : bcp.getRegistros().entrySet()) {
            memoria.getContenido()[posicionAleatoria] = entry.getKey() + " = " + entry.getValue().toString();
            posicionAleatoria++;
        }
        memoria.getContenido()[posicionAleatoria] = ir;
        actualizarTablaMemoria();
    }

    public void actualizarBcp(){
        for(int i = 0; i<registros.length; i++){
            String id = registros[i].getIdentificador();
            bcp.getRegistros().replace(id, registros[i].getValor());
        }
    }

    public int validarOperacion(String operacion) {
        for(int i = 0; i<operaciones.length; i++){
            if (operaciones[i].equals(operacion)) {
                return i;
            }
        }
        return -1;
    }

    public int buscarRegistro(String identificador){
        for(int i = 0; i<registros.length; i++){
            if(identificador.equals(registros[i].getIdentificador())){
                return i;
            }
        }
        return 0;
    }

    public void actualizarTabla(){
        dtm.addRow(new Object[] { bcp.getId(), bcp.getEstado(), bcp.getRegistros().get("AX"), bcp.getRegistros().get("BX"), bcp.getRegistros().get("CX"), bcp.getRegistros().get("DX"), bcp.getRegistros().get("AC"), bcp.getRegistros().get("PC"), this.ir, bcp.getInicioMemoria(),bcp.getFinalMemoria()});
    }

    public boolean validarRegistro(String registro) {
        for(int i = 0; i<identificadores.length; i++){
            if (identificadores[i].equals(registro)) {
                return true;
            }
        }
        return false;
    }

    public boolean esNumerico(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        char[] cadena = str.toCharArray();
        if(cadena[0]=='-'){
            cadena = Arrays.copyOfRange(cadena, 1, cadena.length);
        }
        for (char c : cadena) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }
}
