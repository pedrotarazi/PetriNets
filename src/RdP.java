import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;


public class RdP {	
	private int[][] incidencia;
	private int[] marcadoActual;
	private int A,B,C,D,EnA,EnB,EnC,EnD,Cond;
	public RdP(){
		cargarMatrizIndidencia("matrizI_default.txt",9,12);
		cargarMarcadoActual("marcado_default.txt",9);
	}
	
	private void inicializacion(int[] marcadoActual) {
		A=marcadoActual[0];
		B=marcadoActual[1];
		C=marcadoActual[2];
		D=marcadoActual[3];
		EnA=marcadoActual[4];
		EnB=marcadoActual[5];
		EnC=marcadoActual[6];
		EnD=marcadoActual[7];
		Cond=marcadoActual[8];
	}

	private void cargarMarcadoActual(String file_name, int numeroP) {
		int cant_plazas = numeroP;
		marcadoActual = new int[cant_plazas];
		try{            
            FileInputStream fstream = new FileInputStream(file_name);	// Abrimos el archivo            
            DataInputStream entrada = new DataInputStream(fstream);		// Creamos el objeto de entrada 
            BufferedReader buffer = new BufferedReader(new InputStreamReader(entrada));		// Creamos el Buffer de Lectura
            String strLinea;
            int j=0;
            // Leer el archivo linea por linea	
            while ((strLinea = buffer.readLine()) != null)   {
            	String [] linea = strLinea.split(" ");  //Separamos la linea por cada espacio y lo guardamos en un arreglo
            	for(j=0;j<cant_plazas;j++){
            		marcadoActual[j] = Integer.parseInt(linea[j]);
            	}
            }	
            entrada.close();	// Cerramos el archivo
        }catch (Exception e){ //Catch de excepciones
            System.err.println("Ocurrio un error: " + e.getMessage());
        }
	}

	private void cargarMatrizIndidencia(String file_name, int numeroP, int numeroT) {
		int cant_trans = numeroT;
		int cant_plazas = numeroP;
		incidencia = new int[cant_plazas][cant_trans];
        try{            
            FileInputStream fstream = new FileInputStream(file_name);	// Abrimos el archivo            
            DataInputStream entrada = new DataInputStream(fstream);		// Creamos el objeto de entrada 
            BufferedReader buffer = new BufferedReader(new InputStreamReader(entrada));		// Creamos el Buffer de Lectura
            String strLinea;
            int j=0, pos;
            // Leer el archivo linea por linea	
            while ((strLinea = buffer.readLine()) != null)   {
            	String [] linea = strLinea.split(" ");
            	pos = 0;
            	for(int i=0;i<cant_trans;i++){
            		incidencia[j][pos] = Integer.parseInt(linea[pos]);
            		pos++;
            	}
            	j++;	
            }
            entrada.close();	// Cerramos el archivo
        }catch (Exception e){ //Catch de excepciones
            System.err.println("Ocurrio un error: " + e.getMessage());
        }
    }
	
	//Método que devuelve la cantidad de Plazas que tiene la RdP
	public int get_numeroP(){
		return incidencia.length;
	}
 
	//Método que devuelve la cantidad de Transiciones que tiene la RdP	 
	public int get_numeroT(){
		return incidencia[0].length;
	}
	
	public Boolean calcularEstado(int transicion){
		for(int i=0 ; i<get_numeroP() ; i++){
			//Vemos que plazas van a una transicion en particular, y si esas plazas tienen 0 token.
			if (( incidencia[i][transicion] < 0 ) && (( marcadoActual[i] - 1) < 0 )){
				return false;
			}	
		}
		for(int i=0 ; i<get_numeroP() ; i++){
			marcadoActual[i] += incidencia[i][transicion];
		}
		return true;
	}
	
	public int[] calcularTranSens(){
		inicializacion(marcadoActual);
		int[] vectorS = new int[]{0,0,0,0,0,0,0,0,0,0,0,0};
		
		if(EnC==1 && D==1 && EnD==0)
			vectorS[0]=1; //T0 sensibilizada
		if(EnD==1 && EnA==0 && A==1)
			vectorS[1]=1; //T1 sensibilizada
		if(EnA==1 && B==1 && EnB==0)
			vectorS[2]=1; //T2 sensibilizada
		if(EnC==0 && EnB==1 && C==1)
			vectorS[3]=1; //T3 sensibilizada
		if(D==1 && Cond>0 && EnD==0)
			vectorS[4]=1; //T4 sensibilizada
		if(EnA==1)
			vectorS[5]=1; //T5 sensibilizada
		if(B==1 && Cond>0 && EnB==0)
			vectorS[6]=1; //T6 sensibilizada
		if(EnC==1)
			vectorS[7]=1; //T7 sensibilizada
		if(EnD==1)
			vectorS[8]=1; //T8 sensibilizada
		if(A==1 && Cond>0 && EnA==0)
			vectorS[9]=1; //T9 sensibilizada
		if(EnB==1)
			vectorS[10]=1; //T10 sensibilizada
		if(C==1 && Cond>0 && EnC==0)
			vectorS[11]=1; //T11 sensibilizadas
		return vectorS;
	}
	
	public void getMarcado(){
		String print = new String();
		for(int i=0 ; i<get_numeroP() ; i++){
			print += (marcadoActual[i] + " ");
		}
		System.out.println(print);
	}
}
