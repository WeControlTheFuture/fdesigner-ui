/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
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
package org.fdesigner.expressions.internal.expressions;

import org.fdesigner.expressions.EvaluationResult;
import org.fdesigner.expressions.Expression;
import org.fdesigner.expressions.ExpressionInfo;
import org.fdesigner.expressions.IEvaluationContext;
import org.fdesigner.runtime.common.runtime.Assert;
import org.fdesigner.runtime.common.runtime.CoreException;

public class NotExpression extends Expression {
	/**
	 * The seed for the hash code for all not expressions.
	 */
	private static final int HASH_INITIAL= NotExpression.class.getName().hashCode();

	private Expression fExpression;

	public NotExpression(Expression expression) {
		Assert.isNotNull(expression);
		fExpression= expression;
	}

	@Override
	public EvaluationResult evaluate(IEvaluationContext context) throws CoreException {
		return fExpression.evaluate(context).not();
	}

	@Override
	public void collectExpressionInfo(ExpressionInfo info) {
		fExpression.collectExpressionInfo(info);
	}

	@Override
	public boolean equals(final Object object) {
		if (!(object instanceof NotExpression))
			return false;

		final NotExpression that= (NotExpression)object;
		return this.fExpression.equals(that.fExpression);
	}

	@Override
	protected int computeHashCode() {
		return HASH_INITIAL * HASH_FACTOR + fExpression.hashCode();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(getClass().getSimpleName());
		builder.append(" [expression="); //$NON-NLS-1$
		builder.append(fExpression);
		builder.append("]"); //$NON-NLS-1$
		return builder.toString();
	}
}
