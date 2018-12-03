import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JLabel;


public class Monitor {
	
	private int outCars = 0; //Indicar de la cantidad de autos que terminaron su ejecucion
	private int cantP, cantT, cantA; //Cantidad de Plazas y Cantidad de Transiciones
	private Semaforo colaEntrada; //Cola de entrada al Monitor
	private ArrayList<Semaforo> transiciones = new ArrayList<Semaforo>(); //Array de Semaforos. 1 por c/T
	private RdP rdp;  
	private Politica pol;
	private int[] vectorC, vectorS, vectorAND; //Vectores que guardan los vectores COLA, SENSIBILIZADO, y AND
	private int dispT; //Indicador de la proxima transicion a disparar segun la politica
	private JLabel pA, pB, pC, pD, pN, pS, pE, pO, psN, psS, psE, psO, fin;
	private boolean ejecucionDesdeView = false;
	private int tiempo=0;
	public Monitor(){
		rdp = new RdP(); //Creamos un objeto RdP (Red de Petri)
		cantP = rdp.get_numeroP(); 
		cantT = rdp.get_numeroT();
		pol = new Politica(cantT); 
		crearSemaforos(cantT); //Creamos los semaforos segun la cantidad de transiciones
		vectorC = new int[]{0,0,0,0,0,0,0,0,0,0,0,0};
		vectorS = new int[]{0,0,0,0,0,0,0,0,0,0,0,0};
		vectorAND = new int[]{0,0,0,0,0,0,0,0,0,0,0,0};
	}
	
	public Monitor(boolean fuenteEjecucion){
		ejecucionDesdeView = true;
		rdp = new RdP(); //Creamos un objeto RdP (Red de Petri)
		cantP = rdp.get_numeroP(); 
		cantT = rdp.get_numeroT();
		pol = new Politica(cantT); 
		crearSemaforos(cantT); //Creamos los semaforos segun la cantidad de transiciones
		vectorC = new int[]{0,0,0,0,0,0,0,0,0,0,0,0};
		vectorS = new int[]{0,0,0,0,0,0,0,0,0,0,0,0};
		vectorAND = new int[]{0,0,0,0,0,0,0,0,0,0,0,0};
	}
	/*
	 * Funcion que crea los semaforos segun la cantidad de transiciones que se les pase
	 */
	private void crearSemaforos(int t) throws NullPointerException{
		colaEntrada = new Semaforo(1,false,13);
		for(int j=0;j<t;j++)
			transiciones.add(new Semaforo(0,false,j)); // 0 semaforo en rojo. 1 semaforo en verde
	}

	/*
	 * Funcion que llaman los Autos cuando quieren disparar una transicion.
	 * @transicion: transicion a disparar
	 * @id: id del auto que disparar la transicion
	 */
	public void disparar(Integer transicion, String id) throws InterruptedException {
		
		colaEntrada.acquireUninterruptibly(); //Monitor ocupado
		
		// Mientras no se pueda disparar
		while(!(rdp.calcularEstado(transicion))){
			colaEntrada.release(); //Liberamos cola de entrada del monitor
			transiciones.get(transicion).acquireUninterruptibly(); //El auto que no pudo disparar
																   //se duerme y se encola.
		}

		//Cuando se ejecutan las transiciones de salida, outCars suma 1.
		if(transicion==5 || transicion==10 || transicion==7 || transicion==8){
			outCars++;
		}
		if(ejecucionDesdeView){
			writeEntradas(transicion,id);
			writeView(transicion,id);
		}

		// Cuando se disparó...
		vectorS = rdp.calcularTranSens(); //Calcular vector sensibilizado
		vectorC = quienEstaEnLaCola(transiciones); //Verifica que transicion tiene autos en espera
		vectorAND = calcularAND(vectorS, vectorC); //Calcula la AND, indicando transiciones que 
												   //pueden dispararse y que tienen autos en espera
		printTransicion(transicion,id); //Se imprime mensaje
		//Cuando hay al menos un auto en espera en alguna transicion, el boolean es TRUE
		Boolean hayAutosColas = verifColas();
		//Se calcula que transicion debe dispararse segun la politica
		dispT = pol.calcularPolitica(vectorAND,transiciones);
		
		//Se pasa la exlusion mutua a quien corresponda
		if(hayAutosColas){ //Si alguno de los autos encolados se puede disparar, entonces lo despierto
			if(dispT>=0){
				transiciones.get(dispT).release();
			}
			else colaEntrada.release(); //Si no se puede disparar, libero el monitor
		}
		else{ //Si no hay autos en las colas, se libera el monitor
			colaEntrada.release();
		}
		
	}
	
	/*
	 * Funcion de impresion.
	 */
	private void printTransicion(Integer transicion, String id) {
		Semaforo s = transiciones.get(transicion);
		System.out.print(s.getNombre()+" ");
		System.out.print(id+" ");
		toStringVectores(vectorC, vectorS, vectorAND, pol.getPriority());
		rdp.getMarcado();
		if(outCars==cantA) {getTimeMaxAuto();}
		}

	private void toStringVectores(int[] vectorC2, int[] vectorS2, int[] vectorAND2, int[] vectorPOL) {
		for(int i=0; i<vectorC2.length;i++)
			System.out.print(""+vectorC2[i]+", ");
		for(int i=0; i<vectorS2.length;i++)
			System.out.print(""+vectorS2[i]+", ");
		for(int i=0; i<vectorAND2.length;i++)
			System.out.print(""+vectorAND2[i]+", ");
		for(int i=0; i<vectorPOL.length;i++)
			System.out.print(""+vectorPOL[i]+", ");
		System.out.print("");
	}

	/*
	 * Funcion que devuelve TRUE si hay al menos 1 auto en alguna cola. De lo contrario, FALSE
	 */
	private Boolean verifColas() {
		for(int i=0;i<vectorC.length;i++){
			if(vectorC[i]!=0)
				return true;
		}
		return false;
	}

	/*
	 * Imprime una lista de la cantidad de autos que tienen todas las transiciones
	 */
	public void mostrarAutosEnColas(){
		System.out.print("COL : ");
		for(int i=0;i<cantT;i++){
			System.out.print(transiciones.get(i).getQueueLength()+", ");
		}	
		System.out.println("\n");
	}
	
	/*
	 * Funcion que devuelve el vector C, indicando que transiciones tienen autos en espera
	 */
	private int[] quienEstaEnLaCola(ArrayList<Semaforo> colas){
		int[] vector = new int[]{0,0,0,0,0,0,0,0,0,0,0,0};
		Semaforo s;
		for(int i=0; i<cantT; i++){
			s = colas.get(i);
			if(s.getQueueLength()>0)
				vector[i] = 1;
		}
		return vector;
	}
	
	/*
	 * Funcion que retorna la AND entre S y C. 
	 * S: transiciones SENSIBILIZADAS
	 * C: transiciones con autos en ESPERA
	 * AND: transiciones que tienen autos en ESPERA y SE PUEDEN DISPARAR
	 */
	private int[] calcularAND(int[] s, int[] c){
		int[] resultado = new int[]{0,0,0,0,0,0,0,0,0,0,0,0};
		for(int i=0; i<cantT; i++){	
			resultado[i] = s[i] & c[i];
		}
		return resultado;
	}

	/*
	 * Retorna el array de transiciones.
	 */
	public ArrayList<Semaforo> getSemaforos(){
		return transiciones;
	}
	
	public void getTimeMaxAuto(){
		long max=-1;
		String s="";
		String j="";
		for(int i=0; i<cantT; i++){
			if(transiciones.get(i).getMaxTime()>max){
				max=transiciones.get(i).getMaxTime();
				s=transiciones.get(i).getNameMaxTime();
				j=transiciones.get(i).getNombre();
			}
		}
		//System.out.println("Auto con maximo tiempo en cola: "+s+" estuvo "+max+
		//		" milisegundos en "+j);
		System.out.print(j+" "+s+" "+max);
	}
	
	public long getTimeMax(){
		long max=-1;
		for(int i=0; i<cantT; i++){
			if(transiciones.get(i).getMaxTime()>max){
				max=transiciones.get(i).getMaxTime();
			}
		}
		return max;
	}
	
	public void setCantAutos(int cant){
		cantA = cant;
	}
	
	public int getCantAutos(){
		return cantA;
	}
	
	public void setView(JLabel A, JLabel B, JLabel C, JLabel D, JLabel N, JLabel S, JLabel E, JLabel O,  JLabel sN,  JLabel sS,  JLabel sE,  JLabel sO, JLabel finalizados) {
		pA = A;
		pB = B;
		pC = C;
		pD = D;
		pN = N;
		pS = S;
		pE = E;
		pO = O;
		psN = sN;
		psS = sS;
		psE = sE;
		psO = sO;
		fin = finalizados;
		
	}
	
	public void writeEntradas(int transicion, String id){
		if(transicion == 4)
			pO.setText(id);
		
		if(transicion == 6)
			pE.setText(id);
		
		if(transicion == 9)
			pS.setText(id);
		
		if(transicion == 11)
			pN.setText(id);
		
		if(transicion == 4 && pO.getText().equals("") && transiciones.get(4).getQueueLength()>0)
			pO.setText(transiciones.get(4).getPrimero());
		
		if(transicion == 6 && pE.getText().equals("") && transiciones.get(6).getQueueLength()>0)
			pO.setText(transiciones.get(6).getPrimero());
		
		if(transicion == 9 && pS.getText().equals("") && transiciones.get(9).getQueueLength()>0)
			pS.setText(transiciones.get(9).getPrimero());
		
		if(transicion == 11 && pN.getText().equals("") && transiciones.get(11).getQueueLength()>0)
			pN.setText(transiciones.get(11).getPrimero());
		
	}

	private void writeView(Integer transicion, String id) {
		//Resalto con color antes de mover
		switch(transicion){
			case 0: pC.setForeground(Color.RED); break;
			case 1: pD.setForeground(Color.RED); break;
			case 2: pA.setForeground(Color.RED); break;
			case 3: pB.setForeground(Color.RED); break;
			case 4: pO.setForeground(Color.RED); break;
			case 5: pA.setForeground(Color.RED); break;
			case 6: pE.setForeground(Color.RED); break;
			case 7: pC.setForeground(Color.RED); break;
			case 8: pD.setForeground(Color.RED); break;
			case 9: pS.setForeground(Color.RED); break;
			case 10: pB.setForeground(Color.RED); break;
			case 11: pN.setForeground(Color.RED); break;
		}
		

		//Espero un tiempo para que sea visible
		try{Thread.sleep(tiempo);}catch(Exception e){e.printStackTrace();}
		
		//Quito el resaltado
		pA.setForeground(Color.BLACK);
		pB.setForeground(Color.BLACK);
		pC.setForeground(Color.BLACK);
		pD.setForeground(Color.BLACK);
		pN.setForeground(Color.BLACK);
		pS.setForeground(Color.BLACK);
		pE.setForeground(Color.BLACK);
		pO.setForeground(Color.BLACK);
		psN.setForeground(Color.BLACK);
		psS.setForeground(Color.BLACK);
		psE.setForeground(Color.BLACK);
		psO.setForeground(Color.BLACK);
		
		//Seteo texto del siguiente y borro/cambio el anterior
		//Resalto con color el texto en la nueva posicion
		switch(transicion){
			case 0: pD.setText(id); pD.setForeground(Color.RED); pC.setText(""); break;
			case 1: pA.setText(id); pA.setForeground(Color.RED); pD.setText(""); break;
			case 2: pB.setText(id); pB.setForeground(Color.RED); pA.setText(""); break;
			case 3: pC.setText(id); pC.setForeground(Color.RED); pB.setText(""); break;
			case 4: pD.setText(id); pD.setForeground(Color.RED); 
					if(transiciones.get(transicion).getQueueLength()>0)
							pO.setText(transiciones.get(transicion).getPrimero());
					else
							pO.setText("");
					break;
			case 5: psE.setText(id); psE.setForeground(Color.RED);  pA.setText(""); break;
			case 6: pB.setText(id); pB.setForeground(Color.RED);
					if(transiciones.get(transicion).getQueueLength()>0)
							pE.setText(transiciones.get(transicion).getPrimero());
					else
							pE.setText("");
					break;
			case 7: psO.setText(id); psO.setForeground(Color.RED); pC.setText(""); break;
			case 8: psS.setText(id); psS.setForeground(Color.RED); pD.setText(""); break;
			case 9: pA.setText(id); pA.setForeground(Color.RED);
					if(transiciones.get(transicion).getQueueLength()>0)
						pS.setText(transiciones.get(transicion).getPrimero());
					else
						pS.setText("");
					break;
			case 10: psN.setText(id); psN.setForeground(Color.RED); pB.setText(""); break;
			case 11: pC.setText(id); pC.setForeground(Color.RED);
					if(transiciones.get(transicion).getQueueLength()>0)
						pN.setText(transiciones.get(transicion).getPrimero());
					else
						pN.setText("");
					break;
		}

		//Espero un tiempo breve con el destino resaltado
		try{Thread.sleep(tiempo);}catch(Exception e){e.printStackTrace();}
		
		//Quito el resaltado
		pA.setForeground(Color.BLACK);
		pB.setForeground(Color.BLACK);
		pC.setForeground(Color.BLACK);
		pD.setForeground(Color.BLACK);
		pN.setForeground(Color.BLACK);
		pS.setForeground(Color.BLACK);
		pE.setForeground(Color.BLACK);
		pO.setForeground(Color.BLACK);
		psN.setForeground(Color.BLACK);
		psS.setForeground(Color.BLACK);
		psE.setForeground(Color.BLACK);
		psO.setForeground(Color.BLACK);

		fin.setText("Autos que finalizaron: "+outCars);
		
	}
	
	public void setTiempos(int t){
		tiempo = t;
	}
	
	public void aumentarVelocidad(){
		tiempo += 50;
	}
	
	public void reducirVelocidad(){
		if(tiempo >= 100)
			tiempo -=  50;
	}
	
	public int getTiempo(){
		return tiempo;
	}
}