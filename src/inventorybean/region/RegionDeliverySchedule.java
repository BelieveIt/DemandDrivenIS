package inventorybean.region;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import model.DeliverySchedule;
import model.Product;

import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleModel;

import dao.DeliveryScheduleDao;

@ManagedBean(name="regionDeliverySchedule")
@ViewScoped
public class RegionDeliverySchedule implements Serializable{
	private static final long serialVersionUID = 2123162782712611885L;
	private ScheduleModel eventModel;
	private DeliveryScheduleDao deliveryScheduleDao;
	private Integer everyWeekSchedule;
	private Integer everyMonthSchedule;
	private Integer everyWeekHourSchedule;
	private Integer everyMonthHourSchedule;
	private ArrayList<Integer> daysOfMonth;
	private ArrayList<Integer> hoursOfDay;
	@PostConstruct
	public void init(){
		deliveryScheduleDao = new DeliveryScheduleDao();
        daysOfMonth = new ArrayList<Integer>();
        hoursOfDay = new ArrayList<Integer>();
        for(int i = 1; i < 28; i++){
        	daysOfMonth.add(i);
        }
        for(int i = 0; i < 23; i++){
        	hoursOfDay.add(i);
        }
        initProcess();
	}

	public void initProcess(){
		eventModel = new DefaultScheduleModel();
		DeliverySchedule weekSchedule = new DeliverySchedule();
		weekSchedule = deliveryScheduleDao.queryDeliverySchedule(Product.EVERYWEEK);
		DeliverySchedule monthSchedule = new DeliverySchedule();
		monthSchedule = deliveryScheduleDao.queryDeliverySchedule(Product.EVERYMONTH);
        initialEvent(weekSchedule, monthSchedule);
	}

	private void initialEvent(DeliverySchedule weekSchedule, DeliverySchedule monthSchedule){
		try {
		Date today = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(today);
		String todayYear = Integer.toString(calendar.get(Calendar.YEAR));
		String todayMonth = Integer.toString(calendar.get(Calendar.MONTH)+1);
		String todayDay = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
		String todayYearNext = Integer.toString(calendar.get(Calendar.YEAR)+1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String startInString = todayYear + "-"+todayMonth+"-"+todayDay + " 5:0:0";
		String endInString = todayYearNext + "-01-01 00:00:00";

		Date start = sdf.parse(startInString);
		Date end = sdf.parse(endInString);

		Calendar startC = Calendar.getInstance();
		startC.setTime(start);

		Calendar endC = Calendar.getInstance();
		endC.setTime(end);

		for (; startC.before(endC); startC.add(Calendar.DATE, 1)) {
			if(startC.get(Calendar.DAY_OF_MONTH) == monthSchedule.getDeliveryMark().intValue()){
				startC.set(Calendar.HOUR_OF_DAY, monthSchedule.getDeliveryHour());
				DefaultScheduleEvent event1 = new DefaultScheduleEvent("Delivery for EveryMonth Frequency", startC.getTime(),  startC.getTime());
				event1.setStyleClass("monthEvent");
				eventModel.addEvent(event1);
			}

			if(startC.get(Calendar.DAY_OF_WEEK) == weekSchedule.getDeliveryMark().intValue()){
				startC.set(Calendar.HOUR_OF_DAY, weekSchedule.getDeliveryHour());
				DefaultScheduleEvent event2 = new DefaultScheduleEvent("Delivery for EveryWeek Frequency",  startC.getTime(),  startC.getTime());
				event2.setStyleClass("weekEvent");
				eventModel.addEvent(event2);
			}

		}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public void openChangeSchedule(){
		DeliverySchedule weekSchedule = new DeliverySchedule();
		weekSchedule = deliveryScheduleDao.queryDeliverySchedule(Product.EVERYWEEK);
		DeliverySchedule monthSchedule = new DeliverySchedule();
		monthSchedule = deliveryScheduleDao.queryDeliverySchedule(Product.EVERYMONTH);
		everyMonthSchedule = monthSchedule.getDeliveryMark();
		everyWeekSchedule = weekSchedule.getDeliveryMark();
		everyMonthHourSchedule = monthSchedule.getDeliveryHour();
		everyWeekHourSchedule = weekSchedule.getDeliveryHour();
		RequestContext.getCurrentInstance().execute("PF('changeSchedule').show();");
	}

	public void changeSchedule(){
		DeliverySchedule weekSchedule = new DeliverySchedule();
		weekSchedule.setDeliveryHour(everyWeekHourSchedule);
		weekSchedule.setDeliveryMark(everyWeekSchedule);
		weekSchedule.setDeliveryType(Product.EVERYWEEK);
		DeliverySchedule monthSchedule = new DeliverySchedule();
		monthSchedule.setDeliveryHour(everyMonthHourSchedule);
		monthSchedule.setDeliveryMark(everyMonthSchedule);
		monthSchedule.setDeliveryType(Product.EVERYMONTH);

		deliveryScheduleDao.updateDeliverySchdule(weekSchedule);
		deliveryScheduleDao.updateDeliverySchdule(monthSchedule);

		initProcess();
		RequestContext.getCurrentInstance().execute("PF('changeSchedule').hide();");
	}

	public ScheduleModel getEventModel() {
			return eventModel;
	}
	public void setEventModel(ScheduleModel eventModel) {
		this.eventModel = eventModel;
	}

	public DeliveryScheduleDao getDeliveryScheduleDao() {
		return deliveryScheduleDao;
	}

	public void setDeliveryScheduleDao(DeliveryScheduleDao deliveryScheduleDao) {
		this.deliveryScheduleDao = deliveryScheduleDao;
	}

	public Integer getEveryWeekSchedule() {
		return everyWeekSchedule;
	}

	public void setEveryWeekSchedule(Integer everyWeekSchedule) {
		this.everyWeekSchedule = everyWeekSchedule;
	}

	public Integer getEveryMonthSchedule() {
		return everyMonthSchedule;
	}

	public void setEveryMonthSchedule(Integer everyMonthSchedule) {
		this.everyMonthSchedule = everyMonthSchedule;
	}

	public ArrayList<Integer> getDaysOfMonth() {
		return daysOfMonth;
	}

	public void setDaysOfMonth(ArrayList<Integer> daysOfMonth) {
		this.daysOfMonth = daysOfMonth;
	}

	public ArrayList<Integer> getHoursOfDay() {
		return hoursOfDay;
	}

	public void setHoursOfDay(ArrayList<Integer> hoursOfDay) {
		this.hoursOfDay = hoursOfDay;
	}

	public Integer getEveryWeekHourSchedule() {
		return everyWeekHourSchedule;
	}

	public void setEveryWeekHourSchedule(Integer everyWeekHourSchedule) {
		this.everyWeekHourSchedule = everyWeekHourSchedule;
	}

	public Integer getEveryMonthHourSchedule() {
		return everyMonthHourSchedule;
	}

	public void setEveryMonthHourSchedule(Integer everyMonthHourSchedule) {
		this.everyMonthHourSchedule = everyMonthHourSchedule;
	}
}
