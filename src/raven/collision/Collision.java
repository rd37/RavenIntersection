package raven.collision;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import raven.collision.datastructure.MathFactory;

public class Collision implements MouseListener,Runnable{
	private JFrame mainFrame = new JFrame("Collision Demo");
	private JPanel controlPanel = new JPanel();
	private JPanel viewPanel = new JPanel();
	private Canvas canvas = new Canvas();
	
	private int maxScreenSegments=4;
	private LineSegment[] linesegments;
	private RavenArm[] armSegments;
	private int setSegments = 0;
	private double angle=0.01;
	
	private static int state=0;//
	public static int  WAITINGFORSEGMENTPOINTS=0;
	public static int WAITINGTOSTARTSIM=1;
	public static int SIMULATIONRUNNING=2;
	
	private CollisionDetection cd = new CollisionDetection();
	
	public LineSegment[] getLineSegments(){
		LineSegment array[] = new LineSegment[16];
		for(int i=0;i<linesegments.length;i++){
			array[4*i]=armSegments[i].currseg;
			array[4*i+1]=armSegments[i].seg1;
			array[4*i+2]=armSegments[i].seg2;
			array[4*i+3]=armSegments[i].seg12;
		}
		return array;
	}
	
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
			linesegments[i].start.seg=linesegments[i];
			linesegments[i].name="seg"+i;
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
		armSegments = new RavenArm[4];
		Thread thisThread = new Thread(this);
		thisThread.start();
	}
	
	public void run(){
		while(true){
			try{
				Thread.sleep(300);
				switch(state){
					case (0):
						//print("add pick off arms and start demo");
					    break;
					case (1):
						//print("in state waiting to start demo");
					    break;
					case (2):
						//print("simulation is running");
					    //update endpoints then update canvas
					    for(int i=0;i<this.setSegments;i++){
					    	this.angle=this.angle+0.02;
					    	EndPoint ep = this.linesegments[i].stopref;
					    	EndPoint ep2 = this.linesegments[i].currstop;
					    	double[] shiftret = getNewShiftedCoords(ep.x, ep.y);
					    	ep2.x=shiftret[0];
					    	ep2.y=shiftret[1];
					    	cd.clear();
					    	cd.populateEventQ(this.getLineSegments());
					    	cd.solve();
					    	this.updateCanvas();
					    }
					    break;
					case (3):
						//print("attemp simulation reset");
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
	
	private double[] getNewShiftedCoords(double x, double y) {
		/*
		 * Center is at 100,100 , find offset from center and determine angle and radius,
		 * then change angle and recalculate x,y
		 */
		double xoffset = (x-100)*(-1);
		double yoffset = y-100;
		
		double newx=(xoffset*Math.cos(this.angle)-yoffset*Math.sin(this.angle));
		double newy=(xoffset*Math.sin(this.angle)+yoffset*Math.cos(this.angle));
	    double retx = (newx*(-1))+100;
	    double rety = (newy)+100;
	    double ret[] = {retx,rety};
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
			EndPoint ord[] = MathFactory.getInstance().getSweepLineOrdered(linesegments[i]);
			if(i<2)
				armSegments[i].udpate(ord[0], ord[1]);
			else
				armSegments[i].udpate(ord[1], ord[0]);
			int sx = (int)this.armSegments[i].currseg.start.x;
			int sy = (int)this.armSegments[i].currseg.start.y;
			int ex = (int)this.armSegments[i].currseg.currstop.x;
			int ey = (int)this.armSegments[i].currseg.currstop.y;
			g.setColor(Color.BLACK);
			g.drawLine(sx, sy, ex, ey );
			
			int sx1 = (int)this.armSegments[i].seg1.start.x;
			int sy1 = (int)this.armSegments[i].seg1.start.y;
			int ex1 = (int)this.armSegments[i].seg1.currstop.x;
			int ey1 = (int)this.armSegments[i].seg1.currstop.y;
			
			g.setColor(Color.BLUE);
			g.drawLine(sx1, sy1, ex1, ey1 );
			
			int sx2 = (int)this.armSegments[i].seg2.start.x;
			int sy2 = (int)this.armSegments[i].seg2.start.y;
			int ex2 = (int)this.armSegments[i].seg2.currstop.x;
			int ey2 = (int)this.armSegments[i].seg2.currstop.y;
			
			g.setColor(Color.BLUE);
			g.drawLine(sx2, sy2, ex2, ey2 );
			g.drawLine(ex1, ey1, ex2, ey2);
			g.setColor(Color.RED);
			g.drawOval(0, 0, 199, 199);
		}
		LinkedList<CollisionPoint> list=cd.collisions;
		for(int i=0;i<list.size();i++){
			CollisionPoint cp = list.get(i);
			g.setColor(Color.red);
			g.drawOval((int)cp.x-2, (int)cp.y-2, 5, 5);
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
			ep.seg=this.linesegments[this.setSegments];
			ep2.seg=this.linesegments[this.setSegments];
			
			EndPoint ord[] = MathFactory.getInstance().getSweepLineOrdered(linesegments[this.setSegments]);
			if(this.setSegments<2)
				this.armSegments[this.setSegments]=new RavenArm(ord[0],ord[1]);
			else
				this.armSegments[this.setSegments]=new RavenArm(ord[1],ord[0]);
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
