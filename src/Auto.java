import java.util.ArrayList;

public class Auto implements Runnable{
	private ArrayList<Integer> vector_disparo = new ArrayList<Integer>();
	private Monitor esq = new Monitor();
	private String id;
	private int contador=0;
	private boolean termino=false;
	public Auto(Monitor esq, String id, ArrayList<Integer> vector){
		this.esq = esq;
		this.id = id;
		vector_disparo = vector;
	} 

	public void run() {
		int disparo_i = 0;
		while(disparo_i<vector_disparo.size()){	
			try{
				esq.disparar(vector_disparo.get(disparo_i++),id);	
				Thread.sleep(10);}
			catch(Exception e){e.printStackTrace();
			}	
		}
	}
	
	public void getCamino(){
		System.out.print(" Camino "+id+": ");
		for(int i=0;i<vector_disparo.size();i++){
			System.out.print(" "+vector_disparo.get(i));
		}
		System.out.print("\n");
	}
	
	public String getAuto(){
		return id;
	}

}
