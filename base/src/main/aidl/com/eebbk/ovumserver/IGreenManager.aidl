// IGreenManager.aidl
package com.eebbk.ovumserver;
import android.os.ParcelFileDescriptor;
import com.eebbk.ovumserver.IDetectCallback;
import com.eebbk.ovumserver.IUnlockFileCallback;
import com.eebbk.ovumserver.ViolationFileInfo;

/**
* {@hide}
*/
interface IGreenManager {
    void pictureDetectByShareMemory(in ParcelFileDescriptor pfd, int size, IDetectCallback callback);

    void pictureDetectByFile(String path, int width, int height, int format, IDetectCallback callback);

    void textContentDetect(String content, IDetectCallback callback);

    List<String> getInterceptedFilePathList();

    String getInterceptedFileFormat(String path);

    List<ViolationFileInfo> getInterceptedFileInfoList();

    void unlockViolationFile(String path, IUnlockFileCallback callback);
}