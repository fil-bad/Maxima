package robots.DH;

import org.ejml.simple.SimpleMatrix;
import processing.core.PApplet;

import static java.lang.Math.*;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class DenHart {

    /**
     * Attributes
     */

    private ArrayList<Link> denHartTab;

    private MatrixQ Q_tot;
    private RobVars vars;

    private MatrixQ J;

    PApplet win;

    /**
     * Constructors
     */

    public DenHart() {
        this((PApplet) null);
    }

    public DenHart(PApplet win) {
        this.denHartTab = new ArrayList<Link>(0);
        this.Q_tot = new MatrixQ().setIdentity();
//        this.vars = new ArrayList<Variable<DoubleReal>>(0);
        this.vars = new RobVars();
        this.J = this.getDsym().jacobian();

        this.win = win;
    }

    private DenHart(PApplet win, ArrayList<Link> denHartTab) {
        this(win);
        for (Link l : denHartTab) {
            this.addLink(l);
        }
    }

    public DenHart(DenHart denHart) {
        this.denHartTab = denHart.getLinks();
        this.Q_tot = denHart.Q_tot;
        this.vars = denHart.vars;
        this.win = denHart.win;
    }

    public void draw() {
        win.push();
        for (Link l : denHartTab) {
            l.draw();
        }
        win.pop();
    }

    /**
     * Structural methods
     */

    public void addLink(Link link) {
        //append a new link to D-H table
        this.denHartTab.add(link);
        this.Q_tot.mulOnSelf(link.getQLink());
        this.vars.addVar(link.getVar());
        this.J = this.getDsym().jacobian();
    }

    public Link removeLink() {
        // remove last link (-> entry of D-H table). WARNING: could be very heavy to compute.
        //todo: vedere di ottimizare
        Link link2ret = this.denHartTab.remove(denHartTab.size() - 1);
        DenHart dhTmp = new DenHart(win, this.denHartTab);
        // we copy only the fields we need
        this.Q_tot = dhTmp.Q_tot;
        this.vars = dhTmp.vars;
        this.J = dhTmp.getDsym().jacobian();
        return link2ret;
    }

    /**
     * Variable interaction
     **/

    public RobVars getRVar() {
        return this.vars;
    }

    /**
     * Compute Numeric Matrix form variable value
     **/
    public SimpleMatrix getQ() {
        return this.Q_tot.getNumeric();
    }

    public SimpleMatrix getD() {
        return getDsym().getNumeric();
    }

    public SimpleMatrix getR() {
        return getRsym().getNumeric();
    }

    public SimpleMatrix getJ() {
        return this.J.getNumeric();
    }

    /**
     * Symbolic Matrix
     **/
    public MatrixQ getQsym() {
        return this.Q_tot;
    }

    public MatrixQ getDsym() {
        return this.Q_tot.getVPos();
    }

    public MatrixQ getRsym() {
        return this.Q_tot.getMatRot();
    }

    public MatrixQ getJsym() {
        return this.J;
    }


    /**
     * DH table informatio
     **/
    public ArrayList<Link> getLinks() {
        return this.denHartTab;
    }

    public int getNumDOF() {
        return this.denHartTab.size();
    }


    /**
     * Print methods
     */

    public void printDHTab() {
        System.out.println("DH sym:\t\t\t\t\t\tDH num:");
        for (Link l : this.denHartTab) {
            l.printLink();
            System.out.print("\t");
            l.printValLink();
            System.out.println();
        }
        System.out.println();

        Q_tot.printMatValue();
    }

    /** Demo main**/
    public static void main(String[] args) {
        DenHart dh = new DenHart();
        dh.addLink(new RotLink("q1", 50, (float) PI, 0));
        dh.addLink(new PrismLink((float) PI / 2, "q2", 0, 20));
        dh.printDHTab();
        System.out.println("Robot RP, in cui si fa variare la P a ogni iterazione:");
        int i = 0;
        while (true) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            dh.getRVar().setVars(1, i);  //checked
//            dh.getRVar().setVars("q2", i);  //checked
//            double[] v = {0.0,i}; dh.getRVar().setVars(v);    //checked
//            dh.getRVar().getVar()[1].set(new DoubleReal(i));  //checked
            dh.printDHTab();
            System.out.println("Variabili di giunto:");
            dh.getRVar().printVar();
            System.out.println("Matrice cinematica diretta:");
            dh.getQ().print("%.3f");
            System.out.println("Matrice Rotazione:");
            dh.getR().print("%.3f");
            System.out.println("Matrice Posizione:");
            dh.getD().print("%.3f");
            System.out.println("Matrice Jacobiana:");
            dh.getJ().print("%.3f");
            System.out.println();
            i++;
        }
    }

}
