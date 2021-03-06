import javax.swing.JFrame;

import org.math.plot.Plot2DPanel;

public class StepCounter {

	public static void main(String[] args) {

	}

	/***
	 * Counts the number of steps based on sensor data
	 * 
	 * Our first idea which counts a step only if a peak crosses a peak
	 * threshold followed by a trough which crosses a trough threshold
	 * 
	 * @param times
	 *            a 1d array with the elapsed times in milliseconds for each row
	 *            in the sensorData array
	 * @param sensorData
	 *            a 2D-array where rows represent successive sensor data
	 *            samples, and the columns represent different sensors. We
	 *            assume there are 6 columns. Columns 0-2 are data from the x,
	 *            y, and z axes of an accelerometer, and 3-5 are data from the
	 *            x,y, and z axes of a gyro
	 * @return an int representing the number of steps
	 */

	public static int countSteps(double[] times, double[][] sensorData) {

		double magnitudes[] = extractData(sensorData);

		int peakCounter = 0;

		boolean crossedPeakThreshold = false;

		double peakThreshold = .75 * (calculateStandardDeviation(magnitudes, calculateMean(magnitudes)));

		double troughThreshold = .25 * (calculateStandardDeviation(magnitudes, calculateMean(magnitudes)));

		for (int i = 1; i < magnitudes.length - 1; i++) {

			if (magnitudes[i] > magnitudes[i - 1] && magnitudes[i] > magnitudes[i + 1]
					&& magnitudes[i] > calculateMean(magnitudes) + peakThreshold) {

				crossedPeakThreshold = true;

			}

			else if (magnitudes[i] < magnitudes[i - 1] && magnitudes[i] < magnitudes[i + 1]
					&& magnitudes[i] < calculateMean(magnitudes) - troughThreshold && crossedPeakThreshold) {

				peakCounter++;

				crossedPeakThreshold = false;

			}
		}

		return peakCounter;

	}

	/***
	 * Our second idea which takes the average of the peaks and troughs
	 * 
	 * @param times
	 *            a 1d array with the elapsed times in milliseconds for each row
	 *            in the sensorData array
	 * @param sensorData
	 *            a 2D-array where rows represent successive sensor data
	 *            samples, and the columns represent different sensors. We
	 *            assume there are 6 columns. Columns 0-2 are data from the x,
	 *            y, and z axes of an accelerometer, and 3-5 are data from the
	 *            x,y, and z axes of a gyro
	 * @return an double representing the number of steps
	 */

	public static double countStepsWithAverage(double[] times, double[][] sensorData) {

		double magnitudes[] = extractData(sensorData);

		int peakCounter = 0;

		int troughCounter = 0;

		boolean crossedPeakThreshold = false;

		double peakThreshold = .75 * (calculateStandardDeviation(magnitudes, calculateMean(magnitudes)));

		double troughThreshold = .25 * (calculateStandardDeviation(magnitudes, calculateMean(magnitudes)));

		for (int i = 1; i < magnitudes.length - 1; i++) {

			if (magnitudes[i] > magnitudes[i - 1] && magnitudes[i] > magnitudes[i + 1]
					&& magnitudes[i] > calculateMean(magnitudes) + peakThreshold) {

				peakCounter++;

				crossedPeakThreshold = true;

			}

			else if (magnitudes[i] < magnitudes[i - 1] && magnitudes[i] < magnitudes[i + 1]
					&& magnitudes[i] < calculateMean(magnitudes) - troughThreshold && crossedPeakThreshold) {

				troughCounter++;

				crossedPeakThreshold = false;

			}
		}

		return Math.round((peakCounter + troughCounter) / (double) 2);

	}

	/***
	 * Counts the number of steps based on sensor data.
	 * 
	 * @param times
	 *            a 1d-array with the elapsed times in miliseconds for each row
	 *            in the sensorData array.
	 * 
	 * @param sensorData
	 *            a 2d-array where rows represent successive sensor data
	 *            samples, and the columns represent different sensors. We
	 *            assume there are 6 columns. Columns 0 - 2 are data from the x,
	 *            y, and z axes of an accelerometer, and 3-5 are data from the
	 *            x, y, and z axes of a gyro.
	 * 
	 * @return an int representing the number of steps
	 */
	public static int countStepsOriginal(double[] times, double[][] sensorData) {

		double magnitudes[] = extractData(sensorData);

		int peakCounter = 0;

		for (int i = 1; i < magnitudes.length - 1; i++) {

			if (magnitudes[i] > magnitudes[i - 1] && magnitudes[i] > magnitudes[i + 1]
					&& magnitudes[i] > calculateMean(magnitudes)
							+ .7 * (calculateStandardDeviation(magnitudes, calculateMean(magnitudes)))) {

				peakCounter++;

			}
		}

		return peakCounter;

	}

	public static double[] extractData(double[][] sensorData) {

		double[][] smallerData = new double[sensorData.length][3];

		for (int row = 0; row < sensorData.length; row++) {

			for (int col = 0; col < 2; col++) {

				smallerData[row][col] = sensorData[row][col];

			}

		}

		double[] magnitudes = calculateMagnitudesFor(smallerData);

		return magnitudes;

	}

	public static double calculateMagnitude(double x, double y, double z) {

		return Math.sqrt((x * x) + (y * y) + (z * z));

	}

	private static double[] calculateMagnitudesFor(double[][] sensorData) {

		double[] magnitudes = new double[sensorData.length];

		for (int row = 0; row < sensorData.length; row++) {

			magnitudes[row] = calculateMagnitude(sensorData[row][0], sensorData[row][1], sensorData[row][2]);

		}

		return magnitudes;

	}

	public static double calculateStandardDeviation(double[] arr, double mean) {

		double sum = 0;

		for (int i = 0; i < arr.length; i++) {

			sum += (arr[i] - mean) * (arr[i] - mean);

		}

		sum /= (double) (arr.length - 1);

		return Math.sqrt(sum);

	}

	public static double calculateMean(double[] arr) {

		double sum = 0;

		for (int i = 0; i < arr.length; i++) {

			sum += arr[i];

		}

		return sum / arr.length;

	}

	public static double[][] replaceAbsoluteTimeWithElapsedTime(double[][] sensorData) {

		double previous = sensorData[0][0];

		sensorData[0][0] = 0;

		for (int i = 1; i < sensorData.length; i++) {

			double temp = sensorData[i][0];

			sensorData[i][0] = sensorData[i][0] - previous;

			previous = temp;

			System.out.println(sensorData[i][0]);

		}

		return sensorData;

	}

}
