import geometry.Vertex;
import graph.QTGraph;
import processing.core.*;
import processingElement.*;
import quadtree.Boundary;
import quadtree.QuadTree;


public class ProcessingClass extends PApplet {


    public static void main(String[] args) {
        PApplet.main("ProcessingClass");
    }

    @Override
    public void settings() {
        // TODO: Customize screen size and so on here
        size(1200, 720, P3D);
    }

    Box ob1, ob2;
    CommonDraw com;
    Terra gnd;
    Pointer point;

    QuadTree qt;
    QTGraph qtGraph;

    @Override
    public void setup() {
        // TODO: Your custom drawing and setup on applet start belongs here
        clear();
        cameraInit();
        frameRate(60);


        com = CommonDraw.getInstance(this);
        gnd = new Terra(this, 800, 400, color(200, 150, 100));
        point = new Pointer(this, 60, 800, 400);
        ob1 = new Box(this, 50, 40, 10, color(255, 150, 0, 100));
        ob2 = new Box(this, 50, 40, 10, color(0, 255, 0, 100));


//        ob1.setR(radians(50));
        ob2.setD(150, -60, 0);


    }


    int a = 0;

    @Override
    public void draw() {
        // TODO: Do your drawing for each frame here
        clear();
        cameraSet();

        //Oggetti da graficare
        gnd.draw();
        com.axes(255);
        point.draw();

        ob1.setD(point.getX(), point.getY(), 0);


        Obstacle[] obstacles = {ob1, ob2};
        qt = new QuadTree(obstacles, new Boundary(-400, -200, 400, 200), 10);
        qtGraph = new QTGraph(qt, 100, obstacles);


        QuadTree.dfs(qt, this);
        qtGraph.printGraph(this, 10);
        qtGraph.printPath(this, 10);

        ob1.draw();
        ob2.draw();
    }

    @Override
    public void keyPressed() {
        if (key == 'r') {
            cameraInit();
        }
        if (key == 'd') {
            qtGraph.calcVert2Visit(new Vertex(-30, 150), new Vertex(310, 100));
            //todo: risolvere il crash che si verifica che si aggiorna troppo
            //N.B.: se non aggiorno Dijkstra insieme al grafo, crasha
        }
    }


    private float eyeX, eyeY, eyeZ;
    private float centerX, centerY, centerZ;
    private float Zrot;
    Vertex addPoint; // variabile per calcolare di quanto spostare il puntatore

    // Set camera e sistema ortonormale destro
    private void cameraInit() {
        eyeX = 0.0f;
        eyeY = 400.f;
        eyeZ = 400.0f;

        centerX = 0.0f;
        centerY = 0.0f;
        centerZ = 0.0f;

        Zrot = 0.0f;

        addPoint = new Vertex(0, 0);
    }

    private void cameraSet() {
        //Fondale da disegnare
        background(color(0x96FCFA));

        directionalLight(223, 126, 126, 0, 0, (float) -1);
        ambientLight(200, 200, 200);

        float d = dist(eyeX, eyeY, eyeZ, centerX, centerY, centerZ);

        if (mousePressed && (mouseButton == LEFT)) { //zoom e rotazione
            float x, y, z, dn;
            x = eyeX - centerX;
            y = eyeY - centerY;
            z = eyeZ - centerZ;
            dn = dist(0, 0, 0, x, y, z);
            x /= dn;
            y /= dn;
            z /= dn;
            d += mouseY - pmouseY;
            if (d < 10)    // 150
                d = 10;
            eyeX = centerX + x * d;
            eyeY = centerY + y * d;
            eyeZ = centerZ + z * d;

            //Allenamento per l'uso delle matrici
//            SimpleMatrix look = new SimpleMatrix(3,1);
//            look.set(0,0, eyeX);
//            look.set(1,0, eyeY);
//            look.set(2,0, eyeZ);
//            println("look");
//            look.print();
//            SimpleMatrix center = new SimpleMatrix(3,1);
//            center.set(0,0, centerX);
//            center.set(1,0, centerY);
//            center.set(2,0, centerZ);
//            println("center");
//            center.print();
//            float d = dist(eyeX,eyeY,eyeZ,centerX,centerY,centerZ);
//            d += mouseY - pmouseY;
//
//            SimpleMatrix pos = new SimpleMatrix(3,1);
//            pos.set(look.plus(-1,center)); //look-center
//            pos = pos.divide(NormOps_DDRM.normP2(pos.getDDRM())); //normalize
//            println("pos");
//            pos.print();
//            center = center.plus(d,pos);
//            center.print();
//
//            eyeX = (float)center.get(0,0);
//            eyeY = (float)center.get(1,0);
//            eyeZ = (float)center.get(2,0);
            Zrot += (mouseX - pmouseX) / 200.0;

        } else if (mousePressed && (mouseButton == RIGHT)) {    // traslazione xy
            eyeX -= (mouseX - pmouseX) / 2.0;
            centerX -= (mouseX - pmouseX) / 2.0;
            eyeY -= (mouseY - pmouseY) / 2.0;
            centerY -= (mouseY - pmouseY) / 2.0;
        } else if (mousePressed && (mouseButton == CENTER)) {    // Seleziona

        } else {
            //
            addPoint.set((mouseX - pmouseX), -(mouseY - pmouseY));
            addPoint.rotate(-Zrot);
            point.addX((float) addPoint.getX() * d / 800);
            point.addY((float) addPoint.getY() * d / 800);
        }

//        if (centerZ + 10 > eyeZ)
//            centerZ = eyeZ - 15;

        camera(eyeX, eyeY, eyeZ, // eyeX, eyeY, eyeZ
                centerX, centerY, centerZ, // centerX, centerY, centerZ
                0.0f, 1.0f, 0.0f); // upX, upY, upZ

        //Ortonormale Destro
        //rotateZ(PI/2);
        scale(1, -1, 1);
        rotateZ(Zrot);
    }

    // Funzione visualizzazione asse
    private int p = 100;   //profondità
    private int b = 10;    //base
    private int h = 10;     //altezza
    private int lF = 10;   //lunghezza semi lato freccia

    private void assi(float alfa) {
        pushStyle();
        strokeWeight((float) 0.5);
        fill(255, 0, 0, alfa); // rosso = x
        pushMatrix();
        rotateY(PI / 2);
        translate(0, 0, p >> 1);  //disegno in base
        box(h, b, p);
        translate(0, 0, p >> 1);  //sposto origine alla fine
        piramide(lF);
        popMatrix();

        fill(0, 255, 0, alfa); // verde = y
        pushMatrix();
        rotateX(-PI / 2);
        translate(0, 0, p >> 1);  //disegno in base
        box(h, b, p);
        translate(0, 0, p >> 1);  //sposto origine alla fine
        piramide(lF);
        popMatrix();

        fill(0, 0, 255, alfa); // blu = z
        pushMatrix();
        translate(0, 0, p >> 1);  //disegno in base
        box(h, b, p);
        translate(0, 0, p >> 1);  //sposto origine alla fine
        piramide(lF);
        popMatrix();
        popStyle();
    }

    private void piramide(int h) {
        beginShape();
        vertex(-h, -h);
        vertex(+h, -h);
        vertex(0, 0, 2 * h);
        endShape(CLOSE);

        beginShape();
        vertex(+h, -h);
        vertex(+h, +h);
        vertex(0, 0, 2 * h);
        endShape(CLOSE);

        beginShape();
        vertex(+h, +h);
        vertex(-h, +h);
        vertex(0, 0, 2 * h);
        endShape(CLOSE);

        beginShape();
        vertex(-h, +h);
        vertex(-h, -h);
        vertex(0, 0, 2 * h);
        endShape(CLOSE);

        beginShape();
        vertex(-h, -h);
        vertex(+h, -h);
        vertex(+h, +h);
        vertex(-h, +h);
        endShape(CLOSE);
    }
}
