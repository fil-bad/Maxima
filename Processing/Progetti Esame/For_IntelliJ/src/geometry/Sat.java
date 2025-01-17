package geometry;

import org.ejml.dense.fixed.CommonOps_DDF2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Sat {

    /**
     * Test di Collisioni
     **/
    // True if inside the circle there are almost one vertex of the polygon
    public static boolean haveCollided(Polygon poly1, Vertex c, double r) {
        // Implements the actual SAT algorithm
        ArrayList<Vertex> edges = polyToEdges(poly1.getVertices());

        Vertex[] axes = new Vertex[edges.size()];
        for (int i = 0; i < edges.size(); i++) {
            axes[i] = edges.get(i).orthogonal();
            axes[i].norm();
        }
        for (Vertex axis : axes) {
            if (!overlap(project(poly1.getVertices(), axis), project(c, r, axis))) {
                // The polys don't overlap on this axis so they can't be touching
                return false;
            }
        }
        // The polys overlap on all axes so they must be touching
        return true;
    }

    // true if p1 intersect p2, or otherwise
    public static boolean haveCollided(Polygon poly1, Polygon poly2) {

        Vertex[] vert_poly1 = poly1.getVertices(); // this two lines were added due to compatibility reasons
        Vertex[] vert_poly2 = poly2.getVertices();
        // Do an optimization check using the maxDist
        // No maxDist so run SAT on the polys (we deleted this "optimization", we preferred to run every case)
        return runSAT(vert_poly1, vert_poly2);
    }

    // true if p1 contains p2, not otherwise
    public static boolean contains(Polygon p1, Polygon p2) {
        /*
        ask if the first (bigger) contains the second (smaller); in our algorithm, we must test if a generic shape
        contains a square of QuadTree; we can do this only with convex polygons.
        */
        for (Vertex v : p2.getVertices()) {
            //   int wn = p1.wn_PnPoly2(v);
            //   System.out.println("Value of wn = " + wn);
            //   if (wn == 0) return false;
            if (!p1.contains(v)) return false;
        }
        // if all the vertices are into the shape, then the entire polygon p2 is inside the first one
        return true;
    }


    private static boolean runSAT(Vertex[] poly1, Vertex[] poly2) {
        // Implements the actual SAT algorithm
        ArrayList<Vertex> edges = polyToEdges(poly1);
        edges.addAll(polyToEdges(poly2));
        Vertex[] axes = new Vertex[edges.size()];
        for (int i = 0; i < edges.size(); i++) {
            axes[i] = edges.get(i).orthogonal();
            axes[i].norm();
        }
        for (Vertex axis : axes) {
            if (!overlap(project(poly1, axis), project(poly2, axis))) {
                // The polys don't overlap on this axis so they can't be touching
                return false;
            }
        }
        // The polys overlap on all axes so they must be touching
        return true;
    }

    /** Calcoli utili**/
    /**
     * Returns a vector going from point1 to point2
     */
    private static Vertex edgeVector(Vertex p1, Vertex p2) {
        return new Vertex(p2.getX() - p1.getX(), p2.getY() - p1.getY());
    }

    /**
     * Returns an array of the edges of the poly as vectors
     */
    private static ArrayList<Vertex> polyToEdges(Vertex[] poly) {
        ArrayList<Vertex> vertices = new ArrayList<>(poly.length);
        for (int i = 0; i < poly.length; i++) {
            vertices.add(edgeVector(poly[i], poly[(i + 1) % poly.length]));
        }
        return vertices;
    }

    /**
     * Returns the dot (or scalar) product of the two vectors
     */
    private static double dotProduct(Vertex v1, Vertex v2) {
        return CommonOps_DDF2.dot(v1.get(), v2.get());
    }

    /**
     * Returns a vector showing how much of the poly lies along the axis
     */
    private static Vertex project(Vertex[] poly, Vertex axis) {
        List<Double> dots = new ArrayList<>();
        for (Vertex vertex : poly) {
            dots.add(dotProduct(vertex, axis));
        }
        return new Vertex(Collections.min(dots), Collections.max(dots));
    }

    /**
     * Returns a vector showing how much of the Circle lies along the axis
     */
    private static Vertex project(Vertex center, double r, Vertex axis) {
        double dot = dotProduct(center, axis);
//        System.out.println("dot:"+dot);
        return new Vertex(dot - r, dot + r);
    }

    /**
     * Returns a boolean indicating if the two projections overlap
     */
    private static boolean overlap(Vertex projection1, Vertex projection2) {
//        System.out.println("banda1:"+ projection1.getX()+";"+projection1.getY()+"  banda2:"+ projection2.getX()+";"+projection2.getY());
        return projection1.getX() <= projection2.getY() &&
                projection2.getX() <= projection1.getY();
    }

    /**
     * Demo main
     **/
    public static void main(String[] args) throws Exception {
        // creating two polygons
        System.out.println("####Intersect polygons:####");
        Polygon a = new Polygon(new Vertex(10, 10), new Vertex(10, 100), new Vertex(100, 100), new Vertex(100, 10));
        Polygon b = new Polygon(new Vertex(20, 20), new Vertex(20, 120), new Vertex(120, 120), new Vertex(120, 20));
        // they are two squares which overlaps
        System.out.print("a: ");
        a.printVertices();
        System.out.print("b: ");
        b.printVertices();
        System.out.println("\tDo a & b collide? " + haveCollided(a, b));
        System.out.println("\tDoes a contain b? " + contains(a, b));
        System.out.println("----Now invert the polygons:----");
        System.out.println("\tDo a & b collide? " + haveCollided(b, a));
        System.out.println("\tDoes a contain b? " + contains(b, a));
        System.out.println("----Now same polygon:----");
        System.out.println("\tDo a & a collide? " + haveCollided(a, a));
        System.out.println("\tDoes a contain a? " + contains(a, a));

        System.out.println();
        // now the entire polygon c is contained in a
        System.out.println("####Contained polygons:####");
        Polygon c = new Polygon(new Vertex(30, 30), new Vertex(30, 90), new Vertex(90, 90), new Vertex(90, 30));
        System.out.print("c: ");
        c.printVertices();
        System.out.println("\tDo a & c collide? " + haveCollided(a, c));
        System.out.println("\tDoes a contain c? " + contains(a, c));
        System.out.println("----Now invert the polygon:----");
        System.out.println("\tDo c & a collide? " + haveCollided(c, a));
        System.out.println("\tDoes c contain a? " + contains(c, a));

        System.out.println();
        // an edge smaller
        System.out.println("####One edge common polygons:####");
        Polygon d = new Polygon(new Vertex(10, 10), new Vertex(10, 30), new Vertex(100, 30), new Vertex(100, 10));
        System.out.print("d: ");
        d.printVertices();
        System.out.println("\tDo a & d collide? " + haveCollided(a, d));
        System.out.println("\tDoes a contain d? " + contains(a, d));
        System.out.println("----Now invert the polygon:----");
        System.out.println("\tDo d & a collide? " + haveCollided(d, a));
        System.out.println("\tDoes d contain a? " + contains(d, a));
        // si engono a creare problemi ogni qualvolta si ha un vertica coincidente o un vertice su uno spigolo

        System.out.println();
        // Not in contact
        System.out.println("####Not in contact polygons:####");
        Polygon e = new Polygon(new Vertex(-10, 10), new Vertex(-10, 100), new Vertex(-100, 100), new Vertex(-100, 10));
        System.out.print("e: ");
        e.printVertices();
        System.out.println("\tDo a & e collide? " + haveCollided(a, e));
        System.out.println("\tDoes a contain e? " + contains(a, e));
        System.out.println("----Now invert the polygon:----");
        System.out.println("\tDo e & a collide? " + haveCollided(e, a));
        System.out.println("\tDoes e contain a? " + contains(e, a));

        System.out.println();
        // Circle test
        System.out.println("####Circle have vertex of polygon inside:####");
        Vertex center = new Vertex(100, 20);
        System.out.print("Center: ");
        center.printVertex();
        System.out.println();
        System.out.println("\tDo a & circle r = 20 collide? " + haveCollided(a, center, 20));
        System.out.println("\tDo a & circle r = 10 collide? " + haveCollided(a, center, 10));
        System.out.println("\tDo a & circle r = 5 collide? " + haveCollided(a, center, 5));

        System.out.println();
        System.out.println("####Circle haven't vertex of polygon inside but collide:####");
        Vertex center2 = new Vertex(1, 50);
        System.out.print("Center: ");
        center2.printVertex();
        System.out.println();
        System.out.println("\tDo a & circle r = 20 collide? " + haveCollided(a, center2, 20));
        System.out.println("\tDo a & circle r = 10 collide? " + haveCollided(a, center2, 10));
        System.out.println("\tDo a & circle r = 5 collide? " + haveCollided(a, center2, 5));
    }
}