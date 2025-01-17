package processingElement;

import geometry.Polygon;
import geometry.Vertex;
import processing.core.PApplet;

public class Box extends Solid {
    private CommonDraw com;


    private int w, l, h;
    private int color, highlight, showCol;

    public Box(PApplet win, int w, int l, int h, int color) {
        super(win);
        com = CommonDraw.getInstance();
        this.w = w;
        this.l = l;
        this.h = h;
        this.color = color;
        this.showCol = this.color;
        highlight = win.color(255, 255, 0);
        this.traslC.set(2, h);
    }

    // !!! DA USARE SOLO PER AVERE DEGLI OSTACOLI
    public Box(int w, int l, int h) {
        super(null);
        this.w = w;
        this.l = l;
        this.h = h;
    }

    @Override
    public Obstacle myCopy() {
        Box ret = new Box(win, w, l, h, color);
        ret.setD(this.getD());
        ret.setR(this.getR());
        return ret;
    }

    @Override
    public void draw() {
        win.push();


        applyCoord();

        win.translate(0, 0, h / 2.0f);
        win.fill(showCol);
        win.box(w, l, h);
        win.translate(0, 0, h / 2.0f);
        com.axes(100);

        win.pop();
    }


    @Override
    public void highlight(boolean b) {
        if (b)
            this.showCol = this.highlight;
        else
            this.showCol = this.color;
    }

    @Override
    public void highlight() {
        if (this.showCol == this.highlight)
            this.showCol = this.color;
        else
            this.showCol = this.highlight;
    }

    @Override
    public Polygon getPoly() {

        Vertex[] v_s = {
                new Vertex(+w / 2.0, +l / 2.0),     // Nord-Est
                new Vertex(-w / 2.0, +l / 2.0),     // Nord-Ovest
                new Vertex(-w / 2.0, -l / 2.0),     // Sud-Ovest
                new Vertex(+w / 2.0, -l / 2.0),     // Sud-Est
        };
        Polygon p = new Polygon(v_s);
        p.rotate(this.getR());
        p.translate(getD().get(0, 0), d.get(1, 0)); // getX, getY of d vector
        return p;
    }
}
