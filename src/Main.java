import java.util.ArrayList;


public class Main {
	private static int cantidad_autos;
	private static int cantidad_caminos;
	private static ArrayList<Thread> autos = new ArrayList<Thread>();
	private static Monitor monitor = new Monitor();
	
	public static void main (String[] args) {
		ArrayList<Integer> camino1 = new ArrayList<Integer>();
		camino1.add(9);camino1.add(5);
		ArrayList<Integer> camino2 = new ArrayList<Integer>();
		camino2.add(9);camino2.add(2);camino2.add(10);
		ArrayList<Integer> camino3 = new ArrayList<Integer>();
		camino3.add(9);camino3.add(2);camino3.add(3);camino3.add(7);
		
		ArrayList<Integer> camino4 = new ArrayList<Integer>();
		camino4.add(6);camino4.add(10);
		ArrayList<Integer> camino5 = new ArrayList<Integer>();
		camino5.add(6);camino5.add(3);camino5.add(7);
		ArrayList<Integer> camino6 = new ArrayList<Integer>();
		camino6.add(6);camino6.add(3);camino6.add(0);camino6.add(8);
		
		ArrayList<Integer> camino7 = new ArrayList<Integer>();
		camino7.add(11);camino7.add(7);
		ArrayList<Integer> camino8 = new ArrayList<Integer>();
		camino8.add(11);camino8.add(0);camino8.add(8);
		ArrayList<Integer> camino9 = new ArrayList<Integer>();
		camino9.add(11);camino9.add(0);camino9.add(1);camino9.add(5);
		
		ArrayList<Integer> camino10 = new ArrayList<Integer>();
		camino10.add(4);camino10.add(8);
		ArrayList<Integer> camino11 = new ArrayList<Integer>();
		camino11.add(4);camino11.add(1);camino11.add(5);
		ArrayList<Integer> camino12 = new ArrayList<Integer>();
		camino12.add(4);camino12.add(1);camino12.add(2);camino12.add(10);
		
		ArrayList<ArrayList<Integer>> caminos = new ArrayList<ArrayList<Integer>>();
		caminos.add(camino1);caminos.add(camino2);caminos.add(camino3);caminos.add(camino4);
		caminos.add(camino5);caminos.add(camino6);caminos.add(camino7);caminos.add(camino8);
		caminos.add(camino9);caminos.add(camino10);caminos.add(camino11);caminos.add(camino12);
		
		cantidad_caminos = 12;
		cantidad_autos = 32;
		monitor.setCantAutos(cantidad_autos);
		RdP rdp = new RdP();
		rdp.getMarcado();
		System.out.print("\n");
		
		for(int i=0;i<cantidad_autos;i++){
			int aleatorio = (int) Math.floor(Math.random()*(cantidad_caminos));
			String name = "A"+i;
			Auto a = new Auto(monitor, name, caminos.get(aleatorio));
			Thread h = new Thread(a);
			h.setName(name);
			//a.getCamino();
			autos.add(h);
			h.start();
			try{Thread.currentThread().sleep(5);}
			catch (Exception e){}
		}
	}
	
	public int getCantAutos(){
		return cantidad_autos;
	}
	
	public ArrayList<Thread> getAutos(){
		return autos;
	}
	
	public Monitor getMonitor(){
		return monitor;
	}
}

