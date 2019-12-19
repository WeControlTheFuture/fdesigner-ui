/*******************************************************************************
 * Copyright (c) 2006, 2015 IBM Corporation and others.
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
 *     Remy Chi Jian Suen <remy.suen@gmail.com> - Bug 202170 [Wizards] Empy Source Folder and Package in New Class Wizard
 *******************************************************************************/
package org.fdesigner.workbench.internal.handlers;

import java.util.Map;

import org.eclipse.swt.widgets.Shell;
import org.fdesigner.commands.AbstractHandler;
import org.fdesigner.commands.ExecutionEvent;
import org.fdesigner.commands.ExecutionException;
import org.fdesigner.runtime.common.runtime.Adapters;
import org.fdesigner.runtime.common.runtime.CoreException;
import org.fdesigner.supplement.util.NLS;
import org.fdesigner.ui.jface.dialogs.IDialogSettings;
import org.fdesigner.ui.jface.viewers.ISelection;
import org.fdesigner.ui.jface.viewers.IStructuredSelection;
import org.fdesigner.ui.jface.viewers.StructuredSelection;
import org.fdesigner.ui.jface.wizard.WizardDialog;
import org.fdesigner.workbench.IEditorInput;
import org.fdesigner.workbench.IEditorPart;
import org.fdesigner.workbench.IWorkbenchCommandConstants;
import org.fdesigner.workbench.IWorkbenchPart;
import org.fdesigner.workbench.IWorkbenchWindow;
import org.fdesigner.workbench.IWorkbenchWizard;
import org.fdesigner.workbench.PlatformUI;
import org.fdesigner.workbench.commands.IElementUpdater;
import org.fdesigner.workbench.handlers.HandlerUtil;
import org.fdesigner.workbench.internal.IWorkbenchHelpContextIds;
import org.fdesigner.workbench.internal.LegacyResourceSupport;
import org.fdesigner.workbench.internal.WorkbenchMessages;
import org.fdesigner.workbench.internal.WorkbenchPlugin;
import org.fdesigner.workbench.internal.dialogs.ImportExportWizard;
import org.fdesigner.workbench.internal.dialogs.NewWizard;
import org.fdesigner.workbench.menus.UIElement;
import org.fdesigner.workbench.wizards.IWizardDescriptor;
import org.fdesigner.workbench.wizards.IWizardRegistry;

/**
 * Abstract handler for commands that launch the import, export and new wizards.
 * <p>
 * This class is only intended to be extended by the three inner classes
 * (<code>Export</code>, <code>Import</code> and <code>New</code>) defined here.
 * </p>
 *
 * @since 3.2
 */
public abstract class WizardHandler extends AbstractHandler implements IElementUpdater {

	/**
	 * Default handler for launching export wizards.
	 */
	public static final class Export extends WizardHandler {
		private static final int SIZING_WIZARD_WIDTH = 470;
		private static final int SIZING_WIZARD_HEIGHT = 550;

		@Override
		protected String getWizardIdParameterId() {
			return IWorkbenchCommandConstants.FILE_EXPORT_PARM_WIZARDID;
		}

		@Override
		protected IWizardRegistry getWizardRegistry() {
			return PlatformUI.getWorkbench().getExportWizardRegistry();
		}

		@Override
		protected void executeHandler(ExecutionEvent event) {
			IWorkbenchWindow activeWorkbenchWindow = HandlerUtil.getActiveWorkbenchWindow(event);
			if (activeWorkbenchWindow == null) {
				// action has been disposed
				return;
			}
			ImportExportWizard wizard = new ImportExportWizard(ImportExportWizard.EXPORT);
			IStructuredSelection selectionToPass = getSelectionToUse(event);

			wizard.init(activeWorkbenchWindow.getWorkbench(), selectionToPass);
			IDialogSettings workbenchSettings = WorkbenchPlugin.getDefault().getDialogSettings();
			IDialogSettings wizardSettings = workbenchSettings.getSection("ImportExportAction"); //$NON-NLS-1$
			if (wizardSettings == null) {
				wizardSettings = workbenchSettings.addNewSection("ImportExportAction"); //$NON-NLS-1$
			}
			wizard.setDialogSettings(wizardSettings);
			wizard.setForcePreviousAndNextButtons(true);

			Shell parent = activeWorkbenchWindow.getShell();
			WizardDialog dialog = new WizardDialog(parent, wizard);
			dialog.create();
			dialog.getShell().setSize(Math.max(SIZING_WIZARD_WIDTH, dialog.getShell().getSize().x),
					SIZING_WIZARD_HEIGHT);
			activeWorkbenchWindow.getWorkbench().getHelpSystem().setHelp(dialog.getShell(),
					IWorkbenchHelpContextIds.EXPORT_WIZARD);
			dialog.open();
		}

	}

	/**
	 * Default handler for launching import wizards.
	 */
	public static final class Import extends WizardHandler {
		private static final int SIZING_WIZARD_WIDTH = 470;
		private static final int SIZING_WIZARD_HEIGHT = 550;

		@Override
		protected String getWizardIdParameterId() {
			return IWorkbenchCommandConstants.FILE_IMPORT_PARM_WIZARDID;
		}

		@Override
		protected IWizardRegistry getWizardRegistry() {
			return PlatformUI.getWorkbench().getImportWizardRegistry();
		}

		@Override
		protected void executeHandler(ExecutionEvent event) {
			IWorkbenchWindow activeWorkbenchWindow = HandlerUtil.getActiveWorkbenchWindow(event);
			if (activeWorkbenchWindow == null) {
				// action has been disposed
				return;
			}
			ImportExportWizard wizard = new ImportExportWizard(ImportExportWizard.IMPORT);
			IStructuredSelection selectionToPass = getSelectionToUse(event);

			wizard.init(activeWorkbenchWindow.getWorkbench(), selectionToPass);
			IDialogSettings workbenchSettings = WorkbenchPlugin.getDefault().getDialogSettings();
			IDialogSettings wizardSettings = workbenchSettings.getSection("ImportExportAction"); //$NON-NLS-1$
			if (wizardSettings == null) {
				wizardSettings = workbenchSettings.addNewSection("ImportExportAction"); //$NON-NLS-1$
			}
			wizard.setDialogSettings(wizardSettings);
			wizard.setForcePreviousAndNextButtons(true);

			Shell parent = activeWorkbenchWindow.getShell();
			WizardDialog dialog = new WizardDialog(parent, wizard);
			dialog.create();
			dialog.getShell().setSize(Math.max(SIZING_WIZARD_WIDTH, dialog.getShell().getSize().x),
					SIZING_WIZARD_HEIGHT);
			activeWorkbenchWindow.getWorkbench().getHelpSystem().setHelp(dialog.getShell(),
					IWorkbenchHelpContextIds.IMPORT_WIZARD);
			dialog.open();
		}

	}

	/**
	 * Default handler for launching new wizards.
	 */
	public static final class New extends WizardHandler {

		/**
		 * The wizard dialog width
		 */
		private static final int SIZING_WIZARD_WIDTH = 500;

		/**
		 * The wizard dialog height
		 */
		private static final int SIZING_WIZARD_HEIGHT = 500;

		/**
		 * The id of the category to show or <code>null</code> to show all the
		 * categories.
		 */
		private String categoryId = null;

		@Override
		protected String getWizardIdParameterId() {
			return IWorkbenchCommandConstants.FILE_NEW_PARM_WIZARDID;
		}

		@Override
		protected IWizardRegistry getWizardRegistry() {
			return PlatformUI.getWorkbench().getNewWizardRegistry();
		}

		/**
		 * Returns the id of the category of wizards to show or <code>null</code> to
		 * show all categories.
		 *
		 * @return String
		 */
		public String getCategoryId() {
			return categoryId;
		}

		/**
		 * Sets the id of the category of wizards to show or <code>null</code> to show
		 * all categories.
		 *
		 * @param id
		 */
		public void setCategoryId(String id) {
			categoryId = id;
		}

		@Override
		protected IStructuredSelection getSelectionToUse(ExecutionEvent event) {
			ISelection selection = HandlerUtil.getCurrentSelection(event);
			IStructuredSelection selectionToPass = StructuredSelection.EMPTY;
			if (selection instanceof IStructuredSelection) {
				selectionToPass = (IStructuredSelection) selection;
			} else {
				// @issue the following is resource-specific legacy code
				// Build the selection from the IFile of the editor
				Class<?> resourceClass = LegacyResourceSupport.getResourceClass();
				if (resourceClass != null) {
					IWorkbenchWindow activeWorkbenchWindow = HandlerUtil.getActiveWorkbenchWindow(event);
					IWorkbenchPart part = activeWorkbenchWindow.getPartService().getActivePart();
					if (part instanceof IEditorPart) {
						IEditorInput input = ((IEditorPart) part).getEditorInput();
						Object resource = Adapters.adapt(input, resourceClass);
						if (resource != null) {
							selectionToPass = new StructuredSelection(resource);
						}
					}
				}
			}
			return selectionToPass;
		}

		@Override
		protected void executeHandler(ExecutionEvent event) {
			IWorkbenchWindow activeWorkbenchWindow = HandlerUtil.getActiveWorkbenchWindow(event);
			if (activeWorkbenchWindow == null) {
				// action has been disposed
				return;
			}
			NewWizard wizard = new NewWizard();
			wizard.setCategoryId(categoryId);

			IStructuredSelection selectionToPass = getSelectionToUse(event);
			wizard.init(activeWorkbenchWindow.getWorkbench(), selectionToPass);

			IDialogSettings workbenchSettings = WorkbenchPlugin.getDefault().getDialogSettings();
			IDialogSettings wizardSettings = workbenchSettings.getSection("NewWizardAction"); //$NON-NLS-1$
			if (wizardSettings == null) {
				wizardSettings = workbenchSettings.addNewSection("NewWizardAction"); //$NON-NLS-1$
			}
			wizard.setDialogSettings(wizardSettings);
			wizard.setForcePreviousAndNextButtons(true);

			Shell parent = activeWorkbenchWindow.getShell();
			WizardDialog dialog = new WizardDialog(parent, wizard);
			dialog.create();
			dialog.getShell().setSize(Math.max(SIZING_WIZARD_WIDTH, dialog.getShell().getSize().x),
					SIZING_WIZARD_HEIGHT);
			activeWorkbenchWindow.getWorkbench().getHelpSystem().setHelp(dialog.getShell(),
					IWorkbenchHelpContextIds.NEW_WIZARD);
			dialog.open();
		}

	}

	/**
	 * This is the execution of the handler to open a wizard dialog.
	 *
	 * @param event
	 */
	protected abstract void executeHandler(ExecutionEvent event);

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		String wizardId = event.getParameter(getWizardIdParameterId());

		IWorkbenchWindow activeWindow = HandlerUtil.getActiveWorkbenchWindowChecked(event);

		if (wizardId == null) {
			executeHandler(event);
		} else {

			IWizardRegistry wizardRegistry = getWizardRegistry();
			IWizardDescriptor wizardDescriptor = wizardRegistry.findWizard(wizardId);
			if (wizardDescriptor == null) {
				throw new ExecutionException("unknown wizard: " + wizardId); //$NON-NLS-1$
			}

			try {
				IWorkbenchWizard wizard = wizardDescriptor.createWizard();
				wizard.init(PlatformUI.getWorkbench(), getSelectionToUse(event));

				if (wizardDescriptor.canFinishEarly() && !wizardDescriptor.hasPages()) {
					wizard.performFinish();
					return null;
				}

				Shell parent = activeWindow.getShell();
				WizardDialog dialog = new WizardDialog(parent, wizard);
				dialog.create();
				dialog.open();

			} catch (CoreException ex) {
				throw new ExecutionException("error creating wizard", ex); //$NON-NLS-1$
			}

		}

		return null;
	}

	/**
	 * Returns a structured selection based on the event to initialize the wizard
	 * with.
	 *
	 * @param event the event object containing information about the current state
	 *              of the application
	 * @return the current structured selection of the application
	 */
	protected IStructuredSelection getSelectionToUse(ExecutionEvent event) {
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		if (selection instanceof IStructuredSelection) {
			return (IStructuredSelection) selection;
		}
		return StructuredSelection.EMPTY;
	}

	@Override
	public void updateElement(UIElement element, Map parameters) {

		String wizardId = (String) parameters.get(getWizardIdParameterId());
		if (wizardId == null)
			return;
		IWizardDescriptor wizard = getWizardRegistry().findWizard(wizardId);
		if (wizard != null) {
			element.setText(NLS.bind(WorkbenchMessages.WizardHandler_menuLabel, wizard.getLabel()));
			element.setTooltip(wizard.getDescription());
			element.setIcon(wizard.getImageDescriptor());
		}
	}

	/**
	 * Returns the id of the parameter used to indicate which wizard this command
	 * should launch.
	 *
	 * @return The id of the parameter used to indicate which wizard this command
	 *         should launch.
	 */
	protected abstract String getWizardIdParameterId();

	/**
	 * Returns the wizard registry for the concrete <code>WizardHandler</code>
	 * implementation class.
	 *
	 * @return The wizard registry for the concrete <code>WizardHandler</code>
	 *         implementation class.
	 */
	protected abstract IWizardRegistry getWizardRegistry();

}
