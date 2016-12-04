import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public abstract class Level extends MouseAdapter implements KeyListener{

	protected GameState state;
	protected Graph graph;
	protected int clickedVertex;
	protected int hoveredVertex;
    //vertex-mouse-offset: the offset of the mouse from the coordinates of the vertex (used so that the vertex doesn't "jump" when starting to drag)
	private int vmOffsetX;
	private int vmOffsetY;

	public Level(GameState state, Graph graph){
		this.state = state;
		this.graph = graph;
		clickedVertex = -1;
		hoveredVertex = -1;
	}

	public void terminate(){
		state.setState(GameState.MAIN_MENU);
		state.states[GameState.INGAME] = null;
	}
	public void setGraph(Graph graph){
		this.graph = graph;
	}


	public abstract void draw(Graphics2D g);
	public void tick(){}
	public void keyTyped(KeyEvent e) {}
	public void keyReleased(KeyEvent e) {}

	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
			state.setState(GameState.PAUSE_MENU);
	}
	
	public void mousePressed(MouseEvent e){
		clickedVertex = graph.getVertexAt(e.getX(), e.getY());
		if(clickedVertex != -1){
			Vertex v = graph.getVertex(clickedVertex);
    		vmOffsetX = e.getX() - v.getX();
     		vmOffsetY = e.getY() - v.getY();
		}			
	}
	
	public void mouseDragged(MouseEvent e){

		if(clickedVertex != -1){
			int newX = e.getX() - vmOffsetX;
			int newY = e.getY() - vmOffsetY;

			Vertex v = graph.getVertex(clickedVertex);
			if(newX < 0)
				newX = 0;
			if(newX > Game.WIDTH - v.getDiameter())
				newX = Game.WIDTH - v.getDiameter();
			if(newY < 0)
				newY = 0;
			if(newY > Game.HEIGHT - v.getDiameter())
				newY = Game.HEIGHT - v.getDiameter();
			v.move(newX, newY);
		}
	}

	public void mouseReleased(MouseEvent e){
		clickedVertex = -1;
	}
}