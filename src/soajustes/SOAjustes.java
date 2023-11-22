
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
                System.out.println("| SO | 0 - " + so);
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
                            
                            procesosExec.add(j);

                        } else if ((base + procesos[j][0]) > mem) {
                            continue;
                        } else {
                            System.out.print("| P" + j + " | " + (base+1) + " - " + (procesos[j][0] + base));
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
                        }
                    } else {
                        continue;
                    }
                }

                if (banderaEspacio) {
                    /*for(int j=0;j<ram.length;j++){ 
                                    System.out.print("J:  "+j+" PROCESOS: "+ram[j]);
                                    System.out.println("");
                                 }
                    */
                    estado1 = false;
                    boolean IO = true; // verdadero = salen procesos, falso = entran procesos
                    if (tamaño != 0){
                        System.out.println("| -- | " + (base+1) + " - " + (mem-1));
                        tamaño-=2;
                        //int [][] matriz ={{base,tamaño}};
                        //nodos.add(matriz);
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
                    for (int e = 1; banderaEspacio == true; e++) {
                        System.out.println("Estado: " + e);
                        System.out.println("| SO | 0 - " + so);

                        // si faltan procesos por ejecutar. AGREGAR RESTO DE PROCESOS A PROCESOSWAIT
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
                                            for(int k=procesos[indice][3]+1;k<=procesos[indice][4];k++){
                                                
                                                ram[k] = -2;
                                            }
                                            //System.out.println("k: "+procesos[indice][3]);
                                            //System.out.println("Limite: "+procesos[indice][4]);
                                            procesosSalidos++;
                                            iterator.remove();
                                         } 
                                         
                                }                                       
                                   /* 
                                for(int j=0;j<ram.length;j++){ 
                                    System.out.print("J:  "+j+" PROCESOS: "+ram[j]);
                                    System.out.println("");
                                 }
                                 */
                            //NODOS

                            for(int j=0;j<ram.length;j++){
                                int indice=ram[j];
                                if(indice==-2){
                                
                                    /* 
                                    if(base == 0){  //no hay nodos y el que será creado puede estar en cualquier rango
                                                base=procesos[indice][3];
                                                tamaño=procesos[indice][4]-base;
                                                int matriz[][] ={{base,tamaño}};
                                                nodos.set(0,matriz);

                                            }
                                            else if(base>0 && procesosSalidos<1){   //ya hay un nodo y el proceso cabe dentro de el
                                                

                                            }
                                             else if(procesosSalidos>1){   //hay más de un nodo, y lo agregaremos al final de la lista
                                                 base=procesos[indice][3];
                                                tamaño=procesos[indice][4]-base;
                                                int matriz[][] ={{base,tamaño}};
                                                nodos.add(matriz);

                                 
                                            }
                                            */





                                        }
                            }           
                           
                                 //IMPRIMIR BLOQUE RAM
                            int temp=-50;
                            for(int j=0;j<=ram.length;j++){
                                 
                                if(j==ram.length){   //ultimo bloque
                                    System.out.print(j-1);
                                    System.out.println("");
                                    break;
                                 }
                                int proceso = ram[j];
                                if(proceso!=temp && temp!=-50){
                                    System.out.print(j-1);
                                    System.out.println("");
                                
                                 }
                                
                                 if(proceso>=0 && proceso!=temp){  //1. proceso 0 temp = -3 proceso 0 temp = 0
                                    temp = ram[j]; //2. 0
                                    System.out.print("| P"+temp+" | "+ j +" - ");
                                    }
                                if(proceso == -2 && proceso!=temp){
                                    temp=ram[j];
                                    System.out.print("| -- | "+j+" - ");
                                }
                                 
                            }

                            // Ajustar el código para registrar los nodos correctamente
                                    for (int j = 0; j < ram.length; j++) {
                                        int proceso = ram[j];
                                        if (proceso == -2) {
                                         base = j;
                                        tamaño = 0;

                                        // Encontrar el final del nodo (cambios de procesos o espacio vacío)
                                        while (j < ram.length && (ram[j] == -2 || ram[j] == proceso)) {
                                            tamaño++;
                                            j++;
                                        }

                                        // Restar 1 al tamaño para obtener el rango real (índices basados en 0)
                                        tamaño--;

                                        // Almacenar el nodo si tiene un tamaño válido
                                        if (tamaño > 0) {
                                            int[][] nodo = { { base, tamaño } };
                                            nodos.add(nodo);
                                            }
                                        }
                                    }



                            //NODOS
                            for (int j = 0; j < nodos.size(); j++) {
                                int[][] nodo = nodos.get(j);
                                if(base!=0){
                                System.out.println("Nodo " + (j + 1) + ":"); // Utiliza j en lugar de i para el número del nodo
                                System.out.println("Base: " + nodo[0][0] + ". Tamaño = " + nodo[0][1]);
                                System.out.println();
                                }
                                else{
                                    System.out.println("Nodo 0:");
                                    System.out.println("Base: 0. Tamaño = 0");
                                }
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
                                    tamañoMenor = procesos[indice][0];                             
                                    indiceMenor = indice;

                                }
                                 else{
                                    tamañoMenor=procesos[indiceMenor][0];
                                 }
                            }

                            for(int j=0; j<nodos.size();j++){
                                int[][] nodo=nodos.get(j);
                                 if(tamañoMenor<=nodo[j][1]){
                                    
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

