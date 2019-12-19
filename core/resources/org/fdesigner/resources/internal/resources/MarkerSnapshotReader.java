/*******************************************************************************
 * Copyright (c) 2000, 2016 IBM Corporation and others.
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
 *     Mickael Istria (Red Hat Inc.) - Bug 488937
 *******************************************************************************/
package org.fdesigner.resources.internal.resources;

import java.io.DataInputStream;
import java.io.IOException;

import org.fdesigner.resources.internal.utils.Messages;
import org.fdesigner.runtime.common.runtime.CoreException;
import org.fdesigner.supplement.util.NLS;

public class MarkerSnapshotReader {
	protected Workspace workspace;

	public MarkerSnapshotReader(Workspace workspace) {
		super();
		this.workspace = workspace;
	}

	/**
	 * Returns the appropriate reader for the given version.
	 */
	protected MarkerSnapshotReader getReader(int formatVersion) throws IOException {
		switch (formatVersion) {
			case 1 :
				return new MarkerSnapshotReader_1(workspace);
			case 2 :
				return new MarkerSnapshotReader_2(workspace);
			default :
				throw new IOException(NLS.bind(Messages.resources_format, formatVersion));
		}
	}

	public void read(DataInputStream input) throws IOException, CoreException {
		int formatVersion = readVersionNumber(input);
		MarkerSnapshotReader reader = getReader(formatVersion);
		reader.read(input);
	}

	protected static int readVersionNumber(DataInputStream input) throws IOException {
		return input.readInt();
	}
}
