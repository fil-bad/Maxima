package processingElement;

import geometry.Vertex;
import processing.core.PApplet;

public class Pointer {
    PApplet win;
    float x, y, h;
    float minX, minY, maxX, maxY;
    private CommonDraw com = CommonDraw.getInstance();
    private int b = 5;    //base
    private int lF = 5;   //lunghezza semi lato freccia

    public Pointer(PApplet win, int heigt, float minX, float minY, float maxX, float maxY) {
        this.win = win;
        x = y = 0;
        this.h = heigt;
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
    }
    // sup center in 0,0
    public Pointer(PApplet win, int heigt, float w, float h) {
        this.win = win;
        x = y = 0;
        this.h = heigt;
        this.minX = -w / 2;
        this.minY = -h / 2;
        this.maxX = w / 2;
        this.maxY = h / 2;
    }

    public void draw() {
        win.push();
        win.translate(x, y);

        //Cerchio su terreno
        win.pushMatrix();
        win.fill(0, 255, 255); // azzurro
        win.translate(0, 0, 3);
        win.circle(0, 0, 10);
        win.popMatrix();

        //Freccia
        win.translate(0, 0, h);
        win.pushMatrix();
        win.scale(1, 1, -1);
        win.translate(0, 0, h / 4.0f);  //disegno in base
        win.box(b, b, h / 2.0f);
        win.translate(0, 0, h / 4.0f);  //sposto origine alla fine
        com.pyramid(lF);
        win.popMatrix();

        win.pop();
    }

    public void printCoord() {
        System.out.println("Coord:{" + x + "," + y + "}");
    }

    public void addX(float x) {
        this.x += x;
        if (this.x < minX)
            this.x = minX;
        if (this.x > maxX)
            this.x = maxX;
    }

    public void addY(float y) {
        this.y += y;
        if (this.y < minY)
            this.y = minY;
        if (this.y > maxY)
            this.y = maxY;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
        if (this.x < minX)
            this.x = minX;
        if (this.x > maxX)
            this.x = maxX;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
        if (this.y < minY)
            this.y = minY;
        if (this.y > maxY)
            this.y = maxY;
    }

    public Vertex get() {
        return new Vertex(getX(), getY());
    }
}
