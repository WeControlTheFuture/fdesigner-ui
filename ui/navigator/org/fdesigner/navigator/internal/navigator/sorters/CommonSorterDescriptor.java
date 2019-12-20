/*******************************************************************************
 * Copyright (c) 2006, 2015 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/

package org.fdesigner.navigator.internal.navigator.sorters;

import org.fdesigner.expressions.EvaluationResult;
import org.fdesigner.expressions.Expression;
import org.fdesigner.expressions.IEvaluationContext;
import org.fdesigner.navigator.internal.navigator.CustomAndExpression;
import org.fdesigner.navigator.internal.navigator.NavigatorPlugin;
import org.fdesigner.navigator.internal.navigator.NavigatorSafeRunnable;
import org.fdesigner.navigator.internal.navigator.extensions.INavigatorContentExtPtConstants;
import org.fdesigner.runtime.common.runtime.CoreException;
import org.fdesigner.runtime.common.runtime.SafeRunner;
import org.fdesigner.runtime.registry.runtime.IConfigurationElement;
import org.fdesigner.ui.jface.viewers.Viewer;
import org.fdesigner.ui.jface.viewers.ViewerComparator;
import org.fdesigner.ui.jface.viewers.ViewerSorter;

/**
 *
 * Describes a <b>commonSorter</b> element under a
 * <b>org.eclipse.ui.navigator.navigatorContent</b> extension.
 *
 * @since 3.2
 */
public class CommonSorterDescriptor implements INavigatorContentExtPtConstants {

	private IConfigurationElement element;

	private Expression parentExpression;

	private String id;

	protected CommonSorterDescriptor(IConfigurationElement anElement) {
		element = anElement;
		init();
	}

	private void init() {
		id = element.getAttribute(ATT_ID);
		if (id == null) {
			id = ""; //$NON-NLS-1$
		}
		IConfigurationElement[] children = element
				.getChildren(TAG_PARENT_EXPRESSION);
		if (children.length == 1) {
			parentExpression = new CustomAndExpression(children[0]);
		}
	}

	/**
	 *
	 * @return An identifier used to determine whether the sorter is visible.
	 *         May not be unique.
	 */
	public String getId() {
		return id;
	}

	/**
	 *
	 * @param aParent
	 *            An element from the viewer
	 * @return True if and only if this CommonSorter can sort the children of
	 *         the given parent.
	 */
	public boolean isEnabledForParent(Object aParent) {
		if(aParent == null) {
			return false;
		}

		if (parentExpression != null) {
			IEvaluationContext context = NavigatorPlugin.getEvalContext(aParent);
			return NavigatorPlugin.safeEvaluate(parentExpression, context) == EvaluationResult.TRUE;
		}
		return true;
	}

	/**
	 *
	 * @return An instance of the ViewerSorter defined by the extension. Callers
	 *         of this method are responsible for managing the instantiated
	 *         filter.
	 */
	public ViewerSorter createSorter() {
		final ViewerSorter[] sorter = new ViewerSorter[1];

		SafeRunner.run(new NavigatorSafeRunnable(element) {
			@Override
			public void run() throws Exception {
				sorter[0] = createSorterInstance();
			}
		});
		if (sorter[0] != null)
			return sorter[0];
		return SkeletonViewerSorter.INSTANCE;
	}

	private ViewerSorter createSorterInstance() throws CoreException {
		Object contributed = element.createExecutableExtension(ATT_CLASS);
		if (contributed instanceof ViewerSorter) {
			return (ViewerSorter) contributed;
		}
		if (contributed instanceof ViewerComparator) {
			return new WrappedViewerComparator((ViewerComparator) contributed);
		}
		throw new ClassCastException("Class contributed by " + element.getNamespaceIdentifier() + //$NON-NLS-1$
				" to " + INavigatorContentExtPtConstants.TAG_NAVIGATOR_CONTENT + //$NON-NLS-1$
				"/" + INavigatorContentExtPtConstants.TAG_COMMON_SORTER //$NON-NLS-1$
				+ " is not an instance of " + ViewerComparator.class.getName() + ": " + contributed.getClass().getName() //$NON-NLS-1$ //$NON-NLS-2$
		);
	}

	/**
	 * Public for tests only.
	 */
	public static class WrappedViewerComparator extends ViewerSorter {

		private final ViewerComparator comparator;

		public WrappedViewerComparator(ViewerComparator comparator) {
			this.comparator = comparator;
		}

		@Override
		public int category(Object element) {
			return comparator.category(element);
		}

		@Override
		public int compare(Viewer viewer, Object e1, Object e2) {
			return comparator.compare(viewer, e1, e2);
		}

		@Override
		public boolean isSorterProperty(Object element, String property) {
			return comparator.isSorterProperty(element, property);
		}

		/**
		 * Public for tests only.
		 *
		 * @return Returns the original comparator instance wrapped by this
		 *         instance.
		 */
		public ViewerComparator getWrappedComparator() {
			return comparator;
		}
	}

	@Override
	public String toString() {
		return "CommonSorterDescriptor[" + getId() + "]"; //$NON-NLS-1$//$NON-NLS-2$
	}
}
