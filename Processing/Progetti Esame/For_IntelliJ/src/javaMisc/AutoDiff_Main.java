package javaMisc;

import javaMisc.math.DoubleReal;
import javaMisc.math.DoubleRealFactory;
import javaMisc.math.autodiff.Constant;
import javaMisc.math.autodiff.DifferentialFunction;
import javaMisc.math.autodiff.DifferentialRealFunctionFactory;
import javaMisc.math.autodiff.Variable;

public class AutoDiff_Main {

    private static final DoubleRealFactory RNFactory = DoubleRealFactory.instance();
    private static final DifferentialRealFunctionFactory<DoubleReal> DFFactory = new DifferentialRealFunctionFactory<DoubleReal>(RNFactory);


    public static void main(String[] args) {

        double value_x = 3.0;
        double value_y = 3.0;

        Variable<DoubleReal> xVar = DFFactory.var("x", new DoubleReal(value_x));
        Variable<DoubleReal> y = DFFactory.var("y", new DoubleReal(value_y));

        Constant<DoubleReal> q = DFFactory.val(new DoubleReal(5));

        //h = q*x*( cos(x*y) + y )
        //DifferentialFunction<DoubleReal> h = q.mul(x).mul( DFFactory.cos( x.mul(y) ).plus(y) );
        DifferentialFunction<DoubleReal> h = xVar.plus(q);
        System.out.println(h.toString());

        System.out.println(h.getValue().doubleValue());

        xVar.set(new DoubleReal(-5));

        System.out.println(h.getValue().doubleValue());


        DifferentialFunction<DoubleReal> dhpx = h.diff(xVar);
        System.out.println(dhpx.toString());

        DifferentialFunction<DoubleReal> r = xVar.mul(xVar).diff(xVar);
        System.out.println(r.toString());

        DifferentialFunction<DoubleReal> trig = DFFactory.cos(xVar.mul(xVar)).diff(xVar);
        System.out.println(trig.toString());

    }
}