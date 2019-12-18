/*******************************************************************************
 * Copyright (c) 2008, 2015 IBM Corporation and others.
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

import org.fdesigner.workbench.services.AbstractServiceFactory;
import org.fdesigner.workbench.services.IEvaluationService;
import org.fdesigner.workbench.services.IServiceLocator;

/**
 * @since 3.4
 *
 */
public class EvaluationServiceFactory extends AbstractServiceFactory {

	@Override
	public Object create(Class serviceInterface, IServiceLocator parentLocator, IServiceLocator locator) {
		if (!IEvaluationService.class.equals(serviceInterface)) {
			return null;
		}
		Object parent = parentLocator.getService(serviceInterface);
		if (parent == null) {
			return null;
		}
		return new SlaveEvaluationService((IEvaluationService) parent);
	}

}
