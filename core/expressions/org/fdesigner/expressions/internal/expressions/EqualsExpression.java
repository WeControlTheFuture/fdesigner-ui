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
import org.fdesigner.runtime.registry.runtime.IConfigurationElement;
import org.w3c.dom.Element;

public class EqualsExpression extends Expression {
	/**
	 * The seed for the hash code for all equals expressions.
	 */
	private static final int HASH_INITIAL= EqualsExpression.class.getName().hashCode();

	private Object fExpectedValue;

	public EqualsExpression(Object expectedValue) {
		Assert.isNotNull(expectedValue);
		fExpectedValue= expectedValue;
	}

	public EqualsExpression(IConfigurationElement element) throws CoreException {
		String value= element.getAttribute(ATT_VALUE);
		Expressions.checkAttribute(ATT_VALUE, value);
		fExpectedValue= Expressions.convertArgument(value);
	}

	public EqualsExpression(Element element) throws CoreException {
		String value= element.getAttribute(ATT_VALUE);
		Expressions.checkAttribute(ATT_VALUE, value.isEmpty() ? null : value);
		fExpectedValue= Expressions.convertArgument(value);
	}

	@Override
	public EvaluationResult evaluate(IEvaluationContext context) throws CoreException {
		Object element= context.getDefaultVariable();
		return EvaluationResult.valueOf(element.equals(fExpectedValue));
	}

	@Override
	public void collectExpressionInfo(ExpressionInfo info) {
		info.markDefaultVariableAccessed();
	}

	@Override
	public boolean equals(final Object object) {
		if (!(object instanceof EqualsExpression))
			return false;

		final EqualsExpression that= (EqualsExpression)object;
		return this.fExpectedValue.equals(that.fExpectedValue);
	}

	@Override
	protected int computeHashCode() {
		return HASH_INITIAL * HASH_FACTOR + fExpectedValue.hashCode();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(getClass().getSimpleName());
		builder.append(" [expected="); //$NON-NLS-1$
		builder.append(fExpectedValue);
		builder.append("]"); //$NON-NLS-1$
		return builder.toString();
	}
}
