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

package org.fdesigner.ui.jface.action;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.eclipse.swt.SWT;
import org.fdesigner.ui.jface.resource.JFaceResources;
import org.fdesigner.ui.jface.util.Util;

/**
 * <p>
 * Some static utility methods for handling labels on actions. This includes
 * mnemonics and accelerators.
 * </p>
 * <p>
 * Clients may neither instantiate this class nor extend.
 * </p>
 *
 * @since 3.2
 */
public final class LegacyActionTools {

	/**
	 * Table of key codes (key type: <code>String</code>, value type:
	 * <code>Integer</code>); <code>null</code> if not yet initialized.
	 *
	 * @see #findKeyCode
	 */
	private static Map<String, Integer> keyCodes = null;

	/**
	 * Table of string representations of keys (key type: <code>Integer</code>,
	 * value type: <code>String</code>); <code>null</code> if not yet
	 * initialized.
	 *
	 * @see #findKeyString
	 */
	private static Map<Integer, String> keyStrings = null;

	/**
	 * The localized uppercase version of ALT
	 */
	private static String localizedAlt;

	/**
	 * The localized uppercase version of COMMAND
	 */
	private static String localizedCommand;

	/**
	 * The localized uppercase version of CTRL
	 */
	private static String localizedCtrl;

	/**
	 * Table of key codes (key type: <code>String</code>, value type:
	 * <code>Integer</code>); <code>null</code> if not yet initialized. The
	 * key is the localalized name of the key as it appears in menus.
	 *
	 * @see #findLocalizedKeyCode
	 */
	private static Map<String, Integer> localizedKeyCodes = null;

	/**
	 * The localized uppercase version of SHIFT
	 */
	private static String localizedShift;

	/**
	 * The constant to use if there is no mnemonic for this location.
	 */
	public static final char MNEMONIC_NONE = 0;

	/**
	 * Converts an accelerator key code to a string representation.
	 *
	 * @param keyCode
	 *            the key code to be translated
	 * @return a string representation of the key code
	 */
	public static String convertAccelerator(final int keyCode) {
		String modifier = getModifierString(keyCode);
		String fullKey;
		if (modifier.isEmpty()) {
			fullKey = findKeyString(keyCode);
		} else {
			fullKey = modifier + "+" + findKeyString(keyCode); //$NON-NLS-1$
		}
		return fullKey;
	}

	/**
	 * Parses the given accelerator text, and converts it to an accelerator key
	 * code.
	 *
	 * @param acceleratorText
	 *            the accelerator text
	 * @return the SWT key code, or 0 if there is no accelerator
	 */
	public static int convertAccelerator(final String acceleratorText) {
		int accelerator = 0;
		StringTokenizer stok = new StringTokenizer(acceleratorText, "+"); //$NON-NLS-1$

		int keyCode = -1;

		boolean hasMoreTokens = stok.hasMoreTokens();
		while (hasMoreTokens) {
			String token = stok.nextToken();
			hasMoreTokens = stok.hasMoreTokens();
			// Every token except the last must be one of the modifiers
			// Ctrl, Shift, Alt, or Command
			if (hasMoreTokens) {
				int modifier = findModifier(token);
				if (modifier != 0) {
					accelerator |= modifier;
				} else { // Leave if there are none
					return 0;
				}
			} else {
				keyCode = findKeyCode(token);
			}
		}
		if (keyCode != -1) {
			accelerator |= keyCode;
		}
		return accelerator;
	}

	/**
	 * Parses the given accelerator text, and converts it to an accelerator key
	 * code.
	 *
	 * Support for localized modifiers is for backwards compatibility with 1.0.
	 * Use setAccelerator(int) to set accelerators programatically or the
	 * <code>accelerator</code> tag in action definitions in plugin.xml.
	 *
	 * @param acceleratorText
	 *            the accelerator text localized to the current locale
	 * @return the SWT key code, or 0 if there is no accelerator
	 */
	static int convertLocalizedAccelerator(final String acceleratorText) {
		int accelerator = 0;
		StringTokenizer stok = new StringTokenizer(acceleratorText, "+"); //$NON-NLS-1$

		int keyCode = -1;

		boolean hasMoreTokens = stok.hasMoreTokens();
		while (hasMoreTokens) {
			String token = stok.nextToken();
			hasMoreTokens = stok.hasMoreTokens();
			// Every token except the last must be one of the modifiers
			// Ctrl, Shift, Alt, or Command
			if (hasMoreTokens) {
				int modifier = findLocalizedModifier(token);
				if (modifier != 0) {
					accelerator |= modifier;
				} else { // Leave if there are none
					return 0;
				}
			} else {
				keyCode = findLocalizedKeyCode(token);
			}
		}
		if (keyCode != -1) {
			accelerator |= keyCode;
		}
		return accelerator;
	}

	/**
	 * Extracts the accelerator text from the given text. Returns
	 * <code>null</code> if there is no accelerator text, and the empty string
	 * if there is no text after the accelerator delimiter (last tab or
	 * last '@' if there's no tab).
	 *
	 * @param text
	 *            the text for the action; may be <code>null</code>.
	 * @return the accelerator text, or <code>null</code>
	 */
	public static String extractAcceleratorText(final String text) {
		if (text == null) {
			return null;
		}

		int index = text.lastIndexOf('\t');
		if (index == -1) {
			index = text.lastIndexOf('@');
		}
		if (index >= 0) {
			return text.substring(index + 1);
		}
		return null;
	}

	/**
	 * Extracts the mnemonic text from the given string.
	 *
	 * @param text
	 *            The text from which the mnemonic should be extracted; may be
	 *            <code>null</code>
	 * @return The text of the mnemonic; will be {@link #MNEMONIC_NONE} if there
	 *         is no mnemonic;
	 */
	public static char extractMnemonic(final String text) {
		if (text == null) {
			return MNEMONIC_NONE;
		}

		int index = text.indexOf('&');
		if (index == -1) {
			return MNEMONIC_NONE;
		}

		final int textLength = text.length();

		// Ignore '&' at the end of the string.
		if (index == textLength - 1) {
			return MNEMONIC_NONE;
		}

		// Ignore two consecutive ampersands.
		while (text.charAt(index + 1) == '&') {
			index = text.indexOf('&', ++index);
			if (index == textLength - 1) {
				return MNEMONIC_NONE;
			}
		}

		return text.charAt(index + 1);
	}

	/**
	 * Maps a standard keyboard key name to an SWT key code. Key names are converted
	 * to upper case before comparison. If the key name is a single letter, for
	 * example "S", its character code is returned.
	 * <p>
	 * The following key names are known (case is ignored):
	 * </p>
	 * <ul>
	 * <li><code>"BACKSPACE"</code></li>
	 * <li><code>"TAB"</code></li>
	 * <li><code>"RETURN"</code></li>
	 * <li><code>"ENTER"</code></li>
	 * <li><code>"ESC"</code></li>
	 * <li><code>"ESCAPE"</code></li>
	 * <li><code>"DELETE"</code></li>
	 * <li><code>"SPACE"</code></li>
	 * <li><code>"ARROW_UP"</code>, <code>"ARROW_DOWN"</code>,
	 * <code>"ARROW_LEFT"</code>, and <code>"ARROW_RIGHT"</code></li>
	 * <li><code>"PAGE_UP"</code> and <code>"PAGE_DOWN"</code></li>
	 * <li><code>"HOME"</code></li>
	 * <li><code>"END"</code></li>
	 * <li><code>"INSERT"</code></li>
	 * <li><code>"F1"</code>, <code>"F2"</code> through <code>"F12"</code></li>
	 * </ul>
	 *
	 * @param token the key name
	 * @return the SWT key code, <code>-1</code> if no match was found
	 * @see SWT
	 */
	public static int findKeyCode(String token) {
		if (keyCodes == null) {
			initKeyCodes();
		}
		token = token.toUpperCase();
		Integer i = keyCodes.get(token);
		if (i != null) {
			return i.intValue();
		}
		if (token.length() == 1) {
			return token.charAt(0);
		}
		return -1;
	}

	/**
	 * Maps an SWT key code to a standard keyboard key name. The key code is
	 * stripped of modifiers (SWT.CTRL, SWT.ALT, SWT.SHIFT, and SWT.COMMAND). If
	 * the key code is not an SWT code (for example if it a key code for the key
	 * 'S'), a string containing a character representation of the key code is
	 * returned.
	 *
	 * @param keyCode
	 *            the key code to be translated
	 * @return the string representation of the key code
	 * @see SWT
	 * @since 2.0
	 */
	public static String findKeyString(final int keyCode) {
		if (keyStrings == null) {
			initKeyStrings();
		}
		int i = keyCode & ~(SWT.CTRL | SWT.ALT | SWT.SHIFT | SWT.COMMAND);
		Integer integer = Integer.valueOf(i);
		String result = keyStrings.get(integer);
		if (result != null) {
			return result;
		}
		return new String(new char[] { (char) i });
	}

	/**
	 * Find the supplied code for a localized key. As #findKeyCode but localized
	 * to the current locale.
	 *
	 * Support for localized modifiers is for backwards compatibility with 1.0.
	 * Use setAccelerator(int) to set accelerators programatically or the
	 * <code>accelerator</code> tag in action definitions in plugin.xml.
	 *
	 * @param token
	 *            the localized key name
	 * @return the SWT key code, <code>-1</code> if no match was found
	 * @see #findKeyCode
	 */
	private static int findLocalizedKeyCode(String token) {
		if (localizedKeyCodes == null) {
			initLocalizedKeyCodes();
		}
		token = token.toUpperCase();
		Integer i = localizedKeyCodes.get(token);
		if (i != null) {
			return i.intValue();
		}
		if (token.length() == 1) {
			return token.charAt(0);
		}
		return -1;
	}

	/**
	 * Maps the localized modifier names to a code in the same manner as
	 * #findModifier.
	 *
	 * Support for localized modifiers is for backwards compatibility with 1.0.
	 * Use setAccelerator(int) to set accelerators programatically or the
	 * <code>accelerator</code> tag in action definitions in plugin.xml.
	 *
	 * @see #findModifier
	 */
	private static int findLocalizedModifier(String token) {
		if (localizedCtrl == null) {
			initLocalizedModifiers();
		}

		token = token.toUpperCase();
		if (token.equals(localizedCtrl)) {
			return SWT.CTRL;
		}
		if (token.equals(localizedShift)) {
			return SWT.SHIFT;
		}
		if (token.equals(localizedAlt)) {
			return SWT.ALT;
		}
		if (token.equals(localizedCommand)) {
			return SWT.COMMAND;
		}
		return 0;
	}

	/**
	 * Maps standard keyboard modifier key names to the corresponding SWT
	 * modifier bit. The following modifier key names are recognized (case is
	 * ignored): <code>"CTRL"</code>, <code>"SHIFT"</code>,
	 * <code>"ALT"</code>, and <code>"COMMAND"</code>. The given modifier
	 * key name is converted to upper case before comparison.
	 *
	 * @param token
	 *            the modifier key name
	 * @return the SWT modifier bit, or <code>0</code> if no match was found
	 * @see SWT
	 */
	public static int findModifier(String token) {
		token = token.toUpperCase();
		if (token.equals("CTRL")) { //$NON-NLS-1$
			return SWT.CTRL;
		}
		if (token.equals("SHIFT")) { //$NON-NLS-1$
			return SWT.SHIFT;
		}
		if (token.equals("ALT")) { //$NON-NLS-1$
			return SWT.ALT;
		}
		if (token.equals("COMMAND")) { //$NON-NLS-1$
			return SWT.COMMAND;
		}
		return 0;
	}

	/**
	 * Returns a string representation of an SWT modifier bit (SWT.CTRL,
	 * SWT.ALT, SWT.SHIFT, and SWT.COMMAND). Returns <code>null</code> if the
	 * key code is not an SWT modifier bit.
	 *
	 * @param keyCode
	 *            the SWT modifier bit to be translated
	 * @return the string representation of the SWT modifier bit, or
	 *         <code>null</code> if the key code was not an SWT modifier bit
	 * @see SWT
	 */
	public static String findModifierString(final int keyCode) {
		if (keyCode == SWT.CTRL) {
			return JFaceResources.getString("Ctrl"); //$NON-NLS-1$
		}
		if (keyCode == SWT.ALT) {
			return JFaceResources.getString("Alt"); //$NON-NLS-1$
		}
		if (keyCode == SWT.SHIFT) {
			return JFaceResources.getString("Shift"); //$NON-NLS-1$
		}
		if (keyCode == SWT.COMMAND) {
			return JFaceResources.getString("Command"); //$NON-NLS-1$
		}
		return null;
	}

	/**
	 * Returns the string representation of the modifiers (Ctrl, Alt, Shift,
	 * Command) of the key event.
	 *
	 * @param keyCode
	 *            The key code for which the modifier string is desired.
	 * @return The string representation of the key code; never
	 *         <code>null</code>.
	 */
	private static String getModifierString(int keyCode) {
		String modString = ""; //$NON-NLS-1$

		if ((keyCode & SWT.CTRL) != 0) {
			modString = findModifierString(keyCode & SWT.CTRL);
		}

		if ((keyCode & SWT.ALT) != 0) {
			if (modString.isEmpty()) {
				modString = findModifierString(keyCode & SWT.ALT);
			} else {
				modString = modString
						+ "+" + findModifierString(keyCode & SWT.ALT); //$NON-NLS-1$
			}
		}

		if ((keyCode & SWT.SHIFT) != 0) {
			if (modString.isEmpty()) {
				modString = findModifierString(keyCode & SWT.SHIFT);
			} else {
				modString = modString
						+ "+" + findModifierString(keyCode & SWT.SHIFT); //$NON-NLS-1$
			}
		}

		if ((keyCode & SWT.COMMAND) != 0) {
			if (modString.isEmpty()) {
				modString = findModifierString(keyCode & SWT.COMMAND);
			} else {
				modString = modString
						+ "+" + findModifierString(keyCode & SWT.COMMAND); //$NON-NLS-1$
			}
		}

		return modString;
	}

	/**
	 * Initializes the internal key code table.
	 */
	private static void initKeyCodes() {
		keyCodes = new HashMap<>(40);

		keyCodes.put("BACKSPACE", Integer.valueOf(8)); //$NON-NLS-1$
		keyCodes.put("TAB", Integer.valueOf(9)); //$NON-NLS-1$
		keyCodes.put("RETURN", Integer.valueOf(13)); //$NON-NLS-1$
		keyCodes.put("ENTER", Integer.valueOf(13)); //$NON-NLS-1$
		keyCodes.put("ESCAPE", Integer.valueOf(27)); //$NON-NLS-1$
		keyCodes.put("ESC", Integer.valueOf(27)); //$NON-NLS-1$
		keyCodes.put("DELETE", Integer.valueOf(127)); //$NON-NLS-1$

		keyCodes.put("SPACE", Integer.valueOf(' ')); //$NON-NLS-1$
		keyCodes.put("ARROW_UP", Integer.valueOf(SWT.ARROW_UP)); //$NON-NLS-1$
		keyCodes.put("ARROW_DOWN", Integer.valueOf(SWT.ARROW_DOWN)); //$NON-NLS-1$
		keyCodes.put("ARROW_LEFT", Integer.valueOf(SWT.ARROW_LEFT)); //$NON-NLS-1$
		keyCodes.put("ARROW_RIGHT", Integer.valueOf(SWT.ARROW_RIGHT)); //$NON-NLS-1$
		keyCodes.put("PAGE_UP", Integer.valueOf(SWT.PAGE_UP)); //$NON-NLS-1$
		keyCodes.put("PAGE_DOWN", Integer.valueOf(SWT.PAGE_DOWN)); //$NON-NLS-1$
		keyCodes.put("HOME", Integer.valueOf(SWT.HOME)); //$NON-NLS-1$
		keyCodes.put("END", Integer.valueOf(SWT.END)); //$NON-NLS-1$
		keyCodes.put("INSERT", Integer.valueOf(SWT.INSERT)); //$NON-NLS-1$
		keyCodes.put("F1", Integer.valueOf(SWT.F1)); //$NON-NLS-1$
		keyCodes.put("F2", Integer.valueOf(SWT.F2)); //$NON-NLS-1$
		keyCodes.put("F3", Integer.valueOf(SWT.F3)); //$NON-NLS-1$
		keyCodes.put("F4", Integer.valueOf(SWT.F4)); //$NON-NLS-1$
		keyCodes.put("F5", Integer.valueOf(SWT.F5)); //$NON-NLS-1$
		keyCodes.put("F6", Integer.valueOf(SWT.F6)); //$NON-NLS-1$
		keyCodes.put("F7", Integer.valueOf(SWT.F7)); //$NON-NLS-1$
		keyCodes.put("F8", Integer.valueOf(SWT.F8)); //$NON-NLS-1$
		keyCodes.put("F9", Integer.valueOf(SWT.F9)); //$NON-NLS-1$
		keyCodes.put("F10", Integer.valueOf(SWT.F10)); //$NON-NLS-1$
		keyCodes.put("F11", Integer.valueOf(SWT.F11)); //$NON-NLS-1$
		keyCodes.put("F12", Integer.valueOf(SWT.F12)); //$NON-NLS-1$
		keyCodes.put("F13", Integer.valueOf(SWT.F13)); //$NON-NLS-1$
		keyCodes.put("F14", Integer.valueOf(SWT.F14)); //$NON-NLS-1$
		keyCodes.put("F15", Integer.valueOf(SWT.F15)); //$NON-NLS-1$
		keyCodes.put("F16", Integer.valueOf(SWT.F16)); //$NON-NLS-1$
		keyCodes.put("F17", Integer.valueOf(SWT.F17)); //$NON-NLS-1$
		keyCodes.put("F18", Integer.valueOf(SWT.F18)); //$NON-NLS-1$
		keyCodes.put("F19", Integer.valueOf(SWT.F19)); //$NON-NLS-1$
		keyCodes.put("F20", Integer.valueOf(SWT.F20)); //$NON-NLS-1$
	}

	/**
	 * Initializes the internal key string table.
	 */
	private static void initKeyStrings() {
		keyStrings = new HashMap<>(40);

		keyStrings.put(Integer.valueOf(8), JFaceResources.getString("Backspace")); //$NON-NLS-1$
		keyStrings.put(Integer.valueOf(9), JFaceResources.getString("Tab")); //$NON-NLS-1$
		keyStrings.put(Integer.valueOf(13), JFaceResources.getString("Return")); //$NON-NLS-1$
		keyStrings.put(Integer.valueOf(13), JFaceResources.getString("Enter")); //$NON-NLS-1$
		keyStrings.put(Integer.valueOf(27), JFaceResources.getString("Escape")); //$NON-NLS-1$
		keyStrings.put(Integer.valueOf(27), JFaceResources.getString("Esc")); //$NON-NLS-1$
		keyStrings.put(Integer.valueOf(127), JFaceResources.getString("Delete")); //$NON-NLS-1$

		keyStrings.put(Integer.valueOf(' '), JFaceResources.getString("Space")); //$NON-NLS-1$

		keyStrings.put(Integer.valueOf(SWT.ARROW_UP), JFaceResources
				.getString("Arrow_Up")); //$NON-NLS-1$
		keyStrings.put(Integer.valueOf(SWT.ARROW_DOWN), JFaceResources
				.getString("Arrow_Down")); //$NON-NLS-1$
		keyStrings.put(Integer.valueOf(SWT.ARROW_LEFT), JFaceResources
				.getString("Arrow_Left")); //$NON-NLS-1$
		keyStrings.put(Integer.valueOf(SWT.ARROW_RIGHT), JFaceResources
				.getString("Arrow_Right")); //$NON-NLS-1$
		keyStrings.put(Integer.valueOf(SWT.PAGE_UP), JFaceResources
				.getString("Page_Up")); //$NON-NLS-1$
		keyStrings.put(Integer.valueOf(SWT.PAGE_DOWN), JFaceResources
				.getString("Page_Down")); //$NON-NLS-1$
		keyStrings.put(Integer.valueOf(SWT.HOME), JFaceResources.getString("Home")); //$NON-NLS-1$
		keyStrings.put(Integer.valueOf(SWT.END), JFaceResources.getString("End")); //$NON-NLS-1$
		keyStrings.put(Integer.valueOf(SWT.INSERT), JFaceResources
				.getString("Insert")); //$NON-NLS-1$
		keyStrings.put(Integer.valueOf(SWT.F1), JFaceResources.getString("F1")); //$NON-NLS-1$
		keyStrings.put(Integer.valueOf(SWT.F2), JFaceResources.getString("F2")); //$NON-NLS-1$
		keyStrings.put(Integer.valueOf(SWT.F3), JFaceResources.getString("F3")); //$NON-NLS-1$
		keyStrings.put(Integer.valueOf(SWT.F4), JFaceResources.getString("F4")); //$NON-NLS-1$
		keyStrings.put(Integer.valueOf(SWT.F5), JFaceResources.getString("F5")); //$NON-NLS-1$
		keyStrings.put(Integer.valueOf(SWT.F6), JFaceResources.getString("F6")); //$NON-NLS-1$
		keyStrings.put(Integer.valueOf(SWT.F7), JFaceResources.getString("F7")); //$NON-NLS-1$
		keyStrings.put(Integer.valueOf(SWT.F8), JFaceResources.getString("F8")); //$NON-NLS-1$
		keyStrings.put(Integer.valueOf(SWT.F9), JFaceResources.getString("F9")); //$NON-NLS-1$
		keyStrings.put(Integer.valueOf(SWT.F10), JFaceResources.getString("F10")); //$NON-NLS-1$
		keyStrings.put(Integer.valueOf(SWT.F11), JFaceResources.getString("F11")); //$NON-NLS-1$
		keyStrings.put(Integer.valueOf(SWT.F12), JFaceResources.getString("F12")); //$NON-NLS-1$
		keyStrings.put(Integer.valueOf(SWT.F13), JFaceResources.getString("F13")); //$NON-NLS-1$
		keyStrings.put(Integer.valueOf(SWT.F14), JFaceResources.getString("F14")); //$NON-NLS-1$
		keyStrings.put(Integer.valueOf(SWT.F15), JFaceResources.getString("F15")); //$NON-NLS-1$
		keyStrings.put(Integer.valueOf(SWT.F16), JFaceResources.getString("F16")); //$NON-NLS-1$
		keyStrings.put(Integer.valueOf(SWT.F17), JFaceResources.getString("F17")); //$NON-NLS-1$
		keyStrings.put(Integer.valueOf(SWT.F18), JFaceResources.getString("F18")); //$NON-NLS-1$
		keyStrings.put(Integer.valueOf(SWT.F19), JFaceResources.getString("F19")); //$NON-NLS-1$
		keyStrings.put(Integer.valueOf(SWT.F20), JFaceResources.getString("F20")); //$NON-NLS-1$
	}

	/**
	 * Initializes the localized internal key code table.
	 */
	private static void initLocalizedKeyCodes() {
		localizedKeyCodes = new HashMap<>(40);

		localizedKeyCodes.put(JFaceResources
				.getString("Backspace").toUpperCase(), Integer.valueOf(8)); //$NON-NLS-1$
		localizedKeyCodes.put(
				JFaceResources.getString("Tab").toUpperCase(), Integer.valueOf(9)); //$NON-NLS-1$
		localizedKeyCodes
				.put(
						JFaceResources.getString("Return").toUpperCase(), Integer.valueOf(13)); //$NON-NLS-1$
		localizedKeyCodes
				.put(
						JFaceResources.getString("Enter").toUpperCase(), Integer.valueOf(13)); //$NON-NLS-1$
		localizedKeyCodes
				.put(
						JFaceResources.getString("Escape").toUpperCase(), Integer.valueOf(27)); //$NON-NLS-1$
		localizedKeyCodes.put(
				JFaceResources.getString("Esc").toUpperCase(), Integer.valueOf(27)); //$NON-NLS-1$
		localizedKeyCodes
				.put(
						JFaceResources.getString("Delete").toUpperCase(), Integer.valueOf(127)); //$NON-NLS-1$

		localizedKeyCodes
				.put(
						JFaceResources.getString("Space").toUpperCase(), Integer.valueOf(' ')); //$NON-NLS-1$

		localizedKeyCodes
				.put(
						JFaceResources.getString("Arrow_Up").toUpperCase(), Integer.valueOf(SWT.ARROW_UP)); //$NON-NLS-1$
		localizedKeyCodes
				.put(
						JFaceResources.getString("Arrow_Down").toUpperCase(), Integer.valueOf(SWT.ARROW_DOWN)); //$NON-NLS-1$
		localizedKeyCodes
				.put(
						JFaceResources.getString("Arrow_Left").toUpperCase(), Integer.valueOf(SWT.ARROW_LEFT)); //$NON-NLS-1$
		localizedKeyCodes
				.put(
						JFaceResources.getString("Arrow_Right").toUpperCase(), Integer.valueOf(SWT.ARROW_RIGHT)); //$NON-NLS-1$
		localizedKeyCodes
				.put(
						JFaceResources.getString("Page_Up").toUpperCase(), Integer.valueOf(SWT.PAGE_UP)); //$NON-NLS-1$
		localizedKeyCodes
				.put(
						JFaceResources.getString("Page_Down").toUpperCase(), Integer.valueOf(SWT.PAGE_DOWN)); //$NON-NLS-1$
		localizedKeyCodes
				.put(
						JFaceResources.getString("Home").toUpperCase(), Integer.valueOf(SWT.HOME)); //$NON-NLS-1$
		localizedKeyCodes
				.put(
						JFaceResources.getString("End").toUpperCase(), Integer.valueOf(SWT.END)); //$NON-NLS-1$
		localizedKeyCodes
				.put(
						JFaceResources.getString("Insert").toUpperCase(), Integer.valueOf(SWT.INSERT)); //$NON-NLS-1$
		localizedKeyCodes
				.put(
						JFaceResources.getString("F1").toUpperCase(), Integer.valueOf(SWT.F1)); //$NON-NLS-1$
		localizedKeyCodes
				.put(
						JFaceResources.getString("F2").toUpperCase(), Integer.valueOf(SWT.F2)); //$NON-NLS-1$
		localizedKeyCodes
				.put(
						JFaceResources.getString("F3").toUpperCase(), Integer.valueOf(SWT.F3)); //$NON-NLS-1$
		localizedKeyCodes
				.put(
						JFaceResources.getString("F4").toUpperCase(), Integer.valueOf(SWT.F4)); //$NON-NLS-1$
		localizedKeyCodes
				.put(
						JFaceResources.getString("F5").toUpperCase(), Integer.valueOf(SWT.F5)); //$NON-NLS-1$
		localizedKeyCodes
				.put(
						JFaceResources.getString("F6").toUpperCase(), Integer.valueOf(SWT.F6)); //$NON-NLS-1$
		localizedKeyCodes
				.put(
						JFaceResources.getString("F7").toUpperCase(), Integer.valueOf(SWT.F7)); //$NON-NLS-1$
		localizedKeyCodes
				.put(
						JFaceResources.getString("F8").toUpperCase(), Integer.valueOf(SWT.F8)); //$NON-NLS-1$
		localizedKeyCodes
				.put(
						JFaceResources.getString("F9").toUpperCase(), Integer.valueOf(SWT.F9)); //$NON-NLS-1$
		localizedKeyCodes
				.put(
						JFaceResources.getString("F10").toUpperCase(), Integer.valueOf(SWT.F10)); //$NON-NLS-1$
		localizedKeyCodes
				.put(
						JFaceResources.getString("F11").toUpperCase(), Integer.valueOf(SWT.F11)); //$NON-NLS-1$
		localizedKeyCodes
				.put(
						JFaceResources.getString("F12").toUpperCase(), Integer.valueOf(SWT.F12)); //$NON-NLS-1$
		localizedKeyCodes
				.put(
						JFaceResources.getString("F13").toUpperCase(), Integer.valueOf(SWT.F13)); //$NON-NLS-1$
		localizedKeyCodes
				.put(
						JFaceResources.getString("F14").toUpperCase(), Integer.valueOf(SWT.F14)); //$NON-NLS-1$
		localizedKeyCodes
				.put(
						JFaceResources.getString("F15").toUpperCase(), Integer.valueOf(SWT.F15)); //$NON-NLS-1$
		localizedKeyCodes
				.put(
						JFaceResources.getString("F16").toUpperCase(), Integer.valueOf(SWT.F16)); //$NON-NLS-1$
		localizedKeyCodes
				.put(
						JFaceResources.getString("F17").toUpperCase(), Integer.valueOf(SWT.F17)); //$NON-NLS-1$
		localizedKeyCodes
				.put(
						JFaceResources.getString("F18").toUpperCase(), Integer.valueOf(SWT.F18)); //$NON-NLS-1$
		localizedKeyCodes
				.put(
						JFaceResources.getString("F19").toUpperCase(), Integer.valueOf(SWT.F19)); //$NON-NLS-1$
		localizedKeyCodes
				.put(
						JFaceResources.getString("F20").toUpperCase(), Integer.valueOf(SWT.F20)); //$NON-NLS-1$
	}

	/**
	 * Initialize the list of localized modifiers
	 */
	private static void initLocalizedModifiers() {
		localizedCtrl = JFaceResources.getString("Ctrl").toUpperCase(); //$NON-NLS-1$
		localizedShift = JFaceResources.getString("Shift").toUpperCase(); //$NON-NLS-1$
		localizedAlt = JFaceResources.getString("Alt").toUpperCase(); //$NON-NLS-1$
		localizedCommand = JFaceResources.getString("Command").toUpperCase(); //$NON-NLS-1$
	}

	/**
	 * Convenience method for removing any optional accelerator text from the
	 * given string. The accelerator text appears at the end of the text, and is
	 * separated from the main part by the last tab character <code>'\t'</code>
	 * (or the last <code>'@'</code> if there is no tab).
	 *
	 * @param text
	 *            the text
	 * @return the text sans accelerator
	 */
	public static String removeAcceleratorText(final String text) {
		int index = text.lastIndexOf('\t');
		if (index == -1) {
			index = text.lastIndexOf('@');
		}
		if (index >= 0) {
			return text.substring(0, index);
		}
		return text;
	}

	/**
	 * Convenience method for removing any mnemonics from the given string. For
	 * example, <code>removeMnemonics("&amp;Open")</code> will return
	 * <code>"Open"</code>.
	 *
	 * @param text the text
	 * @return the text sans mnemonics
	 */
	public static String removeMnemonics(final String text) {
		int index = text.indexOf('&');
		if (index == -1) {
			return text;
		}
		int len = text.length();
		StringBuilder sb = new StringBuilder(len);
		int lastIndex = 0;
		while (index != -1) {
			// ignore & at the end
			if (index == len - 1) {
				break;
			}
			// handle the && case
			if (text.charAt(index + 1) == '&') {
				++index;
			}

			// DBCS languages use "(&X)" format
			if (index > 0 && text.charAt(index - 1) == '('
					&& text.length() >= index + 3
					&& text.charAt(index + 2) == ')') {
				sb.append(text.substring(lastIndex, index - 1));
				index += 3;
			} else {
				sb.append(text.substring(lastIndex, index));
				// skip the &
				++index;
			}

			lastIndex = index;
			index = text.indexOf('&', index);
		}
		if (lastIndex < len) {
			sb.append(text.substring(lastIndex, len));
		}
		return sb.toString();
	}

	/**
	 * Convenience method for escaping all mnemonics in the given string. For
	 * example, <code>escapeMnemonics("a &amp; b &amp; c")</code> will return
	 * <code>"a &amp;&amp; b &amp;&amp; c"</code>.
	 *
	 * @param text the text
	 * @return the text with mnemonics escaped
	 * @since 3.6
	 */
	public static String escapeMnemonics(String text) {
		return Util.replaceAll(text, "&", "&&"); //$NON-NLS-1$//$NON-NLS-2$
	}

	/**
	 * This class cannot be instantiated.
	 */
	private LegacyActionTools() {
		// Does nothing
	}

}