

package com.google.protobuf;

import java.io.InputStream;

public interface Parser<MessageType> {

  public MessageType parseFrom(CodedInputStream input)
      throws InvalidProtocolBufferException;

  public MessageType parseFrom(CodedInputStream input,
                               ExtensionRegistryLite extensionRegistry)
      throws InvalidProtocolBufferException;

  public MessageType parsePartialFrom(CodedInputStream input)
      throws InvalidProtocolBufferException;

  public MessageType parsePartialFrom(CodedInputStream input,
                                      ExtensionRegistryLite extensionRegistry)
      throws InvalidProtocolBufferException;

  public MessageType parseFrom(ByteString data)
      throws InvalidProtocolBufferException;

  public MessageType parseFrom(ByteString data,
                               ExtensionRegistryLite extensionRegistry)
      throws InvalidProtocolBufferException;

  public MessageType parsePartialFrom(ByteString data)
      throws InvalidProtocolBufferException;

  public MessageType parsePartialFrom(ByteString data,
                                      ExtensionRegistryLite extensionRegistry)
      throws InvalidProtocolBufferException;

  public MessageType parseFrom(byte[] data, int off, int len)
      throws InvalidProtocolBufferException;

  public MessageType parseFrom(byte[] data, int off, int len,
                               ExtensionRegistryLite extensionRegistry)
      throws InvalidProtocolBufferException;

  public MessageType parseFrom(byte[] data)
      throws InvalidProtocolBufferException;

  public MessageType parseFrom(byte[] data,
                               ExtensionRegistryLite extensionRegistry)
      throws InvalidProtocolBufferException;

  public MessageType parsePartialFrom(byte[] data, int off, int len)
      throws InvalidProtocolBufferException;

  public MessageType parsePartialFrom(byte[] data, int off, int len,
                                      ExtensionRegistryLite extensionRegistry)
      throws InvalidProtocolBufferException;

  public MessageType parsePartialFrom(byte[] data)
      throws InvalidProtocolBufferException;

  public MessageType parsePartialFrom(byte[] data,
                                      ExtensionRegistryLite extensionRegistry)
      throws InvalidProtocolBufferException;

  public MessageType parseFrom(InputStream input)
      throws InvalidProtocolBufferException;

  public MessageType parseFrom(InputStream input,
                               ExtensionRegistryLite extensionRegistry)
      throws InvalidProtocolBufferException;

  public MessageType parsePartialFrom(InputStream input)
      throws InvalidProtocolBufferException;

  public MessageType parsePartialFrom(InputStream input,
                                      ExtensionRegistryLite extensionRegistry)
      throws InvalidProtocolBufferException;

  public MessageType parseDelimitedFrom(InputStream input)
      throws InvalidProtocolBufferException;

  public MessageType parseDelimitedFrom(InputStream input,
                                        ExtensionRegistryLite extensionRegistry)
      throws InvalidProtocolBufferException;

  public MessageType parsePartialDelimitedFrom(InputStream input)
      throws InvalidProtocolBufferException;

  public MessageType parsePartialDelimitedFrom(
      InputStream input,
      ExtensionRegistryLite extensionRegistry)
      throws InvalidProtocolBufferException;
}
