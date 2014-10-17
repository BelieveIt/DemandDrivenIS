package dao;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.DeliverySchedule;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import utils.DaoUtil;

public class DeliveryScheduleDao implements Serializable{
	private static final long serialVersionUID = -8875484044293832082L;
	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert simpleJdbcInsert;
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	public DeliveryScheduleDao(){
		jdbcTemplate = new JdbcTemplate(DaoUtil.getDataSource());
		simpleJdbcInsert = new SimpleJdbcInsert(DaoUtil.getDataSource())
		.withTableName("DELIVERY_SCHEDULE");
		namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(DaoUtil.getDataSource());
	}
	public void insertDeliverySchedule(DeliverySchedule deliverySchedule){
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("DELIVERY_TYPE", deliverySchedule.getDeliveryType());
		parameters.put("DELIVERY_MARK", deliverySchedule.getDeliveryMark());
		parameters.put("DELIVERY_HOUR_MARK", deliverySchedule.getDeliveryHour());
		simpleJdbcInsert.execute(parameters);
	}
	public DeliverySchedule queryDeliverySchedule(String deliveryType){
		String sql = "select * from DELIVERY_SCHEDULE where DELIVERY_TYPE = :deliveryType";
		SqlParameterSource namedParameters = new MapSqlParameterSource("deliveryType", deliveryType);
		return namedParameterJdbcTemplate.queryForObject(sql, namedParameters, new DeliveryScheduleMapper());
	}

	public int updateDeliverySchdule(DeliverySchedule deliverySchedule){
		String sql = "update DELIVERY_SCHEDULE set " +
				"DELIVERY_MARK = :deliveryMark, DELIVERY_HOUR_MARK = :deliveryHour " +
				"where DELIVERY_TYPE = :deliveryType";
		SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(deliverySchedule);
		return namedParameterJdbcTemplate.update(sql, sqlParameterSource);
	}
	public int deleteAll(){
		String sql = "delete from DELIVERY_SCHEDULE";
		return jdbcTemplate.update(sql);
	}
	private static final class DeliveryScheduleMapper implements RowMapper<DeliverySchedule> {
	    public DeliverySchedule mapRow(ResultSet rs, int rowNum) throws SQLException {
	    	DeliverySchedule deliverySchedule = new DeliverySchedule();
	    	deliverySchedule.setDeliveryType(rs.getString("DELIVERY_TYPE"));
	    	deliverySchedule.setDeliveryMark(rs.getInt("DELIVERY_MARK"));
	    	deliverySchedule.setDeliveryHour(rs.getInt("DELIVERY_HOUR_MARK"));
	        return deliverySchedule;
	    }
	}
}
