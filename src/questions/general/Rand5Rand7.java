package questions.general;

import java.util.Random;
import java.util.function.Function;

import abstractPackage.IInterviewQuestion;

public class Rand5Rand7 implements IInterviewQuestion {

	public static void main(String[] args) {
		new Rand5Rand7().solve(new Function<Void, Integer>() {
			@Override
			public Integer apply(Void t) {
				Random rand = new Random();
			    int randomNum = rand.nextInt(5) + 1;// rand5
			    return randomNum;
			}
		});
	}

	@Override
	public String getQuestion() {
		return "Provided with a function Rand5(). This function returns perfectly random (equal distribution) integers between 1 and 5."
				+ "Write the function Rand7(), which uses Rand5() to produce perfectly random integers between 1 and 7.";
	}

	@Override
	public String getClarification() {
		return "Here we aim to have the our function returning integers between 1 and 7 such that P(1) = P(2) = ... = P(7). (Trivial observation right?)";
	}

	@Override
	public String getExplanation() {
		return "This is my original solution (no looking on the internet or knowing any idea of answer by any means. "
				+ "First generate a function rand2 that randoms either 0 or 1. This can be generated by using the rand5 as follow: "
				+ "rand5, if result is 1 or 2 then return 0. If result is 3 or 4 then return 1 else repeat "
				+ "P(0) = 2/5 + (2/5) * (1/5) + (2/5) * (1/5)^2 + ... to inf "
				+ "--> P(0 in first try) + P(0 in second try) + P(0 in third try) + ..."
				+ "It's quite obvious that P(0) = P(1). Using the geometric sum, P(0) = P(1) = 1/2"
				+ ""
				+ "Now use rand2 to generate rand7 as follow: "
				+ "rand2 three times, concat the numbers to get a three bits number. If this three bits number is 0 then repeat, else return this number (1 - 7)"
				+ "Similar to our rand2 above, P(1) = 1/8 + (1/8) * (1/8) + (1/8) * (1/8)^2 + ... to inf"
				+ "Using geometric sum, P(1) = 1/7"
				+ "Similar proof gives P(1) = P(2) = ... = P(7) = 1/7, which is what we want";
	}

	@Override
	public void solve(Object abstractInput) {
		final Function<Void, Integer> rand5 = (Function<Void, Integer>) abstractInput;

		//Produce rand2
		final Function<Void, Integer> rand2 = new Function<Void, Integer>() {
			@Override
			public Integer apply(Void t) {
				int rand = -1;
				while (true) {
					rand = rand5.apply(null);
					if (rand == 1 || rand == 2) {
						return 0;
					} else if (rand == 3 || rand == 4) {
						return 1;
					}
				}
			}
		};

		//Now use rand2 to produce rand7
		Function<Void, Integer> rand7 = new Function<Void, Integer>() {
			@Override
			public Integer apply(Void t) {
				while (true) {
					int firstBit = rand2.apply(null);
					int secondBit = rand2.apply(null);
					int thirdBit = rand2.apply(null);

					int rand = (firstBit << 2) + (secondBit << 1) + thirdBit;
					if (rand != 0) {
						return rand;
					}
				}
			}
		};

		//Do some testing
		int[] testArray = new int[7];
		int trial = 10000000;
		for (int i = 0; i < trial; i++) {
			testArray[rand7.apply(null) - 1]++;
		}

		System.out.println("1/7 is " + (1.0/7));
		for (int i = 0; i < 7; i++) {
			System.out.println("Random value " + i + " is of fraction " + testArray[i] / (double)trial);
		}
	}

}
