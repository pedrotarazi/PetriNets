import java.util.ArrayList;
import java.util.HashMap;

public class Semaforo {
	protected int contador = 0;
	private boolean condSalida;
	private ArrayList<Thread> bloqueados;
	private long timeStart, timeFinish, timeVerif;
	private HashMap<Thread, Long> autos_time;
	private String nombre, autoMaxTime;
	private long maximoTime=0;
	public Semaforo(int valorInicial, boolean estadoInicial, int numero){
		contador = valorInicial;
		bloqueados = new ArrayList<Thread>(); //Bloqueados;
		autos_time = new HashMap<Thread, Long>();
		nombre = "T"+numero;
		if(numero>12){nombre = "ColaEntrada";}
	}
	
	synchronized public void acquireUninterruptibly(){		
		while (contador == 0)
			try{
				bloqueados.add(Thread.currentThread());
				autos_time.put(Thread.currentThread(), System.currentTimeMillis());
				do{
					timeStart = System.currentTimeMillis();
					wait(); /* La condicion de salida es que este Thread sea el que
					 		mas tiempo lleva esperando en el semaforo */
					long actualTime = (System.currentTimeMillis() - autos_time.get(Thread.currentThread()));
					if( actualTime > maximoTime ){
						maximoTime = actualTime; 
						autoMaxTime = Thread.currentThread().getName();
					}
					//Al despertar continua desde aqui
					condSalida= bloqueados.get(0).equals(Thread.currentThread());
					/* Si este Thread no puede despertarse, se despierta a otro */
					if(!condSalida) notify();
				}while(!condSalida);
				condSalida=false;
				long tiempo = timeFinish-autos_time.get(Thread.currentThread());
				bloqueados.remove(Thread.currentThread());
				autos_time.remove(Thread.currentThread());
			}
			catch (Exception e) {
				e.printStackTrace();
				System.out.println("ERROR en Semaforo "+nombre);}
		contador--;
	}
	
	synchronized public void release(){
		contador = 1;
		timeFinish = System.currentTimeMillis();
		notify(); //Aqui con despertar a uno es suficiente, pues todos esperan
					// por la misma condicion
	}
	
	public String getBloqueados(){
		String s="";
		for(int i=0;i<bloqueados.size();i++){
			s+=bloqueados.get(i).getName()+" ";
		}
		return s;
	}
	
	public long maxTimeBloqueado(){
		//Collection<Long> tiempos = new List<Long>;
		//tiempos = autos_time.values();
		long max=-1;
		for(Thread t : autos_time.keySet()){
			if(autos_time.get(t)>max){
				max = autos_time.get(t);
			}
		}
		return max;
	}
	
	public long getMaxTime(){
		return maximoTime;
	}
	
	public String getNameMaxTime(){
		return autoMaxTime;
	}
	
	public String getPrimero(){
		return bloqueados.get(0).getName();
	}
	
	public int getContador(){
		return contador;
	}
	
	public int getQueueLength(){
		return bloqueados.size();
	}
	
	public void getTimeStart(){
		System.out.println("TimeStart: "+timeStart);
	}
	
	public void getTimeFinish(){
		System.out.println("TimeFinish: "+timeFinish);
	}
	
	public long getTime(){
		return timeFinish - timeStart;
	}
	
	public String getNombre(){
		return nombre;
	}

	public long getTimeVerif(){
		timeVerif = System.currentTimeMillis() - autos_time.get(bloqueados.get(0));
		return  timeVerif;	
	}
}
	