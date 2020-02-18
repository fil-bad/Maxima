package processingElement;
import javaMisc.Polygon;
import javaMisc.Vertex;
import processing.core.PApplet;

public class Box extends Solid implements Obj3D, Obstacle {
    private CommonDraw com = CommonDraw.getInstance();


    private int w,l,h;
    private int color, highlight, showCol;
    public Box (PApplet win, int w, int l, int h, int color) {
        super(win);
        this.w = w;
        this.l = l;
        this.h = h;
        this.color = color;
        this.showCol = this.color;
        highlight = win.color(255,255,0);
    }

    @Override
    public void draw() {
        win.pushMatrix();
        win.pushStyle();

        applyCoord();

        win.translate(0,0,h/2.0f);
        win.fill(showCol);
        win.box(w,l,h);
        com.assi(100);
        win.translate(0,0,h);
        com.assi(100);

        win.popStyle();
        win.popMatrix();
    }


    @Override
    public void highlight(boolean b) {
        if(b)
            this.showCol = this.highlight;
        else
            this.showCol = this.color;
    }

    @Override
    public Polygon getPoly() {
        return new Polygon(new Vertex(50, 12));

    }
}
