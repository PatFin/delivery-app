package com.hexagone.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;

import org.jcp.xml.dsig.internal.MacOutputStream;

import com.hexagone.delivery.models.DeliveryQuery;
import com.hexagone.delivery.models.Map;
import com.hexagone.delivery.xml.XMLDeserialiser;
import com.hexagone.delivery.xml.XMLException;

public class MainFrame extends JFrame {

	final private JPanel all;
	private JPanel mapPanel;
	private JPanel deliveryPanel;
	private JPanel header;
	private JPanel detailPanel;
	private JPanel tourPanel;
	// a panel with the map, a scroll bar, a search bar and a zoom button
	private JPanel mainPanel;
	private static Map map;
	private static int coefficient=2;
	private DeliveryQuery deliveryQuery;
	private Point p;

	public MainFrame() throws XMLException {
		super();

		// MouseListener for point delivery details
		final MouseListener details = new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {
				p = e.getPoint();
				Boolean b = MapFrame.checkPoint(p);
				if (b) {
					if (detailPanel != null) {
						all.remove(detailPanel);
					}
					detailPanel = new DetailsPanel(map, deliveryQuery, p, coefficient);
					all.add(detailPanel, BorderLayout.EAST);
					all.validate();
					all.repaint();
					validate();
					repaint();
				}

			}

			@Override
			public void mouseExited(MouseEvent e) {}

			@Override
			public void mouseEntered(MouseEvent e) {}

			@Override
			public void mouseClicked(MouseEvent e) {}
		};

		// Listener for "Charger Plan" Button
		ActionListener uploadMap = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				map = new Map();
				try {
					map = XMLDeserialiser.loadMap();
					mapPanel = new MapFrame(map, null,false,coefficient);
					mainPanel.add(mapPanel, BorderLayout.CENTER);
					mainPanel.validate();
					mainPanel.repaint();
					all.validate();
					all.repaint();
				} catch (XMLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		};

		// Listener for "Charger Livraison" Button
		ActionListener uploadDelivery = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				deliveryQuery = new DeliveryQuery();
				try {
					deliveryQuery = XMLDeserialiser.loadDeliveryQuery();
					deliveryPanel = new MapFrame(map, deliveryQuery,false,coefficient);
					deliveryPanel.repaint();
					deliveryPanel.addMouseListener(details);
					mainPanel.remove(mapPanel);
					mainPanel.add(deliveryPanel, BorderLayout.CENTER);
					mainPanel.validate();
					mainPanel.repaint();
					all.validate();
					all.repaint();
				} catch (XMLException e1) {
					e1.printStackTrace();
				}

			}
		};
		
		//Listener for calcuateTour button
		ActionListener calculateTourListener =new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				tourPanel = new MapFrame(map, deliveryQuery,true,coefficient);
				tourPanel.repaint();
				tourPanel.addMouseListener(details);
				all.remove(deliveryPanel);
				all.add(tourPanel, BorderLayout.CENTER);
				all.validate();
				all.repaint();
			}
		};
		
	

		this.setTitle("Delivery App");
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize(screenSize.width, screenSize.height);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setLocationRelativeTo(null);

		all = new JPanel();
		all.setLayout(new BorderLayout());
		all.setBackground(Color.WHITE);

		//header components 
		header = new JPanel();
		header.setLayout(new GridLayout(1, 4));
		JButton loadMap = new JButton("Charger Plan");
		loadMap.addActionListener(uploadMap);
		header.add(loadMap);
		JButton loadDelivery = new JButton("Charger Livraison");
		loadDelivery.addActionListener(uploadDelivery);
		header.add(loadDelivery);
		JButton calculateTour = new JButton("Calculer Tournée");
		calculateTour.addActionListener(calculateTourListener);
		header.add(calculateTour);
		
		
		//mainPanel components
		mainPanel=new JPanel();
		mainPanel.setBackground(Color.WHITE);
		mainPanel.setLayout(new BorderLayout());
		//JScrollBar vbar=new JScrollBar(JScrollBar.VERTICAL, 30, 20, 0, 300);
		//vbar.setUnitIncrement(2);
        //vbar.setBlockIncrement(1);
        //mainPanel.add(vbar, BorderLayout.EAST);
		
		all.add(mainPanel);
		all.add(header, BorderLayout.NORTH);
		

		this.setContentPane(all);
	}
	

}
