import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class Politica {
	//prioridad = t5, t10, t7, t8, t9, t6, t11, t4, t2, t3, t0, t1
	private int[][] matrizPolitica;
	private int[] vectorAND = new int[]{};
	private int[] vectorPOLITICA = new int[]{0,0,0,0,0,0,0,0,0,0,0,0};
	private int[] vectorPrioridad;
	private int cant_trans;
	private int[] vectorPriorMostrar = new int[]{0,0,0,0,0,0,0,0,0,0,0,0};
	public Politica(int cant){
		cant_trans = cant;
		cargarPolitica("prioridad_default.txt",cant_trans);
	}

	private void cargarPolitica(String file_name, int numero) {
		int cant_transiciones = numero;
		vectorPrioridad = new int[cant_transiciones];
		try{            
            FileInputStream fstream = new FileInputStream(file_name);	// Abrimos el archivo            
            DataInputStream entrada = new DataInputStream(fstream);		// Creamos el objeto de entrada 
            BufferedReader buffer = new BufferedReader(new InputStreamReader(entrada));		// Creamos el Buffer de Lectura
            String strLinea;
            int j=0;
            // Leer el archivo linea por linea	
            while ((strLinea = buffer.readLine()) != null)   {
            	String [] linea = strLinea.split(" ");
            	for(j=0;j<cant_transiciones;j++){
            		vectorPrioridad[j] = Integer.parseInt(linea[j]);
            	}
            }	
            entrada.close();	// Cerramos el archivo
        }catch (Exception e){ //Catch de excepciones
            System.err.println("Ocurrio un error: " + e.getMessage());
        }
		for(int i=0; i<vectorPriorMostrar.length; i++)
			vectorPriorMostrar[i] = vectorPrioridad[i];
		crearMatrizP(vectorPrioridad);
	}
	
	public void crearMatrizP(int[] vector){
		matrizPolitica = new int[cant_trans][cant_trans];
		for(int i=0; i<vector.length;i++)
			matrizPolitica[vector[i]][i]=1;	
	}
	
	private int verificarTiempos(int[] and, ArrayList<Semaforo> transiciones){
		int mayor=-1;
		long timeMayor=50, timeActual;
		for(int i=0; i<transiciones.size(); i++){
			if( transiciones.get(i).getQueueLength()>0){
				timeActual = transiciones.get(i).getTimeVerif();
				if(timeActual>timeMayor && and[i]==1){
					mayor = i;
					timeMayor=timeActual;	
				}
			}
		}
		return mayor;
	}
	public int calcularPolitica(int[] vector, ArrayList<Semaforo> transiciones){
		vectorAND = vector;
		Boolean calculoPrioridad = false;
		for(int i=0;i<vectorAND.length;i++){
			if(vectorAND[i]!=0)
				calculoPrioridad = true;
		}
			
		if(calculoPrioridad){
			int[] newPriority = new int[]{}; 
			newPriority = vectorPrioridad;
			int t_delayed = verificarTiempos(vector, transiciones);
			int pos_t_delayed = 0;
			for(int j=0;j<cant_trans;j++){
				if(newPriority[j]==t_delayed)
					pos_t_delayed=j;
			}
			if(t_delayed >= 0){
				int aux;
				for(int i=pos_t_delayed; i>0;i--){
					aux = newPriority[i-1];
					newPriority[i-1] = newPriority[i];
					newPriority[i] = aux;
				}
				setPriority(cant_trans, newPriority);
				
				for(int i=0; i<vectorPriorMostrar.length; i++)
					vectorPriorMostrar[i] = newPriority[i];		
			}
		}
		
		int[] vectorNEW = new int[]{-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
		for(int j=0; j<vectorPOLITICA.length;j++){
			vectorPOLITICA[j]=0;
			vectorNEW[j]=-1;
		}
		for(int i=0; i<vectorAND.length;i++){
			for(int j=0;j<vectorAND.length;j++){
				if(vectorAND[i]==1)
					vectorNEW[i]=i;
			}
		}
		int aux=0;
		for(int i=0; i<vectorAND.length;i++){
			for(int j=0;j<vectorAND.length;j++){
				aux += vectorNEW[j]*matrizPolitica[j][i];
			}
			vectorPOLITICA[i]=aux;
			aux=0;
		}		
		for(int i=0; i<vectorPOLITICA.length;i++){
			if(vectorPOLITICA[i] >= 0)
				return vectorPOLITICA[i];
		}	
		cargarPolitica("prioridad_default.txt",cant_trans);
		return -1; 
		}
	
	public void setPriority(int cant, int[] p){
		cant_trans = cant;
		int[] vPrioridad = p;
		crearMatrizP(vPrioridad);
	}
	
	public int[] getPriority(){
		return vectorPriorMostrar;
	}
}