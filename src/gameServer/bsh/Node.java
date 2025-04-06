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


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/Node.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */