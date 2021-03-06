/*******************************************************************************
 * Copyright (c) 2007, 2017 IBM Corporation and others.
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
package org.fdesigner.p2.engine.internal.p2.engine;

import org.fdesigner.p2.metadata.IInstallableUnit;
import org.fdesigner.runtime.common.runtime.Assert;

/**
 * @since 2.0
 */
public class InstallableUnitOperand extends Operand {
	private final IInstallableUnit first;
	private final IInstallableUnit second;

	/**
	 * Creates a new operand that represents replacing an installable unit
	 * with another. At least one of the provided installable units must be
	 * non-null.
	 * 
	 * @param first The installable unit being removed, or <code>null</code>
	 * @param second The installable unit being added, or <code>null</code>
	 */
	public InstallableUnitOperand(IInstallableUnit first, IInstallableUnit second) {
		//the operand must have at least one non-null units
		Assert.isTrue(first != null || second != null);
		this.first = first;
		this.second = second;
	}

	public IInstallableUnit first() {
		return first;
	}

	public IInstallableUnit second() {
		return second;
	}

	@Override
	public String toString() {
		return first + " --> " + second; //$NON-NLS-1$
	}
}
