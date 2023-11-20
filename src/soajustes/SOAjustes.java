
package soajustes;

import java.util.Scanner;
import java.util.ArrayList;


public class SOAjustes {

    
    public static void main(String[] args) {
        Scanner leer = new Scanner(System.in);
        int mem=0;
        int so=0;
        int cantidadProcesos = 0;
        boolean inte = true;
        int [][] procesos = null;
        
        //preguntar tamaño memoria
        while(inte){
        System.out.println("Inserte el tamaño de memoria");
        if (leer.hasNextInt()){
            mem=leer.nextInt();
            break;
        } else{
            System.out.println("Entrada no valida");
            leer.next();
        }

        }

        //pregunta tamaño sistema operativo
        while(inte){
        System.out.println("Inserte el tamaño del Sistema Operativo");
        if (leer.hasNextInt()){
            so=leer.nextInt();
            break;
        } else{
            System.out.println("Entrada no valida");
            leer.next();
        }

        }

        while (inte) {
            System.out.println("Inserte la cantidad de procesos");
            if (leer.hasNextInt()) {
                cantidadProcesos = leer.nextInt();
               procesos = new int[cantidadProcesos][6];  // numero de proceso, tamaño, duracion, rangoini, rangofin,(0=no ha entrado,1=en ejecucion,2=salió)

                leer.nextLine(); 

                for (int i = 0; i < procesos.length; i++) {
                    System.out.println("Ingrese el tamaño y la duración del proceso " + (i + 1) + " separados por espacio:");
                    String[] procesoInput = leer.nextLine().split(" ");
                    procesos[i][5]=0;

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
                for (int i = 0; i < procesos.length; i++) {
                    System.out.println("Proceso " + (i + 1) + ": Tamaño -> " + procesos[i][0] + ", Duración -> " + procesos[i][1]);
                }
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
        ArrayList<Integer> procesosFin = new ArrayList<>();
        
        //Primer ajuste
        while(ajustes<=3){
            for (int estado = 0, i=0; ejecucion; estado++) {
                System.out.println("Estado "+estado);
                System.out.println("| SO | 0 - "+ so+"     Base: "+base+ " Tamaño: "+tamaño);
                
                        //estado1
                         for (int j = 0; j<procesos.length && tamaño >= 0 && estado1 == true; j++) {
                            if(procesos[j][0]<=tamaño){
                                banderaEspacio = true;
                                if(j<=0){
                                base = procesos[j][0]+so;
                                System.out.print("| P"+j+" | "+so+" - "+base);
                                procesos[j][3]=so; 
                                procesos[j][4]=base;
                                procesos[j][5]=1;
                                tamaño-=procesos[j][0];
                                System.out.println("     Base: "+base+ " Tamaño: "+tamaño);
                                procesosExec.add(j);

                                }   
                                    else if((base+procesos[j][0])>mem){
                                        continue;
                                    }
                                      else{
                                            System.out.print("| P"+j+" | "+base+" - "+(procesos[j][0]+base));
                                            base+=procesos[j][0];  
                                            tamaño-=procesos[j][0];
                                            System.out.println("     Base: "+base+ " Tamaño: "+tamaño);
                                            procesos[j][3]=base;
                                            procesos[j][4]=tamaño;
                                            procesos[j][5]=1;
                                            procesosExec.add(j);
                                      }
                            } 
                                else{
                                    continue;
                                }
                        }

                        if(banderaEspacio){
                            estado1 = false;
                            if(tamaño!=0)
                            System.out.println("| -- | "+base+" - "+mem);



                            //siguientes estados
                                externo:
                                for(int p=0, e=0;p<procesos.length;p++,e++){
                                    //si faltan procesos por ejecutar
                                    for (int j = 0; j < procesos.length; j++) { 
                                        int contadorProcesosTerminados=0; //comprobar que los procesos hayan todos salido
                                        if(procesos[j][5]==0){
                                          procesosWait.add(j);
                                          
                                        }
                                        if(procesos[j][5]==2){
                                            contadorProcesosTerminados++;
                                        }
                                        if(contadorProcesosTerminados==cantidadProcesos){   //POSIBLE ERROR, CUIDAR ÍNDICES
                                            break externo;
                                        }
                                    }




                                        

                                }

                         }
                          else{
                            System.out.println("No hay espacio para ningun proceso: ");
                            break;
                          }

                          



                        //break;
                
                    
                        

                   
                ejecucion = false;
                ajustes = 4;
                break;
                
                   
                    
            }
            
        }

        



    }
    
}
