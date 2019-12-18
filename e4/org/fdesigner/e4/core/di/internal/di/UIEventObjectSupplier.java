/*******************************************************************************
 * Copyright (c) 2010, 2019 IBM Corporation and others.
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
package org.fdesigner.e4.core.di.internal.di;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;

import org.fdesigner.e4.core.contexts.IEclipseContext;
import org.fdesigner.e4.core.di.UIEventTopic;
import org.fdesigner.e4.core.di.UISynchronize;
import org.fdesigner.e4.core.di.annotations.Optional;
import org.fdesigner.e4.core.di.extensions.EventObjectSupplier;
import org.fdesigner.e4.core.di.suppliers.ExtendedObjectSupplier;
import org.fdesigner.e4.core.di.suppliers.IObjectDescriptor;
import org.fdesigner.e4.core.di.suppliers.IRequestor;
import org.fdesigner.services.component.annotations.Component;
import org.fdesigner.services.component.annotations.Reference;
import org.fdesigner.services.event.EventAdmin;
import org.fdesigner.services.event.EventHandler;

@Component(service = { ExtendedObjectSupplier.class, EventHandler.class }, property = {
		"dependency.injection.annotation=org.eclipse.e4.ui.di.UIEventTopic",
		"event.topics=" + IEclipseContext.TOPIC_DISPOSE })
public class UIEventObjectSupplier extends EventObjectSupplier {

	class UIEventHandler implements EventHandler {

		final protected IRequestor requestor;
		final private String topic;

		public UIEventHandler(String topic, IRequestor requestor) {
			this.topic = topic;
			this.requestor = requestor;
		}

		@Override
		public void handleEvent(org.fdesigner.services.event.Event event) {
			if (!requestor.isValid()) {
				unsubscribe(requestor);
				return;
			}

			addCurrentEvent(topic, event);
			requestor.resolveArguments(false);
			removeCurrentEvent(topic);
			if( uiSync == null ) {
				if (logger != null)
					logger.log(Level.WARNING, "No realm found to process UI event " + event);
				return;
			} else {
				uiSync.syncExec(() -> requestor.execute());
			}
		}
	}

	@Override
	@Reference
	public void setEventAdmin(EventAdmin eventAdmin) {
		super.setEventAdmin(eventAdmin);
	}

	@Inject
	@Optional
	protected UISynchronize uiSync;

	@Inject @Optional
	protected Logger logger;

	@Override
	protected EventHandler makeHandler(String topic, IRequestor requestor) {
		return new UIEventHandler(topic, requestor);
	}

	@Override
	protected String getTopic(IObjectDescriptor descriptor) {
		if (descriptor == null)
			return null;
		UIEventTopic qualifier = descriptor.getQualifier(UIEventTopic.class);
		return qualifier.value();
	}

}
