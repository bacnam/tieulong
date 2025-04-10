

package com.google.protobuf;

import com.google.protobuf.Descriptors.EnumDescriptor;
import com.google.protobuf.Descriptors.EnumValueDescriptor;

public interface ProtocolMessageEnum extends Internal.EnumLite {

  int getNumber();

  EnumValueDescriptor getValueDescriptor();

  EnumDescriptor getDescriptorForType();
}
