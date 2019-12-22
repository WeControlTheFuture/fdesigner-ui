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

import org.fdesigner.p2.engine.ISizingPhaseSet;
import org.fdesigner.p2.engine.internal.p2.engine.phases.Sizing;

public class SizingPhaseSet extends PhaseSet implements ISizingPhaseSet {

	private static Sizing sizing;

	public SizingPhaseSet() {
		super(new Phase[] {sizing = new Sizing(100)});
	}

	@Override
	public long getDiskSize() {
		return sizing.getDiskSize();
	}

	@Override
	public long getDownloadSize() {
		return sizing.getDownloadSize();
	}
}
