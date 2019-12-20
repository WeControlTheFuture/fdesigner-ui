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

package org.fdesigner.navigator.internal.navigator.dnd;

import org.fdesigner.expressions.EvaluationResult;
import org.fdesigner.expressions.Expression;
import org.fdesigner.expressions.IEvaluationContext;
import org.fdesigner.navigator.CommonDropAdapterAssistant;
import org.fdesigner.navigator.INavigatorContentDescriptor;
import org.fdesigner.navigator.internal.navigator.CustomAndExpression;
import org.fdesigner.navigator.internal.navigator.NavigatorPlugin;
import org.fdesigner.navigator.internal.navigator.NavigatorSafeRunnable;
import org.fdesigner.navigator.internal.navigator.extensions.INavigatorContentExtPtConstants;
import org.fdesigner.runtime.common.runtime.SafeRunner;
import org.fdesigner.runtime.registry.runtime.IConfigurationElement;
import org.fdesigner.ui.jface.viewers.IStructuredSelection;

/**
 * @since 3.2
 *
 */
public final class CommonDropAdapterDescriptor implements
		INavigatorContentExtPtConstants {

	private final IConfigurationElement element;

	private final INavigatorContentDescriptor contentDescriptor;

	private Expression dropExpr;

	/* package */CommonDropAdapterDescriptor(
			IConfigurationElement aConfigElement,
			INavigatorContentDescriptor aContentDescriptor) {
		element = aConfigElement;
		contentDescriptor = aContentDescriptor;
		init();
	}

	private void init() {
		IConfigurationElement[] children = element.getChildren(TAG_POSSIBLE_DROP_TARGETS);
		if (children.length == 1) {
			dropExpr = new CustomAndExpression(children[0]);
		}
	}

	/**
	 *
	 * @param anElement
	 *            The element from the set of elements being dragged.
	 * @return True if the element matches the drag expression from the
	 *         extension.
	 */
	public boolean isDragElementSupported(Object anElement) {
		return contentDescriptor.isPossibleChild(anElement);
	}

	/**
	 *
	 * @param aSelection
	 *            The set of elements being dragged.
	 * @return True if the element matches the drag expression from the
	 *         extension.
	 */
	public boolean areDragElementsSupported(IStructuredSelection aSelection) {
		if (aSelection.isEmpty()) {
			return false;
		}
		return contentDescriptor.arePossibleChildren(aSelection);
	}

	/**
	 *
	 * @param anElement
	 *            The element from the set of elements benig dropped.
	 * @return True if the element matches the drop expression from the
	 *         extension.
	 */
	public boolean isDropElementSupported(Object anElement) {
		if (dropExpr != null && anElement != null) {
			IEvaluationContext context = NavigatorPlugin.getEvalContext(anElement);
			return NavigatorPlugin.safeEvaluate(dropExpr, context) == EvaluationResult.TRUE;
		}
		return false;
	}

	/**
	 *
	 * @return An instance of {@link CommonDropAdapterAssistant} from the
	 *         descriptor or {@link SkeletonCommonDropAssistant}.
	 */
	public CommonDropAdapterAssistant createDropAssistant() {
		final CommonDropAdapterAssistant[] retValue = new CommonDropAdapterAssistant[1];
		SafeRunner.run(new NavigatorSafeRunnable(element) {
			@Override
			public void run() throws Exception {
				retValue[0] = (CommonDropAdapterAssistant) element
						.createExecutableExtension(ATT_CLASS);
			}
		});
		if (retValue[0] != null)
			return retValue[0];
		return SkeletonCommonDropAssistant.INSTANCE;
	}

	/**
	 *
	 * @return The content descriptor that contains this drop descriptor.
	 */
	public INavigatorContentDescriptor getContentDescriptor() {
		return contentDescriptor;
	}

}
