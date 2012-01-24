package raven.collision;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Collision implements MouseListener,Runnable{
	private JFrame mainFrame = new JFrame("Collision Demo");
	private JPanel controlPanel = new JPanel();
	private JPanel viewPanel = new JPanel();
	private Canvas canvas = new Canvas();
	
	private int maxScreenSegments=4;
	private LineSegment[] linesegments;
	private int setSegments = 0;
	private double angle=0.01;
	
	private static int state=0;//
	public static int  WAITINGFORSEGMENTPOINTS=0;
	public static int WAITINGTOSTARTSIM=1;
	public static int SIMULATIONRUNNING=2;
	
	private CollisionDetection cd = new CollisionDetection();
	
	public void init(){
		mainFrame.getContentPane().setLayout(new BorderLayout());
		mainFrame.getContentPane().add(controlPanel, BorderLayout.WEST);
		mainFrame.getContentPane().add(viewPanel,BorderLayout.CENTER);
		controlPanel.setLayout(new GridLayout(3,1));
		
		JButton startRot = new JButton("Start");
		startRot.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(Collision.state==Collision.WAITINGTOSTARTSIM)
					Collision.state=Collision.SIMULATIONRUNNING;
				else
					print("Add you pickoff arms first by clicking on white area ... you need four");
			}
			
		});
		JButton stopRot = new JButton("Stop");
		stopRot.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(Collision.state==Collision.SIMULATIONRUNNING)
					Collision.state=Collision.WAITINGTOSTARTSIM;
				else
					print("Simulaion not running yet ... add pickoff arms and click start");
			}
			
		});
		JButton clearPane = new JButton("Clear"); //  start over
		clearPane.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				print("attempt reset of simulation");
				Collision.state=3;
			}
			
		});
		controlPanel.add(startRot);controlPanel.add(stopRot);controlPanel.add(clearPane);
		
		canvas.setSize(200, 200);
		canvas.setBackground(Color.WHITE);
		viewPanel.add(canvas);
		canvas.addMouseListener(this);
		
		mainFrame.pack();
		mainFrame.setVisible(true);
		
		this.linesegments = new LineSegment[this.maxScreenSegments];
		for(int i=0;i<this.linesegments.length;i++){
			linesegments[i]=new LineSegment();
			linesegments[i].start=new EndPoint();
			if(i==0){
				linesegments[i].start.x=0;
				linesegments[i].start.y=0;
			}else if(i==1){
				linesegments[i].start.x=200;
				linesegments[i].start.y=0;
			}else if(i==2){
				linesegments[i].start.x=200;
				linesegments[i].start.y=200;
			}else if(i==3){
				linesegments[i].start.x=0;
				linesegments[i].start.y=200;
			}
		}
		Thread thisThread = new Thread(this);
		thisThread.start();
	}
	
	public void run(){
		while(true){
			try{
				Thread.sleep(100);
				switch(state){
					case (0):
						print("add pick off arms and start demo");
					    break;
					case (1):
						print("in state waiting to start demo");
					    break;
					case (2):
						print("simulation is running");
					    //update endpoints then update canvas
					    for(int i=0;i<this.setSegments;i++){
					    	this.angle=this.angle+0.01;
					    	EndPoint ep = this.linesegments[i].stopref;
					    	EndPoint ep2 = this.linesegments[i].currstop;
					    	int[] shiftret = getNewShiftedCoords(ep.x, ep.y);
					    	ep2.x=shiftret[0];
					    	ep2.y=shiftret[1];
					    	cd.solve(this.linesegments);
					    	this.updateCanvas();
					    }
					    break;
					case (3):
						print("attemp simulation reset");
					    Collision.state=0;
					    this.setSegments=0;
					    this.angle=0;
					    this.updateCanvas();
					    break;
				}
			}catch(Exception e){
			}
		}
	}
	
	private int[] getNewShiftedCoords(int x, int y) {
		/*
		 * Center is at 100,100 , find offset from center and determine angle and radius,
		 * then change angle and recalculate x,y
		 */
		int xoffset = (x-100)*(-1);
		int yoffset = y-100;
		
		//double hyp=Math.pow(Math.pow(xoffset, 2.0)+Math.pow(yoffset, 2.0), 0.5);
		//double currangle=Math.atan( ((double)yoffset)/((double)xoffset) );
	    //double vector=this.angle*Math.PI*2/360.0;
	    //int newx=(int)(xoffset+hyp*Math.sin(vector));
	    //int newy=(int)(xoffset+hyp*Math.cos(vector));
		int newx=(int)(xoffset*Math.cos(this.angle)-yoffset*Math.sin(this.angle));
		int newy=(int)(xoffset*Math.sin(this.angle)+yoffset*Math.cos(this.angle));
	    int retx = (newx*(-1))+100;
	    int rety = (newy)+100;
	    int ret[] = {retx,rety};
		return ret;
	}

	public static void main(String args[]){
		Collision c = new Collision();
		c.init();
	}
	
	public void updateCanvas(){
		Graphics g = canvas.getGraphics();
		g.clearRect(0, 0, 200, 200);
		//redraw all lines or as many as shown
		for(int i=0;i<this.setSegments;i++){
			int sx = this.linesegments[i].start.x;
			int sy = this.linesegments[i].start.y;
			int ex = this.linesegments[i].currstop.x;
			int ey = this.linesegments[i].currstop.y;
			g.setColor(Color.BLACK);
			g.drawLine(sx, sy, ex, ey );
			g.setColor(Color.RED);
			g.drawOval(0, 0, 199, 199);
		}
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		print("mouse clickec at x: "+arg0.getX()+"  y: "+arg0.getY());
		if(state==Collision.WAITINGFORSEGMENTPOINTS){
			EndPoint ep = new EndPoint();
			ep.x=arg0.getX();
			ep.y=arg0.getY();
			EndPoint ep2 = new EndPoint();
			ep2.x=arg0.getX();
			ep2.y=arg0.getY();
			this.linesegments[this.setSegments].stopref=ep;
			this.linesegments[this.setSegments].currstop=ep2;
			this.setSegments++;
			if(setSegments==this.maxScreenSegments)
				state=Collision.WAITINGTOSTARTSIM;
			updateCanvas();
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public void print(String msg){
		System.out.println(msg);
	}
}
