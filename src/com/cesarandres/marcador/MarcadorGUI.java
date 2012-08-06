package com.cesarandres.marcador;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class MarcadorGUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtbox_logo;
	private JTextField txtbox_objetivo;
	private JButton btnEligeUnLogo;
	private JButton btnEligeUnObjetivo;
	private JCheckBox chckbox_repeticion;
	private JFileChooser fc;
	private JButton btnAplicar;
	private JSlider slider;
	private JComboBox<String> comboBox;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MarcadorGUI frame = new MarcadorGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MarcadorGUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 496, 166);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		txtbox_logo = new JTextField();
		txtbox_logo.setBounds(66, 11, 283, 20);
		contentPane.add(txtbox_logo);
		txtbox_logo.setColumns(10);

		txtbox_objetivo = new JTextField();
		txtbox_objetivo.setBounds(66, 42, 283, 20);
		contentPane.add(txtbox_objetivo);
		txtbox_objetivo.setColumns(10);

		btnEligeUnLogo = new JButton("Elige un logo");
		btnEligeUnLogo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

				if (e.getSource() == btnEligeUnLogo) {
					int returnVal = fc.showOpenDialog(MarcadorGUI.this);
					File file = null;
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						file = fc.getSelectedFile();
						txtbox_logo.setText(file.getAbsolutePath());

					} else {
					}
				}
			}
		});
		btnEligeUnLogo.setBounds(359, 10, 111, 23);
		contentPane.add(btnEligeUnLogo);

		btnEligeUnObjetivo = new JButton("Elige un objetivo");
		btnEligeUnObjetivo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

				if (e.getSource() == btnEligeUnObjetivo) {
					int returnVal = fc.showOpenDialog(MarcadorGUI.this);
					File file = null;
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						file = fc.getSelectedFile();
						txtbox_objetivo.setText(file.getAbsolutePath());
					} else {

					}
				}
			}
		});
		btnEligeUnObjetivo.setBounds(359, 41, 111, 23);

		contentPane.add(btnEligeUnObjetivo);

		btnAplicar = new JButton("Aplicar");
		btnAplicar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File logo = new File(txtbox_logo.getText());
				File objetivo = new File(txtbox_objetivo.getText());
				btnAplicar.setEnabled(false);
				Procesar(logo, objetivo);
				btnAplicar.setEnabled(true);
			}
		});
		btnAplicar.setBounds(381, 75, 89, 23);
		contentPane.add(btnAplicar);

		JLabel lblLogo = new JLabel("Logo:");
		lblLogo.setBounds(10, 14, 46, 14);
		contentPane.add(lblLogo);

		JLabel logo_objetivo = new JLabel("Objetivo:");
		logo_objetivo.setBounds(10, 45, 46, 14);
		contentPane.add(logo_objetivo);

		chckbox_repeticion = new JCheckBox("Repetir");
		chckbox_repeticion.setBounds(10, 69, 68, 23);
		contentPane.add(chckbox_repeticion);

		slider = new JSlider();
		slider.setBounds(149, 73, 200, 23);
		contentPane.add(slider);

		JLabel lblTransparencia = new JLabel("Opacidad:");
		lblTransparencia.setBounds(84, 73, 65, 14);
		contentPane.add(lblTransparencia);

		JLabel lblPosicion = new JLabel("Posicion:");
		lblPosicion.setBounds(10, 99, 68, 14);
		contentPane.add(lblPosicion);

		comboBox = new JComboBox<String>();
		comboBox.setModel(new DefaultComboBoxModel<String>(new String[] {
				"centro", "superior", "inferior", "derecha", "izquierda",
				"superior-derecha", "superior-izquierda", "inferior-derecha",
				"inferior-izquierda" }));
		comboBox.setSelectedIndex(0);
		comboBox.setBounds(66, 96, 103, 20);
		contentPane.add(comboBox);
	}

	private void Procesar(File logo, File objetivo) {
		if (objetivo.isDirectory()) {
			File[] fileList = objetivo.listFiles();
			for (int i = 0; i < fileList.length; i++) {
				if (fileList[i].isDirectory()
						|| fileList[i].getName().endsWith(".jpg")
						|| fileList[i].getName().endsWith(".jpeg")
						|| fileList[i].getName().endsWith(".png")) {
					Procesar(logo, fileList[i]);
				}
			}
		} else {

			try {
				String identCommand = "identify \""
						+ objetivo.getAbsolutePath() + "\"";
				String line = Comando(identCommand);
				line = line.replace(objetivo.getAbsolutePath(), "");
				boolean primer_div = false;
				boolean segundo_div = false;
				boolean tercer_div = false;
				int primer_index = 0;
				int segundo_index = 0;

				for (int i = 0; i < line.length(); i++) {
					char letra = line.charAt(i);
					if (letra == ' ') {
						if (primer_div == false) {
							primer_div = true;

						} else if (segundo_div == false) {
							segundo_div = true;
							primer_index = i + 1;
						} else if (tercer_div == false) {
							tercer_div = true;
							segundo_index = i;
						}
						if (primer_div && segundo_div && tercer_div) {
							break;
						}
					}
				}

				String size = line.substring(primer_index, segundo_index);

				int width = Integer.parseInt(size.substring(0,
						size.indexOf('x')));
				int height = Integer.parseInt(size.substring(
						size.indexOf('x') + 1, size.length()));

				String directorio = objetivo.getParentFile().getPath();

				String commandResize = "convertIM \"" + logo.getAbsolutePath()
						+ "\" -verbose -resize " + width + "x" + height + " \""
						+ directorio + File.separatorChar + ".logoresized \"";
				line = Comando(commandResize);

				File reLogo = new File(directorio + File.separatorChar
						+ ".logoresized");

				String tile = "";
				if (chckbox_repeticion.isSelected()) {
					tile = "-tile";
				}

				int disolve = slider.getValue() / 2 + 25;

				int index = comboBox.getSelectedIndex();
				String gravity = "center";
				/*
				 * "centro", "superior" "inferior" "derecha", "izquierda",
				 * "superior-derecha", "superior-izquierda", "inferior-derecha",
				 * "inferior-izquierda"
				 */
				switch (index) {
				case 0:
					gravity = "Center";
					break;
				case 1:
					gravity = "North";
					break;
				case 2:
					gravity = "South";
					break;
				case 3:
					gravity = "East";
					break;
				case 4:
					gravity = "West";
					break;
				case 5:
					gravity = "NorthEast";
					break;
				case 6:
					gravity = "NorthWest";
					break;
				case 7:
					gravity = "SouthEast";
					break;
				case 8:
					gravity = "SouthWest";
					break;
				}

				String commandCompose = "composite " + tile + " -dissolve "
						+ disolve + " -gravity " + gravity + " \""
						+ reLogo.getAbsolutePath() + "\" \""
						+ objetivo.getAbsolutePath() + "\" \"" + directorio
						+ File.separatorChar + objetivo.getName() + "\"";
				line = Comando(commandCompose);

				reLogo.delete();

			} catch (Exception e) {
				System.out.println(e.toString());
				e.printStackTrace();
			}

		}
	}

	private String Comando(String comando) {

		System.out.println(comando);
		Process pr = null;
		try {
			pr = Runtime.getRuntime().exec(comando);
		} catch (IOException e) {
			e.printStackTrace();
		}

		BufferedReader input = new BufferedReader(new InputStreamReader(
				pr.getInputStream()));
		StringBuffer buffer = new StringBuffer(100);

		BufferedReader error = new BufferedReader(new InputStreamReader(
				pr.getErrorStream()));

		String line = null;

		try {
			while ((line = input.readLine()) != null) {
				buffer.append(line);
			}
			while ((line = error.readLine()) != null) {
				System.out.print(line);
			}
			System.out.println();
		} catch (IOException e) {
			e.printStackTrace();
		}
		line = buffer.toString();
		try {
			pr.waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return line;
	}
}
