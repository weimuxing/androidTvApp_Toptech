/*___Generated_by_IDEA___*/

/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: Z:\\348_server\\msd348_AN6.0\\device\\mstar\\common\\apps\\MBrowser3\\src\\com\\android\\localmmservice\\ILocalMediaStatusListener.aidl
 */
package com.android.localmmservice;
public interface ILocalMediaStatusListener extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.android.localmmservice.ILocalMediaStatusListener
{
private static final java.lang.String DESCRIPTOR = "com.android.localmmservice.ILocalMediaStatusListener";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.android.localmmservice.ILocalMediaStatusListener interface,
 * generating a proxy if needed.
 */
public static com.android.localmmservice.ILocalMediaStatusListener asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.android.localmmservice.ILocalMediaStatusListener))) {
return ((com.android.localmmservice.ILocalMediaStatusListener)iin);
}
return new com.android.localmmservice.ILocalMediaStatusListener.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_mediaPlayInfo:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _arg1;
_arg1 = data.readInt();
this.mediaPlayInfo(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_mediaPlayError:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _arg1;
_arg1 = data.readInt();
this.mediaPlayError(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_videoSizeChanged:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _arg1;
_arg1 = data.readInt();
this.videoSizeChanged(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_mediaPrepared:
{
data.enforceInterface(DESCRIPTOR);
this.mediaPrepared();
reply.writeNoException();
return true;
}
case TRANSACTION_mediaCompleted:
{
data.enforceInterface(DESCRIPTOR);
this.mediaCompleted();
reply.writeNoException();
return true;
}
case TRANSACTION_mediaStop:
{
data.enforceInterface(DESCRIPTOR);
this.mediaStop();
reply.writeNoException();
return true;
}
case TRANSACTION_mediaSeekCompleted:
{
data.enforceInterface(DESCRIPTOR);
this.mediaSeekCompleted();
reply.writeNoException();
return true;
}
case TRANSACTION_mediaRelease:
{
data.enforceInterface(DESCRIPTOR);
this.mediaRelease();
reply.writeNoException();
return true;
}
case TRANSACTION_playExitByUser:
{
data.enforceInterface(DESCRIPTOR);
this.playExitByUser();
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.android.localmmservice.ILocalMediaStatusListener
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public void mediaPlayInfo(int what, int extra) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(what);
_data.writeInt(extra);
mRemote.transact(Stub.TRANSACTION_mediaPlayInfo, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void mediaPlayError(int what, int extra) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(what);
_data.writeInt(extra);
mRemote.transact(Stub.TRANSACTION_mediaPlayError, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void videoSizeChanged(int width, int height) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(width);
_data.writeInt(height);
mRemote.transact(Stub.TRANSACTION_videoSizeChanged, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void mediaPrepared() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_mediaPrepared, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void mediaCompleted() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_mediaCompleted, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void mediaStop() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_mediaStop, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void mediaSeekCompleted() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_mediaSeekCompleted, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void mediaRelease() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_mediaRelease, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void playExitByUser() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_playExitByUser, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_mediaPlayInfo = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_mediaPlayError = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_videoSizeChanged = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_mediaPrepared = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_mediaCompleted = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_mediaStop = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_mediaSeekCompleted = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
static final int TRANSACTION_mediaRelease = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
static final int TRANSACTION_playExitByUser = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
}
public void mediaPlayInfo(int what, int extra) throws android.os.RemoteException;
public void mediaPlayError(int what, int extra) throws android.os.RemoteException;
public void videoSizeChanged(int width, int height) throws android.os.RemoteException;
public void mediaPrepared() throws android.os.RemoteException;
public void mediaCompleted() throws android.os.RemoteException;
public void mediaStop() throws android.os.RemoteException;
public void mediaSeekCompleted() throws android.os.RemoteException;
public void mediaRelease() throws android.os.RemoteException;
public void playExitByUser() throws android.os.RemoteException;
}
