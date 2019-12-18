/*******************************************************************************
 * Copyright (c) 2009, 2017 IBM Corporation and others.
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
 *     Daniel Kruegler <daniel.kruegler@gmail.com> - Bug 493459
 ******************************************************************************/

package org.fdesigner.e4.core.commands.internal;

import java.util.Map;

import javax.inject.Inject;

import org.fdesigner.commands.Category;
import org.fdesigner.commands.Command;
import org.fdesigner.commands.CommandManager;
import org.fdesigner.commands.IParameter;
import org.fdesigner.commands.ParameterizedCommand;
import org.fdesigner.e4.core.commands.ECommandService;

/**
 *
 */
public class CommandServiceImpl implements ECommandService {

	private CommandManager commandManager;

	@Inject
	public void setManager(CommandManager m) {
		commandManager = m;
	}

	@Override
	public ParameterizedCommand createCommand(String id, Map<String, ?> parameters) {
		Command command = getCommand(id);
		if (command == null) {
			return null;
		}
		return ParameterizedCommand.generateCommand(command, parameters);
	}

	@Override
	public ParameterizedCommand createCommand(String id) {
		return createCommand(id, null);
	}

	@Override
	public Category defineCategory(String id, String name, String description) {
		Category cat = commandManager.getCategory(id);
		if (!cat.isDefined()) {
			cat.define(name, description);
		}
		return cat;
	}

	@Override
	public Command defineCommand(String id, String name, String description, Category category,
			IParameter[] parameters) {
		Command cmd = commandManager.getCommand(id);
		if (!cmd.isDefined()) {
			cmd.define(name, description, category, parameters);
			cmd.setHandler(HandlerServiceImpl.getHandler(id));
		}
		return cmd;
	}

	@Override
	public Command defineCommand(String id, String name, String description, Category category,
			IParameter[] parameters, String helpContextId) {
		Command cmd = commandManager.getCommand(id);
		if (!cmd.isDefined()) {
			cmd.define(name, description, category, parameters, null, helpContextId);
			cmd.setHandler(HandlerServiceImpl.getHandler(id));
		}
		return cmd;
	}

	@Override
	public Category getCategory(String categoryId) {
		return commandManager.getCategory(categoryId);
	}

	@Override
	public Command getCommand(String commandId) {
		return commandManager.getCommand(commandId);
	}

}
