import java.util.ArrayList;
import java.util.Calendar;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class VanRentalSystem {
	private ArrayList<Depot> depotL;
	private ArrayList<Booking> bookingL;
	
	public VanRentalSystem() {
		depotL = new ArrayList<Depot>();
		bookingL = new ArrayList<Booking>();
	}
	
	public static void main(String[] args) {
		VanRentalSystem v = new VanRentalSystem();
		v.readFile(args);
	}
	
	public ArrayList<Depot> getDepotL() {
		return depotL;
	}

	public ArrayList<Booking> getBookingL() {
		return bookingL;
	}
	
	
	public void newDepot(String depotName, String vanName, String type) {
		ArrayList<Campervan> vans = new ArrayList<Campervan>();
		Depot newDepot = new Depot(depotName, vans);
		depotL.add(newDepot);
		addVan(depotName, vanName, type);
	}
	
	public void addVan(String depotName, String VanName, String type) {
		for(int count = 0;count < depotL.size(); count++) {
			if(depotL.get(count).getDepotName().equals(depotName)) {
				//如果存在的话，就拿出来，把车加进去
				Depot depot = depotL.get(count);
				depot.addVans(VanName, type);
				return;
			}
		}
		System.out.println("Unable to find the depot "+ depotName );
	}
	
	public void makeBooking(int id, ArrayList<String> vanReqs, Calendar start, Calendar end) {
		Booking b = new Booking(id, vanReqs, start, end);
		if(b.confirmPass(depotL)) {
			bookingL.add(b);
			System.out.print("Booking " + id+ " ");
			System.out.print(b.getVansUsed().get(0).getDepotName()+" "+b.getVansUsed().get(0).getName());
			for(int i = 1; i < b.getVansUsed().size(); i++) {
				if(b.getVansUsed().get(i).getDepotName().equals(b.getVansUsed().get(i-1).getDepotName())) {
					System.out.print(", " + b.getVansUsed().get(i).getName());
				} else {
					System.out.print("; "+b.getVansUsed().get(i).getDepotName()+" "+b.getVansUsed().get(i).getName());
				}
			}

			System.out.println("");
			return;
		}
			System.out.println("Booking rejected");
			b = null;
	}
	
	public void changeBooking(int id, ArrayList<String> vanReqs, Calendar start, Calendar end) {
		for(int i = 0; i < bookingL.size();i++) {
			if(bookingL.get(i).getId() == id) {
				Booking b = bookingL.get(i);
				if(b.changeBooking(vanReqs, start, end, depotL)) {
					System.out.print("Changing " + id+ " ");
					System.out.print(b.getVansUsed().get(0).getDepotName()+" "+b.getVansUsed().get(0).getName());
					for(int j = 1; j < b.getVansUsed().size(); j++) {
						if(b.getVansUsed().get(j).getDepotName().equals(b.getVansUsed().get(j-1).getDepotName())) {
							System.out.print(", " + b.getVansUsed().get(j).getName());
						} else {
							System.out.print("; "+b.getVansUsed().get(j).getDepotName()+" "+b.getVansUsed().get(j).getName());
						}
					}
					System.out.println();
					return;
				}else {
					break;
				}
			}
		}
		System.out.println("Change rejected");
	}
	
	public void cancelBooking(int id) {
		for(int i = 0;i < bookingL.size();i++) {
			if(bookingL.get(i).getId() == id) {
				bookingL.get(i).cancel();
				bookingL.remove(i);
				System.out.println("Cancel "+ id);
				return;
			}
		}
		System.out.println("Cancel rejected");
	}
	
	private int parseMonth(String month) {
		if (month.equals("Jan"))
			return Calendar.JANUARY;
		if (month.equals("Feb"))
			return Calendar.FEBRUARY;
		if (month.equals("Mar"))
			return Calendar.MARCH;
		if (month.equals("Apr"))
			return Calendar.APRIL;
		if (month.equals("May"))
			return Calendar.MAY;
		if (month.equals("Jun"))
			return Calendar.JUNE;
		if (month.equals("Jul"))
			return Calendar.JULY;
		if (month.equals("Aug"))
			return Calendar.AUGUST;
		if (month.equals("Sep"))
			return Calendar.SEPTEMBER;
		if (month.equals("Oct"))
			return Calendar.OCTOBER;
		if (month.equals("Nov"))
			return Calendar.NOVEMBER;
		if (month.equals("Dec"))
			return Calendar.DECEMBER;
		else
			return -1;
	}

	private String revParseMonth(int month) {
		if (month == Calendar.JANUARY)
			return "Jan";
		if (month == Calendar.FEBRUARY)
			return "Feb";
		if (month == Calendar.MARCH)
			return "Mar";
		if (month == Calendar.APRIL)
			return "Apr";
		if (month == Calendar.MAY)
			return "May";
		if (month == Calendar.JUNE)
			return "Jun";
		if (month == Calendar.JULY)
			return "Jul";
		if (month == Calendar.AUGUST)
			return "Aug";
		if (month == Calendar.SEPTEMBER)
			return "Sep";
		if (month == Calendar.OCTOBER)
			return "Oct";
		if (month == Calendar.NOVEMBER)
			return "Nov";
		if (month == Calendar.DECEMBER)
			return "Dec";
		else
			return null;
	}
	
	public void readFile(String[] args) {
		Scanner sc = null;
		try {
			sc = new Scanner(new FileReader(args[0]));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		while (sc.hasNext()) {
			String inputLine = sc.nextLine();
			String[] eachLineWord = inputLine.split(" ");
			if(eachLineWord[0].equals("Location")) {
				String depotName = eachLineWord[1];
				String vanName = eachLineWord[2];
				String type = eachLineWord[3];
				if(getDepotL().size() == 0) {
					newDepot(depotName,vanName,type);
				} else {
					for(int count = 0;count < getDepotL().size();count++) {
						if(getDepotL().get(count).getDepotName().equals(depotName)) {
							addVan(depotName,vanName,type);
						}else if (count == getDepotL().size() - 1) {
							newDepot(depotName, vanName,type);
							break;
						}
					}
				}
			}else if(eachLineWord[0].equals("Request")) {
				int i = 0;
				i++;
				int id = Integer.parseInt(eachLineWord[i]);
				i++;
				int hour1 = Integer.parseInt(eachLineWord[i]);
				i++;
				int month1 = parseMonth(eachLineWord[i]);
				i++;
				int day1 = Integer.parseInt(eachLineWord[i]);
				i++;
				int hour2 = Integer.parseInt(eachLineWord[i]);
				i++;
				int month2 = parseMonth(eachLineWord[i]);
				i++;
				int day2 = Integer.parseInt(eachLineWord[i]);
				i++;
				Calendar start = Calendar.getInstance();
				Calendar end = Calendar.getInstance();
				start.set(Calendar.HOUR_OF_DAY, hour1);
				start.set(Calendar.MONTH,month1);
				start.set(Calendar.DAY_OF_MONTH,day1);
				end.set(Calendar.HOUR_OF_DAY, hour2);
				end.set(Calendar.MONTH,month2);
				end.set(Calendar.DAY_OF_MONTH,day2);
				ArrayList<String> reqs = new ArrayList<String>();
				while(i < eachLineWord.length) {
					reqs.add(eachLineWord[i]);
					i++;
				}
				makeBooking(id,reqs,start,end);
			}else if(eachLineWord[0].equals("Change")){
				int i = 0;
				i++;
				int id = Integer.parseInt(eachLineWord[i]);
				i++;
				int hour1 = Integer.parseInt(eachLineWord[i]);
				i++;
				int month1 = parseMonth(eachLineWord[i]);
				i++;
				int day1 = Integer.parseInt(eachLineWord[i]);
				i++;
				int hour2 = Integer.parseInt(eachLineWord[i]);
				i++;
				int month2 = parseMonth(eachLineWord[i]);
				i++;
				int day2 = Integer.parseInt(eachLineWord[i]);
				i++;
				Calendar start = Calendar.getInstance();
				Calendar end = Calendar.getInstance();
				start.set(Calendar.HOUR_OF_DAY, hour1);
				start.set(Calendar.MONTH,month1);
				start.set(Calendar.DAY_OF_MONTH,day1);
				end.set(Calendar.HOUR_OF_DAY, hour2);
				end.set(Calendar.MONTH,month2);
				end.set(Calendar.DAY_OF_MONTH,day2);
				ArrayList<String> reqs = new ArrayList<String>();
				while(i < eachLineWord.length) {
					reqs.add(eachLineWord[i]);
					i++;
				}
				changeBooking(id,reqs,start,end);
			}else if(eachLineWord[0].equals("Cancel")){
				int id = Integer.parseInt(eachLineWord[1]);
				cancelBooking(id);
			}else if(eachLineWord[0].equals("Print")) {
				String depotName = eachLineWord[1];
				printDepot(depotName);
			}
		}
			
	}
	
	
	
	
	
	
	public void printDepot(String depotName){
		int depot = 0;
		for (int count = 0; count < depotL.size(); count++) {
			if (depotL.get(count).getDepotName().equals(depotName)) {
				depot = count;
				break;
			}
		}
		Depot d = depotL.get(depot);
		for (int vCount = 0; vCount < d.getVans().size(); vCount++) {
			//System.out.print(d.getDepotName() + " ");
			Campervan v = d.getVans().get(vCount);
			if(v.getBookDates().size() == 0) break;
//			if(v.getOccup()) {
//				System.out.println(v.getName());
//				v.sortDates();
//				
//				Calendar start = v.getBookDates().get(0);
//				Calendar end = v.getBookDates().get(v.getBookDates().size()-1);
//				System.out.print(start.get(Calendar.HOUR_OF_DAY)+":00 ");
//				System.out.print(revParseMonth(start.get(Calendar.MONTH)) + " ");
//				System.out.print(start.get(Calendar.DAY_OF_MONTH) + " ");
//				System.out.print(end.get(Calendar.HOUR_OF_DAY)+":00 ");
//				System.out.print(revParseMonth(end.get(Calendar.MONTH)) + " ");
//				System.out.print(end.get(Calendar.DAY_OF_MONTH) + " ");
//				System.out.println();
//				
//			}
			v.sortDates();
			//System.out.print(d.getDepotName()+" "+v.getName() + " ");
			for (int dCount = 0; dCount + 1 < v.getBookDates().size(); dCount = dCount + 2) {
				Calendar start = v.getBookDates().get(dCount);
				Calendar end = v.getBookDates().get(dCount + 1);
				System.out.print(d.getDepotName()+" "+v.getName() + " ");
				System.out.print(start.get(Calendar.HOUR_OF_DAY)+":00 ");
				System.out.print(revParseMonth(start.get(Calendar.MONTH)) + " ");
				System.out.print(start.get(Calendar.DAY_OF_MONTH) + " ");
				System.out.print(end.get(Calendar.HOUR_OF_DAY)+":00 ");
				System.out.print(revParseMonth(end.get(Calendar.MONTH)) + " ");
				System.out.println(end.get(Calendar.DAY_OF_MONTH));
				/////
//				System.out.print(end.get(Calendar.HOUR_OF_DAY)+":00 ");
//				System.out.print(revParseMonth(end.get(Calendar.MONTH)) + " ");
//				System.out.print(end.get(Calendar.DAY_OF_MONTH) + " ");
//				System.out.print(end.get(Calendar.DAY_OF_YEAR) - start.get(Calendar.DAY_OF_YEAR) + 1);
				///
//				if (end != v.getBookDates().get(v.getBookDates().size() - 1))
//					System.out.print(" ");
				
			}
			//System.out.println();
		}
	}
}












