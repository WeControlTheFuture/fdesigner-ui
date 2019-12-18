/*******************************************************************************
 * Copyright (c) 2000, 2014 IBM Corporation and others.
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

import org.fdesigner.expressions.EvaluationResult;
import org.fdesigner.expressions.IEvaluationContext;
import org.fdesigner.runtime.common.runtime.CoreException;

/**
 * Copied from org.eclipse.core.internal.expressions.
 */
public final class AndExpression extends CompositeExpression {

	/**
	 * The seed for the hash code for all schemes.
	 */
	private static final int HASH_INITIAL = AndExpression.class.getName().hashCode();

	@Override
	protected int computeHashCode() {
		return HASH_INITIAL * HASH_FACTOR + hashCode(fExpressions);
	}

	@Override
	public boolean equals(final Object object) {
		if (object instanceof AndExpression) {
			final AndExpression that = (AndExpression) object;
			return equals(this.fExpressions, that.fExpressions);
		}

		return false;
	}

	@Override
	public EvaluationResult evaluate(final IEvaluationContext context) throws CoreException {
		return evaluateAnd(context);
	}

}
