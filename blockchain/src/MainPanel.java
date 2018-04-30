import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;


import javax.swing.JPanel;


public class MainPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private int petType;

	public MainPanel(int b) {
		setPetType(b);
	}

	@Override
	public void paint(final Graphics g) {
		super.paint(g);
		Toolkit tool = this.getToolkit();
		Image board = tool.getImage("pet"+petType+".jpg");
		g.drawImage(board, 0, 0, 270, 210, this);

	}

	public int getPetType() {
		return petType;
	}

	public void setPetType(int petType) {
		this.petType = petType;
	}


	}