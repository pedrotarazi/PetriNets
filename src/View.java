import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class View extends JFrame {

	private JPanel contentPane;
	private static JLabel A = new JLabel("");
	private static JLabel B = new JLabel("");
	private static JLabel C = new JLabel("");
	private static JLabel D = new JLabel("");
	private static JLabel N = new JLabel("");
	private static JLabel S = new JLabel("");
	private static JLabel E = new JLabel("");
	private static JLabel O = new JLabel("");
	private static JLabel sN = new JLabel("");
	private static JLabel sS = new JLabel("");
	private static JLabel sE = new JLabel("");
	private static JLabel sO = new JLabel("");
	private static JLabel finalizados = new JLabel("");
	private static JLabel creados = new JLabel("");
	private static int cantidad_autos;
	private static boolean finalizo = false;
	private static JButton bmas, bmenos;
	private static Monitor monitor;
	private final JLabel nombres = new JLabel("Aguilar, Mauricio - Tarazi, Pedro");
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					View frame = new View();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		cantidad_autos = 10;
		monitor = new Monitor(true);
		monitor.setView(A, B, C, D, N, S, E, O, sN, sS, sE, sO, finalizados);
		Camino caminos = new Camino();
		monitor.setCantAutos(cantidad_autos);
		monitor.setTiempos(500);
		for(int i=0; i<cantidad_autos; i++){
			String name = "A"+i;
			Auto a = new Auto(monitor, name, caminos.getCaminoAleatorio());
			Thread h = new Thread(a);
			h.setName(name);
			h.start();
		}
	}

	/**
	 * Create the frame.
	 */
	public View() {
		setTitle("Visor de Red de Petri Esquina");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 520, 472);
		contentPane = new JPanel();
		contentPane.setBackground(Color.GREEN);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		A.setOpaque(true);
		A.setFont(new Font("Tahoma", Font.BOLD, 16));
		A.setHorizontalAlignment(SwingConstants.CENTER);
		A.setBounds(278, 202, 90, 90);
		A.setBackground(new Color(255, 140, 0));
		getContentPane().add(A);
		
		B.setOpaque(true);
		B.setHorizontalAlignment(SwingConstants.CENTER);
		B.setFont(new Font("Tahoma", Font.BOLD, 16));
		B.setBounds(278, 112, 90, 90);
		B.setBackground(new Color(255, 140, 0));
		getContentPane().add(B);
		
		C.setBackground(new Color(255, 140, 0));
		C.setOpaque(true);
		C.setHorizontalAlignment(SwingConstants.CENTER);
		C.setFont(new Font("Tahoma", Font.BOLD, 16));
		C.setBounds(188, 112, 90, 90);
		C.setBackground(new Color(255, 140, 0));
		getContentPane().add(C);
		
		D.setOpaque(true);
		D.setHorizontalAlignment(SwingConstants.CENTER);
		D.setFont(new Font("Tahoma", Font.BOLD, 16));
		D.setBounds(188, 202, 90, 90);
		D.setBackground(new Color(255, 140, 0));
		getContentPane().add(D);
		
		O.setOpaque(true);
		O.setHorizontalAlignment(SwingConstants.CENTER);
		O.setFont(new Font("Tahoma", Font.BOLD, 16));
		O.setBounds(99, 202, 90, 90);
		getContentPane().add(O);
		
		S.setOpaque(true);
		S.setHorizontalAlignment(SwingConstants.CENTER);
		S.setFont(new Font("Tahoma", Font.BOLD, 16));
		S.setBounds(278, 292, 90, 90);
		getContentPane().add(S);
		
		E.setOpaque(true);
		E.setHorizontalAlignment(SwingConstants.CENTER);
		E.setFont(new Font("Tahoma", Font.BOLD, 16));
		E.setBounds(368, 112, 90, 90);
		getContentPane().add(E);
		
		N.setOpaque(true);
		N.setHorizontalAlignment(SwingConstants.CENTER);
		N.setFont(new Font("Tahoma", Font.BOLD, 16));
		N.setBounds(188, 22, 90, 90);
		getContentPane().add(N);
		
		sO.setOpaque(true);
		sO.setHorizontalAlignment(SwingConstants.CENTER);
		sO.setFont(new Font("Tahoma", Font.BOLD, 16));
		sO.setBackground(Color.ORANGE);
		sO.setBounds(99, 112, 90, 90);
		getContentPane().add(sO);
		
		sS.setOpaque(true);
		sS.setHorizontalAlignment(SwingConstants.CENTER);
		sS.setFont(new Font("Tahoma", Font.BOLD, 16));
		sS.setBackground(Color.ORANGE);
		sS.setBounds(188, 292, 90, 90);
		getContentPane().add(sS);
		
		sE.setOpaque(true);
		sE.setHorizontalAlignment(SwingConstants.CENTER);
		sE.setFont(new Font("Tahoma", Font.BOLD, 16));
		sE.setBackground(Color.ORANGE);
		sE.setBounds(368, 202, 90, 90);
		getContentPane().add(sE);
		
		sN.setOpaque(true);
		sN.setHorizontalAlignment(SwingConstants.CENTER);
		sN.setFont(new Font("Tahoma", Font.BOLD, 16));
		sN.setBackground(Color.ORANGE);
		sN.setBounds(278, 22, 90, 90);
		getContentPane().add(sN);
		
		finalizados.setOpaque(true);
		finalizados.setHorizontalTextPosition(SwingConstants.CENTER);
		finalizados.setFont(new Font("Tahoma", Font.BOLD, 12));
		finalizados.setBackground(Color.GREEN);
		finalizados.setBounds(16, 349, 173, 33);
		getContentPane().add(finalizados);
		finalizados.setText("Autos que finalizaron:");
		
		creados.setOpaque(true);
		creados.setHorizontalTextPosition(SwingConstants.CENTER);
		creados.setFont(new Font("Tahoma", Font.BOLD, 12));
		creados.setBackground(Color.GREEN);
		creados.setBounds(16, 22, 173, 33);
		getContentPane().add(creados);
		creados.setText("Autos ejecutados: "+cantidad_autos);
		
		bmas = new JButton("+");
		bmas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				bmenos.setEnabled(true);
				monitor.reducirVelocidad();
				if(monitor.getTiempo()==50)
					bmas.setEnabled(false);
			}
		});
		bmas.setBounds(411, 303, 49, 23);
		contentPane.add(bmas);
		
		bmenos = new JButton("-");
		bmenos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				bmas.setEnabled(true);
				monitor.aumentarVelocidad();
				if(monitor.getTiempo()==2000)
					bmenos.setEnabled(false);
			}
		});
		bmenos.setBounds(411, 337, 49, 23);
		contentPane.add(bmenos);
		
		JLabel lblVel = new JLabel("VEL");
		lblVel.setHorizontalAlignment(SwingConstants.CENTER);
		lblVel.setBounds(378, 303, 23, 56);
		contentPane.add(lblVel);
		nombres.setHorizontalAlignment(SwingConstants.RIGHT);
		nombres.setBounds(299, 419, 200, 14);
		
		contentPane.add(nombres);
	}
}
