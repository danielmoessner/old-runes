package sample.neuralnet;

public class TrainingData {

    int input1, input2;
    public int output;

    public TrainingData(int input1, int input2) {
        if(input1 == 0){
            this.input1=-1;
        }
        else
            this.input1 = input1;
        if(input2 == 0){
            this.input2=-1;
        }
        else
            this.input2 = input2;

        if (input1 != input2)
            output = 1;
        else output = 0;
    }

}
