package org.fdesigner.workbench.internal.databinding;

import org.fdesigner.databinding.observable.value.ValueDiff;
import org.fdesigner.databinding.property.INativePropertyListener;
import org.fdesigner.databinding.property.ISimplePropertyListener;
import org.fdesigner.databinding.property.value.SimpleValueProperty;
import org.fdesigner.runtime.common.runtime.IAdapterManager;

public final class AdaptedValueProperty<S, T> extends SimpleValueProperty<S, T> {
	private final Class<T> adapter;
	private final IAdapterManager adapterManager;

	public AdaptedValueProperty(Class<T> adapter, IAdapterManager adapterManager) {
		this.adapter = adapter;
		this.adapterManager = adapterManager;
	}

	@Override
	public Object getValueType() {
		return adapter;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected T doGetValue(S source) {
		if (adapter.isInstance(source))
			return (T) source;
		return adapterManager.getAdapter(source, adapter);
	}

	@Override
	protected void doSetValue(S source, T value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public INativePropertyListener<S> adaptListener(ISimplePropertyListener<S, ValueDiff<? extends T>> listener) {
		return null;
	}
}
