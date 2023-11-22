
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

                /*
                 * for (int i = 0; i < procesos.length; i++) {
                 * System.out.println("Proceso " + (i + 1) + ": Tamaño -> " + procesos[i][0] +
                 * ", Duración -> " + procesos[i][1]);
                 * }
                 */
                break;
            } else {
                System.out.println("Entrada no válida");
                leer.next();
            }
        }

        int base = so;
        int tamaño = mem - so;
        boolean ejecucion = true, estado1 = true;
        int ajustes = 0;
        boolean banderaEspacio = false, banderaFaltantes = true;

        ArrayList<Integer> procesosExec = new ArrayList<>();
        ArrayList<Integer> procesosWait = new ArrayList<>();
        

        // Primer ajuste
        while (ajustes <= 3) {
            for (int estado = 0, i = 0; ejecucion; estado++) {
                System.out.println("Estado " + estado);
                System.out.println("| SO | 0 - " + so + "     Base: " + base + " Tamaño: " + tamaño);
                for(int j=0;j<=tamaño+1;j++){
                    ram[j] = -50;
                }

                // estado1
                for (int j = 0; j < procesos.length && tamaño >= 0 && estado1 == true; j++) {
                    if (procesos[j][0] <= tamaño) {
                        banderaEspacio = true;
                        if (j <= 0) {
                            base = procesos[j][0] + so;
                            System.out.print("| P" + j + " | " + (so+1) + " - " + base);
                            procesos[j][3] = so;
                            procesos[j][4] = base;
                            procesos[j][5] = 1;
                            for(int k=so+1;k<=base+1;k++){
                                ram[k]=0;
                            }
                            tamaño -= procesos[j][0];
                            System.out.println("     Base: " + base + " Tamaño: " + tamaño);
                            procesosExec.add(j);

                        } else if ((base + procesos[j][0]) > mem) {
                            continue;
                        } else {
                            System.out.print("| P" + j + " | " + (base+1) + " - " + (procesos[j][0] + base));
                            procesos[j][3] = base;
                            procesos[j][4] = procesos[j][0]+base;
                            for(int k=base+1;k<=procesos[j][4]+1;k++){
                                ram[k]=j;
                            }
                            base += procesos[j][0];
                            tamaño -= procesos[j][0];
                            System.out.println("     Base: " + base + " Tamaño: " + tamaño);
                            procesos[j][5] = 1;
                            procesosExec.add(j);
                        }
                    } else {
                        continue;
                    }
                }

                if (banderaEspacio) {
                    
                    estado1 = false;
                    boolean IO = true; // verdadero = salen procesos, falso = entran procesos
                    if (tamaño != 0){
                        System.out.println("| -- | " + base + " - " + (mem-1));
                        int [][] matriz ={{base,tamaño}};
                        nodos.add(matriz);
                        System.out.println("Base: "+base);
                        System.out.println("Tamaño: "+tamaño);
                    }
                    else{
                        System.out.println("Base: 0");
                        System.out.println("Tamaño: 0");
                    }

                    externo:
                    // siguientes estados
                    for (int e = 1; banderaEspacio == true; e++) {
                        System.out.println("Estado: " + e);
                        System.out.println("| SO | 0 - " + so);

                        // si faltan procesos por ejecutar
                        for (int j = 0; j < procesos.length; j++) {
                            int contadorProcesosTerminados = 0; // comprobar que los procesos hayan todos salido
                            if (procesos[j][5] == 0) {
                                procesosWait.add(j);

                            }
                            if (procesos[j][5] == 2) {
                                contadorProcesosTerminados++;
                            }
                            if (contadorProcesosTerminados == cantidadProcesos) { // POSIBLE ERROR, CUIDAR ÍNDICES
                                banderaEspacio = false; // termina el programa
                                System.out.println("Todos los procesos terminados");
                                break externo;
                            }
                        }
 
                        int procesosSalidos = 0;
                        if (IO) { //comprobar que todos los procesos posibles que puedan salir salgan
                            IO = false;
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
                            procesos[indiceMenor][5] = 2; // indicar que ese proceso salió
                            procesosSalidos = 1; // contador que me servirá para saber si hay más de un proceso que
                                                 // saldrá y dejará más de un hueco en memoria

                            
                            // indicar los procesos en ejecución que saldrán
                                Iterator<Integer> iterator = procesosExec.iterator();
                                while (iterator.hasNext()) {
                                    int indice = iterator.next();
                                    procesos[indice][1] -= rafagaRestar;
                                        if (procesos[indice][1] <= 0) {
                                            procesos[indice][5] = 2;
                                            for(int k=procesos[indice][3]+1;k<=procesos[indice][4]+1;k++){
                                                ram[k] = -2;
                                            }
                                            procesosSalidos++;
                                            iterator.remove(); // Elimina el elemento actual de la lista
                                    }
                                }                                       
                                   /*          
                                for(int j=0;j<1000;j++){ 
                                    System.out.print("J:  "+j+" PROCESOS: "+ram[j]);
                                    System.out.println("");
                                 }
                            */
                            // empezar a imprimir el estado, caso procesossalidos<=1 no hay ajuste, caso
                            // else sí hay ajustes
                            /* 
                            for (int j = 0; j < procesos.length; j++) {
                                if(procesos[j][5] == 2){
                                    if(base == procesos[j][4]){
                                        System.out.println("| -- | "+procesos[j][3]+" - "+mem);
                                        int tamañoTemp = mem-procesos[j][3];
                                        int [][] matriz = {{procesos[j][3],tamañoTemp}};
                                        nodos.add(matriz);
                                    }
                                    else{
                                        System.out.println("| -- | "+procesos[j][3]+" - "+procesos[j][4]);
                                        int tamañoTemp = procesos[j][4]-procesos[j][3];
                                        int [][] matriz = {{procesos[j][3],tamañoTemp}};
                                        nodos.add(matriz);
                                    }
                                }
                                else if(procesos[j][5]==1){
                                    System.out.println("| P"+j+" | "+procesos[j][3]+" - "+procesos[j][4]);
                                }
                            }
                            */
                            int temp=-50;
                            for(int j=0;j<ram.length;j++){
                                int proceso = ram[j];
                                if(proceso!=temp){
                                    System.out.print("J: "+(j-1)+" Proceso: "+proceso+" Temp: "+temp);
                                    System.out.println("");
                                 }
                                 if(proceso>=0 && proceso!=temp){  //1. proceso 0 temp = -3 proceso 0 temp = 0
                                    temp = ram[j]; //2. 0
                                    System.out.print("| P"+temp+" | "+ j +" - ");
                                    } 
                                 
                            }

                            //NODOS
                            for (int j = 0; j < nodos.size(); j++) {
                                int[][] nodo = nodos.get(j);
                                System.out.println("Nodo " + (j + 1) + ":"); // Utiliza j en lugar de i para el número del nodo
                                System.out.println("Base: " + nodo[0] + ". Tamaño = " + nodo[1] + ";");
                                System.out.println();
                            }
                            



                            System.exit(0);
                        
                        } else if (!IO) {
                            IO = true;

                            //encontrar por fifo el proceso de menor tamaño
                            int tamañoMenor = 0;
                            int indiceMenor = procesosWait.get(0);
                            for (int j = 1; j < procesosWait.size(); j++) {
                                int indice = procesosWait.get(j);
                                if (procesos[indice][0] <= procesos[indiceMenor][0]) {
                                    tamañoMenor = procesos[indice][1];                             
                                    indiceMenor = indice;

                                }
                                 else{
                                    tamañoMenor=procesos[indiceMenor][0];
                                 }
                            }

                            //comparar el tamaño de ese proceso con espacios o espacio disponible por fifo y ver en cual se puede meter
                           //|SO| 0 -400  |P1| 400- 500 ... |---| 500-700 entra P2 de 100 quedaría |P2|500-600 |---|600-700
                            for(int j=0; j<nodos.size();j++){
                                int[][] nodo=nodos.get(j);
                                 if(tamañoMenor<=nodo[j][1]){
                                    procesos[indiceMenor][3] = nodo[j][0];
                                    procesos[indiceMenor][4] = nodo[j][1];
                                    break;
                                 }
                            }




                        }
                    }
                } else {
                    System.out.println("No hay espacio para ningun proceso: ");
                    break;
                }

                // break;

                ejecucion = false;
                ajustes = 4;
                break;

            }

        }

    }

}
