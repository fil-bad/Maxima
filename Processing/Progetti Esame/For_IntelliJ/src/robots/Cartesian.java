package robots;

import processing.core.PApplet;
import robots.DH.DenHart;
import robots.DH.Links.PrismLink;

public class Cartesian extends Robot {

    public Cartesian(PApplet win, float b) {
        dhTab = new DenHart(win);
        dhTab.addLink(new PrismLink(win, b, 0, "q1", -Math.PI / 2.0, 0));
        dhTab.addLink(new PrismLink(win, b, -Math.PI / 2.0, "q2", -Math.PI / 2.0, 0));
        dhTab.addLink(new PrismLink(win, b, 0, "q3", 0, 0));
    }

    public void draw() {
//        com.axes(255);
        dhTab.draw();
    }


}
