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

public class InstanceofExpression extends Expression {
	/**
	 * The seed for the hash code for all instance of expressions.
	 */
	private static final int HASH_INITIAL= InstanceofExpression.class.getName().hashCode();

	private String fTypeName;

	public InstanceofExpression(IConfigurationElement element) throws CoreException {
		fTypeName= element.getAttribute(ATT_VALUE);
		Expressions.checkAttribute(ATT_VALUE, fTypeName);
	}

	public InstanceofExpression(Element element) throws CoreException {
		fTypeName= element.getAttribute(ATT_VALUE);
		Expressions.checkAttribute(ATT_VALUE, fTypeName.isEmpty() ? null : fTypeName);
	}

	public InstanceofExpression(String typeName) {
		Assert.isNotNull(typeName);
		fTypeName= typeName;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.internal.corext.refactoring.participants.Expression#evaluate(java.lang.Object)
	 */
	@Override
	public EvaluationResult evaluate(IEvaluationContext context) {
		Object element= context.getDefaultVariable();
		return EvaluationResult.valueOf(Expressions.isInstanceOf(element, fTypeName));
	}

	@Override
	public void collectExpressionInfo(ExpressionInfo info) {
		info.markDefaultVariableAccessed();
	}

	@Override
	public boolean equals(final Object object) {
		if (!(object instanceof InstanceofExpression))
			return false;

		final InstanceofExpression that= (InstanceofExpression) object;
		return this.fTypeName.equals(that.fTypeName);
	}

	@Override
	protected int computeHashCode() {
		return HASH_INITIAL * HASH_FACTOR + fTypeName.hashCode();
	}

	//---- Debugging ---------------------------------------------------

	@Override
	public String toString() {
		return "<instanceof value=\"" + fTypeName + "\"/>"; //$NON-NLS-1$ //$NON-NLS-2$
	}
}
