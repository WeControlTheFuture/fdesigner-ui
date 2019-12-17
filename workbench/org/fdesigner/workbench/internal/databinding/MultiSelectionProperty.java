package org.fdesigner.workbench.internal.databinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.fdesigner.databinding.observable.list.ListDiff;
import org.fdesigner.databinding.property.INativePropertyListener;
import org.fdesigner.databinding.property.ISimplePropertyListener;
import org.fdesigner.databinding.property.list.SimpleListProperty;
import org.fdesigner.ui.jface.viewers.ISelection;
import org.fdesigner.ui.jface.viewers.IStructuredSelection;
import org.fdesigner.workbench.ISelectionService;

public class MultiSelectionProperty<S extends ISelectionService, E> extends SimpleListProperty<S, E> {
	private final String partId;
	private final boolean post;
	private final Object elementType;

	public MultiSelectionProperty(String partId, boolean post, Object elementType) {
		this.partId = partId;
		this.post = post;
		this.elementType = elementType;
	}

	@Override
	public INativePropertyListener<S> adaptListener(ISimplePropertyListener<S, ListDiff<E>> listener) {
		return new SelectionServiceListener<>(this, listener, partId, post);
	}

	@Override
	public Object getElementType() {
		return elementType;
	}

	@Override
	protected List<E> doGetList(S source) {
		ISelection selection;
		if (partId != null) {
			selection = ((ISelectionService) source).getSelection(partId);
		} else {
			selection = ((ISelectionService) source).getSelection();
		}
		if (selection instanceof IStructuredSelection) {
			List<E> list = ((IStructuredSelection) selection).toList();
			return new ArrayList<>(list);
		}
		return Collections.emptyList();
	}

	@Override
	protected void doSetList(S source, List<E> list, ListDiff<E> diff) {
		throw new UnsupportedOperationException();
	}
}
