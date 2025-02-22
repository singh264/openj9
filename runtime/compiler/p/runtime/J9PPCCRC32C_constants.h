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

#define CRC32C 0x1edc6f41
#define CRC_XOR
#define REFLECT

#ifndef __ASSEMBLY__
#ifdef CRC_TABLE
static const unsigned int crc32c_table[] = {
	0x00000000, 0xf26b8303, 0xe13b70f7, 0x1350f3f4,
	0xc79a971f, 0x35f1141c, 0x26a1e7e8, 0xd4ca64eb,
	0x8ad958cf, 0x78b2dbcc, 0x6be22838, 0x9989ab3b,
	0x4d43cfd0, 0xbf284cd3, 0xac78bf27, 0x5e133c24,
	0x105ec76f, 0xe235446c, 0xf165b798, 0x030e349b,
	0xd7c45070, 0x25afd373, 0x36ff2087, 0xc494a384,
	0x9a879fa0, 0x68ec1ca3, 0x7bbcef57, 0x89d76c54,
	0x5d1d08bf, 0xaf768bbc, 0xbc267848, 0x4e4dfb4b,
	0x20bd8ede, 0xd2d60ddd, 0xc186fe29, 0x33ed7d2a,
	0xe72719c1, 0x154c9ac2, 0x061c6936, 0xf477ea35,
	0xaa64d611, 0x580f5512, 0x4b5fa6e6, 0xb93425e5,
	0x6dfe410e, 0x9f95c20d, 0x8cc531f9, 0x7eaeb2fa,
	0x30e349b1, 0xc288cab2, 0xd1d83946, 0x23b3ba45,
	0xf779deae, 0x05125dad, 0x1642ae59, 0xe4292d5a,
	0xba3a117e, 0x4851927d, 0x5b016189, 0xa96ae28a,
	0x7da08661, 0x8fcb0562, 0x9c9bf696, 0x6ef07595,
	0x417b1dbc, 0xb3109ebf, 0xa0406d4b, 0x522bee48,
	0x86e18aa3, 0x748a09a0, 0x67dafa54, 0x95b17957,
	0xcba24573, 0x39c9c670, 0x2a993584, 0xd8f2b687,
	0x0c38d26c, 0xfe53516f, 0xed03a29b, 0x1f682198,
	0x5125dad3, 0xa34e59d0, 0xb01eaa24, 0x42752927,
	0x96bf4dcc, 0x64d4cecf, 0x77843d3b, 0x85efbe38,
	0xdbfc821c, 0x2997011f, 0x3ac7f2eb, 0xc8ac71e8,
	0x1c661503, 0xee0d9600, 0xfd5d65f4, 0x0f36e6f7,
	0x61c69362, 0x93ad1061, 0x80fde395, 0x72966096,
	0xa65c047d, 0x5437877e, 0x4767748a, 0xb50cf789,
	0xeb1fcbad, 0x197448ae, 0x0a24bb5a, 0xf84f3859,
	0x2c855cb2, 0xdeeedfb1, 0xcdbe2c45, 0x3fd5af46,
	0x7198540d, 0x83f3d70e, 0x90a324fa, 0x62c8a7f9,
	0xb602c312, 0x44694011, 0x5739b3e5, 0xa55230e6,
	0xfb410cc2, 0x092a8fc1, 0x1a7a7c35, 0xe811ff36,
	0x3cdb9bdd, 0xceb018de, 0xdde0eb2a, 0x2f8b6829,
	0x82f63b78, 0x709db87b, 0x63cd4b8f, 0x91a6c88c,
	0x456cac67, 0xb7072f64, 0xa457dc90, 0x563c5f93,
	0x082f63b7, 0xfa44e0b4, 0xe9141340, 0x1b7f9043,
	0xcfb5f4a8, 0x3dde77ab, 0x2e8e845f, 0xdce5075c,
	0x92a8fc17, 0x60c37f14, 0x73938ce0, 0x81f80fe3,
	0x55326b08, 0xa759e80b, 0xb4091bff, 0x466298fc,
	0x1871a4d8, 0xea1a27db, 0xf94ad42f, 0x0b21572c,
	0xdfeb33c7, 0x2d80b0c4, 0x3ed04330, 0xccbbc033,
	0xa24bb5a6, 0x502036a5, 0x4370c551, 0xb11b4652,
	0x65d122b9, 0x97baa1ba, 0x84ea524e, 0x7681d14d,
	0x2892ed69, 0xdaf96e6a, 0xc9a99d9e, 0x3bc21e9d,
	0xef087a76, 0x1d63f975, 0x0e330a81, 0xfc588982,
	0xb21572c9, 0x407ef1ca, 0x532e023e, 0xa145813d,
	0x758fe5d6, 0x87e466d5, 0x94b49521, 0x66df1622,
	0x38cc2a06, 0xcaa7a905, 0xd9f75af1, 0x2b9cd9f2,
	0xff56bd19, 0x0d3d3e1a, 0x1e6dcdee, 0xec064eed,
	0xc38d26c4, 0x31e6a5c7, 0x22b65633, 0xd0ddd530,
	0x0417b1db, 0xf67c32d8, 0xe52cc12c, 0x1747422f,
	0x49547e0b, 0xbb3ffd08, 0xa86f0efc, 0x5a048dff,
	0x8ecee914, 0x7ca56a17, 0x6ff599e3, 0x9d9e1ae0,
	0xd3d3e1ab, 0x21b862a8, 0x32e8915c, 0xc083125f,
	0x144976b4, 0xe622f5b7, 0xf5720643, 0x07198540,
	0x590ab964, 0xab613a67, 0xb831c993, 0x4a5a4a90,
	0x9e902e7b, 0x6cfbad78, 0x7fab5e8c, 0x8dc0dd8f,
	0xe330a81a, 0x115b2b19, 0x020bd8ed, 0xf0605bee,
	0x24aa3f05, 0xd6c1bc06, 0xc5914ff2, 0x37faccf1,
	0x69e9f0d5, 0x9b8273d6, 0x88d28022, 0x7ab90321,
	0xae7367ca, 0x5c18e4c9, 0x4f48173d, 0xbd23943e,
	0xf36e6f75, 0x0105ec76, 0x12551f82, 0xe03e9c81,
	0x34f4f86a, 0xc69f7b69, 0xd5cf889d, 0x27a40b9e,
	0x79b737ba, 0x8bdcb4b9, 0x988c474d, 0x6ae7c44e,
	0xbe2da0a5, 0x4c4623a6, 0x5f16d052, 0xad7d5351,};

#endif /* CRC_TABLE */
#else
#define MAX_SIZE    32768
#if defined(AIXPPC)

/* Constants */
.constants_crc32c:


/* Reduce 262144 kbits to 1024 bits */
        /* x^261120 mod p(x)` << 1, x^261184 mod p(x)` << 1 */
        .llong 0x00000000b6ca9e20
        .llong 0x000000009c37c408

        /* x^260096 mod p(x)` << 1, x^260160 mod p(x)` << 1 */
        .llong 0x00000000350249a8
        .llong 0x00000001b51df26c

        /* x^259072 mod p(x)` << 1, x^259136 mod p(x)` << 1 */
        .llong 0x00000001862dac54
        .llong 0x000000000724b9d0

        /* x^258048 mod p(x)` << 1, x^258112 mod p(x)` << 1 */
        .llong 0x00000001d87fb48c
        .llong 0x00000001c00532fe

        /* x^257024 mod p(x)` << 1, x^257088 mod p(x)` << 1 */
        .llong 0x00000001f39b699e
        .llong 0x00000000f05a9362

        /* x^256000 mod p(x)` << 1, x^256064 mod p(x)` << 1 */
        .llong 0x0000000101da11b4
        .llong 0x00000001e1007970

        /* x^254976 mod p(x)` << 1, x^255040 mod p(x)` << 1 */
        .llong 0x00000001cab571e0
        .llong 0x00000000a57366ee

        /* x^253952 mod p(x)` << 1, x^254016 mod p(x)` << 1 */
        .llong 0x00000000c7020cfe
        .llong 0x0000000192011284

        /* x^252928 mod p(x)` << 1, x^252992 mod p(x)` << 1 */
        .llong 0x00000000cdaed1ae
        .llong 0x0000000162716d9a

        /* x^251904 mod p(x)` << 1, x^251968 mod p(x)` << 1 */
        .llong 0x00000001e804effc
        .llong 0x00000000cd97ecde

        /* x^250880 mod p(x)` << 1, x^250944 mod p(x)` << 1 */
        .llong 0x0000000077c3ea3a
        .llong 0x0000000058812bc0

        /* x^249856 mod p(x)` << 1, x^249920 mod p(x)` << 1 */
        .llong 0x0000000068df31b4
        .llong 0x0000000088b8c12e

        /* x^248832 mod p(x)` << 1, x^248896 mod p(x)` << 1 */
        .llong 0x00000000b059b6c2
        .llong 0x00000001230b234c

        /* x^247808 mod p(x)` << 1, x^247872 mod p(x)` << 1 */
        .llong 0x0000000145fb8ed8
        .llong 0x00000001120b416e

        /* x^246784 mod p(x)` << 1, x^246848 mod p(x)` << 1 */
        .llong 0x00000000cbc09168
        .llong 0x00000001974aecb0

        /* x^245760 mod p(x)` << 1, x^245824 mod p(x)` << 1 */
        .llong 0x000000005ceeedc2
        .llong 0x000000008ee3f226

        /* x^244736 mod p(x)` << 1, x^244800 mod p(x)` << 1 */
        .llong 0x0000000047d74e86
        .llong 0x00000001089aba9a

        /* x^243712 mod p(x)` << 1, x^243776 mod p(x)` << 1 */
        .llong 0x00000001407e9e22
        .llong 0x0000000065113872

        /* x^242688 mod p(x)` << 1, x^242752 mod p(x)` << 1 */
        .llong 0x00000001da967bda
        .llong 0x000000005c07ec10

        /* x^241664 mod p(x)` << 1, x^241728 mod p(x)` << 1 */
        .llong 0x000000006c898368
        .llong 0x0000000187590924

        /* x^240640 mod p(x)` << 1, x^240704 mod p(x)` << 1 */
        .llong 0x00000000f2d14c98
        .llong 0x00000000e35da7c6

        /* x^239616 mod p(x)` << 1, x^239680 mod p(x)` << 1 */
        .llong 0x00000001993c6ad4
        .llong 0x000000000415855a

        /* x^238592 mod p(x)` << 1, x^238656 mod p(x)` << 1 */
        .llong 0x000000014683d1ac
        .llong 0x0000000073617758

        /* x^237568 mod p(x)` << 1, x^237632 mod p(x)` << 1 */
        .llong 0x00000001a7c93e6c
        .llong 0x0000000176021d28

        /* x^236544 mod p(x)` << 1, x^236608 mod p(x)` << 1 */
        .llong 0x000000010211e90a
        .llong 0x00000001c358fd0a

        /* x^235520 mod p(x)` << 1, x^235584 mod p(x)` << 1 */
        .llong 0x000000001119403e
        .llong 0x00000001ff7a2c18

        /* x^234496 mod p(x)` << 1, x^234560 mod p(x)` << 1 */
        .llong 0x000000001c3261aa
        .llong 0x00000000f2d9f7e4

        /* x^233472 mod p(x)` << 1, x^233536 mod p(x)` << 1 */
        .llong 0x000000014e37a634
        .llong 0x000000016cf1f9c8

        /* x^232448 mod p(x)` << 1, x^232512 mod p(x)` << 1 */
        .llong 0x0000000073786c0c
        .llong 0x000000010af9279a

        /* x^231424 mod p(x)` << 1, x^231488 mod p(x)` << 1 */
        .llong 0x000000011dc037f8
        .llong 0x0000000004f101e8

        /* x^230400 mod p(x)` << 1, x^230464 mod p(x)` << 1 */
        .llong 0x0000000031433dfc
        .llong 0x0000000070bcf184

        /* x^229376 mod p(x)` << 1, x^229440 mod p(x)` << 1 */
        .llong 0x000000009cde8348
        .llong 0x000000000a8de642

        /* x^228352 mod p(x)` << 1, x^228416 mod p(x)` << 1 */
        .llong 0x0000000038d3c2a6
        .llong 0x0000000062ea130c

        /* x^227328 mod p(x)` << 1, x^227392 mod p(x)` << 1 */
        .llong 0x000000011b25f260
        .llong 0x00000001eb31cbb2

        /* x^226304 mod p(x)` << 1, x^226368 mod p(x)` << 1 */
        .llong 0x000000001629e6f0
        .llong 0x0000000170783448

        /* x^225280 mod p(x)` << 1, x^225344 mod p(x)` << 1 */
        .llong 0x0000000160838b4c
        .llong 0x00000001a684b4c6

        /* x^224256 mod p(x)` << 1, x^224320 mod p(x)` << 1 */
        .llong 0x000000007a44011c
        .llong 0x00000000253ca5b4

        /* x^223232 mod p(x)` << 1, x^223296 mod p(x)` << 1 */
        .llong 0x00000000226f417a
        .llong 0x0000000057b4b1e2

        /* x^222208 mod p(x)` << 1, x^222272 mod p(x)` << 1 */
        .llong 0x0000000045eb2eb4
        .llong 0x00000000b6bd084c

        /* x^221184 mod p(x)` << 1, x^221248 mod p(x)` << 1 */
        .llong 0x000000014459d70c
        .llong 0x0000000123c2d592

        /* x^220160 mod p(x)` << 1, x^220224 mod p(x)` << 1 */
        .llong 0x00000001d406ed82
        .llong 0x00000000159dafce

        /* x^219136 mod p(x)` << 1, x^219200 mod p(x)` << 1 */
        .llong 0x0000000160c8e1a8
        .llong 0x0000000127e1a64e

        /* x^218112 mod p(x)` << 1, x^218176 mod p(x)` << 1 */
        .llong 0x0000000027ba8098
        .llong 0x0000000056860754

        /* x^217088 mod p(x)` << 1, x^217152 mod p(x)` << 1 */
        .llong 0x000000006d92d018
        .llong 0x00000001e661aae8

        /* x^216064 mod p(x)` << 1, x^216128 mod p(x)` << 1 */
        .llong 0x000000012ed7e3f2
        .llong 0x00000000f82c6166

        /* x^215040 mod p(x)` << 1, x^215104 mod p(x)` << 1 */
        .llong 0x000000002dc87788
        .llong 0x00000000c4f9c7ae

        /* x^214016 mod p(x)` << 1, x^214080 mod p(x)` << 1 */
        .llong 0x0000000018240bb8
        .llong 0x0000000074203d20

        /* x^212992 mod p(x)` << 1, x^213056 mod p(x)` << 1 */
        .llong 0x000000001ad38158
        .llong 0x0000000198173052

        /* x^211968 mod p(x)` << 1, x^212032 mod p(x)` << 1 */
        .llong 0x00000001396b78f2
        .llong 0x00000001ce8aba54

        /* x^210944 mod p(x)` << 1, x^211008 mod p(x)` << 1 */
        .llong 0x000000011a681334
        .llong 0x00000001850d5d94

        /* x^209920 mod p(x)` << 1, x^209984 mod p(x)` << 1 */
        .llong 0x000000012104732e
        .llong 0x00000001d609239c

        /* x^208896 mod p(x)` << 1, x^208960 mod p(x)` << 1 */
        .llong 0x00000000a140d90c
        .llong 0x000000001595f048

        /* x^207872 mod p(x)` << 1, x^207936 mod p(x)` << 1 */
        .llong 0x00000001b7215eda
        .llong 0x0000000042ccee08

        /* x^206848 mod p(x)` << 1, x^206912 mod p(x)` << 1 */
        .llong 0x00000001aaf1df3c
        .llong 0x000000010a389d74

        /* x^205824 mod p(x)` << 1, x^205888 mod p(x)` << 1 */
        .llong 0x0000000029d15b8a
        .llong 0x000000012a840da6

        /* x^204800 mod p(x)` << 1, x^204864 mod p(x)` << 1 */
        .llong 0x00000000f1a96922
        .llong 0x000000001d181c0c

        /* x^203776 mod p(x)` << 1, x^203840 mod p(x)` << 1 */
        .llong 0x00000001ac80d03c
        .llong 0x0000000068b7d1f6

        /* x^202752 mod p(x)` << 1, x^202816 mod p(x)` << 1 */
        .llong 0x000000000f11d56a
        .llong 0x000000005b0f14fc

        /* x^201728 mod p(x)` << 1, x^201792 mod p(x)` << 1 */
        .llong 0x00000001f1c022a2
        .llong 0x0000000179e9e730

        /* x^200704 mod p(x)` << 1, x^200768 mod p(x)` << 1 */
        .llong 0x0000000173d00ae2
        .llong 0x00000001ce1368d6

        /* x^199680 mod p(x)` << 1, x^199744 mod p(x)` << 1 */
        .llong 0x00000001d4ffe4ac
        .llong 0x0000000112c3a84c

        /* x^198656 mod p(x)` << 1, x^198720 mod p(x)` << 1 */
        .llong 0x000000016edc5ae4
        .llong 0x00000000de940fee

        /* x^197632 mod p(x)` << 1, x^197696 mod p(x)` << 1 */
        .llong 0x00000001f1a02140
        .llong 0x00000000fe896b7e

        /* x^196608 mod p(x)` << 1, x^196672 mod p(x)` << 1 */
        .llong 0x00000000ca0b28a0
        .llong 0x00000001f797431c

        /* x^195584 mod p(x)` << 1, x^195648 mod p(x)` << 1 */
        .llong 0x00000001928e30a2
        .llong 0x0000000053e989ba

        /* x^194560 mod p(x)` << 1, x^194624 mod p(x)` << 1 */
        .llong 0x0000000097b1b002
        .llong 0x000000003920cd16

        /* x^193536 mod p(x)` << 1, x^193600 mod p(x)` << 1 */
        .llong 0x00000000b15bf906
        .llong 0x00000001e6f579b8

        /* x^192512 mod p(x)` << 1, x^192576 mod p(x)` << 1 */
        .llong 0x00000000411c5d52
        .llong 0x000000007493cb0a

        /* x^191488 mod p(x)` << 1, x^191552 mod p(x)` << 1 */
        .llong 0x00000001c36f3300
        .llong 0x00000001bdd376d8

        /* x^190464 mod p(x)` << 1, x^190528 mod p(x)` << 1 */
        .llong 0x00000001119227e0
        .llong 0x000000016badfee6

        /* x^189440 mod p(x)` << 1, x^189504 mod p(x)` << 1 */
        .llong 0x00000000114d4702
        .llong 0x0000000071de5c58

        /* x^188416 mod p(x)` << 1, x^188480 mod p(x)` << 1 */
        .llong 0x00000000458b5b98
        .llong 0x00000000453f317c

        /* x^187392 mod p(x)` << 1, x^187456 mod p(x)` << 1 */
        .llong 0x000000012e31fb8e
        .llong 0x0000000121675cce

        /* x^186368 mod p(x)` << 1, x^186432 mod p(x)` << 1 */
        .llong 0x000000005cf619d8
        .llong 0x00000001f409ee92

        /* x^185344 mod p(x)` << 1, x^185408 mod p(x)` << 1 */
        .llong 0x0000000063f4d8b2
        .llong 0x00000000f36b9c88

        /* x^184320 mod p(x)` << 1, x^184384 mod p(x)` << 1 */
        .llong 0x000000004138dc8a
        .llong 0x0000000036b398f4

        /* x^183296 mod p(x)` << 1, x^183360 mod p(x)` << 1 */
        .llong 0x00000001d29ee8e0
        .llong 0x00000001748f9adc

        /* x^182272 mod p(x)` << 1, x^182336 mod p(x)` << 1 */
        .llong 0x000000006a08ace8
        .llong 0x00000001be94ec00

        /* x^181248 mod p(x)` << 1, x^181312 mod p(x)` << 1 */
        .llong 0x0000000127d42010
        .llong 0x00000000b74370d6

        /* x^180224 mod p(x)` << 1, x^180288 mod p(x)` << 1 */
        .llong 0x0000000019d76b62
        .llong 0x00000001174d0b98

        /* x^179200 mod p(x)` << 1, x^179264 mod p(x)` << 1 */
        .llong 0x00000001b1471f6e
        .llong 0x00000000befc06a4

        /* x^178176 mod p(x)` << 1, x^178240 mod p(x)` << 1 */
        .llong 0x00000001f64c19cc
        .llong 0x00000001ae125288

        /* x^177152 mod p(x)` << 1, x^177216 mod p(x)` << 1 */
        .llong 0x00000000003c0ea0
        .llong 0x0000000095c19b34

        /* x^176128 mod p(x)` << 1, x^176192 mod p(x)` << 1 */
        .llong 0x000000014d73abf6
        .llong 0x00000001a78496f2

        /* x^175104 mod p(x)` << 1, x^175168 mod p(x)` << 1 */
        .llong 0x00000001620eb844
        .llong 0x00000001ac5390a0

        /* x^174080 mod p(x)` << 1, x^174144 mod p(x)` << 1 */
        .llong 0x0000000147655048
        .llong 0x000000002a80ed6e

        /* x^173056 mod p(x)` << 1, x^173120 mod p(x)` << 1 */
        .llong 0x0000000067b5077e
        .llong 0x00000001fa9b0128

        /* x^172032 mod p(x)` << 1, x^172096 mod p(x)` << 1 */
        .llong 0x0000000010ffe206
        .llong 0x00000001ea94929e

        /* x^171008 mod p(x)` << 1, x^171072 mod p(x)` << 1 */
        .llong 0x000000000fee8f1e
        .llong 0x0000000125f4305c

        /* x^169984 mod p(x)` << 1, x^170048 mod p(x)` << 1 */
        .llong 0x00000001da26fbae
        .llong 0x00000001471e2002

        /* x^168960 mod p(x)` << 1, x^169024 mod p(x)` << 1 */
        .llong 0x00000001b3a8bd88
        .llong 0x0000000132d2253a

        /* x^167936 mod p(x)` << 1, x^168000 mod p(x)` << 1 */
        .llong 0x00000000e8f3898e
        .llong 0x00000000f26b3592

        /* x^166912 mod p(x)` << 1, x^166976 mod p(x)` << 1 */
        .llong 0x00000000b0d0d28c
        .llong 0x00000000bc8b67b0

        /* x^165888 mod p(x)` << 1, x^165952 mod p(x)` << 1 */
        .llong 0x0000000030f2a798
        .llong 0x000000013a826ef2

        /* x^164864 mod p(x)` << 1, x^164928 mod p(x)` << 1 */
        .llong 0x000000000fba1002
        .llong 0x0000000081482c84

        /* x^163840 mod p(x)` << 1, x^163904 mod p(x)` << 1 */
        .llong 0x00000000bdb9bd72
        .llong 0x00000000e77307c2

        /* x^162816 mod p(x)` << 1, x^162880 mod p(x)` << 1 */
        .llong 0x0000000075d3bf5a
        .llong 0x00000000d4a07ec8

        /* x^161792 mod p(x)` << 1, x^161856 mod p(x)` << 1 */
        .llong 0x00000000ef1f98a0
        .llong 0x0000000017102100

        /* x^160768 mod p(x)` << 1, x^160832 mod p(x)` << 1 */
        .llong 0x00000000689c7602
        .llong 0x00000000db406486

        /* x^159744 mod p(x)` << 1, x^159808 mod p(x)` << 1 */
        .llong 0x000000016d5fa5fe
        .llong 0x0000000192db7f88

        /* x^158720 mod p(x)` << 1, x^158784 mod p(x)` << 1 */
        .llong 0x00000001d0d2b9ca
        .llong 0x000000018bf67b1e

        /* x^157696 mod p(x)` << 1, x^157760 mod p(x)` << 1 */
        .llong 0x0000000041e7b470
        .llong 0x000000007c09163e

        /* x^156672 mod p(x)` << 1, x^156736 mod p(x)` << 1 */
        .llong 0x00000001cbb6495e
        .llong 0x000000000adac060

        /* x^155648 mod p(x)` << 1, x^155712 mod p(x)` << 1 */
        .llong 0x000000010052a0b0
        .llong 0x00000000bd8316ae

        /* x^154624 mod p(x)` << 1, x^154688 mod p(x)` << 1 */
        .llong 0x00000001d8effb5c
        .llong 0x000000019f09ab54

        /* x^153600 mod p(x)` << 1, x^153664 mod p(x)` << 1 */
        .llong 0x00000001d969853c
        .llong 0x0000000125155542

        /* x^152576 mod p(x)` << 1, x^152640 mod p(x)` << 1 */
        .llong 0x00000000523ccce2
        .llong 0x000000018fdb5882

        /* x^151552 mod p(x)` << 1, x^151616 mod p(x)` << 1 */
        .llong 0x000000001e2436bc
        .llong 0x00000000e794b3f4

        /* x^150528 mod p(x)` << 1, x^150592 mod p(x)` << 1 */
        .llong 0x00000000ddd1c3a2
        .llong 0x000000016f9bb022

        /* x^149504 mod p(x)` << 1, x^149568 mod p(x)` << 1 */
        .llong 0x0000000019fcfe38
        .llong 0x00000000290c9978

        /* x^148480 mod p(x)` << 1, x^148544 mod p(x)` << 1 */
        .llong 0x00000001ce95db64
        .llong 0x0000000083c0f350

        /* x^147456 mod p(x)` << 1, x^147520 mod p(x)` << 1 */
        .llong 0x00000000af582806
        .llong 0x0000000173ea6628

        /* x^146432 mod p(x)` << 1, x^146496 mod p(x)` << 1 */
        .llong 0x00000001006388f6
        .llong 0x00000001c8b4e00a

        /* x^145408 mod p(x)` << 1, x^145472 mod p(x)` << 1 */
        .llong 0x0000000179eca00a
        .llong 0x00000000de95d6aa

        /* x^144384 mod p(x)` << 1, x^144448 mod p(x)` << 1 */
        .llong 0x0000000122410a6a
        .llong 0x000000010b7f7248

        /* x^143360 mod p(x)` << 1, x^143424 mod p(x)` << 1 */
        .llong 0x000000004288e87c
        .llong 0x00000001326e3a06

        /* x^142336 mod p(x)` << 1, x^142400 mod p(x)` << 1 */
        .llong 0x000000016c5490da
        .llong 0x00000000bb62c2e6

        /* x^141312 mod p(x)` << 1, x^141376 mod p(x)` << 1 */
        .llong 0x00000000d1c71f6e
        .llong 0x0000000156a4b2c2

        /* x^140288 mod p(x)` << 1, x^140352 mod p(x)` << 1 */
        .llong 0x00000001b4ce08a6
        .llong 0x000000011dfe763a

        /* x^139264 mod p(x)` << 1, x^139328 mod p(x)` << 1 */
        .llong 0x00000001466ba60c
        .llong 0x000000007bcca8e2

        /* x^138240 mod p(x)` << 1, x^138304 mod p(x)` << 1 */
        .llong 0x00000001f6c488a4
        .llong 0x0000000186118faa

        /* x^137216 mod p(x)` << 1, x^137280 mod p(x)` << 1 */
        .llong 0x000000013bfb0682
        .llong 0x0000000111a65a88

        /* x^136192 mod p(x)` << 1, x^136256 mod p(x)` << 1 */
        .llong 0x00000000690e9e54
        .llong 0x000000003565e1c4

        /* x^135168 mod p(x)` << 1, x^135232 mod p(x)` << 1 */
        .llong 0x00000000281346b6
        .llong 0x000000012ed02a82

        /* x^134144 mod p(x)` << 1, x^134208 mod p(x)` << 1 */
        .llong 0x0000000156464024
        .llong 0x00000000c486ecfc

        /* x^133120 mod p(x)` << 1, x^133184 mod p(x)` << 1 */
        .llong 0x000000016063a8dc
        .llong 0x0000000001b951b2

        /* x^132096 mod p(x)` << 1, x^132160 mod p(x)` << 1 */
        .llong 0x0000000116a66362
        .llong 0x0000000048143916

        /* x^131072 mod p(x)` << 1, x^131136 mod p(x)` << 1 */
        .llong 0x000000017e8aa4d2
        .llong 0x00000001dc2ae124

        /* x^130048 mod p(x)` << 1, x^130112 mod p(x)` << 1 */
        .llong 0x00000001728eb10c
        .llong 0x00000001416c58d6

        /* x^129024 mod p(x)` << 1, x^129088 mod p(x)` << 1 */
        .llong 0x00000001b08fd7fa
        .llong 0x00000000a479744a

        /* x^128000 mod p(x)` << 1, x^128064 mod p(x)` << 1 */
        .llong 0x00000001092a16e8
        .llong 0x0000000096ca3a26

        /* x^126976 mod p(x)` << 1, x^127040 mod p(x)` << 1 */
        .llong 0x00000000a505637c
        .llong 0x00000000ff223d4e

        /* x^125952 mod p(x)` << 1, x^126016 mod p(x)` << 1 */
        .llong 0x00000000d94869b2
        .llong 0x000000010e84da42

        /* x^124928 mod p(x)` << 1, x^124992 mod p(x)` << 1 */
        .llong 0x00000001c8b203ae
        .llong 0x00000001b61ba3d0

        /* x^123904 mod p(x)` << 1, x^123968 mod p(x)` << 1 */
        .llong 0x000000005704aea0
        .llong 0x00000000680f2de8

        /* x^122880 mod p(x)` << 1, x^122944 mod p(x)` << 1 */
        .llong 0x000000012e295fa2
        .llong 0x000000008772a9a8

        /* x^121856 mod p(x)` << 1, x^121920 mod p(x)` << 1 */
        .llong 0x000000011d0908bc
        .llong 0x0000000155f295bc

        /* x^120832 mod p(x)` << 1, x^120896 mod p(x)` << 1 */
        .llong 0x0000000193ed97ea
        .llong 0x00000000595f9282

        /* x^119808 mod p(x)` << 1, x^119872 mod p(x)` << 1 */
        .llong 0x000000013a0f1c52
        .llong 0x0000000164b1c25a

        /* x^118784 mod p(x)` << 1, x^118848 mod p(x)` << 1 */
        .llong 0x000000010c2c40c0
        .llong 0x00000000fbd67c50

        /* x^117760 mod p(x)` << 1, x^117824 mod p(x)` << 1 */
        .llong 0x00000000ff6fac3e
        .llong 0x0000000096076268

        /* x^116736 mod p(x)` << 1, x^116800 mod p(x)` << 1 */
        .llong 0x000000017b3609c0
        .llong 0x00000001d288e4cc

        /* x^115712 mod p(x)` << 1, x^115776 mod p(x)` << 1 */
        .llong 0x0000000088c8c922
        .llong 0x00000001eaac1bdc

        /* x^114688 mod p(x)` << 1, x^114752 mod p(x)` << 1 */
        .llong 0x00000001751baae6
        .llong 0x00000001f1ea39e2

        /* x^113664 mod p(x)` << 1, x^113728 mod p(x)` << 1 */
        .llong 0x0000000107952972
        .llong 0x00000001eb6506fc

        /* x^112640 mod p(x)` << 1, x^112704 mod p(x)` << 1 */
        .llong 0x0000000162b00abe
        .llong 0x000000010f806ffe

        /* x^111616 mod p(x)` << 1, x^111680 mod p(x)` << 1 */
        .llong 0x000000000d7b404c
        .llong 0x000000010408481e

        /* x^110592 mod p(x)` << 1, x^110656 mod p(x)` << 1 */
        .llong 0x00000000763b13d4
        .llong 0x0000000188260534

        /* x^109568 mod p(x)` << 1, x^109632 mod p(x)` << 1 */
        .llong 0x00000000f6dc22d8
        .llong 0x0000000058fc73e0

        /* x^108544 mod p(x)` << 1, x^108608 mod p(x)` << 1 */
        .llong 0x000000007daae060
        .llong 0x00000000391c59b8

        /* x^107520 mod p(x)` << 1, x^107584 mod p(x)` << 1 */
        .llong 0x000000013359ab7c
        .llong 0x000000018b638400

        /* x^106496 mod p(x)` << 1, x^106560 mod p(x)` << 1 */
        .llong 0x000000008add438a
        .llong 0x000000011738f5c4

        /* x^105472 mod p(x)` << 1, x^105536 mod p(x)` << 1 */
        .llong 0x00000001edbefdea
        .llong 0x000000008cf7c6da

        /* x^104448 mod p(x)` << 1, x^104512 mod p(x)` << 1 */
        .llong 0x000000004104e0f8
        .llong 0x00000001ef97fb16

        /* x^103424 mod p(x)` << 1, x^103488 mod p(x)` << 1 */
        .llong 0x00000000b48a8222
        .llong 0x0000000102130e20

        /* x^102400 mod p(x)` << 1, x^102464 mod p(x)` << 1 */
        .llong 0x00000001bcb46844
        .llong 0x00000000db968898

        /* x^101376 mod p(x)` << 1, x^101440 mod p(x)` << 1 */
        .llong 0x000000013293ce0a
        .llong 0x00000000b5047b5e

        /* x^100352 mod p(x)` << 1, x^100416 mod p(x)` << 1 */
        .llong 0x00000001710d0844
        .llong 0x000000010b90fdb2

        /* x^99328 mod p(x)` << 1, x^99392 mod p(x)` << 1 */
        .llong 0x0000000117907f6e
        .llong 0x000000004834a32e

        /* x^98304 mod p(x)` << 1, x^98368 mod p(x)` << 1 */
        .llong 0x0000000087ddf93e
        .llong 0x0000000059c8f2b0

        /* x^97280 mod p(x)` << 1, x^97344 mod p(x)` << 1 */
        .llong 0x000000005970e9b0
        .llong 0x0000000122cec508

        /* x^96256 mod p(x)` << 1, x^96320 mod p(x)` << 1 */
        .llong 0x0000000185b2b7d0
        .llong 0x000000000a330cda

        /* x^95232 mod p(x)` << 1, x^95296 mod p(x)` << 1 */
        .llong 0x00000001dcee0efc
        .llong 0x000000014a47148c

        /* x^94208 mod p(x)` << 1, x^94272 mod p(x)` << 1 */
        .llong 0x0000000030da2722
        .llong 0x0000000042c61cb8

        /* x^93184 mod p(x)` << 1, x^93248 mod p(x)` << 1 */
        .llong 0x000000012f925a18
        .llong 0x0000000012fe6960

        /* x^92160 mod p(x)` << 1, x^92224 mod p(x)` << 1 */
        .llong 0x00000000dd2e357c
        .llong 0x00000000dbda2c20

        /* x^91136 mod p(x)` << 1, x^91200 mod p(x)` << 1 */
        .llong 0x00000000071c80de
        .llong 0x000000011122410c

        /* x^90112 mod p(x)` << 1, x^90176 mod p(x)` << 1 */
        .llong 0x000000011513140a
        .llong 0x00000000977b2070

        /* x^89088 mod p(x)` << 1, x^89152 mod p(x)` << 1 */
        .llong 0x00000001df876e8e
        .llong 0x000000014050438e

        /* x^88064 mod p(x)` << 1, x^88128 mod p(x)` << 1 */
        .llong 0x000000015f81d6ce
        .llong 0x0000000147c840e8

        /* x^87040 mod p(x)` << 1, x^87104 mod p(x)` << 1 */
        .llong 0x000000019dd94dbe
        .llong 0x00000001cc7c88ce

        /* x^86016 mod p(x)` << 1, x^86080 mod p(x)` << 1 */
        .llong 0x00000001373d206e
        .llong 0x00000001476b35a4

        /* x^84992 mod p(x)` << 1, x^85056 mod p(x)` << 1 */
        .llong 0x00000000668ccade
        .llong 0x000000013d52d508

        /* x^83968 mod p(x)` << 1, x^84032 mod p(x)` << 1 */
        .llong 0x00000001b192d268
        .llong 0x000000008e4be32e

        /* x^82944 mod p(x)` << 1, x^83008 mod p(x)` << 1 */
        .llong 0x00000000e30f3a78
        .llong 0x00000000024120fe

        /* x^81920 mod p(x)` << 1, x^81984 mod p(x)` << 1 */
        .llong 0x000000010ef1f7bc
        .llong 0x00000000ddecddb4

        /* x^80896 mod p(x)` << 1, x^80960 mod p(x)` << 1 */
        .llong 0x00000001f5ac7380
        .llong 0x00000000d4d403bc

        /* x^79872 mod p(x)` << 1, x^79936 mod p(x)` << 1 */
        .llong 0x000000011822ea70
        .llong 0x00000001734b89aa

        /* x^78848 mod p(x)` << 1, x^78912 mod p(x)` << 1 */
        .llong 0x00000000c3a33848
        .llong 0x000000010e7a58d6

        /* x^77824 mod p(x)` << 1, x^77888 mod p(x)` << 1 */
        .llong 0x00000001bd151c24
        .llong 0x00000001f9f04e9c

        /* x^76800 mod p(x)` << 1, x^76864 mod p(x)` << 1 */
        .llong 0x0000000056002d76
        .llong 0x00000000b692225e

        /* x^75776 mod p(x)` << 1, x^75840 mod p(x)` << 1 */
        .llong 0x000000014657c4f4
        .llong 0x000000019b8d3f3e

        /* x^74752 mod p(x)` << 1, x^74816 mod p(x)` << 1 */
        .llong 0x0000000113742d7c
        .llong 0x00000001a874f11e

        /* x^73728 mod p(x)` << 1, x^73792 mod p(x)` << 1 */
        .llong 0x000000019c5920ba
        .llong 0x000000010d5a4254

        /* x^72704 mod p(x)` << 1, x^72768 mod p(x)` << 1 */
        .llong 0x000000005216d2d6
        .llong 0x00000000bbb2f5d6

        /* x^71680 mod p(x)` << 1, x^71744 mod p(x)` << 1 */
        .llong 0x0000000136f5ad8a
        .llong 0x0000000179cc0e36

        /* x^70656 mod p(x)` << 1, x^70720 mod p(x)` << 1 */
        .llong 0x000000018b07beb6
        .llong 0x00000001dca1da4a

        /* x^69632 mod p(x)` << 1, x^69696 mod p(x)` << 1 */
        .llong 0x00000000db1e93b0
        .llong 0x00000000feb1a192

        /* x^68608 mod p(x)` << 1, x^68672 mod p(x)` << 1 */
        .llong 0x000000000b96fa3a
        .llong 0x00000000d1eeedd6

        /* x^67584 mod p(x)` << 1, x^67648 mod p(x)` << 1 */
        .llong 0x00000001d9968af0
        .llong 0x000000008fad9bb4

        /* x^66560 mod p(x)` << 1, x^66624 mod p(x)` << 1 */
        .llong 0x000000000e4a77a2
        .llong 0x00000001884938e4

        /* x^65536 mod p(x)` << 1, x^65600 mod p(x)` << 1 */
        .llong 0x00000000508c2ac8
        .llong 0x00000001bc2e9bc0

        /* x^64512 mod p(x)` << 1, x^64576 mod p(x)` << 1 */
        .llong 0x0000000021572a80
        .llong 0x00000001f9658a68

        /* x^63488 mod p(x)` << 1, x^63552 mod p(x)` << 1 */
        .llong 0x00000001b859daf2
        .llong 0x000000001b9224fc

        /* x^62464 mod p(x)` << 1, x^62528 mod p(x)` << 1 */
        .llong 0x000000016f788474
        .llong 0x0000000055b2fb84

        /* x^61440 mod p(x)` << 1, x^61504 mod p(x)` << 1 */
        .llong 0x00000001b438810e
        .llong 0x000000018b090348

        /* x^60416 mod p(x)` << 1, x^60480 mod p(x)` << 1 */
        .llong 0x0000000095ddc6f2
        .llong 0x000000011ccbd5ea

        /* x^59392 mod p(x)` << 1, x^59456 mod p(x)` << 1 */
        .llong 0x00000001d977c20c
        .llong 0x0000000007ae47f8

        /* x^58368 mod p(x)` << 1, x^58432 mod p(x)` << 1 */
        .llong 0x00000000ebedb99a
        .llong 0x0000000172acbec0

        /* x^57344 mod p(x)` << 1, x^57408 mod p(x)` << 1 */
        .llong 0x00000001df9e9e92
        .llong 0x00000001c6e3ff20

        /* x^56320 mod p(x)` << 1, x^56384 mod p(x)` << 1 */
        .llong 0x00000001a4a3f952
        .llong 0x00000000e1b38744

        /* x^55296 mod p(x)` << 1, x^55360 mod p(x)` << 1 */
        .llong 0x00000000e2f51220
        .llong 0x00000000791585b2

        /* x^54272 mod p(x)` << 1, x^54336 mod p(x)` << 1 */
        .llong 0x000000004aa01f3e
        .llong 0x00000000ac53b894

        /* x^53248 mod p(x)` << 1, x^53312 mod p(x)` << 1 */
        .llong 0x00000000b3e90a58
        .llong 0x00000001ed5f2cf4

        /* x^52224 mod p(x)` << 1, x^52288 mod p(x)` << 1 */
        .llong 0x000000000c9ca2aa
        .llong 0x00000001df48b2e0

        /* x^51200 mod p(x)` << 1, x^51264 mod p(x)` << 1 */
        .llong 0x0000000151682316
        .llong 0x00000000049c1c62

        /* x^50176 mod p(x)` << 1, x^50240 mod p(x)` << 1 */
        .llong 0x0000000036fce78c
        .llong 0x000000017c460c12

        /* x^49152 mod p(x)` << 1, x^49216 mod p(x)` << 1 */
        .llong 0x000000009037dc10
        .llong 0x000000015be4da7e

        /* x^48128 mod p(x)` << 1, x^48192 mod p(x)` << 1 */
        .llong 0x00000000d3298582
        .llong 0x000000010f38f668

        /* x^47104 mod p(x)` << 1, x^47168 mod p(x)` << 1 */
        .llong 0x00000001b42e8ad6
        .llong 0x0000000039f40a00

        /* x^46080 mod p(x)` << 1, x^46144 mod p(x)` << 1 */
        .llong 0x00000000142a9838
        .llong 0x00000000bd4c10c4

        /* x^45056 mod p(x)` << 1, x^45120 mod p(x)` << 1 */
        .llong 0x0000000109c7f190
        .llong 0x0000000042db1d98

        /* x^44032 mod p(x)` << 1, x^44096 mod p(x)` << 1 */
        .llong 0x0000000056ff9310
        .llong 0x00000001c905bae6

        /* x^43008 mod p(x)` << 1, x^43072 mod p(x)` << 1 */
        .llong 0x00000001594513aa
        .llong 0x00000000069d40ea

        /* x^41984 mod p(x)` << 1, x^42048 mod p(x)` << 1 */
        .llong 0x00000001e3b5b1e8
        .llong 0x000000008e4fbad0

        /* x^40960 mod p(x)` << 1, x^41024 mod p(x)` << 1 */
        .llong 0x000000011dd5fc08
        .llong 0x0000000047bedd46

        /* x^39936 mod p(x)` << 1, x^40000 mod p(x)` << 1 */
        .llong 0x00000001675f0cc2
        .llong 0x0000000026396bf8

        /* x^38912 mod p(x)` << 1, x^38976 mod p(x)` << 1 */
        .llong 0x00000000d1c8dd44
        .llong 0x00000000379beb92

        /* x^37888 mod p(x)` << 1, x^37952 mod p(x)` << 1 */
        .llong 0x0000000115ebd3d8
        .llong 0x000000000abae54a

        /* x^36864 mod p(x)` << 1, x^36928 mod p(x)` << 1 */
        .llong 0x00000001ecbd0dac
        .llong 0x0000000007e6a128

        /* x^35840 mod p(x)` << 1, x^35904 mod p(x)` << 1 */
        .llong 0x00000000cdf67af2
        .llong 0x000000000ade29d2

        /* x^34816 mod p(x)` << 1, x^34880 mod p(x)` << 1 */
        .llong 0x000000004c01ff4c
        .llong 0x00000000f974c45c

        /* x^33792 mod p(x)` << 1, x^33856 mod p(x)` << 1 */
        .llong 0x00000000f2d8657e
        .llong 0x00000000e77ac60a

        /* x^32768 mod p(x)` << 1, x^32832 mod p(x)` << 1 */
        .llong 0x000000006bae74c4
        .llong 0x0000000145895816

        /* x^31744 mod p(x)` << 1, x^31808 mod p(x)` << 1 */
        .llong 0x0000000152af8aa0
        .llong 0x0000000038e362be

        /* x^30720 mod p(x)` << 1, x^30784 mod p(x)` << 1 */
        .llong 0x0000000004663802
        .llong 0x000000007f991a64

        /* x^29696 mod p(x)` << 1, x^29760 mod p(x)` << 1 */
        .llong 0x00000001ab2f5afc
        .llong 0x00000000fa366d3a

        /* x^28672 mod p(x)` << 1, x^28736 mod p(x)` << 1 */
        .llong 0x0000000074a4ebd4
        .llong 0x00000001a2bb34f0

        /* x^27648 mod p(x)` << 1, x^27712 mod p(x)` << 1 */
        .llong 0x00000001d7ab3a4c
        .llong 0x0000000028a9981e

        /* x^26624 mod p(x)` << 1, x^26688 mod p(x)` << 1 */
        .llong 0x00000001a8da60c6
        .llong 0x00000001dbc672be

        /* x^25600 mod p(x)` << 1, x^25664 mod p(x)` << 1 */
        .llong 0x000000013cf63820
        .llong 0x00000000b04d77f6

        /* x^24576 mod p(x)` << 1, x^24640 mod p(x)` << 1 */
        .llong 0x00000000bec12e1e
        .llong 0x0000000124400d96

        /* x^23552 mod p(x)` << 1, x^23616 mod p(x)` << 1 */
        .llong 0x00000001c6368010
        .llong 0x000000014ca4b414

        /* x^22528 mod p(x)` << 1, x^22592 mod p(x)` << 1 */
        .llong 0x00000001e6e78758
        .llong 0x000000012fe2c938

        /* x^21504 mod p(x)` << 1, x^21568 mod p(x)` << 1 */
        .llong 0x000000008d7f2b3c
        .llong 0x00000001faed01e6

        /* x^20480 mod p(x)` << 1, x^20544 mod p(x)` << 1 */
        .llong 0x000000016b4a156e
        .llong 0x000000007e80ecfe

        /* x^19456 mod p(x)` << 1, x^19520 mod p(x)` << 1 */
        .llong 0x00000001c63cfeb6
        .llong 0x0000000098daee94

        /* x^18432 mod p(x)` << 1, x^18496 mod p(x)` << 1 */
        .llong 0x000000015f902670
        .llong 0x000000010a04edea

        /* x^17408 mod p(x)` << 1, x^17472 mod p(x)` << 1 */
        .llong 0x00000001cd5de11e
        .llong 0x00000001c00b4524

        /* x^16384 mod p(x)` << 1, x^16448 mod p(x)` << 1 */
        .llong 0x000000001acaec54
        .llong 0x0000000170296550

        /* x^15360 mod p(x)` << 1, x^15424 mod p(x)` << 1 */
        .llong 0x000000002bd0ca78
        .llong 0x0000000181afaa48

        /* x^14336 mod p(x)` << 1, x^14400 mod p(x)` << 1 */
        .llong 0x0000000032d63d5c
        .llong 0x0000000185a31ffa

        /* x^13312 mod p(x)` << 1, x^13376 mod p(x)` << 1 */
        .llong 0x000000001c6d4e4c
        .llong 0x000000002469f608

        /* x^12288 mod p(x)` << 1, x^12352 mod p(x)` << 1 */
        .llong 0x0000000106a60b92
        .llong 0x000000006980102a

        /* x^11264 mod p(x)` << 1, x^11328 mod p(x)` << 1 */
        .llong 0x00000000d3855e12
        .llong 0x0000000111ea9ca8

        /* x^10240 mod p(x)` << 1, x^10304 mod p(x)` << 1 */
        .llong 0x00000000e3125636
        .llong 0x00000001bd1d29ce

        /* x^9216 mod p(x)` << 1, x^9280 mod p(x)` << 1 */
        .llong 0x000000009e8f7ea4
        .llong 0x00000001b34b9580

        /* x^8192 mod p(x)` << 1, x^8256 mod p(x)` << 1 */
        .llong 0x00000001c82e562c
        .llong 0x000000003076054e

        /* x^7168 mod p(x)` << 1, x^7232 mod p(x)` << 1 */
        .llong 0x00000000ca9f09ce
        .llong 0x000000012a608ea4

        /* x^6144 mod p(x)` << 1, x^6208 mod p(x)` << 1 */
        .llong 0x00000000c63764e6
        .llong 0x00000000784d05fe

        /* x^5120 mod p(x)` << 1, x^5184 mod p(x)` << 1 */
        .llong 0x0000000168d2e49e
        .llong 0x000000016ef0d82a

        /* x^4096 mod p(x)` << 1, x^4160 mod p(x)` << 1 */
        .llong 0x00000000e986c148
        .llong 0x0000000075bda454

        /* x^3072 mod p(x)` << 1, x^3136 mod p(x)` << 1 */
        .llong 0x00000000cfb65894
        .llong 0x000000003dc0a1c4

        /* x^2048 mod p(x)` << 1, x^2112 mod p(x)` << 1 */
        .llong 0x0000000111cadee4
        .llong 0x00000000e9a5d8be

        /* x^1024 mod p(x)` << 1, x^1088 mod p(x)` << 1 */
        .llong 0x0000000171fb63ce
        .llong 0x00000001609bc4b4


.short_constants_crc32c:

/* Reduce final 1024-2048 bits to 64 bits, shifting 32 bits to include the trailing 32 bits of zeros */
        /* x^1952 mod p(x) , x^1984 mod p(x) , x^2016 mod p(x) , x^2048 mod p(x)  */
        .llong 0x7fec2963e5bf8048
        .llong 0x5cf015c388e56f72

        /* x^1824 mod p(x) , x^1856 mod p(x) , x^1888 mod p(x) , x^1920 mod p(x)  */
        .llong 0x38e888d4844752a9
        .llong 0x963a18920246e2e6

        /* x^1696 mod p(x) , x^1728 mod p(x) , x^1760 mod p(x) , x^1792 mod p(x)  */
        .llong 0x42316c00730206ad
        .llong 0x419a441956993a31

        /* x^1568 mod p(x) , x^1600 mod p(x) , x^1632 mod p(x) , x^1664 mod p(x)  */
        .llong 0x543d5c543e65ddf9
        .llong 0x924752ba2b830011

        /* x^1440 mod p(x) , x^1472 mod p(x) , x^1504 mod p(x) , x^1536 mod p(x)  */
        .llong 0x78e87aaf56767c92
        .llong 0x55bd7f9518e4a304

        /* x^1312 mod p(x) , x^1344 mod p(x) , x^1376 mod p(x) , x^1408 mod p(x)  */
        .llong 0x8f68fcec1903da7f
        .llong 0x6d76739fe0553f1e

        /* x^1184 mod p(x) , x^1216 mod p(x) , x^1248 mod p(x) , x^1280 mod p(x)  */
        .llong 0x3f4840246791d588
        .llong 0xc133722b1fe0b5c3

        /* x^1056 mod p(x) , x^1088 mod p(x) , x^1120 mod p(x) , x^1152 mod p(x)  */
        .llong 0x34c96751b04de25a
        .llong 0x64b67ee0e55ef1f3

        /* x^928 mod p(x) , x^960 mod p(x) , x^992 mod p(x) , x^1024 mod p(x)  */
        .llong 0x156c8e180b4a395b
        .llong 0x069db049b8fdb1e7

        /* x^800 mod p(x) , x^832 mod p(x) , x^864 mod p(x) , x^896 mod p(x)  */
        .llong 0xe0b99ccbe661f7be
        .llong 0xa11bfaf3c9e90b9e

        /* x^672 mod p(x) , x^704 mod p(x) , x^736 mod p(x) , x^768 mod p(x)  */
        .llong 0x041d37768cd75659
        .llong 0x817cdc5119b29a35

        /* x^544 mod p(x) , x^576 mod p(x) , x^608 mod p(x) , x^640 mod p(x)  */
        .llong 0x3a0777818cfaa965
        .llong 0x1ce9d94b36c41f1c

        /* x^416 mod p(x) , x^448 mod p(x) , x^480 mod p(x) , x^512 mod p(x)  */
        .llong 0x0e148e8252377a55
        .llong 0x4f256efcb82be955

        /* x^288 mod p(x) , x^320 mod p(x) , x^352 mod p(x) , x^384 mod p(x)  */
        .llong 0x9c25531d19e65dde
        .llong 0xec1631edb2dea967

        /* x^160 mod p(x) , x^192 mod p(x) , x^224 mod p(x) , x^256 mod p(x)  */
        .llong 0x790606ff9957c0a6
        .llong 0x5d27e147510ac59a

        /* x^32 mod p(x) , x^64 mod p(x) , x^96 mod p(x) , x^128 mod p(x)  */
        .llong 0x82f63b786ea2d55c
        .llong 0xa66805eb18b8ea18

.barrett_constants_crc32c:
        /* 33 bit reflected Barrett constant m - (4^32)/n,  x^64 div p(x)` */
        .llong 0x0000000000000000
        .llong 0x00000000dea713f1
        /* 33 bit reflected Barrett constant n */
        .llong 0x0000000000000000
        .llong 0x0000000105ec76f1

#elif defined(LINUXPPC64) || defined(LINUXPPC)

.constants_crc32c:

	/* Reduce 262144 kbits to 1024 bits */
	/* x^261120 mod p(x)` << 1, x^261184 mod p(x)` << 1 */
	.octa 0x00000000b6ca9e20000000009c37c408

	/* x^260096 mod p(x)` << 1, x^260160 mod p(x)` << 1 */
	.octa 0x00000000350249a800000001b51df26c

	/* x^259072 mod p(x)` << 1, x^259136 mod p(x)` << 1 */
	.octa 0x00000001862dac54000000000724b9d0

	/* x^258048 mod p(x)` << 1, x^258112 mod p(x)` << 1 */
	.octa 0x00000001d87fb48c00000001c00532fe

	/* x^257024 mod p(x)` << 1, x^257088 mod p(x)` << 1 */
	.octa 0x00000001f39b699e00000000f05a9362

	/* x^256000 mod p(x)` << 1, x^256064 mod p(x)` << 1 */
	.octa 0x0000000101da11b400000001e1007970

	/* x^254976 mod p(x)` << 1, x^255040 mod p(x)` << 1 */
	.octa 0x00000001cab571e000000000a57366ee

	/* x^253952 mod p(x)` << 1, x^254016 mod p(x)` << 1 */
	.octa 0x00000000c7020cfe0000000192011284

	/* x^252928 mod p(x)` << 1, x^252992 mod p(x)` << 1 */
	.octa 0x00000000cdaed1ae0000000162716d9a

	/* x^251904 mod p(x)` << 1, x^251968 mod p(x)` << 1 */
	.octa 0x00000001e804effc00000000cd97ecde

	/* x^250880 mod p(x)` << 1, x^250944 mod p(x)` << 1 */
	.octa 0x0000000077c3ea3a0000000058812bc0

	/* x^249856 mod p(x)` << 1, x^249920 mod p(x)` << 1 */
	.octa 0x0000000068df31b40000000088b8c12e

	/* x^248832 mod p(x)` << 1, x^248896 mod p(x)` << 1 */
	.octa 0x00000000b059b6c200000001230b234c

	/* x^247808 mod p(x)` << 1, x^247872 mod p(x)` << 1 */
	.octa 0x0000000145fb8ed800000001120b416e

	/* x^246784 mod p(x)` << 1, x^246848 mod p(x)` << 1 */
	.octa 0x00000000cbc0916800000001974aecb0

	/* x^245760 mod p(x)` << 1, x^245824 mod p(x)` << 1 */
	.octa 0x000000005ceeedc2000000008ee3f226

	/* x^244736 mod p(x)` << 1, x^244800 mod p(x)` << 1 */
	.octa 0x0000000047d74e8600000001089aba9a

	/* x^243712 mod p(x)` << 1, x^243776 mod p(x)` << 1 */
	.octa 0x00000001407e9e220000000065113872

	/* x^242688 mod p(x)` << 1, x^242752 mod p(x)` << 1 */
	.octa 0x00000001da967bda000000005c07ec10

	/* x^241664 mod p(x)` << 1, x^241728 mod p(x)` << 1 */
	.octa 0x000000006c8983680000000187590924

	/* x^240640 mod p(x)` << 1, x^240704 mod p(x)` << 1 */
	.octa 0x00000000f2d14c9800000000e35da7c6

	/* x^239616 mod p(x)` << 1, x^239680 mod p(x)` << 1 */
	.octa 0x00000001993c6ad4000000000415855a

	/* x^238592 mod p(x)` << 1, x^238656 mod p(x)` << 1 */
	.octa 0x000000014683d1ac0000000073617758

	/* x^237568 mod p(x)` << 1, x^237632 mod p(x)` << 1 */
	.octa 0x00000001a7c93e6c0000000176021d28

	/* x^236544 mod p(x)` << 1, x^236608 mod p(x)` << 1 */
	.octa 0x000000010211e90a00000001c358fd0a

	/* x^235520 mod p(x)` << 1, x^235584 mod p(x)` << 1 */
	.octa 0x000000001119403e00000001ff7a2c18

	/* x^234496 mod p(x)` << 1, x^234560 mod p(x)` << 1 */
	.octa 0x000000001c3261aa00000000f2d9f7e4

	/* x^233472 mod p(x)` << 1, x^233536 mod p(x)` << 1 */
	.octa 0x000000014e37a634000000016cf1f9c8

	/* x^232448 mod p(x)` << 1, x^232512 mod p(x)` << 1 */
	.octa 0x0000000073786c0c000000010af9279a

	/* x^231424 mod p(x)` << 1, x^231488 mod p(x)` << 1 */
	.octa 0x000000011dc037f80000000004f101e8

	/* x^230400 mod p(x)` << 1, x^230464 mod p(x)` << 1 */
	.octa 0x0000000031433dfc0000000070bcf184

	/* x^229376 mod p(x)` << 1, x^229440 mod p(x)` << 1 */
	.octa 0x000000009cde8348000000000a8de642

	/* x^228352 mod p(x)` << 1, x^228416 mod p(x)` << 1 */
	.octa 0x0000000038d3c2a60000000062ea130c

	/* x^227328 mod p(x)` << 1, x^227392 mod p(x)` << 1 */
	.octa 0x000000011b25f26000000001eb31cbb2

	/* x^226304 mod p(x)` << 1, x^226368 mod p(x)` << 1 */
	.octa 0x000000001629e6f00000000170783448

	/* x^225280 mod p(x)` << 1, x^225344 mod p(x)` << 1 */
	.octa 0x0000000160838b4c00000001a684b4c6

	/* x^224256 mod p(x)` << 1, x^224320 mod p(x)` << 1 */
	.octa 0x000000007a44011c00000000253ca5b4

	/* x^223232 mod p(x)` << 1, x^223296 mod p(x)` << 1 */
	.octa 0x00000000226f417a0000000057b4b1e2

	/* x^222208 mod p(x)` << 1, x^222272 mod p(x)` << 1 */
	.octa 0x0000000045eb2eb400000000b6bd084c

	/* x^221184 mod p(x)` << 1, x^221248 mod p(x)` << 1 */
	.octa 0x000000014459d70c0000000123c2d592

	/* x^220160 mod p(x)` << 1, x^220224 mod p(x)` << 1 */
	.octa 0x00000001d406ed8200000000159dafce

	/* x^219136 mod p(x)` << 1, x^219200 mod p(x)` << 1 */
	.octa 0x0000000160c8e1a80000000127e1a64e

	/* x^218112 mod p(x)` << 1, x^218176 mod p(x)` << 1 */
	.octa 0x0000000027ba80980000000056860754

	/* x^217088 mod p(x)` << 1, x^217152 mod p(x)` << 1 */
	.octa 0x000000006d92d01800000001e661aae8

	/* x^216064 mod p(x)` << 1, x^216128 mod p(x)` << 1 */
	.octa 0x000000012ed7e3f200000000f82c6166

	/* x^215040 mod p(x)` << 1, x^215104 mod p(x)` << 1 */
	.octa 0x000000002dc8778800000000c4f9c7ae

	/* x^214016 mod p(x)` << 1, x^214080 mod p(x)` << 1 */
	.octa 0x0000000018240bb80000000074203d20

	/* x^212992 mod p(x)` << 1, x^213056 mod p(x)` << 1 */
	.octa 0x000000001ad381580000000198173052

	/* x^211968 mod p(x)` << 1, x^212032 mod p(x)` << 1 */
	.octa 0x00000001396b78f200000001ce8aba54

	/* x^210944 mod p(x)` << 1, x^211008 mod p(x)` << 1 */
	.octa 0x000000011a68133400000001850d5d94

	/* x^209920 mod p(x)` << 1, x^209984 mod p(x)` << 1 */
	.octa 0x000000012104732e00000001d609239c

	/* x^208896 mod p(x)` << 1, x^208960 mod p(x)` << 1 */
	.octa 0x00000000a140d90c000000001595f048

	/* x^207872 mod p(x)` << 1, x^207936 mod p(x)` << 1 */
	.octa 0x00000001b7215eda0000000042ccee08

	/* x^206848 mod p(x)` << 1, x^206912 mod p(x)` << 1 */
	.octa 0x00000001aaf1df3c000000010a389d74

	/* x^205824 mod p(x)` << 1, x^205888 mod p(x)` << 1 */
	.octa 0x0000000029d15b8a000000012a840da6

	/* x^204800 mod p(x)` << 1, x^204864 mod p(x)` << 1 */
	.octa 0x00000000f1a96922000000001d181c0c

	/* x^203776 mod p(x)` << 1, x^203840 mod p(x)` << 1 */
	.octa 0x00000001ac80d03c0000000068b7d1f6

	/* x^202752 mod p(x)` << 1, x^202816 mod p(x)` << 1 */
	.octa 0x000000000f11d56a000000005b0f14fc

	/* x^201728 mod p(x)` << 1, x^201792 mod p(x)` << 1 */
	.octa 0x00000001f1c022a20000000179e9e730

	/* x^200704 mod p(x)` << 1, x^200768 mod p(x)` << 1 */
	.octa 0x0000000173d00ae200000001ce1368d6

	/* x^199680 mod p(x)` << 1, x^199744 mod p(x)` << 1 */
	.octa 0x00000001d4ffe4ac0000000112c3a84c

	/* x^198656 mod p(x)` << 1, x^198720 mod p(x)` << 1 */
	.octa 0x000000016edc5ae400000000de940fee

	/* x^197632 mod p(x)` << 1, x^197696 mod p(x)` << 1 */
	.octa 0x00000001f1a0214000000000fe896b7e

	/* x^196608 mod p(x)` << 1, x^196672 mod p(x)` << 1 */
	.octa 0x00000000ca0b28a000000001f797431c

	/* x^195584 mod p(x)` << 1, x^195648 mod p(x)` << 1 */
	.octa 0x00000001928e30a20000000053e989ba

	/* x^194560 mod p(x)` << 1, x^194624 mod p(x)` << 1 */
	.octa 0x0000000097b1b002000000003920cd16

	/* x^193536 mod p(x)` << 1, x^193600 mod p(x)` << 1 */
	.octa 0x00000000b15bf90600000001e6f579b8

	/* x^192512 mod p(x)` << 1, x^192576 mod p(x)` << 1 */
	.octa 0x00000000411c5d52000000007493cb0a

	/* x^191488 mod p(x)` << 1, x^191552 mod p(x)` << 1 */
	.octa 0x00000001c36f330000000001bdd376d8

	/* x^190464 mod p(x)` << 1, x^190528 mod p(x)` << 1 */
	.octa 0x00000001119227e0000000016badfee6

	/* x^189440 mod p(x)` << 1, x^189504 mod p(x)` << 1 */
	.octa 0x00000000114d47020000000071de5c58

	/* x^188416 mod p(x)` << 1, x^188480 mod p(x)` << 1 */
	.octa 0x00000000458b5b9800000000453f317c

	/* x^187392 mod p(x)` << 1, x^187456 mod p(x)` << 1 */
	.octa 0x000000012e31fb8e0000000121675cce

	/* x^186368 mod p(x)` << 1, x^186432 mod p(x)` << 1 */
	.octa 0x000000005cf619d800000001f409ee92

	/* x^185344 mod p(x)` << 1, x^185408 mod p(x)` << 1 */
	.octa 0x0000000063f4d8b200000000f36b9c88

	/* x^184320 mod p(x)` << 1, x^184384 mod p(x)` << 1 */
	.octa 0x000000004138dc8a0000000036b398f4

	/* x^183296 mod p(x)` << 1, x^183360 mod p(x)` << 1 */
	.octa 0x00000001d29ee8e000000001748f9adc

	/* x^182272 mod p(x)` << 1, x^182336 mod p(x)` << 1 */
	.octa 0x000000006a08ace800000001be94ec00

	/* x^181248 mod p(x)` << 1, x^181312 mod p(x)` << 1 */
	.octa 0x0000000127d4201000000000b74370d6

	/* x^180224 mod p(x)` << 1, x^180288 mod p(x)` << 1 */
	.octa 0x0000000019d76b6200000001174d0b98

	/* x^179200 mod p(x)` << 1, x^179264 mod p(x)` << 1 */
	.octa 0x00000001b1471f6e00000000befc06a4

	/* x^178176 mod p(x)` << 1, x^178240 mod p(x)` << 1 */
	.octa 0x00000001f64c19cc00000001ae125288

	/* x^177152 mod p(x)` << 1, x^177216 mod p(x)` << 1 */
	.octa 0x00000000003c0ea00000000095c19b34

	/* x^176128 mod p(x)` << 1, x^176192 mod p(x)` << 1 */
	.octa 0x000000014d73abf600000001a78496f2

	/* x^175104 mod p(x)` << 1, x^175168 mod p(x)` << 1 */
	.octa 0x00000001620eb84400000001ac5390a0

	/* x^174080 mod p(x)` << 1, x^174144 mod p(x)` << 1 */
	.octa 0x0000000147655048000000002a80ed6e

	/* x^173056 mod p(x)` << 1, x^173120 mod p(x)` << 1 */
	.octa 0x0000000067b5077e00000001fa9b0128

	/* x^172032 mod p(x)` << 1, x^172096 mod p(x)` << 1 */
	.octa 0x0000000010ffe20600000001ea94929e

	/* x^171008 mod p(x)` << 1, x^171072 mod p(x)` << 1 */
	.octa 0x000000000fee8f1e0000000125f4305c

	/* x^169984 mod p(x)` << 1, x^170048 mod p(x)` << 1 */
	.octa 0x00000001da26fbae00000001471e2002

	/* x^168960 mod p(x)` << 1, x^169024 mod p(x)` << 1 */
	.octa 0x00000001b3a8bd880000000132d2253a

	/* x^167936 mod p(x)` << 1, x^168000 mod p(x)` << 1 */
	.octa 0x00000000e8f3898e00000000f26b3592

	/* x^166912 mod p(x)` << 1, x^166976 mod p(x)` << 1 */
	.octa 0x00000000b0d0d28c00000000bc8b67b0

	/* x^165888 mod p(x)` << 1, x^165952 mod p(x)` << 1 */
	.octa 0x0000000030f2a798000000013a826ef2

	/* x^164864 mod p(x)` << 1, x^164928 mod p(x)` << 1 */
	.octa 0x000000000fba10020000000081482c84

	/* x^163840 mod p(x)` << 1, x^163904 mod p(x)` << 1 */
	.octa 0x00000000bdb9bd7200000000e77307c2

	/* x^162816 mod p(x)` << 1, x^162880 mod p(x)` << 1 */
	.octa 0x0000000075d3bf5a00000000d4a07ec8

	/* x^161792 mod p(x)` << 1, x^161856 mod p(x)` << 1 */
	.octa 0x00000000ef1f98a00000000017102100

	/* x^160768 mod p(x)` << 1, x^160832 mod p(x)` << 1 */
	.octa 0x00000000689c760200000000db406486

	/* x^159744 mod p(x)` << 1, x^159808 mod p(x)` << 1 */
	.octa 0x000000016d5fa5fe0000000192db7f88

	/* x^158720 mod p(x)` << 1, x^158784 mod p(x)` << 1 */
	.octa 0x00000001d0d2b9ca000000018bf67b1e

	/* x^157696 mod p(x)` << 1, x^157760 mod p(x)` << 1 */
	.octa 0x0000000041e7b470000000007c09163e

	/* x^156672 mod p(x)` << 1, x^156736 mod p(x)` << 1 */
	.octa 0x00000001cbb6495e000000000adac060

	/* x^155648 mod p(x)` << 1, x^155712 mod p(x)` << 1 */
	.octa 0x000000010052a0b000000000bd8316ae

	/* x^154624 mod p(x)` << 1, x^154688 mod p(x)` << 1 */
	.octa 0x00000001d8effb5c000000019f09ab54

	/* x^153600 mod p(x)` << 1, x^153664 mod p(x)` << 1 */
	.octa 0x00000001d969853c0000000125155542

	/* x^152576 mod p(x)` << 1, x^152640 mod p(x)` << 1 */
	.octa 0x00000000523ccce2000000018fdb5882

	/* x^151552 mod p(x)` << 1, x^151616 mod p(x)` << 1 */
	.octa 0x000000001e2436bc00000000e794b3f4

	/* x^150528 mod p(x)` << 1, x^150592 mod p(x)` << 1 */
	.octa 0x00000000ddd1c3a2000000016f9bb022

	/* x^149504 mod p(x)` << 1, x^149568 mod p(x)` << 1 */
	.octa 0x0000000019fcfe3800000000290c9978

	/* x^148480 mod p(x)` << 1, x^148544 mod p(x)` << 1 */
	.octa 0x00000001ce95db640000000083c0f350

	/* x^147456 mod p(x)` << 1, x^147520 mod p(x)` << 1 */
	.octa 0x00000000af5828060000000173ea6628

	/* x^146432 mod p(x)` << 1, x^146496 mod p(x)` << 1 */
	.octa 0x00000001006388f600000001c8b4e00a

	/* x^145408 mod p(x)` << 1, x^145472 mod p(x)` << 1 */
	.octa 0x0000000179eca00a00000000de95d6aa

	/* x^144384 mod p(x)` << 1, x^144448 mod p(x)` << 1 */
	.octa 0x0000000122410a6a000000010b7f7248

	/* x^143360 mod p(x)` << 1, x^143424 mod p(x)` << 1 */
	.octa 0x000000004288e87c00000001326e3a06

	/* x^142336 mod p(x)` << 1, x^142400 mod p(x)` << 1 */
	.octa 0x000000016c5490da00000000bb62c2e6

	/* x^141312 mod p(x)` << 1, x^141376 mod p(x)` << 1 */
	.octa 0x00000000d1c71f6e0000000156a4b2c2

	/* x^140288 mod p(x)` << 1, x^140352 mod p(x)` << 1 */
	.octa 0x00000001b4ce08a6000000011dfe763a

	/* x^139264 mod p(x)` << 1, x^139328 mod p(x)` << 1 */
	.octa 0x00000001466ba60c000000007bcca8e2

	/* x^138240 mod p(x)` << 1, x^138304 mod p(x)` << 1 */
	.octa 0x00000001f6c488a40000000186118faa

	/* x^137216 mod p(x)` << 1, x^137280 mod p(x)` << 1 */
	.octa 0x000000013bfb06820000000111a65a88

	/* x^136192 mod p(x)` << 1, x^136256 mod p(x)` << 1 */
	.octa 0x00000000690e9e54000000003565e1c4

	/* x^135168 mod p(x)` << 1, x^135232 mod p(x)` << 1 */
	.octa 0x00000000281346b6000000012ed02a82

	/* x^134144 mod p(x)` << 1, x^134208 mod p(x)` << 1 */
	.octa 0x000000015646402400000000c486ecfc

	/* x^133120 mod p(x)` << 1, x^133184 mod p(x)` << 1 */
	.octa 0x000000016063a8dc0000000001b951b2

	/* x^132096 mod p(x)` << 1, x^132160 mod p(x)` << 1 */
	.octa 0x0000000116a663620000000048143916

	/* x^131072 mod p(x)` << 1, x^131136 mod p(x)` << 1 */
	.octa 0x000000017e8aa4d200000001dc2ae124

	/* x^130048 mod p(x)` << 1, x^130112 mod p(x)` << 1 */
	.octa 0x00000001728eb10c00000001416c58d6

	/* x^129024 mod p(x)` << 1, x^129088 mod p(x)` << 1 */
	.octa 0x00000001b08fd7fa00000000a479744a

	/* x^128000 mod p(x)` << 1, x^128064 mod p(x)` << 1 */
	.octa 0x00000001092a16e80000000096ca3a26

	/* x^126976 mod p(x)` << 1, x^127040 mod p(x)` << 1 */
	.octa 0x00000000a505637c00000000ff223d4e

	/* x^125952 mod p(x)` << 1, x^126016 mod p(x)` << 1 */
	.octa 0x00000000d94869b2000000010e84da42

	/* x^124928 mod p(x)` << 1, x^124992 mod p(x)` << 1 */
	.octa 0x00000001c8b203ae00000001b61ba3d0

	/* x^123904 mod p(x)` << 1, x^123968 mod p(x)` << 1 */
	.octa 0x000000005704aea000000000680f2de8

	/* x^122880 mod p(x)` << 1, x^122944 mod p(x)` << 1 */
	.octa 0x000000012e295fa2000000008772a9a8

	/* x^121856 mod p(x)` << 1, x^121920 mod p(x)` << 1 */
	.octa 0x000000011d0908bc0000000155f295bc

	/* x^120832 mod p(x)` << 1, x^120896 mod p(x)` << 1 */
	.octa 0x0000000193ed97ea00000000595f9282

	/* x^119808 mod p(x)` << 1, x^119872 mod p(x)` << 1 */
	.octa 0x000000013a0f1c520000000164b1c25a

	/* x^118784 mod p(x)` << 1, x^118848 mod p(x)` << 1 */
	.octa 0x000000010c2c40c000000000fbd67c50

	/* x^117760 mod p(x)` << 1, x^117824 mod p(x)` << 1 */
	.octa 0x00000000ff6fac3e0000000096076268

	/* x^116736 mod p(x)` << 1, x^116800 mod p(x)` << 1 */
	.octa 0x000000017b3609c000000001d288e4cc

	/* x^115712 mod p(x)` << 1, x^115776 mod p(x)` << 1 */
	.octa 0x0000000088c8c92200000001eaac1bdc

	/* x^114688 mod p(x)` << 1, x^114752 mod p(x)` << 1 */
	.octa 0x00000001751baae600000001f1ea39e2

	/* x^113664 mod p(x)` << 1, x^113728 mod p(x)` << 1 */
	.octa 0x000000010795297200000001eb6506fc

	/* x^112640 mod p(x)` << 1, x^112704 mod p(x)` << 1 */
	.octa 0x0000000162b00abe000000010f806ffe

	/* x^111616 mod p(x)` << 1, x^111680 mod p(x)` << 1 */
	.octa 0x000000000d7b404c000000010408481e

	/* x^110592 mod p(x)` << 1, x^110656 mod p(x)` << 1 */
	.octa 0x00000000763b13d40000000188260534

	/* x^109568 mod p(x)` << 1, x^109632 mod p(x)` << 1 */
	.octa 0x00000000f6dc22d80000000058fc73e0

	/* x^108544 mod p(x)` << 1, x^108608 mod p(x)` << 1 */
	.octa 0x000000007daae06000000000391c59b8

	/* x^107520 mod p(x)` << 1, x^107584 mod p(x)` << 1 */
	.octa 0x000000013359ab7c000000018b638400

	/* x^106496 mod p(x)` << 1, x^106560 mod p(x)` << 1 */
	.octa 0x000000008add438a000000011738f5c4

	/* x^105472 mod p(x)` << 1, x^105536 mod p(x)` << 1 */
	.octa 0x00000001edbefdea000000008cf7c6da

	/* x^104448 mod p(x)` << 1, x^104512 mod p(x)` << 1 */
	.octa 0x000000004104e0f800000001ef97fb16

	/* x^103424 mod p(x)` << 1, x^103488 mod p(x)` << 1 */
	.octa 0x00000000b48a82220000000102130e20

	/* x^102400 mod p(x)` << 1, x^102464 mod p(x)` << 1 */
	.octa 0x00000001bcb4684400000000db968898

	/* x^101376 mod p(x)` << 1, x^101440 mod p(x)` << 1 */
	.octa 0x000000013293ce0a00000000b5047b5e

	/* x^100352 mod p(x)` << 1, x^100416 mod p(x)` << 1 */
	.octa 0x00000001710d0844000000010b90fdb2

	/* x^99328 mod p(x)` << 1, x^99392 mod p(x)` << 1 */
	.octa 0x0000000117907f6e000000004834a32e

	/* x^98304 mod p(x)` << 1, x^98368 mod p(x)` << 1 */
	.octa 0x0000000087ddf93e0000000059c8f2b0

	/* x^97280 mod p(x)` << 1, x^97344 mod p(x)` << 1 */
	.octa 0x000000005970e9b00000000122cec508

	/* x^96256 mod p(x)` << 1, x^96320 mod p(x)` << 1 */
	.octa 0x0000000185b2b7d0000000000a330cda

	/* x^95232 mod p(x)` << 1, x^95296 mod p(x)` << 1 */
	.octa 0x00000001dcee0efc000000014a47148c

	/* x^94208 mod p(x)` << 1, x^94272 mod p(x)` << 1 */
	.octa 0x0000000030da27220000000042c61cb8

	/* x^93184 mod p(x)` << 1, x^93248 mod p(x)` << 1 */
	.octa 0x000000012f925a180000000012fe6960

	/* x^92160 mod p(x)` << 1, x^92224 mod p(x)` << 1 */
	.octa 0x00000000dd2e357c00000000dbda2c20

	/* x^91136 mod p(x)` << 1, x^91200 mod p(x)` << 1 */
	.octa 0x00000000071c80de000000011122410c

	/* x^90112 mod p(x)` << 1, x^90176 mod p(x)` << 1 */
	.octa 0x000000011513140a00000000977b2070

	/* x^89088 mod p(x)` << 1, x^89152 mod p(x)` << 1 */
	.octa 0x00000001df876e8e000000014050438e

	/* x^88064 mod p(x)` << 1, x^88128 mod p(x)` << 1 */
	.octa 0x000000015f81d6ce0000000147c840e8

	/* x^87040 mod p(x)` << 1, x^87104 mod p(x)` << 1 */
	.octa 0x000000019dd94dbe00000001cc7c88ce

	/* x^86016 mod p(x)` << 1, x^86080 mod p(x)` << 1 */
	.octa 0x00000001373d206e00000001476b35a4

	/* x^84992 mod p(x)` << 1, x^85056 mod p(x)` << 1 */
	.octa 0x00000000668ccade000000013d52d508

	/* x^83968 mod p(x)` << 1, x^84032 mod p(x)` << 1 */
	.octa 0x00000001b192d268000000008e4be32e

	/* x^82944 mod p(x)` << 1, x^83008 mod p(x)` << 1 */
	.octa 0x00000000e30f3a7800000000024120fe

	/* x^81920 mod p(x)` << 1, x^81984 mod p(x)` << 1 */
	.octa 0x000000010ef1f7bc00000000ddecddb4

	/* x^80896 mod p(x)` << 1, x^80960 mod p(x)` << 1 */
	.octa 0x00000001f5ac738000000000d4d403bc

	/* x^79872 mod p(x)` << 1, x^79936 mod p(x)` << 1 */
	.octa 0x000000011822ea7000000001734b89aa

	/* x^78848 mod p(x)` << 1, x^78912 mod p(x)` << 1 */
	.octa 0x00000000c3a33848000000010e7a58d6

	/* x^77824 mod p(x)` << 1, x^77888 mod p(x)` << 1 */
	.octa 0x00000001bd151c2400000001f9f04e9c

	/* x^76800 mod p(x)` << 1, x^76864 mod p(x)` << 1 */
	.octa 0x0000000056002d7600000000b692225e

	/* x^75776 mod p(x)` << 1, x^75840 mod p(x)` << 1 */
	.octa 0x000000014657c4f4000000019b8d3f3e

	/* x^74752 mod p(x)` << 1, x^74816 mod p(x)` << 1 */
	.octa 0x0000000113742d7c00000001a874f11e

	/* x^73728 mod p(x)` << 1, x^73792 mod p(x)` << 1 */
	.octa 0x000000019c5920ba000000010d5a4254

	/* x^72704 mod p(x)` << 1, x^72768 mod p(x)` << 1 */
	.octa 0x000000005216d2d600000000bbb2f5d6

	/* x^71680 mod p(x)` << 1, x^71744 mod p(x)` << 1 */
	.octa 0x0000000136f5ad8a0000000179cc0e36

	/* x^70656 mod p(x)` << 1, x^70720 mod p(x)` << 1 */
	.octa 0x000000018b07beb600000001dca1da4a

	/* x^69632 mod p(x)` << 1, x^69696 mod p(x)` << 1 */
	.octa 0x00000000db1e93b000000000feb1a192

	/* x^68608 mod p(x)` << 1, x^68672 mod p(x)` << 1 */
	.octa 0x000000000b96fa3a00000000d1eeedd6

	/* x^67584 mod p(x)` << 1, x^67648 mod p(x)` << 1 */
	.octa 0x00000001d9968af0000000008fad9bb4

	/* x^66560 mod p(x)` << 1, x^66624 mod p(x)` << 1 */
	.octa 0x000000000e4a77a200000001884938e4

	/* x^65536 mod p(x)` << 1, x^65600 mod p(x)` << 1 */
	.octa 0x00000000508c2ac800000001bc2e9bc0

	/* x^64512 mod p(x)` << 1, x^64576 mod p(x)` << 1 */
	.octa 0x0000000021572a8000000001f9658a68

	/* x^63488 mod p(x)` << 1, x^63552 mod p(x)` << 1 */
	.octa 0x00000001b859daf2000000001b9224fc

	/* x^62464 mod p(x)` << 1, x^62528 mod p(x)` << 1 */
	.octa 0x000000016f7884740000000055b2fb84

	/* x^61440 mod p(x)` << 1, x^61504 mod p(x)` << 1 */
	.octa 0x00000001b438810e000000018b090348

	/* x^60416 mod p(x)` << 1, x^60480 mod p(x)` << 1 */
	.octa 0x0000000095ddc6f2000000011ccbd5ea

	/* x^59392 mod p(x)` << 1, x^59456 mod p(x)` << 1 */
	.octa 0x00000001d977c20c0000000007ae47f8

	/* x^58368 mod p(x)` << 1, x^58432 mod p(x)` << 1 */
	.octa 0x00000000ebedb99a0000000172acbec0

	/* x^57344 mod p(x)` << 1, x^57408 mod p(x)` << 1 */
	.octa 0x00000001df9e9e9200000001c6e3ff20

	/* x^56320 mod p(x)` << 1, x^56384 mod p(x)` << 1 */
	.octa 0x00000001a4a3f95200000000e1b38744

	/* x^55296 mod p(x)` << 1, x^55360 mod p(x)` << 1 */
	.octa 0x00000000e2f5122000000000791585b2

	/* x^54272 mod p(x)` << 1, x^54336 mod p(x)` << 1 */
	.octa 0x000000004aa01f3e00000000ac53b894

	/* x^53248 mod p(x)` << 1, x^53312 mod p(x)` << 1 */
	.octa 0x00000000b3e90a5800000001ed5f2cf4

	/* x^52224 mod p(x)` << 1, x^52288 mod p(x)` << 1 */
	.octa 0x000000000c9ca2aa00000001df48b2e0

	/* x^51200 mod p(x)` << 1, x^51264 mod p(x)` << 1 */
	.octa 0x000000015168231600000000049c1c62

	/* x^50176 mod p(x)` << 1, x^50240 mod p(x)` << 1 */
	.octa 0x0000000036fce78c000000017c460c12

	/* x^49152 mod p(x)` << 1, x^49216 mod p(x)` << 1 */
	.octa 0x000000009037dc10000000015be4da7e

	/* x^48128 mod p(x)` << 1, x^48192 mod p(x)` << 1 */
	.octa 0x00000000d3298582000000010f38f668

	/* x^47104 mod p(x)` << 1, x^47168 mod p(x)` << 1 */
	.octa 0x00000001b42e8ad60000000039f40a00

	/* x^46080 mod p(x)` << 1, x^46144 mod p(x)` << 1 */
	.octa 0x00000000142a983800000000bd4c10c4

	/* x^45056 mod p(x)` << 1, x^45120 mod p(x)` << 1 */
	.octa 0x0000000109c7f1900000000042db1d98

	/* x^44032 mod p(x)` << 1, x^44096 mod p(x)` << 1 */
	.octa 0x0000000056ff931000000001c905bae6

	/* x^43008 mod p(x)` << 1, x^43072 mod p(x)` << 1 */
	.octa 0x00000001594513aa00000000069d40ea

	/* x^41984 mod p(x)` << 1, x^42048 mod p(x)` << 1 */
	.octa 0x00000001e3b5b1e8000000008e4fbad0

	/* x^40960 mod p(x)` << 1, x^41024 mod p(x)` << 1 */
	.octa 0x000000011dd5fc080000000047bedd46

	/* x^39936 mod p(x)` << 1, x^40000 mod p(x)` << 1 */
	.octa 0x00000001675f0cc20000000026396bf8

	/* x^38912 mod p(x)` << 1, x^38976 mod p(x)` << 1 */
	.octa 0x00000000d1c8dd4400000000379beb92

	/* x^37888 mod p(x)` << 1, x^37952 mod p(x)` << 1 */
	.octa 0x0000000115ebd3d8000000000abae54a

	/* x^36864 mod p(x)` << 1, x^36928 mod p(x)` << 1 */
	.octa 0x00000001ecbd0dac0000000007e6a128

	/* x^35840 mod p(x)` << 1, x^35904 mod p(x)` << 1 */
	.octa 0x00000000cdf67af2000000000ade29d2

	/* x^34816 mod p(x)` << 1, x^34880 mod p(x)` << 1 */
	.octa 0x000000004c01ff4c00000000f974c45c

	/* x^33792 mod p(x)` << 1, x^33856 mod p(x)` << 1 */
	.octa 0x00000000f2d8657e00000000e77ac60a

	/* x^32768 mod p(x)` << 1, x^32832 mod p(x)` << 1 */
	.octa 0x000000006bae74c40000000145895816

	/* x^31744 mod p(x)` << 1, x^31808 mod p(x)` << 1 */
	.octa 0x0000000152af8aa00000000038e362be

	/* x^30720 mod p(x)` << 1, x^30784 mod p(x)` << 1 */
	.octa 0x0000000004663802000000007f991a64

	/* x^29696 mod p(x)` << 1, x^29760 mod p(x)` << 1 */
	.octa 0x00000001ab2f5afc00000000fa366d3a

	/* x^28672 mod p(x)` << 1, x^28736 mod p(x)` << 1 */
	.octa 0x0000000074a4ebd400000001a2bb34f0

	/* x^27648 mod p(x)` << 1, x^27712 mod p(x)` << 1 */
	.octa 0x00000001d7ab3a4c0000000028a9981e

	/* x^26624 mod p(x)` << 1, x^26688 mod p(x)` << 1 */
	.octa 0x00000001a8da60c600000001dbc672be

	/* x^25600 mod p(x)` << 1, x^25664 mod p(x)` << 1 */
	.octa 0x000000013cf6382000000000b04d77f6

	/* x^24576 mod p(x)` << 1, x^24640 mod p(x)` << 1 */
	.octa 0x00000000bec12e1e0000000124400d96

	/* x^23552 mod p(x)` << 1, x^23616 mod p(x)` << 1 */
	.octa 0x00000001c6368010000000014ca4b414

	/* x^22528 mod p(x)` << 1, x^22592 mod p(x)` << 1 */
	.octa 0x00000001e6e78758000000012fe2c938

	/* x^21504 mod p(x)` << 1, x^21568 mod p(x)` << 1 */
	.octa 0x000000008d7f2b3c00000001faed01e6

	/* x^20480 mod p(x)` << 1, x^20544 mod p(x)` << 1 */
	.octa 0x000000016b4a156e000000007e80ecfe

	/* x^19456 mod p(x)` << 1, x^19520 mod p(x)` << 1 */
	.octa 0x00000001c63cfeb60000000098daee94

	/* x^18432 mod p(x)` << 1, x^18496 mod p(x)` << 1 */
	.octa 0x000000015f902670000000010a04edea

	/* x^17408 mod p(x)` << 1, x^17472 mod p(x)` << 1 */
	.octa 0x00000001cd5de11e00000001c00b4524

	/* x^16384 mod p(x)` << 1, x^16448 mod p(x)` << 1 */
	.octa 0x000000001acaec540000000170296550

	/* x^15360 mod p(x)` << 1, x^15424 mod p(x)` << 1 */
	.octa 0x000000002bd0ca780000000181afaa48

	/* x^14336 mod p(x)` << 1, x^14400 mod p(x)` << 1 */
	.octa 0x0000000032d63d5c0000000185a31ffa

	/* x^13312 mod p(x)` << 1, x^13376 mod p(x)` << 1 */
	.octa 0x000000001c6d4e4c000000002469f608

	/* x^12288 mod p(x)` << 1, x^12352 mod p(x)` << 1 */
	.octa 0x0000000106a60b92000000006980102a

	/* x^11264 mod p(x)` << 1, x^11328 mod p(x)` << 1 */
	.octa 0x00000000d3855e120000000111ea9ca8

	/* x^10240 mod p(x)` << 1, x^10304 mod p(x)` << 1 */
	.octa 0x00000000e312563600000001bd1d29ce

	/* x^9216 mod p(x)` << 1, x^9280 mod p(x)` << 1 */
	.octa 0x000000009e8f7ea400000001b34b9580

	/* x^8192 mod p(x)` << 1, x^8256 mod p(x)` << 1 */
	.octa 0x00000001c82e562c000000003076054e

	/* x^7168 mod p(x)` << 1, x^7232 mod p(x)` << 1 */
	.octa 0x00000000ca9f09ce000000012a608ea4

	/* x^6144 mod p(x)` << 1, x^6208 mod p(x)` << 1 */
	.octa 0x00000000c63764e600000000784d05fe

	/* x^5120 mod p(x)` << 1, x^5184 mod p(x)` << 1 */
	.octa 0x0000000168d2e49e000000016ef0d82a

	/* x^4096 mod p(x)` << 1, x^4160 mod p(x)` << 1 */
	.octa 0x00000000e986c1480000000075bda454

	/* x^3072 mod p(x)` << 1, x^3136 mod p(x)` << 1 */
	.octa 0x00000000cfb65894000000003dc0a1c4

	/* x^2048 mod p(x)` << 1, x^2112 mod p(x)` << 1 */
	.octa 0x0000000111cadee400000000e9a5d8be

	/* x^1024 mod p(x)` << 1, x^1088 mod p(x)` << 1 */
	.octa 0x0000000171fb63ce00000001609bc4b4

.short_constants_crc32c:

	/* Reduce final 1024-2048 bits to 64 bits, shifting 32 bits to include the trailing 32 bits of zeros */
	/* x^1952 mod p(x)`, x^1984 mod p(x)`, x^2016 mod p(x)`, x^2048 mod p(x)` */
	.octa 0x7fec2963e5bf80485cf015c388e56f72

	/* x^1824 mod p(x)`, x^1856 mod p(x)`, x^1888 mod p(x)`, x^1920 mod p(x)` */
	.octa 0x38e888d4844752a9963a18920246e2e6

	/* x^1696 mod p(x)`, x^1728 mod p(x)`, x^1760 mod p(x)`, x^1792 mod p(x)` */
	.octa 0x42316c00730206ad419a441956993a31

	/* x^1568 mod p(x)`, x^1600 mod p(x)`, x^1632 mod p(x)`, x^1664 mod p(x)` */
	.octa 0x543d5c543e65ddf9924752ba2b830011

	/* x^1440 mod p(x)`, x^1472 mod p(x)`, x^1504 mod p(x)`, x^1536 mod p(x)` */
	.octa 0x78e87aaf56767c9255bd7f9518e4a304

	/* x^1312 mod p(x)`, x^1344 mod p(x)`, x^1376 mod p(x)`, x^1408 mod p(x)` */
	.octa 0x8f68fcec1903da7f6d76739fe0553f1e

	/* x^1184 mod p(x)`, x^1216 mod p(x)`, x^1248 mod p(x)`, x^1280 mod p(x)` */
	.octa 0x3f4840246791d588c133722b1fe0b5c3

	/* x^1056 mod p(x)`, x^1088 mod p(x)`, x^1120 mod p(x)`, x^1152 mod p(x)` */
	.octa 0x34c96751b04de25a64b67ee0e55ef1f3

	/* x^928 mod p(x)`, x^960 mod p(x)`, x^992 mod p(x)`, x^1024 mod p(x)` */
	.octa 0x156c8e180b4a395b069db049b8fdb1e7

	/* x^800 mod p(x)`, x^832 mod p(x)`, x^864 mod p(x)`, x^896 mod p(x)` */
	.octa 0xe0b99ccbe661f7bea11bfaf3c9e90b9e

	/* x^672 mod p(x)`, x^704 mod p(x)`, x^736 mod p(x)`, x^768 mod p(x)` */
	.octa 0x041d37768cd75659817cdc5119b29a35

	/* x^544 mod p(x)`, x^576 mod p(x)`, x^608 mod p(x)`, x^640 mod p(x)` */
	.octa 0x3a0777818cfaa9651ce9d94b36c41f1c

	/* x^416 mod p(x)`, x^448 mod p(x)`, x^480 mod p(x)`, x^512 mod p(x)` */
	.octa 0x0e148e8252377a554f256efcb82be955

	/* x^288 mod p(x)`, x^320 mod p(x)`, x^352 mod p(x)`, x^384 mod p(x)` */
	.octa 0x9c25531d19e65ddeec1631edb2dea967

	/* x^160 mod p(x)`, x^192 mod p(x)`, x^224 mod p(x)`, x^256 mod p(x)` */
	.octa 0x790606ff9957c0a65d27e147510ac59a

	/* x^32 mod p(x)`, x^64 mod p(x)`, x^96 mod p(x)`, x^128 mod p(x)` */
	.octa 0x82f63b786ea2d55ca66805eb18b8ea18


.barrett_constants_crc32c:
	/* 33 bit reflected Barrett constant m - (4^32)/n */
	.octa 0x000000000000000000000000dea713f1	/* x^64 div p(x)` */
	/* 33 bit reflected Barrett constant n */
	.octa 0x00000000000000000000000105ec76f1

#endif
#endif
