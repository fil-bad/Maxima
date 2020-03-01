package robots.DH;

import robots.DH.math.DoubleReal;
import robots.DH.math.autodiff.Variable;

import static java.lang.Math.PI;


public class PrismLink implements Link {

// todo: extend with GenericLink


    private String d_qi;
    private double theta;
    private double alpha;
    private double a;

    private MatrixQ Q0_1;

    public PrismLink(double theta, String qi, double alpha, double a) {

        this.theta = theta;
        this.d_qi = qi;
        this.alpha = alpha;
        this.a = a;

        MatrixQ avvZ = new MatrixQ().setRotZ("", theta).mul(new MatrixQ().setTslZ(qi, 0));
        MatrixQ avvX = new MatrixQ().setRotX("", alpha).mul(new MatrixQ().setTslX("", a));
        this.Q0_1 = avvZ.mul(avvX);
    }

    @Override
    public MatrixQ getQLink() {
        return this.Q0_1;
    }

    @Override
    public Variable<DoubleReal> getVar() {
        return Q0_1.getRobVars().getVar(0);
    }

    @Override
    public void printLink() {
        System.out.printf("[%.3f  %s  %.3f  %.3f]", this.theta, this.d_qi, this.alpha, this.a);
    }

    @Override
    public void printValLink() {
        System.out.printf("[%.3f  %.3f  %.3f  %.3f]", this.theta, getVar().getValue().doubleValue(), this.alpha, this.a);
    }

    @Override
    public String whichQ_iIs() {
        return this.d_qi;
    }


    public static void main(String[] args) {
        Link l = new PrismLink(PI, "q1", PI / 2, 12);
        l.printLink();
        l.whichQ_iIs();
        l.getQLink().printMatValue();
        System.out.println(l.getVar().getName());
    }

}
