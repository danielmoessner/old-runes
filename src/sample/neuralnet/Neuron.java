package sample.neuralnet;

import java.util.ArrayList;

public class Neuron {

    double m_outputVal;
    double m_gradient;
    int m_myIndex;
    static double eta;
    static double alpha;
    ArrayList<Connection> m_outputWeights = new ArrayList<>();


    public Neuron(int numOutputs, int myIndex) {
        for (int c = 0; c < numOutputs; c++) {
            m_outputWeights.add(new Connection());
            m_outputWeights.get(c).weight = randomWeight();
        }
        m_myIndex = myIndex;

        eta = 0.15; //overall net learning rate [0.0..1.0]
        alpha = 0.5; //momentum, multiplier of last deltaWeight, [0.0..n]
    }

    static double randomWeight() {
        return Math.random();
    }

    void feedForward(Layer prevLayer) {
        double sum = 0.0;

        //Sum the previous layer's outputs(which are our inputs)
        //Include the bias node from the previous layer

        for (int n = 0; n < prevLayer.getNeurons().size(); n++) {
            sum += prevLayer.getNeurons().get(n).m_outputVal
                    * prevLayer.getNeurons().get(n).m_outputWeights.get(m_myIndex).weight;
        }

        m_outputVal = Neuron.transferFunction(sum);
    }

    static double transferFunction(double x) {
        return Math.tanh(x);
    }

    static double transferFunctionDerivative(double x) {
        return 1.0 - x * x;
    }

    void calcOutputGradients(double targetVal) {
        double delta = targetVal - m_outputVal;
        m_gradient = delta * Neuron.transferFunctionDerivative(m_outputVal);
    }

    void calcHiddenGradients(Layer nextLayer) {
        double dow = sumDOW(nextLayer);
        m_gradient = dow * Neuron.transferFunctionDerivative(m_outputVal);
    }

    double sumDOW(Layer nextLayer) {
        double sum = 0.0;

        for (int n = 0; n < nextLayer.getNeurons().size() - 1; n++) {
            sum += m_outputWeights.get(n).weight * nextLayer.getNeurons().get(n).m_gradient;
        }

        return sum;
    }

    void updateInputWeights(Layer prevLayer) {
        // The weights to be updated are in the connection container
        // in the neurons in the preceding layer

        for (int n = 0; n < prevLayer.getNeurons().size(); n++) {
            Neuron neuron = prevLayer.getNeurons().get(n);
            double oldDeltaWeight = neuron.m_outputWeights.get(m_myIndex).deltaWeight;

            double newDeltaWeight = eta
                    * neuron.getOutputVal()
                    * m_gradient
                    //also add momentum = a fraction of the previous delta weight
                    + alpha
                    * oldDeltaWeight;
            neuron.m_outputWeights.get(m_myIndex).deltaWeight = newDeltaWeight;
            neuron.m_outputWeights.get(m_myIndex).weight += newDeltaWeight;
        }
    }

    double getOutputVal() {
        return m_outputVal;
    }

    void setOutputVal(double val) {
        m_outputVal = val;
    }
}
