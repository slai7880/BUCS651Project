import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JPanel;

public class MainPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private String petType;

	public MainPanel(String b) {
		setPetType(b);
	}

	@Override
	public void paint(final Graphics g) {
		super.paint(g);
		Toolkit tool = this.getToolkit();
		Image board = tool.getImage("pet"+petType+".jpg");
		g.drawImage(board, 0, 0, 270, 210, this);

	}

	public String getPetType() {
		return petType;
	}

	public void set(String s) {
		setPetType(s);
	}
	public void setPetType(String petType) {
		this.petType = petType;
	}


	}