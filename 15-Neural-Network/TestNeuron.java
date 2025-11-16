/* filename: TestNeuron.java
 * author: Leisha Figuerres
 * "Driver" class with the main function that creates RELUNeurons. Includes a training function that is called 50,000 iterations along with a readInput method
*/
import java.io.DataInputStream;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.InputStream;

public class TestNeuron {
	public static void main(String[] args) throws IOException {
		RELUNeuron neuron = new RELUNeuron(500); // Creates the single neuron 
		FileInputStream fileWinnerInStream = new FileInputStream("weights.dbl"); //Creates the input stream from weights.dbl that the neuron stream reads from
		DataInputStream winnerInStream = new DataInputStream(fileWinnerInStream);
		double bestScore = 100000; // sets the best score super high so that the first scores will become the best
		double score = 0.0;
		for(int c = 0; c < 50000; c++) { //iterates 50000 times 
			System.out.println((c+1) +")");
			score = train(neuron); // TESTS train()
			if(score < bestScore) {
				System.out.println("NEW BEST");
				bestScore = score;
				neuron.write();	// if there's a new best score, then write to the data output stream. overwritten each time there's a new best. TESTS write()

			}else {
				neuron.tweak(); // if the score is not less than the previous best score, run the neuron through tweak where the weights and bias is changed 
			}
			if(bestScore != 10000) { // doesn't print out the best score being 100,000 in the beginning
				System.out.println(bestScore);
			}
		}
		neuron.read(winnerInStream); // reads the last best weights and bias written to weights.dbl. TESTS write(). I made sure what's written to weights.dbl and what's read from weights.dbl are the same through debugging print statements and manually scrolling for a long time to check if certain values at a random index were the same and they were.
		fileWinnerInStream.close();
		winnerInStream.close();
	
	} 
	public static double[] readInput(DataInputStream inStream) throws IOException { // called in train() method below this one - makes an array of the input training file values and returns that array
		int count = 0;
		double[] inputSet =  new double[501]; // makes array of 501 values corresponding to values in training files
		double val = 0.0;
			try {
				for(int i = 0; i < 501; i++){ //reads the doubles and initializes values in inputSet array
					val = inStream.readDouble();
					inputSet[count] = val;
					count++;
					}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		return inputSet;

	}
	
	
	public static double train(RELUNeuron neuron) throws IOException { // reads all 100 training files, for each file gets the input and expected out into an array and feeds it to output where the result of that is then fed to calculate the error. each error is added to score and returned in main iteration loop to measure if it is the new best score
		String dbl = "TrainingData/NNTrainData00";
		double score = 0.0;
		double[] inputSet = new double[501]; 
		
		for(int i = 0; i < 100; i++) {
			if(i < 10) {
				dbl = "TrainingData/NNTrainData00";
				dbl += "0" + Integer.toString(i); 
				dbl += ".dbl";
				FileInputStream fileInStream = new FileInputStream(dbl);
				DataInputStream inStream = new DataInputStream(fileInStream);
				inputSet = readInput(inStream); // assigns inputSet to result returned from readInput method()
				neuron.output(inputSet); // feeds neuron through output method 
				score += neuron.calculateError(); // calculates the error of the specific file and adds it to the total error (score) for all 100 training files
				fileInStream.close();
				inStream.close();	
			}else {
				dbl = "TrainingData/NNTrainData00";
				int d = i / 10; // opens the files from 10-99 and repeats logic of previous clause 
				int k = i % 10;
				dbl += Integer.toString(d);
				dbl += Integer.toString(k);
				dbl += ".dbl";
				FileInputStream fileInStream = new FileInputStream(dbl);
				DataInputStream inStream = new DataInputStream(fileInStream);				
				inputSet = readInput(inStream);
				neuron.output(inputSet); //TESTS output()
				score += neuron.calculateError();
				fileInStream.close();
				inStream.close();
			}

		}
		
		return score; // returns score to be used in conditional of for loop in main
	}

}
