/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;
import Vista.Estadisticas;
import Vista.PantallaConfiguracion;
import Vista.PantallaMemoria;
import Vista.PantallaPrincipal;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.*;
import java.io.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Stack;
import java.util.Vector;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.concurrent.TimeUnit;
import java.util.function.IntUnaryOperator;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

/**
 *
 * @author Fabian
 */
public class Controlador{

    private PantallaPrincipal pp;
    private PantallaConfiguracion pconfig;
    private PantallaMemoria pm;
    private Estadisticas pe;
    private Archivo archivoActual;
    private Memoria memoriaPrincipal;
    private ArrayList<BloqueProceso> listaBcp;
    private ArrayList<Archivo> listaArchivos;
    private BloqueProceso bcpActual;
    private Registro[] registros;
    private String[] identificadores;
    private String ir;
    private Pattern patron;
    private String regex;
    private Stack<Integer> pila;
    private boolean cmpResult;
    private int tiempoEjecucion;
    private boolean espera;
    private String horaInicio = "";
    private String horaFinal = "";
    

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
        pp.getTextFieldEntrada().addActionListener(textFieldEntrada_AntionPerformed);
        pp.getBtnAutomatico().addActionListener(btnAutomatico_ActionPerformed);
        pp.getBtnSgteArchivo().addActionListener(btnSgtArchivo_ActionPerformed);
        pp.getBtnEstadisticas().addActionListener(btnEstadisticas_ActionPerformed);
        pe.getBtnVolver().addActionListener(btnEstadisticasCerrar_ActionPerformed);
    }

    ActionListener btnInstruccion_ActionPerformed = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(bcpActual!=null){
                if(pp.getBtnInstruccion().getText().equals("Siguiente Proceso")){
                    cambioContexto();
                    pp.getBtnInstruccion().setText("Siguiente Instruccion");
                }else{
                    if(pp.getBtnInstruccion().getText().equals("Iniciar Proceso")){
                        pp.getBtnInstruccion().setText("Siguiente Instruccion");
                    }
                    if(pp.getBtnInstruccion().getText().equals("Siguiente Instruccion")){
                        ejecutarInstruccion();
                    }
                }
            }
        }
    };

    ActionListener btnAutomatico_ActionPerformed = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            new Thread(() -> {
                if(bcpActual!=null){
                    while(!bcpActual.getEstado().equals("Terminado")){
                        ejecutarInstruccion();
                        try {
                            Thread.sleep(1000);  // Pausa de 1 segundo
                        } catch (InterruptedException o) {
                            // Manejo de excepción en caso de que el hilo sea interrumpido
                            o.printStackTrace();
                        }
                        System.out.println(bcpActual.getEstado());
                    }
                }
            }).start();
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
            Archivo archivoNuevo = new Archivo(contenido, formato);
            if(listaArchivos.size()==0){
                archivoActual = archivoNuevo;
                pp.getjTextArea1().setText(contenido);
            }
            listaArchivos.add(archivoNuevo);
        }
    };

    ActionListener btnSgtArchivo_ActionPerformed = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(listaArchivos.size()>1){
                int cont = 0;
                for(int i = 0; i<listaArchivos.size(); i++){
                    if(listaArchivos.get(i)==archivoActual){
                        cont = i;
                    }
                }
                if(cont==listaArchivos.size()-1){
                    archivoActual =  listaArchivos.get(0);
                }else{
                    archivoActual = listaArchivos.get(cont+1);
                }
                pp.getjTextArea1().setText(archivoActual.getContenido());
            }
        }
    };

    ActionListener btnCambiarArchivo_ActionPerformed = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            pp.setVisible(false);
            pm.setVisible(true);
        }
    };

    ActionListener btnEstadisticas_ActionPerformed = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            pp.setVisible(false);
            String duracion = Integer.toString(tiempoEjecucion);
            pe.getLblSegundos().setText(duracion);
            pe.getLblInicio().setText(horaInicio);
            pe.getLblFinal().setText(horaFinal);
            pe.setVisible(true);
        }
    };

    ActionListener btnEstadisticasCerrar_ActionPerformed = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            pe.setVisible(false);
            pp.setVisible(true);
        }
    };

    ActionListener btnMemoria_ActionPerformed = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            pp.setVisible(false);
            pm.setVisible(true);
        }
    };
    
    ActionListener textFieldEntrada_AntionPerformed = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            espera = true;
            ejecutarInstruccion();
        }
    };

    ActionListener btnConfiguracion_ActionPerformed = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String tamanho = Integer.toString(memoriaPrincipal.getContenido().length);
            String limite = Integer.toString(memoriaPrincipal.getLimite());
            String secundaria = Integer.toString(memoriaPrincipal.getSecundaria());
            pp.setVisible(false);
            pconfig.getTamanho().setText(tamanho);
            pconfig.getLimite().setText(limite);;
            pconfig.getSecundaria().setText(secundaria);;
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
            cambiarAjustes();
        }
    };

    ActionListener btnCargar_ActionPerformed = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(listaBcp.size() < 5){
                if(pp.getBtnConfiguracion().isEnabled()){
                    pp.getBtnConfiguracion().setEnabled(false);
                }
                ArrayList<Instruccion> instrucciones;
                String[] listaTemporal = construirLista();
                boolean validacion = validarInstruccion(listaTemporal);
                if(validacion){
                    instrucciones = construirListaInstrucciones(listaTemporal);
                    Random random = new Random();
                    int min = memoriaPrincipal.getLimite();
                    int max = memoriaPrincipal.getContenido().length - memoriaPrincipal.getSecundaria();
                    int posicionAleatoria = random.nextInt(max - min + 1) + min;
                    while(!disponibilidad(posicionAleatoria, max, instrucciones.size())){
                        posicionAleatoria = random.nextInt(max - min + 1) + min;
                    }
                    for(int i = 0; i<instrucciones.size(); i++){
                        memoriaPrincipal.getContenido()[posicionAleatoria]=instrucciones.get(i);
                        posicionAleatoria++;
                    }
                    ir = instrucciones.get(0).getLinea();
                    BloqueProceso bcpNuevo = new BloqueProceso("nuevo",ir, 0, 0);
                    bcpNuevo.setRegistros(inicializarBcpHash());
                    posicionAleatoria-=instrucciones.size();
                    bcpNuevo.setInicioMemoria(posicionAleatoria);
                    bcpNuevo.setFinalMemoria(posicionAleatoria+instrucciones.size());
                    bcpNuevo.getRegistros().replace("PC", posicionAleatoria);
                    bcpNuevo.setEstado("Preparado");
                    if(bcpActual == null){
                        registros[buscarRegistro("PC")].setValor(posicionAleatoria);
                        bcpActual = bcpNuevo;
                        actualizarTabla();
                    }
                    actualizarMemoriaSO(bcpNuevo);
                    listaBcp.add(bcpNuevo);
                }else{
                    JOptionPane.showMessageDialog (null, "Error en la carga del proceso nuevo. Asegurese de que el proceso no contenga errores y de no cargar un proceso vacío", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }else{
                ArrayList<Instruccion> instrucciones;
                String[] listaTemporal = construirLista();
                boolean validacion = validarInstruccion(listaTemporal);
                if(validacion){
                    instrucciones = construirListaInstrucciones(listaTemporal);
                    Random random = new Random();
                    int min = memoriaPrincipal.getContenido().length - memoriaPrincipal.getSecundaria();
                    int max = memoriaPrincipal.getContenido().length;
                    int posicionAleatoria = random.nextInt(max - min + 1) + min;
                    while(!disponibilidad(posicionAleatoria, max, instrucciones.size())){
                        posicionAleatoria = random.nextInt(max - min + 1) + min;
                    }
                    for(int i = 0; i<instrucciones.size(); i++){
                        memoriaPrincipal.getContenido()[posicionAleatoria]=instrucciones.get(i);
                        posicionAleatoria++;
                    }
                }
                pp.getTextAreaPantalla().setText("--> La memoria principal ya ha alcanzado su capacidad maxima, el proceso será guardado en la memoria secundaria");
                actualizarTablaMemoria();
            }
        }
    };

    public Controlador(){
        this.pp = new PantallaPrincipal();
        this.pconfig = new PantallaConfiguracion();
        this.pm = new PantallaMemoria();
        this.pe = new Estadisticas();
        ajustes();
        this.ir = "-";
        this.espera = false;
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
               "(JE|JNE)\\s+[+-]?\\d+|" +
               "PARAM\\s+-?\\d+(\\s+-?\\d+){0,2}|" +
               "PUSH\\s+(AX|BX|CX|DX)|" +
               "POP\\s+(AX|BX|CX|DX))\\s*$";
    
        // Compilar el patrón una sola vez para mejor rendimiento
        this.patron = Pattern.compile(regex);
        this.pila = new Stack<Integer>();
        this.listaBcp = new ArrayList<>();
        this.listaArchivos = new ArrayList<>();
        this.identificadores = new String[]{"AX", "BX", "CX", "DX", "AC", "PC"};
        this.registros = new Registro[6];
        inicializarRegistros();
        String columnas[] = new String[] {"ID", "Estado", "Pila", "AX", "BX", "CX", "DX", "AC", "PC", "IR", "Inicio", "Final"};
        dtm.setColumnIdentifiers(columnas);
        pp.getjTable1().setModel(dtm);
        pp.getJScrollPane2().setViewportView(pp.getjTable1());
        String columnas2[] = new String[] {"Memoria"};
        dtm2.setColumnIdentifiers(columnas2);
        pm.getjTable1().setModel(dtm2);
        pm.getjScrollPane1().setViewportView(pm.getjTable1());
        this.pp.setVisible(true);
        gestionarBotones();
    }

    public ArrayList<Instruccion> construirListaInstrucciones(String[] listaTemporal){
        ArrayList<Instruccion> instrucciones = new ArrayList<>();
        for(int i = 0; i < listaTemporal.length; i++){
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
                        nuevaInstruccion.setCantidadParam(3);
                        instrucciones.add(nuevaInstruccion);
                    }else if(division.length == 3){
                        param2 = Integer.parseInt(division[2]);
                        nuevaInstruccion = new Instruccion(division[0], param1, actual);
                        nuevaInstruccion.setValor2(param2);
                        nuevaInstruccion.setCantidadParam(2);
                        instrucciones.add(nuevaInstruccion);
                    }else{
                        nuevaInstruccion = new Instruccion(division[0], param1, actual);
                        nuevaInstruccion.setCantidadParam(1);
                        instrucciones.add(nuevaInstruccion);
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
        String[] resultado = {};
        if(archivoActual!=null){
            String contenido = this.archivoActual.getContenido();
            String contenido2 = contenido.replaceAll(",", " ");
            String correccion = contenido2.trim();
            String correccion2 = correccion.toUpperCase();
            resultado = correccion2.split("\\n+");
            return resultado; 
        }
        return resultado;
    }
    
    public boolean disponibilidad(int posicion, int limite, int cantidad){
        int cont = 1;
        boolean resultado = false;
        for(int i = posicion; i<limite; i++){
            if(memoriaPrincipal.getContenido()[i] != null){
                break;
            }else if(cont==cantidad){
                resultado = true;
                break;
            }
            cont++;
        }
        return resultado;
    }

    // Método que recibe una lista de instrucciones y valida si todas son correctas
    public boolean validarInstruccion(String[] instrucciones) {
        if(instrucciones.length != 0){
            for (String instruccion : instrucciones) {
                System.out.println(instruccion + "\n");
                Matcher matcher = this.patron.matcher(instruccion);
                if (!matcher.matches()) {
                    return false;
                }
            }
            return true;
        }else{
            return false;
        }  
    }

    public void actualizarTablaMemoria(){
        dtm2 = new DefaultTableModel(0, 0);
        String columnas2[] = new String[] {"Posición", "Memoria"};
        dtm2.setColumnIdentifiers(columnas2);
        pm.getjTable1().setModel(dtm2);
        pm.getjScrollPane1().setViewportView(pm.getjTable1());
        for(int i = 0; i<memoriaPrincipal.getContenido().length; i++){
            Vector<Object> vector = new Vector<>();
            vector.add(i);
            if(memoriaPrincipal.getContenido()[i]!=null){
                Class<?> objClass = memoriaPrincipal.getContenido()[i].getClass();
                String clase = objClass.getSimpleName();
                if(clase.equals("Instruccion")){
                    Instruccion instruccion = (Instruccion)memoriaPrincipal.getContenido()[i];
                    vector.add(instruccion.getLinea());
                }else{
                    vector.add(memoriaPrincipal.getContenido()[i]);
                }
            }else{
                vector.add(memoriaPrincipal.getContenido()[i]);
            }
            dtm2.addRow(vector);
        }
    }

    public void error(int tipo){
        switch (tipo) {
            case 1:
                pp.getTextAreaPantalla().setText("--> ERROR: Imposible acceder a esa posición de la memoria\n");
                bcpActual.setEstado("Terminado");
                pp.getBtnInstruccion().setText("Siguiente Proceso");
                break;
            case 2:
                pp.getTextAreaPantalla().setText("--> ERROR: Ha excedido la capacidad de la pila\n");
                bcpActual.setEstado("Terminado");
                pp.getBtnInstruccion().setText("Siguiente Proceso");
                break;
            case 3:
                pp.getTextAreaPantalla().setText("--> ERROR: La pila se encuentra vacía\n");
                bcpActual.setEstado("Terminado");
                pp.getBtnInstruccion().setText("Siguiente Proceso");
                break;
            case 4:
                pp.getTextAreaPantalla().setText("--> ERROR: Este proceso no cuenta con un final\n");
                bcpActual.setEstado("Terminado");
                pp.getBtnInstruccion().setText("Siguiente Proceso");
                break;
            default:
                break;
        }
        LocalTime horaActual = LocalTime.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("HH:mm:ss");
        horaFinal = horaActual.format(formato);
    }

    public void inicializarRegistros(){
        int cont = 0;
        for(String i : identificadores){
            if(i.equals("PC")){
                Registro registroActual = new Registro(i, "contador", 0);
                registros[cont]=registroActual;
                cont++;
            }else if(i.equals("AC")){
                Registro registroActual = new Registro(i, "acumulador", 0);
                registros[cont]=registroActual;
                cont++;
            }else{
                Registro registroActual = new Registro(i, "general", 0);
                registros[cont]=registroActual;
                cont++;
            }
        }
    }

    public Map<String, Integer> inicializarBcpHash(){
        Map<String, Integer> registros = new HashMap<>();
        for(String i : identificadores){
           registros.put(i, 0);
        }
        return registros;
    }

    public void actualizarMemoriaSO(BloqueProceso procesoNuevo){
        Random random = new Random();
        int max = memoriaPrincipal.getLimite();
        int min = 0;
        int posicionAleatoria = random.nextInt(max - min + 1) + min;
        while (!disponibilidad(posicionAleatoria, max, 11)) {
            posicionAleatoria = random.nextInt(max - min + 1) + min;
        }
        memoriaPrincipal.getContenido()[posicionAleatoria] = procesoNuevo.getId();
        posicionAleatoria++;
        memoriaPrincipal.getContenido()[posicionAleatoria] = procesoNuevo.getEstado();
        posicionAleatoria++;
        memoriaPrincipal.getContenido()[posicionAleatoria] = procesoNuevo.getInicioMemoria();
        posicionAleatoria++;
        memoriaPrincipal.getContenido()[posicionAleatoria] = procesoNuevo.getFinalMemoria();
        posicionAleatoria++;
        for (Map.Entry<String, Integer> entry : procesoNuevo.getRegistros().entrySet()) {
            memoriaPrincipal.getContenido()[posicionAleatoria] = entry.getKey() + " = " + entry.getValue().toString();
            posicionAleatoria++;
        }
        memoriaPrincipal.getContenido()[posicionAleatoria] = procesoNuevo.getIr();
        actualizarTablaMemoria();
    }

    public void actualizarBcp(){
        for(int i = 0; i<registros.length; i++){
            String id = registros[i].getIdentificador();
            bcpActual.getRegistros().replace(id, registros[i].getValor());
        }
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
        dtm.addRow(new Object[] { bcpActual.getId(), bcpActual.getEstado(), pila.toString(), bcpActual.getRegistros().get("AX"), bcpActual.getRegistros().get("BX"), bcpActual.getRegistros().get("CX"), bcpActual.getRegistros().get("DX"), bcpActual.getRegistros().get("AC"), bcpActual.getRegistros().get("PC"), this.ir, bcpActual.getInicioMemoria(),bcpActual.getFinalMemoria()});
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
    
    public boolean isArrayEmpty(Object[] array) {
        for (Object element : array) {
            if (element != null) {
                return false;  // Si encuentras un elemento no nulo, el arreglo no está vacío.
            }
        }
        return true;  // Todas las posiciones son nulas, el arreglo está "vacío".
    }

    public void ejecutarInstruccion(){
        int posicion = registros[buscarRegistro("PC")].getValor();
        Instruccion instruccionActual = (Instruccion) memoriaPrincipal.getContenido()[posicion];
        String operacion = instruccionActual.getOperacion();
        int valor;
        int valor2;
        int valorActual;
        int posicionActualizada;
        int posicionRegistro1;
        int posicionRegistro2;
        String registroOrigen;
        String registroDestino;
        String llamada;
        int param1;
        int param2;
        int param3;
        System.out.println(pila);

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
                valor2 = registros[posicionRegistro2].getValor();
                registros[posicionRegistro2].setValor(valor);
                registros[posicionRegistro1].setValor(valor2);
                break;
            case "INT":
                llamada = instruccionActual.getLlamada();
                if(llamada.equals("20H")){
                    bcpActual.setEstado("Terminado");
                }else if(llamada.equals("10H")){
                    posicionRegistro1 = buscarRegistro("DX");
                    valor = registros[posicionRegistro1].getValor();
                    String impresion = String.valueOf(valor);
                    pp.getTextAreaPantalla().setText("--> " + impresion + "\n");
                }else{
                    if(espera){
                        posicionRegistro1 = buscarRegistro("DX");
                        String impresion = pp.getTextFieldEntrada().getText();
                        valor = Integer.parseInt(impresion);
                        registros[posicionRegistro1].setValor(valor);
                        espera = false;
                        bcpActual.setEstado("Ejecutando");
                    }else{
                        if(!(bcpActual.getEstado().equals("Esperando"))){
                            bcpActual.setEstado("Esperando");
                        }
                    }
                }
                break;
            case "JMP":
                valor = instruccionActual.getValor();
                posicionActualizada = posicion + valor;
                if(posicionActualizada < bcpActual.getFinalMemoria()){
                    posicion = posicionActualizada-1;
                }else{
                    error(1);
                }
                break;
            case "CMP":
                registroOrigen = instruccionActual.getRegistroOrigen();
                posicionRegistro1 = buscarRegistro(registroOrigen);
                valor = registros[posicionRegistro1].getValor();
                registroDestino = instruccionActual.getRegistroDestino();
                posicionRegistro2 = buscarRegistro(registroDestino);
                valor2 = registros[posicionRegistro2].getValor();
                cmpResult = valor == valor2;
                break;
            case "JE":
                if(cmpResult){
                    valor = instruccionActual.getValor();
                    posicionActualizada = posicion + valor;
                    if(posicionActualizada < bcpActual.getFinalMemoria()){
                        posicion = posicionActualizada-1;
                    }else{
                        error(1);
                    }
                }
                break;
            case "JNE":
                if(!cmpResult){
                    valor = instruccionActual.getValor();
                    posicionActualizada = posicion + valor;
                    if(posicionActualizada < bcpActual.getFinalMemoria()){
                        posicion = posicionActualizada-1;
                    }else{
                        error(1);
                    }
                }
                break;
            case "PARAM":
                param1 = instruccionActual.getValor();
                param2 = instruccionActual.getValor2();
                param3 = instruccionActual.getValor3();
                if(instruccionActual.getCantidadParam() == 3){
                    if(pila.size()+3 <= 5){
                        pila.push(param1);
                        pila.push(param2);
                        pila.push(param3);
                    }else{
                        error(2);
                    }
                }else if(instruccionActual.getCantidadParam() == 2){
                    if(pila.size()+2 <= 5){
                        pila.push(param1);
                        pila.push(param2);
                    }else{
                        error(2);
                    }
                }else{
                    if(pila.size()+1 <= 5){
                        pila.push(param1);
                    }else{
                        error(2);
                    }
                }
                break;
            case "PUSH":
                if(pila.size() < 5){
                    registroOrigen = instruccionActual.getLlamada();
                    posicionRegistro1 = buscarRegistro(registroOrigen);
                    valorActual = registros[posicionRegistro1].getValor();
                    pila.push(valorActual);
                }else{
                    error(2);
                }
                break;
            case "POP":
                if(!pila.isEmpty()){
                    registroDestino = instruccionActual.getLlamada();
                    posicionRegistro1 = buscarRegistro(registroDestino);
                    valorActual = pila.pop();
                    registros[posicionRegistro1].setValor(valorActual);
                }else{
                    error(3);
                }
                break;
            default:
                System.out.println("A");
                JOptionPane.showMessageDialog (null, "Error en la carga del proceso nuevo. Asegurese de que el proceso no contenga errores y no cargar un proceso vacío", "Error", JOptionPane.ERROR_MESSAGE);
        }

        if(bcpActual.getEstado().equals("Preparado")){
            bcpActual.setEstado("Ejecutando");
            LocalTime horaActual = LocalTime.now();
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("HH:mm:ss");
            horaInicio = horaActual.format(formato);
        }else if(bcpActual.getEstado().equals("Terminado")){
            LocalTime horaActual = LocalTime.now();
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("HH:mm:ss");
            horaFinal = horaActual.format(formato);
            pp.getBtnInstruccion().setText("Siguiente Proceso");
        }else if(bcpActual.getEstado().equals("Ejecutando")){
            posicion++;
            if(!(posicion == bcpActual.getFinalMemoria())){
                registros[buscarRegistro("PC")].setValor(posicion);
                Instruccion siguienteInstruccion = (Instruccion) memoriaPrincipal.getContenido()[posicion];
                ir = siguienteInstruccion.getLinea();
            }else{
                error(4);
            }
        }
        actualizarBcp();
        actualizarTablaMemoria();
        actualizarTabla();
        try {
            TimeUnit.SECONDS.sleep(1);
            tiempoEjecucion += 1;
        } catch (InterruptedException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    public void ajustes(){
        String rutaArchivo = "C:\\Users\\Fabian\\Documents\\NetBeansProjects\\tareaSistemasOperativos\\proyecto-1-fabian_proyecto1_so\\ajustes.txt";
        try {
            String contenido = new String(Files.readAllBytes(Paths.get(rutaArchivo)));
            String[] listaAjustes = contenido.split("\\n");
            int tamanho = Integer.parseInt(listaAjustes[0].trim());
            int limite = Integer.parseInt(listaAjustes[1].trim());
            int secundaria = Integer.parseInt(listaAjustes[2].trim());
            Object[] contenidoMemoria = new Object[tamanho];
            this.memoriaPrincipal = new Memoria(contenidoMemoria, limite, secundaria);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cambiarAjustes(){
        String rutaArchivo = "C:\\Users\\Fabian\\Documents\\NetBeansProjects\\tareaSistemasOperativos\\proyecto-1-fabian_proyecto1_so\\ajustes.txt";
        String nuevoTamanho = pconfig.getTamanho().getText();
        String nuevoLimite = pconfig.getLimite().getText();
        String nuevaSecundaria = pconfig.getSecundaria().getText();
        String nuevoContenido = nuevoTamanho + "\n" + nuevoLimite + "\n" + nuevaSecundaria;

        try {
            Files.write(Paths.get(rutaArchivo), nuevoContenido.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        ajustes();
    }

    public void cambioContexto(){
        this.tiempoEjecucion = 0;
        this.pila = new Stack<Integer>();
        this.horaFinal = "";
        this.horaInicio = "";
        int id = bcpActual.getId();
        int inicio = bcpActual.getInicioMemoria();
        int finalMemoria = bcpActual.getFinalMemoria();
        if(listaBcp.size()>1){
            listaBcp.remove(0);
            bcpActual = listaBcp.get(0);
            this.ir =  bcpActual.getIr();
            for(int i = 0; i<identificadores.length; i++){
                int valor = bcpActual.getRegistros().get(identificadores[i]);
                int posicion = buscarRegistro(identificadores[i]);
                registros[posicion].setValor(valor);
            }
            actualizarTabla();
        }else{
            bcpActual = null;
            inicializarRegistros();
        }
        for(int i = 0; i < memoriaPrincipal.getLimite(); i++){
            if(memoriaPrincipal.getContenido()[i]!=null){
                Class<?> objClass = memoriaPrincipal.getContenido()[i].getClass();
                String clase = objClass.getSimpleName();
                if(clase.equals("Integer")){
                    int actual = (int) memoriaPrincipal.getContenido()[i];
                    if(actual==id){
                        int cont = i;
                        while(memoriaPrincipal.getContenido()[cont]!=null){
                            memoriaPrincipal.getContenido()[cont]=null;
                            cont++;
                        }
                        break;
                    }
                }
            }
        }
        for(int i = inicio; i<finalMemoria; i++){
            memoriaPrincipal.getContenido()[i]=null;
        }
        actualizarTabla();
    }
}
