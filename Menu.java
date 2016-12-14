import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
public class Menu extends Level {

	private Font font;
	private AudioPlayer menuMusic;
	private boolean isMusic;
	private AudioPlayer blip;

	public Menu(GameState state){
		super(state, null);
		Vertex[] items = new MenuVertex[3];
		items[0] = new MenuVertex((Game.WIDTH/4)-MenuVertex.DIAMETER/2, (Game.HEIGHT*3/4)-MenuVertex.DIAMETER/2, "Import");		// TODO We need to find a way to make these points equidistant from the edges, same as the...
		items[1] = new MenuVertex((Game.WIDTH*3/4)-MenuVertex.DIAMETER/2, (Game.HEIGHT*3/4)-MenuVertex.DIAMETER/2, "Generate");        // TODO ...pause menu issue, where the vertices are displayed evenly in the frame. (ratio issue when dividing double/fraction)
		items[2] = new MenuVertex((Game.WIDTH/2)-MenuVertex.DIAMETER/2, (Game.HEIGHT /4)-MenuVertex.DIAMETER/2, "CircleGraph");
		super.setGraph(new Graph(items, new int[][]{ new int[]{1, 2}, new int[0], new int[]{1}}));
		font = new Font("Main Menu Font", Font.BOLD, 20);
		isMusic = false;
		blip = new AudioPlayer("/resources/SFX/blip 1.wav");
		menuMusic = new AudioPlayer("/resources/Music/bgMusic1.wav");
		menuMusic.play();

	}
	// TODO: drawing the image for the menu
    public void draw(Graphics2D g) {
		g.setFont(font);
		graph.draw(g);

	}

    public void tick(){	}

    public void mousePressed(MouseEvent e){
    	super.mousePressed(e);
		if(e.getButton() == MouseEvent.BUTTON1 && clickedVertex != -1){
    			//insert here what the menu items have to do
    			//start a level
			blip.play();
			if(clickedVertex==0){
				JFileChooser fileChooser = new JFileChooser();
				int hasFile = fileChooser.showDialog(state.game, "Choose graph file");
				if (hasFile == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					int mode = getMode();
					System.out.println("mode is: "+mode);
					state.states[GameState.INGAME] = new PlaygroundLevel(state, GraphUtil.readGraphFromFile(file)); //Start the actual game. HOW to implement feedback: what happens if we cannot compute the chromatic number, or the file was corrupt??
					state.setState(GameState.INGAME);
				}
//				String input = JOptionPane.showInputDialog("Please give the directory to your graph file, make use of \\"+"\\ instead of \\. ");//needs to be fixed
//				String[] args = {input};
			}
			else if(clickedVertex == 1){
				Boolean falseVertexEdgeComb=true;
				int n = 0;
				int m = 0;
				while (falseVertexEdgeComb){
					JTextField vertexField = new JTextField(5);
					JTextField edgeField = new JTextField(5);

					JPanel myPanel = new JPanel();
					myPanel.add(new JLabel("vertices:"));
					myPanel.add(vertexField);
					myPanel.add(Box.createHorizontalStrut(15)); // a spacer
					myPanel.add(new JLabel("edges:"));
					myPanel.add(edgeField);

					int result = JOptionPane.showConfirmDialog(null, myPanel,
							"Please Enter X and Y Values", JOptionPane.OK_CANCEL_OPTION);
					if (result == JOptionPane.OK_OPTION) {
						m = Integer.parseInt(vertexField.getText());//number of vertices
						n = Integer.parseInt(edgeField.getText());//number of edges
						//The #vertices and #edges were/were not compatible together (to less edges: always disconnected vertices, or to much edges: graph would be more than complete)
						if((n<m-1)||(n>((m*(m-1))/2))){ //They were not > Throw an error message
							JPanel newPanel = new JPanel();
							JOptionPane.showMessageDialog(newPanel, "Problem with creating a graph, with: " + m + "vertices, and: "+n+" edges.","Backup problem", JOptionPane.ERROR_MESSAGE);
						}else{ //They were > create the graph
							int mode = getMode();
							System.out.println("mode is: "+mode);
							falseVertexEdgeComb = false;
							state.states[GameState.INGAME] = new PlaygroundLevel(state, GraphUtil.generateRandomGraph(m, n)); //Start the actual game
							state.setState(GameState.INGAME);
						}
					}else if(result == JOptionPane.CANCEL_OPTION){
						falseVertexEdgeComb = false;
					}
				}
    		}
			else if(clickedVertex == 2){
				state.states[GameState.INGAME] = new PlaygroundLevel(state, new Graph(CircleSolver.getVertexObjects(CircleSolver.circlesolver (170, 0, 270, 14, false)), new int[][]{})); //Here we test out CircleSolver
				state.setState(GameState.INGAME);
			}

			clickedVertex = -1;
    	}
    }

    @Override
    public void keyPressed(KeyEvent e){
    	if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
			System.exit(0);
    	}
    }

    public int getMode(){
    	//String[] buttons = {"one","two","three"};
		int mode = 0;
    	JRadioButton oneButton = new JRadioButton ("The bitter end.");
		JRadioButton twoButton = new JRadioButton ("The best upper bound, in a fixed time.");
		JRadioButton threeButton = new JRadioButton ("Random order.");
		oneButton.setSelected(true);
		ButtonGroup group = new ButtonGroup ();
		group.add(oneButton);
		group.add(twoButton);
		group.add(threeButton);
		JPanel panel = new JPanel();
		panel.add(oneButton);
    	panel.add(twoButton);
    	panel.add(threeButton);

		JOptionPane.showOptionDialog(null, panel,"Radio Test", JOptionPane.OK_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE, null, null, null);
		if (oneButton.isSelected()) mode =1;
		else if (twoButton.isSelected()) mode = 2;
		else if (threeButton.isSelected()) mode = 3;
		menuMusic.stop();
		return mode;
	}
}
