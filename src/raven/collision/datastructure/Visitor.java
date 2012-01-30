package raven.collision.datastructure;

import raven.collision.LineSegment;

public interface Visitor {
	public void visit(LineSegment seg);
	public void visit(DataStructureNode dsn);
}
