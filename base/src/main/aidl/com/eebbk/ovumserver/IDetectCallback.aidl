// IDetectCallback.aidl
package com.eebbk.ovumserver;

// Declare any non-default types here with import statements

interface IDetectCallback {

    void onResult(float possibility, boolean isViolate);
}
