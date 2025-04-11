/* filename: RELUNeuron.java
 * author: Leisha Figuerres
 * Class that represents a single RELU Neuron that trains and calculates output using the activation function
 * Implements reading and writing weights and bias to Data output/input streams
 * Tweaks weights and bias based on scores in TestNeuron
*/
import java.util.Random;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

public class RELUNeuron {
	double[] weights; 
	double bias;
	int inputAmount; // Parameter when constructor is called when object is created
	double output; // Will be calculated in output method
	double[] winnerSet; // The "best" weights and bias set read from weights.dbl after write method is called
	double winnerBias; // Apart of the ^ "best" set
	double expectedOut; //Expected output - the last value read from input training files 
	double learningRate; // Set to 0.1 in constructor 
	double error; // Calculated in calculateError()
	Random rand = new Random();
	public RELUNeuron(int inputAmount){
		this.inputAmount = inputAmount; 
		this.output = output; // Placeholders before settings values in methods
		this.error = error;
		this.learningRate = 0.1;
		this.expectedOut = expectedOut;	
		this.winnerSet = new double[501];
		this.weights = new double[inputAmount];
		this.bias = rand.nextDouble(1.0 - -1.0 + 1) + -1.0; // Randomized weights and biases set between -
		for(int i = 0; i < inputAmount; i++) {
			double numWeight = rand.nextDouble(1.0 - -1.0 + 1) + -1.0;
			weights[i] = numWeight;
		}
	}
	
	public double activation(double x) { // activation function if things are going wrong, change currents
		x /= 20.0;
		return x > 0 ? x : 0.0;
	}
	public double calculateError() { //calculates the error based on formula below
		error = Math.abs(expectedOut - output);
		return error;
	}
	public double output(double[] givenInputs) { //calculates output using RELU
		double sum = 0;
		for(int i = 0; i < givenInputs.length - 1; i++) {
			sum += givenInputs[i] * weights[i];
			
		}
		expectedOut = givenInputs[givenInputs.length - 1]; // reads the last value in training file and sets to expectedOutput which is used in the error calculation function
		sum += bias;
		output = activation(sum);
		return output;
		
	}
	
	public void write() { //writes neurons weight and bias to DataOutputStream. is only called when there's a new best score found in TestNeuron
		try {
			FileOutputStream fileWinnerStream = new FileOutputStream("weights.dbl"); // creates weights.dbl file and overwrites based on if there's a new best set in TestNeuron.java. I don't have the exact link but I believe I saw creating a fileoutput stream/file inputstream before creating dataoutput stream etc on GeeksforGeeks
			DataOutputStream winnerStream = new DataOutputStream(fileWinnerStream);
//			int count = 0; ** // Use to check if weights and bias read are the same that was written
			for(double d : weights) { //for every double in the new best weight set write the 500 weights and 1 bias to output stream 
//				System.out.println(count + "VAL WRITTEN ) " + d); ** // Use to check if weights and bias read are the same that was written
//				count++; ** // Use to check if weights and bias read are the same that was written
				winnerStream.writeDouble(d);
			}
			winnerStream.writeDouble(bias); // write the bias at the end
		}catch (IOException e){
			System.err.println("ERROR" + e);
		}
	}
	
	public void read(DataInputStream inStream) { //reads the neurons weights and bias from a DataInputStream using premade readDouble() method. just tests out write by reading one - the last best data set called in TestNeuron
		try {
			DataInputStream inputStream = inStream; 
			for(int i = 0; i < 501; i++) { 
				double val = inputStream.readDouble(); //  creates a new winner set
				winnerSet[i] = val;
//				System.out.println(i + "VALUE READ) " + val); // Use to check if weights and bias read are the same that was written
			
			}
			for(int i = 0; i < 500; i++) {
				weights[i] = winnerSet[i]; // sets the current weights to winner set
			}
			bias = winnerSet[500]; // sets the bias to winner set
			
		
		}catch(IOException e) {
			System.err.println("ERROR " + e);
		}catch(IndexOutOfBoundsException e){
			System.err.println("index out of bounds " + e);
		}
	}

	public void tweak() { // called when the score isn't better than previous score so it changes the weights and bias 
		Random randm = new Random();
		if(output < expectedOut) {
			for(int i = 0; i < 500; i++) { // random formula I created and tweaks the weights and bias through random numbers * a learning rate
				double randNum = randm.nextDouble() * learningRate; // 0.1 is the learning rate
				weights[i] += randNum;	
				weights[i] = Math.abs(weights[i]); // takes the absolute value so the weights aren't negative
			}
			double randNum = randm.nextDouble()* learningRate;
			bias += randNum;
		}
		else {
			for(int i = 0; i < 500; i++) { //repeats here but subtracts instead of adding
				double randNum = randm.nextDouble()* learningRate;
				weights[i] -= randNum;	
				weights[i] = Math.abs(weights[i]);
			}
			double randNum = randm.nextDouble() * learningRate;
			bias -= randNum;
			bias = Math.abs(bias);
			
		}
		
			
	}
				
}

 