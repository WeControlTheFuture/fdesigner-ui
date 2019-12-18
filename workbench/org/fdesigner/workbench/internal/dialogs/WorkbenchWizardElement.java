/*******************************************************************************
 * Copyright (c) 2000, 2019 IBM Corporation and others.
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
 *     Fair Isaac Corporation <Hemant.Singh@Gmail.com> - Bug 326695
 *     Alexander Fedorov <alexander.fedorov@arsysop.ru> - Bug 548799
 *******************************************************************************/
package org.fdesigner.workbench.internal.dialogs;

import org.fdesigner.runtime.common.runtime.CoreException;
import org.fdesigner.runtime.common.runtime.IAdaptable;
import org.fdesigner.runtime.core.Platform;
import org.fdesigner.runtime.registry.runtime.IConfigurationElement;
import org.fdesigner.ui.jface.resource.ImageDescriptor;
import org.fdesigner.ui.jface.resource.ResourceLocator;
import org.fdesigner.ui.jface.viewers.IStructuredSelection;
import org.fdesigner.ui.jface.viewers.StructuredSelection;
import org.fdesigner.workbench.IPluginContribution;
import org.fdesigner.workbench.IWorkbenchWizard;
import org.fdesigner.workbench.PlatformUI;
import org.fdesigner.workbench.SelectionEnabler;
import org.fdesigner.workbench.internal.ISelectionConversionService;
import org.fdesigner.workbench.internal.WorkbenchPlugin;
import org.fdesigner.workbench.internal.registry.IWorkbenchRegistryConstants;
import org.fdesigner.workbench.internal.registry.KeywordRegistry;
import org.fdesigner.workbench.internal.registry.RegistryReader;
import org.fdesigner.workbench.model.IWorkbenchAdapter;
import org.fdesigner.workbench.model.IWorkbenchAdapter2;
import org.fdesigner.workbench.model.IWorkbenchAdapter3;
import org.fdesigner.workbench.model.WorkbenchAdapter;
import org.fdesigner.workbench.wizards.IWizardCategory;
import org.fdesigner.workbench.wizards.IWizardDescriptor;

/**
 * Instances represent registered wizards.
 */
public class WorkbenchWizardElement extends WorkbenchAdapter
		implements IAdaptable, IPluginContribution, IWizardDescriptor {
	private String id;

	private ImageDescriptor imageDescriptor;

	private SelectionEnabler selectionEnabler;

	private IConfigurationElement configurationElement;

	private ImageDescriptor descriptionImage;

	private WizardCollectionElement parentCategory;

	/**
	 * TODO: DO we need to make this API?
	 */
	public static final String TAG_PROJECT = "project"; //$NON-NLS-1$

	private static final String[] EMPTY_TAGS = new String[0];

	private static final String[] PROJECT_TAGS = new String[] { TAG_PROJECT };

	private String[] keywordLabels;

	/**
	 * Create a new instance of this class
	 *
	 * @param configurationElement
	 * @since 3.1
	 */
	public WorkbenchWizardElement(IConfigurationElement configurationElement) {
		this.configurationElement = configurationElement;
		id = configurationElement.getAttribute(IWorkbenchRegistryConstants.ATT_ID);
	}

	/**
	 * Answer a boolean indicating whether the receiver is able to handle the passed
	 * selection
	 *
	 * @return boolean
	 * @param selection IStructuredSelection
	 */
	public boolean canHandleSelection(IStructuredSelection selection) {
		return getSelectionEnabler().isEnabledForSelection(selection);
	}

	/**
	 * Answer the selection for the receiver based on whether the it can handle the
	 * selection. If it can return the selection. If it can handle the adapted to
	 * IResource value of the selection. If it satisfies neither of these conditions
	 * return an empty IStructuredSelection.
	 *
	 * @return IStructuredSelection
	 * @param selection IStructuredSelection
	 */
	@Override
	public IStructuredSelection adaptedSelection(IStructuredSelection selection) {
		if (canHandleSelection(selection)) {
			return selection;
		}

		IStructuredSelection adaptedSelection = convertToResources(selection);
		if (canHandleSelection(adaptedSelection)) {
			return adaptedSelection;
		}

		// Couldn't find one that works so just return
		return StructuredSelection.EMPTY;
	}

	/**
	 * Create an the instance of the object described by the configuration element.
	 * That is, create the instance of the class the isv supplied in the extension
	 * point.
	 *
	 * @return the new object
	 * @throws CoreException
	 */
	public Object createExecutableExtension() throws CoreException {
		return WorkbenchPlugin.createExtension(configurationElement, IWorkbenchRegistryConstants.ATT_CLASS);
	}

	/**
	 * Returns an object which is an instance of the given class associated with
	 * this object. Returns <code>null</code> if no such object can be found.
	 */
	@Override
	public <T> T getAdapter(Class<T> adapter) {
		if (adapter == IWorkbenchAdapter.class || adapter == IWorkbenchAdapter2.class
				|| adapter == IWorkbenchAdapter3.class) {
			return adapter.cast(this);
		} else if (adapter == IPluginContribution.class) {
			return adapter.cast(this);
		} else if (adapter == IConfigurationElement.class) {
			return adapter.cast(configurationElement);
		}
		return Platform.getAdapterManager().getAdapter(this, adapter);
	}

	/**
	 * @return IConfigurationElement
	 */
	public IConfigurationElement getConfigurationElement() {
		return configurationElement;
	}

	/**
	 * Answer the description parameter of this element
	 *
	 * @return java.lang.String
	 */
	@Override
	public String getDescription() {
		return RegistryReader.getDescription(configurationElement);
	}

	/**
	 * Answer the icon of this element.
	 */
	@Override
	public ImageDescriptor getImageDescriptor() {
		if (imageDescriptor == null) {
			String iconName = configurationElement.getAttribute(IWorkbenchRegistryConstants.ATT_ICON);
			if (iconName == null) {
				return null;
			}
			imageDescriptor = ResourceLocator
					.imageDescriptorFromBundle(configurationElement.getNamespaceIdentifier(), iconName).orElse(null);
		}
		return imageDescriptor;
	}

	/**
	 * Returns the name of this wizard element.
	 */
	@Override
	public ImageDescriptor getImageDescriptor(Object element) {
		return getImageDescriptor();
	}

	/**
	 * Returns the name of this wizard element.
	 */
	@Override
	public String getLabel(Object element) {
		return configurationElement.getAttribute(IWorkbenchRegistryConstants.ATT_NAME);
	}

	/**
	 * Answer self's action enabler, creating it first iff necessary
	 */
	protected SelectionEnabler getSelectionEnabler() {
		if (selectionEnabler == null) {
			selectionEnabler = new SelectionEnabler(configurationElement);
		}

		return selectionEnabler;
	}

	/**
	 * Attempt to convert the elements in the passed selection into resources by
	 * asking each for its IResource property (iff it isn't already a resource). If
	 * all elements in the initial selection can be converted to resources then
	 * answer a new selection containing these resources; otherwise answer an empty
	 * selection.
	 *
	 * @param originalSelection the original selection
	 * @return the converted selection or an empty selection
	 */
	private IStructuredSelection convertToResources(IStructuredSelection originalSelection) {
		Object selectionService = PlatformUI.getWorkbench().getService(ISelectionConversionService.class);
		if (selectionService == null || originalSelection == null) {
			return StructuredSelection.EMPTY;
		}
		return ((ISelectionConversionService) selectionService).convertToResources(originalSelection);
	}

	@Override
	public String getLocalId() {
		return getId();
	}

	@Override
	public String getPluginId() {
		return (configurationElement != null) ? configurationElement.getNamespaceIdentifier() : null;
	}

	@Override
	public ImageDescriptor getDescriptionImage() {
		if (descriptionImage == null) {
			String descImage = configurationElement.getAttribute(IWorkbenchRegistryConstants.ATT_DESCRIPTION_IMAGE);
			if (descImage == null) {
				return null;
			}
			descriptionImage = ResourceLocator
					.imageDescriptorFromBundle(configurationElement.getNamespaceIdentifier(), descImage).orElse(null);
		}
		return descriptionImage;
	}

	@Override
	public String getHelpHref() {
		return configurationElement.getAttribute(IWorkbenchRegistryConstants.ATT_HELP_HREF);
	}

	@Override
	public IWorkbenchWizard createWizard() throws CoreException {
		return (IWorkbenchWizard) createExecutableExtension();
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getLabel() {
		return getLabel(this);
	}

	@Override
	public IWizardCategory getCategory() {
		return (IWizardCategory) getParent(this);
	}

	/**
	 * Return the collection.
	 *
	 * @return the collection
	 * @since 3.1
	 */
	public WizardCollectionElement getCollectionElement() {
		return (WizardCollectionElement) getParent(this);
	}

	@Override
	public String[] getTags() {

		String flag = configurationElement.getAttribute(IWorkbenchRegistryConstants.ATT_PROJECT);
		if (Boolean.parseBoolean(flag)) {
			return PROJECT_TAGS;
		}

		return EMPTY_TAGS;
	}

	@Override
	public Object getParent(Object object) {
		return parentCategory;
	}

	/**
	 * Set the parent category.
	 *
	 * @param parent the parent category
	 * @since 3.1
	 */
	public void setParent(WizardCollectionElement parent) {
		parentCategory = parent;
	}

	@Override
	public boolean canFinishEarly() {
		return Boolean
				.parseBoolean(configurationElement.getAttribute(IWorkbenchRegistryConstants.ATT_CAN_FINISH_EARLY));
	}

	@Override
	public boolean hasPages() {
		String hasPagesString = configurationElement.getAttribute(IWorkbenchRegistryConstants.ATT_HAS_PAGES);
		// default value is true
		if (hasPagesString == null) {
			return true;
		}
		return Boolean.parseBoolean(hasPagesString);
	}

	public String[] getKeywordLabels() {
		if (keywordLabels == null) {

			IConfigurationElement[] children = configurationElement
					.getChildren(IWorkbenchRegistryConstants.TAG_KEYWORD_REFERENCE);
			keywordLabels = new String[children.length];
			KeywordRegistry registry = KeywordRegistry.getInstance();
			for (int i = 0; i < children.length; i++) {
				String id = children[i].getAttribute(IWorkbenchRegistryConstants.ATT_ID);
				keywordLabels[i] = registry.getKeywordLabel(id);
			}
		}
		return keywordLabels;
	}
}
