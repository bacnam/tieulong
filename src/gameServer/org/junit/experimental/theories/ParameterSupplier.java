package org.junit.experimental.theories;

import java.util.List;

public abstract class ParameterSupplier {
  public abstract List<PotentialAssignment> getValueSources(ParameterSignature paramParameterSignature) throws Throwable;
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/junit/experimental/theories/ParameterSupplier.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */