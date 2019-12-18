/*******************************************************************************
 * Copyright (c) 2007, 2015 IBM Corporation and others.
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
 *     Lars Vogel <Lars.Vogel@gmail.com> - Bug 440810
 *******************************************************************************/

package org.fdesigner.workbench.actions;

import java.util.Collections;

import org.eclipse.swt.widgets.Event;
import org.fdesigner.commands.ExecutionException;
import org.fdesigner.commands.IHandler;
import org.fdesigner.commands.IHandler2;
import org.fdesigner.commands.NotEnabledException;
import org.fdesigner.commands.NotHandledException;
import org.fdesigner.commands.common.NotDefinedException;
import org.fdesigner.e4.core.commands.EHandlerService;
import org.fdesigner.e4.core.commands.internal.HandlerServiceImpl;
import org.fdesigner.e4.core.contexts.IEclipseContext;
import org.fdesigner.expressions.EvaluationContext;
import org.fdesigner.expressions.IEvaluationContext;
import org.fdesigner.runtime.registry.runtime.IConfigurationElement;
import org.fdesigner.ui.jface.viewers.StructuredSelection;
import org.fdesigner.workbench.IEditorSite;
import org.fdesigner.workbench.IPartListener;
import org.fdesigner.workbench.ISources;
import org.fdesigner.workbench.IWorkbench;
import org.fdesigner.workbench.IWorkbenchPart;
import org.fdesigner.workbench.IWorkbenchPartSite;
import org.fdesigner.workbench.IWorkbenchWindow;
import org.fdesigner.workbench.handlers.IHandlerService;
import org.fdesigner.workbench.internal.actions.CommandAction;
import org.fdesigner.workbench.internal.handlers.ActionDelegateHandlerProxy;
import org.fdesigner.workbench.internal.handlers.E4HandlerProxy;
import org.fdesigner.workbench.internal.handlers.IActionCommandMappingService;
import org.fdesigner.workbench.internal.registry.IWorkbenchRegistryConstants;
import org.fdesigner.workbench.internal.services.IWorkbenchLocationService;
import org.fdesigner.workbench.part.MultiPageEditorSite;
import org.fdesigner.workbench.services.IServiceLocator;

/**
 * For a declarative editor action, see if we can link it to a command.
 * <p>
 * This is a legacy bridge class, and should not be used outside of the Eclipse
 * SDK. Please use menu contributions to display a command in a menu or toolbar.
 * </p>
 * <p>
 * <b>Note:</b> Clients may instantiate.
 * </p>
 *
 * @since 3.3
 */
public final class ContributedAction extends CommandAction {
	private IEvaluationContext appContext;

	private IHandler partHandler;

	private boolean localHandler = false;

	private IPartListener partListener;

	/**
	 * Create an action that can call a command.
	 *
	 * @param locator The appropriate service locator to use. If you use a part site
	 *                as your locator, this action will be tied to your part.
	 * @param element the contributed action element
	 * @throws CommandNotMappedException if the element is not mapped to a command
	 */
	public ContributedAction(IServiceLocator locator, IConfigurationElement element) throws CommandNotMappedException {

		String actionId = element.getAttribute(IWorkbenchRegistryConstants.ATT_ID);
		String commandId = element.getAttribute(IWorkbenchRegistryConstants.ATT_DEFINITION_ID);

		// TODO throw some more exceptions here :-)

		String contributionId = null;
		if (commandId == null) {

			Object obj = element.getParent();
			if (obj instanceof IConfigurationElement) {
				contributionId = ((IConfigurationElement) obj).getAttribute(IWorkbenchRegistryConstants.ATT_ID);
				if (contributionId == null) {
					throw new CommandNotMappedException("Action " //$NON-NLS-1$
							+ actionId + " configuration element invalid"); //$NON-NLS-1$
				}
			}
			// legacy bridge part
			IActionCommandMappingService mapping = locator.getService(IActionCommandMappingService.class);
			if (mapping == null) {
				throw new CommandNotMappedException("No action mapping service available"); //$NON-NLS-1$
			}

			commandId = mapping.getCommandId(mapping.getGeneratedCommandId(contributionId, actionId));
		}
		// what, still no command?
		if (commandId == null) {
			throw new CommandNotMappedException("Action " + actionId //$NON-NLS-1$
					+ " in contribution " + contributionId //$NON-NLS-1$
					+ " not mapped to a command"); //$NON-NLS-1$
		}

		init(locator, commandId, null);

		if (locator instanceof IWorkbenchPartSite) {
			updateSiteAssociations((IWorkbenchPartSite) locator, commandId, actionId, element);
		}

		setId(actionId);
	}

	private void updateSiteAssociations(IWorkbenchPartSite site, String commandId, String actionId,
			IConfigurationElement element) {
		IWorkbenchLocationService wls = site.getService(IWorkbenchLocationService.class);
		IWorkbench workbench = wls.getWorkbench();
		IWorkbenchWindow window = wls.getWorkbenchWindow();
		IHandlerService serv = workbench.getService(IHandlerService.class);
		appContext = new EvaluationContext(serv.getCurrentState(), Collections.EMPTY_LIST);

		// set up the appContext as we would want it.
		appContext.addVariable(ISources.ACTIVE_CURRENT_SELECTION_NAME, StructuredSelection.EMPTY);
		appContext.addVariable(ISources.ACTIVE_PART_NAME, site.getPart());
		appContext.addVariable(ISources.ACTIVE_PART_ID_NAME, site.getId());
		appContext.addVariable(ISources.ACTIVE_SITE_NAME, site);
		if (site instanceof IEditorSite) {
			appContext.addVariable(ISources.ACTIVE_EDITOR_NAME, site.getPart());
			appContext.addVariable(ISources.ACTIVE_EDITOR_ID_NAME, site.getId());
		}
		appContext.addVariable(ISources.ACTIVE_WORKBENCH_WINDOW_NAME, window);
		appContext.addVariable(ISources.ACTIVE_WORKBENCH_WINDOW_SHELL_NAME, window.getShell());

		partHandler = lookUpHandler(site, commandId);
		if (partHandler == null) {
			localHandler = true;
			// if we can't find the handler, then at least we can
			// call the action delegate run method
			partHandler = new ActionDelegateHandlerProxy(element, IWorkbenchRegistryConstants.ATT_CLASS, actionId,
					getParameterizedCommand(), site.getWorkbenchWindow(), null, null, null);
		}
		if (site instanceof MultiPageEditorSite) {
			IHandlerService siteServ = site.getService(IHandlerService.class);
			siteServ.activateHandler(commandId, partHandler);
		}

		if (getParameterizedCommand() != null) {
			getParameterizedCommand().getCommand().removeCommandListener(getCommandListener());
		}
		site.getPage().addPartListener(getPartListener());
	}

	private IHandler lookUpHandler(IServiceLocator site, String commandId) {
		HandlerServiceImpl impl = (HandlerServiceImpl) site.getService(EHandlerService.class);
		IEclipseContext c = impl.getContext();
		Object h = HandlerServiceImpl.lookUpHandler(c, commandId);
		if (h instanceof E4HandlerProxy) {
			return ((E4HandlerProxy) h).getHandler();
		}
		return null;
	}

	@Override
	public void runWithEvent(Event event) {
		if (partHandler != null && getParameterizedCommand() != null) {
			IHandler oldHandler = getParameterizedCommand().getCommand().getHandler();
			try {
				getParameterizedCommand().getCommand().setHandler(partHandler);
				getParameterizedCommand().executeWithChecks(event, appContext);
			} catch (ExecutionException | NotDefinedException | NotEnabledException | NotHandledException e) {
				// TODO some logging, perhaps?
			} finally {
				getParameterizedCommand().getCommand().setHandler(oldHandler);
			}
		} else {
			super.runWithEvent(event);
		}
	}

	@Override
	public boolean isEnabled() {
		if (partHandler != null) {
			if (partHandler instanceof IHandler2) {
				((IHandler2) partHandler).setEnabled(appContext);
			}
			return partHandler.isEnabled();
		}
		return false;
	}

	private IPartListener getPartListener() {
		if (partListener == null) {
			final IWorkbenchPartSite site = (IWorkbenchPartSite) appContext.getVariable(ISources.ACTIVE_SITE_NAME);

			final IWorkbenchPart currentPart;
			if (site instanceof MultiPageEditorSite) {
				currentPart = ((MultiPageEditorSite) site).getMultiPageEditor();
			} else {
				currentPart = site.getPart();
			}

			partListener = new IPartListener() {
				@Override
				public void partActivated(IWorkbenchPart part) {
				}

				@Override
				public void partBroughtToTop(IWorkbenchPart part) {
				}

				@Override
				public void partClosed(IWorkbenchPart part) {
					if (part == currentPart) {
						ContributedAction.this.disposeAction();
					}
				}

				@Override
				public void partDeactivated(IWorkbenchPart part) {
				}

				@Override
				public void partOpened(IWorkbenchPart part) {
				}
			};
		}
		return partListener;
	}

	// TODO make public in 3.4
	private void disposeAction() {
		if (appContext != null) {
			if (localHandler) {
				partHandler.dispose();
			}
			if (partListener != null) {
				IWorkbenchPartSite site = (IWorkbenchPartSite) appContext.getVariable(ISources.ACTIVE_SITE_NAME);
				site.getPage().removePartListener(partListener);
				partListener = null;
			}
			appContext = null;
			partHandler = null;
		}
		dispose();
	}
}
