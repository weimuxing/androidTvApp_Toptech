/*___Generated_by_IDEA___*/

/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: Z:\\348_server\\msd348_AN6.0\\device\\mstar\\common\\apps\\MLocalMM2_M029\\src\\com\\jrm\\localmm\\ui\\music\\IMusicStatusListener.aidl
 */
package com.jrm.localmm.ui.music;
public interface IMusicStatusListener extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.jrm.localmm.ui.music.IMusicStatusListener
{
private static final java.lang.String DESCRIPTOR = "com.jrm.localmm.ui.music.IMusicStatusListener";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.jrm.localmm.ui.music.IMusicStatusListener interface,
 * generating a proxy if needed.
 */
public static com.jrm.localmm.ui.music.IMusicStatusListener asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.jrm.localmm.ui.music.IMusicStatusListener))) {
return ((com.jrm.localmm.ui.music.IMusicStatusListener)iin);
}
return new com.jrm.localmm.ui.music.IMusicStatusListener.Stub.Proxy(obj);
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
case TRANSACTION_musicPlayException:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.musicPlayException(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_musicPlayErrorWithMsg:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
boolean _result = this.musicPlayErrorWithMsg(_arg0);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_handleSongSpectrum:
{
data.enforceInterface(DESCRIPTOR);
this.handleSongSpectrum();
reply.writeNoException();
return true;
}
case TRANSACTION_handleMessageInfo:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.handleMessageInfo(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_musicPrepared:
{
data.enforceInterface(DESCRIPTOR);
this.musicPrepared();
reply.writeNoException();
return true;
}
case TRANSACTION_musicCompleted:
{
data.enforceInterface(DESCRIPTOR);
this.musicCompleted();
reply.writeNoException();
return true;
}
case TRANSACTION_musicSeekCompleted:
{
data.enforceInterface(DESCRIPTOR);
this.musicSeekCompleted();
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.jrm.localmm.ui.music.IMusicStatusListener
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
/**
      *  when music play Exception,callback 
      */
@Override public void musicPlayException(java.lang.String errMessage) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(errMessage);
mRemote.transact(Stub.TRANSACTION_musicPlayException, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
/**
      *  when music play error,OnError()澶勭悊,callback
      *  @param  errMessage
      */
@Override public boolean musicPlayErrorWithMsg(java.lang.String errMessage) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(errMessage);
mRemote.transact(Stub.TRANSACTION_musicPlayErrorWithMsg, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
      * handle song spectrum  (澶勭悊闊充箰棰戣氨)
      */
@Override public void handleSongSpectrum() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_handleSongSpectrum, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
/**
       *  when handle music info,callback
       */
@Override public void handleMessageInfo(java.lang.String strMessage) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(strMessage);
mRemote.transact(Stub.TRANSACTION_handleMessageInfo, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
/**
       *  when music prepares,callback
       */
@Override public void musicPrepared() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_musicPrepared, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
/**
       *  when music completes,callback
       */
@Override public void musicCompleted() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_musicCompleted, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
/**
       * when music seeks completed,callback
       */
@Override public void musicSeekCompleted() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_musicSeekCompleted, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_musicPlayException = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_musicPlayErrorWithMsg = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_handleSongSpectrum = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_handleMessageInfo = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_musicPrepared = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_musicCompleted = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_musicSeekCompleted = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
}
/**
      *  when music play Exception,callback 
      */
public void musicPlayException(java.lang.String errMessage) throws android.os.RemoteException;
/**
      *  when music play error,OnError()澶勭悊,callback
      *  @param  errMessage
      */
public boolean musicPlayErrorWithMsg(java.lang.String errMessage) throws android.os.RemoteException;
/**
      * handle song spectrum  (澶勭悊闊充箰棰戣氨)
      */
public void handleSongSpectrum() throws android.os.RemoteException;
/**
       *  when handle music info,callback
       */
public void handleMessageInfo(java.lang.String strMessage) throws android.os.RemoteException;
/**
       *  when music prepares,callback
       */
public void musicPrepared() throws android.os.RemoteException;
/**
       *  when music completes,callback
       */
public void musicCompleted() throws android.os.RemoteException;
/**
       * when music seeks completed,callback
       */
public void musicSeekCompleted() throws android.os.RemoteException;
}
