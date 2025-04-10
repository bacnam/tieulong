package core.network.proto;

import java.util.ArrayList;
import java.util.List;

public class TaskActive
{
int id;
int value;
public List<Integer> stepStatus = new ArrayList<>();

public int getId() {
return this.id;
}

public void setId(int id) {
this.id = id;
}

public int getValue() {
return this.value;
}

public void setValue(int value) {
this.value = value;
}

public List<Integer> getStepStatus() {
return this.stepStatus;
}

public void setStepStatus(List<Integer> stepStatus) {
this.stepStatus = stepStatus;
}
}

