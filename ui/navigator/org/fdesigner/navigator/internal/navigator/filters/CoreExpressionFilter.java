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

package org.fdesigner.navigator.internal.navigator.filters;

import org.fdesigner.expressions.EvaluationResult;
import org.fdesigner.expressions.Expression;
import org.fdesigner.expressions.IEvaluationContext;
import org.fdesigner.navigator.internal.navigator.NavigatorPlugin;
import org.fdesigner.ui.jface.viewers.Viewer;
import org.fdesigner.ui.jface.viewers.ViewerFilter;

/**
 * @since 3.2
 *
 */
public class CoreExpressionFilter extends ViewerFilter {

	private Expression filterExpression;

	/**
	 * Creates a filter which hides all elements that match the given
	 * expression.
	 *
	 * @param aFilterExpression
	 *            An expression to hide elements in the viewer.
	 */
	public CoreExpressionFilter(Expression aFilterExpression) {
		filterExpression = aFilterExpression;
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {

		IEvaluationContext context = NavigatorPlugin.getEvalContext(element);
		return NavigatorPlugin.safeEvaluate(filterExpression, context) != EvaluationResult.TRUE;
	}

}
