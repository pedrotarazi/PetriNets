import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;


public class Testing {
	//Monitor monitor = new Monitor();
	@Test
	/* TEST DE ENCOLADO
	 * Se crean 2 autos y ambos quieren disparar la misma transicion.
	 * El primero dispara correctamente y el segundo se encola en la transicion.
	 */
	public void test_Encolado() {
		Monitor monitor = new Monitor();
		monitor.setCantAutos(2);
		ArrayList<Integer> camino1 = new ArrayList<Integer>();
		camino1.add(9);
		
		Auto a1 = new Auto(monitor, "a1", camino1);
		Thread h1 = new Thread(a1);
		Auto a2 = new Auto(monitor, "a2", camino1);
		Thread h2 = new Thread(a2);
		
		h1.start();
		try{Thread.currentThread().sleep(50);}
		catch (Exception e){}
		h2.start();
		try{Thread.currentThread().sleep(50);}
		catch (Exception e){}
		
		assertFalse(h1.isAlive()); //El primer hilo ya deberia haber finalizado
		assertTrue(h2.isAlive());  //El segundo hilo no, ya que estaría esperando el recurso para disparar
								   // a T9
		assertEquals(1,monitor.getSemaforos().get(9).getQueueLength()); //Comprobamos que efectivamente el hilo esta bloqueado
	}
	
	@Test
	/*
	 * Este test prueba si la politica funciona correctamente. 
	 * Definimos un vectorAND (este nos indica que transiciones que tienen autos bloqueados..
	 * ... en la cola pueden dispararse) y nos debe devolver la transicion que se debe disparar
	 * segun la politica de prioridades que se indica aqui:
	 * 		OrdenPrioridadDefault = t5, t10, t7, t8, t2, t3, t0, t1, t9, t6, t11, t4
	 */
	public void test_MetodoPolitica() {
		Politica pol = new Politica(12);
		//Crear Semaforos
		ArrayList<Semaforo> transiciones = new ArrayList<Semaforo>();
		for(int j=0;j<12;j++)
			transiciones.add(new Semaforo(0,false,j)); // 0 semaforo en rojo. 1 semaforo en verde		
		//Crear Vector AND         (0,1,2,3,4,5,6,7,8,9,10,11)
		int[] vectorAND = new int[]{1,0,0,0,1,0,0,0,0,0, 0, 0};
		assertEquals(4, pol.calcularPolitica(vectorAND, transiciones));
	}

	@Test
	/*
	 * PRUEBA DEL FUNCIONAMIENTO DE TODO EL MONITOR.
	 * Aca se prueba que todos los autos que se ejecutan, llegan a su fin. 
	 */
	public void test_Monitor_Tiempos(){
		Main main = new Main();
		main.main(null);
		int cantidad_autos = main.getCantAutos();
		ArrayList<Thread> autos = main.getAutos();
		
		try{Thread.currentThread().sleep(20*cantidad_autos);}
			catch (Exception e){}
		for(int i=0;i<cantidad_autos;i++){
			assertFalse(autos.get(i).isAlive());
		}
		long max = main.getMonitor().getTimeMax();
		assertTrue(max<(cantidad_autos*6));
	}
	
	@Test
	public void test_Monitor_Politica(){
		Monitor monitor = new Monitor();
		monitor.setCantAutos(4);
		
		ArrayList<Integer> camino1 = new ArrayList<Integer>();
		camino1.add(11);camino1.add(0);camino1.add(8);
		ArrayList<Integer> camino2 = new ArrayList<Integer>();
		camino2.add(4);camino2.add(1);camino2.add(5);
		
		Auto a1 = new Auto(monitor, "a1", camino1);
		Thread h1 = new Thread(a1);
		h1.setName("a1");
		Auto a2 = new Auto(monitor, "a2", camino1);
		Thread h2 = new Thread(a2);
		h2.setName("a2");
		Auto a3 = new Auto(monitor, "a3", camino2);
		Thread h3 = new Thread(a3);
		h3.setName("a3");
		Auto a4 = new Auto(monitor, "a4", camino2);
		Thread h4 = new Thread(a4);
		h4.setName("a4");
		
		h1.start();
		try{Thread.currentThread().sleep(10);}
		catch (Exception e){}
		h2.start();
		try{Thread.currentThread().sleep(10);}
		catch (Exception e){}
		h3.start();
		try{Thread.currentThread().sleep(10);}
		catch (Exception e){}
		h4.start();
		try{Thread.currentThread().sleep(100);}
		catch (Exception e){}
	}
	
	@Test
	/*
	 * Prueba que no hay nunca mas de un auto por plaza
	 */
	public void test_Monitor_AutosEnPlazas(){
		Monitor monitor = new Monitor();
		monitor.setCantAutos(8);
		ArrayList<Integer> camino1 = new ArrayList<Integer>();
		camino1.add(9);
		ArrayList<Integer> camino2 = new ArrayList<Integer>();
		camino2.add(6);
		ArrayList<Integer> camino3 = new ArrayList<Integer>();
		camino3.add(11);
		ArrayList<Integer> camino4 = new ArrayList<Integer>();
		camino4.add(4);
		
		Auto a1 = new Auto(monitor, "a1", camino1);
		Thread h1 = new Thread(a1);
		Auto a2 = new Auto(monitor, "a2", camino2);
		Thread h2 = new Thread(a2);
		Auto a3 = new Auto(monitor, "a3", camino3);
		Thread h3 = new Thread(a3);
		Auto a4 = new Auto(monitor, "a4", camino4);
		Thread h4 = new Thread(a4);
		Auto a5 = new Auto(monitor, "a5", camino1);
		Thread h5 = new Thread(a5);
		Auto a6 = new Auto(monitor, "a6", camino2);
		Thread h6 = new Thread(a6);
		Auto a7 = new Auto(monitor, "a7", camino3);
		Thread h7 = new Thread(a7);
		Auto a8 = new Auto(monitor, "a8", camino4);
		Thread h8 = new Thread(a8);
		
		h1.start();
		try{Thread.currentThread().sleep(10);}
		catch (Exception e){}
		h2.start();
		try{Thread.currentThread().sleep(10);}
		catch (Exception e){}
		h3.start();
		try{Thread.currentThread().sleep(10);}
		catch (Exception e){}
		h4.start();
		try{Thread.currentThread().sleep(10);}
		catch (Exception e){}
		h5.start();
		try{Thread.currentThread().sleep(10);}
		catch (Exception e){}
		h6.start();
		try{Thread.currentThread().sleep(10);}
		catch (Exception e){}
		h7.start();
		try{Thread.currentThread().sleep(10);}
		catch (Exception e){}
		h8.start();
		
		try{Thread.currentThread().sleep(200);}
		catch (Exception e){}
		System.out.println("TRA : 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11");
		monitor.mostrarAutosEnColas();
	}
}
