/*******************************************************************************
 * Copyright (c) 2015 IBM Corporation and others.
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

package org.fdesigner.workbench.internal.e4.migration;

import java.io.IOException;
import java.io.StringWriter;

import org.fdesigner.workbench.IMemento;
import org.fdesigner.workbench.XMLMemento;
import org.fdesigner.workbench.internal.WorkbenchPlugin;

public class MementoSerializer {

	private IMemento memento;

	MementoSerializer(IMemento memento) {
		this.memento = memento;
	}

	String serialize() {
		if (!(memento instanceof XMLMemento)) {
			return null;
		}
		StringWriter writer = new StringWriter();
		try {
			((XMLMemento) memento).save(writer);
		} catch (IOException e) {
			WorkbenchPlugin.log(e);
		}
		return writer.toString();
	}
}
