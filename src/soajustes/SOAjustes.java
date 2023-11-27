
package soajustes;

import java.util.Scanner;
import java.util.Iterator;

import javax.swing.text.StyledEditorKit.ForegroundAction;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SOAjustes {

    public static void main(String[] args) {
        Scanner leer = new Scanner(System.in);
        int mem = 0;
        int so = 0;
        int cantidadProcesos = 0;
        boolean inte = true;
        int[][] procesos = null;
        int[][] procesos2 = null;
        int[][] procesos3 = null;
        int [] ram =null;
        ArrayList<int[][]> nodos = new ArrayList<>();
        ArrayList<Integer> procesosExec = new ArrayList<>();
        ArrayList<Integer> procesosWait = new ArrayList<>();

        //externo2:
        // preguntar tamaño memoria
        while (true) {
            System.out.println("Inserte el tamaño de memoria");
            if (leer.hasNextInt()) {
                mem = leer.nextInt()+1;
                if ((mem-1) > 0) {
                    ram = new int[mem];
                    for (int j = 0; j < mem; j++) {
                        ram[j] = -2;
                    }
                    break;
                } else {
                    System.out.println("El tamaño de memoria debe ser mayor a cero");
                    
                }
            } else {
                System.out.println("Entrada no valida");
                leer.next();
            }
        }


        // pregunta tamaño sistema operativo
        while (true) {
            System.out.println("Inserte el tamaño del Sistema Operativo");
            if (leer.hasNextInt()) {
                so = leer.nextInt();
                if (so > 0) {
                    break;
                } else {
                    System.out.println("El tamaño del Sistema Operativo debe ser mayor a cero");
                    
                }
            } else {
                System.out.println("Entrada no valida");
                leer.next();
            }
        }
        

        while (inte) {
            System.out.println("Inserte la cantidad de procesos");
            if (leer.hasNextInt()) {
                cantidadProcesos = leer.nextInt();
                procesos = new int[cantidadProcesos][6];
                procesos2 = new int[cantidadProcesos][6];
                procesos3 = new int[cantidadProcesos][6];
                 // numero de proceso, tamaño, duracion, rangoini,
                                                         // rangofin,(0=no ha entrado,1=en ejecucion,2=salió)

                leer.nextLine();     
                for (int i = 0; i < procesos.length; i++) {
                    System.out.println(
                            "Ingrese el tamaño y la duración del proceso " + (i + 1) + " separados por espacio:");
                    String[] procesoInput = leer.nextLine().split(" ");
                    procesos[i][5] = 0;
                    procesos2[i][5] = 0;
                    procesos3[i][5] = 0;
                    //procesosWait.add(i);

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

        for (int i = 0; i < procesos.length; i++) {
            procesos2[i][0] = procesos[i][0];
            procesos3[i][0] = procesos[i][0];
            procesos2[i][1] = procesos[i][1];
            procesos3[i][1] = procesos[i][1];

        }

        int base = so;
        int tamaño = (mem - so)-2;
        boolean estado1 = true;
        boolean banderaEspacio = false;
        int ajuste = 0;
                externo:
                for(;ajuste<3;ajuste++){
                    for (int i = 0; i < procesos.length; i++) {
                        procesosWait.add(i);
                 }
                    if(ajuste==0){
                        System.out.println("Primer ajuste");
                    } else if(ajuste == 1){
                        System.out.println("Mejor ajuste");
                    }
                    else{
                        System.out.println("Peor ajuste");
                    }

                System.out.println("Estado 0");
                System.out.println("| SO | 0 - " + so);
                for(int j=0;j<=so+1;j++){
                    ram[j] = -50;
                }

                // ESTADO 0, PROCESOS ENTRAN A MEMORIA
                for (int j = 0; j < procesos.length && tamaño > 0 && estado1 == true; j++) {
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
                                 
                
                //tamaño-=2;
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
                        System.out.println("Base: 0");
                        System.out.println("Tamaño: 0");
                        System.out.println("---");
                    }

                    //externo:
                    // siguientes estados
                    for (int e = 1; banderaEspacio; ) {
                        int contadorProcesosTerminados = 0;

                        
                        for (int j = 0; j < procesos.length; j++) {
                             
                            
                            if (procesos[j][5] == 1) {
                                contadorProcesosTerminados++;
                                
                            } 
                            if (contadorProcesosTerminados == cantidadProcesos) { 
                                if(ajuste>=0){
                                    
                                    for(int k=0;k<procesos.length;k++){
                                    procesos[k][0]=procesos2[k][0];
                                    procesos[k][1]=procesos2[k][1];
                                    procesos[k][3]=procesos2[k][3];
                                    procesos[k][4]=procesos2[k][4];
                                    procesos[k][5]=procesos2[k][5];
                                    }
                                    for(int k=0;k<ram.length;k++){
                                        if(k<=so)
                                            ram[k]=-50;
                                        else
                                            ram[k] = -2;
                                    
                                    }

                                }
                               

                                banderaEspacio = false; // termina el programa
                                System.out.println("Todos los procesos entraron");
                                System.out.println("Estados "+e);
                                System.out.println("");
                                tamaño = (mem - so)-2;
                                estado1 = true;
                                nodos.clear();
                                procesosWait.clear();
                                procesosExec.clear();
                                continue externo;
                            }
                        }
 

                            System.out.println("Estado: " + e);
                            System.out.println("| SO | 0 - " + so);


                        if (IO) { //comprobar que todos los procesos posibles que puedan salir salgan
                            IO = false;

                            /* 
                            boolean mismaRafaga= true;
                            int duracion = procesos[procesosExec.get(0)][1];
                            for (int j = 0; j < procesosExec.size(); j++) {
                                int indice = procesosExec.get(j);
                                if (procesos[indice][1] != duracion) {
                                    mismaRafaga = false;
                                    break;
                                }
                            }

                            if (mismaRafaga) {
                                System.out.println("Todos los procesos tienen la misma ráfaga");
                                break externo2;
                            }

                            */
                            //metodo procesosSalir
                            salidaProcesos(procesosExec, procesosWait, procesos, ram);
                            e++;
                            nodos.clear();
                            imprimirBloqueRAM(ram, nodos);
                            for(int k=0;k<procesos.length;k++){
                                        System.out.print("P"+k+": "+procesos[k][1]+", ");
                                }
                                        System.out.println("");
                                
                                
                        } else if (!IO) {
                            IO = true;
                                     if(entrarProcesos(procesosExec, procesosWait, procesos, ram, nodos,ajuste))
                                            e++;


                                     nodos.clear();
                                     imprimirBloqueRAM(ram, nodos);
                                     
                                        
                        }
                    }
                } else {
                    System.out.println("No entran procesos");
                    
                }

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

    public static int encontrarProcesoMenorDuracion(ArrayList<Integer> procesosExec, int[][] procesos) {
        int duracionMenor = procesos[procesosExec.get(0)][1];
        int indiceMenor = procesosExec.get(0);
        
        for (int j = 1; j < procesosExec.size(); j++) {
            int indice = procesosExec.get(j);
            if (procesos[indice][1] <= duracionMenor) {
                duracionMenor = procesos[indice][1];
                indiceMenor = indice;
            }
        }
        
        return indiceMenor;
    }
    

    public static void salidaProcesos(ArrayList<Integer> procesosExec, ArrayList<Integer> procesosWait, int[][] procesos, int[] ram) {
                int indiceMenor = encontrarProcesoMenorDuracion(procesosExec, procesos);
                int rafagaRestar = procesos[indiceMenor][1];
                
                Iterator<Integer> iterator = procesosExec.iterator();
                while (iterator.hasNext()) {
                    int indice = iterator.next();
                    //System.out.println("Proceso en ejecucion: "+indice);
                    procesos[indice][1] -= rafagaRestar;
                    if (procesos[indice][1] <= 0) {
                        for (int k = procesos[indice][3] + 1; k <= procesos[indice][4]; k++) {
                            ram[k] = -2;
                            
                        }
                        iterator.remove();
                    }
                }
     
    }


    public static boolean entrarProcesos(ArrayList<Integer> procesosExec,ArrayList<Integer> procesosWait,int[][] procesos, int[] ram, ArrayList<int[][]> nodos,int ajuste){
        //metodo procesosEntrar
                                    boolean procesoEntrado = false;
                                    switch (ajuste) {
                                    case 1:
                                        
                                        Collections.sort(nodos, new Comparator<int[][]>() {
                                            @Override
                                            public int compare(final int[][] entrada1, final int[][] entrada2) {
                                                final int tamaño1 = entrada1[0][1];
                                                final int tamaño2 = entrada2[0][1];
                                                return Integer.compare(tamaño1, tamaño2);
                                            }
                                        });
                                        break;
                                    case 2:
                                        //descendente
                                        Collections.sort(nodos, new Comparator<int[][]>() {
                                            @Override
                                            public int compare(final int[][] entrada1, final int[][] entrada2) {
                                                final int tamaño1 = entrada1[0][1];
                                                final int tamaño2 = entrada2[0][1];
                                                return Integer.compare(tamaño2, tamaño1);
                                            }
                                        });
                                        break;
                                    default:
                                        // Lógica por defecto 
                                        break;
                                }




                            Iterator<Integer> iterator = procesosWait.iterator();
                            while (iterator.hasNext()) {
                                int indice = iterator.next();

                                boolean procesoAsignado = false; // Bandera para verificar si el proceso ha sido asignado

                                for (int h = 0; h < nodos.size(); h++) {
                                    int[][] nodo = nodos.get(h);
                                        int tamañoNodo = nodo[0][1];
                                        if (procesos[indice][0] - 1 <= tamañoNodo) {
                                            procesos[indice][5] = 1;
                                            procesos[indice][3] = nodo[0][0] - 1;
                                            procesos[indice][4] = nodo[0][0] + procesos[indice][0];
                                            int [][] matriz = new int[1][2];
                                            matriz[0][0] = (procesos[indice][0]+nodo[0][0]);
                                            matriz[0][1] = (nodo[0][0] +nodo[0][1]) - matriz[0][0];
                                        
                                            if(matriz[0][1]<=0){
                                                matriz[0][0] = 0;
                                                matriz[0][1] = 0;
                                                procesos[indice][4]--;
                                            }
                                            nodos.set(h, matriz);
                                            
                                            procesosExec.add(indice);
                                            
                                            procesoAsignado = true; // El proceso ha sido asignado
                                            for (int m = nodo[0][0]; m <(procesos[indice][0]+nodo[0][0]); m++) { 
                                               
                                                ram[m] = indice;
                                            }
                                           
                                            
                                        }
                                    
                                    if (procesoAsignado) {
                                        procesoEntrado = true;
                                        iterator.remove(); // Elimina el elemento actual del Iterator
                                        break; // Sal del bucle externo una vez que el proceso ha sido asignado
                                    }
                                }
                            }
                            return procesoEntrado;
                            
    }
    


}

