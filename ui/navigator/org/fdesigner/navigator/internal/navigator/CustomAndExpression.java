/*******************************************************************************
 * Copyright (c) 2005, 2015 IBM Corporation and others.
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
package org.fdesigner.navigator.internal.navigator;

import java.util.ArrayList;
import java.util.List;

import org.fdesigner.expressions.ElementHandler;
import org.fdesigner.expressions.EvaluationResult;
import org.fdesigner.expressions.Expression;
import org.fdesigner.expressions.ExpressionConverter;
import org.fdesigner.expressions.IEvaluationContext;
import org.fdesigner.runtime.common.runtime.Assert;
import org.fdesigner.runtime.common.runtime.SafeRunner;
import org.fdesigner.runtime.registry.runtime.IConfigurationElement;

/**
 * Create an AND-type core expression from an IConfigurationElement of arbitrary
 * name.
 *
 */
public class CustomAndExpression extends Expression {

	protected List<Expression> fExpressions;

	/**
	 * Create an AND-type core expression from an IConfigurationElement of
	 * arbitrary name. The children elements are combined using boolean AND
	 * semantics to evaluate the expression.
	 *
	 * @param element
	 *            An IConfigurationElement of arbitrary name.
	 */
	public CustomAndExpression(IConfigurationElement element) {
		Assert.isNotNull(element);

		final IConfigurationElement[] children = element.getChildren();
		if (children.length == 0)
			return;
		SafeRunner.run(new NavigatorSafeRunnable() {
			@Override
			public void run() throws Exception {
				fExpressions = new ArrayList<Expression>();
				for (IConfigurationElement configurationElement : children) {
					fExpressions.add(ElementHandler.getDefault().create(
							ExpressionConverter.getDefault(), configurationElement));
				}
			}
		});

	}

	@Override
	public EvaluationResult evaluate(IEvaluationContext scope) {
		if (fExpressions == null) {
			return EvaluationResult.TRUE;
		}
		NavigatorPlugin.Evaluator evaluator = new NavigatorPlugin.Evaluator();
		EvaluationResult result = EvaluationResult.TRUE;
		for (Expression expression : fExpressions) {
			evaluator.expression = expression;
			evaluator.scope = scope;
			SafeRunner.run(evaluator);
			result = result.and(evaluator.result);
			// keep iterating even if we have a not loaded found. It can be
			// that we find a false which will result in a better result.
			if (result == EvaluationResult.FALSE) {
				return result;
			}
		}
		return result;
	}

}
