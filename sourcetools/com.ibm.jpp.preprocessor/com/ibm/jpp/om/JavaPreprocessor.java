/*******************************************************************************
 * Copyright IBM Corp. and others 1999
 *
 * This program and the accompanying materials are made available under
 * the terms of the Eclipse Public License 2.0 which accompanies this
 * distribution and is available at https://www.eclipse.org/legal/epl-2.0/
 * or the Apache License, Version 2.0 which accompanies this distribution and
 * is available at https://www.apache.org/licenses/LICENSE-2.0.
 *
 * This Source Code may also be made available under the following
 * Secondary Licenses when the conditions for such availability set
 * forth in the Eclipse Public License, v. 2.0 are satisfied: GNU
 * General Public License, version 2 with the GNU Classpath
 * Exception [1] and GNU General Public License, version 2 with the
 * OpenJDK Assembly Exception [2].
 *
 * [1] https://www.gnu.org/software/classpath/license.html
 * [2] https://openjdk.org/legal/assembly-exception.html
 *
 * SPDX-License-Identifier: EPL-2.0 OR Apache-2.0 OR GPL-2.0 WITH Classpath-exception-2.0 OR LicenseRef-GPL-2.0 WITH Assembly-exception
 *******************************************************************************/
package com.ibm.jpp.om;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

/**
 * This class pre-processes the contents of an input stream, providing
 * a mechanism for text exclusion which is similar to the #ifdef mechanism
 * in the C pre-processor.
 * <p>
 * It looks for expressions of the form:
 * <pre>
 * /&#42;[IF flag1 | flag2 &amp; (flag3 &circ; flag4)]&#42;/
 * &quot;lines to include if the expression is true&quot;
 * /&#42;[ENDIF]&#42;/
 * </pre>
 * (Note: No space between last "*" and last "/")
 * <p>
 * The commands must be the first item on the line, and they may be nested.
 * <p>
 * Note that the source is assumed to be 8859_1 text.
 *
 * @author IBM Corp.
 * @version initial
 */
public class JavaPreprocessor {

	private static final class IfState {

		/**
		 * The current IF or ELSEIF branch is active.
		 */
		static final int IF_ACTIVE = 0;

		/**
		 * A previous IF or ELSEIF branch was selected.
		 */
		static final int IF_PREVIOUS = 1;

		/**
		 * The current IF or ELSEIF branch is inactive
		 * (and no previous branch was selected).
		 */
		static final int IF_INACTIVE = 2;

		/**
		 * We're in an active ELSE branch.
		 */
		static final int ELSE_ACTIVE = 3;

		/**
		 * We're in an inactive ELSE branch.
		 */
		static final int ELSE_INACTIVE = 4;

		final boolean outerActive;

		int state;

		IfState(boolean active, Stack<IfState> stack) {
			super();
			this.outerActive = stack.isEmpty() || stack.peek().isActive();
			this.state = active ? IF_ACTIVE : IF_INACTIVE;
		}

		boolean isActive() {
			switch (state) {
			case IF_ACTIVE:
			case ELSE_ACTIVE:
				return outerActive;
			default:
				return false;
			}
		}

	}

	private static final Charset charset;

	static {
		if ("z/OS".equalsIgnoreCase(System.getProperty("os.name"))) { //$NON-NLS-1$ //$NON-NLS-2$
			charset = Charset.forName("IBM-1047"); //$NON-NLS-1$
		} else {
			charset = StandardCharsets.US_ASCII;
		}
	}

	private final File inFile;
	private BufferedReader in;
	private final Stack<BufferedReader> inStack;
	private final Writer out;

	/**
	 * Configurable options. The following can be configured by setters before
	 * preprocess() is called.
	 */
	private final Set<String> flags = new HashSet<>();
	// Flags that are required in an INCLUDE-IF for the file to be included
	private Set<String> requiredIncludeFlags = new HashSet<>();
	// Flags which were ref'ed by source code.
	private List<String> foundFlags = new ArrayList<>();
	// The PRs found fixed in the code (using the PR command)
	private Set<String> fixedPRs = new HashSet<>();
	private final Set<String> newIDEAs = new HashSet<>();
	private Set<String> jxeRules = new HashSet<>();
	private List<PreprocessorWarning> invalidFlags = new ArrayList<>();
	// Property collection containing key-value pairs declared in MSG command
	private Map<String, String> externalMessages;
	// Inline messages flag
	private boolean replaceMsg_getString = false;
	private boolean jxePreserveApi = false;
	private final Map<String, String> macros = new HashMap<>();
	private final Map<String, Integer> numericMacros = new HashMap<>();
	private JitAttributes jitAttributes;
	// Whether files with no INCLUDE-IF commands should be included
	private boolean includeIfUnsure = false;
	// whether to give warning about the files that do not have include if directive
	/* [PR 117967] idea 491: Automatically create the jars required for test bootpath */
	private boolean noWarnIncludeIf = false;
	private final Stack<IfState> ifResults = new Stack<>();
	private int lineCount = 0;
	private int outputLineCount = 0;
	private boolean echo = true;
	private final Set<String> validFlags = new HashSet<>();

	// Stores MSG-command key-value pairs until shouldInclude is determined,
	// at which point the entries may be added to externalMessages
	final Map<String, String> externalMessagesToBeAdded = new HashMap<>();

	// variables to handle generating jxe rules for public api
	private String packageName = null;
	private String rootClassName = null;

	private boolean useMacros = false;
	private MsgCallInliner msgCallInliner;

	char[] line = new char[0]; // current line
	int lineLength = 0; // Length of the current line.
	private static final int MAX_COMMAND = 16; // Maximum size of a command.
	private String command = ""; // Current command (only 1).
	private String argument = ""; // Current argument (only 1).

	private final List<PreprocessorWarning> warnings = new LinkedList<>();
	private PreprocessorWarning error = null;
	private int numberOfIncludeIfs = 0;
	private boolean shouldInclude;
	/*
	 * true iff this file should be included based on the first INCLUDE-IF directive. If no INCLUDE-IF directive is found in the
	 * file, this will be set to the value of the flag "includeIfUnsure"
	 */
	private long timeTaken = 0;
	private int numberOfBlankLines = 0;
	// continuation of a long line
	private final boolean continuation = false;
	private boolean foundCopyright = false;
	// name of file being processed
	private final String file;
	private Writer metadataOut;

	private static final String newLine = System.lineSeparator();

	private final MsgClassSubstituter msgClassSubstituter = new MsgClassSubstituter();
	boolean substituteMsgClass = false;

	private boolean addMSGcomment = false;
	private String msg_comment;
	/* [PR 120411] Use a javadoc tag instead of TestBootpath preprocessor tag */
	private boolean isTestBootpathJavaDocTagWritten = false;
	private boolean preprocessingTestBootpath = false;
	private boolean testBootpathJavaDocIsNeeded = false;

	final void replaceLine(String replacement) {
		line = replacement.toCharArray();
		lineLength = line.length;
	}

	/**
	 * Create the new instance of the preprocessor.
	 *
	 * @param       metadataOut
	 * @param       inputFile       the input file handle
	 * @param       out             stream to write the converted output on
	 * @param       outputFile      the output file handle
	 */
	public JavaPreprocessor(OutputStream metadataOut, File inputFile, OutputStream out, File outputFile) {
		super();
		this.file = inputFile.getAbsolutePath();
		this.inStack = new Stack<>();
		this.inFile = inputFile;
		this.out = new OutputStreamWriter(out, charset);

		if (metadataOut != null) {
			this.metadataOut = new OutputStreamWriter(metadataOut, charset);
			try {
				this.metadataOut.write(inputFile.getAbsolutePath());
				this.metadataOut.write(newLine);
				this.metadataOut.write(outputFile.getAbsolutePath());
				this.metadataOut.write(newLine);
			} catch (IOException ex) {
				error("IOException on write: " + ex.getMessage(), ex);
			}
		}
	}

	/**
	 * Adds flags to the list of valid flags.
	 *
	 * @param       flags       the flags to be added
	 */
	public void addValidFlags(Set<String> flags) {
		validFlags.addAll(flags);
	}

	/**
	 * Sets whether preprocessing for bootpath project or not
	 * @param       bootPath    true if bootPath project, otherwise false
	 *
	 */
	/* [PR 120411] Use a javadoc tag instead of TestBootpath preprocessor tag */
	public void setTestBootPath(boolean bootPath) {
		preprocessingTestBootpath = bootPath;
	}

	/**
	 * Removes all the valid flags from this Java preprocessor.
	 */
	public void removeValidFlags() {
		validFlags.clear();
	}

	/**
	 * Checks if the given string matches one of the current state flags.
	 *
	 * @param arg String the flag to test.
	 * @return boolean true if we the string is defined and false otherwise.
	 */
	private boolean defined(String arg) {
		return flags.contains(arg);
	}

	/**
	 * Invoke the current command/argument pair.
	 */
	private void doCommand() {
		String cmd = command;

		if (cmd.length() <= 0) {
			error("Missing command");
		}

		String arg = argument.trim();

		if (cmd.equals("IF")) {
			doCommand_IF(arg);
		} else if (cmd.equals("ENDIF")) {
			doCommand_ENDIF(arg);
		} else if (cmd.equals("ELSE")) {
			doCommand_ELSE(arg);
		} else if (cmd.equals("ELSEIF")) {
			doCommand_ELSEIF(arg);
		} else if (cmd.equals("PR") || cmd.equals("BUG")) {
			doCommand_PR(arg);
		} else if (cmd.equals("IDEA")) {
			doCommand_IDEA(arg);
		} else if (cmd.equals("INCLUDE-IF")) {
			doCommand_INCLUDEIF(arg);
		} else if (cmd.equals("MSG")) {
			doCommand_MSG(arg);
		} else if (cmd.equals("JXERULE")) {
			doCommand_JXERULE(arg);
		} else if (cmd.equals("REM")) {
			//
		} else if (cmd.equals("EXCLUDE-FILE")) {
			numberOfIncludeIfs++; // INCLUDE-IF replacement
		} else if (cmd.equals("USEMACROS")) {
			doCommand_USEMACROS();
		} else if (cmd.equals("#INCLUDE")) {
			doCommand_SHARPINCLUDE(arg);
		} else {
			error("Bogus command -- \"" + cmd + "\"");
		}
	}

	/**
	 * Handle the ELSE command.
	 *
	 * @param arg String the expression to test.
	 */
	private void doCommand_ELSE(String arg) {
		if (ifResults.empty()) {
			error("ELSE with no IF");
		}

		IfState top = ifResults.peek();

		switch (top.state) {
		case IfState.IF_ACTIVE:
		case IfState.IF_PREVIOUS:
			top.state = IfState.ELSE_INACTIVE;
			break;
		case IfState.IF_INACTIVE:
			top.state = IfState.ELSE_ACTIVE;
			break;
		case IfState.ELSE_ACTIVE:
		case IfState.ELSE_INACTIVE:
			error("ELSE after ELSE");
			break;
		}

		echo = top.isActive();
	}

	/**
	 * Handle the ELSEIF command.
	 *
	 * @param arg String the expression to test.
	 */
	private void doCommand_ELSEIF(String arg) {
		if (ifResults.empty()) {
			error("ELSEIF with no IF");
		}

		boolean satisfied;

		if (arg.isEmpty()) {
			satisfied = false;
		} else {
			try {
				ExpressionResult parseResult = ExpressionParser.parse(arg, this);

				if (!parseResult.isBoolean) {
					error(String.format("boolean expression required: '%s'", arg)); //$NON-NLS-1$
					return;
				}

				satisfied = parseResult.value != 0;
			} catch (ParseException pe) {
				error(pe);
				return;
			}
		}

		IfState top = ifResults.peek();

		switch (top.state) {
		case IfState.IF_ACTIVE:
			top.state = IfState.IF_PREVIOUS;
			break;
		case IfState.IF_INACTIVE:
			if (satisfied) {
				top.state = IfState.IF_ACTIVE;
			}
			break;
		case IfState.IF_PREVIOUS:
			break;
		case IfState.ELSE_ACTIVE:
		case IfState.ELSE_INACTIVE:
			error("ELSEIF after ELSE");
			break;
		}

		echo = top.isActive();
	}

	/**
	 * Handle the PR command.
	 *
	 * @param arg String the PR number to be added.
	 */
	private void doCommand_PR(String arg) {
		fixedPRs.add(arg);
	}

	/**
	 * Handle the IDEA command.
	 *
	 * @param arg String the PR number to be added.
	 */
	private void doCommand_IDEA(String arg) {
		newIDEAs.add(arg);
	}

	/**
	 * Handle the INCLUDEIF command.
	 *
	 * @param arg String the boolean expression involving flags
	 */
	private void doCommand_INCLUDEIF(String arg) {
		numberOfIncludeIfs++;
		if (numberOfIncludeIfs != 1) {
			warning("Extra INCLUDE-IF directive found on line " + lineCount + " will be ignored.");
			return;
		}
		if (arg.isEmpty()) {
			shouldInclude = false;
		} else {
			try {
				/* [PR 120411] Use a javadoc tag instead of TestBootpath preprocessor tag */
				if (preprocessingTestBootpath) {
					if (arg.indexOf("TestBootpath") != -1) {
						testBootpathJavaDocIsNeeded = true;
					}
				}

				ExpressionResult parseResult = ExpressionParser.parse(arg, this);

				if (!parseResult.isBoolean) {
					error(String.format("boolean expression required: '%s'", arg)); //$NON-NLS-1$
					return;
				}

				boolean expressionResult = parseResult.value != 0;

				boolean forceExclude = false;
				if (expressionResult) {
					for (String flag : flags) {
						if (requiredIncludeFlags.contains(flag)) {
							forceExclude = true;
							int pos = -1;
							while (pos <= arg.length()) {
								pos = arg.indexOf(flag, pos + 1);
								if (pos == -1) {
									break;
								}
								if (isWordStart(arg, pos) && isWordEnd(arg, pos + flag.length())) {
									forceExclude = false;
									break;
								}
							}
							if (!forceExclude) {
								break;
							}
						}
					}
				}
				shouldInclude = forceExclude ? false : expressionResult;
			} catch (ParseException pe) {
				error(pe);
				return;
			}
		}
	}

	/**
	 * Is the character at the given index the start of a word?
	 * That is, there's only whitespace between the preceding
	 * punctuation (if any) and that character.
	 */
	private static boolean isWordStart(String text, int index) {
		while (index > 0 && Character.isWhitespace(text.charAt(index - 1))) {
			index -= 1;
		}
		return (index == 0) || isOperatorOrBracket(text.charAt(index - 1));
	}

	/**
	 * Is the character at the given index the end of a word?
	 * That is, there's only whitespace between the following
	 * punctuation (if any) and that character.
	 */
	private static boolean isWordEnd(String text, int index) {
		int length = text.length();
		while (index < length && Character.isWhitespace(text.charAt(index))) {
			index += 1;
		}
		return (index == length) || isOperatorOrBracket(text.charAt(index));
	}

	/**
	 * Handle the ENDIF command.
	 *
	 * @param arg String the expression to test.
	 */
	private void doCommand_ENDIF(String arg) {
		if (ifResults.empty()) {
			error("ENDIF with no IF");
		}

		ifResults.pop();
		echo = ifResults.empty() || ifResults.peek().isActive();
	}

	/**
	 * Handle the IF command.
	 *
	 * @param arg String the expression to test.
	 */
	private void doCommand_IF(String arg) {
		boolean satisfied;

		if (arg.isEmpty()) {
			satisfied = false;
		} else {
			try {
				ExpressionResult parseResult = ExpressionParser.parse(arg, this);

				if (!parseResult.isBoolean) {
					error(String.format("boolean expression required: '%s'", arg)); //$NON-NLS-1$
					return;
				}

				satisfied = parseResult.value != 0;
			} catch (ParseException pe) {
				error(pe);
				return;
			}
		}

		IfState top = new IfState(satisfied, ifResults);

		ifResults.push(top);
		echo = top.isActive();
	}

	/**
	 * Handle the MSG command
	 *
	 * @param arg String the argument to this MSG command.
	 */
	private void doCommand_MSG(String arg) {
		if (externalMessages == null || !echo) {
			return;
		}

		try {
			MSGArg msgArg = new MSGArg(arg);
			String key = msgArg.getKey();
			String value = msgArg.getValue();
			boolean store = true;

			// First check to see if the key and value exist already
			if (externalMessages.containsKey(key)) {
				if (!value.equals(externalMessages.get(key))) {
					throw new SyntaxException("\"" + key + "\" already has the value \"" + externalMessages.get(key) + "\"");
				}
			} else if (externalMessages.containsValue(value)) {
				warning("MSG command warning: \"" + value + "\" is given multiple keys");
			}

			if (externalMessagesToBeAdded.containsKey(key)) {
				if (value.equals(externalMessagesToBeAdded.get(key))) {
					store = false;
				} else {
					throw new SyntaxException("\"" + key + "\" already has the value \"" + externalMessagesToBeAdded.get(key) + "\"");
				}
			} else if (externalMessagesToBeAdded.containsValue(value)) {
				warning("MSG command warning: \"" + value + "\" is given multiple keys");
			}

			if (store) {
				externalMessagesToBeAdded.put(key, value);
			}
			/* [PR 119681] Desing:952 - Core.JCL : Add external messages in generated source */
			if (msgCallInliner == null || msgCallInliner.inlineKeys) {
				value = value.replaceAll("\n", "\\\\n");
				value = value.replaceAll("\b", "\\\\b");
				value = value.replaceAll("\t", "\\\\t");
				value = value.replaceAll("\f", "\\\\f");
				value = value.replaceAll("\r", "\\\\r");
				msg_comment = "// " + key + " = " + value;
				addMSGcomment = true;
			}
		} catch (SyntaxException e) {
			warning("MSG command syntax error, command ignored: MSG " + arg + " - Detail: " + e.getMessage());
		}
	}

	/**
	 * Handles the JXERULE command. The quoted text following this command is
	 * added to the java/lang/jxeLink.rules file. Command syntax example: <code>
	 * [JXERULE "-includeField java.io.FileInputStream:java.io.FileInputStream.fd"]
	 * </code>
	 */
	private void doCommand_JXERULE(String arg) {
		if (!echo) {
			return;
		}

		StringBuilder buff = new StringBuilder();
		boolean quoted = false;
		boolean escapeChar = false;
		int argLength = arg.length();
		for (int i = 0; i < argLength; i++) {
			char current = arg.charAt(i);
			if (quoted) {
				if (escapeChar) {
					buff.append(current);
					escapeChar = false;
				} else {
					if (current == '\\') {
						escapeChar = true;
					} else if (current == '"') {
						quoted = false;
					} else {
						buff.append(current);
					}
				}
			} else {
				if (current == '"') {
					quoted = true;
				} else if (!Character.isWhitespace(current)) {
					warning("JXERULE command warning: Unquoted argument for command");
					return;
				}
			}
		}
		String rule = buff.toString();

		if (rule.length() > 0 && jxeRules != null) {
			jxeRules.add(rule);
		}
	}

	/**
	 * Tells the preprocessor to search for macros on the following line.
	 */
	private void doCommand_USEMACROS() {
		this.useMacros = true;
	}

	/**
	 * @param arg
	 */
	private void doCommand_SHARPINCLUDE(String arg) {
		if (!(arg.startsWith("/") || arg.startsWith(":", 1))) {
			arg = inFile.getParent() + File.separator + arg;
		}

		try {
			BufferedReader includeReader = new BufferedReader(new InputStreamReader(new FileInputStream(arg), charset));

			includeReader.mark(1024);

			String line = includeReader.readLine();

			if (line.indexOf("/*[INCLUDE-IF") == -1) {
				includeReader.reset();
			}

			inStack.push(in);
			in = includeReader;
		} catch (FileNotFoundException e) {
			error("Could not find #include file: " + arg, e);
		} catch (IOException e) {
			error("IO error when loading #include file: " + arg, e);
		} catch (Exception e) {
			error("Error when loading #include file: " + arg, e);
		}
	}

	/** ********************** end class MSGArg ************************** */

	/**
	 * Handle the ATTR command
	 *
	 * @param arg String the argument to this ATTR command.
	 */
	@SuppressWarnings("unused")
	private void doCommand_ATTR(String arg) {
		/* If we are inside an IF directive, do not process the attribute. */
		if (!echo) {
			return;
		}

		StringTokenizer st = new StringTokenizer(arg);
		String type = null;
		try {
			type = st.nextToken();

			if (type.equals("Trusted")) {
				try {
					int argNum = Integer.valueOf(st.nextToken()).intValue();
					jitAttributes.addTrustedMethodOrClass(st.nextToken(), argNum);
				} catch (NumberFormatException e) {
					warning("ATTR command warning: Expecting integer argument for 'Trusted'");
				}
			} else if (type.equals("Untrusted")) {
				try {
					int argNum = Integer.valueOf(st.nextToken()).intValue();
					jitAttributes.addUntrustedMethod(st.nextToken(), argNum);
				} catch (NumberFormatException e) {
					warning("ATTR command warning: Expecting integer argument for 'Untrusted'");
				}
			} else if (type.equals("Recognized")) {
				jitAttributes.addRecognizedMethod(st.nextToken());
			} else if (type.equals("SkipNullChecks")) {
				jitAttributes.skipNullChecksFor(st.nextToken());
			} else if (type.equals("SkipBoundChecks")) {
				jitAttributes.skipBoundChecksFor(st.nextToken());
			} else if (type.equals("SkipCheckCasts")) {
				jitAttributes.skipCheckCastsFor(st.nextToken());
			} else if (type.equals("SkipDivChecks")) {
				jitAttributes.skipDivChecksFor(st.nextToken());
			} else if (type.equals("SkipArrayStoreChecks")) {
				jitAttributes.skipArrayStoreChecksFor(st.nextToken());
			} else if (type.equals("SkipChecksOnArrayCopies")) {
				jitAttributes.skipChecksOnArrayCopiesFor(st.nextToken());
			} else if (type.equals("SkipZeroInitializationOnNewarrays")) {
				jitAttributes.skipZeroInitializationOnNewarraysFor(st.nextToken());
			} else {
				warning("ATTR command warning: bogus type '" + type + "'");
			}
		} catch (PreprocessorException ppe) {
			warning(ppe.toString());
		} catch (NoSuchElementException e) {
			if (type == null) {
				warning("ATTR command warning: no type specified");
			} else {
				warning("ATTR command warning: expecting another argument for type '" + type + "'");
			}
			return;
		}
		if (st.hasMoreTokens()) {
			warning("ATTR command warning: ignoring extra arguments");
		}
	}

	/**
	 * Represents and parses MSG command arguments.
	 */
	private static final class MSGArg {
		private final String arg;
		private final String key;
		private final String value;
		private int begin;
		private int end;

		/**
		 * Construct an instance of this class which parses the string arg.
		 *
		 * @param arg String the command argument to parse
		 * @throws SyntaxException If arg does not have the correct syntax for
		 *             an MSG argument string
		 */
		MSGArg(String arg) throws SyntaxException {
			this.arg = arg;
			key = determineKey();
			value = determineValue();
		}

		/**
		 * Answers the receiver's key string
		 *
		 * @return String the receiver's key string
		 */
		String getKey() {
			return key;
		}

		/**
		 * Answers the receiver's value string
		 *
		 * @return String the receiver's value string
		 */
		String getValue() {
			return value;
		}

		/**
		 * Beginning at an offset this.begin in this.arg, this method steps past
		 * leading whitespace then returns the quoted string following this
		 * whitespace, in the process advancing this.end to point to the closing
		 * quote for this quoted string.
		 *
		 * @return String the next quoted string
		 */
		private String getNextQuotedString() throws SyntaxException {
			while (Character.isWhitespace(arg.charAt(begin))) {
				begin++;
			}

			if (arg.charAt(begin) != '"') {
				throw new SyntaxException("A non-whitespace character occurred where only whitespace should.");
			}

			begin++;
			end = indexOfUnescaped(arg, '"', begin);
			if (end == -1) {
				throw new SyntaxException("Non-terminated quoted string.");
			}

			return substringUnescaped(arg, '"', begin, end);
		}

		/**
		 * Determines and answers the key string for the command argument being
		 * parsed. Advances this.end to point to the closing quote following the
		 * key string.
		 *
		 * @return String the key
		 * @throws SyntaxException
		 */
		private String determineKey() throws SyntaxException {
			// get the key
			begin = 0;
			return getNextQuotedString();
		}

		/**
		 * If called following a call to getKey(), this method will determine
		 * and answer the value string. A precondition for this method is that
		 * 'end' must point to a character which is followed by whitespace,
		 * which must, in turn, be followed by the quoted value string
		 *
		 * @return String the receiver's value string
		 */
		private String determineValue() throws SyntaxException {
			// get the value
			begin = end + 1;
			while (Character.isWhitespace(arg.charAt(begin))) {
				begin++;
			}

			if (arg.charAt(begin) != ',') {
				throw new SyntaxException("A comma and optional whitespace expected between quoted key and value strings.");
			}

			begin++;
			String value = getNextQuotedString();
			int index = value.indexOf('\\');

			if (index < 0 || index + 1 >= value.length()) {
				return value;
			}

			int last = 0;
			int from = 0;
			StringBuilder result = new StringBuilder();
			while (index >= 0) {
				int special = "\\btnfr".indexOf(value.charAt(index + 1));
				if (special >= 0) {
					result.append(value.substring(from, index));
					result.append("\\\b\t\n\f\r".charAt(special));
					last = index + 2;
					from = index + 2;
				} else {
					last = index + 1;
				}
				index = value.indexOf('\\', last);
			}

			if (from == 0) {
				return value;
			}
			result.append(value.substring(from));
			return result.toString();
		}
	}

	/** ********************** end class MSGArg ************************** */

	/**
	 * Finds the index, greater than or equal to fromIndex, of a 'non-escaped'
	 * instance of the character ch in the string str. 'non-escaped' means not
	 * directly preceded by an odd number of backslashes. Returns -1 if an
	 * unescaped instance of ch is not found.
	 *
	 * @param str String the string to search
	 * @param ch int the int value of the character to search for
	 * @param fromIndex int the offset in str at which to begin the search
	 */
	static int indexOfUnescaped(String str, int ch, int fromIndex) {
		int result = -1;
		boolean escaped = true;
		while (escaped) {
			result = str.indexOf(ch, fromIndex);
			escaped = false;
			int i = result - 1;
			while (i >= 0 && str.charAt(i) == '\\') {
				escaped = !escaped;
				i--;
			}
			fromIndex = result + 1;
		}
		return result;
	}

	static String substringUnescaped(String str, char escapedChar, int begin, int end) {
		StringBuilder strBuffer = new StringBuilder(str);
		for (int i = begin; i < end; i++) {
			if (strBuffer.charAt(i) == '\\' && strBuffer.charAt(i + 1) == escapedChar) {
				strBuffer.deleteCharAt(i);
				end--;
			}
		}
		return strBuffer.substring(begin, end);
	}

	/**
	 * Answers a new string based on str in which all instances of charToEscape
	 * are preceded by a backslash.
	 *
	 * @param str String the string to escape
	 * @param charToEscape char the character to be escaped
	 * @return String the escaped version of str
	 */
	static String escape(String str, char charToEscape) {
		StringBuilder buf = new StringBuilder(str);
		int i = 0;
		int lengthDiff = 0;
		while ((i = str.indexOf(charToEscape, i)) != -1) {
			buf.insert(i + lengthDiff, '\\');
			lengthDiff++;
			i++;
		}
		return buf.toString();
	}

	/**
	 * Each preprocessor may use a single instance of this class to replace
	 * specially-formatted calls to Msg.getString() with appropriate
	 * String-valued expressions at preprocess time.
	 */
	private final class MsgCallInliner {
		// arrays and method calls
		private int pos;
		private int closeMethod;
		private String lineString;
		private StringBuilder lineBuf;
		final boolean inlineKeys;
		private int callIdx;

		MsgCallInliner(boolean inlineKeys) {
			super();
			this.inlineKeys = inlineKeys;
		}

		void replaceMsg_getString() throws SyntaxException {
			boolean argumentsSupplied = true;
			lineString = new String(line, 0, lineLength);
			lineBuf = new StringBuilder(lineString);

			// Determine if a reference to Msg.getString is on this line
			// otherwise return
			callIdx = lineString.indexOf("com.ibm.oti.util.Msg.getString");
			if (callIdx == -1) {
				callIdx = lineString.indexOf("com.ibm.oti.rmi.util.Msg.getString");
				if (callIdx == -1) {
					callIdx = lineString.indexOf("Msg.getString");
					if (callIdx == -1) {
						return;
					}
					pos = callIdx + 13;
				} else {
					pos = callIdx + 34;
				}
			} else {
				pos = callIdx + 30;
			}
			skipWhite();

			if (line[pos] != '(') {
				// Make sure it is a method call
				return;
			}
			pos++;
			skipWhite();

			if (line[pos] != '"') {
				// Make sure the first argument is a string literal
				return;
			}
			pos++;

			// Find position of closing ")" for method call
			closeMethod = closeParenthesis(true, pos - 1);
			if (closeMethod == -1) {
				// Make sure the method call terminates on this line
				throw new SyntaxException("Call to Msg.getString() spanned multiple lines in '" + file + "'");
			}

			// Take key string and begin replacement message string
			int begin = pos;
			pos = indexOfUnescaped(lineString, '"', begin);
			String key = substringUnescaped(lineString, '"', begin, pos);
			String messageStr = inlineKeys ? key : externalMessagesToBeAdded.get(key);
			if (messageStr == null) {
				throw new SyntaxException("Call to Msg.getString() specified a key string with no corresponding MSG statement.");
			}

			if (!advanceIfObjectArray()) {
				pos++;
				skipWhite();
				if (line[pos] == ')') {
					argumentsSupplied = false; // no arguments supplied
				} else if (line[pos] != ',') {
					throw new SyntaxException("Comma expected after literal key string in Msg.getString() call.");
				} else {
					pos++;
				}
			}

			// int callEnd = lineString.indexOf(')', pos);
			messageStr = escape(messageStr, '"');
			/* [PR 120571] "\n" in external messages causes error */
			// messageStr = escape(messageStr, '\n');
			messageStr = messageStr.replaceAll("\n", "\\\\n");
			messageStr = messageStr.replaceAll("\b", "\\\\b");
			messageStr = messageStr.replaceAll("\t", "\\\\t");
			messageStr = messageStr.replaceAll("\f", "\\\\f");
			messageStr = messageStr.replaceAll("\r", "\\\\r");

			messageStr = "\"" + messageStr + "\"";
			lineBuf.replace(callIdx, closeMethod + 1, messageStr);
			if (argumentsSupplied) {
				getAndSubstituteObjectStrings(callIdx + messageStr.length() - 1);
			}
			substituteLine();
		}

		private boolean advanceIfObjectArray() {
			int i = lineString.indexOf("new", pos);
			// added pos so that it will not check before current position
			if (i != -1 && i < closeMethod) {
				for (i += 3; Character.isWhitespace(line[i]); i++) {
					// skip whitespace
				}

				if (lineString.regionMatches(i, "Object", 0, 6)) {
					for (i += 6; Character.isWhitespace(line[i]); i++) {
						// skip whitespace
					}

					if (line[i] == '[') {
						for (i += 1; Character.isWhitespace(line[i]); i++) {
							// skip whitespace
						}

						if (line[i] == ']') {
							for (i += 1; Character.isWhitespace(line[i]); i++) {
								// skip whitespace
							}

							if (line[i] == '{') {
								pos = i + 1;
								return true;
							}
						}
					}
				}
			}
			return false;
		}

		private void skipWhite() {
			while (Character.isWhitespace(line[pos])) {
				pos++;
			}
		}

		private void getAndSubstituteObjectStrings(int messageEnd) throws SyntaxException {
			int objNumber = 0;
			do {
				String objString = getNextObjectString();
				String objStub = "{" + NumberFormat.getNumberInstance().format(objNumber) + "}";
				String lineBufStr = lineBuf.toString();
				int stubIdx = lineBufStr.indexOf(objStub, callIdx);
				if (!inlineKeys && (stubIdx == -1 || stubIdx >= messageEnd)) {
					throw new SyntaxException("Call to Msg.getString() specified to many objects.");
				}

				if (inlineKeys) {
					int start = messageEnd + 1;
					int end = messageEnd + 1;
					String appendString = " + \" " + objStub + "=\" + " + objString;
					messageEnd += appendString.length();
					lineBuf.replace(start, end, appendString);
					lineBufStr = lineBuf.toString();
				} else {
					// KRLW changed to handle using an argument multiple times
					String originalObjString = objString;
					do {
						int start = stubIdx;
						int end = stubIdx + objStub.length();

						if (stubIdx == callIdx + 1) {
							objString = objString + "+\"";
							start--;
							messageEnd--;
						} else if (stubIdx + objStub.length() == messageEnd) {
							objString = "\"+" + objString;
							end++;
							messageEnd--;
						} else {
							objString = "\"+" + objString + "+\"";
						}

						messageEnd += objString.length() - objStub.length();
						lineBuf.replace(start, end, objString);

						lineBufStr = lineBuf.toString();
						stubIdx = lineBufStr.indexOf(objStub, callIdx);
						objString = originalObjString;
					} while (stubIdx > -1 && stubIdx < messageEnd);
				}
				objNumber++;
			} while (line[pos++] == ',');

			String lineBufStr = lineBuf.toString();
			while (lineBufStr.indexOf("+\"\"+") > -1) {
				int idx = lineBufStr.indexOf("+\"\"+");
				lineBuf.replace(idx, idx + 4, "+");
				lineBufStr = lineBuf.toString();
			}
		}

		private String getNextObjectString() throws SyntaxException {
			// boolean createdObject = false;
			skipWhite();
			int begin = pos;

			// If a string literal, pull it out and return it
			if (lineString.charAt(begin) == '"') {
				pos = indexOfUnescaped(lineString, '"', begin + 1);
				pos++;
				return substringUnescaped(lineString, '"', begin, pos);
			}

			// Make sure this is a valid java identifier or an opening
			// parenthesis
			if (!(Character.isJavaIdentifierStart(line[pos]) || line[pos] == '(')) {
				throw new SyntaxException("Object arguments to Msg.getString() and elements of the Object[] argument to Msg.getString() must be specified as literal strings or variable identifiers");
			}
			// while(Character.isJavaIdentifierPart(line[pos]))
			// pos++;

			boolean inString = false;
			boolean inSingleQuote = false;
			int parenCount = 0;
			int squigleCount = 0;
			--pos;
			do {
				++pos;
				while (line[pos] != ',' || inString || inSingleQuote) {
					if (!inSingleQuote && pos == indexOfUnescaped(lineString, '"', pos)) {
						inString = !inString;
					}
					if (!inString && pos == indexOfUnescaped(lineString, '\'', pos)) {
						inSingleQuote = !inSingleQuote;
					}

					if (!(inString || inSingleQuote)) {
						if (line[pos] == '(') {
							parenCount++;
						} else if (line[pos] == ')') {
							parenCount--;
						} else if (line[pos] == '{') {
							squigleCount++;
						} else if (line[pos] == '}') {
							squigleCount--;
						}

						if (parenCount == -1) {
							break;
						}
						if (squigleCount == -1) {
							break;
						}
					}
					pos++;
				}
			} while (parenCount > 0 || squigleCount > 0);

			// Check the validity of the arguments.
			char ch = line[pos];
			if (!(Character.isWhitespace(ch) || ch == ')' || ch == ',' || ch == '}')) {
				throw new SyntaxException("Object arguments to Msg.getString() and elements of the Object[] argument to Msg.getString() must be specified as literal strings or variable identifiers");
			}

			String objString = lineString.substring(begin, pos);

			skipWhite();
			return objString;
		}

		private void substituteLine() {
			replaceLine(lineBuf.toString());
		}

		private int closeParenthesis(boolean inString, int index) {
			int parenthesisCount = 1;
			boolean inSingleQuote = false;

			while (parenthesisCount > 0 && index < (line.length - 1)) {
				index++;
				if (!inSingleQuote && index == indexOfUnescaped(lineString, '"', index)) {
					inString = !inString;
				}
				if (!inString && index == indexOfUnescaped(lineString, '\'', index)) {
					inSingleQuote = !inSingleQuote;
				}

				if (!(inString || inSingleQuote)) {
					if (line[index] == '(') {
						parenthesisCount++;
					} else if (line[index] == ')') {
						parenthesisCount--;
					}
				}
			}

			if (parenthesisCount > 0) {
				return -1;
			}
			return index;
		}

	}

	/** ********************** end class MsgCallInliner ***************** */

	/**
	 * Each preprocessor may use a single instance of this class to replace
	 * the name of the class used to lookup messages
	 */
	private final class MsgClassSubstituter {
		private final static String GET_STRING = ".getString";
		private final static String DEFAULT_MSG_CLASS = "com.ibm.oti.util.Msg";
		private final static String DEFAULT_UNQUALIFIED_MSG_CLASS = "Msg";
		private final static String DEFAULT_MSG_GET_STRING = DEFAULT_MSG_CLASS + GET_STRING;
		private final static String DEFAULT_UNQUALIFIED_MSG_GET_STRING = DEFAULT_UNQUALIFIED_MSG_CLASS + GET_STRING;
		// arrays and method calls
		private int pos;
		private int closeMethod;
		private String lineString;
		private StringBuilder lineBuf;
		private String msgClassName;
		private int callIdx;
		int classEnd = 0;

		MsgClassSubstituter() {
			super();
		}

		public void setMessageClassName(String msgClassName) {
			this.msgClassName = msgClassName;
		}

		void replaceMsgClass() throws SyntaxException {
			lineString = new String(line, 0, lineLength);
			lineBuf = new StringBuilder(lineString);

			// Determine if a reference to Msg.getString is on this line
			// otherwise
			// return
			callIdx = lineString.indexOf(DEFAULT_MSG_GET_STRING);
			if (callIdx == -1) {
				callIdx = lineString.indexOf(DEFAULT_UNQUALIFIED_MSG_GET_STRING);
				if (callIdx == -1) {
					return;
				}
				pos = callIdx + DEFAULT_UNQUALIFIED_MSG_GET_STRING.length();
				classEnd = callIdx + DEFAULT_UNQUALIFIED_MSG_CLASS.length();
			} else {
				pos = callIdx + DEFAULT_MSG_GET_STRING.length();
				classEnd = callIdx + DEFAULT_MSG_CLASS.length();
			}
			skipWhite();

			if (line[pos] != '(') {
				// Make sure it is a method call
				return;
			}
			pos++;
			skipWhite();

			if (line[pos] != '"') {
				// Make sure the first argument is a string literal
				return;
			}
			pos++;

			// Find position of closing ")" for method call
			closeMethod = closeParenthesis(true, pos - 1);
			if (closeMethod == -1) {
				// Make sure the method call terminates on this line
				throw new SyntaxException("Call to Msg.getString() spanned multiple lines in '" + file + "'");
			}

			// now replace the class used
			lineBuf.replace(callIdx, classEnd, msgClassName);

			substituteLine();
		}

		private void skipWhite() {
			while (Character.isWhitespace(line[pos])) {
				pos++;
			}
		}

		private int closeParenthesis(boolean inString, int index) {
			int parenthesisCount = 1;
			boolean inSingleQuote = false;

			while (parenthesisCount > 0 && index < (line.length - 1)) {
				index++;
				if (!inSingleQuote && index == indexOfUnescaped(lineString, '"', index)) {
					inString = !inString;
				}
				if (!inString && index == indexOfUnescaped(lineString, '\'', index)) {
					inSingleQuote = !inSingleQuote;
				}

				if (!(inString || inSingleQuote)) {
					if (line[index] == '(') {
						parenthesisCount++;
					} else if (line[index] == ')') {
						parenthesisCount--;
					}
				}
			}

			if (parenthesisCount > 0) {
				return -1;
			}
			return index;
		}

		private void substituteLine() {
			replaceLine(lineBuf.toString());
		}

	}

	/** ********************** end class MsgCallInliner ***************** */

	/**
	 * Exception class used in parsing MSG commands
	 */
	private static final class SyntaxException extends Exception {
		/**
		 * serialVersionUID
		 */
		private static final long serialVersionUID = 3256728398444573747L;

		/**
		 * Syntax exception constructor.
		 *
		 * @param       msg     the exception message
		 */
		public SyntaxException(String msg) {
			super(msg);
		}
	}

	/** ********************** end class SyntaxException ***************** */

	private static final class ExpressionScanner {

		static final class Token {

			final int kind;

			final int offset;

			final String text;

			Token(int kind, String text, int offset) {
				super();
				this.kind = kind;
				this.offset = offset;
				this.text = text;
			}

		}

		// tokens
		static final int TK_EOI = 0;
		static final int TK_IDENT = 1;
		static final int TK_NUMBER = 2;
		static final int TK_LEFT_PAREN = 3;
		static final int TK_RIGHT_PAREN = 4;
		static final int TK_AND = 5;
		static final int TK_NOT = 6;
		static final int TK_OR = 7;
		static final int TK_XOR = 8;
		static final int TK_LESS_THAN = 9;
		static final int TK_LESS_EQUAL = 10;
		static final int TK_EQUAL = 11;
		static final int TK_NOT_EQUAL = 12;
		static final int TK_GREATER_EQUAL = 13;
		static final int TK_GREATER_THAN = 14;

		private int index;

		private final String input;

		private final int length;

		private Token nextToken;

		ExpressionScanner(String input) {
			super();
			this.input = input;
			this.length = input.length();
			this.nextToken = null;
		}

		private void skipWhitespace() {
			while (index < length && Character.isWhitespace(input.charAt(index))) {
				index += 1;
			}
		}

		Token getNextToken() throws ParseException {
			Token token = nextToken;

			if (token != null) {
				nextToken = null;
				return token;
			}

			skipWhitespace();

			if (index >= length) {
				return new Token(TK_EOI, "", index);
			}

			int start = index;
			char nextChar = input.charAt(start);

			index += 1;

			switch (nextChar) {
			case '(':
				token = new Token(TK_LEFT_PAREN, "(", start);
				break;

			case ')':
				token = new Token(TK_RIGHT_PAREN, ")", start);
				break;

			case '&':
				token = new Token(TK_AND, "&", start);
				break;

			case '!':
				if (index < length && input.charAt(index) == '=') {
					index += 1;
					token = new Token(TK_NOT_EQUAL, "!=", start);
				} else {
					token = new Token(TK_NOT, "!", start);
				}
				break;

			case '|':
				token = new Token(TK_OR, "|", start);
				break;

			case '^':
				token = new Token(TK_XOR, "^", start);
				break;

			case '<':
				if (index < length && input.charAt(index) == '=') {
					index += 1;
					token = new Token(TK_LESS_EQUAL, "<=", start);
				} else {
					token = new Token(TK_LESS_THAN, "<", start);
				}
				break;

			case '=':
				if (index < length && input.charAt(index) == '=') {
					index += 1;
					token = new Token(TK_EQUAL, "==", start);
				} else {
					throw new ParseException("unrecognized token '='", start);
				}
				break;

			case '>':
				if (index < length && input.charAt(index) == '=') {
					index += 1;
					token = new Token(TK_GREATER_EQUAL, ">=", start);
				} else {
					token = new Token(TK_GREATER_THAN, ">", start);
				}
				break;

			default:
				if (Character.isDigit(nextChar)) {
					while (index < length && Character.isDigit(input.charAt(index))) {
						index += 1;
					}

					String number = input.substring(start, index);

					token = new Token(TK_NUMBER, number, start);
				} else {
					while (index < length && !isOperatorOrBracket(input.charAt(index))) {
						index += 1;
					}

					String id = input.substring(start, index).trim();

					token = new Token(TK_IDENT, id, start);
				}
				break;
			}

			return token;
		}

		void pushBackToken(Token token) {
			if (nextToken != null) {
				throw new IllegalStateException();
			}

			nextToken = token;
		}

	}

	private static final class ExpressionResult {

		final boolean isBoolean;

		final int value;

		ExpressionResult(boolean value) {
			super();
			this.isBoolean = true;
			this.value = value ? 1 : 0;
		}

		ExpressionResult(int value) {
			super();
			this.isBoolean = false;
			this.value = value;
		}

	}

	private static final class ExpressionParser {

		/*
		 * Expression
		 *   : Term
		 *   | Expression '&' Term
		 *   | Expression '|' Term
		 *   | Expression '^' Term
		 *
		 * Term
		 *   : Primary
		 *   | Primary '<'  Primary
		 *   | Primary '<=' Primary
		 *   | Primary '==' Primary
		 *   | Primary '!=' Primary
		 *   | Primary '>=' Primary
		 *   | Primary '>'  Primary
		 *
		 * Primary
		 *   : Identifier
		 *   | Number
		 *   | '!' Primary
		 *   | '(' Expression ')'
		 */
		static ExpressionResult parse(String input, JavaPreprocessor processor) throws ParseException {
			ExpressionScanner scanner = new ExpressionScanner(input);
			ExpressionParser parser = new ExpressionParser(processor, scanner);
			ExpressionResult result = parser.parseExpression();
			ExpressionScanner.Token token = scanner.getNextToken();

			if (token.kind == ExpressionScanner.TK_EOI) {
				return result;
			} else {
				throw new ParseException("malformed expression", token.offset);
			}
		}

		/**
		 * Evaluate a combination of two boolean values.
		 */
		private static boolean combineBoolean(int lhs, int operator, int rhs) {
			boolean lhsBool = lhs != 0;
			boolean rhsBool = rhs != 0;

			switch (operator) {
			case ExpressionScanner.TK_AND:
				return lhsBool & rhsBool;
			case ExpressionScanner.TK_OR:
				return lhsBool | rhsBool;
			case ExpressionScanner.TK_XOR:
				return lhsBool ^ rhsBool;
			default:
				throw new IllegalArgumentException();
			}
		}

		/**
		 * Evaluate a comparison of two numeric values.
		 */
		private static boolean compareNumeric(int lhs, int operator, int rhs) {
			switch (operator) {
			case ExpressionScanner.TK_LESS_THAN:
				return lhs < rhs;
			case ExpressionScanner.TK_LESS_EQUAL:
				return lhs <= rhs;
			case ExpressionScanner.TK_EQUAL:
				return lhs == rhs;
			case ExpressionScanner.TK_NOT_EQUAL:
				return lhs != rhs;
			case ExpressionScanner.TK_GREATER_EQUAL:
				return lhs >= rhs;
			case ExpressionScanner.TK_GREATER_THAN:
				return lhs > rhs;
			default:
				throw new IllegalArgumentException();
			}
		}

		private final JavaPreprocessor processor;

		private final ExpressionScanner scanner;

		private ExpressionParser(JavaPreprocessor processor, ExpressionScanner scanner) {
			super();
			this.processor = processor;
			this.scanner = scanner;
		}

		/*
		 * Expression
		 *   : Term
		 *   | Expression '&' Term
		 *   | Expression '|' Term
		 *   | Expression '^' Term
		 */
		private ExpressionResult parseExpression() throws ParseException {
			ExpressionResult result = parseTerm();

			for (;;) {
				ExpressionScanner.Token token = scanner.getNextToken();

				switch (token.kind) {
				case ExpressionScanner.TK_AND:
				case ExpressionScanner.TK_OR:
				case ExpressionScanner.TK_XOR:
					ExpressionResult rhs = parseTerm();

					if (result.isBoolean & rhs.isBoolean) {
						result = new ExpressionResult(combineBoolean(result.value, token.kind, rhs.value));
					} else {
						throw new ParseException(token.text + " requires boolean operands", token.offset);
					}
					break;

				default:
					scanner.pushBackToken(token);
					return result;
				}
			}
		}

		/*
		 * Term
		 *   : Primary
		 *   | Primary '<'  Primary
		 *   | Primary '<=' Primary
		 *   | Primary '==' Primary
		 *   | Primary '!=' Primary
		 *   | Primary '>=' Primary
		 *   | Primary '>'  Primary
		 */
		private ExpressionResult parseTerm() throws ParseException {
			ExpressionResult result = parsePrimary();
			ExpressionScanner.Token token = scanner.getNextToken();

			switch (token.kind) {
			case ExpressionScanner.TK_LESS_THAN:
			case ExpressionScanner.TK_LESS_EQUAL:
			case ExpressionScanner.TK_EQUAL:
			case ExpressionScanner.TK_NOT_EQUAL:
			case ExpressionScanner.TK_GREATER_EQUAL:
			case ExpressionScanner.TK_GREATER_THAN:
				ExpressionResult rhs = parsePrimary();

				if (result.isBoolean | rhs.isBoolean) {
					throw new ParseException(token.text + " requires numeric operands", token.offset);
				} else {
					result = new ExpressionResult(compareNumeric(result.value, token.kind, rhs.value));
				}
				break;

			default:
				scanner.pushBackToken(token);
				break;
			}

			return result;
		}

		/*
		 * Primary
		 *   : Identifier
		 *   | Number
		 *   | '!' Primary
		 *   | '(' Expression ')'
		 */
		private ExpressionResult parsePrimary() throws ParseException {
			ExpressionResult result;
			ExpressionScanner.Token token = scanner.getNextToken();

			switch (token.kind) {
			case ExpressionScanner.TK_IDENT:
				result = processor.resolve(token.text);
				break;

			case ExpressionScanner.TK_NUMBER:
				result = new ExpressionResult(Integer.parseInt(token.text));
				break;

			case ExpressionScanner.TK_NOT:
				result = parsePrimary();
				if (result.isBoolean) {
					result = new ExpressionResult(result.value == 0);
				} else {
					throw new ParseException(token.text + " requires a boolean operand", token.offset);
				}
				break;

			case ExpressionScanner.TK_LEFT_PAREN:
				result = parseExpression();
				token = scanner.getNextToken();
				if (token.kind != ExpressionScanner.TK_RIGHT_PAREN) {
					throw new ParseException("expected ')'", token.offset);
				}
				break;

			default:
				throw new ParseException("malformed expression", token.offset);
			}

			return result;
		}

	}

	/**
	 * Print out an error message and quit.
	 */
	private void error(String msg) {
		error(msg, null);
	}

	private void error(String msg, Exception cause) {
		if (msg == null) {
			msg = "";
		}
		this.error = new PreprocessorWarning(msg, lineCount, 0, lineLength);
		String fullMessage = msg + " [" + file + ", line " + lineCount + ']';
		writeLog(fullMessage);
		PreprocessorException exception = new PreprocessorException(fullMessage);
		if (cause != null) {
			exception.initCause(cause);
		}
		throw exception;
	}

	private void error(ParseException pe) {
		String msg = pe.getMessage();
		if (msg == null) {
			msg = "";
		}
		this.error = new PreprocessorWarning(msg, lineCount, 0, lineLength);
		String fullMessage = msg + " [" + file + ", line " + lineCount + ", column " + pe.getErrorOffset() + ']';
		writeLog(fullMessage);
		PreprocessorException exception = new PreprocessorException(fullMessage);
		exception.initCause(pe);
		throw exception;
	}

	/**
	 * Write a message to the log file.
	 */
	private static void writeLog(String msg) {
		final String logFileName = "jpp-error.txt";
		if (msg == null || msg.equals("")) {
			return;
		}
		try (FileOutputStream fos = new FileOutputStream(logFileName, true);
			 PrintWriter out = new PrintWriter(fos)) {
			out.println(msg);
		} catch (IOException e) {
			// ignore exceptions
		}
	}

	/**
	 * Answer the number of lines which have been preprocessed.
	 *
	 * @return int the number of lines that have been preprocessed.
	 */
	public int getLineCount() {
		return lineCount;
	}

	/**
	 * Output a line break to the output stream.
	 */
	private void newLine() {
		try {
			if (echo) {
				out.write(newLine);
			}
		} catch (IOException ex) {
			error("IOException on write", ex);
		}
	}

	/**
	 * Process the streams. Answer if the stream should be included based on the
	 * first INCLUDE-IF directive found.
	 * <p>
	 * @return boolean the result of the first INCLUDE-IF directive in the
	 *         input, or if none is found, true iff the flag "includeIfUnsure"
	 *         is defined (this is a hack until all source files have an
	 *         INCLUDE-IF directive)
	 */
	public boolean preprocess() {
		long start = System.currentTimeMillis();
		FileInputStream fis = null;

		try {
			fis = new FileInputStream(inFile);
			this.in = new BufferedReader(new InputStreamReader(fis, charset));
		} catch (FileNotFoundException e) {
			error("File not found: " + inFile.getAbsolutePath(), e);
		}

		{ // populate the map of numeric macros
			final Pattern Numeric = Pattern.compile("\\d+");

			numericMacros.clear();

			for (Map.Entry<String, String> entry : macros.entrySet()) {
				String value = entry.getValue();

				if (Numeric.matcher(value).matches()) {
					numericMacros.put(entry.getKey(), Integer.valueOf(value));
				}
			}
		}

		/* [PR] The state machine based version was too brittle. Here is a simpler, line base version. */
		// parameter initialization
		testBootpathJavaDocIsNeeded = false;
		isTestBootpathJavaDocTagWritten = false;

		// assume a copyright notice is present
		foundCopyright = true;

		while (readLine()) {
			if (addMSGcomment) {
				try {
					for (int i = 0; i < line.length && (line[i] == '\t' || line[i] == ' '); ++i) {
						out.write(line[i]);
					}
					out.write(msg_comment);
					newLine();
				} catch (IOException e) {
					error("IOException on write: " + e.getMessage(), e);
				}
				addMSGcomment = false;
				msg_comment = "";
			}
			if (replaceMsg_getString && echo) {
				try {
					msgCallInliner.replaceMsg_getString();
				} catch (SyntaxException e) {
					warning("Improper usage of Msg.getString(). Method call could not be removed from code. Detail: " + e.getMessage());
				}
			}

			if (substituteMsgClass && echo) {
				try {
					msgClassSubstituter.replaceMsgClass();
				} catch (SyntaxException e) {
					warning("Improper usage of Msg.getString(). Message class could not be substituted. Detail: " + e.getMessage());
				}
			}

			if (!continuation && findCommand()) {
				doCommand();
				if (!shouldInclude) {
					break;
				}
			} else {
				if (!shouldInclude) {
					break;
				}

				// check for class definition
				if (jxePreserveApi && echo) {
					if (rootClassName == null) {
						checkPackageDeclaration();
					}
					checkClassDeclaration();
				}
				// replace macros
				if (useMacros && macros != null) {
					replaceMacros();
					useMacros = false;
				}
				if ((lineLength != 0) || (numberOfBlankLines < 1)) {
					// don't print the line if it's a blank and we've already
					// printed enough blanks
					writeLine();
				}
			}
		}

		timeTaken = System.currentTimeMillis() - start;
		if (numberOfIncludeIfs == 0) {
			if (!noWarnIncludeIf) {
				if (includeIfUnsure) {
					warnings.add(new PreprocessorWarning("No INCLUDE-IF directives found.  File will be INCLUDED", lineCount, 0, lineLength, false));
				} else {
					warnings.add(new PreprocessorWarning("No INCLUDE-IF directives found.  File will be EXCLUDED", lineCount, 0, lineLength, false));
				}
			}
		} else if (shouldInclude && !foundCopyright) {
			warning("No copyright");
		}

		if (shouldInclude) {
			externalMessages.putAll(externalMessagesToBeAdded);
		}

		try {
			if (fis != null) {
				fis.close();
			}

			if (in != null) {
				in.close();
			}

			if (this.metadataOut != null) {
				this.metadataOut.flush();
			}

			if (this.out != null) {
				this.out.flush();
			}
		} catch (IOException e) {
			error(e.toString(), e);
		}

		return shouldInclude;
	}

	/**
	 * Scans the current line for a public class definition. If one is found, it
	 * is preserved by including a <code>-includeLibraryClass</code> rule in the
	 * jxeRules collection.
	 */
	private void checkClassDeclaration() {
		try {
			String line = new String(this.line, 0, lineLength);
			if (line.indexOf("class") == -1 && line.indexOf("interface") == -1) {
				return;
			}

			boolean isDefinition = false;
			boolean isPublic = false;
			StringTokenizer tokenizer = new StringTokenizer(line, " \n\r\t", false);
			String className = null;
			while (tokenizer.hasMoreTokens()) {
				String token = tokenizer.nextToken();
				if (token.equals("public")) {
					isPublic = true;
					continue;
				} else if (token.equals("private") || token.equals("protected")) {
					return;
				} else if (token.equals("static") || token.equals("final") || token.equals("abstract")) {
					continue;
				} else if (token.equals("class") || token.equals("interface")) {
					isDefinition = true;
					continue;
				} else if (isDefinition && isPublic) {
					// this is the identifier
					className = token;
					break;
				} else {
					return;
				}
			}
			if (className == null || className.equals(rootClassName)) {
				return;
			}

			if (this.rootClassName != null) {
				// this is a inner class
				className = this.rootClassName + "$" + className;
			} else {
				this.rootClassName = className;
			}

			if (this.packageName != null) {
				className = this.packageName + "." + className;

				if (packageName.startsWith("java.") || packageName.startsWith("javax.")) {
					jxeRules.add("-includeLibraryClass " + className);
				}
			}
		} catch (StringIndexOutOfBoundsException e) {
			// ignore
		}
	}

	/**
	 * Scans the current line for a package declaration.
	 */
	private void checkPackageDeclaration() {
		try {
			String line = new String(this.line, 0, this.lineLength);
			int posPackage = line.indexOf("package");
			if (posPackage == -1) {
				return;
			}

			int posIdentifierStart = posPackage + "package".length();

			while (!Character.isJavaIdentifierStart(line.charAt(posIdentifierStart))) {
				posIdentifierStart++;
			}

			int posIdentifierEnd = posIdentifierStart + 1;
			while (!(line.charAt(posIdentifierEnd) == ';')) {
				posIdentifierEnd++;
			}

			this.packageName = line.substring(posIdentifierStart, posIdentifierEnd).trim();
		} catch (StringIndexOutOfBoundsException e) {
			// ignore
		}
	}

	/**
	 * Replaces macro identifiers with their associated values on this line.
	 * Macro identifiers are denoted by %%MACRO@macroname%%, where macroname is
	 * the name of the macro to retrieve the value from. The macro values are
	 * stored in the macros Properties object, set in the constructor.
	 */
	private void replaceMacros() {
		final String macroStartId = "%%MACRO@";
		final String macroEndId = "%%";
		String line = new String(this.line, 0, this.lineLength);
		int posMacroStart = 0;
		int posMacroEnd = 0;
		while ((posMacroStart = line.indexOf(macroStartId, posMacroEnd)) != -1) {
			int posIdentifierStart = posMacroStart + macroStartId.length();
			int posIdentifierEnd = line.indexOf(macroEndId, posIdentifierStart);
			if (posIdentifierEnd == -1) {
				warning("Unterminated macro");
				return;
			}
			if (posIdentifierEnd <= posIdentifierStart) {
				error("A macro identifer must be at least one character long");
				return;
			}

			posMacroEnd = posIdentifierEnd + macroEndId.length();

			String identifier = line.substring(posIdentifierStart, posIdentifierEnd);
			String replacement = macros.get(identifier);
			if (replacement == null) {
				warning("A replacement for the macro \"" + identifier + "\" could not be found");
				replacement = "";
			}

			StringBuilder newLine = new StringBuilder(line.length() - identifier.length() + replacement.length());
			if (posMacroStart > 0) {
				newLine.append(line.substring(0, posMacroStart));
			}
			newLine.append(replacement);
			if (posMacroEnd < line.length() - 1) {
				newLine.append(line.substring(posMacroEnd, line.length()));
			}

			line = newLine.toString();

			// line has now changed in size thus we need to update posMacroEnd otherwise
			// it will point to some other place and indexOf(str,int) will give us a wrong value
			posMacroEnd = (posMacroEnd + replacement.length()) - (identifier.length() + macroStartId.length() + macroEndId.length());
		}
		replaceLine(line);
	}

	/**
	 * Read the next line of input. If there are no characters available answer
	 * false, otherwise fill the input line buffer with the characters,
	 * excluding the trailing LF, remember the line length, and answer true.
	 * Strip out all CR characters. If the line is too long to buffer, then just
	 * assume it isn't a command and write it back out.
	 *
	 * @return boolean
	 */
	private boolean readLine() {
		try {
			for (;;) {
				String nextLine = in.readLine();

				if (nextLine != null) {
					int length = nextLine.length();

					while (length > 0) {
						char lastCh = nextLine.charAt(length - 1);

						if ((lastCh == '\n') || (lastCh == '\r')) {
							length -= 1;
						} else {
							break;
						}
					}

					line = nextLine.substring(0, length).toCharArray();
					lineCount += 1;
					lineLength = line.length;
					return true;
				} else {
					in.close();
					in = null;

					if (inStack.isEmpty()) {
						return false;
					}

					in = inStack.pop();
				}
			}
		} catch (IOException ex) {
			error("IOException on read", ex);
		}

		return true;
	}

	/**
	 * If we are currently echoing, then write out the current line.
	 */
	private void writeLine() {
		if (echo) {
			try {
				out.write(line, 0, lineLength);
				/* [PR 120411] Use a javadoc tag instead of TestBootpath preprocessor tag */
				if (preprocessingTestBootpath & !isTestBootpathJavaDocTagWritten & testBootpathJavaDocIsNeeded) {
					newLine();
					out.write("\n/**\n * @TestBootpath\n */");
					newLine();
					isTestBootpathJavaDocTagWritten = true;
				}

				if (metadataOut != null) {
					StringBuilder sb = new StringBuilder();
					sb.append(lineCount);
					sb.append(":");
					sb.append(++outputLineCount);
					metadataOut.write(sb.toString());
					metadataOut.write(newLine);
				}
				if (!continuation) {
					newLine();
				}

				if (lineLength == 0) {
					numberOfBlankLines++;
				} else {
					numberOfBlankLines = 0;
				}
			} catch (IOException ex) {
				error("IOException on write: " + ex.getMessage(), ex);
			}
		}
	}

	/**
	 * Detect if the current line has a command in it. If not, just return
	 * false. If it does have a command, pull out the command and argument
	 * pieces, and then return true.
	 */
	private boolean findCommand() {
		command = "";

		int i = 0;

		// Skip whitespace before the command.
		while (i < lineLength && Character.isWhitespace(line[i])) {
			i += 1;
		}

		// Give up if it doesn't start with the command indicator.
		if (i > (lineLength - 4) || line[i] != '/' || line[i + 1] != '*' || line[i + 2] != '[') {
			return false;
		}

		i += 3;

		// Get the command.
		int cmdStart = i;
		int cmdLen = 0;

		for (; cmdLen <= MAX_COMMAND && i < lineLength; ++cmdLen, ++i) {
			char ch = line[i];

			if (ch == ']' || Character.isWhitespace(ch)) {
				break;
			}
		}

		command = new String(line, cmdStart, cmdLen).toUpperCase();

		// Skip whitespace before the argument.
		while (i < lineLength && Character.isWhitespace(line[i])) {
			i += 1;
		}

		// Find the last ']' on the line to allow arguments to include that
		// character, but this means that ']' cannot be used in comments
		// after the command on the same line.
		int positionOfClose = lineLength;

		for (int pos = i; pos < lineLength; ++pos) {
			if (line[pos] == ']') {
				positionOfClose = pos;
			}
		}

		// Get the argument if there is one.
		argument = new String(line, i, positionOfClose - i);

		return true;
	}

	private static final String SeparatorChars = "()|!&^<=>";

	/**
	 * Answer true iff the char is an operator or bracket
	 * (i.e. in the set { '&', '^', '|', '!', '(', ')', '<', '=', '>' })
	 *
	 * @param c char the char to inspect
	 */
	static boolean isOperatorOrBracket(char c) {
		return SeparatorChars.indexOf(c) != -1;
	}

	/**
	 * Checks if the given string matches one of the current state flags, check
	 * if this flag is a valid flag and add it to the list of incorrect flags if
	 * it isn't.
	 *
	 * @param flag      the flag to test.
	 * @return boolean true if we the string is defined and false otherwise.
	 */
	private boolean checkFlag(String flag) {
		synchronized (foundFlags) {
			if (!foundFlags.contains(flag)) {
				foundFlags.add(flag);
			}
		}
		if (!validFlags.contains(flag)) {
			if (flag == null) {
				flag = "";
			}
			invalidFlags.add(new PreprocessorWarning("The flag " + flag + " is not valid.", lineCount, 0, lineLength));
		}
		return defined(flag);
	}

	/**
	 * Resolve the value of the given identifier.
	 */
	ExpressionResult resolve(String identifier) {
		Integer macro = numericMacros.get(identifier);

		if (macro != null) {
			return new ExpressionResult(macro.intValue());
		} else {
			return new ExpressionResult(checkFlag(identifier));
		}
	}

	/**
	 * Adds a warning to the internal list of warnings.
	 *
	 * @param s             the warning to add
	 */
	private void warning(String s) {
		if (s == null) {
			s = "";
		}
		warnings.add(new PreprocessorWarning(s, lineCount, 0, lineLength));
	}

	/**
	 * Answers true iff the preprocessor has found things in the input it would
	 * like to warn about
	 *
	 * @return boolean if warnings exist
	 */
	public boolean hasWarnings() {
		return warnings.size() > 0;
	}

	/**
	 * Returns an iterator over any warnings found in the inputStream
	 *
	 * @return List a List of warning Strings
	 */
	public List<PreprocessorWarning> getWarnings() {
		return warnings;
	}

	/**
	 * Returns a collection containing the invalid flags.
	 *
	 * @return      the invalid flags collection
	 */
	public Collection<PreprocessorWarning> getInvalidFlags() {
		return invalidFlags;
	}

	/**
	 * Returns the time taken to preprocess
	 *
	 * @return long the time taken to preprocess
	 */
	public long getTime() {
		return timeTaken;
	}

	/**
	 * Returns the number of flags found in the source code
	 *
	 * @return int the number of flags found
	 */
	public int getIncludeCount() {
		return numberOfIncludeIfs;
	}

	/**
	 * Returns the fatal error that occured
	 *
	 * @return PreprocessorWarning the error that occured
	 */
	public PreprocessorWarning getError() {
		return error;
	}

	/**
	 * Adds flags to include when preprocessing.
	 *
	 * @param       flags       the flags to be added
	 */
	public void addFlags(Set<String> flags) {
		if (flags == null) {
			throw new IllegalArgumentException();
		}
		this.flags.addAll(flags);
	}

	/**
	 * Sets the flags to include when preprocessing.
	 *
	 * @param       flags       the flags to be added
	 */
	public void setFlags(String[] flags) {
		if (flags == null) {
			throw new IllegalArgumentException();
		}
		for (String flag : flags) {
			this.flags.add(flag);
		}
	}

	/**
	 * Sets the required include flags.
	 *
	 * @param       requiredIncludeFlags    the required include flags
	 */
	public void setRequiredIncludeFlags(Set<String> requiredIncludeFlags) {
		this.requiredIncludeFlags = requiredIncludeFlags;
	}

	/**
	 * Sets the list to store flags found in this file.
	 *
	 * @param       foundFlags      the found flags to be set
	 */
	public void setFoundFlags(List<String> foundFlags) {
		if (foundFlags == null) {
			throw new IllegalArgumentException();
		}
		this.foundFlags = foundFlags;
	}

	/**
	 * Sets the list to store invalid flags found in this file.
	 *
	 * @param       invalidFlags    the invalid flags to be set
	 */
	public void setInvalidFlags(List<PreprocessorWarning> invalidFlags) {
		if (invalidFlags == null) {
			throw new IllegalArgumentException();
		}
		this.invalidFlags = invalidFlags;
	}

	/**
	 * Sets the Set to store fixed PRs found in this file.
	 *
	 * @param       fixedPRs        the fixed PRs to be stored
	 */
	public void setFixedPRs(Set<String> fixedPRs) {
		if (fixedPRs == null) {
			throw new IllegalArgumentException();
		}
		this.fixedPRs = fixedPRs;
	}

	/**
	 * Sets the Properties to store external messages found in this file.
	 *
	 * @param       externalMessages    the external messages
	 */
	public void setExternalMessages(Map<String, String> externalMessages) {
		this.externalMessages = externalMessages;
	}

	/**
	 * Determines whether messages should be inlined.
	 *
	 * @param       inlineMessages      the inline messages
	 */
	public void setInlineMessages(boolean inlineMessages) {
		replaceMsg_getString = inlineMessages;
		msgCallInliner = (inlineMessages) ? new MsgCallInliner(false) : null;
	}

	/**
	 * Determines whether message keys should be inlined.
	 *
	 * @param       inlineMessageKeys   the inline keys
	 */
	public void setInlineMessageKeys(boolean inlineMessageKeys) {
		replaceMsg_getString = inlineMessageKeys;
		msgCallInliner = (inlineMessageKeys) ? new MsgCallInliner(true) : null;
	}

	/**
	 * Sets the macros names and values for replacement.
	 *
	 * @param       macros              the macros
	 */
	public void setMacros(Map<String, String> macros) {
		this.macros.clear();
		this.macros.putAll(macros);
	}

	/**
	 * Sets the Set to store jxelink rules found in this file.
	 *
	 * @param       jxeRules            the JXELink rules
	 */
	public void setJxeRules(Set<String> jxeRules) {
		this.jxeRules = jxeRules;
	}

	/**
	 * Determines whether public api should be preserved using jxelink rules.
	 *
	 * @param       jxePreserveApi      <code>true</code> if public APIs should be preserved, <code>false</code> otherwise
	 */
	public void setJxePreserveApi(boolean jxePreserveApi) {
		this.jxePreserveApi = jxePreserveApi;
	}

	/**
	 * @param       jitAttributes       the JIT attributes
	 */
	public void setJitAttributes(JitAttributes jitAttributes) {
		this.jitAttributes = jitAttributes;
	}

	/**
	 * Sets the includeIfUnsure.
	 *
	 * @param       includeIfUnsure     the includeIfUnsure to set
	 */
	public void setIncludeIfUnsure(boolean includeIfUnsure) {
		this.includeIfUnsure = includeIfUnsure;
		this.shouldInclude = includeIfUnsure;
	}

	/**
	 * Sets the noWarnIncludeIf.
	 *
	 * @param       noWarnIncludeIf     the noWarnIncludeIf to set
	 */
	public void setNoWarnIncludeIf(boolean noWarnIncludeIf) {
		this.noWarnIncludeIf = noWarnIncludeIf;
	}

	/**
	 * Sets the name of the message class that should be used
	 *
	 * @param       name the name of the message class
	 */
	public void setMessageClassName(String name) {
		this.msgClassSubstituter.setMessageClassName(name);
		this.substituteMsgClass = true;
	}

}
