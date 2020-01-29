package sample.neuralnet;

import javafx.scene.control.TextArea;

import java.util.ArrayList;

public class Net {

    ArrayList<Layer> m_layers = new ArrayList<Layer>();
    double m_error;
    double m_recentAverageError;
    double m_recentAverageSmoothingFactor;

    //Fill the Net with Layers and the Layers with Neurons
    public Net(ArrayList<Integer> topology, TextArea ta) {
        for (int layerNum = 0; layerNum < topology.size(); layerNum++) {

            Layer layer = new Layer();
            int numOutputs = layerNum == (topology.size() - 1) ? 0 : topology.get(layerNum + 1);

            for (int neuronNum = 0; neuronNum <= topology.get(layerNum); neuronNum++) {

                layer.addNeuron(new Neuron(numOutputs, neuronNum));
//                System.out.println("Created A Neuron!");
                ta.appendText("Created A Neuron!"+"\n");
            }

            m_layers.add(layer);

            //force the bias node's output value to 1.0. It's the last neuron created above
            m_layers.get(layerNum).getNeurons().get(m_layers.get(layerNum).getNeurons().size() - 1).setOutputVal(1.0);

        }
    }

    public void feedForward(ArrayList<Double> inputVals) {
        //Assign (latch) the input values into the input neurons
        for (int i = 0; i < m_layers.get(0).getNeurons().size()-1; i++) {
            m_layers.get(0).getNeurons().get(i).setOutputVal(inputVals.get(inputVals.size()-1-i));
        }

        //Forward propagate
        for (int layerNum = 1; layerNum < m_layers.size(); layerNum++) {
            Layer prevLayer = m_layers.get(layerNum - 1);
            for (int n = 0; n < m_layers.get(layerNum).getNeurons().size() - 1; n++) {
                m_layers.get(layerNum).getNeurons().get(n).feedForward(prevLayer);
            }
        }
    }

    public void backProp(ArrayList<Double> targetVals) {
        //Calculate overall net error (RMS of output neuron errors)
        Layer outputLayer = m_layers.get(m_layers.size() - 1);
        m_error = 0.0;

        for (int n = 0; n < outputLayer.getNeurons().size() - 1; n++) {
//            double delta = targetVals.get(n) - outputLayer.getNeurons().get(n).getOutputVal();
            double delta = targetVals.get(targetVals.size() - 1) - outputLayer.getNeurons().get(0).getOutputVal();
            m_error += delta * delta;
        }

        m_error /= outputLayer.getNeurons().size() - 1;//get average error squared
        m_error = Math.sqrt(m_error);//RMS

        //Implement a recent average measurement
        m_recentAverageError = (m_recentAverageError * m_recentAverageSmoothingFactor + m_error)
                / (m_recentAverageSmoothingFactor + 1.0);

        //Calculate output layer gradients
        for (int n = 0; n < outputLayer.getNeurons().size() - 1; n++) {
//            outputLayer.getNeurons().get(n).calcOutputGradients(targetVals.get(n));
            outputLayer.getNeurons().get(n).calcOutputGradients(targetVals.get(targetVals.size() - 1));
        }

        //Calculate gradients on hidden layers
        for (int layerNum = m_layers.size() - 2; layerNum > 0; layerNum--) {
            Layer hiddenLayer = m_layers.get(layerNum);
            Layer nextLayer = m_layers.get(layerNum + 1);

            for (int n = 0; n < hiddenLayer.getNeurons().size(); n++) {
                hiddenLayer.getNeurons().get(n).calcHiddenGradients(nextLayer);
            }
        }


        //For all layers from output to first hidden layer
        //Update connection weights
        for (int layerNum = m_layers.size() - 1; layerNum > 0; layerNum--) {

            for (int n = 0; n < m_layers.get(layerNum).getNeurons().size() - 1; n++) {
                m_layers.get(layerNum).getNeurons().get(n).updateInputWeights(m_layers.get(layerNum - 1));
            }
        }

    }

    public void getResults(ArrayList<Double> resultVals) {
        resultVals.clear();
        for (int n = 0; n < (m_layers.get(m_layers.size() - 1).getNeurons().size() - 1); n++) {
            resultVals.add(m_layers.get(m_layers.size() - 1).getNeurons().get(n).getOutputVal());
        }
    }

    public double getRecentAverageError() {
        return m_recentAverageError;
    }
}
