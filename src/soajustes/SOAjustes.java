
package soajustes;

import java.util.Scanner;
import java.util.Iterator;

import javax.swing.text.StyledEditorKit.ForegroundAction;


import java.util.ArrayList;

public class SOAjustes {

    public static void main(String[] args) {
        Scanner leer = new Scanner(System.in);
        int mem = 0;
        int so = 0;
        int cantidadProcesos = 0;
        boolean inte = true;
        int[][] procesos = null;
        int [] ram =null;
        ArrayList<int[][]> nodos = new ArrayList<>();
        ArrayList<Integer> procesosExec = new ArrayList<>();
        ArrayList<Integer> procesosWait = new ArrayList<>();

        // preguntar tamaño memoria
        while (inte) {
            System.out.println("Inserte el tamaño de memoria");
            if (leer.hasNextInt()) {
                mem = leer.nextInt()+1;
                ram=  new int[mem];
                for(int j=0;j<mem;j++){
                    ram[j] = -2;
                }
                break;
            } else {
                System.out.println("Entrada no valida");
                leer.next();
            }

        }

        // pregunta tamaño sistema operativo
        while (inte) {
            System.out.println("Inserte el tamaño del Sistema Operativo");
            if (leer.hasNextInt()) {
                so = leer.nextInt();
                break;
            } else {
                System.out.println("Entrada no valida");
                leer.next();
            }

        }

        while (inte) {
            System.out.println("Inserte la cantidad de procesos");
            if (leer.hasNextInt()) {
                cantidadProcesos = leer.nextInt();
                procesos = new int[cantidadProcesos][6]; // numero de proceso, tamaño, duracion, rangoini,
                                                         // rangofin,(0=no ha entrado,1=en ejecucion,2=salió)

                leer.nextLine();     
                for (int i = 0; i < procesos.length; i++) {
                    System.out.println(
                            "Ingrese el tamaño y la duración del proceso " + (i + 1) + " separados por espacio:");
                    String[] procesoInput = leer.nextLine().split(" ");
                    procesos[i][5] = 0;
                    procesosWait.add(i);

                    if (procesoInput.length == 2) {
                        try {
                            procesos[i][0] = Integer.parseInt(procesoInput[0]);
                            procesos[i][1] = Integer.parseInt(procesoInput[1]);
                        } catch (NumberFormatException e) {
                            System.out.println("Formato inválido para los valores del proceso " + (i + 1) + ".");
                            i--;
                        }
                    } else {
                        System.out.println("Debe ingresar dos valores separados por espacio.");
                        i--;
                    }
                }

                
                break;
            } else {
                System.out.println("Entrada no válida");
                leer.next();
            }
        }

        int base = so;
        int tamaño = (mem - so)-2;
        boolean ejecucion = true, estado1 = true;
        boolean banderaEspacio = false;
        
                System.out.println("Estado 0");
                System.out.println("| SO | 0 - " + so);
                for(int j=0;j<=so+1;j++){
                    ram[j] = -50;
                }

                // ESTADO 0, PROCESOS ENTRAN A MEMORIA
                for (int j = 0; j < procesos.length && tamaño > 0 && estado1 == true; j++) {
                    //System.out.println("TAMAÑO: "+tamaño);
                    //System.out.println("Procesos: "+j+" tamaño: "+procesos[j][0]);
                    if (procesos[j][0] <= tamaño) {
                        banderaEspacio = true;
                        if (j <= 0) {
                            base = procesos[j][0] + so;
                            System.out.println("| P" + j + " | " + (so+1) + " - " + base);
                            procesos[j][3] = so;
                            procesos[j][4] = base;
                            procesos[j][5] = 1;
                            
                            for(int k=so+1;k<=base+1;k++){
                                ram[k]=0;
                            }
                            
                            tamaño -= procesos[j][0] ;
                            
                            procesosExec.add(j);

                            for(int k=0;k<procesosWait.size();k++){
                                if(procesosWait.get(k) == j)
                                    procesosWait.remove(k);
                            }

                        } else if ((base + procesos[j][0]) > mem) {
                            continue;
                        } else {
                            System.out.println("| P" + j + " | " + (base+1) + " - " + (procesos[j][0] + base));
                            procesos[j][3] = base;
                            procesos[j][4] = procesos[j][0]+base;
                            //System.out.println("BASE: "+(base+1));
                            //System.out.println("LIMITE K: "+(procesos[j][4]+1));
                            
                            for(int k=base+1;k<=procesos[j][4];k++){
                                ram[k]=j;
                            }
                            base += procesos[j][0];
                            tamaño -= procesos[j][0];
                            
                            procesos[j][5] = 1;
                            procesosExec.add(j);
                            for(int k=0;k<procesosWait.size();k++){
                                if(procesosWait.get(k) == j)
                                    procesosWait.remove(k);
                            }
                        }
                    } else {
                        continue;
                    }
                }
                   
                
                                        
                //System.out.println("Tamaño: "+tamaño);
                tamaño-=2;
                if (banderaEspacio) {
                    estado1 = false;
                    boolean IO = true; // verdadero = salen procesos, falso = entran procesos
                    if (tamaño > 0){
                        System.out.println("| -- | " + (base+1) + " - " + (mem-1));
                        
                        System.out.println("Base: "+base+". Tamaño = "+tamaño);

                    }
                    else{
                        base = 0;
                        tamaño = 0;
                        //int [][] matriz = {{base,tamaño}};
                        //nodos.add(matriz);
                        System.out.println("Base: 0");
                        System.out.println("Tamaño: 0");
                        System.out.println("---");
                    }

                    externo:
                    // siguientes estados
                    for (int e = 1; banderaEspacio; e++) {
                        int contadorProcesosTerminados = 0;

                        // si faltan procesos por ejecutar. AGREGAR RESTO DE PROCESOS A PROCESOSWAIT
                        for (int j = 0; j < procesos.length; j++) {
                             // comprobar que los procesos hayan todos salido
                            
                            if (procesos[j][5] == 1) {
                                contadorProcesosTerminados++;
                                //System.out.println("Proceso: "+j);
                            }
                            if (contadorProcesosTerminados == cantidadProcesos) { // POSIBLE ERROR, CUIDAR ÍNDICES
                                banderaEspacio = false; // termina el programa
                                System.out.println("Todos los procesos entraron");
                                break externo;
                            }
                        }
 
                                System.out.println("Estado: " + e);
                                System.out.println("| SO | 0 - " + so);
                        if (IO) { //comprobar que todos los procesos posibles que puedan salir salgan
                            IO = false;
                            
                            //System.out.println("salen procesos");
                            // hallar proceso que saldrá, el proceso de menor duración
                            int duracionMenor = 0;
                            int indiceMenor = procesosExec.get(0);
                            for (int j = 1; j < procesosExec.size(); j++) {
                                int indice = procesosExec.get(j);
                                if (procesos[indice][1] <= procesos[indiceMenor][1]) {
                                    duracionMenor = procesos[indice][1];                             
                                    indiceMenor = indice;

                                }
                                 else{
                                    duracionMenor=procesos[indiceMenor][1];
                                 }
                            }

                           
                            boolean mismaRafaga= true;
                            for (int j = 0; j < procesosExec.size(); j++) {
                                int indice = procesosExec.get(j);
                                if (procesos[indice][1] != duracionMenor) {
                                    mismaRafaga = false;
                                    break;
                                }
                            }

                            if (mismaRafaga) {
                                System.out.println("Todos los procesos tienen la misma ráfaga");
                                break externo;
                            }

                            // proceso de menor duración encontrado, ahora ver si otro proceso sale al
                            // restarle la ráfaga de ese primer proceso
                            int rafagaRestar = procesos[indiceMenor][1];
                            //procesos[indiceMenor][5] = 2; // indicar que ese proceso salió
                            /* 
                            System.out.println("ProcesosWait");
                                    for(int j=0;j<procesosWait.size();j++){
                                        System.out.println(procesosWait.get(j));
                                    }
                                    */
                            
                            // indicar los procesos en ejecución que saldrán
                                Iterator<Integer> iterator = procesosExec.iterator();
                                while (iterator.hasNext()) {
                                    /* 
                                    System.out.println("ProcesosExec");
                                    for(int j=0;j<procesosExec.size();j++){
                                        System.out.println(procesosExec.get(j));
                                    }
                                    */
                                    int indice = iterator.next();
                                    procesos[indice][1] -= rafagaRestar;
                                    //System.out.println("Proceso: "+indice+" Ráfaga: "+procesos[indice][1]+" RafagaRestar: "+rafagaRestar);
                                        if (procesos[indice][1] <= 0) {
                                            //procesos[indice][5] = 2;
                                            for(int k=procesos[indice][3]+1;k<=procesos[indice][4];k++){
                                                
                                                ram[k] = -2;
                                            }
                                            //System.out.println("k: "+procesos[indice][3]);
                                            //System.out.println("Limite: "+procesos[indice][4]);
                                            
                                            iterator.remove();
                                         } 
                                         
                                }                                       
                                   /* 
                                for(int j=0;j<ram.length;j++){ 
                                    System.out.print("J:  "+j+" PROCESOS: "+ram[j]);
                                    System.out.println("");
                                 }
                                 */
                                       
                           
                            imprimirBloqueRAM(ram, nodos);
                            for(int k=0;k<procesos.length;k++){
                                        System.out.print("P"+k+": "+procesos[k][1]+", ");
                                        }
                                        System.out.println("");
                                
                                
                        } else if (!IO) {
                            IO = true;
                            
                            Iterator<Integer> iterator = procesosWait.iterator();
                            while (iterator.hasNext()) {
                                int indice = iterator.next();

                                boolean procesoAsignado = false; // Bandera para verificar si el proceso ha sido asignado

                                for (int h = 0; h < nodos.size(); h++) {
                                    int[][] nodo = nodos.get(h);
                                    for (int l = 0; l < nodo.length; l++) {
                                        int tamañoNodo = nodo[l][1];
                                        if (procesos[indice][0] - 1 <= tamañoNodo) {
                                            procesos[indice][5] = 1;
                                            procesos[indice][3] = nodo[l][0] - 1;
                                            procesos[indice][4] = nodo[l][0] + procesos[indice][0];
                                            procesosExec.add(indice);

                                            procesoAsignado = true; // El proceso ha sido asignado
                                            //System.out.println("Inicio: "+nodo[l][0]+" Final: "+(tamañoNodo+nodo[l][0]));
                                            for (int m = nodo[l][0]; m <(procesos[indice][0]+nodo[l][0]); m++) {
                                               
                                                ram[m] = indice;
                                            }
                                            
                                            break; // Sal del bucle interno una vez que el proceso ha sido asignado
                                        }
                                    }
                                    if (procesoAsignado) {
                                        iterator.remove(); // Elimina el elemento actual del Iterator
                                        break; // Sal del bucle externo una vez que el proceso ha sido asignado
                                    }
                                }
                            }

                            
                                     
                                     nodos.clear();
                                     imprimirBloqueRAM(ram, nodos);
                                     /*for(int k=0;k<procesos.length;k++){
                                        System.out.print("P"+k+": "+procesos[k][1]+", ");
                                        }
                                        */
                                         
                                        
                        }
                    }
                } else {
                    System.out.println("No entran procesos");
                    
                }

            


    }
    public static void imprimirBloqueRAM(int[] ram, ArrayList<int[][]> nodos) {
        int base = 0;
        int tamaño = 0;
        int temp = -50;
    
        for (int j = 0; j <= ram.length; j++) {
            if (j == ram.length) { // último bloque
                System.out.print(j - 1);
                System.out.println("");
                break;
            }
    
            int proceso = ram[j];
            if (proceso != temp && temp != -50) {
                System.out.print(j - 1);
                System.out.println("");
            }
    
            if (proceso >= 0 && proceso != temp) {
                temp = ram[j];
                System.out.print("| P" + temp + " | " + j + " - ");
            }
    
            if (proceso == -2 && proceso != temp) {
                temp = ram[j];
                System.out.print("| -- | " + j + " - ");
            }
        }
    
        for (int j = 0; j < ram.length; j++) {
            int proceso = ram[j];
            if (proceso == -2) {
                base = j;
                tamaño = 0;
    
                while (j < ram.length && (ram[j] == -2 || ram[j] == proceso)) {
                    tamaño++;
                    j++;
                }
    
                tamaño--;
    
                if (tamaño > 0) {
                    int[][] nodo = { { base, tamaño } };
                    nodos.add(nodo);
                }
            }
        }
    
        for (int j = 0; j < nodos.size(); j++) {
            int[][] nodo = nodos.get(j);
            if (base != 0) {
                System.out.println("Nodo " + j + ":");
                System.out.println("Base: " + nodo[0][0] + ". Tamaño = " + nodo[0][1]);
                System.out.println();
            } else {
                System.out.println("Nodo 0:");
                System.out.println("Base: 0. Tamaño = 0");
            }
        }

        
    }
    


}

