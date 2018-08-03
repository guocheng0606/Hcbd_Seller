package com.android.hcbd.seller.crash;

/**
 * Created by guocheng on 2017/5/10.
 */

final class QuitCockroachException extends RuntimeException {
    public QuitCockroachException(String message) {
        super(message);
    }
}
