package ADT;

public class Tuple<K1, K2, K3> {
    K1 work;
    K2 input;
    K3 output;

    public Tuple(K1 work, K2 input, K3 output) {
        this.work = work;
        this.input = input;
        this.output = output;
    }

    public K1 getWorkingStack() {
        return this.work;
    }

    public K2 getInputStack() {
        return this.input;
    }

    public K3 getOutputStack() {
        return this.output;
    }

    public void setWorkingStack(K1 work) {
        this.work = work;
    }

    public void setInputStack(K2 input) {
        this.input = input;
    }

    public void setOutputStack(K3 output) {
        this.output = output;
    }

    @Override
    public String toString() {
        return "Tuple { workingStack = " + work + ", inputStack = "
                + input + ", outputStack = " + output + "}\n";
    }
}
