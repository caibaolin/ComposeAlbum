// IDecodeFileCallback.aidl
package com.eebbk.ovumserver;

// Declare any non-default types here with import statements

interface IUnlockFileCallback {

    void onUnlocking(int progress);

    void onUnlockFinsih(boolean isSuccessful);
}
