package raven.collision;

import raven.collision.datastructure.MathFactory;

public class RavenArm {
	//public LineSegment mainSegment;
	public LineSegment seg1;
	public LineSegment seg2;
	public LineSegment seg12;
	
	public LineSegment orientationSegment;
	public LineSegment currseg;
	
	public RavenArm(EndPoint srcEP,EndPoint endEP){
		currseg = new LineSegment();
		currseg.start=srcEP;
		currseg.stopref=endEP;
		currseg.currstop=endEP;
		//double length = MathFactory.getInstance().getLength(currseg);
		orientationSegment = new LineSegment();
		this.orientationSegment.start=new EndPoint(0,0);
		this.orientationSegment.stopref=new EndPoint(200,0);
		this.orientationSegment.currstop=new EndPoint(200,0);
		seg1=new LineSegment();
		seg1.start=currseg.start;
		seg1.stopref=new  EndPoint(currseg.start.x,currseg.start.y-10);
		seg1.currstop=new  EndPoint(currseg.start.x,currseg.start.y-10);
		double dotprod = MathFactory.getInstance().dotProduct(this.orientationSegment, currseg);
		double angle = Math.acos(dotprod);
		if(srcEP.y>100)
			angle*=-1;
		seg1=MathFactory.getInstance().phaseShiftEndPoint(seg1.start, seg1.currstop, angle);
		
		seg2=new LineSegment();
		seg2.start=currseg.currstop;
		seg2.stopref=new  EndPoint(currseg.currstop.x,currseg.currstop.y-10);
		seg2.currstop=new  EndPoint(currseg.currstop.x,currseg.currstop.y-10);
		double dotprod2 = MathFactory.getInstance().dotProduct(this.orientationSegment, currseg);
		double angle2 = Math.acos(dotprod2);
		if(srcEP.y>100)
			angle2*=-1;
		seg2=MathFactory.getInstance().phaseShiftEndPoint(seg2.start, seg2.currstop, angle2);
		
		seg12 = new LineSegment();
		seg12.start=seg1.currstop;
		seg12.stopref=seg2.currstop;
		seg12.currstop=seg2.currstop;
		currseg.arm=this;seg1.arm=this;seg12.arm=this;seg2.arm=this;
		currseg.name="";seg1.name="";seg12.name="";seg2.name="";
		currseg.start.seg=currseg;currseg.stopref.seg=currseg;currseg.currstop.seg=currseg;
		seg1.start.seg=seg1;seg1.stopref.seg=seg1;seg1.currstop.seg=seg1;
		seg2.start.seg=seg2;seg2.stopref.seg=seg2;seg2.currstop.seg=seg2;
		seg12.start.seg=seg12;seg12.stopref.seg=seg12;seg12.currstop.seg=seg12;
	}
	
	public void udpate(EndPoint srcEP,EndPoint endEP){
		currseg = new LineSegment();
		currseg.start=srcEP;
		currseg.stopref=endEP;
		currseg.currstop=endEP;
		//double length = MathFactory.getInstance().getLength(currseg);
		orientationSegment = new LineSegment();
		this.orientationSegment.start=new EndPoint(0,0);
		this.orientationSegment.stopref=new EndPoint(200,0);
		this.orientationSegment.currstop=new EndPoint(200,0);
		seg1=new LineSegment();
		seg1.start=currseg.start;
		seg1.stopref=new  EndPoint(currseg.start.x,currseg.start.y-10);
		seg1.currstop=new  EndPoint(currseg.start.x,currseg.start.y-10);
		double dotprod = MathFactory.getInstance().dotProduct(this.orientationSegment, currseg);
		double angle = Math.acos(dotprod);
		if(srcEP.y>100)
			angle*=-1;
		seg1=MathFactory.getInstance().phaseShiftEndPoint(seg1.start, seg1.currstop, angle);
		
		seg2=new LineSegment();
		seg2.start=currseg.currstop;
		seg2.stopref=new  EndPoint(currseg.currstop.x,currseg.currstop.y-10);
		seg2.currstop=new  EndPoint(currseg.currstop.x,currseg.currstop.y-10);
		double dotprod2 = MathFactory.getInstance().dotProduct(this.orientationSegment, currseg);
		double angle2 = Math.acos(dotprod2);
		if(srcEP.y>100)
			angle2*=-1;
		seg2=MathFactory.getInstance().phaseShiftEndPoint(seg2.start, seg2.currstop, angle2);
		
		seg12 = new LineSegment();
		seg12.start=seg1.currstop;
		seg12.stopref=seg2.currstop;
		seg12.currstop=seg2.currstop;
		currseg.arm=this;seg1.arm=this;seg12.arm=this;seg2.arm=this;
		currseg.name="";seg1.name="";seg12.name="";seg2.name="";
		currseg.start.seg=currseg;currseg.stopref.seg=currseg;currseg.currstop.seg=currseg;
		seg1.start.seg=seg1;seg1.stopref.seg=seg1;seg1.currstop.seg=seg1;
		seg2.start.seg=seg2;seg2.stopref.seg=seg2;seg2.currstop.seg=seg2;
		seg12.start.seg=seg12;seg12.stopref.seg=seg12;seg12.currstop.seg=seg12;
	}
	
}
