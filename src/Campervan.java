import java.util.ArrayList;
import java.util.Calendar;

//compare object to sorting
public class Campervan implements Comparable<Campervan> {
	private String belongedDepot;
	private String vanName;
	private String type;
	private ArrayList<Calendar> bookTime = new ArrayList<Calendar>();
	private boolean occup;
	private int vanNumber;
	
	public Campervan(String belongedDepot, String vanName, String type, int vanNumber) {
		this.belongedDepot = belongedDepot;
		this.vanName = vanName;
		this.type = type;
		this.vanNumber = vanNumber;
		this.occup = false;
		bookTime = new ArrayList<Calendar>();
	}
	
	public int getVanNumber() {
		return vanNumber;
	}
	
	public void setVanNumber(int num) {
		this.vanNumber = num;
	}
	

	public String getType() {
		return type;
	}

	public String getName() {
		return vanName;
	}

	public String getDepotName() {
		return belongedDepot;
	}

	public ArrayList<Calendar> getBookDates() {
		return bookTime;
	}
	
	/*
	 * addBookTime adds the required book time to a van
	 * arrayList come with pair start,end,start,end 
	 */
	public void addBookedtime(Calendar start, Calendar end) {
		/* 1. if the array is empty
		 * 2. compare the new start and the first start, is smaller then add in front
		 * 3. compare the new start and the last end, if new start larger than the last end
		 *    straight way add at the back of list (compare day of year)
		 * 4. add in the middle by using of loop
		 */
		//1.
		if(bookTime.size() == 0) {
			bookTime.add(start);
			bookTime.add(end);
			return;
		}
		//2.
		Calendar start2 = bookTime.get(0);//take the first time and compare
		if(start.before(start2)) {
			bookTime.add(0, start);
			bookTime.add(1,end);	
			return;
		}
		//3. compare the new one and last one, if number of days large, then add
		// if same days compare hour, if more hours, then add
		Calendar end2 = bookTime.get(bookTime.size() - 1);
		if(start.get(Calendar.DAY_OF_YEAR) > end2.get(Calendar.DAY_OF_YEAR)) {
			bookTime.add(start);
			bookTime.add(end);
			return;
		} else if(start.get(Calendar.DAY_OF_YEAR) == end2.get(Calendar.DAY_OF_YEAR)) {
			if(start.get(Calendar.HOUR_OF_DAY) > end2.get(Calendar.HOUR_OF_DAY)) {
				bookTime.add(start);
				bookTime.add(end);
				return;
			}
		}
		//4.use loop check every end time and the next time of start, since new is not the largest one
		//set count = 0, then end = count+1, and next start = count+2
		//if new start > current_end and new end < next_start, then add current_end + 1
		//since current_end = count+1, so add on count+2, end add on count +3
		//for comparing,compare days first, if days equal then compare hours
		for(int count = 0; count < bookTime.size() - 2; count = count + 2) {
			Calendar currentEnd = bookTime.get(count+1);
			Calendar nextStart = bookTime.get(count+2);
			if(start.get(Calendar.DAY_OF_YEAR) > currentEnd.get(Calendar.DAY_OF_YEAR)
					&& end.get(Calendar.DAY_OF_YEAR) < nextStart.get(Calendar.DAY_OF_YEAR)) {
				bookTime.add(count+2,start);
				bookTime.add(count+3,end);
				return;
			}else if(start.get(Calendar.DAY_OF_YEAR) == currentEnd.get(Calendar.DAY_OF_YEAR)
					&& end.get(Calendar.DAY_OF_YEAR) == nextStart.get(Calendar.DAY_OF_YEAR)) {
				if(start.get(Calendar.HOUR_OF_DAY) > currentEnd.get(Calendar.HOUR_OF_DAY)
					&& end.get(Calendar.HOUR_OF_DAY) < nextStart.get(Calendar.HOUR_OF_DAY)) {
					bookTime.add(count+2,start);
					bookTime.add(count+3,end);
					return;
				}
			}
		}
		

	}
	
	public void sortDates() {
		for (int count1 = 0; count1 < bookTime.size(); count1 = count1 + 2) {
			for (int count2 = 2; count2 < bookTime.size() - count1; count2 = count2 + 2) {
				if (bookTime.get(count2).before(bookTime.get(count2 - 2))) {
					Calendar temp = bookTime.get(count2);
					Calendar temp2 = bookTime.get(count2 + 1);
					bookTime.set(count2, bookTime.get(count2 - 2));
					bookTime.set(count2 + 1, bookTime.get(count2 - 1));
					bookTime.set(count2 - 2, temp);
					bookTime.set(count2 - 1, temp2);
				}
			}
		}
	}

	public void setOccup(boolean occup) {
		this.occup = occup;
	}

	public boolean getOccup() {
		return this.occup;
	}
	
	@Override
	public int compareTo(Campervan compareVan) {
		//被比较的车的id 返回的是当前的减去被比较的
		int compareNum = ((Campervan) compareVan).getVanNumber();
		return this.vanNumber - compareNum;
	}
	
}
