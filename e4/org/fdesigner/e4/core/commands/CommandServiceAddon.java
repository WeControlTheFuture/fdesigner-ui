/*******************************************************************************
 * Copyright (c) 2010, 2016 IBM Corporation and others.
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
 *     Lars Vogel <Lars.Vogel@vogella.com> - Bug 431180, 482649
 ******************************************************************************/

package org.fdesigner.e4.core.commands;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.fdesigner.commands.CommandManager;
import org.fdesigner.e4.core.commands.internal.CommandServiceImpl;
import org.fdesigner.e4.core.commands.internal.HandlerServiceCreationFunction;
import org.fdesigner.e4.core.commands.internal.HandlerServiceImpl;
import org.fdesigner.e4.core.contexts.ContextInjectionFactory;
import org.fdesigner.e4.core.contexts.IEclipseContext;

/**
 * Provide the command and handler service as an add-on. Must be instantiated against the
 * application level context.
 *
 * @noinstantiate
 */
public class CommandServiceAddon {

	@PostConstruct
	void init(IEclipseContext context) {
		// global command service. There can be only one ... per application :-)
		CommandManager manager = context.get(CommandManager.class);
		if (manager == null) {
			manager = new CommandManager();
			context.set(CommandManager.class, manager);
		}

		CommandServiceImpl service = ContextInjectionFactory.make(CommandServiceImpl.class, context);
		context.set(ECommandService.class, service);

		// handler service
		context.set(EHandlerService.class.getName(), new HandlerServiceCreationFunction());
		// provide the initial application context, just in case.
		HandlerServiceImpl.push(context, null);
	}

	@PreDestroy
	void cleanup() {
		HandlerServiceImpl.pop();
	}

}
