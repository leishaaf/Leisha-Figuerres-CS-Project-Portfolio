//My formula is in my time calculator method. I couldn't find a formula online that does what I wanted to do so I made my own.
// Credits taking = ((fallCreds + winterCreds + springCreds + summerCreds) * years)/16 Calculates how many semesters they're by dividing total of credits every year * the expected amount of years til graduation by 16 ( average amount of credits in semester)
// Credits needed depends on their degree and input. And it will be the credits their program/degree requires - amount of credits they've already earned
// If the credits theyre taking is less than the amount they need then the difference of credits is calculated.
// I also calculate how many months and years they have worth of credits. 

import java.util.Scanner;
import java.util.InputMismatchException;
public class GraduationCalculator{

  	public static void checkForEnd(String str) {
		if(str.equalsIgnoreCase("quit")){
			System.exit(0);
		}
	}
public static int[] calculator1(String degree, String classLev, Scanner scanner) {
	int years = 0;
	int creditsNeeded = 0;
	int creditsRequired = 0;
	int creditsEarned = 0;
	int[] result = {0,0,0,0};
	try{
		System.out.println("How many credits have you currently earned - including transfer credits - ? Please enter a number.");
		String credStr = scanner.next();
		checkForEnd(credStr);
		creditsEarned = Integer.valueOf(credStr);
		while(creditsEarned < 0 || creditsEarned > 128){
			System.out.println("Please enter a valid number of credits.");
			credStr = scanner.next();
			checkForEnd(credStr);
			creditsEarned = Integer.valueOf(credStr);
		}
		if(degree.equals("B")){
			creditsRequired = 128;
			System.out.println("You are trying to get a Bachelors Degree! USFCA Bachelors degrees requires 128 credits.");
			creditsNeeded = creditsRequired - creditsEarned;
			if(classLev.equals("Freshmen")){
				years = 2027 - 2024;
			}else if(classLev.equals("Sophomore")){
				years = 2026 - 2024;
			}else if(classLev.equals("Junior")){
				years = 2025 - 2024;
			}else if(classLev.equals("Senior")){
				years = 0;
			}			
		}else if(degree.equals("M")){
			System.out.println("You are trying to get a Masters Degree! USFCA Masters degrees takes between 30-60 Credits based on your program.\nPlease enter how many credits your program needs for you degree.");
			credStr = scanner.next();
			checkForEnd(credStr);
			creditsRequired = Integer.valueOf(credStr);
			while(creditsRequired < 30 || creditsRequired > 60){
			System.out.println("Masters Degrees of USFCA takes between 30-60 Credits based on your program. Please enter a valid number of credits.");
			credStr = scanner.next();
			checkForEnd(credStr);
			creditsRequired = Integer.valueOf(credStr);
			}
			creditsNeeded = creditsRequired - creditsEarned;

			System.out.println("Please enter your target year for graduation from 2024-2026:");
			String yearStr = scanner.next();
			checkForEnd(yearStr);
			years = Integer.valueOf(yearStr);
			while(years < 2024 || years > 2026){
				System.out.println("Masters Degrees of USFCA takes between 30-60 Credits based on your program. Please enter a valid year from 2024-2026:");
				yearStr = scanner.next();
				checkForEnd(yearStr);
				years = Integer.valueOf(yearStr);
			}
			years -= 2024;

		}else if(degree.equals("D")){
			System.out.println("You are trying to get a Doctorates Degree! USFCA Masters degrees takes between 60-120 Credits based on your program.\nPlease enter how many credits your program needs for you degree.");
			credStr = scanner.next();
			checkForEnd(credStr);
			creditsRequired = Integer.valueOf(credStr);
			while(creditsRequired < 60 || creditsRequired > 120){
			System.out.println("Doctorates Degrees of USFCA takes between 60-120 Credits based on your program. Please enter a valid number of credits.");
			credStr = scanner.next();
			checkForEnd(credStr);
			creditsRequired = Integer.valueOf(credStr);
			}
			creditsNeeded = creditsRequired - creditsEarned;

			System.out.println("Please enter your target year for graduation from 2024-2028:");
			String yearStr = scanner.next();
			checkForEnd(yearStr);
			years = Integer.valueOf(yearStr);
			while(years < 2024 || years > 2028){
				System.out.println("Doctorates Degrees of USFCA takes between 60-120 Credits. Please enter a valid year from 2024-2028:");
				yearStr = scanner.next();
				checkForEnd(yearStr);
				years = Integer.valueOf(yearStr);			
			}
				years -= 2024;	
		}
	}catch(NumberFormatException e){
		System.out.println("Invalid input, you were asked to input numbers/valid numbers. Better luck next time, feel free to try again.");
		System.exit(0);
	}catch(InputMismatchException e){
		System.exit(0);
		System.out.println("Input MisMatchException. You were asked to input numbers/valid numbers. Better luck next time, feel free to try again.");
}
result[0] = years;
result[1] = creditsNeeded;
result[2] = creditsRequired;
result[3] = creditsEarned;
return result;
}
public static int[] semesterCreditsCalculator(int years, Scanner scanner){
	int[] result = {0,0,0,0};
	int fallCreds = 0;
	int winterCreds = 0;
	int springCreds = 0;
	int summerCreds = 0;	
	try{
		System.out.println("Enter the amount of credits you will be taking for every semester for each year until graduation.\nPlease note regular semesters - Fall and Spring - require a minimum of 12 Credits from full time students.\nEnter 0 if you don't plan on taking classes that semester. ");
		System.out.println("Fall Semester: ");
		String fallCstr = scanner.next();
		checkForEnd(fallCstr);
		fallCreds = Integer.valueOf(fallCstr);
		while(fallCreds < 0 || fallCreds > 18){
			System.out.println("Please enter a valid number of credits");
			fallCstr = scanner.next();
			checkForEnd(fallCstr);
			fallCreds = Integer.valueOf(fallCstr);
		}
		System.out.println("Winter Intersession: ");
		String winterCstr = scanner.next();
		checkForEnd(winterCstr);
		winterCreds = Integer.valueOf(winterCstr);
		while(winterCreds < 0 || winterCreds > 18){
			System.out.println("Please enter a valid number of credits");
			winterCstr = scanner.next();
			checkForEnd(winterCstr);
			winterCreds = Integer.valueOf(winterCstr);
		}
		System.out.println("Spring Semester: ");
		String springCstr = scanner.next();
		checkForEnd(springCstr);
		springCreds = Integer.valueOf(springCstr);
		while(springCreds < 0 || springCreds > 18){
			System.out.println("Please enter a valid number of credits");
			springCstr = scanner.next();
			checkForEnd(springCstr);
			springCreds = Integer.valueOf(springCstr);
		}
		System.out.println("Summer Semester: ");
		String summerCstr = scanner.next();
		checkForEnd(summerCstr);
		summerCreds = Integer.valueOf(summerCstr);
		while(summerCreds < 0 || summerCreds > 18){
			System.out.println("Please enter a valid number of credits");
			summerCstr = scanner.next();
			checkForEnd(summerCstr);
			summerCreds = Integer.valueOf(summerCstr);
	}
	}catch(NumberFormatException e){
		System.out.println("Invalid input, you were asked to input numbers/valid numbers. Better luck next time, feel free to try again.");
		System.exit(0);
	}catch(InputMismatchException e){
		System.out.println("Input MisMatchException. You were asked to input numbers/valid numbers. Better luck next time, feel free to try again.");
		System.exit(0);
	}
	result[0] = fallCreds;
	result[1] = winterCreds;
	result[2] = springCreds;
	result[3] = summerCreds;
	return result;
}
public static void timeCalculator(double years, double creditsNeeded, double fallCreds, double winterCreds, double springCreds, double summerCreds) {
	double totalCreds = fallCreds + winterCreds + springCreds + summerCreds;
	double semestersNeeded = 0;
	double monthsUntil = 0; 
	double yearsUntil = 0;
	double credsxYrs = (fallCreds + winterCreds + springCreds + summerCreds) * years;
	double credDiff = (creditsNeeded - credsxYrs);

	if(years == 0){
		System.out.println("You are expected to graduate this year therefore the amount of years used in calculation is 0.");
		if(totalCreds >= creditsNeeded){
			semestersNeeded = totalCreds/16;
			monthsUntil = (semestersNeeded * 4); 
			yearsUntil = (monthsUntil/12);
			semestersNeeded = Math.abs(semestersNeeded);
			monthsUntil = Math.abs(monthsUntil);
			yearsUntil = Math.abs(yearsUntil);
			System.out.println("According to the calculations based on your input...");
			System.out.println("You are predicted to exceed the required amount of credits in by taking " + totalCreds + " credits every year for the next " + years + " years:");
			System.out.printf("You are taking " + "%.2f" +" semesters, " + "%.2f" + " months, " + "%.2f" + " and year(s) worth of credits in order to graduate which exceeds requirements. \n",semestersNeeded,monthsUntil,yearsUntil);
			System.out.println("Now you know...");
			System.exit(0);
		}else{
			semestersNeeded = totalCreds/16;
			monthsUntil = (semestersNeeded * 4); 
			yearsUntil = (monthsUntil/12);
			credDiff = (creditsNeeded - totalCreds);
			System.out.println("According to the calculations based on your input...");
			System.out.printf("You are taking " + totalCreds + " credits this year which is " + "%.2f" + " semesters, " + "%.2f" + " months, " + "%.2f" + " and year(s) worth of credits to graduate.\n",semestersNeeded,monthsUntil,yearsUntil);
			semestersNeeded = credDiff/16;
			monthsUntil = (semestersNeeded * 4); 
			yearsUntil = (monthsUntil/12);
			System.out.printf("However this is not enough to meet your credit requirements, after this amount of time, you would still need " + credDiff + " credits, which is " + "%.2f" + " semesters, " + "%.2f" + " months, " + "%.2f" + " and year(s) worth of credits to graduate.\n",semestersNeeded,monthsUntil,yearsUntil);
			System.out.println("Now you know...");
			System.exit(0);
		}	
	}
	if(credsxYrs >= creditsNeeded){
		semestersNeeded = credsxYrs/16;
		monthsUntil = (semestersNeeded * 4); 
		yearsUntil = (monthsUntil/12);
		semestersNeeded = Math.abs(semestersNeeded);
		monthsUntil = Math.abs(monthsUntil);
		yearsUntil = Math.abs(yearsUntil);
		System.out.println("According to the calculations based on your input...");
		System.out.println("You are predicted to exceed the required amount of credits in by taking " + totalCreds + " credits every year for the next " + years + " years:");
		System.out.printf("You are taking " + "%.2f" +" semesters, " + "%.2f" + " months, " + "%.2f" + " and year(s) worth of credits in order to graduate which exceeds requirements. \n",semestersNeeded,monthsUntil,yearsUntil);
		System.out.println("Now you know...");
	}else{
		semestersNeeded = credsxYrs/16;
		monthsUntil = (semestersNeeded * 4); 
		yearsUntil = (monthsUntil/12);
		credDiff = (creditsNeeded - credsxYrs);
		System.out.println("According to the calculations based on your input...");
		System.out.printf("You are taking " + totalCreds + " credits every year for the next " + years + " years:\n");
		System.out.printf("This is " + "%.2f" +" semesters, " + "%.2f" + " months, " + "%.2f" + " and year(s) worth of credits.\n",semestersNeeded,monthsUntil,yearsUntil);
		semestersNeeded = credDiff/16;
		monthsUntil = (semestersNeeded * 4); 
		yearsUntil = (monthsUntil/12);
		System.out.printf("However this is not enough to meet your credit requirements, after the " + years + " years, you would still need " + credDiff + " credits, which is " + "%.2f" + " semesters, " + "%.2f" + " months, " + "%.2f" + " and year(s) worth of credits to graduate.\n",semestersNeeded,monthsUntil,yearsUntil);
		System.out.println("Now you know...");
	}
	
}
public static void main(String[] args) {
	Scanner scanner = new Scanner(System.in);
	System.out.println("NOTE: THIS PROGRAM CALCULATES SEMESTERS/MONTHS/YEARS WORTH OF STUDYING LEFT BASED ON REGULAR SEMESTERS WHICH ARE WORTH 16 CREDITS.");
	System.out.println("Hello user, welcome to the USFCA Graduation Calculator! Quit at anytime by typing 'Quit'");
	System.out.println("Please enter your year. 1 for Freshmen, 2 for Sophomore, 3 for Junior, and 4 for Senior: ");
	String classType = "";
	String degreeType = "";
	int years = 0;
	int creditsRequired = 0;
	int creditsNeeded = 0;
	int creditsEarned = 0;
	int fallCreds = 0;
	int winterCreds = 0;
	int springCreds = 0;
	int summerCreds = 0;
	String level;
	String dLevel;
 	level = scanner.next();
 	checkForEnd(level);
 	while(!level.equals("1") && !level.equals("2")&& !level.equals("3") && !level.equals("4")){
 		System.out.println("Please enter a number from 1-4.");
 		level = scanner.next();
 		checkForEnd(level);	
 	}
		if(level.equals("1")){
			System.out.println("Hello Freshmen!");
			classType = "Freshmen";
		}
		else if(level.equals("2")){
			System.out.println("Hello Sophomore!");
			classType = "Sophomore";
		}
		else if(level.equals("3")){
			System.out.println("Hello Junior!");
			classType = "Junior";
		}
		if(level.equals("4")){
			System.out.println("Hello Senior!");
			classType = "Senior";
		}

 	System.out.println("What degree are you trying to get?");
 	System.out.println("Enter 1 f for Bachelors, 2 for Masters, and 3 for Doctorates.");
	dLevel = scanner.next();
 	checkForEnd(dLevel);
 	while(!dLevel.equals("1") && !dLevel.equals("2") && !dLevel.equals("3") && !dLevel.equals("4")){
 		System.out.println("Please enter a number from 1-3.");
 		dLevel = scanner.next();
 		checkForEnd(dLevel);
 	}
		if(dLevel.equals("1")){
			degreeType = "B";
			int[] result = calculator1(degreeType, classType, scanner);
			years = result[0];
			creditsNeeded = result[1];
			creditsRequired = result[2]; 
			creditsEarned = result[3];
			System.out.println("Based on the year you graduate, you are expected to graduate in " + years+" year(s).");
			System.out.println("Your degree requires "+ creditsRequired+ " credits. You need " + creditsNeeded +" credits to graduate.");
		}
		else if(dLevel.equals("2")){
			degreeType = "M";
			int[] result = calculator1(degreeType, classType, scanner);
			years = result[0];
			creditsNeeded = result[1];
			creditsRequired = result[2]; 
			creditsEarned = result[3];
			System.out.println("Based on the year you graduate, you are expected to graduate in " + years+" year(s).");
			System.out.println("Your degree requires "+ creditsRequired+ " credits. You need " + creditsNeeded +" credits to graduate."); 
		}
		else if(dLevel.equals("3")){
			degreeType = "D";
			int[] result = calculator1(degreeType, classType, scanner);
			years = result[0];
			creditsEarned = result[1];
			creditsRequired = result[2]; 
			creditsEarned = result[3];

			System.out.println("Based on the year you graduate, you are expected to graduate in " + years+" year(s).");
			System.out.println("Your degree requires "+ creditsRequired+ " credits. You need " + creditsNeeded +" credits to graduate.");
		}

	int[]result2 = semesterCreditsCalculator(years,scanner);
	fallCreds = result2[0];
	winterCreds = result2[1];
	springCreds = result2[2];
	summerCreds = result2[3];
	
	timeCalculator(years, creditsNeeded,fallCreds, winterCreds, springCreds, summerCreds);
    }
}
