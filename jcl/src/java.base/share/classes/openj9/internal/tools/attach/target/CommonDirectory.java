/*[INCLUDE-IF JAVA_SPEC_VERSION >= 8]*/
/*******************************************************************************
 * Copyright IBM Corp. and others 2009
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
package openj9.internal.tools.attach.target;

import static openj9.internal.tools.attach.target.IPC.LOGGING_DISABLED;
import static openj9.internal.tools.attach.target.IPC.loggingStatus;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;

/**
 * This class maintains the directory holding the attach API file artifacts.
 *
 */
public abstract class CommonDirectory {
	private static final String ATTACH_LOCK = "_attachlock"; //$NON-NLS-1$
	private static final String COM_IBM_TOOLS_ATTACH_DIRECTORY = "com.ibm.tools.attach.directory"; //$NON-NLS-1$
	private static final int COMMON_DIRECTORY_PERMISSIONS = 01777; /* allow anyone to create directories, but only owner can delete */
	private static final int COMMON_LOCK_FILE_PERMISSIONS = 0666; /* allow anyone to create and use the file */
	private static final String CONTROLLER_LOCKFILE = "_controller"; //$NON-NLS-1$
	static final String CONTROLLER_NOTIFIER = "_notifier"; //$NON-NLS-1$
	static final int SEMAPHORE_OKAY = 0;
	private static final String TRASH_PREFIX = ".trash_"; //$NON-NLS-1$

	static final class syncObject { /* empty class for synchronization only. */}
	private static final syncObject accessorMutex = new syncObject();
	private static FileLock attachLock;
	private static File commonDirFile; /* file where all IPC files are held */
	private static FileLock controllerLock;
	private static String semaphoreId;
	private static int controllerLockCount = 0;
	/**
	 * default name of directories where VMs place their advertisements
	 */
	private static final String systemTmpDir = IPC.getTmpDir();
	/**
	 * Indicates the control file is newer than the semaphore
	 */
	public static final int J9PORT_INFO_SHSEM_OPENED_STALE = 103;
	/**
	 * commonDir holds the IPC directories, including those holding advertisements and semaphores
	 * @param dir File object for the IPC directory.
	 */
	private  static void setCommonDirFileObject(File dir) {
		synchronized (accessorMutex) {
			commonDirFile = dir;
		}
	}

	/**
	 * @return File object for the IPC directory.
	 */
	public static  File getCommonDirFileObject() {
		synchronized (accessorMutex) { /* Jazz 27019: lazy initialization of the object */
			if (null == commonDirFile) {
				/*[PR CMVC 165300 initialize no longer throws an IOException ]*/
				initialize();
			}
			return commonDirFile;
		}
	}

	/**
	 * @return Path to the IPC directory.
	 * @note this strips trailing directory separators
	 */
	static  String getCommonDirPath() {
		return getCommonDirFileObject().getAbsolutePath();
	}

	private static void initialize() {
		/* Set the common directory used by all VMs.  Defaults to /tmp/.com_ibm_tools_attach */
		String ipcDirProperty = com.ibm.oti.vm.VM.getVMLangAccess().internalGetProperties().getProperty(COM_IBM_TOOLS_ATTACH_DIRECTORY, 
				(new File(systemTmpDir,".com_ibm_tools_attach")).getPath()); //$NON-NLS-1$
		/*[PR CMVC 165300 restriction on embedded blanks was unnecessary. Also, trailing separators were redundant. ]*/
		if (LOGGING_DISABLED != loggingStatus) {
			IPC.logMessage("IPC Directory=", ipcDirProperty); //$NON-NLS-1$
		}
		File cd = new File(ipcDirProperty);
		setCommonDirFileObject(cd);
	}

	/**
	 * Sets up the common directory
	 * @throws IOException if common directory is not writable
	 */
	 /*[PR Jazz 30075] createDirectoryAndSemaphore is a misnomer, since it does not create the semaphore. */
	static void prepareCommonDirectory() throws IOException {
		File cd = getCommonDirFileObject();
		if (cd.exists()) {
			if (!cd.canWrite()) {
				IPC.logMessage("could not write ", cd.getAbsolutePath()); //$NON-NLS-1$				
				throw new IOException(cd.getAbsolutePath());
			} else if (!cd.isDirectory()) {
				if (!cd.delete()) { /* file where a directory should be */
					IPC.logMessage("could not delete ", cd.getAbsolutePath()); //$NON-NLS-1$
					throw new IOException(cd.getAbsolutePath());
				} else {
					IPC.logMessage("deleted ", cd.getAbsolutePath()); //$NON-NLS-1$
				}
				IPC.mkdirWithPermissions(cd.getAbsolutePath(), COMMON_DIRECTORY_PERMISSIONS);
				IPC.logMessage("After deletion of an existing file, created the common directory at ", cd.getAbsolutePath()); //$NON-NLS-1$
			} else {
				if (LOGGING_DISABLED != loggingStatus) {
					IPC.logMessage("The common directory already exists at ", cd.getAbsolutePath()); //$NON-NLS-1$
				}
			}
		} else {
			IPC.mkdirWithPermissions(cd.getAbsolutePath(), COMMON_DIRECTORY_PERMISSIONS);
			if (LOGGING_DISABLED != loggingStatus) {
				IPC.logMessage("Created the common directory at ", cd.getAbsolutePath()); //$NON-NLS-1$
			}
		}
		/*[PR Jazz 30075] setupSemaphore was redundant. */
	}
	/**
	 * Lock the controller lock file. Create the lockfile if necessary.
	 * @param callSite caller info
	 * @throws IOException if the file is already locked.
	 */
	public static void obtainControllerLock(String callSite) throws IOException {
		FileLock controllerLockCopy = null;
		synchronized (accessorMutex) {
			++controllerLockCount;
			if (1 == controllerLockCount) {
				controllerLockCopy = getControllerLock();
			}
		}
		if (null != controllerLockCopy) { /* first entry */
			controllerLockCopy.lockFile(true, callSite + "_obtainControllerLock", IPC.useFileLockWatchdog); //$NON-NLS-1$
		}
	}

	/**
	 * non-blocking lock the controller lockfile. 
	 * Create the lockfile if necessary.
	 * @param callSite caller info
	 * @return true if lock obtained
	 */
	static boolean tryObtainControllerLock(String callSite) {
		boolean controllerLockEntered = true;
		synchronized (accessorMutex) {
			++controllerLockCount; /* optimistically assume we enter the lock */
			if (1 == controllerLockCount) { /* first time in */
				try {
					controllerLockEntered = getControllerLock().lockFile(false, callSite + "_tryObtainControllerLock", IPC.useFileLockWatchdog); //$NON-NLS-1$
					if (!controllerLockEntered) { /* lock failed, so revert */
						--controllerLockCount;
					}
				} catch (IOException e) { /* this shouldn't happen */
					controllerLockEntered = false;
					IPC.logMessage("IOException in tryObtainControllerLock," //$NON-NLS-1$
							+ " controllerLockCount=" +controllerLockCount, e); //$NON-NLS-1$
				}
			}
			return controllerLockEntered;		
		}
	}

	/**
	 * Release the lock on the controller lock file
	 * @param callSite caller info
	 */
	public static void releaseControllerLock(String callSite) {
		synchronized (accessorMutex) {
			if (controllerLockCount <= 0) {
				IPC.logMessage("releaseControllerLock: Illegal value for controllerLockCount", controllerLockCount); //$NON-NLS-1$
				return;
			}
			--controllerLockCount;
			if (Objects.nonNull(controllerLock) && (0 == controllerLockCount)) {
				controllerLock.unlockFile(callSite + "_releaseControllerLock"); //$NON-NLS-1$
				controllerLock = null;
			}
		}
	}

	/**
	 * Lock the attach lockfile. Create the lockfile if necessary.
	 * @param callSite caller info
	 * @throws IOException if file already locked
	 */
	 
	public static void obtainAttachLock(String callSite) throws IOException {
		getAttachLock().lockFile(true, callSite + "_obtainAttachLock", IPC.useFileLockWatchdog); //$NON-NLS-1$
	}

	/**
	 * Release the lock on the attach lock file
	 * @param callSite caller info
	 */
	public static void releaseAttachLock(String callSite) {
		try {
			getAttachLock().unlockFile(callSite + "_releaseAttachLock"); //$NON-NLS-1$
		} catch (NullPointerException e) {
			return;
		}
	}

	/**
	 * Create the control file for the semaphore
	 * @throws IOException
	 */
	 /*[PR Jazz 30075] refactored out of obtainControllerLock */
	static void createNotificationFile() throws IOException {
		File notifierFile = new File(getCommonDirFileObject(), CONTROLLER_NOTIFIER);
		if (notifierFile.createNewFile()) {
			IPC.chmod(notifierFile.getAbsolutePath(), COMMON_LOCK_FILE_PERMISSIONS);
		}
	}

	/**
	 * @param obtainLock 
	 * @return name of semaphore
	 * @throws IOException if mkdir fails
	 * Caller is responsible for ensuring that the controller lockfile is held.
	 */
	static String openSemaphore() throws IOException {
		String semName = CONTROLLER_NOTIFIER; /*[PR Jazz 48044] semaphore name is a constant */
		int status = IPC.openSemaphore(getCommonDirFileObject().getAbsolutePath(), semName, true);
		/*[MSG "K0538", "semaphore {0} status= {1}"]*/
		if (SEMAPHORE_OKAY != status) {
			throw new IOException(com.ibm.oti.util.Msg.getString("K0538" , semName, Integer.valueOf(status)));  //$NON-NLS-1$
		}
		return semName;
	}

	/**
	 * close the semaphore and reopen it.  
	 * @return 0 if success
	 */
	static int reopenSemaphore() {
		int status = 0;
		IPC.logMessage("reopenSemaphore"); //$NON-NLS-1$
		closeSemaphore();
		status = IPC.openSemaphore(getCommonDirFileObject().getAbsolutePath(), CONTROLLER_NOTIFIER, true);		
		return status;
	}

	/**
	 * wait for a post on the semaphore for this VM. Use notifyVm() to do the post
	 * @param myVmId for diagnostics
	 * @return 0 if success.
	 */
	static int waitSemaphore(String myVmId) {

		int status = IPC.waitSemaphore();
		if (SEMAPHORE_OKAY != status) {
			IPC.logMessage("waitSemaphore status != JNI_OK, ==", status); //$NON-NLS-1$
		}
		return status;
	}

	/**
	 * Open the semaphore, post to it, and close it
	 * @param numberOfTargets number of times to post to the semaphore
	 * @param global Use the global semaphore (Windows only)
	 * @param callSite caller info
	 * @return 0 on success
	 */
	public static int notifyVm(int numberOfTargets, boolean global, String callSite) {
		IPC.logMessage(callSite + "_notifyVm ", numberOfTargets, " targets"); //$NON-NLS-1$ //$NON-NLS-2$
		IPC.logMessage(getCommonDirFileObject().getAbsolutePath() + ", global = " + global); //$NON-NLS-1$
		return IPC.notifyVm(getCommonDirFileObject().getAbsolutePath(), CONTROLLER_NOTIFIER, numberOfTargets, global);
	}

	/**
	 * Open the semaphore, decrement it without blocking to it, and close it
	 * @param numberOfTargets number of times to decrement to the semaphore
	 * @param global Use the global semaphore (Windows only)
	 * @return 0 on success
	 */
	public static int cancelNotify(int numberOfTargets, boolean global) {
		return IPC.cancelNotify(getCommonDirPath(), CONTROLLER_NOTIFIER, numberOfTargets, global);
	}

	/**
	 * close but do not destroy this VM's notification semaphore
	 */
	static void closeSemaphore() {

		IPC.closeSemaphore();
	}

	/**
	 * close and destroy the semaphore if possible
	 */
	static void destroySemaphore() {
		IPC.destroySemaphore();
	}

	/**
	 * Count the number of target directories in the common directory
	 * @return Number of directories
	 */
	public static int countTargetDirectories () {
		File dir= getCommonDirFileObject();

		if (!dir.isDirectory()) {
			return 0;
		}
		File[] vmDirs = dir.listFiles();
		if (null == vmDirs) {
			return 0;
		} else {
			int count = 0;
			for (int i = 0; i < vmDirs.length; ++i) {
				String dirMemberName = vmDirs[i].getName();
				if (dirMemberName.startsWith(TRASH_PREFIX) || isCommonControlFile(dirMemberName)) {
					continue;
				}
				++count;
			}
			return count;
		}
	}

	static private boolean isCommonControlFile(String dirMemberName) {
		return (ATTACH_LOCK.equalsIgnoreCase(dirMemberName) || CONTROLLER_LOCKFILE.equalsIgnoreCase(dirMemberName) 
				|| CONTROLLER_NOTIFIER.equalsIgnoreCase(dirMemberName));
	}

	/**
	 * Look for and delete leftover files and directories from previous VMs
	 * @param myId VMID of the current.  Set to non-null to prevent deleting own directory.
	 */
	static void deleteStaleDirectories(String myId) {
		long myUid = IPC.getUid();
		if (LOGGING_DISABLED != loggingStatus) {
			IPC.logMessage("deleting stale dirs, myId = " + myId + ", myUid = " + myUid); //$NON-NLS-1$ //$NON-NLS-2$
		}
		File[] vmDirs = getCommonDirFileObject().listFiles(new DirectorySampler());
		if (null == vmDirs) {
			return;
		}
		for (File dirMember: vmDirs) {
			/*[PR 169137 abort if we shutting down while checking]*/
			if (AttachHandler.isAttachApiTerminated()) {
				IPC.logMessage("deleteStaleDirectories aborted due to AttachAPI is terminated"); //$NON-NLS-1$
				break;
			}

			if (!isFileOwnedByUid(dirMember, myUid)) {
				continue;
			}
			String dirMemberName = dirMember.getName();
			if (LOGGING_DISABLED != loggingStatus) {
				IPC.logMessage("deleteStaleDirectories checking ", dirMemberName); //$NON-NLS-1$
			}
			if (dirMember.isFile()) {
				if (isCommonControlFile(dirMemberName)) {
					continue;
				}
				if (dirMemberName.startsWith(TRASH_PREFIX)) {
					IPC.logMessage("delete trash file ", dirMemberName); //$NON-NLS-1$
					if (!dirMember.delete()) {
						IPC.logMessage("error deleting ", dirMemberName); //$NON-NLS-1$
					}
				} 
			} else { /* the member is a directory */
				if (dirMemberName.equalsIgnoreCase(myId)) {
					continue; /* Jazz 26865: no need to check myself */
				} 
				long pid;
				if (Character.isDigit(dirMemberName.charAt(0))) {
					/* 
					 * directory name is probably numeric.  Doing a quick test and catching the exception 
					 * is faster than doing a full regex match.
					 */
					try {
						long uid;
						pid = Long.parseLong(dirMemberName);
						uid = CommonDirectory.getFileOwner(dirMember.getAbsolutePath());
						if (((0 != myUid) && (uid != myUid))) {
							/* 
							 * If I am not running as root and the target is owned by another UID, then ignore the file.
							 */
							pid = 0;
						}
					} catch (NumberFormatException e) {
						pid = getPidFromFile(dirMember, myUid);
					}
				} else { 			
					pid = getPidFromFile(dirMember, myUid);
				}
				if ((0 != pid) /* the PID is valid and directory is owned by me or I am root */
						&& !IPC.processExists(pid)) {
					IPC.logMessage("delete defunct directory ", dirMemberName); //$NON-NLS-1$
					TargetDirectory.deleteTargetDirectory(dirMemberName);
				}
			}
		}
	}

	/**
	 * Check if the file is owned by the UID.  Note that UID 0 "owns" all files.
	 * @param f File or directory
	 * @param myUid user UID. 
	 * @return true if the uid owns the file or uid == 0.
	 */
	public static boolean isFileOwnedByUid(File f, long myUid) {
		return (0 == myUid) || (myUid == CommonDirectory.getFileOwner(f.getAbsolutePath()));
	}
	
		/**
		 * reads the process ID from the advertisement file. 
		 * Returns 0 (illegal PID) if the advertisement file does not exist
		 * or the process is not owned by the current user.
		 * @param dirMember directory which should hold an advertisement file
		 * @return the target process's PID or 0
		 */
	private static long getPidFromFile(File dirMember, long myUid) {
		long pid = 0;
		File advertFile = new File(dirMember, Advertisement.getFilename());
		if (!advertFile.exists() || (0 == advertFile.length())) {
			IPC.logMessage("delete stale directory ", dirMember.getName()); //$NON-NLS-1$
			TargetDirectory.deleteTargetDirectory(dirMember.getName());
		}
		Advertisement advert;
		try (FileInputStream propStream = new FileInputStream(advertFile)) {
			advert = Advertisement.readAdvertisementFile(propStream); /* throws IOException if file cannot be read */
			propStream.close();
		} catch (IOException e) {
			return 0;
		}
		pid = advert.getProcessId();
		long uid = advert.getUid();
		if (LOGGING_DISABLED != loggingStatus) {
			IPC.logMessage("getPidFromFile pid = ", (int) pid, ", fileNmae = " + dirMember.getName()); //$NON-NLS-1$ //$NON-NLS-2$
		}
		/*advertisement is from an older version or is corrupt, or claims to be owned by root.  Get the owner via file stat ]*/
		if (0 == uid) {
			uid = CommonDirectory.getFileOwner(dirMember.getAbsolutePath());
		}
		if (LOGGING_DISABLED != loggingStatus) {
			IPC.logMessage("getPidFromFile uid = ", (int) uid); //$NON-NLS-1$
		}
		if (((0 != myUid) && (uid != myUid))) {
			/* 
			 * If I am not running as root and the target is owned by another UID, then ignore the file.
			 */
			pid = 0;
		}
		return pid;
	}

	private static FileLock getAttachLock() {
		synchronized (accessorMutex) {
			if (null == attachLock) { /* hang on to a copy since we need it at shutdown */
				/*[PR Jazz 30075] inlined function which was called only from this method */
				attachLock = new FileLock((new File(getCommonDirFileObject(), ATTACH_LOCK)).getAbsolutePath(), COMMON_LOCK_FILE_PERMISSIONS);
			}
		}
		return attachLock;
	}
	
	/**
	 * Returns a FileLock object, creating it if necessary.
	 * @return FileLock object
	 * @note this is not thread safe.  This should be called while holding accessorMutex.
	 */
	private static FileLock getControllerLock() {
		if (null == controllerLock) {
			File commonDirFileObject = new File(getCommonDirFileObject(), CONTROLLER_LOCKFILE);
			String commonDirPath = commonDirFileObject.getAbsolutePath();
			controllerLock = new FileLock(commonDirPath, COMMON_LOCK_FILE_PERMISSIONS);
		}
		return controllerLock;
	}
	
	/**
	 * Get the UID of a file's owner
	 * @param path file path
	 * @return UID of file owner
	 */
	public static native long getFileOwner(String path);
	
	static final class DirectorySampler implements FileFilter {

		private int acceptCount = 16;
		private long skip;
		private long range = 2;
		@Override
		public boolean accept(File candidate) {
			if (acceptCount > 0) { /* accept the first N files unconditionally */
				--acceptCount;
				return true;
			}
			/* then accept one, reject a random number (possibly 0), accept one, reject a larger random number, accept one, and so on. */
			if (skip > 0) {
				--skip;
				return false;
			} else {
				skip = System.currentTimeMillis() % range;
				range *= 2;
				return true;
			}
		}

	}
}
