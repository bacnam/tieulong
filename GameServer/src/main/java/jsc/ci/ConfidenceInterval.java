package jsc.ci;

public interface ConfidenceInterval {
    double getConfidenceCoeff();

    void setConfidenceCoeff(double paramDouble);

    double getLowerLimit();

    double getUpperLimit();
}

