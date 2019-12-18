/*******************************************************************************
 * Copyright (c) 2006, 2014 IBM Corporation and others.
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
 *******************************************************************************/
package org.fdesigner.workbench.internal.expressions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.fdesigner.expressions.EvaluationResult;
import org.fdesigner.expressions.Expression;
import org.fdesigner.expressions.ExpressionInfo;
import org.fdesigner.expressions.IEvaluationContext;
import org.fdesigner.runtime.common.runtime.CoreException;

/**
 * Copied from org.eclipse.core.internal.expressions.
 */
public abstract class CompositeExpression extends Expression {

	private static final Expression[] EMPTY_ARRAY = new Expression[0];

	protected List<Expression> fExpressions;

	public void add(Expression expression) {
		if (fExpressions == null) {
			fExpressions = new ArrayList<>(2);
		}
		fExpressions.add(expression);
	}

	public Expression[] getChildren() {
		if (fExpressions == null) {
			return EMPTY_ARRAY;
		}
		return fExpressions.toArray(new Expression[fExpressions.size()]);
	}

	protected EvaluationResult evaluateAnd(IEvaluationContext scope) throws CoreException {
		if (fExpressions == null) {
			return EvaluationResult.TRUE;
		}
		EvaluationResult result = EvaluationResult.TRUE;
		for (Iterator<Expression> iter = fExpressions.iterator(); iter.hasNext();) {
			Expression expression = iter.next();
			result = result.and(expression.evaluate(scope));
			// keep iterating even if we have a not loaded found. It can be
			// that we find a false which will result in a better result.
			if (result == EvaluationResult.FALSE) {
				return result;
			}
		}
		return result;
	}

	protected EvaluationResult evaluateOr(IEvaluationContext scope) throws CoreException {
		if (fExpressions == null) {
			return EvaluationResult.TRUE;
		}
		EvaluationResult result = EvaluationResult.FALSE;
		for (Iterator<Expression> iter = fExpressions.iterator(); iter.hasNext();) {
			Expression expression = iter.next();
			result = result.or(expression.evaluate(scope));
			if (result == EvaluationResult.TRUE) {
				return result;
			}
		}
		return result;
	}

	@Override
	public void collectExpressionInfo(ExpressionInfo info) {
		if (fExpressions == null) {
			return;
		}
		for (Iterator<Expression> iter = fExpressions.iterator(); iter.hasNext();) {
			Expression expression = iter.next();
			expression.collectExpressionInfo(info);
		}
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(getClass().getSimpleName());
		Expression[] children = getChildren();
		if (children.length > 0) {
			builder.append(" [children="); //$NON-NLS-1$
			builder.append(Arrays.toString(children));
			builder.append("]"); //$NON-NLS-1$
		}
		return builder.toString();
	}

}
