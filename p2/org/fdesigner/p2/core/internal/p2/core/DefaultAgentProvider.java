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
 *******************************************************************************/
package org.fdesigner.p2.core.internal.p2.core;

import java.net.URI;
import java.util.Dictionary;
import java.util.Hashtable;

import org.fdesigner.framework.framework.BundleContext;
import org.fdesigner.framework.framework.Constants;
import org.fdesigner.framework.framework.ServiceRegistration;
import org.fdesigner.p2.core.IAgentLocation;
import org.fdesigner.p2.core.IProvisioningAgent;
import org.fdesigner.p2.core.IProvisioningAgentProvider;

/**
 * Default implementation of {@link IProvisioningAgentProvider}.
 */
public class DefaultAgentProvider implements IProvisioningAgentProvider {
	private BundleContext context;

	public void activate(BundleContext aContext) {
		this.context = aContext;
	}

	@Override
	public IProvisioningAgent createAgent(URI location) {
		ProvisioningAgent result = new ProvisioningAgent();
		result.setBundleContext(context);
		result.setLocation(location);
		IAgentLocation agentLocation = result.getService(IAgentLocation.class);
		Dictionary<String, Object> properties = new Hashtable<>(5);
		if (agentLocation != null)
			properties.put("locationURI", String.valueOf(agentLocation.getRootLocation())); //$NON-NLS-1$
		// make the currently running system have a higher service ranking
		if (location == null) {
			properties.put(Constants.SERVICE_RANKING, Integer.valueOf(100));
			properties.put(IProvisioningAgent.SERVICE_CURRENT, Boolean.TRUE.toString());
		}
		ServiceRegistration<IProvisioningAgent> reg = context.registerService(IProvisioningAgent.class, result,
				properties);
		result.setServiceRegistration(reg);
		return result;
	}
}
