package robots.DH.math.autodiff;


import robots.DH.math.AbstractIdentityFactory;
import robots.DH.math.Field;

import java.util.Collection;


public class VariableVector<X extends Field<X>> extends DifferentialVectorFunction<X> {

    public VariableVector(
            AbstractIdentityFactory<X> i_factory,
            Variable<X>... i_v) {
        super(i_factory, i_v);
    }

    public VariableVector(
            AbstractIdentityFactory<X> i_factory,
            Collection<Variable<X>> i_v) {
        super(i_factory, i_v);
    }


    public Variable<X> get(int i) {
        return (Variable<X>) m_v.get(i);
    }

    public void assign(DifferentialVectorFunction<X> i_v) {
        final int SIZE = size();
        if (SIZE != size()) {
            //throw Error
            return;
        }
        for (int i = SIZE - 1; i >= 0; i--) {
            get(i).set(i_v.get(i).getValue());
        }
    }
}
