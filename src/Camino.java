import java.util.ArrayList;

public class Camino {

	private int cantidad_caminos = 12;
	private ArrayList<Integer> camino1 = new ArrayList<Integer>();
	private ArrayList<Integer> camino2 = new ArrayList<Integer>();
	private ArrayList<Integer> camino3 = new ArrayList<Integer>();
	private ArrayList<Integer> camino4 = new ArrayList<Integer>();
	private ArrayList<Integer> camino5 = new ArrayList<Integer>();
	private ArrayList<Integer> camino6 = new ArrayList<Integer>();
	private ArrayList<Integer> camino7 = new ArrayList<Integer>();
	private ArrayList<Integer> camino8 = new ArrayList<Integer>();
	private ArrayList<Integer> camino9 = new ArrayList<Integer>();
	private ArrayList<Integer> camino10 = new ArrayList<Integer>();
	private ArrayList<Integer> camino11 = new ArrayList<Integer>();
	private ArrayList<Integer> camino12 = new ArrayList<Integer>();
	
	private ArrayList<ArrayList<Integer>> caminos = new ArrayList<ArrayList<Integer>>();
	
	public Camino(){
		
		//Se crean las 12 combinaciones de caminos posibles
		camino1.add(9);camino1.add(5);
		camino2.add(9);camino2.add(2);camino2.add(10);
		camino3.add(9);camino3.add(2);camino3.add(3);camino3.add(7);
		
		camino4.add(6);camino4.add(10);
		camino5.add(6);camino5.add(3);camino5.add(7);
		camino6.add(6);camino6.add(3);camino6.add(0);camino6.add(8);
		
		camino7.add(11);camino7.add(7);
		camino8.add(11);camino8.add(0);camino8.add(8);
		camino9.add(11);camino9.add(0);camino9.add(1);camino9.add(5);
		
		camino10.add(4);camino10.add(8);
		camino11.add(4);camino11.add(1);camino11.add(5);
		camino12.add(4);camino12.add(1);camino12.add(2);camino12.add(10);
		
		//Se agrega los caminos a una lista
		caminos.add(camino1);caminos.add(camino2);caminos.add(camino3);caminos.add(camino4);
		caminos.add(camino5);caminos.add(camino6);caminos.add(camino7);caminos.add(camino8);
		caminos.add(camino9);caminos.add(camino10);caminos.add(camino11);caminos.add(camino12);
	}
	
	public ArrayList<Integer> getCamino(int nroCamino){
		return caminos.get(nroCamino);
	}
	
	public int getCantCaminos(){
		return cantidad_caminos;
	}

	public ArrayList<Integer> getCaminoAleatorio(){
		int aleatorio = (int) Math.floor(Math.random()*(getCantCaminos()));
		return getCamino(aleatorio);
	}
}
