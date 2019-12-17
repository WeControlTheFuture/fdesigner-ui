/*******************************************************************************
 * Copyright (c) 2013, 2015IBM Corporation and others.
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
 *     Lars Vogel <Lars.Vogel@vogella.com> - Bug 472654
 ******************************************************************************/

package org.eclipse.e4.ui.internal.workbench;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IRegistryEventListener;
import org.eclipse.e4.ui.model.application.MApplicationElement;
import org.eclipse.e4.ui.model.application.impl.ApplicationPackageImpl;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.osgi.service.log.LogService;

/**
 * A factory which is able to build the EMF based EObjects for the given {@link MApplicationElement}
 * class.
 *
 * <p>
 * This factory checks the Eclipse ExtensionRegistry for all registered EMF-packages, via the
 * {@code "org.eclipse.emf.ecore.generated_package"} ExtensionPoint generated by EMF. It uses the
 * EPackage Namespace URI mentioned in this ExtensionPoint to build a mapping between normal java
 * class and the corresponding {@link EClass}.
 * </p>
 *
 * <p>
 * <b>Important:</b> The mapping will only contain {@link EClass}es which extend the
 * {@link MApplicationElement} and are neither abstract nor an interface.
 * </p>
 */
final class GenericMApplicationElementFactoryImpl {

	/**
	 * An ExtensionRegistryListener which will build the required {@link Class} to {@link EClass}
	 * mapping.
	 */
	private final MApplicationElementClassToEClass emfGeneratedPackages;

	/**
	 * Sole constructor.
	 *
	 * @param extensionRegistry
	 *            the Eclipse ExtensionRegistry
	 * @throws NullPointerException
	 *             if the given Eclipse ExtensionRegistry is {@code null}
	 */
	GenericMApplicationElementFactoryImpl(IExtensionRegistry extensionRegistry) {
		if (extensionRegistry == null)
			throw new NullPointerException("No ExtensionRegistry given!"); //$NON-NLS-1$

		emfGeneratedPackages = new MApplicationElementClassToEClass();

		// A clean-up would be nice but the only using service is realized as a singleton-service
		// which is used throughout the running application and so this instance will also life as
		// long as the application is running.
		extensionRegistry.addListener(emfGeneratedPackages,
				MApplicationElementClassToEClass.EP_MODEL_DEFINITION_ENRICHMENT);
		emfGeneratedPackages.initialize(extensionRegistry);
	}

	/**
	 * Takes the given class and creates the corresponding {@link EObject} implementation for it.
	 *
	 * @param clazz
	 *            the class for which the corresponding {@link EObject} should be created
	 * @return the corresponding {@link EObject} or {@code null} if it wasn't able to create one
	 *         (e.g.: no {@link EClass} maps to the given {@link Class})
	 */
	public EObject createEObject(Class<? extends MApplicationElement> clazz) {
		EClass eClass = emfGeneratedPackages.getEClass(clazz);
		if (eClass != null) {
			return EcoreUtil.create(eClass);
		}

		return null;
	}

	/**
	 * An Eclipse ExtensionRegistry-Listener which will build the required map to find the
	 * {@link EClass} for the given {@link Class}.
	 *
	 * <p>
	 * This Listener must be registered on EMF's {@value #EP_MODEL_DEFINITION_ENRICHMENT} extension
	 * point to build the appropriate mapping between {@link Class} and {@link EClass}.
	 * </p>
	 *
	 * <p>
	 * <b>Info:</b> This map will only contain concrete {@link EClass} objects which extend the
	 * {@link MApplicationElement}.
	 * </p>
	 */
	private static final class MApplicationElementClassToEClass implements IRegistryEventListener {

		/** The extension point name which holds the required information. */
		public static final String EP_MODEL_DEFINITION_ENRICHMENT = "org.eclipse.e4.workbench.model.definition.enrichment"; //$NON-NLS-1$

		/**
		 * The configuration element inside the extension point which holds the required
		 * information.
		 */
		private static final String CONFIG_ELEMENT_NAME = "definitionEnrichment"; //$NON-NLS-1$

		/** Attribute name which holds the EMF EPackage Namespace URI. */
		private static final String CONFIG_ATTR_EPACKAGE_URI = "ePackageNS"; //$NON-NLS-1$

		/** Holds the mapping between {@link Class} and {@link EClass}. */
		private final ConcurrentMap<Class<? extends MApplicationElement>, EClass> classToEClass = new ConcurrentHashMap<>();

		/**
		 * Holds the required information per extension point which needs to be clean-up in the
		 * {@link #removed(IExtension[])} method.
		 */
		private final ConcurrentMap<IExtension, List<Class<? extends MApplicationElement>>> registeredClasses = new ConcurrentHashMap<>();

		/** A reference to the {@link MApplicationElement}-EClass. */
		private final EClass mApplicationElementEClass = ApplicationPackageImpl.eINSTANCE
				.getApplicationElement();

		/**
		 * Method which will initialize the mapping with the information from the given Eclipse
		 * ExtensionRegistry.
		 *
		 * <p>
		 * The method will retrieve all {@link #EP_MODEL_DEFINITION_ENRICHMENT} extensions form the
		 * given Eclipse ExtensionRegistry and initializes the basic mapping.
		 * </p>
		 *
		 * @param extensionRegistry
		 *            the Eclipse ExtensionRegistry on which the listener is already registered
		 */
		void initialize(IExtensionRegistry extensionRegistry) {
			if (extensionRegistry == null) { // just for safety's sake
				throw new IllegalArgumentException("No ExtensionRegistry given!"); //$NON-NLS-1$
			}

			IExtensionPoint epGeneratedPackage = extensionRegistry
					.getExtensionPoint(EP_MODEL_DEFINITION_ENRICHMENT);
			if (epGeneratedPackage != null) {
				added(epGeneratedPackage.getExtensions());
			}
		}

		/**
		 * Lookup the {@link EClass} for the given {@link Class}.
		 *
		 * @param elementType
		 *            the {@link Class} to which the {@link EClass} should be found
		 * @return the corresponding {@link EClass} or {@code null} if none was found
		 */
		public EClass getEClass(Class<? extends MApplicationElement> elementType) {
			return classToEClass.get(elementType);
		}

		@Override
		public void added(IExtension[] extensions) {
			for (IExtension extension : extensions) {
				List<Class<? extends MApplicationElement>> elementsToCleanup = addToMapping(extension
						.getConfigurationElements());

				if (elementsToCleanup != null) {
					// keep the list of registered class per extension to remove them in the
					// #remove(IExtension[]) method
					registeredClasses.put(extension, elementsToCleanup);
				}
			}
		}

		@Override
		public void removed(IExtension[] extensions) {
			for (IExtension extension : extensions) {
				List<Class<? extends MApplicationElement>> modelClassesToRemove = registeredClasses
						.remove(extension);

				if (modelClassesToRemove != null) {
					// clean-up
					for (Class<? extends MApplicationElement> modelClass : modelClassesToRemove) {
						classToEClass.remove(modelClass);
					}
				}
			}
		}

		@Override
		public void added(IExtensionPoint[] extensionPoints) {
			// not of interest
		}

		@Override
		public void removed(IExtensionPoint[] extensionPoints) {
			// not of interest
		}

		/**
		 * Reads the information from the given {@link IConfigurationElement}s and updates the
		 * mapping.
		 *
		 * @param configurationElements
		 *            the elements to read the information from
		 * @return the list of {@link Class}es which were put to the {@link #classToEClass} mapping
		 *         or <code>null</code> if none were put into that list
		 */
		private List<Class<? extends MApplicationElement>> addToMapping(
				IConfigurationElement[] configurationElements) {
			if (configurationElements == null) {
				return null;
			}

			List<Class<? extends MApplicationElement>> allMappedEntried = new ArrayList<>();

			for (IConfigurationElement configElement : configurationElements) {
				if (configElement.getName().equals(CONFIG_ELEMENT_NAME)) {
					String emfNsURI = configElement.getAttribute(CONFIG_ATTR_EPACKAGE_URI);

					// find EPackage
					EPackage ePackage = EPackage.Registry.INSTANCE.getEPackage(emfNsURI);

					// build Class to EClass mapping from the classes in the EPackage
					Map<Class<? extends MApplicationElement>, EClass> mapping = buildMapping(ePackage);

					if (mapping != null) {
						for (Map.Entry<Class<? extends MApplicationElement>, EClass> entry : mapping
								.entrySet()) {

							// if the current thread added the mapping we keep the key so it can be
							// removed afterwards in the #remove(IExtension[]) method
							if (classToEClass.putIfAbsent(entry.getKey(), entry.getValue()) == null) {
								allMappedEntried.add(entry.getKey());
							}
						}
					}
				}
			}

			// null means nothing from the given configurationElementes was added to the Class to
			// EClass map
			return allMappedEntried.isEmpty() ? null : allMappedEntried;
		}

		/**
		 * Utility method which walks through all {@link EClass} of the given {@link EPackage} to
		 * build a Class-To-EClass map.
		 * <p>
		 * This method will only take {@link EClass}es into account which extend the
		 * {@link MApplicationElement} and are neither a abstract class nor an interface. Which
		 * means the mapping will only contain {@link EModelService#createModelElement(Class)}
		 * relevant classes.
		 * </p>
		 *
		 * @param ePackage
		 *            the EPackage to scan
		 * @return a map containing all {@link Class}es and their corresponding {@link EClass} which
		 *         are provided by the given {@link EPackage} and extend the
		 *         {@link MApplicationElement}; {@code null} otherwise
		 */
		private final Map<Class<? extends MApplicationElement>, EClass> buildMapping(
				EPackage ePackage) {
			if (ePackage == null)
				return null;

			List<EClassifier> eClassifiers = ePackage.getEClassifiers();
			Map<Class<? extends MApplicationElement>, EClass> mapping = new HashMap<>();

			for (EClassifier eClassifier : eClassifiers) {
				if (eClassifier instanceof EClass) {
					EClass eClass = (EClass) eClassifier;

					if (mApplicationElementEClass.isSuperTypeOf(eClass) && !eClass.isAbstract()
							&& !eClass.isInterface()) {
						@SuppressWarnings("unchecked")
						Class<? extends MApplicationElement> instanceClass = (Class<? extends MApplicationElement>) eClass
								.getInstanceClass();

						// the Map.Entry check is just for safety, because of the EMF special for
						// Key/Value pairs in HashMaps
						// (see: UIElements.ecore/application/StringToStringMap)
						if (!instanceClass.equals(Map.Entry.class)) {
							// add the entry, but if there was already a mapping we should log it
							EClass previousEntry = mapping.put(instanceClass, eClass);

							if (previousEntry != null) {
								Activator
										.log(LogService.LOG_WARNING,
												instanceClass
														+ " is mapped to multiple EClasses (" + eClass.getName() + ", " + previousEntry.getName() + ")!"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
							}
						}
					}

				}
			}

			return mapping.isEmpty() ? null : mapping;
		}
	}
}
