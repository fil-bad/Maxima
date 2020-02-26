package javaMisc.math.autodiff;

import java.util.Collection;

import javaMisc.math.AbstractIdentityFactory;
import javaMisc.math.Field;


public class ConstantVector<X extends Field<X>> extends DifferentialVectorFunction<X> {

	public ConstantVector(AbstractIdentityFactory<X> i_factory, Constant<X>... i_v) {
		super(i_factory, i_v);
	}

	public ConstantVector(AbstractIdentityFactory<X> i_factory, Collection<Constant<X>> i_v) {
		super(i_factory, i_v);
	}


	public Constant<X> get(int i) {
		return (Constant<X>) m_v.get(i);
	}

}