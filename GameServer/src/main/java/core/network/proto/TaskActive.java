package core.network.proto;

import java.util.ArrayList;
import java.util.List;

public class TaskActive {
    public List<Integer> stepStatus = new ArrayList<>();
    int id;
    int value;

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

