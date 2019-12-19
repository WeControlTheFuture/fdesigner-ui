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

package org.fdesigner.workbench.internal.model;

import org.fdesigner.workbench.application.WorkbenchAdvisor;
import org.fdesigner.workbench.model.ContributionComparator;
import org.fdesigner.workbench.model.IContributionService;

public class ContributionService implements IContributionService {

	private WorkbenchAdvisor advisor;

	/**
	 * @param advisor
	 */
	public ContributionService(WorkbenchAdvisor advisor) {
		this.advisor = advisor;
	}

	@Override
	public ContributionComparator getComparatorFor(String contributionType) {
		return advisor.getComparatorFor(contributionType);
	}
}