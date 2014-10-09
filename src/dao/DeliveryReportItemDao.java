package dao;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.DeliveryReportItem;
import model.ReplenishmentReportItem;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import utils.DaoUtil;

public class DeliveryReportItemDao implements Serializable{
	private static final long serialVersionUID = 5074810115902407331L;
	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert simpleJdbcInsert;
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	public DeliveryReportItemDao(){
		jdbcTemplate = new JdbcTemplate(DaoUtil.getDataSource());
		simpleJdbcInsert = new SimpleJdbcInsert(DaoUtil.getDataSource())
		.withTableName("DELIVERY_REPORT_ITEM");
		namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(DaoUtil.getDataSource());
	}

	public void insertItems(List<DeliveryReportItem> list){
		for(DeliveryReportItem item : list){
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("REPORT_ID", item.getReportId());
			parameters.put("PRODUCT_ID", item.getProductId());
			parameters.put("DELIVERY_NUMBER", item.getDeliveryNumber());
			simpleJdbcInsert.execute(parameters);
		}
	}

	public List<DeliveryReportItem> queryDeliveryReportItemsByReportId(String reportId){
		String sql = "select * from DELIVERY_REPORT_ITEM where REPORT_ID = :reportId";
		SqlParameterSource namedParameters = new MapSqlParameterSource("reportId", reportId);
		return namedParameterJdbcTemplate.query(sql, namedParameters, new DeliveryReportItemMapper());
	}

	public int deleteAll(){
		String sql = "delete from DELIVERY_REPORT_ITEM";
		return jdbcTemplate.update(sql);
	}
	private static final class DeliveryReportItemMapper implements RowMapper<DeliveryReportItem> {
	    public DeliveryReportItem mapRow(ResultSet rs, int rowNum) throws SQLException {
	    	DeliveryReportItem deliveryReportItem = new DeliveryReportItem();
	    	deliveryReportItem.setDeliveryNumber(rs.getInt("DELIVERY_NUMBER"));
	    	deliveryReportItem.setProductId(rs.getString("PRODUCT_ID"));
	    	deliveryReportItem.setReportId(rs.getString("REPORT_ID"));
	        return deliveryReportItem;
	    }
	}
}
