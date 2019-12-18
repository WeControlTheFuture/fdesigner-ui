/*******************************************************************************
 * Copyright (c) 2007, 2015 IBM Corporation and others.
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

package org.fdesigner.workbench.internal.services;

import org.fdesigner.e4.core.commands.ExpressionContext;
import org.fdesigner.e4.core.contexts.IEclipseContext;
import org.fdesigner.e4.core.contexts.RunAndTrack;
import org.fdesigner.e4.ui.workbench.internal.workbench.Activator;
import org.fdesigner.expressions.EvaluationResult;
import org.fdesigner.expressions.Expression;
import org.fdesigner.expressions.IEvaluationContext;
import org.fdesigner.runtime.common.runtime.CoreException;
import org.fdesigner.services.log.LogService;
import org.fdesigner.ui.jface.util.IPropertyChangeListener;
import org.fdesigner.ui.jface.util.PropertyChangeEvent;
import org.fdesigner.workbench.services.IEvaluationReference;

/**
 * @since 3.3
 *
 */
public class EvaluationReference extends RunAndTrack implements IEvaluationReference {
	final IEclipseContext context;
	final Expression expression;
	final IPropertyChangeListener listener;
	final String property;
	final int sourcePriority;
	boolean cache;
	boolean participating = true;
	boolean postingChanges = true;
	boolean hasRun = false;

	public EvaluationReference(IEclipseContext context, Expression expression, IPropertyChangeListener listener,
			String property) {
		this.context = context;
		this.expression = expression;
		this.listener = listener;
		this.property = property;
		this.sourcePriority = SourcePriorityNameMapping.computeSourcePriority(expression);
	}

	@Override
	public void clearResult() {
	}

	@Override
	public Expression getExpression() {
		return expression;
	}

	@Override
	public int getSourcePriority() {
		return sourcePriority;
	}

	@Override
	public boolean evaluate(IEvaluationContext context) {
		if (expression == null) {
			cache = true;
		} else {
			try {
				cache = expression.evaluate(context) != EvaluationResult.FALSE;
			} catch (CoreException e) {
				Activator.log(LogService.LOG_ERROR, "Failed to evaluate: " + expression, e); //$NON-NLS-1$
				return false;
			}
		}
		return cache;
	}

	@Override
	public void setResult(boolean result) {
		cache = result;
	}

	@Override
	public boolean changed(IEclipseContext context) {
		if (!participating) {
			return false;
		}

		evaluate();
		return participating;
	}

	public void evaluate() {
		boolean value = cache;
		evaluate(new ExpressionContext(context));
		if (!postingChanges) {
			return;
		}
		if (!hasRun) {
			getListener().propertyChange(new PropertyChangeEvent(this, getProperty(), null, Boolean.valueOf(cache)));
		} else if (!participating) {
			getListener().propertyChange(new PropertyChangeEvent(this, getProperty(), Boolean.valueOf(value), null));
		}
		if (value != cache) {
			getListener().propertyChange(
					new PropertyChangeEvent(this, getProperty(), Boolean.valueOf(value), Boolean.valueOf(cache)));
		}
		hasRun = true;
	}

	@Override
	public IPropertyChangeListener getListener() {
		return listener;
	}

	@Override
	public String getProperty() {
		return property;
	}

	public void setPostingChanges(boolean b) {
		postingChanges = b;
	}

	public boolean isPostingChanges() {
		return postingChanges;
	}
}
