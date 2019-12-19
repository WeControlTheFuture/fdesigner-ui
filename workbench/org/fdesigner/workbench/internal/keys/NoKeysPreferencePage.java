/*******************************************************************************
 * Copyright (c) 2010, 2014 IBM Corporation and others.
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

package org.fdesigner.workbench.internal.keys;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.fdesigner.ui.jface.preference.PreferencePage;
import org.fdesigner.workbench.IWorkbench;
import org.fdesigner.workbench.IWorkbenchPreferencePage;

public class NoKeysPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

	@Override
	public void init(IWorkbench workbench) {
		// do nothing, we don't have content
	}

	@Override
	protected Control createContents(Composite parent) {
		Label info = new Label(parent, SWT.NONE);
		info.setText(
				"Custom key preferences are not available at this time.\nPlease create key bindings through the model."); //$NON-NLS-1$
		return info;
	}

}
