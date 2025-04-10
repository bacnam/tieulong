package bsh;

import java.io.Serializable;

interface Node extends Serializable {
  void jjtOpen();

  void jjtClose();

  void jjtSetParent(Node paramNode);

  Node jjtGetParent();

  void jjtAddChild(Node paramNode, int paramInt);

  Node jjtGetChild(int paramInt);

  int jjtGetNumChildren();
}

