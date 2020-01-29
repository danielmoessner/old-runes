package sample.neuralnet;

import java.util.ArrayList;

public class Layer {

    ArrayList<Neuron> neurons = new ArrayList<Neuron>();

    void addNeuron(Neuron neuron) {
        neurons.add(neuron);
    }

    ArrayList<Neuron> getNeurons() {
        return neurons;
    }
}
