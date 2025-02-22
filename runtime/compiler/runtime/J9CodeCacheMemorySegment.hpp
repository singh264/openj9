/*******************************************************************************
 * Copyright IBM Corp. and others 2000
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

#ifndef J9_CODECACHEMEMORYSEGMENT_INCL
#define J9_CODECACHEMEMORYSEGMENT_INCL


#ifndef J9_CODECACHEMEMORYSEGMENT_COMPOSED
#define J9_CODECACHEMEMORYSEGMENT_COMPOSED
namespace J9 { class CodeCacheMemorySegment; }
namespace J9 { typedef CodeCacheMemorySegment CodeCacheMemorySegmentConnector; }
#endif

#include "env/jittypes.h"
#include "runtime/OMRCodeCacheMemorySegment.hpp"
#include "env/IO.hpp"
#include "env/VMJ9.h"

struct J9MemorySegment;

namespace J9
{

class OMR_EXTENSIBLE CodeCacheMemorySegment : public OMR::CodeCacheMemorySegmentConnector
   {
public:
   CodeCacheMemorySegment() : OMR::CodeCacheMemorySegmentConnector(), _segment(NULL)
      { }

   CodeCacheMemorySegment(uint8_t *memory, uint8_t *top) : OMR::CodeCacheMemorySegmentConnector(memory, top)
      {
      _segment = NULL;
      }

   CodeCacheMemorySegment(J9MemorySegment *segment);

   void * operator new (size_t s, TR::CodeCacheMemorySegment *m) { return m; }

   J9MemorySegment *j9segment()                          { return _segment; }
   void             setSegment(J9MemorySegment *segment) { _segment = segment; }

   void free(TR::CodeCacheManager *manager);

private:
   J9MemorySegment *_segment;
   };

}

#endif
