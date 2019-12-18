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
package org.fdesigner.workbench.internal.actions;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Menu;
import org.fdesigner.ui.jface.action.Action;
import org.fdesigner.ui.jface.action.ActionContributionItem;
import org.fdesigner.ui.jface.action.IAction;
import org.fdesigner.ui.jface.action.Separator;
import org.fdesigner.ui.jface.bindings.keys.IKeyLookup;
import org.fdesigner.ui.jface.bindings.keys.KeyLookupFactory;
import org.fdesigner.ui.jface.window.Window;
import org.fdesigner.workbench.IWorkbenchWindow;
import org.fdesigner.workbench.IWorkingSet;
import org.fdesigner.workbench.internal.WorkbenchMessages;
import org.fdesigner.workbench.internal.dialogs.SimpleWorkingSetSelectionDialog;

/**
 * Action to select the visible working sets for a given workbench page.
 *
 * @since 3.2
 */
public class SelectWorkingSetsAction extends AbstractWorkingSetPulldownDelegate {

	private class ManageWorkingSetsAction extends Action {

		ManageWorkingSetsAction() {
			super(WorkbenchMessages.Edit);
		}

		@Override
		public void run() {
			SelectWorkingSetsAction.this.run(this);
		}
	}

	private class ToggleWorkingSetAction extends Action {
		private IWorkingSet set;

		ToggleWorkingSetAction(IWorkingSet set) {
			super(set.getLabel(), IAction.AS_CHECK_BOX);
			setImageDescriptor(set.getImageDescriptor());
			this.set = set;
			setChecked(isWorkingSetEnabled(set));
		}

		@Override
		public void runWithEvent(Event event) {

			Set newList = new HashSet(Arrays.asList(getWindow().getActivePage().getWorkingSets()));

			if (isChecked()) {
				// if the primary modifier key is down then clear the list
				// first. this makes the selection exclusive rather than
				// additive.
				boolean modified = (event.stateMask
						& KeyLookupFactory.getDefault().formalModifierLookup(IKeyLookup.M1_NAME)) != 0;

				if (modified)
					newList.clear();
				newList.add(set);
			} else {
				newList.remove(set);
			}

			getWindow().getActivePage()
					.setWorkingSets((IWorkingSet[]) newList.toArray(new IWorkingSet[newList.size()]));
		}
	}

	@Override
	protected void fillMenu(Menu menu) {
		IWorkingSet[][] typedSets = splitSets();

		for (IWorkingSet[] sets : typedSets) {
			for (IWorkingSet set : sets) {
				// only add visible sets
				// if (set.isVisible()) {
				ActionContributionItem item = new ActionContributionItem(new ToggleWorkingSetAction(set));
				item.fill(menu, -1);
				// }
			}
			Separator separator = new Separator();
			separator.fill(menu, -1);
		}

		ActionContributionItem item = new ActionContributionItem(new ManageWorkingSetsAction());
		item.fill(menu, -1);

	}

	private IWorkingSet[] getEnabledSets() {
		return getWindow().getActivePage().getWorkingSets();
	}

	private boolean isWorkingSetEnabled(IWorkingSet set) {
		IWorkingSet[] enabledSets = getEnabledSets();
		for (IWorkingSet enabledSet : enabledSets) {
			if (enabledSet.equals(set)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void run(IAction action) {
		ConfigureWindowWorkingSetsDialog dialog = new ConfigureWindowWorkingSetsDialog(getWindow());
		if (dialog.open() == Window.OK) {

		}

	}
}

class ConfigureWindowWorkingSetsDialog extends SimpleWorkingSetSelectionDialog {

	private IWorkbenchWindow window;

	protected ConfigureWindowWorkingSetsDialog(IWorkbenchWindow window) {
		super(window.getShell(), null, window.getActivePage().getWorkingSets(), true);
		this.window = window;
		setTitle(WorkbenchMessages.WorkingSetSelectionDialog_title_multiSelect);
		setMessage(WorkbenchMessages.WorkingSetSelectionDialog_message_multiSelect);
	}

	@Override
	protected void okPressed() {
		super.okPressed();
		window.getActivePage().setWorkingSets(getSelection());
	}
}
