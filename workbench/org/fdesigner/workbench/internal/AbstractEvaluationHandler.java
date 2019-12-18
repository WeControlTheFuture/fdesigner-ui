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
 *     Lars Vogel <Lars.Vogel@gmail.com> - Bug 440810
 ******************************************************************************/

package org.fdesigner.workbench.internal;

import org.fdesigner.expressions.Expression;
import org.fdesigner.ui.jface.util.IPropertyChangeListener;
import org.fdesigner.workbench.PlatformUI;
import org.fdesigner.workbench.services.IEvaluationReference;
import org.fdesigner.workbench.services.IEvaluationService;

/**
 * This internal class serves as a foundation for any handler that would like
 * its enabled state controlled by core expressions and the IEvaluationService.
 *
 * @since 3.3
 */
public abstract class AbstractEvaluationHandler extends AbstractEnabledHandler {
	private static final String PROP_ENABLED = "enabled"; //$NON-NLS-1$
	private IEvaluationService evaluationService;
	private IPropertyChangeListener enablementListener;
	private IEvaluationReference enablementRef;

	protected IEvaluationService getEvaluationService() {
		if (evaluationService == null) {
			evaluationService = PlatformUI.getWorkbench().getService(IEvaluationService.class);
		}
		return evaluationService;
	}

	protected void registerEnablement() {
		enablementRef = getEvaluationService().addEvaluationListener(getEnabledWhenExpression(),
				getEnablementListener(), PROP_ENABLED);
	}

	protected abstract Expression getEnabledWhenExpression();

	/**
	 * @return
	 */
	private IPropertyChangeListener getEnablementListener() {
		if (enablementListener == null) {
			enablementListener = event -> {
				if (event.getProperty() == PROP_ENABLED) {
					if (event.getNewValue() instanceof Boolean) {
						setEnabled(((Boolean) event.getNewValue()).booleanValue());
					} else {
						setEnabled(false);
					}
				}
			};
		}
		return enablementListener;
	}

	@Override
	public void dispose() {
		if (enablementRef != null) {
			evaluationService.removeEvaluationListener(enablementRef);
			enablementRef = null;
			enablementListener = null;
			evaluationService = null;
		}
		super.dispose();
	}
}
