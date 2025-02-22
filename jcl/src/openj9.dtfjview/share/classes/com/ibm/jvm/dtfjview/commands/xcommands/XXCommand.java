/*[INCLUDE-IF Sidecar18-SE]*/
/*******************************************************************************
 * Copyright IBM Corp. and others 2004
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
package com.ibm.jvm.dtfjview.commands.xcommands;

import java.io.PrintStream;

import com.ibm.dtfj.image.CorruptDataException;
import com.ibm.dtfj.image.ImageAddressSpace;
import com.ibm.dtfj.image.ImagePointer;
import com.ibm.dtfj.image.MemoryAccessException;
import com.ibm.java.diagnostics.utils.IContext;
import com.ibm.java.diagnostics.utils.plugins.DTFJPlugin;
import com.ibm.jvm.dtfjview.commands.helpers.Utils;

@DTFJPlugin(version="1.*", runtime=false)
public class XXCommand extends XCommand {
	{
		addCommand("x/x", "<hex address>", "displays the hex value of the bytes at the specified address");	
	}
	
	@Override
	public boolean recognises(String command, IContext context) {
		if(super.recognises(command, context)) {
			return command.toLowerCase().endsWith("x");
		}
		return false;
	}
	
	public void doCommand(String[] args)
	{
		Long address;
		String param = args[0];
		address = Utils.longFromStringWithPrefix(param);
		if (null == address)
		{
			out.println("invalid hex address specify; address must be specified as "
					+ "\"0x<hex_address>\"");
			return;
		}

		out.print("\n");
		
		boolean found = false;
		for (int index = 0; index < argUnitNumber; index++)
		{
			long currAddr = address.longValue() + (index * argUnitSize);
			
			out.print("\t");
			out.print(Utils.toHex(currAddr));
			out.print(": ");

			ImageAddressSpace ias = ctx.getAddressSpace();
			ImagePointer ip = ias.getPointer(currAddr);
			
			byte b = 0;
			short s = 0;
			int i = 0;
			long l = 0;
			try {
				switch (argUnitSize)
				{
				case 1:
					b = ip.getByteAt(0);
					break;
				case 2:
					s = ip.getShortAt(0);
					break;
				case 4:
					i = ip.getIntAt(0);
					break;
				case 8:
					l = ip.getLongAt(0);
					break;
				}
				
				found = true;
			} catch (CorruptDataException e) {
				found = false;
			} catch (MemoryAccessException e) {
				found = false;
			}

			if (found)
			{
				switch (argUnitSize)
				{
				case 1:
					out.print(Utils.toFixedWidthHex(b));
					break;
				case 2:
					out.print(Utils.toFixedWidthHex(s));
					break;
				case 4:
					out.print(Utils.toFixedWidthHex(i));
					break;
				case 8:
					out.print(Utils.toFixedWidthHex(l));
					break;
				}
			}
			out.print("\n");
		}
			
		if (!found)
		{
			out.print("<address not found in any address space>");
		}
		out.print("\n");
		
	}

	@Override
	public void printDetailedHelp(PrintStream out) {
		super.printDetailedHelp(out);
		out.println("displays the hex value of the bytes at the specified address\n\n" +
				"parameters: 0x<addr>\n\n" +
				"Displays the hex value of the bytes at the specified address, " +
				"adjusted for the endianness of the architecture this dump file is " +
				"from.\n\n" +
				"Note: This command uses the number of items and unit size passed to " +
				"it by the \"x/\" command.\n"
		);
	}
	
}
