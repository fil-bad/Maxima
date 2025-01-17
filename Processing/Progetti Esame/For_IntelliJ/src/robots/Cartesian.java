package robots;

import org.ejml.simple.SimpleMatrix;
import processing.core.PApplet;
import robots.DH.DenHart;
import robots.DH.Links.PrismLink;
import robots.DH.Links.RotLink;


public class Cartesian extends Robot {

    public Cartesian(PApplet win, float b) {
        dhTab = new DenHart(win);
        this.win = win;
        dhTab.addLinkStrut(new PrismLink(win, b, 0, "q1", -Math.PI / 2.0, 0));
        dhTab.addLinkStrut(new PrismLink(win, b, -Math.PI / 2.0, "q2", -Math.PI / 2.0, 0));
        dhTab.addLinkStrut(new PrismLink(win, b, 0, "q3", 0, 0));
        dhTab.addLinkOri(new RotLink(win, b, "q4", 0, -Math.PI / 2.0, 0));
        dhTab.addLinkOri(new RotLink(win, b, "q5", 0, Math.PI / 2.0, 0));
        dhTab.addLinkOri(new RotLink(win, b, "q6", 50, 0, 0));
        dhTab.getDHVar().setVars(50, 50, 50, 0.1, 0.2, 0);

        super.setCtrl(0.2, 0.1, 0.2);
    }

    @Override
    public SimpleMatrix inverse(double x, double y, double z, double theta) {
        SimpleMatrix Kep, Keo;
        double lambda = 1 / 10.0;
        double gamma = 1 / 100.0;
        Kep = SimpleMatrix.identity(dhTab.getStrutVar().varSize()).scale(lambda);
        Keo = SimpleMatrix.identity(dhTab.getOriVar().varSize()).scale(gamma);
        SimpleMatrix ret = null;
        try {
            ret = super.inverse(10000, x, y, z, theta, Kep, Keo);
        } catch (Exception e) {
            if (e.getMessage().equals("EndWork")) {
                System.out.println("Soluzione trovata");
                if (ret == null)
                    return dhTab.getDHVar().get_qVect();
            } else {
                System.err.println(e.getMessage());
                e.printStackTrace();
                System.exit(-1);
            }
        }
//        SimpleMatrix ret = super.inverse(1, x, y, z, theta, Kep, Keo);
        return ret;
    }


}
