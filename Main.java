/* Mohtashim Syed */

import java.io.*;
import java.util.*;
import java.text.*;



public class Main {
	
	public static void main(String[] args) throws IOException{
		Scanner inp = new Scanner(System.in);
		
		System.out.println("Please enter regular customer filename to read: ");
		String regFile = inp.next();
		Scanner job1 = new Scanner(new File(regFile));
		
		System.out.println("Please enter preferred customer filename to read: ");
		String prefFile = inp.next();
		Scanner job2 = new Scanner(new File(prefFile));
		
		System.out.println("Please enter customer order filename to read: ");
		String orderFile = inp.next();
		Scanner job3 = new Scanner(new File(orderFile));
		
		int count1 = 0; //to count lines in regular orders file
		int count2 = 0; //to count lines in preferred orders file
		
		while(job1.hasNextLine()) {
			job1.nextLine();
			count1++;
		}
		job1.close();
		
		while(job2.hasNextLine()) {
			job2.nextLine();
			count2++;
		}
		job2.close();
		
		Customer[] regulars = readCustomerFile(regFile, count1);
		Customer[] preferreds = null;
		
		boolean exists = preferredCustomerExists(prefFile);
		
		//in case preferred already exists:
		if(exists) {
			preferreds = readPreferredFile(prefFile, count2);
			exists = true;
		}
		
		while(job3.hasNextLine()) {
		   //takes in info from orders file:
			String iD = job3.next();
			char size = job3.next().charAt(0);
			String drink = job3.next();
			double customCost = job3.nextDouble();
			int quantity = job3.nextInt();
			
			int index = -1;
			
			if(exists) { //setting index
				for(int i = 0; i < preferreds.length; i++) {
					if(iD.equals(preferreds[i].getID())) {
						index = i;
						break;
					}
				}
			}
			
			if(index != -1) { //preferred customer
				if(preferreds[index] instanceof Gold) {
					double price = getPrice(size, drink, customCost, quantity, preferreds[index]);
					preferreds[index].setSpent(preferreds[index].getSpent() + price);
					
					// check if Gold Customer gets better discount
					if (preferreds[index].getSpent() > 150)
						((Gold)preferreds[index]).setDisc(15);
					else if (preferreds[index].getSpent() > 100)
						((Gold)preferreds[index]).setDisc(10);
					else if(preferreds[index].getSpent() > 200) {
						double moneySpent = getPrice(size, drink, customCost, quantity);
						int bbucks = (int)moneySpent % 5;
						preferreds = addPlat(preferreds, preferreds[index], bbucks);
//						((Platinum)preferred[index]).setFirst() = ((Gold)preferred[index]).getFirst();
//						(Platinum)preferred[index].setLast() = ((Gold)preferred[index]).getLast();
//						((Platinum)preferred[index].setID() = ((Gold)preferred[index]).getID();
//						((Platinum)preferred[index]).setSpent() = ((Gold)preferred[index]).getSpent();
//						((Platinum)preferred[index].setBucks()) = (((Platinum)preferred[index]).getSpent()%5);
					}
				} else if(preferreds[index] instanceof Platinum) {
					double price = getPrice(size, drink, customCost, quantity, preferreds[index]);
					preferreds[index].setSpent(preferreds[index].getSpent() + price);
					
					
				}
			} else { //!preferred
				for(int i = 0; i < regulars.length; i++) {
					if(iD.equals(regulars[i].getID())) {
						index = i;
						break;
					}
				}
			}
			
			if(index != -1) { //regular
				double price = getPrice(size, drink, customCost, quantity);
				regulars[index].setSpent(regulars[index].getSpent()+ price);
				
				 if(regulars[index].getSpent() > 200) { //set to plat
					double moneySpent = getPrice(size, drink, customCost, quantity);
					int bbucks = (int)moneySpent % 5;
					preferreds = addPlat(preferreds, regulars[index], bbucks);
					//regulars = deleteCustomer(regulars, index);
				} else if(regulars[index].getSpent() > 150) { //set to gold 15
					preferreds = addGold(preferreds, regulars[index], 15);
					//regulars = deleteCustomer(regulars, index);
				} else if(regulars[index].getSpent() > 100) { //set to gold 10
					preferreds = addGold(preferreds, regulars[index], 10);
					//regulars = deleteCustomer(regulars, index);
				} else if(regulars[index].getSpent() > 50) { //set to gold 5
					preferreds = addGold(preferreds, regulars[index], 5);
					//regulars = deleteCustomer(regulars, index);
				}
			} else {
				System.out.println("Error: Customer doesn't exist.");
			}
		}
		
		writeRegular(regulars);
		if (exists)
			writePreferred(preferreds);
		
		job3.close();
		inp.close();
		
		
	}
	
	public static boolean preferredCustomerExists(String prefFile) throws FileNotFoundException{ //check preferred existence
		File thisFile = new File(prefFile);
		return thisFile.exists();
	}
	
	public static Customer[] readCustomerFile(String regFile, int count1) throws FileNotFoundException{ //reads regular file
		Scanner scnr = new Scanner(new File(regFile));
		Customer[] regularArray = new Customer[count1];
		int counter = 0;
		while(scnr.hasNextLine()) {
			String customerID = scnr.next();
			String firstName = scnr.next();
			String lastName = scnr.next();
			double amtSpent = scnr.nextDouble();
			
			Customer person = new Customer(customerID, firstName, lastName, amtSpent);
			regularArray[counter] = person;
			
			counter++;			
		}
		scnr.close();
		return regularArray;
	}
	
public static Customer[] readPreferredFile(String prefFile, int count2) throws FileNotFoundException { //reads preferred file
    Scanner scnr = new Scanner(new File(prefFile));
    Customer[] preferred = new Customer[count2];
    int counter = 0;

    while (scnr.hasNext()) {
        String customerID = scnr.next();
        String firstName = scnr.next();
        String lastName = scnr.next();
        double amountSpent = scnr.nextDouble();

        if (scnr.hasNext()) {
            String nextToken = scnr.next();
            if (nextToken.endsWith("%")) {
                // If the next token ends with a percentage sign, it's a discount
                int discount = Integer.parseInt(nextToken.replace("%", ""));
                Gold personG = new Gold(customerID, firstName, lastName, amountSpent, discount);
                preferred[counter] = personG;
            } else {
                // Otherwise, assume the next token is bonus bucks
                int bbucks = Integer.parseInt(nextToken);
                Platinum personP = new Platinum(customerID, firstName, lastName, amountSpent, bbucks);
                preferred[counter] = personP;
            }
        } else {
            // Handle the case where there is no more data on the line (no discount or bonus bucks)
            Platinum personP = new Platinum(customerID, firstName, lastName, amountSpent, 0); // Default bonus bucks
            preferred[counter] = personP;
        }
        counter++;
    }
    scnr.close();
    return preferred;
}

	
	public static void writeRegular(Customer[] reg) throws IOException{ //writes regular into customer.dat
		DecimalFormat d = new DecimalFormat("0.00");
		BufferedWriter wrt = new BufferedWriter(new FileWriter("customer.dat"));
		
		for( int i = 0; i < reg.length; i++) {
			Customer customer = reg[i];
			
			if(i != 0) {
				wrt.write("\n");
			}
			wrt.write(customer.getID() + " " + customer.getFirst() + " " + customer.getLast() + " " + d.format(customer.getSpent()));
			
			
		}
		wrt.close();
	}
	
	public static void writePreferred(Customer [] pref) throws IOException{ //writes preferred to preferred.dat
		DecimalFormat d = new DecimalFormat("0.00");
		BufferedWriter wrt = new BufferedWriter(new FileWriter("preferred.dat"));
		for(int i = 0; i < pref.length; i++) {
			Customer preferred = pref[i];
			if(i != 0) {
				wrt.write("\n");
			}
			
			wrt.write(preferred.getID() + " " + preferred.getFirst() + " " + preferred.getLast() + " " + d.format(preferred.getSpent()) + " ");
			if(preferred instanceof Gold) { //if gold
				Gold goldCust = (Gold) preferred;
				wrt.write(goldCust.getDisc() + "%");
			} else if (preferred instanceof Platinum){ //if platinum
				Platinum platinumCust = (Platinum) preferred;
				wrt.write(platinumCust.getBucks());
			}
			
			
		}
		wrt.close();
	}
	
	public static Customer[] deleteCustomer(Customer[] cust, int index) { //deletes a customer
		Customer[] newCust = new Customer[cust.length - 1];
		
		for(int i = 0; i < index; i++) {
			newCust[i] = cust[i];
		}
		for(int c = index+1; c < cust.length; c++) {
			newCust[c-1] = cust[c];
		}
		
		return newCust;
	}
	
	//creating a new array and adding the customer in the array (for no prior existing array)
	
	//adding a gold customer to an existing preferred array.
	public static Customer[] addGold(Customer[] cust, Customer dude, int discount) {
		Customer[] newGold = new Customer[cust.length+1];
		for(int i = 0; i < cust.length; i++) {
			newGold[i] = cust[i];
		}
			Customer upgradeGold = new Gold(dude.getID(), dude.getFirst(), dude.getLast(), dude.getSpent(), discount);
			newGold[newGold.length-1] = upgradeGold;
			return newGold;
	}
	
	//adding a platinum customer to an existing preferred array.
	public static Customer[] addPlat(Customer[] cust, Customer man, int bonus) {
		Customer[] newPlat = new Customer[cust.length+1];
		for(int i = 0; i < cust.length; i++) {
			newPlat[i] = cust[i];
		}
			Customer upgradePlat = new Platinum(man.getID(), man.getFirst(), man.getLast(), man.getSpent(), bonus);
			newPlat[newPlat.length-1] = upgradePlat;
			return newPlat;
	}
	
	//if there is no preferred array (gold):
	public static Customer[] addNewGold(Customer c, int disc) {
		Customer[] newArr = new Gold[1];
		newArr[0] = new Gold(c.getID(), c.getFirst(), c.getLast(), c.getSpent(), disc);
		return newArr;
	}

	//if there is no preferred array (platinum):
	public static Customer[] addNewPlat(Customer c, int bonus) {
		Customer[] newArr = new Platinum[1];
		newArr[0] = new Platinum(c.getID(), c.getFirst(), c.getLast(), c.getSpent(), bonus);
		return newArr;
	}
	
	//gets price but doesn't work sometimes
	public static double getPrice(char size, String drink, double customCost, int quantity) {
		double price = -1;
		switch(size) {
		case 'S':
			if(drink.equals("soda")) {
				price = 0.2*12;
			} else if(drink.equals("tea")) {
				price = 0.12*12;
			} else if(drink.equals("punch")){
				price = 0.15*12;
			}
			
			price += (Math.PI*(4*4.5)*customCost);
			price *= quantity;
			break;
		case 'M':
			if(drink.equals("soda")) {
				price = 0.2*20;
			} else if(drink.equals("tea")) {
				price = 0.12*20;
			} else if(drink.equals("punch")){
				price = 0.15*20;
			}
			
			price += (Math.PI*(4.5*5.75)*customCost);
			price *= quantity;
			break;
		case 'L':
			if(drink.equals("soda")) {
				price = 0.2*32;
			} else if(drink.equals("tea")) {
				price = 0.12*32;
			} else if(drink.equals("punch")){
				price = 0.15*32;
			}
			
			price += (Math.PI*(5.5*7)*customCost);
			price *= quantity;
			break;
		default:
			System.out.println("error in the size");
		}
		
		return price;
	}
	
	//gets price for preferred but kinda useless
	public static double getPrice(char size, String drink, double customCost, int quantity, Customer customer) {
	    double basePrice = 0;

	    // Calculate the base price based on the drink type and size
	    switch (size) {
	        case 'S':
	            if (drink.equals("soda")) {
	                basePrice = 0.2 * 12;
	            } else if (drink.equals("tea")) {
	                basePrice = 0.12 * 12;
	            } else if (drink.equals("punch")) {
	                basePrice = 0.15 * 12;
	            }
	            basePrice += (Math.PI * (4 * 4.5) * customCost);
	            break;
	        case 'M':
	            if (drink.equals("soda")) {
	                basePrice = 0.2 * 20;
	            } else if (drink.equals("tea")) {
	                basePrice = 0.12 * 20;
	            } else if (drink.equals("punch")) {
	                basePrice = 0.15 * 20;
	            }
	            basePrice += (Math.PI * (4.5 * 5.75) * customCost);
	            break;
	        case 'L':
	            if (drink.equals("soda")) {
	                basePrice = 0.2 * 32;
	            } else if (drink.equals("tea")) {
	                basePrice = 0.12 * 32;
	            } else if (drink.equals("punch")) {
	                basePrice = 0.15 * 32;
	            }
	            basePrice += (Math.PI * (5.5 * 7) * customCost);
	            break;
	        default:
	            System.out.println("Error in the size");
	            return -1;
	    }

	    // Apply customer discounts or bonus bucks
	    double discountedPrice = basePrice;

	    if (customer instanceof Gold) {
	        Gold goldCustomer = (Gold) customer;
	        int discount = goldCustomer.getDisc();
	        discountedPrice = basePrice - (basePrice * discount / 100.0);
	    } else if (customer instanceof Platinum) {
	        Platinum platinumCustomer = (Platinum) customer;
	        int bonusBucks = platinumCustomer.getBucks();
	        int usedBucks = Math.min(bonusBucks, (int) discountedPrice);
	        discountedPrice -= usedBucks;
	    }

	    // Calculate the total price for the given quantity
	    return discountedPrice * quantity;
	}

}
