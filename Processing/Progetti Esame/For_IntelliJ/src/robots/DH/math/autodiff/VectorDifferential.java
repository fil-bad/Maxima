package robots.DH.math.autodiff;

import robots.DH.math.Field;


public interface VectorDifferential<X extends Field<X>, D> {

    D diff(VariableVector<X> i_v);
}
