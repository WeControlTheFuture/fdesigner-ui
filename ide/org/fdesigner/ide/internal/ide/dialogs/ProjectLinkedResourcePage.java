/*******************************************************************************
 * Copyright (c) 2000, 2015 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Serge Beauchamp (Freescale Semiconductor) - initial API and implementation
 *******************************************************************************/
package org.fdesigner.ide.internal.ide.dialogs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.fdesigner.ide.internal.ide.IDEWorkbenchMessages;
import org.fdesigner.ide.internal.ide.IIDEHelpContextIds;
import org.fdesigner.resources.IProject;
import org.fdesigner.resources.IResource;
import org.fdesigner.runtime.common.runtime.Adapters;
import org.fdesigner.runtime.common.runtime.IAdaptable;
import org.fdesigner.ui.jface.preference.PreferencePage;
import org.fdesigner.workbench.IWorkbench;
import org.fdesigner.workbench.IWorkbenchPreferencePage;
import org.fdesigner.workbench.IWorkbenchPropertyPage;
import org.fdesigner.workbench.PlatformUI;
import org.fdesigner.workbench.dialogs.PropertyPage;

/**
 * A property page for viewing and modifying the set
 * of path variables in a given project.
 * @since 3.4
 */
public class ProjectLinkedResourcePage extends PropertyPage implements
		IWorkbenchPropertyPage {

	private Label topLabel;

	private PathVariablesGroup pathVariablesGroup;
	private LinkedResourceEditor linkedResourceEditor;

	/**
	 *
	 */
	public ProjectLinkedResourcePage() {
		pathVariablesGroup = new PathVariablesGroup(true, IResource.FILE | IResource.FOLDER);
		linkedResourceEditor = new LinkedResourceEditor();
		this.noDefaultAndApplyButton();
	}

	@Override
	protected Control createContents(Composite parent) {

		PlatformUI.getWorkbench().getHelpSystem().setHelp(parent,
				IIDEHelpContextIds.LINKED_RESOURCE_PAGE);

		IAdaptable adaptable = getElement();
		IProject project = Adapters.adapt(adaptable, IProject.class);
		if (project != null) {
			pathVariablesGroup.setResource(project);
			linkedResourceEditor.setProject(project);
		}

		Font font = parent.getFont();

		// PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, IIDEHelpContextIds.LINKED_RESOURCE_PREFERENCE_PAGE);
		// define container & its gridding
		Composite pageComponent = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		pageComponent.setLayout(layout);
		GridData data = new GridData();
		data.verticalAlignment = GridData.FILL;
		data.horizontalAlignment = GridData.FILL;
		pageComponent.setLayoutData(data);
		pageComponent.setFont(font);

		// createSpace(pageComponent);

		TabFolder tabFolder = new TabFolder(pageComponent, SWT.TOP);
		tabFolder.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TabFolder source = (TabFolder) e.getSource();
				if (source.getSelectionIndex() == 1)
					switchToLinkedResources();
				else
					switchToPathVariables();
			}
		});

		pageComponent.setLayout(layout);
		data = new GridData();
		data.verticalAlignment = GridData.FILL;
		data.horizontalAlignment = GridData.FILL;
		data.grabExcessHorizontalSpace = true;
		data.grabExcessVerticalSpace = true;
		tabFolder.setLayoutData(data);
		tabFolder.setFont(font);

		TabItem variableItem = new TabItem(tabFolder, SWT.BORDER);

		Composite variableComposite = new Composite(tabFolder, 0);
		variableComposite.setLayout(new GridLayout());
		variableComposite.setFont(font);

		topLabel = new Label(variableComposite, SWT.NONE);
		topLabel.setText(IDEWorkbenchMessages.ProjectLinkedResourcePage_description);
		data = new GridData();
		data.verticalAlignment = GridData.FILL;
		data.horizontalAlignment = GridData.FILL;
		topLabel.setLayoutData(data);
		topLabel.setFont(font);

		pathVariablesGroup.createContents(variableComposite);

		variableItem.setControl(variableComposite);
		variableItem.setText(IDEWorkbenchMessages.ProjectLinkedResourcePage_pathVariableTabTitle);

		TabItem linkedResourceItem = new TabItem(tabFolder, SWT.BORDER);

		Composite linkedResourceComposite = new Composite(tabFolder, 0);
		linkedResourceComposite.setLayout(new GridLayout());
		linkedResourceComposite.setFont(font);

		linkedResourceEditor.createContents(linkedResourceComposite);

		linkedResourceItem.setControl(linkedResourceComposite);
		linkedResourceItem.setText(IDEWorkbenchMessages.ProjectLinkedResourcePage_linkedResourcesTabTitle);

		updateWidgetState(true);
		return pageComponent;
	}

	private void switchToPathVariables() {
		pathVariablesGroup.reloadContent();
	}
	private void switchToLinkedResources() {
		pathVariablesGroup.performOk();
		linkedResourceEditor.reloadContent();
	}

	/**
	 * Creates a tab of one horizontal spans.
	 *
	 * @param parent  the parent in which the tab should be created
	 */
	protected static void createSpace(Composite parent) {
		Label vfiller = new Label(parent, SWT.LEFT);
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.BEGINNING;
		gridData.grabExcessHorizontalSpace = false;
		gridData.verticalAlignment = GridData.CENTER;
		gridData.grabExcessVerticalSpace = false;
		vfiller.setLayoutData(gridData);
	}

	/**
	 * Disposes the path variables group.
	 * @see org.eclipse.jface.dialogs.IDialogPage#dispose()
	 */
	@Override
	public void dispose() {
		pathVariablesGroup.dispose();
		linkedResourceEditor.dispose();
		super.dispose();
	}

	/**
	 * Empty implementation. This page does not use the workbench.
	 * @param workbench
	 *
	 * @see IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	}

	/**
	 * Commits the temporary state to the path variable manager in response to user
	 * confirmation.
	 *
	 * @see PreferencePage#performOk()
	 * @see PathVariablesGroup#performOk()
	 */
	@Override
	public boolean performOk() {
		return pathVariablesGroup.performOk() && linkedResourceEditor.performOk();
	}

	/**
	 * Set the widget enabled state
	 *
	 * @param enableLinking the new widget enabled state
	 */
	protected void updateWidgetState(boolean enableLinking) {
		topLabel.setEnabled(enableLinking);
		pathVariablesGroup.setEnabled(enableLinking);
		linkedResourceEditor.setEnabled(enableLinking);
	}
}
