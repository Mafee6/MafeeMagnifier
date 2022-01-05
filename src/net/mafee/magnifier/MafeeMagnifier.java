/*
   Copyright [202] [Mafee7]

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

package net.mafee.magnifier;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Robot;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

@SuppressWarnings("unused")
public class MafeeMagnifier {

	static JFrame window;
	static JLabel imglbl;
	static Robot robot;
	static Graphics2D g2d;
	static String config;
	static JFrame controlwindow;
	 
	public static void main(String[] args) {
		
		Image img;
		config = "";
		
		window = new JFrame("Mafee Magnifier");
		controlwindow = new JFrame("Mafee Magnifier - Controls");
		imglbl = new JLabel();
				
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
		
		window.getContentPane().addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent ev) {
				if(ev.getButton() == MouseEvent.BUTTON3) {
					
				}
			}
		});
		
		window.add(imglbl);
		
		window.setLayout(null);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setLocation(10, 10);
		window.setSize(300, 300);
		window.setUndecorated(true);
		window.setShape(new RoundRectangle2D.Float(0, 0, window.getWidth(), window.getHeight(), 10, 10));
		window.setIconImage(new ImageIcon("MafeeMagnifier.png").getImage());
		window.setVisible(true);
		window.setAlwaysOnTop(true);
		window.repaint();
		
		controlwindow.setLayout(null);
		controlwindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		controlwindow.setLocation(10, 10);
		controlwindow.setSize(40, 100);
		controlwindow.setUndecorated(true);
		controlwindow.setShape(new RoundRectangle2D.Float(0, 0, controlwindow.getWidth(), controlwindow.getHeight(), 5, 5));
		controlwindow.setIconImage(new ImageIcon("MafeeMagnifier.png").getImage());
		controlwindow.setVisible(true);
		controlwindow.setAlwaysOnTop(true);
		controlwindow.getContentPane().setBackground(new Color(34, 34, 34));
		controlwindow.repaint();
		
		JButton closebtn = new JButton();
		closebtn.setBackground(Color.red);
		closebtn.setBounds(2, 2, controlwindow.getWidth() - 5, 30);
		closebtn.setForeground(Color.black);
		closebtn.setFocusPainted(false);
		closebtn.setBorderPainted(false);
		closebtn.setIcon(new ImageIcon(new ImageIcon("CloseIcon.png").getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		closebtn.addActionListener(ev -> System.exit(0));
		controlwindow.add(closebtn);
		controlwindow.setLocation(window.getX() + window.getWidth() + 10, window.getY());
		
		imglbl.setBounds(0, 0, window.getWidth(), window.getHeight());
			
		Rectangle rect = new Rectangle();
		
		BufferedReader configbr;
		
		boolean invis = false;
		
		try {
			configbr = new BufferedReader(new FileReader(new File("config.txt")));
			configbr.lines().forEach(line -> {
				config += line.toString() + "\n";
			});
			
			try {
				configbr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		System.out.println("Config:\n" + config);
		
		int mouseX;
		int mouseY;
		
		new Thread() {
			public void run() {
				
				boolean invis = false;
				
				while(true) {
					
					
				if(config.contains("HideOnMouseOver:true")) {
					if(
						(int) MouseInfo.getPointerInfo().getLocation().getX() < window.getX() + window.getWidth() &&
						(int) MouseInfo.getPointerInfo().getLocation().getY() < window.getY() + window.getHeight()
					) { invis = true; } else { invis = false; }
					
					if(invis) {
						window.setState(JFrame.ICONIFIED);
					} else {
						window.setState(JFrame.NORMAL);
					}
				}
				}
			}
		}.start();
		
		while(true) {
			
			mouseX = (int) MouseInfo.getPointerInfo().getLocation().getX();
			mouseY = (int) MouseInfo.getPointerInfo().getLocation().getY();
			
			if(imglbl.isVisible()) {
				rect.setBounds(
					(int) mouseX - 100,
					(int) mouseY - 100,
					200, 200
				);
				
				img = (Image) robot.createMultiResolutionScreenCapture(rect);
				
				g2d = (Graphics2D) imglbl.getGraphics();
				
				if(config.contains("UseEnhancementSettings:true")) {
				if(config.contains("Antialiasing:true")) { 
					g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				}
				
				if(config.contains("Interpolation:true")) {
					g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
				}
				}
				
				g2d.setPaint(Color.gray);
				g2d.fillRect(0, 0, imglbl.getWidth(), imglbl.getHeight());
				g2d.drawImage(img, 2, 2, imglbl.getWidth() - 4, imglbl.getHeight() - 4, null);
			}
		}
		
	}
}
