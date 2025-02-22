/*******************************************************************************
 * Copyright IBM Corp. and others 2006
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

package jit.test.vich;

import org.testng.annotations.Test;
import org.testng.log4testng.Logger;
import jit.test.vich.utils.Timer;

public class HashEfficiencyTest {

	private static Logger logger = Logger.getLogger(HashEfficiencyTest.class);
	Timer timer;
	static final int LOOPCOUNT = 10;
	int elementCount;
	String [] elementData;
	String [] elementKeys;
	private int loadFactor;
	private int maxSize;
	int probeCount;

	private static final int DEFAULT_SIZE = 101;


public HashEfficiencyTest() {
	this (DEFAULT_SIZE);
}
 
public HashEfficiencyTest (int capacity) {
	if (capacity <= 0) throw new IllegalArgumentException();
	elementCount = 0;
	elementKeys = new String [capacity];
	elementData = new String [capacity];
	loadFactor = 7500;	// Default load factor of 0.75
	computeMaxSize();
	probeCount = 0;
	timer = new Timer ();
}
public void computeMaxSize() {
	maxSize = (int)((long)elementKeys.length * loadFactor / 10000);
}
public int findIndex(String object, String[] array) {
	Object key;
	int length = array.length;
	int index = (object.hashCode() & 0x7FFFFFFF) % length;
	for (int i = index; i < length; i++) {
		probeCount++;
		if (((key = array[i]) == null) || (key == object))
			return i;
		if (key.equals(object))
			return i;
	}
	for (int i = 0; i < index; i++) {
		probeCount++;
		if (((key = array[i]) == null) || (key == object))
			return i;
		if (key.equals(object))
			return i;
	}
	return -1;
}
 
public synchronized Object get (String key) {
	return elementData [findIndex (key, elementKeys)];
}
public String [] keys() {

		 String [] s = {
  			"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", 
  			"P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z",
  			"AA", "BA", "CA", "DA", "EA", "FA", "GA", "HA", "IA", "JA", "KA", "LA", "MA", "NA", "OA", 
  			"PA", "QA", "RA", "SA", "TA", "UA", "VA", "WA", "XA", "YA", "ZA",
   			"AAB", "BAB", "CAB", "DAB", "EAB", "FAB", "GAB", "HAB", "IAB", "JAB", "KAB", "LAB", "MAB", "NAB", "OAB", 
  			"PAB", "QAB", "RAB", "SAB", "TAB", "UAB", "VAB", "WAB", "XAB", "YAB", "ZAB",
 			"AAAB", "BAAB", "CAAB", "DAAB", "EAAB", "FAAB", "GAAB", "HAAB", "IAAB", "JAAB", "KAAB", "LAAB", "MAAB", "NAAB", "OAAB", 
  			"PAAB", "QAAB", "RAAB", "SAAB", "TAAB", "UAAB", "VAAB", "WAAB", "XAAB", "YAAB", "ZAAB", 
   			"AABCD", "BABCD", "CABCD", "DABCD", "EABCD", "FABCD", "GABCD", "HABCD", "IABCD", "JABCD", "KABCD", "LABCD", "MABCD", "NABCD", "OABCD", 
  			"PABCD", "QABCD", "RABCD", "SABCD", "TABCD", "UABCD", "VABCD", "WABCD", "XABCD", "YABCD", "ZABCD",
  			
  			"AZ", "BZ", "CZ", "DZ", "EZ", "FZ", "GZ", "HZ", "IZ", "JZ", "KZ", "LZ", "MZ", "NZ", "OZ", 
  			"PZ", "QZ", "RZ", "SZ", "TZ", "UZ", "VZ", "WZ", "XZ", "YZ", "ZZ",
  			"AAZ", "BAZ", "CAZ", "DAZ", "EAZ", "FAZ", "GAZ", "HAZ", "IAZ", "JAZ", "KAZ", "LAZ", "MAZ", "NAZ", "OAZ", 
  			"PAZ", "QAZ", "RAZ", "SAZ", "TAZ", "UAZ", "VAZ", "WAZ", "XAZ", "YAZ", "ZAZ",
   			"AABZ", "BABZ", "CABZ", "DABZ", "EABZ", "FABZ", "GABZ", "HABZ", "IABZ", "JABZ", "KABZ", "LABZ", "MABZ", "NABZ", "OABZ", 
  			"PABZ", "QABZ", "RABZ", "SABZ", "TABZ", "UABZ", "VABZ", "WABZ", "XABZ", "YABZ", "ZABZ",
 			"AAABZ", "BAABZ", "CAABZ", "DAABZ", "EAABZ", "FAABZ", "GAABZ", "HAABZ", "IAABZ", "JAABZ", "KAABZ", "LAABZ", "MAABZ", "NAABZ", "OAABZ", 
  			"PAABZ", "QAABZ", "RAABZ", "SAABZ", "TAABZ", "UAABZ", "VAABZ", "WAABZ", "XAABZ", "YAABZ", "ZAABZ", 
   			"AABCDZ", "BABCDZ", "CABCDZ", "DABCDZ", "EABCDZ", "FABCDZ", "GABCDZ", "HABCDZ", "IABCDZ", "JABCDZ", "KABCDZ", "LABCDZ", "MABCDZ", "NABCDZ", "OABCDZ", 
  			"PABCDZ", "QABCDZ", "RABCDZ", "SABCDZ", "TABCDZ", "UABCDZ", "VABCDZ", "WABCDZ", "XABCDZ", "YABCDZ", "ZABCD",
 			"ATER", "BTER", "CTER", "DTER", "ETER", "FTER", "GTER", "HTER", "ITER", "JTER", "KTER", "LTER", "MTER", "NTER", "OTER", 
  			"PTER", "QTER", "RTER", "STER", "TTER", "UTER", "VTER", "WTER", "XTER", "YTER", "ZTER",
  			"AATER", "BATER", "CATER", "DATER", "EATER", "FATER", "GATER", "HATER", "IATER", "JATER", "KATER", "LATER", "MATER", "NATER", "OATER", 
  			"PATER", "QATER", "RATER", "SATER", "TATER", "UATER", "VATER", "WATER", "XATER", "YATER", "ZATER",
   			"AABTER", "BABTER", "CABTER", "DABTER", "EABTER", "FABTER", "GABTER", "HABTER", "IABTER", "JABTER", "KABTER", "LABTER", "MABTER", "NABTER", "OABTER", 
  			"PABTER", "QABTER", "RABTER", "SABTER", "TABTER", "UABTER", "VABTER", "WABTER", "XABTER", "YABTER", "ZABTER",
 			"AAABTER", "BAABTER", "CAABTER", "DAABTER", "EAABTER", "FAABTER", "GAABTER", "HAABTER", "IAABTER", "JAABTER", "KAABTER", "LAABTER", "MAABTER", "NAABTER", "OAABTER", 
  			"PAABTER", "QAABTER", "RAABTER", "SAABTER", "TAABTER", "UAABTER", "VAABTER", "WAABTER", "XAABTER", "YAABTER", "ZAABTER", 
   			"AABCDTER", "BABCDTER", "CABCDTER", "DABCDTER", "EABCDTER", "FABCDTER", "GABCDTER", "HABCDTER", "IABCDTER", "JABCDTER", "KABCDTER", "LABCDTER", "MABCDTER", "NABCDTER", "OABCDTER", 
  			"PABCDTER", "QABCDTER", "RABCDTER", "SABCDTER", "TABCDTER", "UABCDTER", "VABCDTER", "WABCDTER", "XABCDTER", "YABCDTER", "ZABCD",
  			"AZTER", "BZTER", "CZTER", "DZTER", "EZTER", "FZTER", "GZTER", "HZTER", "IZTER", "JZTER", "KZTER", "LZTER", "MZTER", "NZTER", "OZTER", 
  			"PZTER", "QZTER", "RZTER", "SZTER", "TZTER", "UZTER", "VZTER", "WZTER", "XZTER", "YZTER", "ZZTER",
  			"AAZTER", "BAZTER", "CAZTER", "DAZTER", "EAZTER", "FAZTER", "GAZTER", "HAZTER", "IAZTER", "JAZTER", "KAZTER", "LAZTER", "MAZTER", "NAZTER", "OAZTER", 
  			"PAZTER", "QAZTER", "RAZTER", "SAZTER", "TAZTER", "UAZTER", "VAZTER", "WAZTER", "XAZTER", "YAZTER", "ZAZTER",
   			"AABZTER", "BABZTER", "CABZTER", "DABZTER", "EABZTER", "FABZTER", "GABZTER", "HABZTER", "IABZTER", "JABZTER", "KABZTER", "LABZTER", "MABZTER", "NABZTER", "OABZTER", 
  			"PABZTER", "QABZTER", "RABZTER", "SABZTER", "TABZTER", "UABZTER", "VABZTER", "WABZTER", "XABZTER", "YABZTER", "ZABZTER",
 			"AAABZTER", "BAABZTER", "CAABZTER", "DAABZTER", "EAABZTER", "FAABZTER", "GAABZTER", "HAABZTER", "IAABZTER", "JAABZTER", "KAABZTER", "LAABZTER", "MAABZTER", "NAABZTER", "OAABZTER", 
  			"PAABZTER", "QAABZTER", "RAABZTER", "SAABZTER", "TAABZTER", "UAABZTER", "VAABZTER", "WAABZTER", "XAABZTER", "YAABZTER", "ZAABZTER", 
   			"AABCDZTER", "BABCDZTER", "CABCDZTER", "DABCDZTER", "EABCDZTER", "FABCDZTER", "GABCDZTER", "HABCDZTER", "IABCDZTER", "JABCDZTER", "KABCDZTER", "LABCDZTER", "MABCDZTER", "NABCDZTER", "OABCDZTER", 
  			"PABCDZTER", "QABCDZTER", "RABCDZTER", "SABCDZTER", "TABCDZTER", "UABCDZTER", "VABCDZTER", "WABCDZTER", "XABCDZTER", "YABCDZTER", "ZABCDTER",

  			"AZED", "BZED", "CZED", "DZED", "EZED", "FZED", "GZED", "HZED", "IZED", "JZED", "KZED", "LZED", "MZED", "NZED", "OZED", 
  			"PZED", "QZED", "RZED", "SZED", "TZED", "UZED", "VZED", "WZED", "XZED", "YZED", "ZZED",
  			"AAZED", "BAZED", "CAZED", "DAZED", "EAZED", "FAZED", "GAZED", "HAZED", "IAZED", "JAZED", "KAZED", "LAZED", "MAZED", "NAZED", "OAZED", 
  			"PAZED", "QAZED", "RAZED", "SAZED", "TAZED", "UAZED", "VAZED", "WAZED", "XAZED", "YAZED", "ZAZED",
   			"AABZED", "BABZED", "CABZED", "DABZED", "EABZED", "FABZED", "GABZED", "HABZED", "IABZED", "JABZED", "KABZED", "LABZED", "MABZED", "NABZED", "OABZED", 
  			"PABZED", "QABZED", "RABZED", "SABZED", "TABZED", "UABZED", "VABZED", "WABZED", "XABZED", "YABZED", "ZABZED",
 			"AAABZED", "BAABZED", "CAABZED", "DAABZED", "EAABZED", "FAABZED", "GAABZED", "HAABZED", "IAABZED", "JAABZED", "KAABZED", "LAABZED", "MAABZED", "NAABZED", "OAABZED", 
  			"PAABZED", "QAABZED", "RAABZED", "SAABZED", "TAABZED", "UAABZED", "VAABZED", "WAABZED", "XAABZED", "YAABZED", "ZAABZED", 
   			"AABCDZED", "BABCDZED", "CABCDZED", "DABCDZED", "EABCDZED", "FABCDZED", "GABCDZED", "HABCDZED", "IABCDZED", "JABCDZED", "KABCDZED", "LABCDZED", "MABCDZED", "NABCDZED", "OABCDZED", 
  			"PABCDZED", "QABCDZED", "RABCDZED", "SABCDZED", "TABCDZED", "UABCDZED", "VABCDZED", "WABCDZED", "XABCDZED", "YABCDZED", "ZABCD",
  			"AZZED", "BZZED", "CZZED", "DZZED", "EZZED", "FZZED", "GZZED", "HZZED", "IZZED", "JZZED", "KZZED", "LZZED", "MZZED", "NZZED", "OZZED", 
  			"PZZED", "QZZED", "RZZED", "SZZED", "TZZED", "UZZED", "VZZED", "WZZED", "XZZED", "YZZED", "ZZZED",
  			"AAZZED", "BAZZED", "CAZZED", "DAZZED", "EAZZED", "FAZZED", "GAZZED", "HAZZED", "IAZZED", "JAZZED", "KAZZED", "LAZZED", "MAZZED", "NAZZED", "OAZZED", 
  			"PAZZED", "QAZZED", "RAZZED", "SAZZED", "TAZZED", "UAZZED", "VAZZED", "WAZZED", "XAZZED", "YAZZED", "ZAZZED",
   			"AABZZED", "BABZZED", "CABZZED", "DABZZED", "EABZZED", "FABZZED", "GABZZED", "HABZZED", "IABZZED", "JABZZED", "KABZZED", "LABZZED", "MABZZED", "NABZZED", "OABZZED", 
  			"PABZZED", "QABZZED", "RABZZED", "SABZZED", "TABZZED", "UABZZED", "VABZZED", "WABZZED", "XABZZED", "YABZZED", "ZABZZED",
 			"AAABZZED", "BAABZZED", "CAABZZED", "DAABZZED", "EAABZZED", "FAABZZED", "GAABZZED", "HAABZZED", "IAABZZED", "JAABZZED", "KAABZZED", "LAABZZED", "MAABZZED", "NAABZZED", "OAABZZED", 
  			"PAABZZED", "QAABZZED", "RAABZZED", "SAABZZED", "TAABZZED", "UAABZZED", "VAABZZED", "WAABZZED", "XAABZZED", "YAABZZED", "ZAABZZED", 
   			"AABCDZZED", "BABCDZZED", "CABCDZZED", "DABCDZZED", "EABCDZZED", "FABCDZZED", "GABCDZZED", "HABCDZZED", "IABCDZZED", "JABCDZZED", "KABCDZZED", "LABCDZZED", "MABCDZZED", "NABCDZZED", "OABCDZZED", 
  			"PABCDZZED", "QABCDZZED", "RABCDZZED", "SABCDZZED", "TABCDZZED", "UABCDZZED", "VABCDZZED", "WABCDZZED", "XABCDZZED", "YABCDZZED", "ZABCDZED",
 			"ATERZED", "BTERZED", "CTERZED", "DTERZED", "ETERZED", "FTERZED", "GTERZED", "HTERZED", "ITERZED", "JTERZED", "KTERZED", "LTERZED", "MTERZED", "NTERZED", "OTERZED", 
  			"PTERZED", "QTERZED", "RTERZED", "STERZED", "TTERZED", "UTERZED", "VTERZED", "WTERZED", "XTERZED", "YTERZED", "ZTERZED",
  			"AATERZED", "BATERZED", "CATERZED", "DATERZED", "EATERZED", "FATERZED", "GATERZED", "HATERZED", "IATERZED", "JATERZED", "KATERZED", "LATERZED", "MATERZED", "NATERZED", "OATERZED", 
  			"PATERZED", "QATERZED", "RATERZED", "SATERZED", "TATERZED", "UATERZED", "VATERZED", "WATERZED", "XATERZED", "YATERZED", "ZATERZED",
   			"AABTERZED", "BABTERZED", "CABTERZED", "DABTERZED", "EABTERZED", "FABTERZED", "GABTERZED", "HABTERZED", "IABTERZED", "JABTERZED", "KABTERZED", "LABTERZED", "MABTERZED", "NABTERZED", "OABTERZED", 
  			"PABTERZED", "QABTERZED", "RABTERZED", "SABTERZED", "TABTERZED", "UABTERZED", "VABTERZED", "WABTERZED", "XABTERZED", "YABTERZED", "ZABTERZED",
 			"AAABTERZED", "BAABTERZED", "CAABTERZED", "DAABTERZED", "EAABTERZED", "FAABTERZED", "GAABTERZED", "HAABTERZED", "IAABTERZED", "JAABTERZED", "KAABTERZED", "LAABTERZED", "MAABTERZED", "NAABTERZED", "OAABTERZED", 
  			"PAABTERZED", "QAABTERZED", "RAABTERZED", "SAABTERZED", "TAABTERZED", "UAABTERZED", "VAABTERZED", "WAABTERZED", "XAABTERZED", "YAABTERZED", "ZAABTERZED", 
   			"AABCDTERZED", "BABCDTERZED", "CABCDTERZED", "DABCDTERZED", "EABCDTERZED", "FABCDTERZED", "GABCDTERZED", "HABCDTERZED", "IABCDTERZED", "JABCDTERZED", "KABCDTERZED", "LABCDTERZED", "MABCDTERZED", "NABCDTERZED", "OABCDTERZED", 
  			"PABCDTERZED", "QABCDTERZED", "RABCDTERZED", "SABCDTERZED", "TABCDTERZED", "UABCDTERZED", "VABCDTERZED", "WABCDTERZED", "XABCDTERZED", "YABCDTERZED", "ZABCD",
  			"AZTERZED", "BZTERZED", "CZTERZED", "DZTERZED", "EZTERZED", "FZTERZED", "GZTERZED", "HZTERZED", "IZTERZED", "JZTERZED", "KZTERZED", "LZTERZED", "MZTERZED", "NZTERZED", "OZTERZED", 
  			"PZTERZED", "QZTERZED", "RZTERZED", "SZTERZED", "TZTERZED", "UZTERZED", "VZTERZED", "WZTERZED", "XZTERZED", "YZTERZED", "ZZTERZED",
  			"AAZTERZED", "BAZTERZED", "CAZTERZED", "DAZTERZED", "EAZTERZED", "FAZTERZED", "GAZTERZED", "HAZTERZED", "IAZTERZED", "JAZTERZED", "KAZTERZED", "LAZTERZED", "MAZTERZED", "NAZTERZED", "OAZTERZED", 
  			"PAZTERZED", "QAZTERZED", "RAZTERZED", "SAZTERZED", "TAZTERZED", "UAZTERZED", "VAZTERZED", "WAZTERZED", "XAZTERZED", "YAZTERZED", "ZAZTERZED",
   			"AABZTERZED", "BABZTERZED", "CABZTERZED", "DABZTERZED", "EABZTERZED", "FABZTERZED", "GABZTERZED", "HABZTERZED", "IABZTERZED", "JABZTERZED", "KABZTERZED", "LABZTERZED", "MABZTERZED", "NABZTERZED", "OABZTERZED", 
  			"PABZTERZED", "QABZTERZED", "RABZTERZED", "SABZTERZED", "TABZTERZED", "UABZTERZED", "VABZTERZED", "WABZTERZED", "XABZTERZED", "YABZTERZED", "ZABZTERZED",
 			"AAABZTERZED", "BAABZTERZED", "CAABZTERZED", "DAABZTERZED", "EAABZTERZED", "FAABZTERZED", "GAABZTERZED", "HAABZTERZED", "IAABZTERZED", "JAABZTERZED", "KAABZTERZED", "LAABZTERZED", "MAABZTERZED", "NAABZTERZED", "OAABZTERZED", 
  			"PAABZTERZED", "QAABZTERZED", "RAABZTERZED", "SAABZTERZED", "TAABZTERZED", "UAABZTERZED", "VAABZTERZED", "WAABZTERZED", "XAABZTERZED", "YAABZTERZED", "ZAABZTERZED", 
   			"AABCDZTERZED", "BABCDZTERZED", "CABCDZTERZED", "DABCDZTERZED", "EABCDZTERZED", "FABCDZTERZED", "GABCDZTERZED", "HABCDZTERZED", "IABCDZTERZED", "JABCDZTERZED", "KABCDZTERZED", "LABCDZTERZED", "MABCDZTERZED", "NABCDZTERZED", "OABCDZTERZED", 
  			"PABCDZTERZED", "QABCDZTERZED", "RABCDZTERZED", "SABCDZTERZED", "TABCDZTERZED", "UABCDZTERZED", "VABCDZTERZED", "WABCDZTERZED", "XABCDZTERZED", "YABCDZTERZED", "ZABCDTERZED",


  			"The end "
	 
	 };
	return s;
}
 
public synchronized Object put (String key, String value) {
	if (key == null) throw new NullPointerException ();
	if (value == null) throw new NullPointerException ();
	int index = findIndex (key, elementKeys);
	Object object = elementData [index];
	if (object == null) {
		if (++elementCount > maxSize) {
			rehash ();
			index = findIndex (key, elementKeys);
		}
	}
	elementKeys [index] = key;
	elementData [index] = value;
	return object;
}
/**
 * Increases the capacity of this Hashtable. This method is sent when
 * the size of this Hashtable exceeds the load factor.
 *
 * @author		OTI
 * @version		initial
 */
protected void rehash () {
	String key;
	int index, length = elementKeys.length<<1;
	String [] newKeys = new String [length];
	String [] newData = new String [length];
	for (int i=0; i<elementKeys.length; i++) {
		if ((key = elementKeys [i]) != null) {
			index = findIndex (key, newKeys);
			newKeys [index] = key;
			newData [index] = elementData [i];
		}
	}
	elementKeys = newKeys;
	elementData = newData;
	computeMaxSize();
}
@Test(groups = { "level.sanity","component.jit" })
public void testHashEfficiency() {
	probeCount = 0; 
	String keys[] = keys(); 
	for (int i = 0; i < keys.length; i++) {
		put(keys[i], keys[i]);
	}
	timer.reset();
	for (int i = 0; i < keys.length; i++) {
		String s = keys[i];
		for (int loop = 0; loop < LOOPCOUNT; loop++) {
			put(s, s);
		}
	}
	timer.mark();
	logger.info("HashEfficiencyTest - Re-Insert " + keys.length + " elements into  " + LOOPCOUNT + " times: = "+ Long.toString(timer.delta()));
	logger.info("HashEfficiencyTest - Re-Insert   probes: = "+ Long.toString (probeCount));

	probeCount = 0; 
	timer.reset();
	for (int i = 0; i < keys.length; i++) {
		String s = keys[i];
		for (int loop = 0; loop < LOOPCOUNT; loop++) {
			if (get(s) != s) {
				logger.error("This will not happen");
			}
		}
	}
	timer.mark();
	logger.info("HashEfficiencyTest - Lookup " + keys.length + " elements in Hash Table " + LOOPCOUNT + " times: = "+ Long.toString(timer.delta()));
	logger.info("HashEfficiencyTest - Lookup  probes: = "+ Long.toString (probeCount));
}
}
